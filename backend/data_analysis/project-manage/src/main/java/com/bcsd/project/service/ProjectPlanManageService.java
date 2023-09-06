package com.bcsd.project.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.common.annotation.DataScope;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.domain.entity.SysDictData;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.DictUtils;
import com.bcsd.common.utils.SecurityUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.constants.Constants;
import com.bcsd.project.domain.*;
import com.bcsd.project.domain.vo.ProjectUserVO;
import com.bcsd.project.domain.vo.VerifyVO;
import com.bcsd.project.mapper.ProjectPlanLogMapper;
import com.bcsd.project.mapper.ProjectPlanManageMapper;
import com.bcsd.project.mapper.ProjectPlanYearManageMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.bcsd.project.constants.Constants.*;

/**
 * 计划管理 服务实现类
 * @author liuliang
 * @since 2023-02-20
 */
@Slf4j
@Service
public class ProjectPlanManageService extends ServiceImpl<ProjectPlanManageMapper, ProjectPlanManage> implements IService<ProjectPlanManage> {

    @Autowired
    ProjectRollLibraryService rollLibraryService;
    @Autowired
    ProjectImplService implService;
    @Autowired
    ProjectPlanYearManageMapper planYearManageMapper;
    @Autowired
    ProjectPlanLogMapper logMapper;

    /**
     * 保存计划年度
     * @param params
     * @return
     */
    public int savePlanYear(ProjectPlanYearManage params){
        if (StringUtils.isNotEmpty(params.getFileList())){
            params.setFileInfo(JSON.toJSONString(params.getFileList()));
        }else{
            params.setFileInfo("");
        }
        if (params.getId()==null){
            params.setCreateTime(DateUtils.getNowDate());
            return planYearManageMapper.insert(params);
        }else{
            params.setUpdateBy(params.getCreateBy());
            params.setUpdateTime(DateUtils.getNowDate());
            params.setCreateBy(null);
            return planYearManageMapper.updateById(params);
        }
    }

