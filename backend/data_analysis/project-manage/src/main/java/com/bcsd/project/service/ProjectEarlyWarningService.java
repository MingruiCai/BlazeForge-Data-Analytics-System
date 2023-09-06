package com.bcsd.project.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.utils.ArithUtil;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.DictUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.constants.Constants;
import com.bcsd.project.domain.*;
import com.bcsd.project.mapper.ProjectEarlyWarningMapper;
import com.bcsd.project.mapper.ProjectImplVersionMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProjectEarlyWarningService extends ServiceImpl<ProjectEarlyWarningMapper, ProjectEarlyWarning> implements IService<ProjectEarlyWarning> {

    @Autowired
    ProjectImplVersionMapper versionMapper;
    @Autowired
    ProblemRectifyService problemRectifyService;
    @Autowired
    ProjectImplService projectImplService;

    /**
     * 统计
     * @return
     */
    public Map statistics(ProjectEarlyWarning params){
        return super.baseMapper.statistics(params);
    }

    /**
     * 分页列表
     * @param params
     * @return
     */
    public List<ProjectEarlyWarning> listPage(ProjectEarlyWarning params){
        LambdaQueryWrapper<ProjectEarlyWarning> wrapper = new LambdaQueryWrapper();
        if (params.getType()!=null){
            wrapper.eq(ProjectEarlyWarning::getType,params.getType());
        }
        if (StringUtils.isNotBlank(params.getProjectName())){
            wrapper.like(ProjectEarlyWarning::getProjectName,params.getProjectName());
        }
        if (StringUtils.isNotBlank(params.getDataTime())){
            wrapper.eq(ProjectEarlyWarning::getDataTime,params.getDataTime());
        }
        if (params.getState()!=null){
            wrapper.eq(ProjectEarlyWarning::getState,params.getState());
        }
        wrapper.orderByDesc(ProjectEarlyWarning::getCreateTime);
        return super.list(wrapper);
    }

    /**
     * 预测预警生成数据
     * @param dataTime
     * @param userName
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult generateWarning(String dataTime,String userName){
        if (exist(dataTime)){
            return AjaxResult.error(dataTime+"已生成预警！");
        }
        //删除过期数据
        deleteExpireData(dataTime);
        //查询项目实施数据
        List<ProjectImplVersion> dataList = versionMapper.selectNewDataByBuildStatus();
        if (StringUtils.isEmpty(dataList)){
            return AjaxResult.success();
        }
        //指标
        String dayStr = DictUtils.getDictValue(Constants.DICT_KEY_OVERDUE_NOT_STARTED,"day");
        String bofulv = DictUtils.getDictValue(Constants.DICT_KEY_BOFULV_TARGET,DateUtils.getCurrentMonthNoFormat());
        String wanchenglv = DictUtils.getDictValue(Constants.DICT_KEY_WANCHENGLV_TARGET,DateUtils.getCurrentMonthNoFormat());
        //组装数据
        List<ProjectEarlyWarning> list = new ArrayList<>();
        Date date = DateUtils.getNowDate();
        for (ProjectImplVersion data:dataList) {
            //超期未开工
            if (StringUtils.isNotBlank(dayStr)){
                ProjectEarlyWarning pew = overdueNotStarted(data,Integer.parseInt(dayStr));
                if (pew!=null){
                    pew.setDataTime(dataTime);
                    pew.setCreateBy(userName);
                    pew.setCreateTime(date);
                    list.add(pew);
                }
            }
            //专项资金拨付率未达标
            if (StringUtils.isNotBlank(bofulv)){
                ProjectEarlyWarning pew = fundWarning(data,bofulv,2);
                if (pew!=null){
                    pew.setDataTime(dataTime);
                    pew.setCreateBy(userName);
                    pew.setCreateTime(date);
                    list.add(pew);
                }
            }
            //专项资金完成率未达标
            if (StringUtils.isNotBlank(bofulv)){
                ProjectEarlyWarning pew = fundWarning(data,wanchenglv,3);
                if (pew!=null){
                    pew.setDataTime(dataTime);
                    pew.setCreateBy(userName);
                    pew.setCreateTime(date);
                    list.add(pew);
                }
            }

        }
        //数据入库
        if (StringUtils.isNotEmpty(list)){
            saveBatch(list);
        }
        return AjaxResult.success();
    }

    /**
     * 删除过期数据
     * @param dataTime
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteExpireData(String dataTime){
        LambdaQueryWrapper<ProjectEarlyWarning> wrapper = new LambdaQueryWrapper();
        wrapper.eq(ProjectEarlyWarning::getState,1);
        wrapper.ne(ProjectEarlyWarning::getDataTime,dataTime);
        super.remove(wrapper);
    }

    /**
     * 是否存在
     * @param dataTime
     * @return
     */
    public boolean exist(String dataTime){
        LambdaQueryWrapper<ProjectEarlyWarning> wrapper = new LambdaQueryWrapper();
        wrapper.eq(ProjectEarlyWarning::getDataTime,dataTime);
        return super.count(wrapper)>0;
    }

    /**
     * 转办
     * @param id
     * @param userName
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult transfer(Long id, String userName){
        ProjectEarlyWarning pew = super.getById(id);
        if (pew==null){
            return AjaxResult.error("数据不存在！");
        }
        if (pew.getState()!=1){
            return AjaxResult.error("预警已转办！");
        }
        //修改预警信息
        ProjectEarlyWarning updatePew = new ProjectEarlyWarning();
        updatePew.setId(pew.getId());
        updatePew.setUpdateTime(DateUtils.getNowDate());
        updatePew.setUpdateBy(userName);
        updatePew.setState(2);
        boolean bool = updateById(updatePew);
        if (bool){
            //转到问题
            ProblemRectifyRecord record = new ProblemRectifyRecord();
            record.setEarlyWarningId(pew.getId());
            record.setDeptId(pew.getDeptId());
            record.setProjectNo(pew.getProjectNo());
            record.setProjectName(pew.getProjectName());
            //查询项目信息
            ProjectImplBasicInfo pibi = projectImplService.getProjectNameByNo(pew.getProjectNo());
            if (pibi!=null){
                record.setUserId(pibi.getUserId());
            }
            record.setMonitorTime(pew.getCreateTime());
            switch (pew.getType()){
                case 1:
                    record.setProblemType("smssqk");
                    record.setProblemContent(StringUtils.join("超期未开工,预计开工时间:",pew.getTargetValue()));
                    break;
                case 2:
                    record.setProblemType("zjsyqk");
                    record.setProblemContent(StringUtils.join("专项资金拨付率未达标,目标值:",pew.getTargetValue(),"%,实际值:",pew.getActualValue(),"%,偏差值:",pew.getDeviationValue(),"%"));
                    break;
                case 3:
                    record.setProblemType("zjsyqk");
                    record.setProblemContent(StringUtils.join("专项资金完成率未达标,目标值:",pew.getTargetValue(),"%,实际值:",pew.getActualValue(),"%,偏差值:",pew.getDeviationValue(),"%"));
                    break;
                default:break;
            }
            record.setProblemSource("xmssqkjc");
            record.setProblemLevel("yb");
            record.setRectifyStatus("dzg");
            record.setCreateBy(userName);
            problemRectifyService.saveRecord(record);
        }
        return AjaxResult.success();
    }

    /**
     * 逾期未开工
     * @param data
     * @param quota
     */
    private ProjectEarlyWarning overdueNotStarted(ProjectImplVersion data,Integer quota){
        //状态判断
        if (Constants.BUILD_TYPE_4 == data.getBuildStatus()||Constants.BUILD_TYPE_5 == data.getBuildStatus()){
            return null;
        }
        //预计开工时间
        String scheduledDate = data.getPlanBeginDate();
        if (StringUtils.isBlank(scheduledDate)){
            return null;
        }
        //计算时间差
        Date data1 = DateUtils.parseDate(scheduledDate);
        Date data2 = DateUtils.getNowDate();
        if (data2.compareTo(data1)!=1){
            return null;
        }
        int day = DateUtils.differentDaysByMillisecond(data1,data2);
        if (quota<day){
            return null;
        }
        //数据组装
        ProjectEarlyWarning pew = new ProjectEarlyWarning();
        pew.setDeptId(data.getDeptId());
        pew.setProjectNo(data.getProjectNo());
        pew.setProjectName(data.getProjectName());
        pew.setType(1);
        pew.setTargetValue(scheduledDate);
        pew.setDeviationValue(String.valueOf(day));
        pew.setState(1);
        return pew;
    }

    /**
     * 资金预警
     * @param data
     * @param quota
     * @param type 2-拨付 3-完成
     * @return
     */
    private ProjectEarlyWarning fundWarning(ProjectImplVersion data,String quota,Integer type){
        String rate = "0";
        if (StringUtils.isNotBlank(data.getFundsInfo())){
            //获取资金详情
            ProjectImplFundInfo fundInfo = JSON.parseObject(data.getFundsInfo(),ProjectImplFundInfo.class);
            if (fundInfo !=null){
                //获取最后一个计划年度
                String planYear = getPlanYear(data.getPlanYear());
                //数据处理
                JSONObject planFunds = initJSONObject();
                addFundData(fundInfo.getPlanPcIssueFundList(),planFunds,planYear);
                addFundData(fundInfo.getPlanPcIssueFundAdjustmentAmountList(),planFunds,planYear);
                addFundData(fundInfo.getPlanDcIssueFundAdjustmentAmountList(),planFunds,planYear);
                JSONObject funds = initJSONObject();
                switch (type){
                    case 2:
                        addFundData(fundInfo.getPaymentFundList(),funds,planYear);
                        break;
                    case 3:
                        addFundData(fundInfo.getCompleteFundList(),funds,planYear);
                        break;
                    default:return null;
                }
                //计算当前月份指标完成率
                String month = DateUtils.getCurrentMonthNoFormat();
                rate = ArithUtil.rate(ArithUtil.div(funds.getString(month),planFunds.getString(month)));
            }
        }
        if (Double.valueOf(quota)<=Double.valueOf(rate)){
            return null;
        }
        //数据组装
        ProjectEarlyWarning pew = new ProjectEarlyWarning();
        pew.setDeptId(data.getDeptId());
        pew.setProjectNo(data.getProjectNo());
        pew.setProjectName(data.getProjectName());
        pew.setType(type);
        pew.setTargetValue(quota);
        pew.setActualValue(rate);
        pew.setDeviationValue(ArithUtil.sub(rate,quota));
        pew.setState(1);
        return pew;
    }

    private String getPlanYear(String planYears){
        String[] array = planYears.split("/");
        return array[array.length-1];
    }

    private JSONObject initJSONObject(){
        JSONObject obj = new JSONObject();
        for (int i = 1; i <13 ; i++) {
            obj.put(i+"","0");
        }
        return obj;
    }

    /**
     * 数据累加
     */
    private void addFundData(List<ProjectImplFundDetailInfo> list, JSONObject obj,String planYear){
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        for (ProjectImplFundDetailInfo info:list){
            if (planYear.equals(info.getFundYear())){
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
    }


}
