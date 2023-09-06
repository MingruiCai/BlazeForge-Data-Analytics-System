package com.bcsd.project.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class LedgerProgressVO {

    private String id;

    /*名称*/
    private String name;

    /*总数量*/
    private Integer num;

    /*未提交*/
    private Integer notReport;

    /*已提交*/
    private Integer report;

    /*未审核通过*/
    private Integer notApprove;

    /*已审核通过*/
    private Integer approve;

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<LedgerProgressVO> children;

    public LedgerProgressVO() {
    }

    public LedgerProgressVO(String name) {
        this.name = name;
        this.setId(IdWorker.getIdStr());
        this.num = 0;
        this.notReport = 0;
        this.report = 0;
        this.notApprove = 0;
        this.approve = 0;
    }

    public void add(LedgerProgressVO vo){
        this.num += vo.getNum();
        this.notReport += vo.getNotReport();
        this.report += vo.getReport();
        this.notApprove += vo.getNotApprove();
        this.approve += vo.getApprove();
    }
}
