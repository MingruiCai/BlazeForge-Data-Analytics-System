package com.bcsd.project.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProjectFileDownloadVO {

    private List<String> projectNos;

    private String type;

}
