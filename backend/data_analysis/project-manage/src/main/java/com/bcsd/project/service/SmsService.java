package com.bcsd.project.service;

import cn.com.flaginfo.sdk.cmc.api.result.ComResult;
import cn.com.flaginfo.sdk.cmc.api.sms.report.SMSReportAPI;
import cn.com.flaginfo.sdk.cmc.api.sms.report.SMSReportRequest;
import cn.com.flaginfo.sdk.cmc.api.sms.report.SMSReportResult;
import cn.com.flaginfo.sdk.cmc.api.sms.send.SMSApi;
import cn.com.flaginfo.sdk.cmc.api.sms.send.SMSSendDataResult;
import cn.com.flaginfo.sdk.cmc.api.sms.send.SMSSendRequest;
import cn.com.flaginfo.sdk.cmc.common.ApiEnum;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bcsd.common.core.domain.entity.SysDictData;
import com.bcsd.common.core.domain.entity.SysUser;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.DictUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.common.utils.ThreadManager;
import com.bcsd.project.constants.Constants;
import com.bcsd.project.domain.SmsSendRecord;
import com.bcsd.project.domain.vo.SmsVO;
import com.bcsd.project.mapper.ProjectImplBasicInfoMapper;
import com.bcsd.project.mapper.SmsSendRecordMapper;
import com.bcsd.project.sms.SmsUtils;
import com.bcsd.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * 短信处理
 */
@Slf4j
@Service
public class SmsService {

    @Autowired
    SmsSendRecordMapper smsSendRecordMapper;
    @Autowired
    ISysUserService userService;
    @Autowired
    ProjectImplBasicInfoMapper basicInfoMapper;

    /**
     * 发送短信（异步）
     * @param vo
     */
    public void sendSmsAsyn(SmsVO vo){
        ThreadManager.getThreadPollProxy().execute(new Runnable() {
            @Override
            public void run() {
                sendSms(vo);
            }
        });
    }

    /**
     * 发送短信
     * @param vo
     * @return
     */
    public boolean sendSms(SmsVO vo){
        //获取短信模板
        SysDictData dict = DictUtils.getDict("sms_template",vo.getType());
        if (dict==null){
            log.error("未获取到短信模板配置，短信发送失败;模板类型:{}",vo.getType());
            return false;
        }
        //根据用户ID获取用户信息
        SysUser user = userService.selectUserById(vo.getUserId());
        if (user==null||StringUtils.isBlank(user.getPhonenumber())){
            log.error("未获取到用户信息或手机号为空，短信发送失败;用户ID:{}",vo.getUserId());
            return false;
        }
        //设置短信参数
        SMSSendRequest sendRequest = new SMSSendRequest();
        sendRequest.setSerialNumber(IdWorker.getIdStr());
        sendRequest.setTemplateId(dict.getDictValue());
        sendRequest.setUserNumber(user.getPhonenumber());
        String smsMsg = dict.getRemark();
        switch (vo.getType()){
            case "1":
                smsMsg = MessageFormat.format(smsMsg,vo.getParams().get(0),vo.getParams().get(1));
                break;
            case "2":
            case "3":
                smsMsg = MessageFormat.format(smsMsg,vo.getParams().get(0));
                break;
            default:break;
        }
        sendRequest.setMessageContent(smsMsg);
        //发送短信
        ComResult<SMSSendDataResult> result =  send(sendRequest);
        //发送记录
        SmsSendRecord sendRecord = new SmsSendRecord();
        sendRecord.setId(Long.valueOf(sendRequest.getSerialNumber()));
        sendRecord.setUserId(vo.getUserId());
        sendRecord.setPhone(user.getPhonenumber());
        sendRecord.setTemplateId(sendRequest.getTemplateId());
        sendRecord.setSmsContent(smsMsg);
        sendRecord.setCreateTime(new Date());
        if (result.isSucc()){
            sendRecord.setStatus(Constants.SMS_STATUS_0);
        }else{
            sendRecord.setStatus(Constants.SMS_STATUS_1);
        }
        sendRecord.setRemark(result.getMsg());
        smsSendRecordMapper.insert(sendRecord);
        return result.isSucc();
    }

    /**
     * 发送短信
     * @param sendRequest
     * @return
     */
    private ComResult<SMSSendDataResult> send(SMSSendRequest sendRequest){
        if (StringUtils.isBlank(sendRequest.getSerialNumber())){
            sendRequest.setSerialNumber(IdWorker.getIdStr());
        }
        SMSApi api = SmsUtils.apiProvider.getApi(ApiEnum.SENDSMS);
        return api.request(sendRequest);
    }

    /**
     * 发送短信（每月15号）
     */
    public void sendSmsTask(){
        //获取数据
        List<Long> userIds = basicInfoMapper.selectUserId(DateUtils.getCurrentYear());
        if (StringUtils.isEmpty(userIds)){
            return;
        }
        //发送短信
        for (Long userId:userIds) {
            SmsVO vo = new SmsVO();
            vo.setType("3");
            vo.setUserId(userId);
            vo.setParams(Collections.singletonList(DateUtils.getCurrentMonthNoFormat()));
            sendSmsAsyn(vo);
        }
    }

    /**
     * 状态报告
     */
    public ComResult<List<SMSReportResult>> getSmsSendStatus() {
        //请求接口
        SMSReportAPI api = SmsUtils.apiProvider.getApi(ApiEnum.REPORTSMS);
        //请求
        return api.request(new SMSReportRequest());
    }

    /**
     * 分页列表
     * @param params
     * @return
     */
    public List<SmsSendRecord> listPage(SmsSendRecord params){
        return smsSendRecordMapper.listPage(params);
    }


}
