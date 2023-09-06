package com.bcsd.project.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.fastjson2.JSONObject;
import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.common.utils.file.FileUtils;
import com.bcsd.project.domain.ProjectStatisticsTarget;
import com.bcsd.project.domain.vo.InformationVO;
import com.bcsd.project.domain.vo.ProjectImplStatisticsVO;
import com.bcsd.project.service.ProjectImplStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * 项目实施统计 控制器
 * @author liuliang
 * @since 2023-02-20
 */
@Slf4j
@RestController
@RequestMapping("/project/impl/statistics")
public class ProjectImplStatisticsController extends BaseController {

    @Autowired
    HttpServletResponse response;
    @Autowired
    private ProjectImplStatisticsService statisticsService;

    /**
     * 项目汇总表
     * @param params
     * @return
     */
    @PostMapping("/summary/list")
    public AjaxResult projectSummary(@RequestBody ProjectImplStatisticsVO params){
        return success(statisticsService.projectSummary(params));
    }

    /**
     * 首页统计
     * @param params
     * @return
     */
    @PostMapping("/home")
    public AjaxResult home(@RequestBody InformationVO params){
        return success(statisticsService.homeStatistics(params));
    }

    /**
     * 保存统计目标
     * @param params
     * @return
     */
    @PostMapping("/save/target")
    public AjaxResult saveTarget(@Validated @RequestBody ProjectStatisticsTarget params){
        params.setUserId(getUserId());
        params.setCreateBy(getUsername());
        return toAjax(statisticsService.saveTarget(params));
    }

    /**
     * 获取统计目标
     * @return
     */
    @PostMapping("/get/target")
    public AjaxResult getTarget(){
        return success(statisticsService.getByUserId(getUserId()));
    }

    /**
     * 分类统计
     * @param type
     * @return
     */
    @PostMapping("/info/{type}")
    public AjaxResult implInfoStatistic(@PathVariable("type") String type){
        switch (type){
            case "R_01": return success(statisticsService.implInfoStatistic(0));
            case "R_02": return success(statisticsService.implInfoStatistic(1));
            case "R_03": return success(statisticsService.notStartProjectList());
            case "R_04": return success();
            case "R_05": return success();
            case "R_06": return success();
            default:break;
        }
        return error("类型错误");
    }

    /**
     * 导出
     * @param type
     * @return
     */
    @PostMapping("/export/{type}")
    public AjaxResult export(@PathVariable("type") String type) throws IOException {
        //获取导出数据
        JSONObject data = statisticsService.export(type);
        if (data==null){
            return error("无数据无法导出！");
        }
        //文件名称和模板地址
        String fileName = null;
        String templatePath = null;
        switch (type){
            case "R_01":
                fileName = StringUtils.join(data.getString("year"),"年度及以前三峡后续工作项目实施情况统计表.xlsx");
                templatePath = "/template/statistics_template_1.xlsx";
                break;
            case "R_02":
                fileName = StringUtils.join(data.getString("year"),"年度三峡后续工作项目实施情况统计表.xlsx");
                templatePath = "/template/statistics_template_2.xlsx";
                break;
            case "R_03":
                fileName = StringUtils.join(data.getString("year"),"年度三峡后续提前批未开工项目清单.xlsx");
                templatePath = "/template/statistics_template_3.xlsx";
                break;
            default:return error("类型错误");
        }
        //设置导出文件名
        FileUtils.setAttachmentResponseHeader(response,fileName);
        //获取模板并导出
        InputStream is = this.getClass().getResourceAsStream(templatePath);
        try(ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(is).build()){
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            excelWriter.fill(new FillWrapper("list",data.getJSONArray("list")), writeSheet);
            excelWriter.fill(data,writeSheet);
        }
        return success();
    }

}

