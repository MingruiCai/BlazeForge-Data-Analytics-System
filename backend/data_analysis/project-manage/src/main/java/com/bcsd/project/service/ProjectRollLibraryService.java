package com.bcsd.project.service;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.common.annotation.DataScope;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.domain.TreeSelect;
import com.bcsd.common.core.domain.entity.SysDept;
import com.bcsd.common.core.domain.entity.SysDictData;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.DictUtils;
import com.bcsd.common.utils.SecurityUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.constants.Constants;
import com.bcsd.project.domain.ProjectRollCycle;
import com.bcsd.project.domain.ProjectRollLibrary;
import com.bcsd.project.domain.ProjectRollLibraryLog;
import com.bcsd.project.domain.vo.ProjectRollLibrarySummaryVO;
import com.bcsd.project.domain.vo.ProjectRollLibraryVO;
import com.bcsd.project.domain.vo.StatisticsQueryVO;
import com.bcsd.project.domain.vo.VerifyVO;
import com.bcsd.project.mapper.ProjectDictInfoMapper;
import com.bcsd.project.mapper.ProjectRollLibraryLogMapper;
import com.bcsd.project.mapper.ProjectRollLibraryMapper;
import com.bcsd.system.service.ISysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.bcsd.project.constants.Constants.*;

/**
 * 滚动项目库 服务实现类
 * @author liuliang
 * @since 2023-02-16
 */
@Slf4j
@Service
public class ProjectRollLibraryService extends ServiceImpl<ProjectRollLibraryMapper, ProjectRollLibrary> implements IService<ProjectRollLibrary> {

    @Autowired
    ISysDeptService deptService;
    @Autowired
    ProjectDictInfoMapper dictInfoMapper;
    @Autowired
    ProjectRollCycleService rollCycleService;
    @Autowired
    ProjectRollLibraryLogMapper logMapper;

