package com.bcsd.project.service;

import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.poi.ExcelUtil;
import com.bcsd.project.domain.lyInventory;
import com.bcsd.project.domain.lyInventoryThreshold;
import com.bcsd.project.domain.lyRequirement;
import com.bcsd.project.mapper.lyInventoryMapper;
import com.bcsd.project.mapper.lyRequirementMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.bcsd.common.utils.SecurityUtils.getUsername;

/**
 * 需求管理设置实现类
 *
 * @ClassName lyRequirementServiceImpl
 * @Description: TODO
 * @Author Mingrui
 * @Date 2023/9/8
 **/
@Slf4j
@Service
public class lyRequirementImplService implements lyRequirementService {

    @Autowired
    private lyRequirementMapper requirementMapper;
    @Autowired
    private lyInventoryMapper inventoryMapper;

    /**
     * 列表
     *
     * @param requirement
     */
    @Override
    public List<lyRequirement> list(lyRequirement requirement) {
        return requirementMapper.selectRequirementList(requirement);
    }

    /**
     * 零件号列表
     *
     * @param requirement
     */
    @Override
    public List<lyRequirement> codeList(lyRequirement requirement) {
        return requirementMapper.selectCodeList(requirement);
    }

    /**
     * 新增更新
     *
     * @param requirement
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addOrUpdate(lyRequirement requirement) {
        lyInventory byMatCode = inventoryMapper.getByMatCodeGroup(requirement.getCode());
        if (byMatCode != null && (requirement.getProcessingStatus()==null || requirement.getProcessingStatus() != 2)) {
            Double qty = Double.valueOf(requirement.getQuantity()) - byMatCode.getTotalQty();
            if (qty > 0) {
                requirement.setProcessingStatus(1);  //未处理
            } else {
                requirement.setProcessingStatus(0);  //无需处理
            }
        }
        if (requirement.getId() != null) {
            requirement.setUpdateBy(getUsername());
            requirement.setUpdateTime(new Date());
            requirementMapper.updateByPrimaryKeySelective(requirement);
        } else {
            List<lyRequirement> result = requirementMapper.checkCodeExists(requirement);
            if (!result.isEmpty()) {
                return AjaxResult.error("当天存在重复零件号");
            } else {
                requirement.setCreateBy(getUsername());
                requirement.setCreateTime(new Date());
                requirementMapper.insertSelective(requirement);
            }
        }
        //inventoryImplService.updinventoryStatus(requirement);
        return AjaxResult.success();
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult delete(Long id) {
        requirementMapper.deleteByPrimaryKey(id);
        return AjaxResult.success();
    }

    /**
     * 模板下载
     *
     * @param response
     * @param request
     * @return
     */
    @Override
    public void excelDownload(HttpServletResponse response, HttpServletRequest request) {
        List<List<String>> dataList = new ArrayList<>();
        List<String> headList = new ArrayList<>();
        headList.add("零件号");
        headList.add("零件颜色");
        headList.add("计划日期");
        headList.add("计划发货数量");
        ExcelUtil.uploadExcelAboutUser(request, response, "需求计划设置导入模板", headList, dataList);
    }

    /**
     * 导入数据
     *
     * @param data
     * @param userName
     * @return
     */
    @Override
    public AjaxResult importData(List<lyRequirement> data, String userName) {
        for (lyRequirement requirement : data) {
            if (!requirement.getCode().isEmpty() && requirement.getDate() != null) {
                List<lyRequirement> result = requirementMapper.checkCodeExists(requirement);
                lyInventory byMatCode = inventoryMapper.getByMatCodeGroup(requirement.getCode());
                if (byMatCode != null && (requirement.getProcessingStatus()==null || requirement.getProcessingStatus() != 2)) {
                    Double qty = Double.valueOf(requirement.getQuantity()) - byMatCode.getTotalQty();
                    if (qty > 0) {
                        requirement.setProcessingStatus(1);  //未处理
                    } else {
                        requirement.setProcessingStatus(0);  //无需处理
                    }
                }
                if (!result.isEmpty()) {
                    requirement.setId(result.get(0).getId());
                    requirement.setUpdateBy(userName);
                    requirement.setUpdateTime(DateUtils.getNowDate());
                    requirementMapper.updateByPrimaryKeySelective(requirement);
                } else {
                    requirement.setCreateBy(userName);
                    requirement.setCreateTime(DateUtils.getNowDate());
                    requirement.setUpdateBy(userName);
                    requirement.setUpdateTime(new Date());
                    requirementMapper.insertSelective(requirement);
                }
            }
            //inventoryImplService.updinventoryStatus(requirement);
        }
        return AjaxResult.success();
    }

    /**
     * 新增库存时，修改状态
     * @param matCodeSet
     */
    public void updinventoryStatus(Set<String> matCodeSet) {
        for (String matCode : matCodeSet) {
            lyInventory byMatCode = inventoryMapper.getByMatCodeGroup(matCode);
            lyRequirement requirement = requirementMapper.getRequirement(matCode);
            if (byMatCode != null && requirement != null && (requirement.getProcessingStatus()==null || requirement.getProcessingStatus() != 2)) {
                Double qty = Double.valueOf(requirement.getQuantity()) - byMatCode.getTotalQty();
                if (qty > 0) {
                    requirement.setProcessingStatus(1);  //未处理
                } else {
                    requirement.setProcessingStatus(0);  //无需处理
                }
                requirementMapper.updateByPrimaryKeySelective(requirement);
            }
        }
    }



}

