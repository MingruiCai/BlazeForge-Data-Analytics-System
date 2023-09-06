package com.bcsd.project.domain;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.bcsd.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 项目实施资金情况明细
 *
 * @author liuliang
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("project_impl_fund_detail_info")
public class ProjectImplFundDetailInfo extends BaseInfo {
    private static final long serialVersionUID = 1L;

    /*项目编号*/
    private String projectNo;

    /*资金类型*/
    private Integer fundType;

    /*资金年份*/
    private String fundYear;

    /*总计*/
    private String fundTotal;

    /*1月*/
    @TableField("month_1")
    private String month1;
    /*2月*/
    @TableField("month_2")
    private String month2;
    /*3月*/
    @TableField("month_3")
    private String month3;
    /*4月*/
    @TableField("month_4")
    private String month4;
    /*5月*/
    @TableField("month_5")
    private String month5;
    /*6月*/
    @TableField("month_6")
    private String month6;
    /*7月*/
    @TableField("month_7")
    private String month7;
    /*8月*/
    @TableField("month_8")
    private String month8;
    /*9月*/
    @TableField("month_9")
    private String month9;
    /*10月*/
    @TableField("month_10")
    private String month10;
    /*11月*/
    @TableField("month_11")
    private String month11;
    /*12月*/
    @TableField("month_12")
    private String month12;

    /*1月*/
    @JsonIgnore
    @TableField("month_1_json")
    private String month1Json;
    /*2月*/
    @JsonIgnore
    @TableField("month_2_json")
    private String month2Json;
    /*3月*/
    @JsonIgnore
    @TableField("month_3_json")
    private String month3Json;
    /*4月*/
    @JsonIgnore
    @TableField("month_4_json")
    private String month4Json;
    /*5月*/
    @JsonIgnore
    @TableField("month_5_json")
    private String month5Json;
    /*6月*/
    @JsonIgnore
    @TableField("month_6_json")
    private String month6Json;
    /*7月*/
    @JsonIgnore
    @TableField("month_7_json")
    private String month7Json;
    /*8月*/
    @JsonIgnore
    @TableField("month_8_json")
    private String month8Json;
    /*9月*/
    @JsonIgnore
    @TableField("month_9_json")
    private String month9Json;
    /*10月*/
    @JsonIgnore
    @TableField("month_10_json")
    private String month10Json;
    /*11月*/
    @JsonIgnore
    @TableField("month_11_json")
    private String month11Json;
    /*12月*/
    @JsonIgnore
    @TableField("month_12_json")
    private String month12Json;

    @TableField(exist = false)
    private List<ProjectImplFileInfo> month1List;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> month2List;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> month3List;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> month4List;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> month5List;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> month6List;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> month7List;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> month8List;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> month9List;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> month10List;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> month11List;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> month12List;

    public List<ProjectImplFileInfo> getMonth1List() {
        return getFileList(month1List,month1Json);
    }

    public List<ProjectImplFileInfo> getMonth2List() {
        return getFileList(month2List,month2Json);
    }

    public List<ProjectImplFileInfo> getMonth3List() {
        return getFileList(month3List,month3Json);
    }

    public List<ProjectImplFileInfo> getMonth4List() {
        return getFileList(month4List,month4Json);
    }

    public List<ProjectImplFileInfo> getMonth5List() {
        return getFileList(month5List,month5Json);
    }

    public List<ProjectImplFileInfo> getMonth6List() {
        return getFileList(month6List,month6Json);
    }

    public List<ProjectImplFileInfo> getMonth7List() {
        return getFileList(month7List,month7Json);
    }

    public List<ProjectImplFileInfo> getMonth8List() {
        return getFileList(month8List,month8Json);
    }

    public List<ProjectImplFileInfo> getMonth9List() {
        return getFileList(month9List,month9Json);
    }

    public List<ProjectImplFileInfo> getMonth10List() {
        return getFileList(month10List,month10Json);
    }

    public List<ProjectImplFileInfo> getMonth11List() {
        return getFileList(month11List,month11Json);
    }

    public List<ProjectImplFileInfo> getMonth12List() {
        return getFileList(month12List,month12Json);
    }

    public String getMonth1Json() {
        return getJsonStr(month1Json,month1List);
    }

    public String getMonth2Json() {
        return getJsonStr(month2Json,month2List);
    }

    public String getMonth3Json() {
        return getJsonStr(month3Json,month3List);
    }

    public String getMonth4Json() {
        return getJsonStr(month4Json,month4List);
    }

    public String getMonth5Json() {
        return getJsonStr(month5Json,month5List);
    }

    public String getMonth6Json() {
        return getJsonStr(month6Json,month6List);
    }

    public String getMonth7Json() {
        return getJsonStr(month7Json,month7List);
    }

    public String getMonth8Json() {
        return getJsonStr(month8Json,month8List);
    }

    public String getMonth9Json() {
        return getJsonStr(month9Json,month9List);
    }

    public String getMonth10Json() {
        return getJsonStr(month10Json,month10List);
    }

    public String getMonth11Json() {
        return getJsonStr(month11Json,month11List);
    }

    public String getMonth12Json() {
        return getJsonStr(month12Json,month12List);
    }

    private String getJsonStr(String jsonStr,List<ProjectImplFileInfo> files){
        if (StringUtils.isBlank(jsonStr)){
            if (StringUtils.isNotEmpty(files)){
                return JSON.toJSONString(files);
            }
            return "";
        }
        return jsonStr;
    }

    private List<ProjectImplFileInfo> getFileList(List<ProjectImplFileInfo> files,String jsonStr){
        if (StringUtils.isEmpty(files)){
            if (StringUtils.isNotBlank(jsonStr)){
                return JSON.parseArray(jsonStr,ProjectImplFileInfo.class);
            }
            return null;
        }
        return files;
    }



}
