package com.bcsd.project.controller;

import com.alibaba.excel.EasyExcel;
import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.domain.ProjectRollCycle;
import com.bcsd.project.domain.ProjectRollLibrary;
import com.bcsd.project.domain.vo.ProjectRollLibraryVO;
import com.bcsd.project.domain.vo.ProjectUserVO;
import com.bcsd.project.domain.vo.StatisticsQueryVO;
import com.bcsd.project.domain.vo.VerifyVO;
import com.bcsd.project.service.ProjectPlanManageService;
import com.bcsd.project.service.ProjectRollCycleService;
import com.bcsd.project.service.ProjectRollLibraryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 滚动项目库 控制器
 * @author liuliang
 * @since 2023-02-16
 */
@Slf4j
@RestController
@RequestMapping("/roll/project")
public class ProjectRollLibraryController extends BaseController {

    @Autowired
    private ProjectRollCycleService cycleService;
    @Autowired
    private ProjectRollLibraryService libraryService;
    @Autowired
    private ProjectPlanManageService planManageService;

    /**
     * 新增滚动周期
     * @param params
     * @return
     */
    @PostMapping("/cycle/add")
    public AjaxResult addCycle(@Validated @RequestBody ProjectRollCycle params){
        //查询是否存在
        if (cycleService.selectByParams(params)>0){
            return error("滚动周期已存在！");
        }
        params.setCreateBy(getUsername());
        return toAjax(cycleService.add(params));
    }

    /**
     * 修改滚动周期
     * @param params
     * @return
     */
    @PostMapping("/cycle/update")
    public AjaxResult updateCycle(@Validated @RequestBody ProjectRollCycle params){
        //查询是否存在
        if (cycleService.selectByParams(params)>0){
            return error("滚动周期已存在！");
        }
        params.setUpdateBy(getUsername());
        return toAjax(cycleService.update(params));
    }

    /**
     * 删除滚动周期
     * @param id
     * @return
     */
    @PostMapping("/cycle/delete/{id}")
    public AjaxResult deleteCycle(@PathVariable("id") Long id){
        if (libraryService.selectByCycleId(id)>0){
            return error("滚动周期内还有项目存在，不能删除！");
        }
        return toAjax(cycleService.delete(id));
    }

    /**
     * 滚动周期列表
     * @return
     */
    @PostMapping("/cycle/list")
    public AjaxResult getAllCycle(){
        return success(cycleService.findAll());
    }

    /**
     * 添加滚动项目库
     */
    @PostMapping("/library/add")
    public AjaxResult addLibrary(@Validated @RequestBody ProjectRollLibrary params){
        if (params.getRollCycleId()==null){
            return AjaxResult.error("滚动周期不能为空!");
        }
        params.setUserId(getUserId());
        params.setCreateBy(getUsername());
        params.setCreateTime(DateUtils.getNowDate());
        params.setUpdateBy(params.getCreateBy());
        params.setUpdateTime(params.getCreateTime());
        return libraryService.save(params,getRoleKeys());
    }

    /**
     * 修改滚动项目库
     */
    @PostMapping("/library/update")
    public AjaxResult updateLibrary(@Validated @RequestBody ProjectRollLibrary params){
        params.setUpdateBy(getUsername());
        params.setUpdateTime(DateUtils.getNowDate());
        return libraryService.save(params,getRoleKeys());
    }

    /**
     * 删除滚动项目库
     */
    @PostMapping("/library/delete/{id}")
    public AjaxResult deleteProjectRollLibrary(@PathVariable("id") Long id){
        ProjectRollLibrary rollLibrary = libraryService.getById(id);
        if (rollLibrary!=null&&(!"-2".equals(rollLibrary.getState()))){
            return error("项目库不是草稿态，不能删除！");
        }
        return toAjax(libraryService.delete(id));
    }

    /**
     * 获取滚动项目库详情
     */
    @PostMapping("/library/info/{id}")
    public AjaxResult getProjectRollLibrary(@PathVariable("id") Long id){
        return success(libraryService.getInfoById(id));
    }

