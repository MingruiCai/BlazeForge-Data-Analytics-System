package com.bcsd.project.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bcsd.common.core.domain.TreeSelect;
import com.bcsd.common.core.domain.entity.SysDept;
import com.bcsd.common.core.domain.entity.SysDictData;
import com.bcsd.common.core.text.Convert;
import com.bcsd.common.utils.ArithUtil;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.DictUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.domain.*;
import com.bcsd.project.domain.vo.*;
import com.bcsd.project.mapper.ProjectImplInfoSnapshotMapper;
import com.bcsd.project.mapper.ProjectImplVersionMapper;
import com.bcsd.project.mapper.ProjectStatisticsTargetMapper;
import com.bcsd.system.service.ISysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.bcsd.project.constants.Constants.*;

@Slf4j
@Service
public class ProjectImplStatisticsService {

    @Autowired
    ProjectImplInfoSnapshotMapper snapshotMapper;
    @Autowired
    ProjectImplVersionMapper versionMapper;
    @Autowired
    ProjectDictInfoService projectDictInfoService;
    @Autowired
    ProjectStatisticsTargetMapper targetMapper;
    @Autowired
    ISysDeptService deptService;

    /**
     * 项目汇总
     * @param params
     * @return
     */
    public List<ProjectSummaryVO> projectSummary(ProjectImplStatisticsVO params){
        //判断时间范围
        if (TIME_SCOPE_1.equals(params.getTimeScope())){
            params.setPlanYears(Arrays.asList(DateUtils.getCurrentYear()));
            params.setIsYearly(false);
        }else{
            params.setProjectType(null);
        }
        //数据key
        List<Map<String,List<String>>> keyList = new ArrayList<>();
        //区县
        if (Convert.toBool(params.getStatisticsType().substring(0,1))){
            Map<String,List<String>> keyMap = new HashMap<>();
            List<String> keys = new ArrayList<>();
            List<SysDept> depts = deptService.selectDeptSubset();
            for (SysDept dept:depts){
                if (CollectionUtils.isNotEmpty(params.getDistricts())){
                    if (!params.getDistricts().contains(dept.getDeptName())){
                        continue;
                    }
                }
                keys.add(dept.getDeptName());
            }
            keyMap.put("维度",keys);
            keyList.add(keyMap);
        }
        //范围
        if (Convert.toBool(params.getStatisticsType().substring(1,2))){
            Map<String,List<String>> keyMap = new HashMap<>();
            List<String> keys = new ArrayList<>();
            List<SysDictData> dictData = DictUtils.getDictCache(DICT_KEY_FANWEIQUYU);
            for (SysDictData dict:dictData){
                if (CollectionUtils.isNotEmpty(params.getScopes())){
                    if (!params.getScopes().contains(dict.getDictValue())){
                        continue;
                    }
                }
                keys.add(dict.getDictValue());
            }
            keyMap.put("维度",keys);
            keyList.add(keyMap);
        }
        //计划规划类别
        if (Convert.toBool(params.getStatisticsType().substring(2,3))){
            Map<String,List<String>> keyMap = new HashMap<>();
            List<String> keys = new ArrayList<>();
            List<ProjectDictInfo> dicts1 = projectDictInfoService.getByPid("02000000");
            for (ProjectDictInfo dict1:dicts1){
                if (CollectionUtils.isNotEmpty(params.getTypes())){
                    if (!params.getTypes().contains(dict1.getId())){
                        continue;
                    }
                }
                List<ProjectDictInfo> dicts2 = projectDictInfoService.getByPid(dict1.getId());
                for (ProjectDictInfo dict2:dicts2){
                    keys.add(StringUtils.join(dict1.getName(),"_",dict2.getName()));
                }
            }
            keyMap.put("维度",keys);
            keyList.add(keyMap);
        }
        //145类别
        /*if (Convert.toBool(params.getStatisticsType().substring(3,4))){
            Map<String,List<String>> keyMap = new HashMap<>();
            List<String> keys = new ArrayList<>();
            List<ProjectDictInfo> dicts1 = projectDictInfoService.getByPid("03000000");
            for (ProjectDictInfo dict1:dicts1){
                if (CollectionUtils.isNotEmpty(params.getTypes())){
                    if (!params.getTypes().contains(dict1.getId())){
                        continue;
                    }
                }
                List<ProjectDictInfo> dicts2 = projectDictInfoService.getByPid(dict1.getId());
                for (ProjectDictInfo dict2:dicts2){
                    keys.add(StringUtils.join(dict1.getName(),"_",dict2.getName()));
                }
            }
            keyMap.put("维度",keys);
            keyList.add(keyMap);
        }*/
        //分年
        if (params.getIsYearly()){
            Map<String,List<String>> keyMap = new HashMap<>();
            List<String> keys = new ArrayList<>();
            String beginYear = "2021";
            String endYear = DateUtils.getCurrentYear();
            for (int i = 0; i < Integer.parseInt(ArithUtil.sub(endYear,beginYear))+1; i++) {
                keys.add(ArithUtil.add(beginYear,String.valueOf(i)));
            }
            keyMap.put("维度",keys);
            keyList.add(keyMap);
        }
        //获取所有的key列表
        List<String> keys = getKeys(keyList);
        if (CollectionUtils.isEmpty(keys)){
            return new ArrayList<>();
        }
        //数据处理
        JSONObject data = new JSONObject();
        //查询数据
        List<ProjectImplVersion> dataList = snapshotMapper.getDataByYear(params);
        if (CollectionUtils.isNotEmpty(dataList)){
            data = initData(dataList,params);
        }
        //数据组装
        List<ProjectSummaryVO> result = new ArrayList<>();
        //合计
        String[] arr = new String[keys.get(0).split("_").length];
        Arrays.fill(arr,"合计");
        ProjectSummaryVO total = new ProjectSummaryVO(getColumn(arr,0),getColumn(arr,1),getColumn(arr,2),getColumn(arr,3),getColumn(arr,4));
        result.add(total);
        for (String key:keys){
            String[] column = key.split("_");
            ProjectSummaryVO summaryVO = data.getObject(key,ProjectSummaryVO.class);
            if (summaryVO==null){
                summaryVO = new ProjectSummaryVO(getColumn(column,0),getColumn(column,1),getColumn(column,2),getColumn(column,3),getColumn(column,4));
            }else{
                summaryVO.setColumn1(getColumn(column,0));
                summaryVO.setColumn2(getColumn(column,1));
                summaryVO.setColumn3(getColumn(column,2));
                summaryVO.setColumn4(getColumn(column,3));
                summaryVO.setColumn5(getColumn(column,4));
            }
            total.add(summaryVO);
            result.add(summaryVO);
        }
        return result;
    }

