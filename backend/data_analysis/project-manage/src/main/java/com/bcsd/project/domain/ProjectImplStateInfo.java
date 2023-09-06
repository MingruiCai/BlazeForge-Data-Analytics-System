package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 实施情况
 *
 * @author liuliang
 * @since 2023-02-23
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("project_impl_state_info")
public class ProjectImplStateInfo extends BaseInfo {

    private static final long serialVersionUID = 1L;
    /*项目编号*/
    private String projectNo;
    //工作阶段
    private String workPhase;
    /*项目建议书-批复日期*/
    private String ppAd;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> ppAdList;
    /*项目建议书-估算投资*/
    private String ppEi;
    /*项目建议书-初步选址意见  Y N*/
    private String ppIsi;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> ppIsiList;
    /*可行性研究-批复日期*/
    private String fsAd;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> fsAdList;
    /*可行性研究-估算投资*/
    private String fsEi;
    /*可行性研究-初步选址意见 Y N*/
    private String fsIsi;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> fsIsiList;
    /*可行性研究-用地审批 Y N*/
    private String fsLa;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> fsLaList;
    /*可行性研究-节能评估审查  Y N*/
    private String fsEeee;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> fsEeeeList;
    /*可行性研究-环境影响评价文件审批   Y N*/
    private String fsEada;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> fsEadaList;
    /*可行性研究-水土保持方案审批  Y N*/
    private String fsWsrsa;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> fsWsrsaList;
    /*初步设计-初设批复日期*/
    private String idIdad;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> idIdadList;
    /*初步设计-概算批复日期*/
    private String idFead;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> idFeadList;
    /*初步设计-概算投资（万元）-直接费*/
    private String idFeDf;
    /*初步设计-概算投资（万元）-其它费*/
    private String idFeOf;
    /*初步设计-概算投资（万元）-预备费*/
    private String idFeRf;
    /*初步设计-概算投资（万元）-总计*/
    private String idFeTotal;
    /*初步设计-用地规划许可证  Y N*/
    private String idLpp;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> idLppList;
    /*施工图设计-工程预算金额*/
    private String cddPba;
    /*施工图设计-施工图批复或通过审查日期*/
    private String cddCdad;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> cddCdadList;
    /*施工图设计-施工图预算通过审核日期*/
    private String cddCdbad;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> cddCdbadList;
    /*施工图设计-施工招标模式*/
    private String cddCbm;
    /*施工图设计-工程规划许可证  Y N*/
    private String cddEpp;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> cddEppList;
    /*施工图设计-预算评审完成日期*/
    private String cddBrcd;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> cddBrcdList;
    /*施工图招标-招标公告发布日期*/
    private String cdbBard;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> cdbBardList;
    /*施工图招标-招标最高限价（万元）*/
    private String cdbBcp;
    /*施工图招标-开标日期*/
    private String cdbBod;
    /*施工图招标-中标候选人公示开始日期*/
    private String cdbBwcpsd;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> cdbBwcpsdList;
    /*施工图招标-中标结果公示开始日期*/
    private String cdbBwrpsd;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> cdbBwrpsdList;
    /*施工图招标-中标通知书发出日期*/
    private String cdbBwnid;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> cdbBwnidList;
    /*施工图招标-合同签订日期*/
    private String cdbCsd;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> cdbCsdList;
    /*开工-开工日期*/
    private String swSwd;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> swSwdList;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> swCylipList;
    /*完工情况-完工日期*/
    private String ccCd;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> ccCdList;
    /*交工验收-验收日期*/
    private String haAd;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> haAdList;
    /*工程结算-结算日期*/
    private String psSd;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> psSdList;
    /*工程结算-结算审计金额（万元）*/
    private String psSaa;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> psSaaList;
    /*竣工验收-验收日期*/
    private String caAd;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> caAdList;
    /*竣工验收-消防验收或竣工验收备案  Y N*/
    private String caCaf;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> caCafList;
    /*竣工验收-环境保护设施竣工验收  Y N*/
    private String caEpfca;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> caEpfcaList;
    /*竣工验收-水土保持设施竣工验收  Y N*/
    private String caWscfca;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> caWscfcaList;
    /*竣工验收-防雷装置竣工验收  Y N*/
    private String caLpdca;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> caLpdcaList;
    /*竣工财务决算-决算批复文号*/
    private String cfsFaano;
    /*竣工财务决算-决算批复时间*/
    private String cfsFaad;
    @TableField(exist = false)
    private List<ProjectImplFileInfo> cfsFaadList;
    /*竣工财务决算-决算金额（万元）*/
    private String cfsFaa;
    /*竣工财务决算-决算金额-专项资金（万元）*/
    private String cfsFaaSf;
}
