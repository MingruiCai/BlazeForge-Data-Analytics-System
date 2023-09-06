package com.bcsd.project.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.common.annotation.DataScope;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.constants.Constants;
import com.bcsd.project.domain.ProblemRectifyNotice;
import com.bcsd.project.domain.ProblemRectifyRecord;
import com.bcsd.project.domain.ProjectEarlyWarning;
import com.bcsd.project.domain.ProjectImplBasicInfo;
import com.bcsd.project.domain.vo.BindUserVO;
import com.bcsd.project.domain.vo.SmsVO;
import com.bcsd.project.mapper.ProblemRectifyNoticeMapper;
import com.bcsd.project.mapper.ProblemRectifyRecordMapper;
import com.bcsd.project.mapper.ProjectEarlyWarningMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 问题整改 服务实现类
 * @author liuliang
 * @since 2023-03-20
 */
@Slf4j
@Service
public class ProblemRectifyService extends ServiceImpl<ProblemRectifyRecordMapper, ProblemRectifyRecord> implements IService<ProblemRectifyRecord> {

    @Autowired
    ProblemRectifyNoticeMapper noticeMapper;
    @Autowired
    ProjectEarlyWarningMapper earlyWarningMapper;
    @Autowired
    ProjectImplService projectImplService;
    @Autowired
    SmsService smsService;

    /**
     * 保存通知信息
     * @param params
     * @return
     */
    public int saveNotice(ProblemRectifyNotice params){
        if (StringUtils.isNotEmpty(params.getNoticeFileList())){
            params.setNoticeFile(JSON.toJSONString(params.getNoticeFileList()));
        }else{
            params.setNoticeFile("");
        }
        if (params.getId()==null){
            params.setCreateTime(DateUtils.getNowDate());
            params.setUpdateBy(params.getCreateBy());
            params.setUpdateTime(params.getCreateTime());
            return noticeMapper.insert(params);
        }else{
            params.setUpdateBy(params.getCreateBy());
            params.setUpdateTime(DateUtils.getNowDate());
            params.setCreateBy(null);
            return noticeMapper.updateById(params);
        }
    }

    /**
     * 删除通知信息
     * @param id
     * @param userName
     * @return
     */
    public int deleteNotice(Long id,String userName){
        ProblemRectifyNotice notice = new ProblemRectifyNotice();
        notice.setId(id);
        notice.setUpdateBy(userName);
        notice.setUpdateTime(DateUtils.getNowDate());
        notice.setDeleteTag(Constants.DELETE_TAG_1);
        return noticeMapper.updateById(notice);
    }