    /**
     * key列表
     * @param keyList
     * @return
     */
    private List<String> getKeys(List<Map<String,List<String>>> keyList){
        //每个集合中元素与元素之间开始拼接，集合只有一个就没必要拼接了
        if(CollectionUtils.isNotEmpty(keyList)){
            //取出第一个集合
            List<String> left = keyList.get(0).get("维度");
            //用于控制循环次数
            int size = keyList.size();
            if (size>1){
                int i = 1;
                do{
                    //用于临时存储
                    List<String> all = new ArrayList<String>();
                    //取出第二个
                    List<String> right = keyList.get(i).get("维度");
                    //开始比对拼接
                    for (String s : left) {
                        for (String s1 : right) {
                            String ss = StringUtils.join(s,"_",s1);
                            all.add(ss);
                        }
                    }
                    //重新赋值给left，用于下次比对拼接
                    left = all;
                    i ++;
                }while (i < size);
            }
            return left;
        }
        return null;
    }

    /**
     * 获取列名
     * @param array
     * @param index
     * @return
     */
    private String getColumn(String[] array,int index){
        String value = null;
        try{
            value = array[index];
        }catch (ArrayIndexOutOfBoundsException e){}
        return value;
    }

    /**
     * 初始化数据
     * @param dataList
     * @param params
     * @return
     */
    private JSONObject initData(List<ProjectImplVersion> dataList,ProjectImplStatisticsVO params){
        JSONObject result = new JSONObject();
        Map<String,ProjectDictInfo> dictMap = projectDictInfoService.getMapByPid("02000000");
        //拼装数据
        for (ProjectImplVersion version:dataList){
            ProjectImplBasicInfo basicInfo = JSON.parseObject(version.getBasicInfo(),ProjectImplBasicInfo.class);
            ProjectImplStateInfo implInfo = JSON.parseObject(version.getImplInfo(),ProjectImplStateInfo.class);
            if (implInfo==null){
                implInfo = new ProjectImplStateInfo();
            }
            ProjectImplFundInfo fundInfo = JSON.parseObject(version.getFundsInfo(),ProjectImplFundInfo.class);
            if (fundInfo==null){
                fundInfo = new ProjectImplFundInfo();
            }
            //获取key
            String key = getKey(params,basicInfo,dictMap);
            if (StringUtils.isBlank(key)){
                continue;
            }
            //数据汇总
            ProjectSummaryVO summary = result.getObject(key,ProjectSummaryVO.class);
            if (summary==null){
                summary = new ProjectSummaryVO(null,null,null,null,null);
            }
            //项目总投资（万元）
            summary.setProjectTotalInvest(ArithUtil.add(summary.getProjectTotalInvest(),basicInfo.getProjectTotalInvest()));
            //省市已下达专项补助资金（万元）
            summary.setPlanFinalIssueFund(ArithUtil.add(summary.getPlanFinalIssueFund(),fundInfo.getPlanFinalIssueFund()));
            //项目个数
            summary.setProjectTotalNum(summary.getProjectTotalNum()+1);
            switch (basicInfo.getBuildStatus()){
                case BUILD_TYPE_1:
                    //未开工（待建）
                    summary.setProjectNoStartedNum(summary.getProjectNoStartedNum()+1);
                    break;
                case BUILD_TYPE_2:
                    //在建
                    summary.setProjectStartedNum(summary.getProjectStartedNum()+1);
                    break;
                case BUILD_TYPE_3:
                    //完建
                    summary.setProjectCompleteNum(summary.getProjectCompleteNum()+1);
                    break;
                case BUILD_TYPE_4:
                    //完成交工验收（交工验收日期）
                    if (StringUtils.isNotBlank(implInfo.getHaAd())){
                        summary.setProjectJiaoGongNum(summary.getProjectJiaoGongNum()+1);
                    }
                    //完成竣工验收（竣工验收日期）
                    if (StringUtils.isNotBlank(implInfo.getCaAd())){
                        summary.setProjectJunGongNum(summary.getProjectJunGongNum()+1);
                    }
                    break;
                case BUILD_TYPE_5:
                    //取消
                    summary.setProjectCancelNum(summary.getProjectCancelNum()+1);
                    break;
                default:break;

            }
            //完成决算（竣工财务决算批复日期）
            if (StringUtils.isNotBlank(implInfo.getCfsFaad())){
                summary.setProjectFinalNum(summary.getProjectFinalNum()+1);
            }
            //累计完成项目总投资（完成的专项资金+配套资金）
            summary.setCompleteTotalInvestment(ArithUtil.adds(summary.getCompleteTotalInvestment(),fundInfo.getCompleteFund(),fundInfo.getCompleteAssortFund()));
            //项目完成专项资金
            summary.setCompleteFund(ArithUtil.add(summary.getCompleteFund(),fundInfo.getCompleteFund()));
            //区县拨付专项资金
            summary.setPaymentFund(ArithUtil.add(summary.getPaymentFund(),fundInfo.getPaymentFund()));
            //使用专项资金
            summary.setUseFund(ArithUtil.add(summary.getUseFund(),fundInfo.getUseFund()));
            //赋值
            result.put(key,summary);
        }
        return result;
    }