    /**
     * 保存（申请）
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult save(ProjectRollLibrary params,Set<String> roleKeys){
        if (params.getId()!=null){
            ProjectRollLibrary prl = getById(params.getId());
            Integer state = Integer.parseInt(prl.getState());
            if (state>=0){
                return AjaxResult.error("数据审核中或已审核完成，不能操作！");
            }
        }else{
            //判断项目是否已经存在
            if (exist(params.getRollCycleId(),params.getProjectName())){
                return AjaxResult.error("滚动周期内已存在同名项目，请联系管理员！");
            }
        }
        if (!VERIFY_STATUS_DRAFT.equals(params.getState())){
            List<SysDictData> dicts = DictUtils.getDictCacheObCc(DICT_KEY_PROJECT_LIBRARY_STATE);
            if (StringUtils.isNotEmpty(dicts)){
                String state = VERIFY_STATUS_QUXIAN;
                if (dicts.size()>1){
                    if (roleKeys.contains(ROLE_KEY_QUXIANTIANBAO)){
                        state = VERIFY_STATUS_QUXIAN;
                    }else if (roleKeys.contains(ROLE_KEY_SHIJITIANBAO)){
                        state = VERIFY_STATUS_SHIJI;
                    }else if(roleKeys.contains(ROLE_KEY_SHENGJITIANBAO)){
                        state = VERIFY_STATUS_SANFANG;
                    }else{
                        Integer index = DictUtils.getVerifyDictIndex(dicts,roleKeys);
                        if (index != null){
                            state = dicts.get(index).getDictValue();
                        }
                    }
                }
                params.setState(state);
            }else{
                params.setState(VERIFY_STATUS_PASS);
            }
        }
        if (StringUtils.isNotEmpty(params.getApplicationFileList())){
            params.setApplicationFiles(params.getApplicationFileList().toJSONString());
        }else{
            params.setApplicationFiles("");
        }
        boolean result = super.saveOrUpdate(params);
        if (!result){
            return AjaxResult.error();
        }
        //新增日志
        if (!VERIFY_STATUS_DRAFT.equals(params.getState())){
            ProjectRollLibraryLog rollLibraryLog = new ProjectRollLibraryLog();
            rollLibraryLog.setRollLibraryId(params.getId());
            rollLibraryLog.setStep("项目申请");
            rollLibraryLog.setCreateBy(params.getUpdateBy());
            rollLibraryLog.setType(VERIFY_TYPE_1);
            addLog(rollLibraryLog);
        }
        return AjaxResult.success();
    }

    /**
     * 审核
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult verify(VerifyVO params){
        //查询项目库是否存在
        ProjectRollLibrary rollLibrary = getById(params.getId());
        if (rollLibrary==null){
            return AjaxResult.error("项目库不存在！");
        }
        String state = rollLibrary.getState();
        if (Integer.parseInt(state)<=Integer.parseInt(VERIFY_STATUS_PASS)){
            return AjaxResult.error("项目库未提交审核！");
        }
        //项目库
        rollLibrary = new ProjectRollLibrary();
        rollLibrary.setId(params.getId());
        rollLibrary.setUpdateBy(params.getUserName());
        rollLibrary.setUpdateTime(DateUtils.getNowDate());
        //日志
        ProjectRollLibraryLog lastRollLibraryLog = null;
        ProjectRollLibraryLog rollLibraryLog = new ProjectRollLibraryLog();
        rollLibraryLog.setRollLibraryId(params.getId());
        rollLibraryLog.setCreateBy(params.getUserName());
        //字典处理
        List<SysDictData> dicts = DictUtils.getDictCacheObCc(DICT_KEY_PROJECT_LIBRARY_STATE);
        Integer index = DictUtils.getVerifyDictIndex(dicts,params.getRoleKeys());
        if (index==null){
            return AjaxResult.error("当前用户无权限审批！");
        }
        SysDictData dict = dicts.get(index);
        rollLibraryLog.setStep(dict.getDictLabel().substring(1));
        rollLibraryLog.setDescription(params.getDescription());
        //判断是否通过
        if (params.getPass()){
            if (Integer.parseInt(state)>Integer.parseInt(dict.getDictValue())){
                return AjaxResult.error("当前角色已审批！");
            }
            dict = dicts.get(index+1);
            rollLibrary.setState(dict.getDictValue());
            rollLibraryLog.setType(VERIFY_TYPE_2);
            //是否为终审
            if (VERIFY_STATUS_PASS.equals(dict.getDictValue())){
                lastRollLibraryLog = new ProjectRollLibraryLog();
                lastRollLibraryLog.setRollLibraryId(params.getId());
                lastRollLibraryLog.setType(VERIFY_TYPE_4);
            }
            //修改项目库内容
            if (params.getObj()!=null){
                JSONArray files = params.getObj().getJSONArray("applicationFileList");
                if (StringUtils.isEmpty(files)){
                    params.getObj().remove("applicationFileList");
                }
                ProjectRollLibrary prl = params.getObj().to(ProjectRollLibrary.class);
                if (StringUtils.isNotEmpty(prl.getApplicationFileList())){
                    prl.setApplicationFiles(prl.getApplicationFileList().toJSONString());
                }
                updateById(prl);
            }
        }else{
            if (params.getState()!=null && Constants.VERIFY_TYPE_6==params.getState()){
                if (Integer.parseInt(state)>Integer.parseInt(dict.getDictValue())){
                    return AjaxResult.error("当前角色已审批！");
                }
                rollLibrary.setState(VERIFY_STATUS_PENDING);
                rollLibraryLog.setType(VERIFY_TYPE_6);
            }else{
                if (Integer.parseInt(state)>(Integer.parseInt(dict.getDictValue())+1)){
                    return AjaxResult.error("当前状态无法驳回！");
                }
                rollLibrary.setState(VERIFY_STATUS_REJECT);
                rollLibraryLog.setType(VERIFY_TYPE_3);
            }
        }
        //修改项目库状态
        updateById(rollLibrary);
        //新增日志
        addLog(rollLibraryLog);
        if (lastRollLibraryLog!=null){
            addLog(lastRollLibraryLog);
        }
        return AjaxResult.success();
    }

    /**
     * 批量审核
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchVerify(VerifyVO params){
        for (Long id:params.getIds()) {
            params.setId(id);
            verify(params);
        }
    }

    /**
     * 新增日志
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public void addLog(ProjectRollLibraryLog params){
        String deptName = SecurityUtils.getLoginUser().getUser().getDept().getDeptName();
        if (params.getType()!= Constants.VERIFY_TYPE_4){
            params.setCreateBy(StringUtils.join(deptName,"-",params.getCreateBy()));
        }
        params.setCreateTime(DateUtils.getNowDate());
        logMapper.insert(params);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public boolean delete(Long id){
        ProjectRollLibrary params = new ProjectRollLibrary();
        params.setId(id);
        params.setDeleteTag(DELETE_TAG_1);
        return super.updateById(params);
    }

    /**
     * 分页查询
     * @param params
     * @return
     */
    @DataScope(deptAlias = "t1",userAlias = "t1")
    public List<ProjectRollLibrary> list(ProjectRollLibrary params){
        return super.baseMapper.listPage(params);
    }