    /**
     * 计划年度是否存在
     * @param planYear
     * @param id
     * @return
     */
    public boolean planYearIsExist(Integer planYear,Long id){
        LambdaQueryWrapper<ProjectPlanYearManage> wrapper =  new LambdaQueryWrapper<>();
        wrapper.eq(ProjectPlanYearManage::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(ProjectPlanYearManage::getPlanYear,planYear);
        if (id!=null){
            wrapper.ne(ProjectPlanYearManage::getId,id);
        }
        return planYearManageMapper.selectCount(wrapper)>0;
    }

    /**
     * 计划年度删除
     * @param id
     * @return
     */
    public int deletePlanYear(Long id,String userName){
        ProjectPlanYearManage planYearManage = new ProjectPlanYearManage();
        planYearManage.setId(id);
        planYearManage.setDeleteTag(DELETE_TAG_1);
        planYearManage.setUpdateBy(userName);
        planYearManage.setUpdateTime(DateUtils.getNowDate());
        return planYearManageMapper.updateById(planYearManage);
    }

    /**
     * 查询所有
     * @return
     */
    public List<ProjectPlanYearManage> getPlanYears(){
        LambdaQueryWrapper<ProjectPlanYearManage> wrapper =  new LambdaQueryWrapper<>();
        wrapper.eq(ProjectPlanYearManage::getDeleteTag,DELETE_TAG_0);
        wrapper.orderByDesc(ProjectPlanYearManage::getPlanYear);
        return planYearManageMapper.selectList(wrapper);
    }

    /**
     * 信息回填
     * @param id
     * @return
     */
    public ProjectPlanManage getByRLId(Long id){
        //查询滚动项目库信息
        ProjectRollLibrary rollLibrary = rollLibraryService.getById(id);
        ProjectPlanManage info = new ProjectPlanManage();
        BeanUtils.copyProperties(rollLibrary,info);
        info.setId(null);
        info.setRollLibraryId(rollLibrary.getId());
        info.setYearBuildTask(rollLibrary.getBuildContentScale());
        if (StringUtils.isNotBlank(rollLibrary.getProjectType())){
            info.setProjectType(Arrays.asList("新报","续报").contains(rollLibrary.getProjectType())?"新建":"续建");
        }
        //其他信息填充
        ProjectImplVersion version = implService.getNewVersion(info.getProjectNo());
        if (version!=null){
            info.setFirstArrangeFundYear(version.getFirstArrangeFundYear());
            info.setScheduledArrangeFund(version.getPlanFinalIssueFund());
            info.setCompletedArrangeFund(version.getCompleteFund());
        }
        return info;
    }

    /**
     * 分页查询
     * @param params
     * @return
     */
    @DataScope(deptAlias = "ppm",userAlias = "ppm")
    public List<ProjectPlanManage> list(ProjectPlanManage params){
        return super.baseMapper.listPage(params);
    }

    /**
     * 项目是否存在
     * @param params
     * @return
     */
    public boolean exists(ProjectPlanManage params){
        LambdaQueryWrapper<ProjectPlanManage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectPlanManage::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(ProjectPlanManage::getProjectNo,params.getProjectNo());
        wrapper.eq(ProjectPlanManage::getPlanYear,params.getPlanYear());
        wrapper.eq(ProjectPlanManage::getBatch,params.getBatch());
        if (params.getId()!=null){
            wrapper.ne(ProjectPlanManage::getId,params.getId());
        }
        return super.baseMapper.selectCount(wrapper) > 0;
    }

    /**
     * 查询最大计划年度和计划资金安排情况
     * @return
     */
    public JSONObject getPlanFundInfo(Integer type){
        //最大计划年度
        Integer maxPlanYear = planYearManageMapper.getMaxPlanYear();
        //获取资金数据并处理
        List<Map> listData = null;
        switch (type){
            case 1:
                listData = super.baseMapper.getFundGb145AndYear();
                break;
            case 2:
                listData = super.baseMapper.getFundGbQXAndYear();
        }
        JSONArray list = new JSONArray();
        if (CollectionUtils.isNotEmpty(listData)){
            JSONObject obj = new JSONObject();
            for (Map map:listData){
                String key = map.get("name").toString();
                JSONObject data = obj.getJSONObject(key);
                if (data==null){
                    data = new JSONObject();
                    data.put("name",key);
                }
                data.put("xd_tz_"+map.get("year"),map.get("fund"));
                obj.put(key,data);
            }
            for (String key:obj.keySet()){
                list.add(obj.getJSONObject(key));
            }
        }
        JSONObject result = new JSONObject();
        result.put("maxPlanYear",maxPlanYear);
        result.put("list",list);
        return result;
    }

    /**
     * 根据部门和计划年度查询
     * @param params
     * @return
     */
    public List<ProjectPlanManage> selectByDeptIdAndPlanYear(ProjectPlanManage params){
        return super.baseMapper.selectByDeptIdAndPlanYear(params);
    }

    /**
     * 项目分配
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public void projectBindUser(ProjectUserVO params){
        //修改项目库项目和用户绑定关系
        LambdaQueryWrapper<ProjectRollLibrary> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(ProjectRollLibrary::getId,params.getProjectIds());
        ProjectRollLibrary library = new ProjectRollLibrary();
        library.setUserId(params.getUserId());
        rollLibraryService.update(library,wrapper1);
        List<String> projectNos = rollLibraryService.getProjectNos(params.getProjectIds());
        if (StringUtils.isNotEmpty(projectNos)){
            //修改计划和用户绑定关系
            LambdaQueryWrapper<ProjectPlanManage> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.in(ProjectPlanManage::getProjectNo,projectNos);
            ProjectPlanManage planManage = new ProjectPlanManage();
            planManage.setUserId(params.getUserId());
            update(planManage,wrapper2);
            //修改项目实施和用户绑定关系
            LambdaQueryWrapper<ProjectImplBasicInfo> wrapper3 = new LambdaQueryWrapper<>();
            wrapper3.in(ProjectImplBasicInfo::getProjectNo,projectNos);
            ProjectImplBasicInfo implBasicInfo = new ProjectImplBasicInfo();
            implBasicInfo.setUserId(params.getUserId());
            implService.update(implBasicInfo,wrapper3);
        }
    }

    /**
     * 保存（申请）
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult savePlan(ProjectPlanManage params, Set<String> roleKeys){
        //判断项目是否已经存在
        if (exists(params)){
            return AjaxResult.error("年度计划（批次）已申请，请联系管理员！");
        }
        boolean isAdd = true;
        if (params.getId()!=null){
            isAdd = false;
            ProjectPlanManage ppm = getById(params.getId());
            Integer state = Integer.parseInt(ppm.getState());
            if (state>=0){
                return AjaxResult.error("数据审核中或已审核完成，不能操作！");
            }
        }
        if (!VERIFY_STATUS_DRAFT.equals(params.getState())){
            List<SysDictData> dicts = DictUtils.getDictCacheObCc(DICT_KEY_PROJECT_PLAN_STATE);
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
        if (StringUtils.isNotEmpty(params.getApprovalFiles())){
            params.setApprovalFilesJson(JSON.toJSONString(params.getApprovalFiles()));
        }else{
            params.setApprovalFilesJson("");
        }
        boolean result = super.saveOrUpdate(params);
        if (!result){
            return AjaxResult.error();
        }
        //回填项目库项目编号
        if (isAdd){
            ProjectRollLibrary rollLibrary = new ProjectRollLibrary();
            rollLibrary.setProjectNo(params.getProjectNo());
            rollLibrary.setId(params.getRollLibraryId());
            rollLibraryService.updateById(rollLibrary);
        }
        //新增日志
        if (!VERIFY_STATUS_DRAFT.equals(params.getState())){
            ProjectPlanLog planLog = new ProjectPlanLog();
            planLog.setPlanId(params.getId());
            planLog.setStep("计划申请");
            planLog.setCreateBy(params.getUpdateBy());
            planLog.setType(VERIFY_TYPE_1);
            addLog(planLog);
        }
        return AjaxResult.success();
    }

    /**
     * 新增日志
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public void addLog(ProjectPlanLog params){
        String deptName = SecurityUtils.getLoginUser().getUser().getDept().getDeptName();
        if (params.getType()!= Constants.VERIFY_TYPE_4){
            params.setCreateBy(StringUtils.join(deptName,"-",params.getCreateBy()));
        }
        params.setCreateTime(DateUtils.getNowDate());
        logMapper.insert(params);
    }

    /**
     * 获取详情
     * @param id
     * @return
     */
    public ProjectPlanManage getInfoById(Long id){
        ProjectPlanManage ppm = getById(id);
        if (ppm!=null){
            ppm.setLogs(logMapper.selectByPlanId(id));
        }
        return ppm;
    }

    /**
     * 审核
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult verify(VerifyVO params){
        //查询项目库是否存在
        ProjectPlanManage ppm = getById(params.getId());
        if (ppm==null){
            return AjaxResult.error("计划申请不存在！");
        }
        String state = ppm.getState();
        if (Integer.parseInt(state)<=Integer.parseInt(VERIFY_STATUS_PASS)){
            return AjaxResult.error("计划申请未提交审核！");
        }
        //计划
        ProjectPlanManage uppm = new ProjectPlanManage();
        uppm.setId(params.getId());
        uppm.setUpdateBy(params.getUserName());
        uppm.setUpdateTime(DateUtils.getNowDate());
        //日志
        ProjectPlanLog planLog = new ProjectPlanLog();
        planLog.setPlanId(params.getId());
        planLog.setCreateBy(params.getUserName());
        //字典处理
        List<SysDictData> dicts = DictUtils.getDictCacheObCc(DICT_KEY_PROJECT_PLAN_STATE);
        Integer index = DictUtils.getVerifyDictIndex(dicts,params.getRoleKeys());
        if (index==null){
            return AjaxResult.error("当前用户无权限审批！");
        }
        SysDictData dict = dicts.get(index);
        planLog.setStep(dict.getDictLabel().substring(1));
        planLog.setDescription(params.getDescription());
        //判断是否通过
        if (params.getPass()){
            if (Integer.parseInt(state)>Integer.parseInt(dict.getDictValue())){
                return AjaxResult.error("当前角色已审批！");
            }
            dict = dicts.get(index+1);
            uppm.setState(dict.getDictValue());
            planLog.setType(VERIFY_TYPE_2);
            //修改计划内容
            if (params.getObj()!=null){
                JSONArray files = params.getObj().getJSONArray("applicationFileList");
                if (StringUtils.isEmpty(files)){
                    params.getObj().remove("applicationFileList");
                }
                ProjectPlanManage planManage = params.getObj().to(ProjectPlanManage.class);
                if (StringUtils.isNotEmpty(planManage.getApplicationFileList())){
                    planManage.setApplicationFiles(planManage.getApplicationFileList().toJSONString());
                }
                if (StringUtils.isNotEmpty(planManage.getApprovalFiles())){
                    planManage.setApprovalFilesJson(JSON.toJSONString(planManage.getApprovalFiles()));
                }
                updateById(planManage);
            }
        }else{
            if (params.getState()!=null && Constants.VERIFY_TYPE_6==params.getState()){
                if (Integer.parseInt(state)>Integer.parseInt(dict.getDictValue())){
                    return AjaxResult.error("当前角色已审批！");
                }
                uppm.setState(VERIFY_STATUS_PENDING);
                planLog.setType(VERIFY_TYPE_6);
            }else{
                if (Integer.parseInt(state)>(Integer.parseInt(dict.getDictValue())+1)){
                    return AjaxResult.error("当前状态无法驳回！");
                }
                uppm.setState(VERIFY_STATUS_REJECT);
                planLog.setType(VERIFY_TYPE_3);
            }
        }
        //修改计划状态
        updateById(uppm);
        //新增日志
        addLog(planLog);
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
     * 下达计划
     * @param id
     * @param userName
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult releasePlan(Long id,String userName){
        //查询项目库是否存在
        ProjectPlanManage ppm = getById(id);
        if (ppm==null){
            return AjaxResult.error("计划申请不存在！");
        }
        if (!VERIFY_STATUS_PASS.equals(ppm.getState())){
            return AjaxResult.error("计划申请不是审核通过状态！");
        }
        //生成项目实施数据
        ProjectImplBasicInfo pibi = new ProjectImplBasicInfo();
        BeanUtils.copyProperties(ppm,pibi);
        pibi.setId(null);
        pibi.setCreateBy(userName);
        pibi.setUpdateBy(userName);
        pibi.setCreateTime(DateUtils.getNowDate());
        pibi.setUpdateTime(pibi.getCreateTime());
        pibi.setSpecialFunds(ppm.getCurrentScheduledArrangeFund());
        pibi.setProjectPlanId(ppm.getId());
        pibi.setPlanYear(ppm.getPlanYear().toString());
        //年度任务
        JSONObject yearTask = new JSONObject();
        yearTask.put("year",pibi.getPlanYear()+(ppm.getBatch()==1?"年提前批":"年第二批"));
        yearTask.put("text",ppm.getYearBuildTask()==null?"":ppm.getYearBuildTask());
        pibi.setYearBuildTask(new JSONArray(yearTask).toJSONString());
        if (implService.add(pibi,ppm.getBatch())){
            //修改计划状态
            ppm = new ProjectPlanManage();
            ppm.setId(id);
            ppm.setState(VERIFY_STATUS_RELEASE);
            ppm.setUpdateBy(userName);
            ppm.setUpdateTime(pibi.getUpdateTime());
            updateById(ppm);
            //增加日志
            ProjectPlanLog planLog = new ProjectPlanLog();
            planLog.setPlanId(id);
            planLog.setType(VERIFY_TYPE_4);
            addLog(planLog);
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 计划删除
     * @param id
     * @param userName
     * @return
     */
    public AjaxResult delPlan(Long id,String userName){
        //查询项目库是否存在
        ProjectPlanManage ppm = getById(id);
        if (ppm==null){
            return AjaxResult.error("计划申请不存在！");
        }
        if (!VERIFY_STATUS_DRAFT.equals(ppm.getState())){
            return AjaxResult.error("计划申请不是草稿状态！");
        }
        ppm = new ProjectPlanManage();
        ppm.setId(id);
        ppm.setDeleteTag(DELETE_TAG_1);
        ppm.setUpdateBy(userName);
        ppm.setUpdateTime(DateUtils.getNowDate());
        if (updateById(ppm)){
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 根据项目编号获取名称
     * @return
     */
    public ProjectPlanManage getProjectNameByNo(String projectNo){
        LambdaQueryWrapper<ProjectPlanManage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectPlanManage::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(ProjectPlanManage::getProjectNo,projectNo);
        wrapper.select(ProjectPlanManage::getProjectNo,ProjectPlanManage::getProjectName);
        return super.baseMapper.selectOne(wrapper);
    }

    /**
     * 撤回
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult rollback(VerifyVO params){
        //获取详情
        ProjectPlanManage plan = getById(params.getId());
        if (plan==null){
            return AjaxResult.error("数据不存在");
        }
        //获取审核状态字典
        List<SysDictData> dicts = DictUtils.getDictCacheObCc(DICT_KEY_PROJECT_PLAN_STATE);
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
        if (!plan.getState().equals(dict.getDictValue())){
            return AjaxResult.error(StringUtils.join("计划当前不是",dict.getDictLabel(),"状态，无法撤回！"));
        }
        //修改项目库状态为草稿
        ProjectPlanManage up = new ProjectPlanManage();
        up.setId(plan.getId());
        up.setState(VERIFY_STATUS_DRAFT);
        up.setUpdateBy(params.getUserName());
        up.setUpdateTime(DateUtils.getNowDate());
        if (updateById(up)){
            //增加日志
            ProjectPlanLog planLog = new ProjectPlanLog();
            planLog.setPlanId(params.getId());
            planLog.setStep("撤回申请");
            planLog.setCreateBy(params.getUserName());
            planLog.setType(VERIFY_TYPE_5);
            addLog(planLog);
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }
}