    /**
     * 获取KEY
     * @param params
     * @param basicInfo
     * @return
     */
    private String getKey(ProjectImplStatisticsVO params,ProjectImplBasicInfo basicInfo,Map<String,ProjectDictInfo> dictMap){
        List<String> keys = new ArrayList<>();
        //根据不同的统计类型分类
        if (Convert.toBool(params.getStatisticsType().substring(0,1))&&StringUtils.isNotBlank(basicInfo.getUnitName())){
            keys.add(basicInfo.getUnitName());
        }
        if (Convert.toBool(params.getStatisticsType().substring(1,2))&&StringUtils.isNotBlank(basicInfo.getScopeRegion())){
            keys.add(basicInfo.getScopeRegion());
        }
        if (Convert.toBool(params.getStatisticsType().substring(2,3))&&StringUtils.isNotBlank(basicInfo.getPlanOptimizeType())){
            String pid = basicInfo.getPlanOptimizeType().substring(0,6)+"00";
            ProjectDictInfo dictInfo = dictMap.get(pid);
            if (dictInfo!=null){
                keys.add(dictInfo.getPName());
                keys.add(dictInfo.getName());
            }
        }
        if (params.getIsYearly()&&basicInfo.getFirstArrangeFundYear()!=null){
            keys.add(basicInfo.getFirstArrangeFundYear().toString());
        }
        return StringUtils.join(keys,"_");
    }

    /**
     * 首页统计
     * @param params
     * @return
     */
    public StatisticsInfoVO homeStatistics(InformationVO params){
        StatisticsInfoVO result = new StatisticsInfoVO();
        //获取数据
        log.debug("获取数据");
        List<ProjectImplVersion> data = versionMapper.selectDataByYearAndCity(params);
        //数据处理
        log.debug("数据初始化");
        JSONObject obj = initData(data,result,params.getPlanYears());
        //地图
        List<StatisticsVO> mapList = new ArrayList<>();
        JSONObject mapObj = obj.getJSONObject("map");
        for (String key: mapObj.keySet()){
            StatisticsVO vo = mapObj.getObject(key,StatisticsVO.class);
            vo.setCode(key);
            mapList.add(vo);
        }
        result.setMapList(mapList);
        //区县统计、排名
        log.debug("区县统计");
        List<StatisticsVO> cityDistrictList = new ArrayList<>();
        List<StatisticsRankVO> cityDistrictRankList = new ArrayList<>();
        List<SysDept> depts = deptService.selectDeptSubset();
        JSONObject cityObj = obj.getJSONObject("city");
        JSONObject qxpmObj = obj.getJSONObject("qxpm");
        //权重
        String w1 = getWeight("开工率");
        String w2 = getWeight("专项资金拨付率");
        String w3 = getWeight("专项资金完成率");
        for (SysDept dept:depts){
            if (!contain(params.getCityDistrict(),dept.getCityCode())){
                continue;
            }
            StatisticsVO vo = cityObj.getObject(dept.getDeptId().toString(),StatisticsVO.class);
            StatisticsRankVO vo1 = qxpmObj.getObject(dept.getDeptId().toString(),StatisticsRankVO.class);
            if (vo!=null){
                vo.setName(dept.getDeptName());
                vo.setCode(dept.getCityCode());
                cityDistrictList.add(vo);
            }
            if (vo1!=null&&dept.getDeptId()!=104){
                vo1.setName(dept.getDeptName());
                vo1.setCode(dept.getCityCode());
                //开工率
                vo1.setOperationRate(ArithUtil.rate(ArithUtil.div(String.valueOf(vo1.getZj()+vo1.getWj()),String.valueOf(vo1.getDj()+vo1.getZj()+vo1.getWj()))));
                //完工率
                vo1.setCompleteRate(ArithUtil.rate(ArithUtil.div(String.valueOf(vo1.getWj()),String.valueOf(vo1.getDj()+vo1.getZj()+vo1.getWj()))));
                //决算率
                vo1.setJueSuanRate(ArithUtil.rate(ArithUtil.div(vo1.getJs().toString(),vo1.getWj().toString())));
                //专项资金拨付率
                vo1.setPaymentFundRate(ArithUtil.rate(ArithUtil.div(vo1.getPaymentFund(),vo1.getPlanFinalIssueFund())));
                //专项资金完成率
                vo1.setCompleteFundRate(ArithUtil.rate(ArithUtil.div(vo1.getCompleteFund(),vo1.getPlanFinalIssueFund())));
                //得分
                String s1 = ArithUtil.mul(vo1.getOperationRate(),w1);
                String s2 = ArithUtil.mul(vo1.getPaymentFundRate(),w2);
                String s3 = ArithUtil.mul(vo1.getCompleteFundRate(),w3);
                vo1.setScore(Double.valueOf(ArithUtil.adds(s1,s2,s3)));
                if (vo1.getScore()>0){
                    cityDistrictRankList.add(vo1);
                }
            }


        }
        //排序
        log.debug("区县统计排序1");
        cityDistrictList.sort(Comparator.comparing(StatisticsVO::getFundToDouble).reversed());
        log.debug("区县统计排序2");
        cityDistrictRankList.sort(Comparator.comparing(StatisticsRankVO::getScore).reversed());
        //排名
        log.debug("区县统计排名");
        districtRank(cityDistrictRankList);
        result.setCityDistrictList(cityDistrictList);
        result.setCityDistrictRankList(cityDistrictRankList);
        //规划优化类别(145)
        log.debug("规划优化类别");
        List<StatisticsVO> planOptimizeTypeList = new ArrayList<>();
        List<SysDictData> dicts = DictUtils.getDictCache(DICT_KEY_145_TYPE_ADDR);
        JSONObject ghyhObj = obj.getJSONObject("ghyh");
        for (SysDictData dict:dicts){
            StatisticsVO vo = ghyhObj.getObject(dict.getDictValue(),StatisticsVO.class);
            if (vo==null){
                vo = new StatisticsVO(0,"0");
            }
            vo.setName(dict.getDictLabel());
            vo.setCode(dict.getDictValue());
            planOptimizeTypeList.add(vo);
        }
        result.setPlanOptimizeTypeList(planOptimizeTypeList);
        //分区域项目建设状态
        log.info("分区域项目建设状态");
        List<StatisticsVO> scopeRegionList = new ArrayList<>();
        List<SysDictData> dictData1 = DictUtils.getDictCache(DICT_KEY_FANWEIQUYU);
        JSONObject fwqyObj = obj.getJSONObject("fwqy");
        for (SysDictData dict:dictData1){
            StatisticsVO vo = fwqyObj.getObject(dict.getDictValue(),StatisticsVO.class);
            if (vo==null){
                vo = new StatisticsVO(0,0,0,0);
            }
            vo.setName(dict.getDictLabel());
            vo.setCode(dict.getDictValue());
            scopeRegionList.add(vo);
        }
        result.setScopeRegionList(scopeRegionList);
        //专项资金拨付率、完成率
        log.info("专项资金拨付率、完成率");
        List<StatisticsVO> paymentFundRateList = new ArrayList<>();
        List<StatisticsVO> completeFundRateList = new ArrayList<>();
        JSONObject zxzjObj = obj.getJSONObject("zxzj");
        JSONObject planFunds = zxzjObj.getJSONObject("planFunds");
        if (planFunds==null){
            planFunds = new JSONObject();
        }
        JSONObject paymentFunds = zxzjObj.getJSONObject("paymentFunds");
        if (paymentFunds==null){
            paymentFunds = new JSONObject();
        }
        JSONObject completeFunds = zxzjObj.getJSONObject("completeFunds");
        if (completeFunds==null){
            completeFunds = new JSONObject();
        }
        for (int i = 1;i<13;i++){
            String key = String.valueOf(i);
            String planFund = planFunds.getString(key);
            String paymentFund = paymentFunds.getString(key);
            String completeFund = completeFunds.getString(key);
            StatisticsVO vo = new StatisticsVO();
            vo.setCode(key);
            vo.setName(key+"月");
            vo.setTarget(DictUtils.getDictValue(DICT_KEY_BOFULV_TARGET,key));
            //vo.setRate(ArithUtil.rate(ArithUtil.div(paymentFund,planFund)));
            vo.setRate(ArithUtil.rate(ArithUtil.div(paymentFund,result.getPlanFinalIssueFund())));
            paymentFundRateList.add(vo);
            StatisticsVO vo1 = new StatisticsVO();
            vo1.setCode(key);
            vo1.setName(key+"月");
            vo1.setTarget(DictUtils.getDictValue(DICT_KEY_WANCHENGLV_TARGET,key));
            //vo1.setRate(ArithUtil.rate(ArithUtil.div(completeFund,planFund)));
            vo1.setRate(ArithUtil.rate(ArithUtil.div(completeFund,result.getPlanFinalIssueFund())));
            completeFundRateList.add(vo1);
        }
        result.setPaymentFundRateList(paymentFundRateList);
        result.setCompleteFundRateList(completeFundRateList);
        //总投资-格式化
        result.setProjectTotalInvest(ArithUtil.mul(result.getProjectTotalInvest(),"0.0001"));
        //下达专项资金-格式化
        result.setPlanFinalIssueFund(ArithUtil.mul(result.getPlanFinalIssueFund(),"0.0001"));
        //完成专项资金-格式化
        result.setCompleteFund(ArithUtil.mul(result.getCompleteFund(),"0.0001"));
        //拨付专项资金-格式化
        result.setPaymentFund(ArithUtil.mul(result.getPaymentFund(),"0.0001"));
        return result;
    }

