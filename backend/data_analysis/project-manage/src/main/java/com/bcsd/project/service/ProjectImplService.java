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
import com.bcsd.common.utils.*;
import com.bcsd.project.domain.*;
import com.bcsd.project.domain.vo.*;
import com.bcsd.project.enums.FundEnum;
import com.bcsd.project.mapper.*;
import com.bcsd.system.service.ISysDictTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bcsd.project.constants.Constants.*;

/**
 * 项目实施 服务实现类
 *
 * @author liuliang
 * @since 2023-02-22
 */
@Slf4j
@Service
public class ProjectImplService extends ServiceImpl<ProjectImplBasicInfoMapper, ProjectImplBasicInfo> implements IService<ProjectImplBasicInfo> {

    @Autowired
    ProjectImplStateInfoMapper stateInfoMapper;
    @Autowired
    ProjectImplFundInfoMapper fundInfoMapper;
    @Autowired
    ProjectImplFundDetailInfoMapper fundDetailInfoMapper;
    @Autowired
    ProjectImplEvolveMapper evolveMapper;
    @Autowired
    ProjectImplFileInfoMapper fileInfoMapper;
    @Autowired
    ProjectImplVrFileInfoMapper vrFileInfoMapper;
    @Autowired
    ProjectImplVersionMapper versionMapper;
    @Autowired
    ProjectImplProcessLogMapper processLogMapper;
    @Autowired
    private ISysDictTypeService dictTypeService;
    @Autowired
    private ProjectDictInfoService dictInfoService;
    @Autowired
    private ProjectImplCctvMapper cctvMapper;

    /**
     * 新增项目实施基本信息
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean add(ProjectImplBasicInfo params,Integer batch){
        //参数处理
        String versionDescription = StringUtils.join(params.getPlanYear(),"年计划下达【",batch==1?"提前批":"第二批","】");
        params.setRemark(versionDescription);
        String planYear = params.getPlanYear();
        String fund = params.getSpecialFunds();
        //获取最新版本数据
        ProjectImplVersion version = getNewVersion(params.getProjectNo());
        String basicInfo = null;
        ProjectImplInfoVO implInfoVO = new ProjectImplInfoVO();
        implInfoVO.setUserName(params.getCreateBy());
        ProjectImplFundInfo fundInfo = null;
        if (version==null){
            version = new ProjectImplVersion();
            //新增项目实施信息
            params.setProjectStatus(VERIFY_STATUS_PASS);
            params.setCreateTime(params.getUpdateTime());
            params.setBuildStatus(BUILD_TYPE_1);
            params.setSpecialFunds(ArithUtil.add(fund,params.getScheduledArrangeFund()));
        }else{
            //基本信息处理
            ProjectImplBasicInfo info = JSON.parseObject(version.getBasicInfo(),ProjectImplBasicInfo.class);
            params.setId(info.getId());
            if(!info.getPlanYear().contains(params.getPlanYear())){
                params.setPlanYear(StringUtils.join(info.getPlanYear(),"/",planYear));
            }
            JSONArray yearTasks = JSON.parseArray(info.getYearBuildTask());
            yearTasks.addAll(JSON.parseArray(params.getYearBuildTask()));
            params.setYearBuildTask(yearTasks.toJSONString());
            params.setBuildStatus(info.getBuildStatus());
            params.setGeographyPosition(info.getGeographyPosition());
            params.setCreateBy(null);
            params.setCreateTime(null);
            params.setSpecialFunds(ArithUtil.add(fund,info.getSpecialFunds()));
            JSONObject obj = JSON.parseObject(version.getBasicInfo());
            obj.putAll(JSON.parseObject(JSON.toJSONString(params)));
            basicInfo = obj.toJSONString();
            //资金信息
            fundInfo = getFundsInfo(params.getProjectNo());
        }
        boolean result = saveOrUpdate(params);
        if (result){
            if (StringUtils.isBlank(basicInfo)){
                basicInfo = JSON.toJSONString(params);
            }
            //资金信息
            implInfoVO.setBasicInfo(params);
            fundInfo = initFundInfo(fundInfo,fund,batch,planYear);
            implInfoVO.setFundsInfo(fundInfo);
            //保存资金信息
            saveFundsInfo(implInfoVO);
            //版本资金处理
            String fundVersion = null;
            if (StringUtils.isBlank(version.getFundsInfo())){
                fundVersion = JSON.toJSONString(implInfoVO.getFundsInfo());
            }else{
                ProjectImplFundInfo pifi = JSON.parseObject(version.getFundsInfo(),ProjectImplFundInfo.class);
                pifi = initFundInfo(pifi,fund,batch,planYear);
                fundVersion = JSON.toJSONString(pifi);
            }
            //产生版本信息
            JSONObject vobj = JSON.parseObject(basicInfo);
            vobj.putAll(JSON.parseObject(fundVersion));
            if (StringUtils.isNotBlank(version.getImplInfo())){
                vobj.putAll(JSON.parseObject(version.getImplInfo()));
            }
            if (StringUtils.isNotBlank(version.getEvolveInfo())){
                vobj.putAll(JSON.parseObject(version.getEvolveInfo()));
            }
            ProjectImplVersion newVersion = vobj.to(ProjectImplVersion.class);
            newVersion.setProjectId(params.getId());
            newVersion.setBasicInfo(basicInfo);
            newVersion.setImplInfo(version.getImplInfo());
            newVersion.setFundsInfo(fundVersion);
            newVersion.setEvolveInfo(version.getEvolveInfo());
            newVersion.setFilesInfo(version.getFilesInfo());
            newVersion.setVrInfo(version.getVrInfo());
            newVersion.setCreateBy(params.getUpdateBy());
            newVersion.setCreateTime(params.getUpdateTime());
            newVersion.setVersionDescription(versionDescription);
            addVersion(newVersion);
        }
        return result;
    }

    /**
     * 初始化资金信息
     * @param fundInfo
     * @param fund
     * @param batch
     * @param year
     * @return
     */
    public ProjectImplFundInfo initFundInfo(ProjectImplFundInfo fundInfo,String fund,Integer batch,String year){
        if (fundInfo==null){
            fundInfo = new ProjectImplFundInfo();
            ProjectImplFundDetailInfo fundDetailInfo = initFundDetailInfo(null,fund,batch,year);
            fundInfo.setPlanPcIssueFundList(Arrays.asList(fundDetailInfo));
            fundInfo.setPlanPcIssueFund(fund);
            fundInfo.setPlanFinalIssueFund(fund);
        }else{
            List<ProjectImplFundDetailInfo> detailInfos = fundInfo.getPlanPcIssueFundList();
            if (StringUtils.isEmpty(detailInfos)){
                ProjectImplFundDetailInfo fundDetailInfo = initFundDetailInfo(null,fund,batch,year);
                fundInfo.setPlanPcIssueFundList(Arrays.asList(fundDetailInfo));
            }else{
                boolean mark = false;
                for (ProjectImplFundDetailInfo detail:detailInfos) {
                    if (detail.getFundYear().equals(year)&&detail.getFundType().equals(FundEnum.FUND_TYPE_1.getCode())){
                        initFundDetailInfo(detail,fund,batch,year);
                        mark = true;
                        break;
                    }
                }
                if (!mark){
                    detailInfos.add(initFundDetailInfo(null,fund,batch,year));
                }
            }
            fundInfo.setPlanPcIssueFund(ArithUtil.add(fundInfo.getPlanPcIssueFund(),fund));
            fundInfo.setPlanFinalIssueFund(ArithUtil.add(fundInfo.getPlanFinalIssueFund(),fund));
        }
        return fundInfo;
    }

