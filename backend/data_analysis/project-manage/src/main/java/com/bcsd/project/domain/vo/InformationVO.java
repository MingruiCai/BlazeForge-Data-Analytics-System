package com.bcsd.project.domain.vo;

import com.bcsd.common.utils.StringUtils;
import lombok.Data;

import java.util.List;

@Data
public class InformationVO {

    /**
     * 计划年度集合
     */
    private List<String> planYears;

    /**
     * 年
     */
    private String year;

    /**
     * 月
     */
    private String month;

    /**
     * 是否当年 1-是 0-否
     */
    private Integer isCurrentYear;

    /**
     * 省市区县
     */
    private String cityDistrict;

    /**
     * 省市区县截止
     */
    private String cityDistrictEnd;

    public String getCityDistrictEnd() {
        if (StringUtils.isNotBlank(cityDistrict)){
            if ("00".equals(cityDistrict.substring(2,4))){
                return cityDistrict.substring(0,2)+"9999";
            }else if ("00".equals(cityDistrict.substring(4,6))){
                return cityDistrict.substring(0,4)+"99";
            }else{
                return cityDistrict;
            }
        }
        return cityDistrictEnd;
    }
}