    /**
     * 数据处理
     * @param dataList
     * @param result
     */
    private JSONObject initData(List<ProjectImplVersion> dataList,StatisticsInfoVO result,List<String> years){
        //地图
        JSONObject mapObj = new JSONObject();
        //区县
        JSONObject cityObj = new JSONObject();
        //规划优化类别
        JSONObject ghyhObj = new JSONObject();
        //区域、建设状态
        JSONObject fwqyObj = new JSONObject();
        //专项资金拨付率、完成率
        JSONObject zxzjObj = new JSONObject();
        //区县排名
        JSONObject qxpmObj = new JSONObject();
        if (CollectionUtils.isNotEmpty(dataList)){
            //未开工、在建、完建、完成决算
            Integer notStart = 0,construction = 0,completion = 0,juesuan = 0;
            //数据处理
            for (ProjectImplVersion version:dataList) {
                //资金信息
                ProjectImplFundInfo fundInfo = JSON.parseObject(version.getFundsInfo(),ProjectImplFundInfo.class);
                if (fundInfo == null){
                    fundInfo = new ProjectImplFundInfo();
                }else{
                    //资金过滤
                    if (StringUtils.isNotEmpty(years)){
                        initFundData(fundInfo,years);
                    }
                }
                //区县
                StatisticsVO cityVO = fillData(cityObj,version.getDeptId().toString(),fundInfo.getPlanFinalIssueFund());
                if (cityVO.getDj()==null){
                    cityVO.initStatus();
                }
                //区县排名
                StatisticsRankVO qxpmVO = qxpmObj.getObject(version.getDeptId().toString(),StatisticsRankVO.class);
                if (qxpmVO==null){
                    qxpmVO = new StatisticsRankVO(0,0,0,0,"0","0","0");
                }
                //总投资
                result.setProjectTotalInvest(ArithUtil.add(result.getProjectTotalInvest(),version.getProjectTotalInvest()));
                //下达专项资金
                result.setPlanFinalIssueFund(ArithUtil.add(result.getPlanFinalIssueFund(),fundInfo.getPlanFinalIssueFund()));
                qxpmVO.setPlanFinalIssueFund(ArithUtil.add(qxpmVO.getPlanFinalIssueFund(),fundInfo.getPlanFinalIssueFund()));
                //完成专项资金
                result.setCompleteFund(ArithUtil.add(result.getCompleteFund(),fundInfo.getCompleteFund()));
                qxpmVO.setCompleteFund(ArithUtil.add(qxpmVO.getCompleteFund(),fundInfo.getCompleteFund()));
                //拨付专项资金
                result.setPaymentFund(ArithUtil.add(result.getPaymentFund(),fundInfo.getPaymentFund()));
                qxpmVO.setPaymentFund(ArithUtil.add(qxpmVO.getPaymentFund(),fundInfo.getPaymentFund()));
                //区域、建设状态
                StatisticsVO fwqyVO = fwqyObj.getObject(version.getScopeRegion(),StatisticsVO.class);
                if (fwqyVO==null){
                    fwqyVO = new StatisticsVO(0,0,0,0);
                }
                //建设状态统计
                switch (version.getBuildStatus()){
                    case BUILD_TYPE_1:
                        notStart++;
                        cityVO.setDj(cityVO.getDj()+1);
                        fwqyVO.setDj(fwqyVO.getDj()+1);
                        qxpmVO.setDj(qxpmVO.getDj()+1);
                        break;
                    case BUILD_TYPE_2:
                        construction++;
                        cityVO.setZj(cityVO.getZj()+1);
                        fwqyVO.setZj(fwqyVO.getZj()+1);
                        qxpmVO.setZj(qxpmVO.getZj()+1);
                        break;
                    case BUILD_TYPE_3:
                        completion++;
                        cityVO.setWj(cityVO.getWj()+1);
                        fwqyVO.setWj(fwqyVO.getWj()+1);
                        qxpmVO.setWj(qxpmVO.getWj()+1);
                        break;
                    case BUILD_TYPE_4:
                        cityVO.setWj(cityVO.getWj()+1);
                        fwqyVO.setYs(fwqyVO.getYs()+1);
                        break;
                    default:break;
                }
                fwqyObj.put(version.getScopeRegion(),fwqyVO);
                //完成决算
                if (StringUtils.isNotBlank(version.getCaAd())){
                    juesuan++;
                    qxpmVO.setJs(qxpmVO.getJs()+1);
                }
                //地图
                fillData(mapObj,version.getCityDistrict(), fundInfo.getPlanFinalIssueFund());
                //规划优化类别
                String key = version.getType145();
                if (StringUtils.isNotBlank(key)){
                    key = key.substring(0,4)+"0000";
                }
                fillData(ghyhObj,key,fundInfo.getPlanFinalIssueFund());
                //专项资金拨付、完成率
                fillData(zxzjObj,fundInfo,years);
                //区县排名
                qxpmObj.put(version.getDeptId().toString(),qxpmVO);
            }
            Integer totalNum = notStart+construction+completion;
            //开工率 operationRate （在建+完建）/（在建+完建+未开工）
            result.setOperationRate(ArithUtil.rate(ArithUtil.div(String.valueOf(construction+completion),totalNum.toString())));
            //完工率 completeRate 完工/（在建+完建+未开工）
            result.setCompleteRate(ArithUtil.rate(ArithUtil.div(completion.toString(),totalNum.toString())));
            //决算率 jueSuanRate 完成决算/完建
            result.setJueSuanRate(ArithUtil.rate(ArithUtil.div(juesuan.toString(),completion.toString())));
            //项目数量
            result.setProjectNum(dataList.size());
            //专项资金拨付率
            result.setPaymentFundRate(ArithUtil.rate(ArithUtil.div(result.getPaymentFund(),result.getPlanFinalIssueFund())));
            //专项资金完成率
            result.setCompleteFundRate(ArithUtil.rate(ArithUtil.div(result.getCompleteFund(),result.getPlanFinalIssueFund())));
        }
        //数据分类
        JSONObject obj = new JSONObject();
        obj.put("map",mapObj);
        obj.put("city",cityObj);
        obj.put("ghyh",ghyhObj);
        obj.put("fwqy",fwqyObj);
        obj.put("zxzj",zxzjObj);
        obj.put("qxpm",qxpmObj);
        return obj;
    }

