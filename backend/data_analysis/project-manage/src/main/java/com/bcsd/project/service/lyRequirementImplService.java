package com.bcsd.project.service;

import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.poi.ExcelUtil;
import com.bcsd.project.domain.lyInventoryThreshold;
import com.bcsd.project.domain.lyRequirement;
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
        if (requirement.getId() != null) {
            requirement.setUpdateBy(getUsername());
            requirement.setUpdateTime(new Date());
            requirementMapper.updateByPrimaryKeySelective(requirement);
            return AjaxResult.success("更新成功");
        } else {
            requirement.setCreateBy(getUsername());
            requirement.setCreateTime(new Date());
            requirementMapper.insertSelective(requirement);
            return AjaxResult.success("新增成功");
        }
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
//        headList.add("序号");
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
                if (!result.isEmpty()) {
                    requirement.setUpdateBy(userName);
                    requirement.setUpdateTime(DateUtils.getNowDate());
                    requirementMapper.updateByPrimaryKeySelective(requirement);
                } else {
                    requirement.setCreateBy(userName);
                    requirement.setCreateTime(DateUtils.getNowDate());
                    requirementMapper.insertSelective(requirement);
                }
            }
        }
        return AjaxResult.success();
    }
}