    /**
     * 根据项目名称模糊匹配
     * @param params
     * @return
     */
    @DataScope(deptAlias = "prl")
    public List<Map> getProjectByPN(ProjectRollLibrary params){
        return super.baseMapper.selectByPN(params);
    }

    /**
     * 获取详情
     * @param id
     * @return
     */
    public ProjectRollLibrary getInfoById(Long id){
        ProjectRollLibrary library = getById(id);
        library.setLogs(logMapper.selectByRollLibraryId(id));
        return library;
    }

    /**
     * 根据条件查询数量
     * @param cycleId
     * @return
     */
    public int selectByCycleId(Long cycleId){
        LambdaQueryWrapper<ProjectRollLibrary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectRollLibrary::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(ProjectRollLibrary::getRollCycleId,cycleId);
        return super.baseMapper.selectCount(wrapper);
    }

    /**
     * 导入数据
     * @param data
     * @param userName
     * @return
     */
    public AjaxResult importData(List<ProjectRollLibraryVO> data,Long rollCycleId,String userName){
        //滚动周期
        ProjectRollCycle rollCycle = rollCycleService.getById(rollCycleId);
        //数据转换
        List<ProjectRollLibrary> list = new ArrayList<>();
        int index = 1;
        Set<String> projectNames = new HashSet<>();
        for (ProjectRollLibraryVO vo:data){
            //第一行判断周期是否一致
            if (index == 1){
                List<String> years = StringUtils.getList(vo.getApply1(),vo.getApply2(),vo.getApply3());
                int beginYear = stringToInteger(years.get(0));
                int endYear = stringToInteger(years.get(years.size()-1));
                if (rollCycle.getBeginYear().equals(beginYear)&&rollCycle.getEndYear().equals(endYear)){
                    index++;
                    continue;
                }else{
                    return AjaxResult.error("表格中拟申请资金年度与滚动周期（"+rollCycle.getBeginYear()+"-"+rollCycle.getEndYear()+"）不一致");
                }
            }
            ProjectRollLibrary prl = new ProjectRollLibrary();
            BeanUtils.copyProperties(vo,prl);
            if (StringUtils.isNotBlank(vo.getPlanBuildCycleBeginYear())){
                prl.setPlanBuildCycleBeginYear(Integer.valueOf(vo.getPlanBuildCycleBeginYear()));
            }
            if (StringUtils.isNotBlank(vo.getPlanBuildCycleEndYear())){
                prl.setPlanBuildCycleEndYear(Integer.valueOf(vo.getPlanBuildCycleEndYear()));
            }
            //检查项目名称是否为空
            if (StringUtils.isBlank(prl.getProjectName())){
                return AjaxResult.error("有项目名称为空，请检查数据");
            }
            if (projectNames.contains(prl.getProjectName())){
                return AjaxResult.error("导入数据中存在相同名称的项目:"+prl.getProjectName());
            }
            projectNames.add(prl.getProjectName());
            //项目性质
            if (!Arrays.asList("新报","续报","延续","续建").contains(prl.getProjectType())){
                return AjaxResult.error("项目性质只能是新报、续报、延续、续建中的一种，请检查数据");
            }
            //项目类型
            if (!Arrays.asList("工程建设类","非工程建设类").contains(vo.getProjectCategoryText())){
                return AjaxResult.error("项目类型只能是工程建设类、非工程建设类中的一种，请检查数据");
            }
            prl.setProjectCategory("工程建设类".equals(vo.getProjectCategoryText())?1:0);
            //检查项目是否存在
            if (exist(rollCycleId,prl.getProjectName())){
                return AjaxResult.error("滚动周期（"+rollCycle.getBeginYear()+"-"+rollCycle.getEndYear()+"），项目名称（"+prl.getProjectName()+"）的项目已存在，请检查数据");
            }
            //区县
            if (StringUtils.isBlank(vo.getUnitName())){
                return AjaxResult.error("有区县为空，请检查数据");
            }
            List<SysDept> depts = deptService.selectDeptIdByDeptName(vo.getUnitName());
            if (StringUtils.isEmpty(depts)){
                return AjaxResult.error(vo.getUnitName()+"-无法找到匹配区县，请检查数据");
            }
            if (depts.size()>1){
                return AjaxResult.error(vo.getUnitName()+"-不是区县级别，请检查数据");
            }
            SysDept dept = depts.get(0);
            prl.setCityDistrict(dept.getCityCode());
            prl.setDeptId(dept.getDeptId());
            //145类别
            List<String> type145List = StringUtils.getList(vo.getType1(),vo.getType2(),vo.getType3());
            if (StringUtils.isNotEmpty(type145List)){
                prl.setType145(dictInfoMapper.get145TypeByName(type145List));
            }
            //拟申请资金（年度）
            List<String> applyList = StringUtils.getList(vo.getApply1(),vo.getApply2(),vo.getApply3());
            if (StringUtils.isNotEmpty(applyList)){
                prl.setApplySpecialCapital(StringUtils.join(applyList,","));
            }
            //基础信息
            prl.setRollCycleId(rollCycleId);
            prl.setState("0");
            prl.setCreateBy(userName);
            prl.setCreateTime(DateUtils.getNowDate());
            prl.setUpdateBy(userName);
            prl.setUpdateTime(prl.getCreateTime());
            list.add(prl);
        }
        //保存数据
        saveBatch(list);
        return AjaxResult.success();
    }

