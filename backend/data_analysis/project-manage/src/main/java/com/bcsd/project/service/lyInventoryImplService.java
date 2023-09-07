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


    /**
     * 新增库存基本信息
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(){
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
        if(!jsonObject2.getString("code").equals("0")){
            log.error("获取数据失败"+jsonObject2.getString("message"));
            return;
        }
        String data = jsonObject2.getString("data");
        List <lyInventory> entityList = JSON.parseArray(data,lyInventory.class);
        for (lyInventory inventory : entityList) {
            /*LambdaQueryWrapper<lyInventory> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(lyInventory::getStkCode, inventory.getStkCode());
            lyInventory lyInventory = baseMapper.selectOne(wrapper);*/
            lyInventory lyInventory = baseMapper.selectByStkCode(inventory);
            if(ObjectUtils.isNotEmpty(lyInventory)){
                baseMapper.updateById(lyInventory);
                break;
            }
            inventory.setCreateTime(new Date());
            baseMapper.insert(inventory);
        }
    }

    /**
     * 已装配零件统计
     * @return
     */
    public AjaxResult getAssembled(){
        Map<String,Object> res = new HashMap<>();
        Integer assembledCount = baseMapper.getAssembledCount();
        LambdaQueryWrapper<lyInventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(lyInventory::getBatchAttr07);
        Integer selectCount = baseMapper.selectCount(wrapper);
        //String format = String.format("%.2f", ((assembledCount.doubleValue() / selectCount.doubleValue()) * 100));
        List<Map<String,Object>> getMapNameCount = baseMapper.getMapNameCount();
        res.put("assembledCount",assembledCount);
        //res.put("percentage",format);
        res.put("allCount",selectCount);
        res.put("MapNameCount",getMapNameCount);
        return AjaxResult.success(res);
    }


    /**
     * 已装配零件列表
     * @param params
     * @return
     */
    public List<Map<String,Object>> listPage(lyInventory params) {
        return baseMapper.getAssembledList(params);
    }
    /**
     * 已装配零件列表统计
     * @param params
     * @return
     */
    public Map<String,Object> getAllCount(lyInventory params){
        return baseMapper.getCount(params);
    }



    /**
     * 已装配零件导出
     * @param response
     * @param request
     */
    public void excelDownload(HttpServletResponse response, HttpServletRequest request,lyInventory params) {
        List<Map<String, Object>> assembledList = baseMapper.getAssembledList(params);
        List<List<String>> dataList = new ArrayList<>();
        for (Map<String, Object> map : assembledList) {
            List<String> list = new ArrayList<>();
            list.add(map.get("matCode").toString());
            list.add(map.get("matText").toString());
            list.add(map.get("totalQty").toString());
            list.add(map.get("availableQty").toString());
            dataList.add(list);
        }
        List<String> headList = new ArrayList<>();
        headList.add("零件号");
        headList.add("零件颜色");
        headList.add("已装配数（按总计数量）");
        headList.add("已装配数（按可用库存）");
        ExcelUtil.uploadExcelAboutUser(request,response,"已装配零件",headList,dataList);
    }

    /**
     * 导入数据
     * @param data
     * @param userName
     * @return
     */
    public AjaxResult importData(List<lyInventory> data,String userName){
        //数据转换
        Date curDate = DateUtils.getNowDate();
        for (lyInventory inventory : data) {
            inventory.setCreateTime(curDate);
            inventory.setCreateBy(userName);
            log.error(""+inventory.getStkCode());
        }
        //保存数据
        saveBatch(data);
        return AjaxResult.success();
    }

    /**
     * 零件状态统计
     * @return
     */
    public AjaxResult getBatchAttr07(){
        Map<String,Object> res = new HashMap<>();
        List<Map<String,Object>> getBatchAttr07 = baseMapper.getBatchAttr07();
        LambdaQueryWrapper<lyInventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(lyInventory::getBatchAttr07);
        Integer selectCount = baseMapper.selectCount(wrapper);
//        for (int i = 0; i < getBatchAttr07.size(); i++) {
//            String format = String.format("%.2f", ((Double.valueOf(getBatchAttr07.get(i).toString()) / selectCount.doubleValue()) * 100));
//
//        }
        res.put("batchAttr07",getBatchAttr07);
//        res.put("percentage",format);
        res.put("allCount",selectCount);
        return AjaxResult.success(res);
    }


    /**
     * 零件状态导出
     * @param response
     * @param request
     */
    public void excelDownload2(HttpServletResponse response, HttpServletRequest request,lyInventory params) {
        PageHelper.clearPage();
        List<Map<String, Object>> assembledList = baseMapper.getAssembledList(params);
        List<List<String>> dataList = new ArrayList<>();
        for (Map<String, Object> map : assembledList) {
            List<String> list = new ArrayList<>();
            list.add(map.get("matCode").toString());
            list.add(map.get("matText").toString());
            list.add(map.get("batchAttr07").toString());
            list.add(map.get("totalQty").toString());
            list.add(map.get("availableQty").toString());
            dataList.add(list);
        }
        List<String> headList = new ArrayList<>();
        headList.add("零件号");
        headList.add("零件颜色");
        headList.add("零件状态");
        headList.add("已装配数（按总计数量）");
        headList.add("已装配数（按可用库存）");
        ExcelUtil.uploadExcelAboutUser(request,response,"零件状态",headList,dataList);
    }

}