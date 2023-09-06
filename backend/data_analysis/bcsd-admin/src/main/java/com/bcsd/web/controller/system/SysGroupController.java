package com.bcsd.web.controller.system;

import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.domain.entity.SysGroup;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.system.domain.vo.GroupUserVo;
import com.bcsd.system.service.ISysGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/group")
public class SysGroupController extends BaseController {

    @Autowired
    ISysGroupService groupService;

    /**
     * 保存
     * @param params
     * @return
     */
    @PostMapping("save")
    public AjaxResult save(@Validated @RequestBody SysGroup params){
        if (params.getId()==null){
            params.setUserId(getUserId());
            params.setCreateBy(getUsername());
            params.setCreateTime(DateUtils.getNowDate());
        }else{
            params.setUpdateBy(getUsername());
            params.setUpdateTime(DateUtils.getNowDate());
        }
        return toAjax(groupService.saveOrUpdate(params));
    }

    /**
     * 分组列表
     * @return
     */
    @PostMapping("list")
    public AjaxResult list(){
        return AjaxResult.success(groupService.getGroupListByUserId(getUserId()));
    }

    /**
     * 删除分组
     * @param id
     * @return
     */
    @PostMapping("del/{id}")
    public AjaxResult del(@PathVariable Long id){
        if (id==null){
            return error("分组ID不能为空!");
        }
        SysGroup params = new SysGroup();
        params.setId(id);
        params.setUpdateBy(getUsername());
        return toAjax(groupService.groupDel(params));
    }

    /**
     * 绑定用户
     * @param params
     * @return
     */
    @PostMapping("bind/user")
    public AjaxResult bindUser(@RequestBody GroupUserVo params){
        groupService.bindUser(params);
        return success();
    }

}
