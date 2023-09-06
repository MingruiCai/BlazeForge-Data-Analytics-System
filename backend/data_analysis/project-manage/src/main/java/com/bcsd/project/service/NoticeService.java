package com.bcsd.project.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.constants.Constants;
import com.bcsd.project.domain.Notice;
import com.bcsd.project.domain.NoticeMsg;
import com.bcsd.project.domain.vo.SmsVO;
import com.bcsd.project.mapper.NoticeMapper;
import com.bcsd.project.mapper.NoticeMsgMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 通知公告表 服务实现类
 * @author liuliang
 * @since 2023-05-29
 */
@Slf4j
@Service
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> implements IService<Notice> {

    @Autowired
    NoticeMsgMapper msgMapper;
    @Autowired
    SmsService smsService;

    /**
     * 保存通知
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveNotice(Notice params){
        //修改清除消息体
        if (params.getId()!=null){
            delMsg(params.getId());
        }
        if (StringUtils.isNotEmpty(params.getFiles())){
            params.setFileJson(JSON.toJSONString(params.getFiles()));
        }else{
            params.setFileJson("");
        }
        //保存通知
        saveOrUpdate(params);
        //保存消息
        addMsg(params);
    }

    /**
     * 删除通知
     * @param noticeId
     */
    @Transactional(rollbackFor = Exception.class)
    public void delNotice(Long noticeId){
        if (removeById(noticeId)){
            delMsg(noticeId);
        }
    }

    /**
     * 新增消息
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public void addMsg(Notice params){
        for (Long userId:params.getUserIds()) {
            NoticeMsg msg = new NoticeMsg(params.getId(),userId);
            msg.setCreateBy(params.getUpdateBy());
            msg.setCreateTime(params.getUpdateTime());
            msgMapper.insert(msg);
            //发送短信(异步)
            if (Constants.NOTICE_STATUS_1 == params.getStatus()){
                SmsVO vo = new SmsVO();
                vo.setType("2");
                vo.setUserId(userId);
                vo.setParams( Collections.singletonList(params.getTitle()));
                smsService.sendSmsAsyn(vo);
            }
        }
    }

    /**
     * 删除消息
     * @param noticeId
     */
    @Transactional(rollbackFor = Exception.class)
    public void delMsg(Long noticeId){
        LambdaQueryWrapper<NoticeMsg> wrapper = new LambdaQueryWrapper();
        wrapper.eq(NoticeMsg::getNoticeId,noticeId);
        msgMapper.delete(wrapper);
    }

    /**
     * 发送通知列表
     * @param params
     * @return
     */
    public List<Notice> sendList(Notice params){
        return super.baseMapper.sendList(params);
    }

    /**
     * 接收通知列表
     * @param params
     * @return
     */
    public List<Notice> receiveList(Notice params){
        return super.baseMapper.receiveList(params);
    }

    /**
     * 接收通知详情
     * @param params
     * @return
     */
    public Notice receiveInfo(Notice params){
        Notice notice = super.baseMapper.receiveInfo(params);
        //如果状态为未读，获取详情时变已读
        if (notice.getState()==0){
            readMsg(Long.valueOf(notice.getMsgId()),params.getCreateBy());
        }
        return notice;
    }

    /**
     * 发送通知详情
     * @param id
     * @return
     */
    public Notice sendInfo(Long id){
        Notice notice = getById(id);
        if (notice!=null){
            notice.setUserList(msgMapper.selectByNoticeId(id));
        }
        return notice;
    }

    /**
     * 阅读通知消息
     * @param msgId
     * @param userName
     */
    public void readMsg(Long msgId,String userName){
        NoticeMsg msg = new NoticeMsg();
        msg.setId(msgId);
        msg.setState(1);
        msg.setUpdateBy(userName);
        msg.setUpdateTime(DateUtils.getNowDate());
        msgMapper.updateById(msg);
    }
}
