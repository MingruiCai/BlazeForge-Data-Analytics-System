package com.bcsd.project.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.poi.ExcelUtil;
import com.bcsd.project.constants.Constants;
import com.bcsd.project.domain.lyEmptyContainer;
import com.bcsd.project.domain.lyInventory;
import com.bcsd.project.domain.vo.lyEmptyContainerVO;
import com.bcsd.project.mapper.lyEmptyContainerMapper;
import com.bcsd.project.mapper.lyInventoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 空容器 服务实现类
 *
 * @author liuliang
 * @since 2023-02-22
 */
@Slf4j
@Service
public class lyEmptyContainerImplService extends ServiceImpl<lyEmptyContainerMapper, lyEmptyContainer> implements IService<lyEmptyContainer> {


    /**
     * 新增空容器基本信息
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
                "            \"podTypCode\": \"BB\",\n" +
                "            \"podStr8\": null,\n" +
                "            \"intoBlkUserFlagText\": \"未冻结\",\n" +
                "            \"blkType\": null,\n" +
                "            \"agvFlag\": 1,\n" +
                "            \"emptyPodFlag\": null,\n" +
                "            \"podStr4\": null,\n" +
                "            \"podStr5\": null,\n" +
                "            \"podStr6\": null,\n" +
                "            \"podStr7\": null,\n" +
                "            \"podTypText\": \"BB\",\n" +
                "            \"podStr1\": null,\n" +
                "            \"blkReaText\": null,\n" +
                "            \"initStatus\": 1,\n" +
                "            \"podStr2\": null,\n" +
                "            \"berthCode\": \"205077XD208826\",\n" +
                "            \"podStr3\": null,\n" +
                "            \"podLayout\": null,\n" +
                "            \"layer\": null,\n" +
                "            \"blkReaCode\": null,\n" +
                "            \"podCode\": \"100001\",\n" +
                "            \"cooY\": 208826.0,\n" +
                "            \"cooX\": 205077.0,\n" +
                "            \"mapName\": null,\n" +
                "            \"podText\": \"100001\",\n" +
                "            \"distanceWb\": 0.0,\n" +
                "            \"agvFlagText\": \"AGV库\",\n" +
                "            \"stgTypText\": \"F1一车间叉车收货位\",\n" +
                "            \"outBlkUserFlag\": 0,\n" +
                "            \"middle\": null,\n" +
                "            \"batchAttrValue\": \"{\\\"batchAttr07\\\":\\\"已装配\\\",\\\"dateExpire\\\":1693308343226,\\\"dateGen\\\":1691942400000,\\\"dateInto\\\":1692012343226,\\\"matCode\\\":\\\"H53A5006215AABH01\\\",\\\"ownerCode\\\":\\\"LY\\\",\\\"packFormat\\\":\\\"1/1/0/0\\\",\\\"qualityStatus\\\":\\\"F\\\"}\",\n" +
                "            \"podIntoTempLockText\": \"未锁定\",\n" +
                "            \"outBlkUserFlagText\": \"未冻结\",\n" +
                "            \"emptyBinCount\": 0,\n" +
                "            \"whText\": \"武汉燎原二车间\",\n" +
                "            \"binUtilization\": \"0/1\",\n" +
                "            \"initStatusText\": \"已初始化\",\n" +
                "            \"whCode\": \"2022\",\n" +
                "            \"intoBlkUserFlag\": 0,\n" +
                "            \"blkUser\": null,\n" +
                "            \"berthAlias\": \"205077XD208826\",\n" +
                "            \"podIntoTempLock\": \"0\",\n" +
                "            \"stgTypCode\": \"F101\",\n" +
                "            \"hotIndex\": \"3.0\",\n" +
                "            \"mapCode\": \"XD\",\n" +
                "            \"podWei\": null\n" +
                "        }],\n" +
                "    \"total\": 824,\n" +
                "    \"success\": true\n" +
                "}");
        /*sb.append("");
        sb.append("");*/

