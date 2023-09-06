package com.bcsd.project.domain.vo;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class VerifyVO {

    private Long id;

    private List<Long> ids;

    /*描述*/
    private String description;

    /*是否通过*/
    private Boolean pass;

    /*操作用户*/
    private String userName;

    /*角色标识*/
    private Set<String> roleKeys;

    /*实体对象JSON*/
    private JSONObject obj;

    /*3-审核未通过*/
    private Integer state;

}
