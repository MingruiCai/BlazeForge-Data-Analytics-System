package com.bcsd.project.domain.vo;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bcsd.common.utils.ArithUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class ProjectDataVO {

    private String id;
    private String name;

    /*项目法人或责任单位*/
    private String projectLegalPerson;

    /*项目总投资（万元）*/
    private String projectTotalInvest;

    //年度下达专项资金(万元)
    private String yearIssueFund;

    //前期工作阶段
    private String workPhase;

    /*预计开工时间*/
    private String scheduledDate;

    /*子节点*/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProjectDataVO> children;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String index;

    public ProjectDataVO() {
    }

    public ProjectDataVO(String name) {
        this.name = name;
        this.id = IdWorker.getIdStr();
        this.projectTotalInvest = "0";
        this.yearIssueFund = "0";
    }

    public void add(ProjectDataVO params){
        this.projectTotalInvest = ArithUtil.add(getProjectTotalInvest(),params.getProjectTotalInvest());
        this.yearIssueFund = ArithUtil.add(getYearIssueFund(),params.getYearIssueFund());
    }
}