    /**
     * 统计数据和资金
     * @param cycleId
     * @return
     */
    public Map statisticsNumAndFund(Long cycleId){
        return super.baseMapper.statisticsNumAndFund(cycleId);
    }

    /**
     * 按区县统计
     * @param params
     * @return
     */
    public List<ProjectRollLibrarySummaryVO> statisticsGbQx(StatisticsQueryVO params){
        //查询数据
        List<ProjectRollLibrarySummaryVO> list = super.baseMapper.statisticsGbQx(params);
        if (StringUtils.isEmpty(list)){
            return null;
        }
        List<ProjectRollLibrarySummaryVO> result = new ArrayList<>();
        //获取范围字典
        List<SysDictData> dictData = DictUtils.getDictCache(DICT_KEY_FANWEIQUYU);
        //查询所有城市区县信息
        List<TreeSelect> cityList = deptService.selectCityTreeList(new SysDept(0));
        ProjectRollLibrarySummaryVO total = new ProjectRollLibrarySummaryVO("总计");
        result.add(total);
        //循环拼装数据
        for (SysDictData dict:dictData){
            ProjectRollLibrarySummaryVO fw = new ProjectRollLibrarySummaryVO(dict.getDictLabel());
            for (ProjectRollLibrarySummaryVO vo:list) {
                if (vo.getName().equals(dict.getDictValue())){
                    fw.add(vo);
                }
            }
            if (fw.getProjectNum()>0){
                fw.setChildren(initTree(cityList,list,dict.getDictValue()));
                total.add(fw);
                result.add(fw);
            }
        }
        return result;
    }

    /**
     * 初始化树结构
     * @param cityList
     * @param list
     * @param name
     * @return
     */
    private List<ProjectRollLibrarySummaryVO> initTree(List<TreeSelect> cityList,List<ProjectRollLibrarySummaryVO> list,String name){
        if (StringUtils.isEmpty(cityList)){
            return null;
        }
        List<ProjectRollLibrarySummaryVO> children = new ArrayList<>();
        for (TreeSelect tree:cityList){
            ProjectRollLibrarySummaryVO city = new ProjectRollLibrarySummaryVO(tree.getLabel());
            for (ProjectRollLibrarySummaryVO vo:list) {
                if (vo.getName().equals(name)&&Arrays.asList(vo.getDeptId().split(",")).contains(tree.getId().toString())){
                    city.add(vo);
                }
            }
            if (city.getProjectNum()>0){
                children.add(city);
                city.setChildren(initTree(tree.getChildren(),list,name));
            }
        }
        return children;
    }