    /**
     * 资金详情
     * @param fundDetailInfo
     * @param fund
     * @param batch
     * @param year
     * @return
     */
    public ProjectImplFundDetailInfo initFundDetailInfo(ProjectImplFundDetailInfo fundDetailInfo,String fund,Integer batch,String year){
        if (fundDetailInfo==null){
            fundDetailInfo = new ProjectImplFundDetailInfo();
            fundDetailInfo.setFundYear(year);
            fundDetailInfo.setFundType(FundEnum.FUND_TYPE_1.getCode());
            fundDetailInfo.setFundTotal(fund);
            if (batch==1){
                fundDetailInfo.setMonth3(fund);
            }else{
                fundDetailInfo.setMonth5(fund);
            }
        }else{
            fundDetailInfo.setFundTotal(ArithUtil.add(fundDetailInfo.getFundTotal(),fund));
            if (batch==1){
                fundDetailInfo.setMonth3(ArithUtil.add(fundDetailInfo.getMonth3(),fund)) ;
            }else{
                fundDetailInfo.setMonth5(ArithUtil.add(fundDetailInfo.getMonth5(),fund));
            }
        }
        return fundDetailInfo;
    }

    /**
     * 分页查询
     * @param params
     * @return
     */
    @DataScope(deptAlias = "pibi",userAlias = "pibi")
    public List<ProjectImplBasicInfo> list(ProjectImplBasicInfo params){
        return super.baseMapper.listPage(params);
    }

    /**
     * 根据ID获取实施信息
     * @param id
     * @return
     */
    public ProjectImplInfoVO getById(Long id){
        ProjectImplBasicInfo basicInfo = super.getById(id);
        ProjectImplInfoVO info = null;
        if (basicInfo!=null){
            info = new ProjectImplInfoVO();
            info.setBasicInfo(basicInfo);
            info.setImplInfo(getImplInfo(basicInfo.getProjectNo()));
            info.setFundsInfo(getFundsInfo(basicInfo.getProjectNo()));
            info.setEvolveInfo(getEvolveInfo(basicInfo.getProjectNo()));
            info.setFilesInfo(getFilesInfo(basicInfo.getProjectNo()));
            info.setVrInfo(getVrInfo(basicInfo.getProjectNo()));
            info.setCctvs(getCCTV(basicInfo.getProjectNo()));
            info.setLogs(processLogMapper.selectByProjectNo(basicInfo.getProjectNo()));
        }
        return info;
    }

