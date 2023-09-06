package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 通知公告消息表
 *
 * @author liuliang
 * @since 2023-05-29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("notice_msg")
public class NoticeMsg extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /*通知ID*/
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long noticeId;

    /*用户ID*/
    private Long userId;

    /*是否已读 0-未读 1-已读*/
    private Integer state;

    public NoticeMsg() {
    }

    public NoticeMsg(Long noticeId, Long userId) {
        this.noticeId = noticeId;
        this.userId = userId;
        this.state = 0;
    }
}