    /**
     * 按145统计
     * @param params
     * @return
     */
    public List<Map> statisticsGb145(StatisticsQueryVO params){
        return super.baseMapper.statisticsGb145(params);
    }

    /**
     * 按项目类型
     * @param params
     * @return
     */
    public List<Map> statisticsGbType(StatisticsQueryVO params){
        return super.baseMapper.statisticsGbType(params);
    }

    /**
     * 按是否下达计划
     * @param params
     * @return
     */
    public List<Map> statisticsGbPlan(StatisticsQueryVO params){
        return super.baseMapper.statisticsGbPlan(params);
    }

    /**
     * 按项目实施状态
     * @param params
     * @return
     */
    public List<Map> statisticsGbBuildStatus(StatisticsQueryVO params){
        return super.baseMapper.statisticsGbBuildStatus(params);
    }

    /**
     * 根据滚动周期和项目名称查询是否存在
     * @param rollCycleId
     * @param projectName
     * @return
     */
    private boolean exist(Long rollCycleId,String projectName){
        LambdaQueryWrapper<ProjectRollLibrary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectRollLibrary::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(ProjectRollLibrary::getRollCycleId,rollCycleId);
        wrapper.eq(ProjectRollLibrary::getProjectName,projectName);
        return count(wrapper)>0;
    }

    /**
     * 字符串转整数
     * @param s
     * @return
     */
    private int stringToInteger(String s) {
        // 过滤掉非数字字符
        String numStr = s.replaceAll("\\D", "");
        return Integer.parseInt(numStr);
    }

    /**
     * 获取项目编号
     * @param ids
     * @return
     */
    public List<String> getProjectNos(List<Long> ids){
        LambdaQueryWrapper<ProjectRollLibrary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectRollLibrary::getDeleteTag,DELETE_TAG_0);
        wrapper.isNotNull(ProjectRollLibrary::getProjectNo);
        wrapper.in(ProjectRollLibrary::getId,ids);
        wrapper.select(ProjectRollLibrary::getProjectNo);
        List<Object> list = super.baseMapper.selectObjs(wrapper);
        if (StringUtils.isEmpty(list)){
            return null;
        }
        return list.stream().map(Object::toString).collect(Collectors.toList());
    }

    /**
     * 撤回
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult rollback(VerifyVO params){
        //获取详情
        ProjectRollLibrary library = getById(params.getId());
        if (library==null){
            return AjaxResult.error("数据不存在");
        }
        //获取审核状态字典
        List<SysDictData> dicts = DictUtils.getDictCacheObCc(DICT_KEY_PROJECT_LIBRARY_STATE);
        //根据角色获取审核初始状态
        SysDictData dict = null;
        if (params.getRoleKeys().contains(ROLE_KEY_SHENGJITIANBAO)){
            dict = dicts.get(2);
        }else if (params.getRoleKeys().contains(ROLE_KEY_SHIJITIANBAO)){
            dict = dicts.get(1);
        }else if (params.getRoleKeys().contains(ROLE_KEY_QUXIANTIANBAO)){
            dict = dicts.get(0);
        }else{
            return AjaxResult.error("不是填报人员无法使用撤回功能！");
        }
        //项目库审核状态判断
        if (!library.getState().equals(dict.getDictValue())){
            return AjaxResult.error(StringUtils.join("项目库当前不是",dict.getDictLabel(),"状态，无法撤回！"));
        }
        //修改项目库状态为草稿
        ProjectRollLibrary up = new ProjectRollLibrary();
        up.setId(library.getId());
        up.setState(VERIFY_STATUS_DRAFT);
        up.setUpdateBy(params.getUserName());
        up.setUpdateTime(DateUtils.getNowDate());
        if (updateById(up)){
            //增加日志
            ProjectRollLibraryLog rollLibraryLog = new ProjectRollLibraryLog();
            rollLibraryLog.setRollLibraryId(params.getId());
            rollLibraryLog.setStep("撤回申请");
            rollLibraryLog.setCreateBy(params.getUserName());
            rollLibraryLog.setType(VERIFY_TYPE_5);
            addLog(rollLibraryLog);
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }
}