    /**
     * 编辑
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult update(ProjectImplInfoVO params){
        //基本信息
        ProjectImplBasicInfo basicInfo = params.getBasicInfo();
        ProjectImplBasicInfo pib = super.getById(basicInfo.getId());
        if (Integer.parseInt(pib.getProjectStatus())>0){
            return AjaxResult.error("数据审核中，不能操作！");
        }
        //设置审核状态
        if (VERIFY_STATUS_DRAFT.equals(params.getType())){
            basicInfo.setProjectStatus(VERIFY_STATUS_DRAFT);
            basicInfo.setRemark(" ");
        }else{
            List<SysDictData> dicts = DictUtils.getDictCacheObCc(DICT_KEY_PROJECT_IMPL_STATE);
            if (StringUtils.isNotEmpty(dicts)){
                String state = VERIFY_STATUS_QUXIAN;
                if (dicts.size()>1){
                    if (params.getRoleKeys().contains(ROLE_KEY_QUXIANTIANBAO)){
                        state = VERIFY_STATUS_QUXIAN;
                    }else if (params.getRoleKeys().contains(ROLE_KEY_SHIJITIANBAO)){
                        state = VERIFY_STATUS_SHIJI;
                    }else if(params.getRoleKeys().contains(ROLE_KEY_SHENGJITIANBAO)){
                        state = VERIFY_STATUS_SANFANG;
                    }else{
                        Integer index = DictUtils.getVerifyDictIndex(dicts,params.getRoleKeys());
                        if (index != null){
                            state = dicts.get(index).getDictValue();
                        }
                    }
                }
                basicInfo.setProjectStatus(state);
            }else{
                basicInfo.setProjectStatus(VERIFY_STATUS_PASS);
            }
            basicInfo.setRemark(params.getDescription());
        }
        basicInfo.setUpdateBy(params.getUserName());
        basicInfo.setUpdateTime(DateUtils.getNowDate());
        boolean result = updateById(basicInfo);
        if (!result){
            return AjaxResult.error();
        }
        //实施信息
        saveImplInfo(params);
        //资金
        saveFundsInfo(params);
        //进展
        saveEvolveInfo(params);
        //附件
        saveFilesInfo(params);
        //VR
        saveVrInfo(params);
        //增加记录
        if (!VERIFY_STATUS_DRAFT.equals(params.getType())){
            ProjectImplProcessLog processLog = new ProjectImplProcessLog();
            processLog.setProjectNo(basicInfo.getProjectNo());
            processLog.setDescription(params.getDescription());
            processLog.setType(VERIFY_TYPE_1);
            processLog.setCreateBy(params.getUserName());
            processLog.setCreateTime(DateUtils.getNowDate());
            addLog(processLog);
        }
        return AjaxResult.success();
    }

    /**
     * 编辑(不修改状态)
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateNotStatus(ProjectImplInfoVO params){
        //基本信息
        ProjectImplBasicInfo basicInfo = params.getBasicInfo();
        ProjectImplBasicInfo pib = super.getById(basicInfo.getId());
        if (!VERIFY_STATUS_PASS.equals(pib.getProjectStatus())){
            return AjaxResult.error("该项目在项目管理中不是审核通过状态，不能操作！");
        }
        boolean result = updateById(basicInfo);
        if (!result){
            return AjaxResult.error();
        }
        //实施信息
        saveImplInfo(params);
        //资金
        saveFundsInfo(params);
        //进展
        saveEvolveInfo(params);
        //附件
        saveFilesInfo(params);
        //VR
        saveVrInfo(params);
        //增加版本
        JSONObject vobj = (JSONObject) JSON.toJSON(basicInfo);
        if (params.getImplInfo()!=null){
            vobj.putAll((JSONObject)JSON.toJSON(params.getImplInfo()));
        }
        if (params.getFundsInfo()!=null){
            vobj.putAll((JSONObject)JSON.toJSON(params.getFundsInfo()));
        }
        if (params.getEvolveInfo()!=null){
            vobj.putAll((JSONObject)JSON.toJSON(params.getEvolveInfo()));
        }
        ProjectImplVersion version = vobj.to(ProjectImplVersion.class);
        version.setProjectId(basicInfo.getId());
        version.setBasicInfo(JSON.toJSONString(params.getBasicInfo()));
        version.setImplInfo(JSON.toJSONString(params.getImplInfo()));
        version.setFundsInfo(JSON.toJSONString(params.getFundsInfo()));
        version.setEvolveInfo(JSON.toJSONString(params.getEvolveInfo()));
        version.setFilesInfo(JSON.toJSONString(params.getFilesInfo()));
        version.setVrInfo(JSON.toJSONString(params.getVrInfo()));
        version.setCreateBy(params.getUserName());
        version.setCreateTime(DateUtils.getNowDate());
        version.setVersionDescription(params.getDescription());
        addVersion(version);
        return AjaxResult.success();
    }

    /**
     * 审核
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult verify(VerifyVO params){
        //根据项目ID查询
        ProjectImplBasicInfo basicInfo = super.getById(params.getId());
        if (basicInfo==null){
            return AjaxResult.error("该项目不存在！");
        }
        String state = basicInfo.getProjectStatus();
        if (Integer.parseInt(state)<=Integer.parseInt(VERIFY_STATUS_PASS)){
            return AjaxResult.error("还未提交审核！");
        }
        String projectNo = basicInfo.getProjectNo();
        String versionDescription = basicInfo.getRemark();
        //项目信息
        basicInfo = new ProjectImplBasicInfo();
        basicInfo.setId(params.getId());
        basicInfo.setUpdateBy(params.getUserName());
        basicInfo.setUpdateTime(DateUtils.getNowDate());
        //日志
        ProjectImplProcessLog processLog = new ProjectImplProcessLog();
        processLog.setProjectNo(projectNo);
        processLog.setDescription(params.getDescription());
        processLog.setCreateBy(params.getUserName());
        processLog.setCreateTime(basicInfo.getUpdateTime());
        //字典处理
        List<SysDictData> dicts = DictUtils.getDictCacheObCc(DICT_KEY_PROJECT_IMPL_STATE);
        Integer index = DictUtils.getVerifyDictIndex(dicts,params.getRoleKeys());
        if (index==null){
            return AjaxResult.error("当前用户无权限审批！");
        }
        SysDictData dict = dicts.get(index);
        processLog.setStep(dict.getDictLabel().substring(1));
        processLog.setDescription(params.getDescription());
        //判断是否通过
        if (params.getPass()){
            if (Integer.parseInt(state)>Integer.parseInt(dict.getDictValue())){
                return AjaxResult.error("当前角色已审批！");
            }
            dict = dicts.get(index+1);
            basicInfo.setProjectStatus(dict.getDictValue());
            processLog.setType(VERIFY_TYPE_2);
            //修改内容
            if (params.getObj()!=null){
                ProjectImplInfoVO piiv = JSON.parseObject(params.getObj().toJSONString(),ProjectImplInfoVO.class);
                updateById(piiv.getBasicInfo());
                //实施信息
                saveImplInfo(piiv);
                //资金
                saveFundsInfo(piiv);
                //进展
                saveEvolveInfo(piiv);
                //附件
                saveFilesInfo(piiv);
                //VR
                saveVrInfo(piiv);
            }
        }else{
            if (Integer.parseInt(state)>(Integer.parseInt(dict.getDictValue())+1)){
                return AjaxResult.error("当前状态无法驳回！");
            }
            basicInfo.setProjectStatus(VERIFY_STATUS_REJECT);
            processLog.setType(VERIFY_TYPE_3);
        }
        //更新信息
        boolean result = updateById(basicInfo);
        if (!result){
            return AjaxResult.error();
        }
        //新增日志
        addLog(processLog);
        //是否为终审
        if (VERIFY_STATUS_PASS.equals(basicInfo.getProjectStatus())){
            //产生版本信息
            ProjectImplInfoVO implInfoVO = getById(params.getId());
            JSONObject vobj = (JSONObject) JSON.toJSON(implInfoVO.getBasicInfo());
            if (implInfoVO.getImplInfo()!=null){
                vobj.putAll((JSONObject)JSON.toJSON(implInfoVO.getImplInfo()));
            }
            if (implInfoVO.getFundsInfo()!=null){
                vobj.putAll((JSONObject)JSON.toJSON(implInfoVO.getFundsInfo()));
            }
            if (implInfoVO.getEvolveInfo()!=null){
                vobj.putAll((JSONObject)JSON.toJSON(implInfoVO.getEvolveInfo()));
            }
            ProjectImplVersion version = vobj.to(ProjectImplVersion.class);
            version.setProjectId(basicInfo.getId());
            version.setBasicInfo(JSON.toJSONString(implInfoVO.getBasicInfo()));
            version.setImplInfo(JSON.toJSONString(implInfoVO.getImplInfo()));
            version.setFundsInfo(JSON.toJSONString(implInfoVO.getFundsInfo()));
            version.setEvolveInfo(JSON.toJSONString(implInfoVO.getEvolveInfo()));
            version.setFilesInfo(JSON.toJSONString(implInfoVO.getFilesInfo()));
            version.setVrInfo(JSON.toJSONString(implInfoVO.getVrInfo()));
            version.setCreateBy(params.getUserName());
            version.setCreateTime(basicInfo.getUpdateTime());
            version.setVersionDescription(versionDescription);
            addVersion(version);
        }
        return AjaxResult.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchVerify(VerifyVO params){
        for (Long id:params.getIds()) {
            params.setId(id);
            verify(params);
        }
    }

    /**
     * 历史列表
     * @param projectId
     * @return
     */
    public JSONArray historyList(Long projectId){
        LambdaQueryWrapper<ProjectImplVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplVersion::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(ProjectImplVersion::getProjectId,projectId);
        wrapper.select(ProjectImplVersion::getId,ProjectImplVersion::getBasicInfo,ProjectImplVersion::getVersionDescription,ProjectImplVersion::getCreateTime);
        wrapper.orderByDesc(ProjectImplVersion::getCreateTime);
        List<ProjectImplVersion> list = versionMapper.selectList(wrapper);
        JSONArray result = new JSONArray();
        if (CollectionUtils.isNotEmpty(list)){
            for (ProjectImplVersion version:list){
                JSONObject obj = JSON.parseObject(version.getBasicInfo());
                obj.put("id",version.getId().toString());
                obj.put("createTime",DateUtils.dateTime(version.getCreateTime()));
                obj.put("remark",version.getVersionDescription());
                obj.put("versionDescription",version.getVersionDescription());
                result.add(obj);
            }
        }
        return result;
    }

