package com.bcsd.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.project.domain.RuleSystem;
import com.bcsd.project.mapper.RuleSystemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bcsd.project.constants.Constants.DELETE_TAG_1;

/**
 * 规章制度表 服务实现类
 *
 * @author liuliang
 * @since 2023-03-16
 */
@Slf4j
@Service
public class RuleSystemService extends ServiceImpl<RuleSystemMapper, RuleSystem> implements IService<RuleSystem> {

    /**
     * 删除
     * @param id
     * @return
     */
    public boolean delete(Long id){
        RuleSystem params = new RuleSystem();
        params.setId(id);
        params.setDeleteTag(DELETE_TAG_1);
        return super.updateById(params);
    }

    /**
     * 分页查询
     * @param params
     * @return
     */
    public List<RuleSystem> list(RuleSystem params){
        return super.baseMapper.listPage(params);
    }
}