    /**
     * 资金初始化（过滤）
     * @param fundInfo
     * @param years
     */
    public void initFundData(ProjectImplFundInfo fundInfo,List<String> years){
        //省市下达
        fundInfo.setPlanPcIssueFund(getFundByYear(fundInfo.getPlanPcIssueFundList(),years));
        //省市下达调整
        fundInfo.setPlanPcIssueFundAdjustmentAmount(getFundByYear(fundInfo.getPlanPcIssueFundAdjustmentAmountList(),years));
        //区县调整
        fundInfo.setPlanDcIssueFundAdjustmentAmount(getFundByYear(fundInfo.getPlanDcIssueFundAdjustmentAmountList(),years));
        //最终下达
        fundInfo.setPlanFinalIssueFund(ArithUtil.adds(fundInfo.getPlanPcIssueFund(),fundInfo.getPlanPcIssueFundAdjustmentAmount(),fundInfo.getPlanDcIssueFundAdjustmentAmount()));
        //拨付专项
        fundInfo.setPaymentFund(getFundByYear(fundInfo.getPaymentFundList(),years));
        //拨付配套
        fundInfo.setPaymentAssortFund(getFundByYear(fundInfo.getPaymentAssortFundList(),years));
        //拨付合计
        fundInfo.setPaymentTotalFund(ArithUtil.add(fundInfo.getPaymentFund(),fundInfo.getPaymentAssortFund()));
        //完成专项
        fundInfo.setCompleteFund(getFundByYear(fundInfo.getCompleteFundList(),years));
        //完成配套
        fundInfo.setCompleteAssortFund(getFundByYear(fundInfo.getCompleteAssortFundList(),years));
        //完成合计
        fundInfo.setCompleteTotalFund(ArithUtil.add(fundInfo.getCompleteFund(),fundInfo.getCompleteAssortFund()));
    }

