package com.bcsd.project.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.common.annotation.DataScope;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.domain.entity.SysDept;
import com.bcsd.common.core.domain.entity.SysDictData;
import com.bcsd.common.utils.ArithUtil;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.DictUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.constants.Constants;
import com.bcsd.project.domain.ProjectImplFileInfo;
import com.bcsd.project.domain.ProjectImplFundDetailInfo;
import com.bcsd.project.domain.ProjectImplFundInfo;
import com.bcsd.project.domain.ProjectImplVersion;
import com.bcsd.project.domain.vo.ArchiveInfoVO;
import com.bcsd.project.domain.vo.ArchiveProjectVO;
import com.bcsd.project.domain.vo.ProjectImplFileTypeVO;
import com.bcsd.project.enums.FundEnum;
import com.bcsd.project.mapper.ProjectDictInfoMapper;
import com.bcsd.project.mapper.ProjectImplFileInfoMapper;
import com.bcsd.project.mapper.ProjectImplVersionMapper;
import com.bcsd.system.service.ISysDeptService;
import com.bcsd.system.service.ISysDictTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.bcsd.project.constants.Constants.DELETE_TAG_0;

@Slf4j
@Service
public class ProjectImplVersionService extends ServiceImpl<ProjectImplVersionMapper, ProjectImplVersion> implements IService<ProjectImplVersion> {

    @Autowired
    ISysDeptService deptService;
    @Autowired
    ProjectDictInfoMapper dictInfoMapper;
    @Autowired
    private ISysDictTypeService dictTypeService;
    @Autowired
    ProjectImplFileInfoMapper fileInfoMapper;

    /**
     * 导入数据
     * @param data
     * @param userName
     * @return
     */
    public AjaxResult importData(List<ArchiveProjectVO> data,String userName){
        //数据转换
        List<ProjectImplVersion> list = new ArrayList<>();
        Set<String> projectNos = new HashSet<>();
        Date curDate = DateUtils.getNowDate();
        //分析组装数据
        for (ArchiveProjectVO vo:data) {
            ProjectImplVersion piv = new ProjectImplVersion();
            BeanUtils.copyProperties(vo,piv);
            //检查项目编号是否为空
            if (StringUtils.isBlank(piv.getProjectNo())){
                return AjaxResult.error("有项目编号为空，请检查数据");
            }
            if (projectNos.contains(piv.getProjectNo())){
                return AjaxResult.error("导入数据中存在相同编号的项目:"+piv.getProjectNo());
            }
            projectNos.add(piv.getProjectNo());
            //检查项目名称是否为空
            if (StringUtils.isBlank(piv.getProjectName())){
                return AjaxResult.error("有项目名称为空，请检查数据");
            }
            //检查项目是否存在
            if (exist(piv.getProjectNo())){
                return AjaxResult.error("项目编号（"+piv.getProjectNo()+"）的项目已存在，请检查数据");
            }
            //经纬度处理
            if(StringUtils.isNotBlank(vo.getLongitude())&&StringUtils.isNotBlank(vo.getLatitude())){
                String geographyPosition = StringUtils.join("经度:",vo.getLongitude(),";纬度:",vo.getLatitude());
                piv.setGeographyPosition(geographyPosition);
            }
            //实施状态(未开工、在建、完建、验收)
            if (StringUtils.isNotBlank(vo.getStatus())){
                String status = DictUtils.getDictValue("jianshezhuangtai",vo.getStatus());
                if (StringUtils.isNotBlank(status)){
                    piv.setBuildStatus(Integer.valueOf(status));
                }
            }
            //规划类别处理,判断开始年，2021年之前使用规划优化类别，2021之后使用145规划优化类别
            if (StringUtils.isNotBlank(vo.getBeginYear())){
                //开始年份
                Integer beginYear = Integer.parseInt(vo.getBeginYear());
                //类别
                List<String> typeList = StringUtils.getList(vo.getType1(),vo.getType2());
                if (StringUtils.isNotEmpty(typeList)){
                    if (beginYear<2021){
                        piv.setPlanOptimizeType(dictInfoMapper.getTypeByName(typeList));
                    }else{
                        piv.setType145(dictInfoMapper.get145TypeByName(typeList));
                    }
                }
                //计划年度
                piv.setFirstArrangeFundYear(beginYear);
                piv.setPlanBuildCycleBeginYear(beginYear);
                piv.setPlanBuildCycleEndYear(Integer.parseInt(vo.getEndYear()));

            }
            //下达专项资金处理
            piv.setFundsInfo(initFund(vo,piv));
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
            piv.setCityDistrict(dept.getCityCode());
            piv.setDeptId(dept.getDeptId());
            //填充数据
            piv.setDataSource(1);
            piv.setCreateBy(userName);
            piv.setCreateTime(curDate);
            list.add(piv);
        }
        //保存数据
        saveBatch(list);
        return AjaxResult.success();
    }

