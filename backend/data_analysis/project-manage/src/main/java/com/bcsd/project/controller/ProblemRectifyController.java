package com.bcsd.project.controller;

import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.project.domain.ProblemRectifyNotice;
import com.bcsd.project.domain.ProblemRectifyRecord;
import com.bcsd.project.domain.vo.BindUserVO;
import com.bcsd.project.service.ProblemRectifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 问题整改 控制器
 * @author liuliang
 * @since 2023-03-20
 */
@Slf4j
@RestController
@RequestMapping("/problem/rectify")
public class ProblemRectifyController extends BaseController {

    @Autowired
    ProblemRectifyService problemRectifyService;

    /**
     * 保存通知
     */
    @PostMapping("/notice/save")
    public AjaxResult saveNotice(@Validated @RequestBody ProblemRectifyNotice params){
        params.setCreateBy(getUsername());
        return toAjax(problemRectifyService.saveNotice(params));
    }

    /**
     * 删除通知
     * @param id
     * @return
     */
    @PostMapping("/notice/delete/{id}")
    public AjaxResult deleteNotice(@PathVariable("id") Long id) {
        return toAjax(problemRectifyService.deleteNotice(id,getUsername()));
    }

    /**
     * 通知分页列表
     * @param params
     * @return
     */
    @PostMapping("/notice/list")
    public TableDataInfo noticeListPage(@RequestBody ProblemRectifyNotice params){
        startPage(params);
        return getDataTable(problemRectifyService.noticeListPage(params),true);
    }

    /**
     * 保存记录
     */
    @PostMapping("/record/save")
    public AjaxResult saveRecord(@Validated @RequestBody ProblemRectifyRecord params){
        params.setCreateBy(getUsername());
        return toAjax(problemRectifyService.saveRecord(params));
    }

    /**
     * 删除记录
     * @param id
     * @return
     */
    @PostMapping("/record/delete/{id}")
    public AjaxResult deleteRecord(@PathVariable("id") Long id) {
        return toAjax(problemRectifyService.deleteRecord(id,getUsername()));
    }

    /**
     * 获取记录
     * @param id
     * @return
     */
    @PostMapping("/record/get/{id}")
    public AjaxResult getRecord(@PathVariable("id") Long id) {
        return success(problemRectifyService.getById(id));
    }

    /**
     * 问题记录分页列表
     * @param params
     * @return
     */
    @PostMapping("/record/list")
    public TableDataInfo recordListPage(@RequestBody ProblemRectifyRecord params){
        startPage(params);
        return getDataTable(problemRectifyService.recordListPage(params),true);
    }

    /**
     * 指派用户
     * @param params
     * @return
     */
    @PostMapping("/record/bind/user")
    public AjaxResult bindUser(@RequestBody BindUserVO params){
        return toAjax(problemRectifyService.bindUser(params));
    }

    /**
     * 处理
     * @param params
     * @return
     */
    @PostMapping("/record/handle")
    public AjaxResult handle(@RequestBody ProblemRectifyRecord params){
        params.setUpdateBy(getUsername());
        params.setUpdateTime(DateUtils.getNowDate());
        problemRectifyService.handle(params);
        return success();
    }


}