    private String getFundByYear(List<ProjectImplFundDetailInfo> list,List<String> years){
        String fund = "0";
        if(StringUtils.isNotEmpty(list)){
            for (ProjectImplFundDetailInfo detail:list) {
                if (years.contains(detail.getFundYear())){
                    fund = detail.getFundTotal();
                }
            }
        }
        return fund;
    }

    /**
     * 数据填充
     * @param obj
     * @param key
     * @param fund
     */
    private StatisticsVO fillData(JSONObject obj,String key,String fund){
        StatisticsVO vo = obj.getObject(key,StatisticsVO.class);
        if (vo==null){
            vo = new StatisticsVO(0,"0");
        }
        vo.setNum(vo.getNum()+1);
        vo.setFund(ArithUtil.add(vo.getFund(),fund));
        obj.put(key,vo);
        return vo;
    }

    /**
     * 数据填充
     * @param obj
     * @param fundInfo
     */
    private void fillData(JSONObject obj,ProjectImplFundInfo fundInfo,List<String> years){
        JSONObject planFunds = obj.getJSONObject("planFunds");
        if (planFunds==null){
            planFunds = initJSONObject();
        }
        JSONObject paymentFunds = obj.getJSONObject("paymentFunds");
        if (paymentFunds==null){
            paymentFunds = initJSONObject();
        }
        JSONObject completeFunds = obj.getJSONObject("completeFunds");
        if (completeFunds==null){
            completeFunds = initJSONObject();
        }
        //计划-省市下达专项资金
        List<ProjectImplFundDetailInfo> list1 = fundInfo.getPlanPcIssueFundList();
        //计划-省市下达专项资金调整量
        List<ProjectImplFundDetailInfo> list2 = fundInfo.getPlanPcIssueFundAdjustmentAmountList();
        //计划-区县下达专项资金调整量（万元）
        List<ProjectImplFundDetailInfo> list3 = fundInfo.getPlanDcIssueFundAdjustmentAmountList();
        //拨付-专项资金
        List<ProjectImplFundDetailInfo> list4 = fundInfo.getPaymentFundList();
        //完成-专项资金
        List<ProjectImplFundDetailInfo> list5 = fundInfo.getCompleteFundList();
        //数据累加
        addFundData(list1,planFunds,years);
        addFundData(list2,planFunds,years);
        addFundData(list3,planFunds,years);
        addFundData(list4,paymentFunds,years);
        addFundData(list5,completeFunds,years);
        obj.put("planFunds",planFunds);
        obj.put("paymentFunds",paymentFunds);
        obj.put("completeFunds",completeFunds);
    }

    /**
     * 数据累加
     * @param list
     * @param obj
     */
    private void addFundData(List<ProjectImplFundDetailInfo> list,JSONObject obj,List<String> years){
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        for (ProjectImplFundDetailInfo info:list){
            if (StringUtils.isNotEmpty(years)&&(!years.contains(info.getFundYear()))){
                continue;
            }
            String month1 = StringUtils.join("-",obj.getString("1"));
            String month2 = StringUtils.join("-",obj.getString("2"));
            String month3 = StringUtils.join("-",obj.getString("3"));
            String month4 = StringUtils.join("-",obj.getString("4"));
            String month5 = StringUtils.join("-",obj.getString("5"));
            String month6 = StringUtils.join("-",obj.getString("6"));
            String month7 = StringUtils.join("-",obj.getString("7"));
            String month8 = StringUtils.join("-",obj.getString("8"));
            String month9 = StringUtils.join("-",obj.getString("9"));
            String month10 = StringUtils.join("-",obj.getString("10"));
            String month11 = StringUtils.join("-",obj.getString("11"));
            obj.put("1",ArithUtil.add(obj.getString("1"),info.getMonth1()));
            obj.put("2",ArithUtil.adds(obj.getString("1"),obj.getString("2"),info.getMonth2(),month1));
            obj.put("3",ArithUtil.adds(obj.getString("2"),obj.getString("3"),info.getMonth3(),month2));
            obj.put("4",ArithUtil.adds(obj.getString("3"),obj.getString("4"),info.getMonth4(),month3));
            obj.put("5",ArithUtil.adds(obj.getString("4"),obj.getString("5"),info.getMonth5(),month4));
            obj.put("6",ArithUtil.adds(obj.getString("5"),obj.getString("6"),info.getMonth6(),month5));
            obj.put("7",ArithUtil.adds(obj.getString("6"),obj.getString("7"),info.getMonth7(),month6));
            obj.put("8",ArithUtil.adds(obj.getString("7"),obj.getString("8"),info.getMonth8(),month7));
            obj.put("9",ArithUtil.adds(obj.getString("8"),obj.getString("9"),info.getMonth9(),month8));
            obj.put("10",ArithUtil.adds(obj.getString("9"),obj.getString("10"),info.getMonth10(),month9));
            obj.put("11",ArithUtil.adds(obj.getString("10"),obj.getString("11"),info.getMonth11(),month10));
            obj.put("12",ArithUtil.adds(obj.getString("11"),obj.getString("12"),info.getMonth12(),month11));
        }
    }

    /**
     * 初始化
     * @return
     */
    private JSONObject initJSONObject(){
        JSONObject obj = new JSONObject();
        for (int i = 1; i <13 ; i++) {
            obj.put(i+"","0");
        }
        return obj;
    }