    /**
     * 历史明细
     * @param id
     * @return
     */
    public ProjectImplInfoVO getHistoryDetail(Long id){
        ProjectImplVersion version = versionMapper.selectById(id);
        if (version==null){
            return null;
        }
        ProjectImplInfoVO implInfoVO = new ProjectImplInfoVO();
        implInfoVO.setBasicInfo(JSON.parseObject(version.getBasicInfo(),ProjectImplBasicInfo.class));
        implInfoVO.setImplInfo(JSON.parseObject(version.getImplInfo(),ProjectImplStateInfo.class));
        implInfoVO.setFundsInfo(JSON.parseObject(version.getFundsInfo(),ProjectImplFundInfo.class));
        implInfoVO.setEvolveInfo(JSON.parseObject(version.getEvolveInfo(),ProjectImplEvolve.class));
        implInfoVO.setFilesInfo(JSON.parseArray(version.getFilesInfo(), ProjectImplFileTypeVO.class));
        implInfoVO.setVrInfo(JSON.parseArray(version.getVrInfo(),ProjectImplVrFileInfo.class));
        return implInfoVO;
    }

    /**
     * 删除历史记录
     * @param id
     * @return
     */
    public boolean deleteHistory(Long id,String userName){
        ProjectImplVersion params = new ProjectImplVersion();
        params.setId(id);
        params.setDeleteTag(DELETE_TAG_1);
        params.setUpdateBy(userName);
        params.setUpdateTime(DateUtils.getNowDate());
        return versionMapper.updateById(params)>0;
    }

