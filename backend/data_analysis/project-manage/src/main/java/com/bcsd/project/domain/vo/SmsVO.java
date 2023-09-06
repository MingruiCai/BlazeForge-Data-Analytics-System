package com.bcsd.project.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class SmsVO {

    /**
     * 类型 1-整改通知 2-发送通知 3-上报通知
     */
    private String type;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 参数
     */
    private List<String> params;

}
