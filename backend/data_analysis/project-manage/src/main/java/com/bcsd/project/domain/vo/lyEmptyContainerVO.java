package com.bcsd.project.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.annotation.Excel;
import com.bcsd.common.core.domain.BaseInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 空容器
 */
@Data
public class lyEmptyContainerVO {

    private String podTypText;

    private String podCode;

    private String initStatusText;

    private String berthCode;

    private String binUtilization;


}