    /**
     * 区县排名
     * @param list
     */
    private void districtRank(List<StatisticsRankVO> list){
        int count = 1;
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                list.get(0).setIndex(count);
                count++;
            } else {
                if (list.get(i).getScore().equals(list.get(i-1).getScore())){
                    list.get(i).setIndex(list.get(i-1).getIndex());
                    count++;
                }else {
                    list.get(i).setIndex(count);
                    count++;
                }
            }
        }
    }

    /**
     * 是否包含
     * @param p
     * @param s
     * @return
     */
    private boolean contain(String p,String s){
        if (StringUtils.isBlank(p)){
            return true;
        }
        if ("00".equals(p.substring(2,4))){
            return true;
        }else if ("00".equals(p.substring(4,6))){
            return p.substring(0,4).equals(s.substring(0,4));
        }else{
            return p.equals(s);
        }
    }

    /**
     * 获取权重
     * @param key
     * @return
     */
    private String getWeight(String key){
        String value = DictUtils.getDictValue(DICT_KEY_RANK_WEIGHT,key);
        if (StringUtils.isBlank(value)){
            value = "0";
        }
        return value;
    }

    /**
     * 保存统计目标
     * @param params
     * @return
     */
    public int saveTarget(ProjectStatisticsTarget params){
        //根据用户查询是否存在
        ProjectStatisticsTarget target = getByUserId(params.getUserId());
        if (target==null){
            params.setCreateTime(DateUtils.getNowDate());
            return targetMapper.insert(params);
        }else{
            params.setId(target.getId());
            params.setUpdateBy(params.getCreateBy());
            params.setUpdateTime(DateUtils.getNowDate());
            params.setCreateBy(null);
            return targetMapper.updateById(params);
        }
    }

    /**
     * 获取统计目标
     * @param userId
     * @return
     */
    public ProjectStatisticsTarget getByUserId(Long userId){
        LambdaQueryWrapper<ProjectStatisticsTarget> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectStatisticsTarget::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(ProjectStatisticsTarget::getUserId,userId);
        return targetMapper.selectOne(wrapper);
    }

    /**
     * 实施信息统计
     * @param isCurrentYear 0-往年 1-当年
     * @return
     */
    public List<ProjectDataSummaryVO> implInfoStatistic(Integer isCurrentYear){
        //设置参数
        InformationVO params = new InformationVO();
        params.setIsCurrentYear(isCurrentYear);
        params.setYear(DateUtils.getCurrentYear());
        params.setMonth(DateUtils.getCurrentMonth());
        //获取数据
        List<ProjectImplVersion> dataList = versionMapper.selectDataByYM(params);
        if (StringUtils.isEmpty(dataList)){
            return null;
        }
        //查询所有城市区县信息
        List<TreeSelect> cityList = deptService.selectCityTreeList(new SysDept(1));
        ProjectDataSummaryVO total = new ProjectDataSummaryVO("总计");
        List<ProjectDataSummaryVO> list = initImplTree(cityList,dataList,total,isCurrentYear);
        list.add(0,total);
        return list;
    }

    /**
     * 往年未开工项目
     * @return
     */
    public List<ProjectDataVO> notStartProjectList(){
        //获取数据
        List<ProjectImplVersion> dataList = versionMapper.selectNewDataByBuildStatusAndYear(DateUtils.getCurrentYear());
        if (StringUtils.isEmpty(dataList)){
            return null;
        }
        //查询所有城市区县信息
        List<TreeSelect> cityList = deptService.selectCityTreeList(new SysDept(0));
        ProjectDataVO total = new ProjectDataVO("总计");
        List<ProjectDataVO> list = initProjectTree(cityList,dataList,total);
        list.add(0,total);
        return list;
    }

    //TODO 2022 年度及以前未竣工验收或财政部门暂存资金情况统计表

    //TODO 2022 及以前年度“资金清零、项目销号”进展统计表

    //TODO 三峡后续工作严重滞后项目清单

    /**
     * 初始化树结构
     * @param cityList
     * @param dataList
     * @param total
     * @return
     */
    private List<ProjectDataSummaryVO> initImplTree(List<TreeSelect> cityList, List<ProjectImplVersion> dataList,ProjectDataSummaryVO total,Integer isCurrentYear){
        if (StringUtils.isEmpty(cityList)){
            return null;
        }
        List<ProjectDataSummaryVO> list = new ArrayList<>();
        for (TreeSelect tree:cityList){
            ProjectDataSummaryVO city = new ProjectDataSummaryVO(tree.getLabel());
            for (ProjectImplVersion vo:dataList) {
                if (Arrays.asList(vo.getDeptIds().split(",")).contains(tree.getId().toString())){
                    ProjectDataSummaryVO dataSummaryVO = versionToSummary(vo,isCurrentYear);
                    if (StringUtils.isEmpty(tree.getChildren())){
                        total.add(dataSummaryVO);
                    }
                    city.add(dataSummaryVO);
                }
            }
            if (city.getProjectNum()>0){
                list.add(city);
                city.setChildren(initImplTree(tree.getChildren(),dataList,total,isCurrentYear));
            }
        }
        return list;
    }

    /**
     * 初始化树结构
     * @param cityList
     * @param dataList
     * @param total
     * @return
     */
    private List<ProjectDataVO> initProjectTree(List<TreeSelect> cityList, List<ProjectImplVersion> dataList,ProjectDataVO total){
        if (StringUtils.isEmpty(cityList)){
            return null;
        }
        List<ProjectDataVO> list = new ArrayList<>();
        for (TreeSelect tree:cityList){
            ProjectDataVO city = new ProjectDataVO(tree.getLabel());
            List<ProjectDataVO> children = null;
            if (StringUtils.isEmpty(tree.getChildren())){
                children = new ArrayList<>();
            }
            for (ProjectImplVersion vo:dataList) {
                if (Arrays.asList(vo.getDeptIds().split(",")).contains(tree.getId().toString())){
                    ProjectDataVO projectDataVO = versionToProjectData(vo);
                    if (children!=null){
                        children.add(projectDataVO);
                        total.add(projectDataVO);
                    }
                    city.add(projectDataVO);
                }
            }
            if (!"0".equals(city.getProjectTotalInvest())){
                list.add(city);
                city.setChildren(initProjectTree(tree.getChildren(),dataList,total));
                if (StringUtils.isNotEmpty(children)){
                    city.setChildren(children);
                }
            }
        }
        return list;
    }

    /**
     * 统计转化赋值
     * @param params
     * @return
     */
    private ProjectDataSummaryVO versionToSummary(ProjectImplVersion params,Integer isCurrentYear){
        ProjectDataSummaryVO vo = new ProjectDataSummaryVO("");
        vo.setProjectNum(1);
        switch (params.getBuildStatus()){
            case BUILD_TYPE_1:
                vo.setProjectNoStartedNum(1);
                break;
            case BUILD_TYPE_2:
                vo.setProjectStartedNum(1);
                break;
            case BUILD_TYPE_3:
                vo.setProjectCompleteNum(1);
                break;
            default:break;
        }
        if (StringUtils.isNotBlank(params.getHaAd())){
            vo.setProjectJiaoGongNum(1);
        }
        if (StringUtils.isNotBlank(params.getCaAd())){
            vo.setProjectJunGongNum(1);
        }
        vo.setProjectTotalInvest(params.getProjectTotalInvest());
        if (isCurrentYear==0){
            vo.setPlanFinalIssueFund(params.getPlanFinalIssueFund());
            vo.setCompleteFund(params.getCompleteFund());
            vo.setCompleteTotalInvestment(params.getCompleteTotalFund());
            vo.setPaymentTotalFund(params.getPaymentTotalFund());
            vo.setStagingFinanceFund(ArithUtil.sub(params.getPlanFinalIssueFund(),params.getPaymentFund()));
        }else{
            ProjectImplFundInfo fundInfo = JSON.parseObject(params.getFundsInfo(),ProjectImplFundInfo.class);
            if (fundInfo!=null){
                vo.setPlanFinalIssueFund(ArithUtil.adds(getCurrentFund(fundInfo.getPlanPcIssueFundList()),getCurrentFund(fundInfo.getPlanPcIssueFundAdjustmentAmountList()), getCurrentFund(fundInfo.getPlanDcIssueFundAdjustmentAmountList())));
                vo.setCompleteFund(getCurrentFund(fundInfo.getCompleteFundList()));
                vo.setCompleteTotalInvestment(ArithUtil.add(getCurrentFund(fundInfo.getCompleteFundList()),getCurrentFund(fundInfo.getCompleteAssortFundList())));
                vo.setPaymentTotalFund(getCurrentFund(fundInfo.getPaymentFundList()));
                vo.setStagingFinanceFund(ArithUtil.sub(vo.getPlanFinalIssueFund(),vo.getPaymentTotalFund()));
            }
        }
        return vo;
    }

    /**
     * 类转换
     * @param params
     * @return
     */
    private ProjectDataVO versionToProjectData(ProjectImplVersion params){
        ProjectDataVO vo = new ProjectDataVO("");
        vo.setName(params.getProjectName());
        vo.setProjectLegalPerson(params.getProjectLegalPerson());
        vo.setProjectTotalInvest(params.getProjectTotalInvest());
        vo.setScheduledDate(params.getPlanBeginDate());
        vo.setWorkPhase(params.getProphaseWorkExtent());
        ProjectImplFundInfo fundInfo = JSON.parseObject(params.getFundsInfo(),ProjectImplFundInfo.class);
        if (fundInfo!=null){
            vo.setYearIssueFund(ArithUtil.adds(getCurrentFund(fundInfo.getPlanPcIssueFundList()),getCurrentFund(fundInfo.getPlanPcIssueFundAdjustmentAmountList()), getCurrentFund(fundInfo.getPlanDcIssueFundAdjustmentAmountList())));
        }
        return vo;
    }

    /**
     * 获取当年资金
     * @param list
     * @return
     */
    private String getCurrentFund(List<ProjectImplFundDetailInfo> list){
        String fund = "0";
        if(StringUtils.isNotEmpty(list)){
            for (ProjectImplFundDetailInfo detail:list) {
                if (DateUtils.getCurrentYear().equals(detail.getFundYear())){
                    fund = detail.getFundTotal();
                }
            }
        }
        return fund;
    }

    /**
     * 导出
     * @param type
     * @return
     */
    public JSONObject export(String type){
        //返回结果
        JSONObject result = new JSONObject();
        List dataList = null;
        //判断类型
        switch (type){
            case "R_01":
                result.put("year",DateUtils.getDateYear(DateUtils.getNowDate())-1);
                dataList = implInfoStatistic(0);
                break;
            case "R_02":
                result.put("year",DateUtils.getCurrentYear());
                dataList = implInfoStatistic(1);
                break;
            case "R_03":
                result.put("year",DateUtils.getCurrentYear());
                dataList = notStartProjectList();
                break;
            default:return null;
        }
        //判断是否有数据
        if (StringUtils.isEmpty(dataList)){
            return result;
        }
        //数据转换
        List list = new ArrayList();
        treeToList(list,dataList,"1");
        result.put("list",list);
        return result;
    }
    private void treeToList(List list,List dataList,String index){
        for (Object obj:dataList) {
            if (obj instanceof ProjectDataSummaryVO){
                ProjectDataSummaryVO pds = (ProjectDataSummaryVO) obj;
                pds.setIndex(index);
                list.add(pds);
                if (StringUtils.isNotEmpty(pds.getChildren())){
                    treeToList(list,pds.getChildren(),StringUtils.join(index,".1"));
                    pds.setChildren(null);
                }
            }else if(obj instanceof ProjectDataVO){
                ProjectDataVO pd = (ProjectDataVO) obj;
                pd.setIndex(index);
                list.add(pd);
                if (StringUtils.isNotEmpty(pd.getChildren())){
                    treeToList(list,pd.getChildren(),StringUtils.join(index,".1"));
                    pd.setChildren(null);
                }
            }
            index = getIndex(index);
        }
    }
    private String getIndex(String index){
        String[] array = index.split("\\.");
        int i = array.length-1;
        Integer no = Integer.parseInt(array[i]);
        array[i] = String.valueOf(no+1);
        return StringUtils.join(array,".");
    }



}
