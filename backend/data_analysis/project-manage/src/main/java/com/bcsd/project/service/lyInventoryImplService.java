package com.bcsd.project.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.common.annotation.DataScope;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.domain.entity.SysDept;
import com.bcsd.common.core.domain.entity.SysDictData;
import com.bcsd.common.utils.*;
import com.bcsd.common.utils.http.HttpUtils;
import com.bcsd.common.utils.poi.ExcelUtil;
import com.bcsd.project.domain.*;
import com.bcsd.project.domain.vo.*;
import com.bcsd.project.enums.FundEnum;
import com.bcsd.project.mapper.*;
import com.bcsd.system.service.ISysDictTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.*;

import static com.bcsd.project.constants.Constants.*;

/**
 * 库存 服务实现类
 *
 * @author liuliang
 * @since 2023-02-22
 */
@Slf4j
@Service
public class lyInventoryImplService extends ServiceImpl<lyInventoryMapper, lyInventory> implements IService<lyInventory> {

    @Value("${http.url}")
    private String url;

    @Autowired
    private lyInventoryThresholdMapper inventoryThresholdMapper;
    @Autowired
    private lyRequirementMapper requirementMapper;

    @Autowired
    private lyThresholdManagementMapper thresholdManagementMapper;


    /**
     * 新增库存基本信息
     *
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void add() {
        /*JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNum",1);
        jsonObject.put("pageSize",10000);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("sourceId","REST030104");
        jsonObject.put("param",jsonObject1);
        String res = HttpUtils.sendPost(url+"permit/findByPage", jsonObject.toString());*/
        StringBuilder sb = new StringBuilder();
        sb.append("{\n" +
                "    \"code\": \"0\",\n" +
                "    \"message\": \"成功\",\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"podTypCode\": null,\n" +
                "            \"dateLastInv\": null,\n" +
                "            \"totalQty\": 36.0,\n" +
                "            \"dateLastRm\": null,\n" +
                "            \"toNumLastMov\": \"2000000067004\",\n" +
                "            \"blkReaText\": \"\",\n" +
                "            \"operType\": null,\n" +
                "            \"batchAttr08\": null,\n" +
                "            \"batchAttr07\": \"已装配\",\n" +
                "            \"podCode\": \"100362\",");
        sb.append("\"batchAttr09\": null,\n" +
                "            \"qualityStatus\": \"F\",\n" +
                "            \"availableQty\": 36.0,\n" +
                "            \"containerTypeCode\": null,\n" +
                "            \"inStockInterval\": null,\n" +
                "            \"matUnit\": \"EA\",\n" +
                "            \"boxFlag\": \"0\",\n" +
                "            \"boxCode\": null,\n" +
                "            \"boxFlagText\": \"否\",\n" +
                "            \"outBlkUserFlag\": 0,\n" +
                "            \"stgTypText\": \"F1一车间叉车收货位\",\n" +
                "            \"stgBinTypCode\": null,\n" +
                "            \"batchAttr11\": null,\n" +
                "            \"batchAttr10\": null,\n" +
                "            \"priority\": 0,\n" +
                "            \"batchAttr15\": null,\n" +
                "            \"batchAttr14\": null,\n" +
                "            \"pickNum\": null,\n" +
                "            \"batchAttr13\": null,\n" +
                "            \"batchAttr12\": null,\n" +
                "            \"desBinCode\": null,\n" +
                "            \"binName\": \"100362BE501013\",\n" +
                "            \"weiUnit\": null,\n" +
                "            \"sequence\": 0,\n" +
                "            \"realAvailableQty\": null,\n" +
                "            \"whCode\": \"2022\",\n" +
                "            \"blkUser\": null,\n" +
                "            \"dateLastPm\": \"2023-08-16 09:49:05\",\n" +
                "            \"berthAlias\": \"176233XC205454\",\n" +
                "            \"invFlagText\": \"未启动\",\n" +
                "            \"matText\": \"P03前保险杠下饰板(9D雪域白)\",\n" +
                "            \"toItemLastMov\": \"4965\",\n" +
                "            \"invFlag\": 0,\n" +
                "            \"stockStr1\": null,\n" +
                "            \"intoBlkUserFlagText\": null,\n" +
                "            \"stockStr2\": null,\n" +
                "            \"dateGen\": \"2023-08-16 00:00:00\",\n" +
                "            \"ownerCode\": \"LY\",\n" +
                "            \"outStockInterval\": null,\n" +
                "            \"podTypText\": null,\n" +
                "            \"matAdjustFlag\": 0,\n" +
                "            \"stockStr3\": null,\n" +
                "            \"matAdjustFlagText\": \"未启动\",\n" +
                "            \"stockStr4\": null,\n" +
                "            \"stockStr5\": null,\n" +
                "            \"layer\": null,\n" +
                "            \"adjustQty\": 0.0,\n" +
                "            \"stockStr6\": null,\n" +
                "            \"blkReaCode\": \"\",\n" +
                "            \"packFormat\": \"1/1/0/0\",\n" +
                "            \"ownerName\": \"燎原\",");
        sb.append("\"stkCode\": \"1000000053604\",\n" +
                "            \"matWei\": 0.036,\n" +
                "            \"qualityStatusText\": \"正常库存\",\n" +
                "            \"wmsBatchNum\": \"202308160949050114966\",\n" +
                "            \"mapName\": null,\n" +
                "            \"direction\": \"5\",\n" +
                "            \"channelCode\": null,\n" +
                "            \"directionTex\": \"中\",\n" +
                "            \"dateExpire\": \"2023-08-31 09:49:05\",\n" +
                "            \"binCode\": \"100362BE501013\",\n" +
                "            \"batchNum\": null,\n" +
                "            \"outBlkUserFlagText\": \"未冻结\",\n" +
                "            \"whText\": \"武汉燎原二车间\",\n" +
                "            \"binUtilization\": 0.0,");
        sb.append("\"traceCode\": null,\n" +
                "            \"seqCode\": null,\n" +
                "            \"dateExpireFlag\": \"未过期\",\n" +
                "            \"blkReaType\": null,\n" +
                "            \"dateInto\": \"2023-08-16 09:49:05\",\n" +
                "            \"stgTypCode\": \"F101\",\n" +
                "            \"dateLastMov\": \"2023-08-16 09:49:05\",\n" +
                "            \"matCode\": \"2803103XKV3AA9D\",\n" +
                "            \"hotIndex\": null,\n" +
                "            \"containerTypeName\": null\n" +
                "        }],\n" +
                "    \"total\": -9,\n" +
                "    \"success\": true\n" +
                "}");
        String res = sb.toString();
        JSONObject jsonObject2 = JSONObject.parseObject(res);
        if (!jsonObject2.getString("code").equals("0")) {
            log.error("获取数据失败" + jsonObject2.getString("message"));
            return;
        }
        String data = jsonObject2.getString("data");
        List<lyInventory> entityList = JSON.parseArray(data, lyInventory.class);
        //获取需求计划设置
        List<lyRequirement> requirementList = requirementMapper.getRequirementList(DateUtils.getDate());
        Map<String, Object> xqjhMap = new HashMap<>();
        for (lyRequirement requirement : requirementList) {
            xqjhMap.put(requirement.getCode(), requirement);
        }
        //获取零件库存阈值
        List<lyInventoryThreshold> list = inventoryThresholdMapper.getList();
        Map<String, Object> objectMap = new HashMap<>();
        for (lyInventoryThreshold threshold : list) {
            objectMap.put(threshold.getCode(), threshold);
        }

