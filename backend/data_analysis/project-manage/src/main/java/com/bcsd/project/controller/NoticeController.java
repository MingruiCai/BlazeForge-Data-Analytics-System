package com.bcsd.project.controller;

import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.project.constants.Constants;
import com.bcsd.project.domain.Notice;
import com.bcsd.project.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 通知公告表 控制器
 *
 * @author liuliang
 * @since 2023-05-29
 */
@Slf4j
@RestController
@RequestMapping("/notice")
public class NoticeController extends BaseController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 通知保存
     * @param params
     * @return
     */
    @PostMapping("/save")
    public AjaxResult save(@Valid @RequestBody Notice params){
        params.setCreateBy(getUsername());
        params.setCreateTime(DateUtils.getNowDate());
        params.setUpdateBy(params.getCreateBy());
        params.setUpdateTime(params.getCreateTime());
        if (params.getId()==null){
            params.setDeptId(getDeptId());
            params.setUserId(getUserId());
            params.setRoleKey(getOneRoleKey());
        }else{
            params.setCreateBy(null);
            params.setCreateTime(null);
            Notice notice = noticeService.getById(params.getId());
            if (Constants.NOTICE_STATUS_1 == notice.getStatus()){
                return error("通知已发布，不能编辑！");
            }
        }
        noticeService.saveNotice(params);
        return success();
    }

    /**
     * 删除通知
     * @param id
     * @return
     */
    @PostMapping("/del/{id}")
    public AjaxResult delete(@PathVariable Long id){
        Notice notice = noticeService.getById(id);
        if (Constants.NOTICE_STATUS_1 == notice.getStatus()){
            return error("通知已发布，不能删除！");
        }
        noticeService.delNotice(id);
        return success();
    }

    /**
     * 发送列表
     * @param params
     * @return
     */
    @PostMapping("/send/list")
    public TableDataInfo sendList(@RequestBody Notice params){
        startPage(params);
        params.setUserId(getUserId());
        params.setDeptId(getDeptId());
        params.setRoleKey(getOneRoleKey());
        return getDataTable(noticeService.sendList(params),true);
    }

    /**
     * 接收列表
     * @param params
     * @return
     */
    @PostMapping("/receive/list")
    public TableDataInfo receiveList(@RequestBody Notice params){
        startPage(params);
        params.setUserId(getUserId());
        return getDataTable(noticeService.receiveList(params),true);
    }

    /**
     * 接收通知详情
     * @param id
     * @return
     */
    @PostMapping("/receive/{id}")
    public AjaxResult receiveInfo(@PathVariable Long id){
        Notice params = new Notice();
        params.setId(id);
        params.setUserId(getUserId());
        params.setCreateBy(getUsername());
        return success(noticeService.receiveInfo(params));
    }

    /**
     * 发送通知详情
     * @param id
     * @return
     */
    @PostMapping("/send/{id}")
    public AjaxResult sendInfo(@PathVariable Long id){
        return success(noticeService.sendInfo(id));
    }

    /**
     * 阅读消息
     * @param msgId
     * @return
     */
    @PostMapping("/read/{msgId}")
    public AjaxResult readMsg(@PathVariable Long msgId){
        noticeService.readMsg(msgId,getUsername());
        return success();
    }
}