    /**
     * 初始化资金
     * @param vo
     * @return
     */
    private String initFund(ArchiveProjectVO vo,ProjectImplVersion piv){
        ProjectImplFundInfo fundInfo = new ProjectImplFundInfo();
        List<ProjectImplFundDetailInfo> list = new ArrayList<>();
        String total = initFundDetail(list,vo.getProjectNo(),vo.getFund2011(),"2011");
        total = ArithUtil.adds(total,initFundDetail(list,vo.getProjectNo(),vo.getFund2012(),"2012"));
        total = ArithUtil.adds(total,initFundDetail(list,vo.getProjectNo(),vo.getFund2013(),"2013"));
        total = ArithUtil.adds(total,initFundDetail(list,vo.getProjectNo(),vo.getFund2014(),"2014"));
        total = ArithUtil.adds(total,initFundDetail(list,vo.getProjectNo(),vo.getFund2015(),"2015"));
        total = ArithUtil.adds(total,initFundDetail(list,vo.getProjectNo(),vo.getFund2016(),"2016"));
        total = ArithUtil.adds(total,initFundDetail(list,vo.getProjectNo(),vo.getFund2017(),"2017"));
        total = ArithUtil.adds(total,initFundDetail(list,vo.getProjectNo(),vo.getFund2018(),"2018"));
        total = ArithUtil.adds(total,initFundDetail(list,vo.getProjectNo(),vo.getFund2019(),"2019"));
        total = ArithUtil.adds(total,initFundDetail(list,vo.getProjectNo(),vo.getFund2020(),"2020"));
        total = ArithUtil.adds(total,initFundDetail(list,vo.getProjectNo(),vo.getFund2021(),"2021"));
        total = ArithUtil.adds(total,initFundDetail(list,vo.getProjectNo(),vo.getFund2022(),"2022"));
        total = ArithUtil.adds(total,initFundDetail(list,vo.getProjectNo(),vo.getFund2023(),"2023"));
        fundInfo.setPlanPcIssueFund(total);
        fundInfo.setPlanPcIssueFundList(list);
        fundInfo.setCompleteTotalFund(vo.getCompleteTotalFund());
        fundInfo.setCompleteFund(vo.getCompleteFund());
        fundInfo.setPaymentFund(vo.getPaymentFund());
        fundInfo.setUseFund(vo.getUseFund());
        piv.setPlanPcIssueFund(total);
        return JSON.toJSONString(fundInfo);
    }

    /**
     * 初始化资金明细
     * @param list
     * @param projectNo
     * @param amount
     * @param year
     * @return
     */
    private String initFundDetail(List<ProjectImplFundDetailInfo> list,String projectNo,String amount,String year){
        if (StringUtils.isNotBlank(amount)){
            ProjectImplFundDetailInfo fundDetailInfo = new ProjectImplFundDetailInfo();
            fundDetailInfo.setProjectNo(projectNo);
            fundDetailInfo.setFundTotal(amount);
            fundDetailInfo.setMonth12(amount);
            fundDetailInfo.setFundYear(year);
            fundDetailInfo.setFundType(FundEnum.FUND_TYPE_1.getCode());
            list.add(fundDetailInfo);
            return amount;
        }else{
            return "0";
        }
    }

    /**
     * 是否存在
     * @param projectNo
     * @return
     */
    private boolean exist(String projectNo){
        LambdaQueryWrapper<ProjectImplVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplVersion::getDeleteTag, Constants.DELETE_TAG_0);
        wrapper.eq(ProjectImplVersion::getProjectNo,projectNo);
        return count(wrapper)>0;
    }

    /**
     * 列表分页
     * @param params
     * @return
     */
    @DataScope(deptAlias = "piv")
    public List<ProjectImplVersion> listPage(ProjectImplVersion params){
        return this.baseMapper.listPage(params);
    }

    /**
     * 获取详情
     * @param id
     * @return
     */
    public ArchiveInfoVO getInfo(Long id){
        ArchiveInfoVO info = new ArchiveInfoVO();
        ProjectImplVersion piv = getById(id);
        if (piv!=null){
            info.setBasicInfo(piv);
            if (StringUtils.isNotBlank(piv.getFundsInfo())){
                info.setFundsInfo(JSON.parseObject(piv.getFundsInfo(),ProjectImplFundInfo.class));
            }
            info.setFilesInfo(getFilesInfo(piv.getProjectNo()));
        }
        return info;
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
     * 获取文件列表
     * @param projectNo
     * @param formType
     * @param tabType
     * @param type
     * @return
     */
    private List<ProjectImplFileInfo> getFileList(String projectNo, String formType, String tabType, String type){
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
     * 修改信息
     * @param params
     * @param userName
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInfo(JSONObject params,String userName){
        ProjectImplVersion piv = params.to(ProjectImplVersion.class);
        piv.setUpdateBy(userName);
        piv.setUpdateTime(DateUtils.getNowDate());
        //资金信息处理
        piv.setFundsInfo(JSON.toJSONString(params.to(ProjectImplFundInfo.class)));
        //保存附件
        saveFilesInfo(params.to(ArchiveInfoVO.class).getFilesInfo(),piv.getProjectNo());
        //更新信息
        return updateById(piv);
    }

    /**
     * 保存附件信息
     * @param fileTypes
     * @param projectNo
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveFilesInfo(List<ProjectImplFileTypeVO> fileTypes,String projectNo){
        //先删除，再新增
        LambdaQueryWrapper<ProjectImplFileInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplFileInfo::getProjectNo,projectNo);
        fileInfoMapper.delete(wrapper);
        if (CollectionUtils.isEmpty(fileTypes)){
            return;
        }
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
        if (CollectionUtils.isNotEmpty(list)){
            for (ProjectImplFileInfo fileInfo:list){
                fileInfo.setProjectNo(projectNo);
                fileInfoMapper.insert(fileInfo);
            }
        }
    }

    /**
     * 删除
     * @param projectNo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String projectNo){
        //删除附件
        LambdaQueryWrapper<ProjectImplFileInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplFileInfo::getProjectNo,projectNo);
        fileInfoMapper.delete(wrapper);
        //删除项目信息
        LambdaQueryWrapper<ProjectImplVersion> del = new LambdaQueryWrapper<>();
        del.eq(ProjectImplVersion::getProjectNo,projectNo);
        return remove(del);
    }









}
