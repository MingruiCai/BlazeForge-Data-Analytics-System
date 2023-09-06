package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 短信发送记录
 *
 * @author liuliang
 * @since 2023-05-29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("sms_send_record")
public class SmsSendRecord extends BaseInfo {

    private static final long serialVersionUID = 1L;

    //用户ID
    private Long userId;
    //接收信息手机号
    private String phone;
    //模板ID
    private String templateId;
    //短信内容
    private String smsContent;
    //短信状态 0-成功 1-失败
    private Integer status;
    /*用户名称*/
    @TableField(exist = false)
    private String userName;
    /*部门名称*/
    @TableField(exist = false)
    private String deptName;




}
