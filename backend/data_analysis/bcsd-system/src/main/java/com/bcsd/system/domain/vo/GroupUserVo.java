package com.bcsd.system.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class GroupUserVo {

    private Long groupId;

    private List<Long> userIds;

}
