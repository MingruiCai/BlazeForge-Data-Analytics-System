package com.bcsd.project.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.fastjson2.JSONObject;
import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.common.utils.file.FileUtils;
import com.bcsd.project.domain.LedgerDistrictProgress;
import com.bcsd.project.domain.LedgerDistrictProjectProgress;
import com.bcsd.project.domain.LedgerProvinceProgress;
import com.bcsd.project.domain.vo.VerifyVO;
import com.bcsd.project.service.ProgressLedgerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/progress/ledger")
public class ProgressLedgerController extends BaseController {

    @Autowired
    HttpServletResponse response;
    @Autowired
    ProgressLedgerService progressLedgerService;

    /**
     * 一键生成台账
     * @param params
     * @return
     */
    @PostMapping("/generate")
    public AjaxResult generate(@Validated @RequestBody LedgerDistrictProgress params){
        params.setCreateBy(getUsername());
        params.setDeptId(getDeptId());
        return progressLedgerService.generate(params);
    }

    /**
     * 统计
     * @return
     */
    @PostMapping("/statistics")
    public AjaxResult statistics(){
        return success(progressLedgerService.statistics());
    }

    /**
     * 区县报表分页列表
     */
    @PostMapping("/district/list")
    public TableDataInfo districtList(@RequestBody LedgerDistrictProgress params){
        startPage(params);
        List<LedgerDistrictProgress> list = progressLedgerService.districtList(params);
        return getDataTable(list,true);
    }

    /**
     * 获取区县报表详情
     */
    @PostMapping("/district/info/{id}")
    public AjaxResult getDistrictInfo(@PathVariable("id") Long id){
        return success(progressLedgerService.getDistrictInfoById(id));
    }

    /**
     * 省市报表分页列表
     */
    @PostMapping("/province/list")
    public TableDataInfo provinceList(@RequestBody LedgerProvinceProgress params){
        startPage(params);
        List<LedgerProvinceProgress> list = progressLedgerService.provinceList(params);
        return getDataTable(list,true);
    }

    /**
     * 获取省市报表详情
     */
    @PostMapping("/province/info/{id}")
    public AjaxResult getProvinceInfo(@PathVariable("id") Long id){
        return success(progressLedgerService.getProvinceInfoById(id));
    }

    /**
     * 导出
     * @param id
     * @return
     */
    @PostMapping("/province/export/{id}")
    public AjaxResult export(@PathVariable("id") Long id) throws IOException {
        //获取导出数据
        JSONObject data = progressLedgerService.export(id);
        if (data==null){
            return error("无数据无法导出！");
        }
        //设置导出文件名
        FileUtils.setAttachmentResponseHeader(response, StringUtils.join(data.getString("year"),"年度",data.getString("month"),"月三峡后续工作项目进展情况台账.xlsx"));
        //获取模板并导出
        InputStream is = this.getClass().getResourceAsStream("/template/progress_ledger_template.xlsx");
        try(ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(is).build()){
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            excelWriter.fill(new FillWrapper("list",data.getList("list", LedgerDistrictProjectProgress.class) ), writeSheet);
            excelWriter.fill(data,writeSheet);
        }
        return success();
    }

}