    /**
     * 通知列表分页查询
     * @param params
     * @return
     */
    public List<ProblemRectifyNotice> noticeListPage(ProblemRectifyNotice params){
        LambdaQueryWrapper<ProblemRectifyNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProblemRectifyNotice::getDeleteTag,Constants.DELETE_TAG_0);
        if (StringUtils.isNotBlank(params.getNoticeName())){
            wrapper.like(ProblemRectifyNotice::getNoticeName,params.getNoticeName());
        }
        if (StringUtils.isNotBlank(params.getNoticeNo())){
            wrapper.like(ProblemRectifyNotice::getNoticeNo,params.getNoticeNo());
        }
        wrapper.orderByDesc(ProblemRectifyNotice::getUpdateTime);
        return noticeMapper.selectList(wrapper);
    }

    /**
     * 保存问题记录
     * @param params
     * @return
     */
    public boolean saveRecord(ProblemRectifyRecord params){
        if (StringUtils.isNotEmpty(params.getNoticeSubFileList())){
            params.setNoticeSubFile(JSON.toJSONString(params.getNoticeSubFileList()));
        }else{
            params.setNoticeSubFile("");
        }
        if (params.getId()==null){
            params.setRectifyStatus("dzg");
            params.setCreateTime(DateUtils.getNowDate());
            params.setUpdateBy(params.getCreateBy());
            params.setUpdateTime(params.getCreateTime());
            //查询项目信息
            ProjectImplBasicInfo pibi = projectImplService.getProjectNameByNo(params.getProjectNo());
            if (pibi!=null){
                params.setUserId(pibi.getUserId());
            }
        }else{
            params.setUpdateBy(params.getCreateBy());
            params.setUpdateTime(DateUtils.getNowDate());
            params.setCreateBy(null);
        }
        return super.saveOrUpdate(params);
    }

    /**
     * 删除问题记录
     * @param id
     * @param userName
     * @return
     */
    public boolean deleteRecord(Long id,String userName){
        ProblemRectifyRecord record = new ProblemRectifyRecord();
        record.setId(id);
        record.setUpdateBy(userName);
        record.setUpdateTime(DateUtils.getNowDate());
        record.setDeleteTag(Constants.DELETE_TAG_1);
        return super.updateById(record);
    }

    /**
     * 问题记录列表分页列表
     * @param params
     * @return
     */
    @DataScope(deptAlias = "prr",userAlias = "prr")
    public List<ProblemRectifyRecord> recordListPage(ProblemRectifyRecord params){
        //时间区间处理
        if (StringUtils.isNotEmpty(params.getMonitorTimeList())&&params.getMonitorTimeList().size()>1){
            params.setMonitorTimeBegin(params.getMonitorTimeList().get(0));
            params.setMonitorTimeEnd(params.getMonitorTimeList().get(1));
        }
        if (StringUtils.isNotEmpty(params.getRectifyDeadlineList())&&params.getRectifyDeadlineList().size()>1){
            params.setRectifyDeadlineBegin(params.getRectifyDeadlineList().get(0));
            params.setRectifyDeadlineEnd(params.getRectifyDeadlineList().get(1));
        }
        if (StringUtils.isNotEmpty(params.getRectifyTimeList())&&params.getRectifyTimeList().size()>1){
            params.setRectifyTimeBegin(params.getRectifyTimeList().get(0));
            params.setRectifyTimeEnd(params.getRectifyTimeList().get(1));
        }
        return super.baseMapper.listPage(params);
    }

    /**
     * 绑定用户
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean bindUser(BindUserVO params){
        //查询通知内容
        List<ProblemRectifyRecord> records = this.listByIds(params.getIds());
        for (ProblemRectifyRecord record:records) {
            record.setInform(StringUtils.join(params.getUserIds(),","));
            boolean result = updateById(record);
            //发送短信
            if (result){
                //项目负责人
                SmsVO smsVO = new SmsVO();
                smsVO.setType("1");
                smsVO.setUserId(record.getUserId());
                smsVO.setParams(Arrays.asList(record.getProjectName(),record.getProblemContent()));
                smsService.sendSmsAsyn(smsVO);
                //知会
                for (Long userId:params.getUserIds()) {
                    //发送短信(异步)
                    smsVO = new SmsVO();
                    smsVO.setType("1");
                    smsVO.setUserId(userId);
                    smsVO.setParams(Arrays.asList(record.getProjectName(),record.getProblemContent()));
                    smsService.sendSmsAsyn(smsVO);
                }
            }
        }
        return true;
    }

    /**
     * 处理问题
     * @param params
     */
    @Transactional
    public void handle(ProblemRectifyRecord params){
        //问题整改
        ProblemRectifyRecord updateRecord = new ProblemRectifyRecord();
        updateRecord.setId(params.getId());
        updateRecord.setRectifyTime(params.getRectifyTime());
        updateRecord.setRectifyStatus(params.getRectifyStatus());
        if (StringUtils.isNotEmpty(params.getRectifyCertificateList())){
            updateRecord.setRectifyCertificate(JSON.toJSONString(params.getRectifyCertificateList()));
        }
        updateRecord.setRemark(params.getRemark());
        updateById(updateRecord);
        //修改监测预警状态
        ProblemRectifyRecord record = getById(params.getId());
        if (record.getEarlyWarningId()!=null){
            ProjectEarlyWarning pew = new ProjectEarlyWarning();
            pew.setId(record.getEarlyWarningId());
            pew.setState(3);
            pew.setUpdateBy(params.getUpdateBy());
            pew.setUpdateTime(params.getUpdateTime());
            earlyWarningMapper.updateById(pew);
        }

    }

    /**
     * 获取详情
     * @param id
     * @return
     */
    public ProblemRectifyRecord getById(Long id){
        return super.baseMapper.getById(id);
    }

}
