package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.NoticeMsg;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 通知公告消息表 Mapper 接口
 *
 * @author liuliang
 * @since 2023-05-29
 */
@Repository
public interface NoticeMsgMapper extends BaseMapper<NoticeMsg> {

    List<Map> selectByNoticeId(Long noticeId);

}
