package com.bcsd.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.domain.AchievementArea;
import com.bcsd.project.domain.AchievementAreaDetail;
import com.bcsd.project.domain.AchievementProvince;
import com.bcsd.project.mapper.AchievementAreaDetailMapper;
import com.bcsd.project.mapper.AchievementAreaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bcsd.project.constants.Constants.DELETE_TAG_0;
import static com.bcsd.project.constants.Constants.DELETE_TAG_1;

/**
 * 滚动周期表 服务实现类
 *
 * @author liuliang
 * @since 2023-02-16
 */
@Slf4j
@Service
public class AchievementAreaService extends ServiceImpl<AchievementAreaMapper, AchievementArea> implements IService<AchievementArea> {

    @Autowired
    private AchievementAreaDetailMapper areaDetailMapper;

    @Transactional(rollbackFor = Exception.class)
    public boolean add(AchievementArea area) {
        super.save(area);
        for (AchievementAreaDetail areaDetail:area.getDetailList()) {
            areaDetail.setAchievementId(area.getId());
            areaDetail.setCreateBy(area.getCreateBy());
            areaDetail.setCreateTime(area.getCreateTime());
            areaDetailMapper.insert(areaDetail);
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean update(AchievementArea area) {
        super.updateById(area);

        LambdaQueryWrapper<AchievementAreaDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AchievementAreaDetail::getAchievementId, area.getId());
        areaDetailMapper.delete(wrapper);
        for (AchievementAreaDetail areaDetail:area.getDetailList()) {
            areaDetail.setAchievementId(area.getId());
            areaDetail.setUpdateBy(area.getUpdateBy());
            areaDetail.setUpdateTime(area.getUpdateTime());
            areaDetailMapper.insert(areaDetail);
        }

        return true;
    }

    public boolean delete(Long id) {
        AchievementArea area = new AchievementArea();
        area.setId(id);
        area.setDeleteTag(DELETE_TAG_1);
        return super.updateById(area);
    }

    public List<AchievementArea> list(AchievementArea area) {
        LambdaQueryWrapper<AchievementArea> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AchievementArea::getDeleteTag, DELETE_TAG_0);

        if (area.getYear() != null){
            wrapper.eq(AchievementArea::getYear, area.getYear());
        }

        if (StringUtils.isNotEmpty(area.getStatus())) {
            wrapper.eq(AchievementArea::getStatus, area.getStatus());
        }

        if (StringUtils.isNotEmpty(area.getTransferPaymentName())) {
            wrapper.like(AchievementArea::getTransferPaymentName, area.getTransferPaymentName());
        }

        wrapper.orderByDesc(AchievementArea::getCreateTime);
        return super.baseMapper.selectList(wrapper);
    }

    public AchievementArea getById(Long id) {
        AchievementArea area = super.getById(id);
        LambdaQueryWrapper<AchievementAreaDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AchievementAreaDetail::getAchievementId, area.getId());
        List<AchievementAreaDetail> detailList = areaDetailMapper.selectList(wrapper);
        area.setDetailList(detailList);

        return area;
    }

    public boolean exists(AchievementArea area) {
        LambdaQueryWrapper<AchievementArea> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AchievementArea::getDeleteTag, DELETE_TAG_0);
        wrapper.eq(AchievementArea::getDeptId, area.getDeptId());
        wrapper.eq(AchievementArea::getYear, area.getYear());
        return super.baseMapper.selectCount(wrapper) > 0;

    }
}
