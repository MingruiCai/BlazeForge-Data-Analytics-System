package com.bcsd.project.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.common.annotation.DataScope;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.domain.TreeSelect;
import com.bcsd.common.core.domain.entity.SysDept;
import com.bcsd.common.utils.ArithUtil;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.constants.Constants;
import com.bcsd.project.domain.*;
import com.bcsd.project.domain.vo.LedgerProgressVO;
import com.bcsd.project.enums.FundEnum;
import com.bcsd.project.mapper.*;
import com.bcsd.system.service.ISysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class ProgressLedgerService extends ServiceImpl<LedgerDistrictProgressMapper, LedgerDistrictProgress> implements IService<LedgerDistrictProgress> {

    @Autowired
    LedgerDistrictProjectProgressMapper projectProgressMapper;
    @Autowired
    LedgerDistrictProgressLogMapper logMapper;
    @Autowired
    LedgerProvinceProgressMapper provinceProgressMapper;
    @Autowired
    ProjectImplVersionMapper versionMapper;
    @Autowired
    ProjectImplBasicInfoMapper basicInfoMapper;
    @Autowired
    ISysDeptService deptService;

    /**
     * 初始化数据
     * @param params
     * @return
     */
    public List<LedgerDistrictProjectProgress> initData(LedgerDistrictProgress params){
        List<LedgerDistrictProjectProgress> list = new ArrayList<>();
        //查询实施数据
        List<ProjectImplVersion> dataList = versionMapper.selectNewDataByPlanYearAndDeptId(params.getDataYear().toString(),params.getDeptId());
        if(StringUtils.isEmpty(dataList)){
            return list;
        }
        //数据处理
        for (ProjectImplVersion version:dataList) {
            LedgerDistrictProjectProgress ldpp = new LedgerDistrictProjectProgress(0L,version.getProjectNo());
            BeanUtils.copyProperties(version,ldpp);
            ldpp.setId(null);
            //开工时间或预计开工时间
            String beginDate = version.getSwSwd();
            ldpp.setBeginDate(StringUtils.isBlank(beginDate)?version.getPlanBeginDate():beginDate);
            //本月开工日期
            if (StringUtils.isNotEmpty(beginDate)){
                Date openDate = DateUtils.parseDate(beginDate);
                if (params.getDataYear().equals(DateUtils.getDateYear(openDate))&&params.getDataWeek().equals(DateUtils.getDateMonth(openDate))){
                    ldpp.setMonthBeginDate(beginDate);
                }
            }
            //项目总投资-专项资金
            ldpp.setProjectTotalInvestSpecialFund(version.getPlanFinalIssueFund());
            //年度计划投资
            ldpp.setYearPlanInvest(version.getYearPlanCompletedInvest());
            //获取资金详情
            ProjectImplFundInfo fundInfo = JSON.parseObject(version.getFundsInfo(),ProjectImplFundInfo.class);
            if (fundInfo!=null){
                //资金处理
                fundHandle(fundInfo,ldpp,params);
            }
            //年度建设任务
            ProjectImplBasicInfo basicInfo = JSON.parseObject(version.getBasicInfo(),ProjectImplBasicInfo.class);
            if (basicInfo!=null){
                JSONArray tasks = JSON.parseArray(basicInfo.getYearBuildTask());
                if (StringUtils.isNotEmpty(tasks)){
                    JSONObject task = tasks.getJSONObject(tasks.size()-1);
                    ldpp.setYearBuildTask(task.getString("text"));
                }
            }
            list.add(ldpp);
        }
        return list;
    }

    /**
     * 资金处理
     * @param fundInfo
     * @param ldpp
     * @param params
     */
    private void fundHandle(ProjectImplFundInfo fundInfo,LedgerDistrictProjectProgress ldpp,LedgerDistrictProgress params){
        List<ProjectImplFundDetailInfo> detailInfos = getDetailInfos(fundInfo);
        for (ProjectImplFundDetailInfo detailInfo:detailInfos){
            Integer year = Integer.valueOf(detailInfo.getFundYear());
            Map<Integer,String> monthMap = getMonthFund(detailInfo);
            switch (FundEnum.getEnumMap().get(detailInfo.getFundType())){
                case FUND_TYPE_1:
                case FUND_TYPE_2:
                case FUND_TYPE_4:
                    if (year.equals(params.getDataYear())){
                        /*年度计划投资-专项资金*/
                        ldpp.setYearPlanInvestSpecialFund(ArithUtil.add(ldpp.getYearPlanInvestSpecialFund(),detailInfo.getFundTotal()));
                    }
                    break;
                case FUND_TYPE_5://拨付-专项
                    if (year.equals(params.getDataYear())){
                        for (int i = 1; i < params.getDataWeek(); i++) {
                            ldpp.setPayFund(ArithUtil.add(ldpp.getPayFund(),monthMap.get(i)));
                            ldpp.setPaySpecialFund(ArithUtil.add(ldpp.getPaySpecialFund(),monthMap.get(i)));
                        }
                        ldpp.setAddPayFund(ArithUtil.add(ldpp.getAddPayFund(),monthMap.get(params.getDataWeek())));
                        ldpp.setAddPaySpecialFund(ArithUtil.add(ldpp.getAddPaySpecialFund(),monthMap.get(params.getDataWeek())));
                    }
                    break;
                case FUND_TYPE_6://拨付-配套
                    if (year.equals(params.getDataYear())){
                        for (int i = 1; i < params.getDataWeek(); i++) {
                            ldpp.setPayFund(ArithUtil.add(ldpp.getPayFund(),monthMap.get(i)));
                        }
                        ldpp.setAddPayFund(ArithUtil.add(ldpp.getAddPayFund(),monthMap.get(params.getDataWeek())));
                    }
                    break;
                case FUND_TYPE_7://完成-专项
                    if (year.equals(params.getDataYear())){
                        for (int i = 1; i < params.getDataWeek(); i++) {
                            ldpp.setCompletedInvest(ArithUtil.add(ldpp.getCompletedInvest(),monthMap.get(i)));
                            ldpp.setCompletedInvestSpecialFund(ArithUtil.add(ldpp.getCompletedInvestSpecialFund(),monthMap.get(i)));
                        }
                        ldpp.setAddCompletedInvest(ArithUtil.add(ldpp.getAddCompletedInvest(),monthMap.get(params.getDataWeek())));
                        ldpp.setAddCompletedInvestSpecialFund(ArithUtil.add(ldpp.getAddCompletedInvestSpecialFund(),monthMap.get(params.getDataWeek())));
                    }
                    break;
                case FUND_TYPE_8://完成-配套
                    if (year.equals(params.getDataYear())){
                        for (int i = 1; i < params.getDataWeek(); i++) {
                            ldpp.setCompletedInvest(ArithUtil.add(ldpp.getCompletedInvest(),monthMap.get(i)));
                        }
                        ldpp.setAddCompletedInvest(ArithUtil.add(ldpp.getAddCompletedInvest(),monthMap.get(params.getDataWeek())));
                    }
                    break;
                default:break;
            }
        }
        //百分比
        ldpp.initRate();
    }

    /**
     * 获取详细资金列表
     * @param fundInfo
     * @return
     */
    private List<ProjectImplFundDetailInfo> getDetailInfos(ProjectImplFundInfo fundInfo){
        List<ProjectImplFundDetailInfo> detailInfos = new ArrayList<>();
        if (StringUtils.isNotEmpty(fundInfo.getPlanPcIssueFundList())){
            detailInfos.addAll(fundInfo.getPlanPcIssueFundList());
        }
        if (StringUtils.isNotEmpty(fundInfo.getPlanDcIssueFundAdjustmentAmountList())){
            detailInfos.addAll(fundInfo.getPlanDcIssueFundAdjustmentAmountList());
        }
        if (StringUtils.isNotEmpty(fundInfo.getPlanPcIssueFundAdjustmentAmountList())){
            detailInfos.addAll(fundInfo.getPlanPcIssueFundAdjustmentAmountList());
        }
        if (StringUtils.isNotEmpty(fundInfo.getPaymentFundList())){
            detailInfos.addAll(fundInfo.getPaymentFundList());
        }
        if (StringUtils.isNotEmpty(fundInfo.getPaymentAssortFundList())){
            detailInfos.addAll(fundInfo.getPaymentAssortFundList());
        }
        if (StringUtils.isNotEmpty(fundInfo.getCompleteFundList())){
            detailInfos.addAll(fundInfo.getCompleteFundList());
        }
        if (StringUtils.isNotEmpty(fundInfo.getCompleteAssortFundList())){
            detailInfos.addAll(fundInfo.getCompleteAssortFundList());
        }
        return detailInfos;
    }

    /**
     * 获取月度资金
     * @param detailInfo
     * @return
     */
    private Map<Integer,String> getMonthFund(ProjectImplFundDetailInfo detailInfo){
        Map<Integer,String> map = new HashMap<>();
        map.put(1,detailInfo.getMonth1());
        map.put(2,detailInfo.getMonth2());
        map.put(3,detailInfo.getMonth3());
        map.put(4,detailInfo.getMonth4());
        map.put(5,detailInfo.getMonth5());
        map.put(6,detailInfo.getMonth6());
        map.put(7,detailInfo.getMonth7());
        map.put(8,detailInfo.getMonth8());
        map.put(9,detailInfo.getMonth9());
        map.put(10,detailInfo.getMonth10());
        map.put(11,detailInfo.getMonth11());
        map.put(12,detailInfo.getMonth12());
        return map;
    }

    /**
     * 区县分页列表
     * @param params
     * @return
     */
    @DataScope(deptAlias = "t1",userAlias = "t1")
    public List<LedgerDistrictProgress> districtList(LedgerDistrictProgress params){
        return super.baseMapper.listPage(params);
    }

    /**
     * 获取区县详情
     * @param id
     * @return
     */
    public LedgerDistrictProgress getDistrictInfoById(Long id){
        LedgerDistrictProgress ldp = getById(id);
        if (ldp!=null){
            ldp.setProjectProgressList(getProjectProgressList(id));
            ldp.setLogs(logMapper.selectByLDPId(id));
        }
        return ldp;
    }

    /**
     * 项目进度列表
     * @param id
     * @return
     */
    private List<LedgerDistrictProjectProgress> getProjectProgressList(Long id){
        LambdaQueryWrapper<LedgerDistrictProjectProgress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LedgerDistrictProjectProgress::getDeleteTag,Constants.DELETE_TAG_0);
        wrapper.eq(LedgerDistrictProjectProgress::getLedgerDistrictProgressId,id);
        return projectProgressMapper.selectList(wrapper);
    }

    /**
     * 省分页列表
     * @param params
     * @return
     */
    public List<LedgerProvinceProgress> provinceList(LedgerProvinceProgress params){
        return provinceProgressMapper.listPage(params);
    }

    /**
     * 获取省台账详情
     * @param id
     * @return
     */
    public LedgerProvinceProgress getProvinceInfoById(Long id){
        LedgerProvinceProgress lpp = provinceProgressMapper.selectById(id);
        if (lpp!=null){
            //获取汇总信息
            lpp.setLpdpList(dataSummary(lpp));
        }
        return lpp;
    }

    /**
     * 汇总
     * @param params
     * @return
     */
    public List<LedgerDistrictProjectProgress> dataSummary(LedgerProvinceProgress params){
        //获取数据列表
        List<LedgerDistrictProjectProgress> dataList = projectProgressMapper.selectByDateTime(params);
        if (StringUtils.isEmpty(dataList)){
            return null;
        }
        //获取所有区县
        List<TreeSelect> cityList = deptService.selectCityTreeList(new SysDept(1));
        LedgerDistrictProjectProgress total = new LedgerDistrictProjectProgress("总计");
        List<LedgerDistrictProjectProgress> treeList = initTree(cityList,dataList,total);
        treeList.add(0,total);
        return treeList;
    }

    /**
     * 初始化树结构
     * @param cityList
     * @param dataList
     * @param total
     * @return
     */
    private List<LedgerDistrictProjectProgress> initTree(List<TreeSelect> cityList, List<LedgerDistrictProjectProgress> dataList,LedgerDistrictProjectProgress total){
        if (StringUtils.isEmpty(cityList)){
            return null;
        }
        List<LedgerDistrictProjectProgress> list = new ArrayList<>();
        for (TreeSelect tree:cityList){
            LedgerDistrictProjectProgress city = new LedgerDistrictProjectProgress(tree.getLabel());
            List<LedgerDistrictProjectProgress> children = null;
            if (StringUtils.isEmpty(tree.getChildren())){
                children = new ArrayList<>();
            }
            for (LedgerDistrictProjectProgress vo:dataList) {
                if (Arrays.asList(vo.getDeptIds().split(",")).contains(tree.getId().toString())){
                    city.add(vo);
                    if (children!=null){
                        vo.setName(vo.getProjectName());
                        children.add(vo);
                        total.add(vo);
                    }
                }
            }
            if (!"0".equals(city.getProjectTotalInvest())){
                list.add(city);
                city.setChildren(initTree(tree.getChildren(),dataList,total));
                if (StringUtils.isNotEmpty(children)){
                    city.setChildren(children);
                }
            }
        }
        return list;
    }

    /**
     * 导出
     * @param id
     * @return
     */
    public JSONObject export(Long id){
        //返回结果
        JSONObject result = new JSONObject();
        //查询数据
        LedgerProvinceProgress lpp = getProvinceInfoById(id);
        if (lpp==null){
            return null;
        }
        List<LedgerDistrictProjectProgress> dataList = lpp.getLpdpList();
        //数据处理
        result.put("year",lpp.getDataYear());
        result.put("month",lpp.getDataWeek());
        if (StringUtils.isEmpty(dataList)){
            return result;
        }
        //合计
        LedgerDistrictProjectProgress total = dataList.get(0);
        result.putAll(JSON.parseObject(JSON.toJSONString(total)));
        dataList.remove(0);
        //列表
        List<LedgerDistrictProjectProgress> list = new ArrayList<>();
        treeToList(list,dataList,1);
        result.put("list",list);
        return result;
    }
    private int treeToList(List<LedgerDistrictProjectProgress> list,List<LedgerDistrictProjectProgress> dataList,int index){
        for (LedgerDistrictProjectProgress ldpp:dataList) {
            list.add(ldpp);
            if (StringUtils.isNotEmpty(ldpp.getChildren())){
                index = treeToList(list,ldpp.getChildren(),index);
                ldpp.setChildren(null);
            }else{
                ldpp.setIndex(index);
                index++;
                ldpp.setName(null);
            }
        }
        return index;
    }

    /**
     * 一键生成台账
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult generate(LedgerDistrictProgress params){
        //查询需要生成台账的区县
        List<LedgerDistrictProgress> ldpList = this.baseMapper.selectCreateDistrict(params);
        if (StringUtils.isEmpty(ldpList)){
            return AjaxResult.error("没有需要创建台账的区县");
        }
        //删除本月度所有数据
        delete(params);
        //拼装并保存数据
        Date createDate = DateUtils.getNowDate();
        for (LedgerDistrictProgress ldp:ldpList) {
            ldp.setDataYear(params.getDataYear());
            ldp.setDataWeek(params.getDataWeek());
            ldp.setCreateTime(createDate);
            ldp.setCreateBy(params.getCreateBy());
            ldp.setState(Constants.VERIFY_STATUS_PASS);
            //获取项目数据
            ldp.setProjectProgressList(initData(ldp));
            //保存区县台账
            super.save(ldp);
            //保存项目数据
            saveLedgerDistrictProjectProgress(ldp);
        }
        //保存省台账
        LedgerProvinceProgress lpp = new LedgerProvinceProgress();
        lpp.setDeptId(params.getDeptId());
        lpp.setDataYear(params.getDataYear());
        lpp.setDataWeek(params.getDataWeek());
        lpp.setCreateBy(params.getCreateBy());
        lpp.setCreateTime(params.getCreateTime());
        provinceProgressMapper.insert(lpp);
        return AjaxResult.success();
    }

    /**
     * 保存项目进度台账
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveLedgerDistrictProjectProgress(LedgerDistrictProgress params){
        //保存
        for (LedgerDistrictProjectProgress ldpp:params.getProjectProgressList()) {
            ldpp.setLedgerDistrictProgressId(params.getId());
            ldpp.setDataYear(params.getDataYear());
            ldpp.setDataWeek(params.getDataWeek());
            ldpp.setDeptId(params.getDeptId());
            ldpp.setCityDistrict(params.getCityDistrict());
            ldpp.setCreateBy(params.getCreateBy());
            ldpp.setCreateTime(params.getCreateTime());
            projectProgressMapper.insert(ldpp);
        }
    }

    /**
     * 删除数据
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(LedgerDistrictProgress params){
        LambdaQueryWrapper<LedgerDistrictProgress> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(LedgerDistrictProgress::getDataYear,params.getDataYear());
        wrapper1.eq(LedgerDistrictProgress::getDataWeek,params.getDataWeek());
        super.remove(wrapper1);
        LambdaQueryWrapper<LedgerDistrictProjectProgress> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(LedgerDistrictProjectProgress::getDataYear,params.getDataYear());
        wrapper2.eq(LedgerDistrictProjectProgress::getDataWeek,params.getDataWeek());
        projectProgressMapper.delete(wrapper2);
        LambdaQueryWrapper<LedgerProvinceProgress> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(LedgerProvinceProgress::getDataYear,params.getDataYear());
        wrapper3.eq(LedgerProvinceProgress::getDataWeek,params.getDataWeek());
        provinceProgressMapper.delete(wrapper3);
    }

    /**
     * 实施月度填报统计
     * @return
     */
    public List<LedgerProgressVO> statistics(){
        //获取数据
        List<ProjectImplBasicInfo> dataList = basicInfoMapper.selectByYear(DateUtils.getCurrentYear());
        if (StringUtils.isEmpty(dataList)){
            return null;
        }
        //获取所有区县
        List<TreeSelect> cityList = deptService.selectCityTreeList(new SysDept(1));
        LedgerProgressVO total = new LedgerProgressVO("总计");
        List<LedgerProgressVO> treeList = initTree(cityList,dataList,total);
        treeList.add(0,total);
        return treeList;
    }

    /**
     * 初始化树结构
     * @param cityList
     * @param dataList
     * @param total
     * @return
     */
    private List<LedgerProgressVO> initTree(List<TreeSelect> cityList, List<ProjectImplBasicInfo> dataList,LedgerProgressVO total){
        if (StringUtils.isEmpty(cityList)){
            return null;
        }
        List<LedgerProgressVO> list = new ArrayList<>();
        for (TreeSelect tree:cityList){
            LedgerProgressVO city = new LedgerProgressVO(tree.getLabel());
            List<LedgerProgressVO> children = null;
            if (StringUtils.isEmpty(tree.getChildren())){
                children = new ArrayList<>();
            }
            for (ProjectImplBasicInfo obj:dataList) {
                if (Arrays.asList(obj.getDeptIds().split(",")).contains(tree.getId().toString())){
                    LedgerProgressVO vo = pibiToLpv(obj);
                    city.add(vo);
                    if (children!=null){
                        children.add(vo);
                        total.add(vo);
                    }
                }
            }
            if (city.getNum()>0){
                list.add(city);
                city.setChildren(initTree(tree.getChildren(),dataList,total));
                if (StringUtils.isNotEmpty(children)){
                    city.setChildren(children);
                }
            }
        }
        return list;
    }

    private LedgerProgressVO pibiToLpv(ProjectImplBasicInfo obj){
        LedgerProgressVO lpv = new LedgerProgressVO(obj.getProjectName());
        lpv.setNum(1);
        //获取数据修改时间年月
        Integer dataYear = DateUtils.getDateYear(obj.getUpdateTime());
        Integer dataMonth = DateUtils.getDateMonth(obj.getUpdateTime());
        //是否有修改
        boolean isUpdate = false;
        //对比时间
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.YEAR) == dataYear && (calendar.get(Calendar.MONTH)+1) == dataMonth){
            isUpdate = true;
        }
        //判断状态
        if (!isUpdate||Constants.VERIFY_STATUS_DRAFT.equals(obj.getProjectStatus())){
            //未提交
            lpv.setNotReport(1);
        }else if(Integer.parseInt(obj.getProjectStatus())>0){
            //已提交
            lpv.setReport(1);
        }else if(Constants.VERIFY_STATUS_REJECT.equals(obj.getProjectStatus())){
            //未审核通过
            lpv.setNotApprove(1);
        }else if (Constants.VERIFY_STATUS_PASS.equals(obj.getProjectStatus())){
            //已审核通过
            lpv.setApprove(1);
        }
        return lpv;
    }

}
