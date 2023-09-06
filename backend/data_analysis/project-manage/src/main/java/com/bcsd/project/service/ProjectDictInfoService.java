package com.bcsd.project.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.domain.ProjectDictInfo;
import com.bcsd.project.mapper.ProjectDictInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统字典 服务实现类
 * @author liuliang
 * @since 2023-02-20
 */
@Slf4j
@Service
public class ProjectDictInfoService extends ServiceImpl<ProjectDictInfoMapper, ProjectDictInfo> implements IService<ProjectDictInfo> {

    /**
     * 根据父编号获取
     * @param pid
     * @return
     */
    public List<ProjectDictInfo> getByPid(String pid){
        LambdaQueryWrapper<ProjectDictInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectDictInfo::getPid,pid);
        return super.baseMapper.selectList(wrapper);
    }

    /**
     * 根据PID获取二级结构数据
     * @param pid
     * @return
     */
    public Map<String,ProjectDictInfo> getMapByPid(String pid){
        List<ProjectDictInfo> list = super.baseMapper.selectByPid(pid);
        return list.stream().collect(Collectors.toMap(v -> v.getId(),v -> v));
    }

    /**
     * 获取列表
     * @param no
     * @return
     */
    public List<ProjectDictInfo> getListByNo(String no){
        LambdaQueryWrapper<ProjectDictInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(ProjectDictInfo::getId, StringUtils.join(no,"000000"), StringUtils.join(no,"999999"));
        return super.baseMapper.selectList(wrapper);
    }

    /**
     * 获取Map结构
     * @param no
     * @return
     */
    public Map<String,ProjectDictInfo> getMapByNo(String no){
        List<ProjectDictInfo> list = getListByNo(no);
        return list.stream().collect(Collectors.toMap(v -> v.getId(),v -> v));
    }

    /**
     * 获取树形结构
     * @param no
     * @return
     */
    public JSONArray getTreeByNo(String no){
        return init(getListByNo(no),StringUtils.join(no,"000000"));
    }

    /**
     * 递归
     * @param dicts
     * @param pid
     * @return
     */
    private JSONArray init(List<ProjectDictInfo> dicts, String pid){
        //过滤
        List<ProjectDictInfo> list =  dicts.stream().filter(ProjectDictInfo -> pid.equals(ProjectDictInfo.getPid())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        //数据转换
        JSONArray array = new JSONArray();
        list.forEach(dict ->{
            JSONObject obj = new JSONObject();
            obj.put("value",dict.getId());
            obj.put("label",dict.getName());
            obj.put("remark",dict.getRemark());
            JSONArray subset = init(dicts,dict.getId());
            if (subset!=null){
                obj.put("children",subset);
            }
            array.add(obj);
        });
        //排序
        array.sort(Comparator.comparing(obj -> ((JSONObject)obj).getString("value")));
        return array;
    }

}