        for (lyInventory inventory : entityList) {
            if (!objectMap.isEmpty() && objectMap != null) {
                lyInventoryThreshold threshold = new ObjectMapper().convertValue(objectMap.get(inventory.getMatCode()), lyInventoryThreshold.class);
                if (ObjectUtils.isNotEmpty(threshold) && inventory.getTotalQty() < threshold.getLowerLimit()) {
                    inventory.setInventoryStatus(1);
                } else if (ObjectUtils.isNotEmpty(threshold) && inventory.getTotalQty() > threshold.getUpperLimit()) {
                    inventory.setInventoryStatus(2);
                } else if (ObjectUtils.isNotEmpty(threshold)) {
                    inventory.setInventoryStatus(0);
                }
            }

            if (!xqjhMap.isEmpty() && xqjhMap != null) {
                lyRequirement requirement = new ObjectMapper().convertValue(xqjhMap.get(inventory.getMatCode()), lyRequirement.class);
                if (ObjectUtils.isNotEmpty(requirement)) {
                    Double qty = Double.valueOf(requirement.getQuantity()) - inventory.getTotalQty();
                    if (qty > 0) {
                        inventory.setProcessingStatus(1);  //未处理
                    } else {
                        inventory.setProcessingStatus(0);  //无需处理
                    }
                }
            }
            lyInventory lyInventory = baseMapper.selectByStkCode(inventory);
            if (ObjectUtils.isNotEmpty(lyInventory)) {
                BeanUtils.copyProperties(lyInventory, inventory);
                baseMapper.updateById(lyInventory);
                break;
            }
            inventory.setCreateTime(new Date());
            baseMapper.insert(inventory);
        }
    }

    /**
     * 需求计划新增修改 同步修改零件库存 缺口处理状态
     *
     * @param requirement
     */
    @Transactional(rollbackFor = Exception.class)
    public void updinventoryStatus(lyRequirement requirement) {
        List<lyInventory> byMatCodeList = baseMapper.getByMatCode(requirement.getCode());
        for (lyInventory inventory : byMatCodeList) {
            Double qty = Double.valueOf(requirement.getQuantity()) - inventory.getTotalQty();
            if (qty > 0) {
                inventory.setProcessingStatus(1);  //未处理
            } else {
                inventory.setProcessingStatus(0);  //无需处理
            }
        }
        saveOrUpdateBatch(byMatCodeList);
    }

    /**
     * 需求计划新增修改 同步修改零件库存 告警状态
     *
     * @param threshold
     */
    @Transactional(rollbackFor = Exception.class)
    public void updProcessingStatus(lyInventoryThreshold threshold) {
        List<lyInventory> byMatCodeList = baseMapper.getByMatCode(threshold.getCode());
        for (lyInventory inventory : byMatCodeList) {
            if (inventory.getTotalQty() < threshold.getLowerLimit()) {
                inventory.setInventoryStatus(1);
            } else if (inventory.getTotalQty() > threshold.getUpperLimit()) {
                inventory.setInventoryStatus(2);
            } else {
                inventory.setInventoryStatus(0);
            }
        }
        saveOrUpdateBatch(byMatCodeList);
    }

    /**
     * 已装配零件统计
     *
     * @return
     */
    public AjaxResult getAssembled() {
        Map<String, Object> res = new HashMap<>();
        Integer assembledCount = baseMapper.getAssembledCount();
        LambdaQueryWrapper<lyInventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(lyInventory::getBatchAttr07);
        Integer selectCount = baseMapper.selectCount(wrapper);
        //String format = String.format("%.2f", ((assembledCount.doubleValue() / selectCount.doubleValue()) * 100));
        List<Map<String, Object>> getMapNameCount = baseMapper.getMapNameCount();
        res.put("assembledCount", assembledCount);
        //res.put("percentage",format);
        res.put("allCount", selectCount);
        res.put("MapNameCount", getMapNameCount);
        return AjaxResult.success(res);
    }


    /**
     * 已装配零件列表
     *
     * @param params
     * @return
     */
    public List<Map<String, Object>> listPage(lyInventory params) {
        return baseMapper.getAssembledList(params);
    }

    /**
     * 已装配零件列表统计
     *
     * @param params
     * @return
     */
    public Map<String, Object> getAllCount(lyInventory params) {
        return baseMapper.getCount(params);
    }


    /**
     * 已装配零件导出
     *
     * @param response
     * @param request
     */
    public void excelDownload(HttpServletResponse response, HttpServletRequest request, lyInventory params) {
        List<Map<String, Object>> assembledList = baseMapper.getAssembledList(params);
        List<List<String>> dataList = new ArrayList<>();
        for (Map<String, Object> map : assembledList) {
            List<String> list = new ArrayList<>();
            list.add(CommonUtils.nullToEmpty(map.get("matCode")));
            list.add(CommonUtils.nullToEmpty(map.get("matText")));
            list.add(CommonUtils.nullToEmpty(map.get("totalQty")));
            list.add(CommonUtils.nullToEmpty(map.get("availableQty")));
            dataList.add(list);
        }
        List<String> headList = new ArrayList<>();
        headList.add("零件号");
        headList.add("零件颜色");
        headList.add("已装配数（按总计数量）");
        headList.add("已装配数（按可用库存）");
        ExcelUtil.uploadExcelAboutUser(request, response, "已装配零件", headList, dataList);
    }

    /**
     * 导入数据
     *
     * @param data
     * @param userName
     * @return
     */
    public AjaxResult importData(List<lyInventory> data, String userName) {
        //获取需求计划设置
        List<lyRequirement> requirementList = requirementMapper.getRequirementList(DateUtils.getDate());
        Map<String, Object> xqjhMap = new HashMap<>();
        for (lyRequirement requirement : requirementList) {
            xqjhMap.put(requirement.getCode(), requirement);
        }
        //获取零件库存阈值
        List<lyInventoryThreshold> list = inventoryThresholdMapper.getList();
        Map<String, Object> objectMap = new HashMap<>();
        for (lyInventoryThreshold threshold : list) {
            objectMap.put(threshold.getCode(), threshold);
        }
        for (lyInventory inventory : data) {
            if (!objectMap.isEmpty() && objectMap != null) {
                lyInventoryThreshold threshold = new ObjectMapper().convertValue(objectMap.get(inventory.getMatCode()), lyInventoryThreshold.class);
                if (ObjectUtils.isNotEmpty(threshold) && inventory.getTotalQty() < threshold.getLowerLimit()) {
                    inventory.setInventoryStatus(1);
                } else if (ObjectUtils.isNotEmpty(threshold) && inventory.getTotalQty() > threshold.getUpperLimit()) {
                    inventory.setInventoryStatus(2);
                } else if (ObjectUtils.isNotEmpty(threshold)) {
                    inventory.setInventoryStatus(0);
                }
            }

            if (!xqjhMap.isEmpty() && xqjhMap != null) {
                lyRequirement requirement = new ObjectMapper().convertValue(xqjhMap.get(inventory.getMatCode()), lyRequirement.class);
                if (ObjectUtils.isNotEmpty(requirement)) {
                    Double qty = Double.valueOf(requirement.getQuantity()) - inventory.getTotalQty();
                    if (qty > 0) {
                        inventory.setProcessingStatus(1);  //未处理
                    } else {
                        inventory.setProcessingStatus(0);  //无需处理
                    }
                }
            }
            inventory.setCreateTime(DateUtils.getNowDate());
            inventory.setCreateBy(userName);
            log.error("" + inventory.getStkCode());
        }
        //保存数据
        saveBatch(data);
        return AjaxResult.success();
    }


    /**
     * 零件状态统计
     *
     * @return
     */
    public AjaxResult getBatchAttr07() {
        Map<String, Object> res = new HashMap<>();
        List<Map<String, Object>> getBatchAttr07 = baseMapper.getBatchAttr07();
        LambdaQueryWrapper<lyInventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(lyInventory::getBatchAttr07);
        Integer selectCount = baseMapper.selectCount(wrapper);
        List<lyThresholdManagement> listByType = thresholdManagementMapper.getListByType("零件状态");
        Map<String, Object> jhMap = new HashMap<>();
        for (lyThresholdManagement management : listByType) {
            jhMap.put(management.getName(), management);
        }
        for (Map<String, Object> map : getBatchAttr07) {
            map.put("colourType", 0);
            map.put("lowerLimit", 0);
            map.put("upperLimit", 0);
            if (!jhMap.isEmpty() && jhMap != null) {
                lyThresholdManagement requirement = new ObjectMapper().convertValue(jhMap.get(map.get("batchAttr07")), lyThresholdManagement.class);
                if (ObjectUtils.isNotEmpty(requirement)) {
                    map.put("lowerLimit", requirement.getLowerLimit());
                    map.put("upperLimit", requirement.getUpperLimit());
                    Integer batchAttr07Count = Integer.valueOf(map.get("batchAttr07Count").toString());
                    if (batchAttr07Count < requirement.getLowerLimit() || batchAttr07Count > requirement.getUpperLimit()) {
                        map.put("colourType", 1);
                    }
                }
            }
        }
        res.put("batchAttr07", getBatchAttr07);
        res.put("allCount", selectCount);
        return AjaxResult.success(res);
    }


    /**
     * 零件状态导出
     *
     * @param response
     * @param request
     */
    public void excelDownload2(HttpServletResponse response, HttpServletRequest request, lyInventory params) {
        PageHelper.clearPage();
        List<Map<String, Object>> assembledList = baseMapper.getAssembledList(params);
        List<List<String>> dataList = new ArrayList<>();
        for (Map<String, Object> map : assembledList) {
            List<String> list = new ArrayList<>();
            list.add(CommonUtils.nullToEmpty(map.get("matCode")));
            list.add(CommonUtils.nullToEmpty(map.get("matText")));
            list.add(CommonUtils.nullToEmpty(map.get("batchAttr07")));
            list.add(CommonUtils.nullToEmpty(map.get("totalQty")));
            list.add(CommonUtils.nullToEmpty(map.get("availableQty")));
            dataList.add(list);
        }
        List<String> headList = new ArrayList<>();
        headList.add("零件号");
        headList.add("零件颜色");
        headList.add("零件状态");
        headList.add("已装配数（按总计数量）");
        headList.add("已装配数（按可用库存）");
        ExcelUtil.uploadExcelAboutUser(request, response, "零件状态", headList, dataList);
    }

    /**
     * 零件库存告警统计
     */
    public List<Map<String, Object>> inventoryAlarmCount(lyInventory params) {
        return baseMapper.inventoryAlarmCount(params);
    }

    /**
     * 零件库存告警统计导出
     *
     * @param response
     * @param request
     */
    public void excelDownload3(HttpServletResponse response, HttpServletRequest request, lyInventory params) {
        List<Map<String, Object>> assembledList = baseMapper.inventoryAlarmCount(params);
        List<List<String>> dataList = new ArrayList<>();
        for (Map<String, Object> map : assembledList) {
            List<String> list = new ArrayList<>();
            list.add(CommonUtils.nullToEmpty(map.get("matCode")));
            list.add(CommonUtils.nullToEmpty(map.get("matText")));
            list.add(CommonUtils.nullToEmpty(map.get("upperLimit")));
            list.add(CommonUtils.nullToEmpty(map.get("lowerLimit")));
            list.add(CommonUtils.nullToEmpty(map.get("totalQty")));
            String inventoryStatus = CommonUtils.nullToEmpty(map.get("inventoryStatus"));
            if (StringUtils.isEmpty(inventoryStatus)) {
                list.add(inventoryStatus);
            } else {
                if ("0".equals(inventoryStatus)) {
                    list.add("正常");
                } else if ("1".equals(inventoryStatus)) {
                    list.add("低下限告警");
                } else {
                    list.add("超上限告警");
                }
            }
            dataList.add(list);
        }
        List<String> headList = new ArrayList<>();
        headList.add("零件号");
        headList.add("零件颜色");
        headList.add("库存上限数量");
        headList.add("库存下限数量");
        headList.add("当前库存总数");
        headList.add("库存状态");
        ExcelUtil.uploadExcelAboutUser(request, response, "零件库存告警统计", headList, dataList);
    }

    /**
     * 零件计划缺口统计
     */
    public List<InventoryGapsNumberVO> getGapsNumber(lyInventory params) {
        return baseMapper.getGapsNumber(params);
    }

    /**
     * 零件计划缺口统计导出
     */
    public void excelDownload4(HttpServletResponse response, HttpServletRequest request, lyInventory params) {
        List<InventoryGapsNumberVO> assembledList = baseMapper.getGapsNumber(params);
        List<List<String>> dataList = new ArrayList<>();
        for (InventoryGapsNumberVO map : assembledList) {
            List<String> list = new ArrayList<>();
            list.add(CommonUtils.nullToEmpty(map.getDate()));
            list.add(CommonUtils.nullToEmpty(map.getMatCode()));
            list.add(CommonUtils.nullToEmpty(map.getMatText()));
            list.add(CommonUtils.nullToEmpty(map.getQuantity()));
            list.add(CommonUtils.nullToEmpty(map.getTotalQty()));
            list.add(CommonUtils.nullToEmpty(map.getGapNumber()));
            String processingStatus = CommonUtils.nullToEmpty(map.getProcessingStatus());
            if (StringUtils.isEmpty(processingStatus)) {
                list.add(processingStatus);
            } else {
                if ("0".equals(processingStatus)) {
                    list.add("无需处理");
                } else if ("1".equals(processingStatus)) {
                    list.add("未处理");
                } else {
                    list.add("已处理");
                }
            }
            dataList.add(list);
        }
        List<String> headList = new ArrayList<>();
        headList.add("计划日期");
        headList.add("零件号");
        headList.add("零件颜色");
        headList.add("计划发货数量");
        headList.add("当前库存总数");
        headList.add("缺口数量");
        headList.add("处理状态");
        ExcelUtil.uploadExcelAboutUser(request, response, "零件计划缺口", headList, dataList);
    }

    /**
     * 零件计划缺口统计修改状态
     *
     * @param jsonObject
     * @param userName
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updProcessingStatus(JSONObject jsonObject, String userName) {
        jsonObject.put("updateBy", userName);
        jsonObject.put("updateTime", new Date());
        baseMapper.updProcessingStatus(jsonObject);
        return AjaxResult.success();
    }

    /**
     * 大屏-零件计划缺口统计
     */
    public Map<String, Object> getFhjhljqkqkCount() {
        return baseMapper.getFhjhljqkqkCount();
    }

    /**
     * 大屏-零件计划缺口统计明细
     */
    public List<Map<String, Object>> getFhjhljqkqkList() {
        return baseMapper.getFhjhljqkqkList();
    }


    /**
     * 零大屏-零件库存预警情况统计
     */
    public Map<String, Object> getKcyjCount() {
        return baseMapper.getKcyjCount();
    }

    /**
     * 大屏-零件库存预警情况明细
     */
    public List<Map<String, Object>> getKcyjList() {
        List<Map<String, Object>> kcyjList = baseMapper.getKcyjList();
        for (Map<String, Object> objectMap : kcyjList) {
            Double difference;
            if (objectMap.get("inventoryStatus").toString().equals("1")) {
                difference = Double.valueOf(objectMap.get("lowerLimit").toString()) - Double.valueOf(objectMap.get("totalQty").toString());
            } else {
                difference = Double.valueOf(objectMap.get("totalQty").toString()) - Double.valueOf(objectMap.get("upperLimit").toString());
            }
            objectMap.put("difference", difference);
        }
        return kcyjList;
    }

}