    /**
     * 滚动项目库分页列表
     */
    @PostMapping("/library/list")
    public TableDataInfo libraryList(@RequestBody ProjectRollLibrary params){
        Map<String,Object> map = params.getParams();
        map.put("roleKey",getOneRoleKey());
        startPage(params);
        List<ProjectRollLibrary> list = libraryService.list(params);
        return getDataTable(list,true);
    }

    /**
     * 使用项目名称模糊匹配
     */
    @PostMapping("/library/get/pn")
    public AjaxResult getProjectByPN(@RequestBody ProjectRollLibrary params){
        if (params.getRollCycleId()==null){
            return error("项目库ID不能为空！");
        }
        if (StringUtils.isBlank(params.getProjectName())){
            return error("项目名称不能为空！");
        }
        return success(libraryService.getProjectByPN(params));
    }

    /**
     * 滚动项目库导入
     * @param file
     * @param rollCycleId
     * @return
     */
    @PostMapping("/library/import")
    public AjaxResult importData(MultipartFile file,Long rollCycleId){
        if (file==null||file.isEmpty()){
            return error("导入文件不能为空");
        }
        if (rollCycleId==null){
            return error("项目周期ID不能为空");
        }
        try {
            //读取数据
            List<ProjectRollLibraryVO> data = EasyExcel.read(file.getInputStream()).head(ProjectRollLibraryVO.class).headRowNumber(1).doReadAllSync();
            if (CollectionUtils.isEmpty(data)&&data.size()<1){
                return error("未读取到数据,检查模板是否为空！");
            }
            return libraryService.importData(data,rollCycleId,getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return error("数据导入失败！");
        }
    }

    /**
     * 审核
     */
    @PostMapping("/library/verify")
    public AjaxResult verify(@RequestBody VerifyVO params){
        params.setUserName(getUsername());
        params.setRoleKeys(getRoleKeys());
        return libraryService.verify(params);
    }

    /**
     * 批量审核
     */
    @PostMapping("/library/batch/verify")
    public AjaxResult batchVerify(@RequestBody VerifyVO params){
        if (StringUtils.isEmpty(params.getIds())){
            return error("ID集合不能为空！");
        }
        params.setUserName(getUsername());
        params.setRoleKeys(getRoleKeys());
        libraryService.batchVerify(params);
        return success();
    }

    /**
     * 根据统计数量和资金
     * @param id
     * @return
     */
    @PostMapping("/library/statistics/{id}")
    public AjaxResult statisticsNumAndFund(@PathVariable("id") Long id){
        return success(libraryService.statisticsNumAndFund(id));
    }

    /**
     * 项目库统计
     * @param params
     * @return
     */
    @PostMapping("/library/statistics")
    public AjaxResult statistics(@RequestBody StatisticsQueryVO params){
        switch (params.getType()){
            //区县统计
            case 1: return success(libraryService.statisticsGbQx(params));
            //根据145类别统计
            case 2: return success(libraryService.statisticsGb145(params));
            //根据项目类别统计
            case 3: return success(libraryService.statisticsGbType(params));
            //根据是否下达计划统计
            case 4: return success(libraryService.statisticsGbPlan(params));
            //根据实施状态统计
            case 5: return success(libraryService.statisticsGbBuildStatus(params));
            default:break;
        }
        return success();
    }

    /**
     * 绑定用户
     * @param params
     * @return
     */
    @PostMapping("/bind/user")
    public AjaxResult projectBindUser(@Validated @RequestBody ProjectUserVO params){
        planManageService.projectBindUser(params);
        return success();
    }

    /**
     * 撤回
     * @param id
     * @return
     */
    @PostMapping("/library/rollback/{id}")
    public AjaxResult rollback(@PathVariable("id") Long id){
        VerifyVO params = new VerifyVO();
        params.setId(id);
        params.setRoleKeys(getRoleKeys());
        params.setUserName(getUsername());
        return libraryService.rollback(params);
    }
}

