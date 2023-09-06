package com.bcsd.project.controller;

import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.domain.ProjectImplInfoSnapshot;
import com.bcsd.project.service.ProjectImplInfoSnapshotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目实施信息快照 控制器
 *
 * @author liuliang
 * @since 2023-02-28
 */
@Slf4j
@RestController
@RequestMapping("/project/impl/info/snapshot")
public class ProjectImplSnapshotController extends BaseController {

    @Autowired
    private ProjectImplInfoSnapshotService snapshotService;

    /**
     * 数据快照生成
     * @param yearMonth
     * @return
     */
    @PostMapping("/generate/{yearMonth}")
    public AjaxResult generateDataSnapshot(@PathVariable("yearMonth") String yearMonth){
        String year=null,month=null;
        if (StringUtils.isBlank(yearMonth)){
            year = DateUtils.getCurrentYear();
            month = DateUtils.getCurrentMonth();
        }else{
            String[] ym = yearMonth.split("-");
            year = ym[0];
            month = ym[1];
        }
        return snapshotService.generateDataSnapshot(year,month,getUsername());
    }

    /**
     * 分页列表
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody ProjectImplInfoSnapshot params){
        startPage(params);
        List<ProjectImplInfoSnapshot> list = snapshotService.list(params);
        return getDataTable(list,true);
    }

}

