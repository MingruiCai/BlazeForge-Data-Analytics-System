package com.bcsd.project.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatisticsQueryVO {

    private Long id;

    private String state;

    private Integer type;

}
