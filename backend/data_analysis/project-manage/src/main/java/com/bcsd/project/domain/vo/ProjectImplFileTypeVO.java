package com.bcsd.project.domain.vo;

import com.bcsd.project.domain.ProjectImplFileInfo;
import lombok.Data;

import java.util.List;

@Data
public class ProjectImplFileTypeVO {

    private String typeName;

    private String typeValue;

    private Integer fileNum;

    private List<ProjectImplFileInfo> fileList;

    public Integer getFileNum() {
        if (fileList==null){
            return 0;
        }
        return fileList.size();
    }
}