        String res = sb.toString();
        JSONObject jsonObject2 = JSONObject.parseObject(res);
        if(!jsonObject2.getString("code").equals("0")){
            log.error("获取数据失败"+jsonObject2.getString("message"));
            return;
        }
        String data = jsonObject2.getString("data");
        List <lyEmptyContainer> entityList = JSON.parseArray(data,lyEmptyContainer.class);
        for (lyEmptyContainer param : entityList) {
            String[] split = param.getBinUtilization().split("/");
            param.setTotalCount(Integer.parseInt(split[1]));
            param.setPodTypeCount(Integer.parseInt(split[0]));
            lyEmptyContainer emptyContainer = baseMapper.selectByPodCode(param);
            if(ObjectUtils.isNotEmpty(emptyContainer)){
                BeanUtils.copyProperties(emptyContainer,param);
                baseMapper.updateById(emptyContainer);
                break;
            }
            param.setCreateTime(new Date());
            baseMapper.insert(param);
        }
    }


    /**
     * 导入数据
     * @param data
     * @param userName
     * @return
     */
    public AjaxResult importData(List<lyEmptyContainer> data, String userName){
        //数据转换
        Date curDate = DateUtils.getNowDate();
        for (lyEmptyContainer inventory : data) {
            String[] split = inventory.getBinUtilization().split("/");
            inventory.setTotalCount(Integer.parseInt(split[1]));
            inventory.setPodTypeCount(Integer.parseInt(split[0]));
            inventory.setCreateTime(curDate);
            inventory.setCreateBy(userName);
            log.error(""+inventory.getPodCode());
        }
        //保存数据
        saveBatch(data);
        return AjaxResult.success();
    }

    /**
     * 空容器统计
     * @return
     */
    public List<Map<String, Object>> getContainerCount(){
        List<Map<String, Object>> list = baseMapper.getContainerCount();
        //List<Map<String, Object>> list = new ArrayList();
        for (Map<String, Object> objectMap : list) {
            objectMap.put("percentage",String.format("%.2f", ((Double.valueOf(objectMap.get("podTypeCount").toString()) / Double.valueOf(objectMap.get("totalCount").toString())) * 100)));
        }
        /*
        List<lyEmptyContainerVO> selectList = baseMapper.getList(null);
        Map<String,Object> baMap = new HashMap<>();
        Map<String,Object> bbMap = new HashMap<>();
        Map<String,Object> bcMap = new HashMap<>();
        Map<String,Object> bdMap = new HashMap<>();
        Map<String,Object> beMap = new HashMap<>();
        Map<String,Object> bfMap = new HashMap<>();
        Map<String,Object> bgMap = new HashMap<>();
        Map<String,Object> bhMap = new HashMap<>();
        baMap.put("podTypeCount",0);
        baMap.put("totalCount",0);
        baMap.put("podTypText",Constants.BA);
        bbMap.put("podTypeCount",0);
        bbMap.put("totalCount",0);
        bbMap.put("podTypText",Constants.BB);
        bcMap.put("podTypeCount",0);
        bcMap.put("totalCount",0);
        bcMap.put("podTypText",Constants.BC);
        bdMap.put("podTypeCount",0);
        bdMap.put("totalCount",0);
        bdMap.put("podTypText",Constants.BD);
        beMap.put("podTypeCount",0);
        beMap.put("totalCount",0);
        beMap.put("podTypText",Constants.BE);
        bfMap.put("podTypeCount",0);
        bfMap.put("totalCount",0);
        bfMap.put("podTypText",Constants.BF);
        bgMap.put("podTypeCount",0);
        bgMap.put("totalCount",0);
        bgMap.put("podTypText",Constants.BG);
        bhMap.put("podTypeCount",0);
        bhMap.put("totalCount",0);
        bhMap.put("podTypText",Constants.BH);
        for (lyEmptyContainerVO container : selectList) {
            String[] split = container.getBinUtilization().split("/");
            if(container.getPodTypText().equals(Constants.BA)){
                baMap.put("podTypeCount",Integer.parseInt(baMap.get("podTypeCount").toString()) + Integer.parseInt(split[0].trim()));
                baMap.put("totalCount",Integer.parseInt(baMap.get("totalCount").toString()) + Integer.parseInt(split[1].trim()));
            }else if(container.getPodTypText().equals(Constants.BB)){
                bbMap.put("podTypeCount",Integer.parseInt(bbMap.get("podTypeCount").toString()) + Integer.parseInt(split[0]));
                bbMap.put("totalCount",Integer.parseInt(bbMap.get("totalCount").toString()) + Integer.parseInt(split[1]));
            }else if(container.getPodTypText().equals(Constants.BC)){
                bcMap.put("podTypeCount",Integer.parseInt(bcMap.get("podTypeCount").toString()) + Integer.parseInt(split[0]));
                bcMap.put("totalCount",Integer.parseInt(bcMap.get("totalCount").toString()) + Integer.parseInt(split[1]));
            }else if(container.getPodTypText().equals(Constants.BD)){
                bdMap.put("podTypeCount",Integer.parseInt(bdMap.get("podTypeCount").toString()) + Integer.parseInt(split[0]));
                bdMap.put("totalCount",Integer.parseInt(bdMap.get("totalCount").toString()) + Integer.parseInt(split[1]));
            }else if(container.getPodTypText().equals(Constants.BE)){
                beMap.put("podTypeCount",Integer.parseInt(beMap.get("podTypeCount").toString()) + Integer.parseInt(split[0]));
                beMap.put("totalCount",Integer.parseInt(beMap.get("totalCount").toString()) + Integer.parseInt(split[1]));
            }else if(container.getPodTypText().equals(Constants.BF)){
                bfMap.put("podTypeCount",Integer.parseInt(bfMap.get("podTypeCount").toString()) + Integer.parseInt(split[0]));
                bfMap.put("totalCount",Integer.parseInt(bfMap.get("totalCount").toString()) + Integer.parseInt(split[1]));
            }else if(container.getPodTypText().equals(Constants.BG)){
                bgMap.put("podTypeCount",Integer.parseInt(bgMap.get("podTypeCount").toString()) + Integer.parseInt(split[0]));
                bgMap.put("totalCount",Integer.parseInt(bgMap.get("totalCount").toString()) + Integer.parseInt(split[1]));
            }else if(container.getPodTypText().equals(Constants.BH)){
                bhMap.put("podTypeCount",Integer.parseInt(bhMap.get("podTypeCount").toString()) + Integer.parseInt(split[0]));
                bhMap.put("totalCount",Integer.parseInt(bhMap.get("totalCount").toString()) + Integer.parseInt(split[1]));
            }
        }
        List<Map<String, Object>> list = new ArrayList();
        baMap.put("percentage",String.format("%.2f", ((Double.valueOf(baMap.get("podTypeCount").toString()) / Double.valueOf(baMap.get("totalCount").toString())) * 100)));
        bbMap.put("percentage",String.format("%.2f", ((Double.valueOf(bbMap.get("podTypeCount").toString()) / Double.valueOf(bbMap.get("totalCount").toString())) * 100)));
        bcMap.put("percentage",String.format("%.2f", ((Double.valueOf(bcMap.get("podTypeCount").toString()) / Double.valueOf(bcMap.get("totalCount").toString())) * 100)));
        bdMap.put("percentage",String.format("%.2f", ((Double.valueOf(bdMap.get("podTypeCount").toString()) / Double.valueOf(bdMap.get("totalCount").toString())) * 100)));
        beMap.put("percentage",String.format("%.2f", ((Double.valueOf(beMap.get("podTypeCount").toString()) / Double.valueOf(beMap.get("totalCount").toString())) * 100)));
        bfMap.put("percentage",String.format("%.2f", ((Double.valueOf(bfMap.get("podTypeCount").toString()) / Double.valueOf(bfMap.get("totalCount").toString())) * 100)));
        bgMap.put("percentage",String.format("%.2f", ((Double.valueOf(bgMap.get("podTypeCount").toString()) / Double.valueOf(bgMap.get("totalCount").toString())) * 100)));
        bhMap.put("percentage",String.format("%.2f", ((Double.valueOf(bhMap.get("podTypeCount").toString()) / Double.valueOf(bhMap.get("totalCount").toString())) * 100)));
        list.add(baMap);
        list.add(bbMap);
        list.add(bcMap);
        list.add(bdMap);
        list.add(beMap);
        list.add(bfMap);
        list.add(bgMap);
        list.add(bhMap);*/
        return list;
    }

    /**
     * 空容器统计导出
     * @param response
     * @param request
     */
    public void excelDownload(HttpServletResponse response, HttpServletRequest request) {
        List<Map<String, Object>> assembledList = getContainerCount();
        List<List<String>> dataList = new ArrayList<>();
        for (Map<String, Object> map : assembledList) {
            List<String> list = new ArrayList<>();
            list.add(map.get("podTypText").toString());
            list.add(map.get("totalCount").toString());
            list.add(map.get("podTypeCount").toString());
            list.add(map.get("percentage").toString());
            dataList.add(list);
        }
        List<String> headList = new ArrayList<>();
        headList.add("货架类型");
        headList.add("总仓位数");
        headList.add("空仓位数");
        headList.add("空仓占比（%）");
        ExcelUtil.uploadExcelAboutUser(request,response,"空容器统计",headList,dataList);
    }

    /**
     * 空容器统计列表
     * @param params
     * @return
     */
    public List<lyEmptyContainerVO> listPage(lyEmptyContainer params) {
        List<lyEmptyContainerVO> list = baseMapper.getList(params);
        return list;
    }

    public Map<String,Object> getListCount(lyEmptyContainer params){
        return baseMapper.getListCount(params);
    }


    /**
     * 空容器统计列表导出
     * @param response
     * @param request
     */
    public void excelDownload2(HttpServletResponse response, HttpServletRequest request,lyEmptyContainer params) {
        List<lyEmptyContainerVO> containerList = baseMapper.getList(params);
        List<List<String>> dataList = new ArrayList<>();
        for (lyEmptyContainerVO emptyContainer : containerList) {
            List<String> list = new ArrayList<>();
            list.add(emptyContainer.getPodTypText());
            list.add(emptyContainer.getPodCode());
            list.add(emptyContainer.getInitStatusText());
            list.add(emptyContainer.getBerthCode());
            list.add(emptyContainer.getBinUtilization());
            dataList.add(list);
        }
        List<String> headList = new ArrayList<>();
        headList.add("货架类型名称");
        headList.add("货架编号");
        headList.add("初始化状态");
        headList.add("储位编号");
        headList.add("空仓位数/总仓位数");
        ExcelUtil.uploadExcelAboutUser(request,response,"空容器列表",headList,dataList);
    }
}