    /**
     * 保存实施信息
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveImplInfo(ProjectImplInfoVO params){
        //数据组装
        ProjectImplStateInfo implInfo = params.getImplInfo();
        if (implInfo==null){
            implInfo = new ProjectImplStateInfo();
        }
        implInfo.setProjectNo(params.getBasicInfo().getProjectNo());
        implInfo.setUpdateBy(params.getUserName());
        implInfo.setUpdateTime(DateUtils.getNowDate());
        if (StringUtils.isBlank(implInfo.getCreateBy())){
            implInfo.setCreateBy(implInfo.getUpdateBy());
            implInfo.setCreateTime(implInfo.getUpdateTime());
        }
        //保存
        if (implInfo.getId() == null){
            stateInfoMapper.insert(implInfo);
        }else{
            stateInfoMapper.updateById(implInfo);
        }
    }

    /**
     * 保存资金信息
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveFundsInfo(ProjectImplInfoVO params){
        //数据处理
        ProjectImplFundInfo fundInfo = params.getFundsInfo();
        if (fundInfo==null){
            fundInfo = new ProjectImplFundInfo();
        }
        String projectNo = params.getBasicInfo().getProjectNo();
        fundInfo.setProjectNo(projectNo);
        fundInfo.setUpdateBy(params.getUserName());
        fundInfo.setUpdateTime(DateUtils.getNowDate());
        if (StringUtils.isBlank(fundInfo.getCreateBy())){
            fundInfo.setCreateBy(fundInfo.getUpdateBy());
            fundInfo.setCreateTime(fundInfo.getUpdateTime());
        }
        //保存
        if (fundInfo.getId() == null){
            fundInfoMapper.insert(fundInfo);
        }else{
            fundInfoMapper.updateById(fundInfo);
        }
        params.setFundsInfo(fundInfo);
        //明细数据处理
        List<ProjectImplFundDetailInfo> fundDetailInfoList = new ArrayList<>();
        JSONObject obj = (JSONObject) JSON.toJSON(fundInfo);
        for (Field field:ProjectImplFundInfo.class.getDeclaredFields()) {
            if (List.class==field.getType()){
                List<ProjectImplFundDetailInfo> fundDetailInfos = obj.getList(field.getName(),ProjectImplFundDetailInfo.class);
                if (CollectionUtils.isNotEmpty(fundDetailInfos)){
                    for (ProjectImplFundDetailInfo fundDetailInfo:fundDetailInfos){
                        fundDetailInfo.setProjectNo(projectNo);
                        fundDetailInfo.setFundType(FundEnum.getValueMap().get(field.getName()));
                    }
                    fundDetailInfoList.addAll(fundDetailInfos);
                }
            }
        }
        //先删除，再新增
        LambdaQueryWrapper<ProjectImplFundDetailInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplFundDetailInfo::getProjectNo,projectNo);
        fundDetailInfoMapper.delete(wrapper);
        if (CollectionUtils.isNotEmpty(fundDetailInfoList)){
            for (ProjectImplFundDetailInfo fundDetailInfo:fundDetailInfoList){
                fundDetailInfoMapper.insert(fundDetailInfo);
            }
        }
    }

    /**
     * 保存进展信息
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveEvolveInfo(ProjectImplInfoVO params){
        //数据处理
        ProjectImplEvolve evolve = params.getEvolveInfo();
        if (evolve==null){
            evolve = new ProjectImplEvolve();
        }
        String projectNo = params.getBasicInfo().getProjectNo();
        evolve.setProjectNo(projectNo);
        evolve.setUpdateBy(params.getUserName());
        evolve.setUpdateTime(DateUtils.getNowDate());
        if (StringUtils.isBlank(evolve.getCreateBy())){
            evolve.setCreateBy(evolve.getUpdateBy());
            evolve.setCreateTime(evolve.getUpdateTime());
        }
        //保存
        if (evolve.getId() == null){
            evolveMapper.insert(evolve);
        }else{
            evolveMapper.updateById(evolve);
        }
        params.setEvolveInfo(evolve);
    }

    /**
     * 保存附件信息
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveFilesInfo(ProjectImplInfoVO params){
        List<ProjectImplFileTypeVO> fileTypes = params.getFilesInfo();
        if (CollectionUtils.isEmpty(fileTypes)){
            return;
        }
        String projectNo = params.getBasicInfo().getProjectNo();
        List<ProjectImplFileInfo> list = new ArrayList<>();
        for (ProjectImplFileTypeVO fileTypeVO:fileTypes){
            List<ProjectImplFileInfo> fileList = fileTypeVO.getFileList();
            if (CollectionUtils.isNotEmpty(fileList)){
                for (ProjectImplFileInfo fileInfo:fileList) {
                    fileInfo.setType(fileTypeVO.getTypeValue());
                    list.add(fileInfo);
                }
            }
        }
        //先删除，再新增
        LambdaQueryWrapper<ProjectImplFileInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplFileInfo::getProjectNo,projectNo);
        fileInfoMapper.delete(wrapper);
        if (CollectionUtils.isNotEmpty(list)){
            for (ProjectImplFileInfo fileInfo:list){
                fileInfo.setProjectNo(projectNo);
                fileInfoMapper.insert(fileInfo);
            }
        }
    }

    /**
     * 保存VR
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveVrInfo(ProjectImplInfoVO params){
        String projectNo = params.getBasicInfo().getProjectNo();
        List<ProjectImplVrFileInfo> vrFileInfos = params.getVrInfo();
        //先删除，再新增
        LambdaQueryWrapper<ProjectImplVrFileInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplVrFileInfo::getProjectNo,projectNo);
        vrFileInfoMapper.delete(wrapper);
        if (CollectionUtils.isNotEmpty(vrFileInfos)){
            for (ProjectImplVrFileInfo vrFileInfo:vrFileInfos){
                vrFileInfo.setProjectNo(projectNo);
                vrFileInfo.setCreateBy(params.getUserName());
                vrFileInfo.setCreateTime(DateUtils.getNowDate());
                vrFileInfoMapper.insert(vrFileInfo);
            }
        }
    }

    /**
     * 保存版本
     * @param version
     */
    @Transactional(rollbackFor = Exception.class)
    public void addVersion(ProjectImplVersion version){
        version.setId(null);
        version.setUpdateBy(null);
        version.setUpdateTime(null);
        version.setRemark(null);
        versionMapper.insert(version);
    }

