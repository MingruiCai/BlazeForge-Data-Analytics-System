package com.bcsd.project.controller;

import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.domain.ProjectPlanManage;
import com.bcsd.project.domain.ProjectPlanYearManage;
import com.bcsd.project.domain.vo.VerifyVO;
import com.bcsd.project.service.ProjectPlanManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 计划管理 控制器
 * @author liuliang
 * @since 2023-02-20
 */
@Slf4j
@RestController
@RequestMapping("/project/plan/manage")
public class ProjectPlanManageController extends BaseController {

    @Autowired
    private ProjectPlanManageService planManageService;

    /**
     * 计划添加时回填信息
     * @param id
     * @return
     */
    @PostMapping("/back/fill/{id}")
    public AjaxResult backFill(@PathVariable Long id){
        return success(planManageService.getByRLId(id));
    }

    /**
     * 保存计划
     */
    @PostMapping("/save")
    public AjaxResult save(@Validated @RequestBody ProjectPlanManage params){
        if (params.getPlanYear()==null){
            return AjaxResult.error("计划年度不能为空!");
        }
        params.setUserId(getUserId());
        if (params.getId()==null){
            params.setCreateBy(getUsername());
            params.setCreateTime(DateUtils.getNowDate());
            params.setUpdateBy(params.getCreateBy());
            params.setUpdateTime(params.getCreateTime());
        }else{
            params.setUpdateBy(getUsername());
            params.setUpdateTime(DateUtils.getNowDate());
        }
        return planManageService.savePlan(params,getRoleKeys());
    }

    /**
     * 分页列表
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody ProjectPlanManage params){
        Map<String,Object> map = params.getParams();
        map.put("roleKey",getOneRoleKey());
        startPage(params);
        List<ProjectPlanManage> list = planManageService.list(params);
        return getDataTable(list,true);
    }

    /**
     * 获取计划详情
     */
    @PostMapping("/info/{id}")
    public AjaxResult info(@PathVariable("id") Long id){
        return success(planManageService.getInfoById(id));
    }

    /**
     * 审核
     */
    @PostMapping("/verify")
    public AjaxResult verify(@RequestBody VerifyVO params){
        params.setUserName(getUsername());
        params.setRoleKeys(getRoleKeys());
        return planManageService.verify(params);
    }

    /**
     * 批量审核
     */
    @PostMapping("/batch/verify")
    public AjaxResult batchVerify(@RequestBody VerifyVO params){
        if (StringUtils.isEmpty(params.getIds())){
            return error("ID集合不能为空！");
        }
        params.setUserName(getUsername());
        params.setRoleKeys(getRoleKeys());
        planManageService.batchVerify(params);
        return success();
    }

    /**
     * 下达计划
     */
    @PostMapping("/release/{id}")
    public AjaxResult releasePlan(@PathVariable("id") Long id){
        return planManageService.releasePlan(id,getUsername());
    }

    /**
     * 删除计划
     */
    @PostMapping("/del/{id}")
    public AjaxResult delPlan(@PathVariable("id") Long id){
        return planManageService.delPlan(id,getUsername());
    }

    /**
     * 计划年度保存
     * @param params
     * @return
     */
    @PostMapping("/year/save")
    public AjaxResult savePlanYear(@RequestBody ProjectPlanYearManage params){
        //是否存在
        if (planManageService.planYearIsExist(params.getPlanYear(),params.getId())){
            return error("计划年度已存在");
        }
        params.setCreateBy(getUsername());
        return toAjax(planManageService.savePlanYear(params));
    }

    /**
     * 删除计划年度
     * @param id
     * @return
     */
    @PostMapping("/year/delete/{id}")
    public AjaxResult deletePlanYear(@PathVariable("id") Long id){
        return toAjax(planManageService.deletePlanYear(id,getUsername()));
    }

    /**
     * 计划年度列表
     * @return
     */
    @PostMapping("/year/list")
    public AjaxResult getYearAll(){
        return success(planManageService.getPlanYears());
    }

    /**
     * 查询最大计划年度和计划资金安排情况
     * @return
     */
    @PostMapping("/fund/{type}")
    public AjaxResult getPlanFund(@PathVariable("type") Integer type){
        return success(planManageService.getPlanFundInfo(type));
    }

    /**
     * 根据区县和计划年度查询
     * @param params
     * @return
     */
    @PostMapping("/list/info")
    public AjaxResult listInfo(@RequestBody ProjectPlanManage params){
        if (StringUtils.isNull(params.getDeptId())){
            return error("区县不能为空！");
        }
        if (params.getPlanYear()==null){
            return error("计划年度不能为空！");
        }
        return success(planManageService.selectByDeptIdAndPlanYear(params));
    }

    /**
     * 根据编号获取项目名称
     * @return
     */
    @PostMapping("/get/{no}")
    public AjaxResult getByNo(@PathVariable("no") String no){
        return success(planManageService.getProjectNameByNo(no));
    }

    /**
     * 撤回
     * @param id
     * @return
     */
    @PostMapping("/rollback/{id}")
    public AjaxResult rollback(@PathVariable("id") Long id){
        VerifyVO params = new VerifyVO();
        params.setId(id);
        params.setRoleKeys(getRoleKeys());
        params.setUserName(getUsername());
        return planManageService.rollback(params);
    }
}