    /**
     * 保存日志
     * @param log
     */
    @Transactional(rollbackFor = Exception.class)
    public void addLog(ProjectImplProcessLog log){
        String deptName = SecurityUtils.getLoginUser().getUser().getDept().getDeptName();
        log.setCreateBy(StringUtils.join(deptName,"-",log.getCreateBy()));
        processLogMapper.insert(log);
    }

    /**
     * 获取最新版本数据
     * @param projectNo
     * @return
     */
    public ProjectImplVersion getNewVersion(String projectNo){
        return versionMapper.selectNewOne(projectNo);
    }

    /**
     * 根据项目编号获取压缩文件列表
     * @param params
     * @return
     */
    public List<ZipVO> getZipFiles(ProjectFileDownloadVO params){
        return fileInfoMapper.selectByProjectNo(params);
    }

    /**
     * 获取项目地理位置信息
     * @return
     */
    public List<ProjectImplVersion> getGPData(){
        return versionMapper.selectGPData();
    }

    /**
     * 获取所有项目编号和名称
     * @return
     */
    public List<ProjectImplBasicInfo> getProjectAll(){
        LambdaQueryWrapper<ProjectImplBasicInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplBasicInfo::getDeleteTag,DELETE_TAG_0);
        wrapper.select(ProjectImplBasicInfo::getProjectNo,ProjectImplBasicInfo::getProjectName,ProjectImplBasicInfo::getDeptId);
        return super.baseMapper.selectList(wrapper);
    }

    /**
     * 根据项目编号获取名称
     * @return
     */
    public ProjectImplBasicInfo getProjectNameByNo(String projectNo){
        LambdaQueryWrapper<ProjectImplBasicInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplBasicInfo::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(ProjectImplBasicInfo::getProjectNo,projectNo);
        wrapper.select(ProjectImplBasicInfo::getProjectNo,ProjectImplBasicInfo::getProjectName,ProjectImplBasicInfo::getUserId);
        return super.baseMapper.selectOne(wrapper);
    }

    /**
     * 实施信息
     * @param projectNo
     * @return
     */
    private ProjectImplStateInfo getImplInfo(String projectNo){
        LambdaQueryWrapper<ProjectImplStateInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplStateInfo::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(ProjectImplStateInfo::getProjectNo,projectNo);
        ProjectImplStateInfo info = stateInfoMapper.selectOne(wrapper);
        if (info==null){
            return null;
        }
        //附件处理
        JSONObject obj = (JSONObject) JSON.toJSON(info);
        for (Field field:ProjectImplStateInfo.class.getDeclaredFields()) {
            if (List.class==field.getType()){
                List<ProjectImplFileInfo> files = getFileList(projectNo,field.getName(),"implInfo",null);
                obj.put(field.getName(),files);
            }
        }
        info = obj.to(ProjectImplStateInfo.class);
        return info;
    }

    /**
     * 资金信息
     * @param projectNo
     * @return
     */
    private ProjectImplFundInfo getFundsInfo(String projectNo){
        LambdaQueryWrapper<ProjectImplFundInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplFundInfo::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(ProjectImplFundInfo::getProjectNo,projectNo);
        ProjectImplFundInfo info = fundInfoMapper.selectOne(wrapper);
        if (info==null){
            return new ProjectImplFundInfo().init();
        }
        //明细及附件处理
        JSONObject obj = (JSONObject) JSON.toJSON(info);
        for (Field field:ProjectImplFundInfo.class.getDeclaredFields()) {
            if (List.class==field.getType()){
                List<ProjectImplFundDetailInfo> fundDetails = getFundDetailListByFundType(projectNo, FundEnum.getValueMap().get(field.getName()));
                obj.put(field.getName(),fundDetails);
            }
        }
        info = obj.to(ProjectImplFundInfo.class);
        return info;
    }

    /**
     * 获取进展信息
     * @param projectNo
     * @return
     */
    private ProjectImplEvolve getEvolveInfo(String projectNo){
        LambdaQueryWrapper<ProjectImplEvolve> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplEvolve::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(ProjectImplEvolve::getProjectNo,projectNo);
        return evolveMapper.selectOne(wrapper);
    }

    /**
     * 获取附件信息
     * @param projectNo
     * @return
     */
    private List<ProjectImplFileTypeVO> getFilesInfo(String projectNo){
        List<ProjectImplFileTypeVO> list = new ArrayList<>();
        //查询字典文件分类
        List<SysDictData> dicts = dictTypeService.selectDictDataByType("ziliaoleixing");
        if (CollectionUtils.isEmpty(dicts)){
            return list;
        }
        for (SysDictData dict:dicts){
            ProjectImplFileTypeVO fileType = new ProjectImplFileTypeVO();
            fileType.setTypeName(dict.getDictLabel());
            fileType.setTypeValue(dict.getDictValue());
            fileType.setFileList(getFileList(projectNo,null,null,dict.getDictValue()));
            list.add(fileType);
        }
        return list;
    }

    /**
     * 获取VR信息
     * @param projectNo
     * @return
     */
    private List<ProjectImplVrFileInfo> getVrInfo(String projectNo){
        LambdaQueryWrapper<ProjectImplVrFileInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplVrFileInfo::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(ProjectImplVrFileInfo::getProjectNo,projectNo);
        return vrFileInfoMapper.selectList(wrapper);
    }

    /**
     * 获取CCTV信息
     * @param projectNo
     * @return
     */
    private List<ProjectImplCctv> getCCTV(String projectNo){
        LambdaQueryWrapper<ProjectImplCctv> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplCctv::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(ProjectImplCctv::getProjectNo,projectNo);
        return cctvMapper.selectList(wrapper);
    }

    /**
     * 获取资金明细
     * @param projectNo
     * @param fundType
     * @return
     */
    private List<ProjectImplFundDetailInfo> getFundDetailListByFundType(String projectNo,Integer fundType){
        LambdaQueryWrapper<ProjectImplFundDetailInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplFundDetailInfo::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(ProjectImplFundDetailInfo::getProjectNo,projectNo);
        wrapper.eq(ProjectImplFundDetailInfo::getFundType,fundType);
        return fundDetailInfoMapper.selectList(wrapper);
    }

    /**
     * 获取文件列表
     * @param projectNo
     * @param formType
     * @param tabType
     * @param type
     * @return
     */
    private List<ProjectImplFileInfo> getFileList(String projectNo,String formType,String tabType,String type){
        LambdaQueryWrapper<ProjectImplFileInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplFileInfo::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(ProjectImplFileInfo::getProjectNo,projectNo);
        if (StringUtils.isNotEmpty(tabType)){
            wrapper.eq(ProjectImplFileInfo::getTabType,tabType);
        }
        if (StringUtils.isNotEmpty(formType)){
            wrapper.eq(ProjectImplFileInfo::getFormType,formType);
        }
        if (StringUtils.isNotEmpty(type)){
            wrapper.eq(ProjectImplFileInfo::getType,type);
        }
        return fileInfoMapper.selectList(wrapper);
    }

    /**
     * 导出
     * @return
     */
    public List<ProjectImplVersion> export(){
        //获取数据
        List<ProjectImplVersion> dataList = versionMapper.selectNewData();
        if (StringUtils.isEmpty(dataList)){
            return null;
        }
        //获取字典数据
        Map<String,ProjectDictInfo> dictMap = dictInfoService.getMapByNo("03");
        //循环处理数据
        int index = 1;
        for (ProjectImplVersion version:dataList) {
            //编号
            version.setNo(index);
            index++;
            //145规划类别
            ProjectDictInfo dict3 = dictMap.get(version.getType145());
            ProjectDictInfo dict2 = dictMap.get(dict3.getPid());
            ProjectDictInfo dict1 = dictMap.get(dict2.getPid());
            version.setField1(dict1.getName());
            version.setField2(dict2.getName());
            version.setField3(dict3.getName());
            //项目类别
            version.setField4(version.getProjectCategory()==0?"非工程建设类":"工程建设类");
            //建设状态
            switch (version.getBuildStatus()){
                case BUILD_TYPE_1:
                    version.setField5("未开工");
                    break;
                case BUILD_TYPE_2:
                    version.setField5("在建");
                    break;
                case BUILD_TYPE_3:
                    version.setField5("完建");
                    break;
                case BUILD_TYPE_4:
                    version.setField5("验收");
                    break;
                case BUILD_TYPE_5:
                    version.setField5("取消");
                    break;
                default:break;
            }
            //计划建设周期
            List<Integer> years = new ArrayList<>();
            if (version.getPlanBuildCycleBeginYear()!=null){
                years.add(version.getPlanBuildCycleBeginYear());
            }

            if (version.getPlanBuildCycleEndYear()!=null){
                years.add(version.getPlanBuildCycleEndYear());
            }
            if (StringUtils.isNotEmpty(years)){
                version.setField6(StringUtils.join(years,"-"));
            }
        }
        return dataList;
    }

    /**
     * 撤回
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult rollback(VerifyVO params){
        //获取详情
        ProjectImplBasicInfo basicInfo = super.getById(params.getId());
        if (basicInfo==null){
            return AjaxResult.error("数据不存在");
        }
        //获取审核状态字典
        List<SysDictData> dicts = DictUtils.getDictCacheObCc(DICT_KEY_PROJECT_IMPL_STATE);
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
        if (!basicInfo.getProjectStatus().equals(dict.getDictValue())){
            return AjaxResult.error(StringUtils.join("项目库当前不是",dict.getDictLabel(),"状态，无法撤回！"));
        }
        //修改项目库状态为草稿
        ProjectImplBasicInfo up = new ProjectImplBasicInfo();
        up.setId(params.getId());
        up.setProjectStatus(VERIFY_STATUS_DRAFT);
        up.setUpdateBy(params.getUserName());
        up.setUpdateTime(DateUtils.getNowDate());
        if (updateById(up)){
            //增加日志
            ProjectImplProcessLog processLog = new ProjectImplProcessLog();
            processLog.setProjectNo(basicInfo.getProjectNo());
            processLog.setStep("撤回申请");
            processLog.setType(VERIFY_TYPE_5);
            processLog.setCreateBy(params.getUserName());
            processLog.setCreateTime(up.getUpdateTime());
            addLog(processLog);
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    @Transactional(rollbackFor = Exception.class)
    public void repairData(){
        //查询所有版本数据
        List<ProjectImplVersion> list = versionMapper.selectList(new LambdaQueryWrapper<>());
        for (ProjectImplVersion version:list){
            ProjectImplBasicInfo info = JSON.parseObject(version.getBasicInfo(),ProjectImplBasicInfo.class);
            ProjectImplBasicInfo bi = super.getById(info.getId());
            info.setUnitName(bi.getUnitName());
            ProjectImplVersion updateVersion = new ProjectImplVersion();
            updateVersion.setId(version.getId());
            updateVersion.setBasicInfo(JSON.toJSONString(info));
            versionMapper.updateById(updateVersion);
        }

    }



}
