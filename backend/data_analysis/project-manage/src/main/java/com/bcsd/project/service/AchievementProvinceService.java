package com.bcsd.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.constants.Constants;
import com.bcsd.project.domain.*;
import com.bcsd.project.mapper.AchievementAreaDetailMapper;
import com.bcsd.project.mapper.AchievementAreaMapper;
import com.bcsd.project.mapper.AchievementProvinceDetailMapper;
import com.bcsd.project.mapper.AchievementProvinceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class AchievementProvinceService extends ServiceImpl<AchievementProvinceMapper, AchievementProvince> implements IService<AchievementProvince> {

    @Autowired
    private AchievementAreaMapper areaMapper;
    @Autowired
    private AchievementAreaDetailMapper areaDetailMapper;

    @Autowired
    private AchievementProvinceDetailMapper provinceDetailMapper;

    public AchievementProvince compute(Integer year) {
        AchievementProvince province = new AchievementProvince();
        province.setYear(year);
        province.setDetailList(new ArrayList<AchievementProvinceDetail>());

        LambdaQueryWrapper<AchievementArea> areaWrapper = new LambdaQueryWrapper<>();
        areaWrapper.eq(AchievementArea::getDeleteTag, DELETE_TAG_0);
        areaWrapper.eq(AchievementArea::getStatus, Constants.ACHIEVEMENT_STATUS_REVIEWED);
        areaWrapper.eq(AchievementArea::getYear, year);
        List<AchievementArea> areaList = areaMapper.selectList(areaWrapper);
        if (areaList.isEmpty()) {
            return province;
        }

        LambdaQueryWrapper<AchievementAreaDetail> areaDetailWrapper = new LambdaQueryWrapper<>();
        areaDetailWrapper.eq(AchievementAreaDetail::getYear, year);
        List<AchievementAreaDetail> areaDetailList = areaDetailMapper.selectList(areaDetailWrapper);

        Map<String, AchievementGoal> t1_1_1Map = new HashMap<>();
        Map<String, AchievementGoal> t1_1_2Map = new HashMap<>();
        Map<String, AchievementGoal> t1_1_3Map = new HashMap<>();
        Map<String, AchievementGoal> t1_1_4Map = new HashMap<>();
        Map<String, AchievementGoal> t1_1_5Map = new HashMap<>();

        AchievementGoal t1_2_1 = new AchievementGoal();
        AchievementGoal t1_2_2 = new AchievementGoal();
        AchievementGoal t1_3_1 = new AchievementGoal();
        AchievementGoal t1_3_2 = new AchievementGoal();
        AchievementGoal t1_3_3 = new AchievementGoal();
        AchievementGoal t1_4_1 = new AchievementGoal();
        AchievementGoal t1_4_2 = new AchievementGoal();
        AchievementGoal t2_1_1 = new AchievementGoal();
        AchievementGoal t2_1_2 = new AchievementGoal();
        AchievementGoal t2_2_1 = new AchievementGoal();
        AchievementGoal t2_2_2 = new AchievementGoal();
        AchievementGoal t2_3_1 = new AchievementGoal();
        AchievementGoal t2_3_2 = new AchievementGoal();
        AchievementGoal t2_3_3 = new AchievementGoal();
        AchievementGoal t2_4_1 = new AchievementGoal();
        AchievementGoal t2_4_2 = new AchievementGoal();
        AchievementGoal t3_1_1 = new AchievementGoal();
        AchievementGoal t3_1_2 = new AchievementGoal();


        BigDecimal tBudgetFundCentral = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
        BigDecimal tBudgetFundLocal = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
        BigDecimal tBudgetFundOther = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
        BigDecimal tExecuteFundCentral = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
        BigDecimal tExecuteFundLocal = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
        BigDecimal tExecuteFundOther = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
        for (AchievementArea area: areaList) {
            if (area.getBudgetFundCentral2() != null) {
                tBudgetFundCentral = tBudgetFundCentral.add(new BigDecimal(area.getBudgetFundCentral2()));
            }
            area.setDetailList(new ArrayList<>());
            for (AchievementAreaDetail areaDetail: areaDetailList) {
                if (areaDetail.getAchievementId().equals(area.getId())) {
                    area.getDetailList().add(areaDetail);
                }
            };
        }

        if (tBudgetFundCentral.compareTo(BigDecimal.ZERO) == 0) {
            return province;
        }

        for (AchievementAreaDetail areaDetail: areaList.get(0).getDetailList()) {
            if (areaDetail.getGoalType().equals("1-1-1")) {
                t1_1_1Map.put(areaDetail.getGoalName(), new AchievementGoal(areaDetail.getPosition()));
            } else if (areaDetail.getGoalType().equals("1-1-2")) {
                t1_1_2Map.put(areaDetail.getGoalName(), new AchievementGoal(areaDetail.getPosition()));
            } else if (areaDetail.getGoalType().equals("1-1-3")) {
                t1_1_3Map.put(areaDetail.getGoalName(), new AchievementGoal(areaDetail.getPosition()));
            } else if (areaDetail.getGoalType().equals("1-1-4")) {
                t1_1_4Map.put(areaDetail.getGoalName(), new AchievementGoal(areaDetail.getPosition()));
            } else if (areaDetail.getGoalType().equals("1-1-5")) {
                t1_1_5Map.put(areaDetail.getGoalName(), new AchievementGoal(areaDetail.getPosition()));
            }
        }

        for (AchievementArea area: areaList) {
            if (area.getBudgetFundCentral2() == null || area.getBudgetFundCentral2().equals("0")) {
                continue;
            }
            if (area.getBudgetFundLocal2() != null) {
                tBudgetFundLocal = tBudgetFundLocal.add(new BigDecimal(area.getBudgetFundLocal2()));
            }
            if (area.getBudgetFundOther2() != null) {
                tBudgetFundOther = tBudgetFundOther.add(new BigDecimal(area.getBudgetFundOther2()));
            }
            if (area.getExecuteFundCentral2() != null) {
                tExecuteFundCentral = tExecuteFundCentral.add(new BigDecimal(area.getExecuteFundCentral2()));
            }
            if (area.getExecuteFundLocal2() != null) {
                tExecuteFundLocal = tExecuteFundLocal.add(new BigDecimal(area.getExecuteFundLocal2()));
            }
            if (area.getExecuteFundOther2() != null) {
                tExecuteFundOther = tExecuteFundOther.add(new BigDecimal(area.getExecuteFundOther2()));
            }

            for (AchievementAreaDetail areaDetail: area.getDetailList()) {
                if (areaDetail.getGoalType().equals("1-1-1")) {
                    AchievementGoal achievementGoal = t1_1_1Map.get(areaDetail.getGoalName());
                    if (areaDetail.getGoalValue2() != null) {
                        achievementGoal.setGoalValue(achievementGoal.getGoalValue().add(new BigDecimal(areaDetail.getGoalValue2())));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        achievementGoal.setGoalExecute(achievementGoal.getGoalExecute().add(new BigDecimal(areaDetail.getGoalExecute2())));
                    }
                } else if (areaDetail.getGoalType().equals("1-1-2")) {
                    AchievementGoal achievementGoal = t1_1_2Map.get(areaDetail.getGoalName());
                    if (areaDetail.getGoalValue2() != null) {
                        achievementGoal.setGoalValue(achievementGoal.getGoalValue().add(new BigDecimal(areaDetail.getGoalValue2())));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        achievementGoal.setGoalExecute(achievementGoal.getGoalExecute().add(new BigDecimal(areaDetail.getGoalExecute2())));
                    }
                } else if (areaDetail.getGoalType().equals("1-1-3")) {
                    AchievementGoal achievementGoal = t1_1_3Map.get(areaDetail.getGoalName());
                    if (areaDetail.getGoalValue2() != null) {
                        achievementGoal.setGoalValue(achievementGoal.getGoalValue().add(new BigDecimal(areaDetail.getGoalValue2())));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        achievementGoal.setGoalExecute(achievementGoal.getGoalExecute().add(new BigDecimal(areaDetail.getGoalExecute2())));
                    }
                } else if (areaDetail.getGoalType().equals("1-1-4")) {
                    AchievementGoal achievementGoal = t1_1_4Map.get(areaDetail.getGoalName());
                    if (areaDetail.getGoalValue2() != null) {
                        achievementGoal.setGoalValue(achievementGoal.getGoalValue().add(new BigDecimal(areaDetail.getGoalValue2())));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        achievementGoal.setGoalExecute(achievementGoal.getGoalExecute().add(new BigDecimal(areaDetail.getGoalExecute2())));
                    }
                } else if (areaDetail.getGoalType().equals("1-1-5")) {
                    AchievementGoal achievementGoal = t1_1_5Map.get(areaDetail.getGoalName());
                    if (areaDetail.getGoalValue2() != null) {
                        achievementGoal.setGoalValue(achievementGoal.getGoalValue().add(new BigDecimal(areaDetail.getGoalValue2())));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        achievementGoal.setGoalExecute(achievementGoal.getGoalExecute().add(new BigDecimal(areaDetail.getGoalExecute2())));
                    }
                } else if (areaDetail.getGoalType().equals("1-2-1")) {
                    t1_2_1.setPosition(areaDetail.getPosition());
                    if (areaDetail.getGoalValue2() != null) {
                        BigDecimal value = new BigDecimal(areaDetail.getGoalValue2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t1_2_1.setGoalValue(t1_2_1.getGoalValue().add(value));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        BigDecimal execute = new BigDecimal(areaDetail.getGoalExecute2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t1_2_1.setGoalExecute(t1_2_1.getGoalExecute().add(execute));
                    }
                } else if (areaDetail.getGoalType().equals("1-2-2")) {
                    t1_2_2.setPosition(areaDetail.getPosition());
                    if (areaDetail.getGoalValue2() != null) {
                        BigDecimal value = new BigDecimal(areaDetail.getGoalValue2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t1_2_2.setGoalValue(t1_2_2.getGoalValue().add(value));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        BigDecimal execute = new BigDecimal(areaDetail.getGoalExecute2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t1_2_2.setGoalExecute(t1_2_2.getGoalExecute().add(execute));
                    }
                } else if (areaDetail.getGoalType().equals("1-3-1")) {
                    t1_3_1.setPosition(areaDetail.getPosition());
                    if (areaDetail.getGoalValue2() != null) {
                        BigDecimal value = new BigDecimal(areaDetail.getGoalValue2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t1_3_1.setGoalValue(t1_3_1.getGoalValue().add(value));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        BigDecimal execute = new BigDecimal(areaDetail.getGoalExecute2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t1_3_1.setGoalExecute(t1_3_1.getGoalExecute().add(execute));
                    }
                } else if (areaDetail.getGoalType().equals("1-3-2")) {
                    t1_3_2.setPosition(areaDetail.getPosition());
                    if (areaDetail.getGoalValue2() != null) {
                        BigDecimal value = new BigDecimal(areaDetail.getGoalValue2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t1_3_2.setGoalValue(t1_3_2.getGoalValue().add(value));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        BigDecimal execute = new BigDecimal(areaDetail.getGoalExecute2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t1_3_2.setGoalExecute(t1_3_2.getGoalExecute().add(execute));
                    }
                } else if (areaDetail.getGoalType().equals("1-3-3")) {
                    t1_3_3.setPosition(areaDetail.getPosition());
                    if (areaDetail.getGoalValue2() != null) {
                        BigDecimal value = new BigDecimal(areaDetail.getGoalValue2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t1_3_3.setGoalValue(t1_3_3.getGoalValue().add(value));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        BigDecimal execute = new BigDecimal(areaDetail.getGoalExecute2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t1_3_3.setGoalExecute(t1_3_3.getGoalExecute().add(execute));
                    }
                } else if (areaDetail.getGoalType().equals("1-4-1")) {
                    t1_4_1.setPosition(areaDetail.getPosition());
                    if (areaDetail.getGoalValue2() != null) {
                        BigDecimal value = new BigDecimal(areaDetail.getGoalValue2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t1_4_1.setGoalValue(t1_4_1.getGoalValue().add(value));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        BigDecimal execute = new BigDecimal(areaDetail.getGoalExecute2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t1_4_1.setGoalExecute(t1_4_1.getGoalExecute().add(execute));
                    }
                } else if (areaDetail.getGoalType().equals("1-4-2")) {
                    t1_4_2.setPosition(areaDetail.getPosition());
                    if (areaDetail.getGoalValue2() != null) {
                        BigDecimal value = new BigDecimal(areaDetail.getGoalValue2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t1_4_2.setGoalValue(t1_4_2.getGoalValue().add(value));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        BigDecimal execute = new BigDecimal(areaDetail.getGoalExecute2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t1_4_2.setGoalExecute(t1_4_2.getGoalExecute().add(execute));
                    }
                } else if (areaDetail.getGoalType().equals("2-1-1")) {
                    t2_1_1.setPosition(areaDetail.getPosition());
                    if (areaDetail.getGoalValue2() != null) {
                        BigDecimal value = new BigDecimal(areaDetail.getGoalValue2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t2_1_1.setGoalValue(t2_1_1.getGoalValue().add(value));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        BigDecimal execute = new BigDecimal(areaDetail.getGoalExecute2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t2_1_1.setGoalExecute(t2_1_1.getGoalExecute().add(execute));
                    }
                } else if (areaDetail.getGoalType().equals("2-1-2")) {
                    t2_1_2.setPosition(areaDetail.getPosition());
                    if (areaDetail.getGoalValue2() != null) {
                        BigDecimal value = new BigDecimal(areaDetail.getGoalValue2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t2_1_2.setGoalValue(t2_1_2.getGoalValue().add(value));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        BigDecimal execute = new BigDecimal(areaDetail.getGoalExecute2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t2_1_2.setGoalExecute(t2_1_2.getGoalExecute().add(execute));
                    }
                } else if (areaDetail.getGoalType().equals("2-2-1")) {
                    t2_2_1.setPosition(areaDetail.getPosition());
                    if (areaDetail.getGoalValue2() != null) {
                        BigDecimal value = new BigDecimal(areaDetail.getGoalValue2());
                        t2_2_1.setGoalValue(t2_2_1.getGoalValue().add(value));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        BigDecimal execute = new BigDecimal(areaDetail.getGoalExecute2());
                        t2_2_1.setGoalExecute(t2_2_1.getGoalExecute().add(execute));
                    }
                } else if (areaDetail.getGoalType().equals("2-2-2")) {
                    t2_2_2.setPosition(areaDetail.getPosition());
                    if (areaDetail.getGoalValue2() != null) {
                        BigDecimal value = new BigDecimal(areaDetail.getGoalValue2());
                        t2_2_2.setGoalValue(t2_2_2.getGoalValue().add(value));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        BigDecimal execute = new BigDecimal(areaDetail.getGoalExecute2());
                        t2_2_2.setGoalExecute(t2_2_2.getGoalExecute().add(execute));
                    }
                } else if (areaDetail.getGoalType().equals("2-3-1")) {
                    t2_3_1.setPosition(areaDetail.getPosition());
                    if (areaDetail.getGoalValue2() != null) {
                        BigDecimal value = new BigDecimal(areaDetail.getGoalValue2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t2_3_1.setGoalValue(t2_3_1.getGoalValue().add(value));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        BigDecimal execute = new BigDecimal(areaDetail.getGoalExecute2())
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t2_3_1.setGoalExecute(t2_3_1.getGoalExecute().add(execute));
                    }
                } else if (areaDetail.getGoalType().equals("2-3-2")) {
                    t2_3_2.setPosition(areaDetail.getPosition());
                    if (areaDetail.getGoalValue2() != null) {
                        BigDecimal value = new BigDecimal(areaDetail.getGoalValue2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t2_3_2.setGoalValue(t2_3_2.getGoalValue().add(value));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        BigDecimal execute = new BigDecimal(areaDetail.getGoalExecute2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t2_3_2.setGoalExecute(t2_3_2.getGoalExecute().add(execute));
                    }
                } else if (areaDetail.getGoalType().equals("2-3-3")) {
                    t2_3_3.setPosition(areaDetail.getPosition());
                } else if (areaDetail.getGoalType().equals("2-4-1")) {
                    t2_4_1.setPosition(areaDetail.getPosition());
                    if (areaDetail.getGoalValue2() != null) {
                        BigDecimal value = new BigDecimal(areaDetail.getGoalValue2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t2_4_1.setGoalValue(t2_4_1.getGoalValue().add(value));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        BigDecimal execute = new BigDecimal(areaDetail.getGoalExecute2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t2_4_1.setGoalExecute(t2_4_1.getGoalExecute().add(execute));
                    }
                } else if (areaDetail.getGoalType().equals("2-4-2")) {
                    t2_4_2.setPosition(areaDetail.getPosition());
                } else if (areaDetail.getGoalType().equals("3-1-1")) {
                    t3_1_1.setPosition(areaDetail.getPosition());
                    if (areaDetail.getGoalValue2() != null) {
                        BigDecimal value = new BigDecimal(areaDetail.getGoalValue2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t3_1_1.setGoalValue(t3_1_1.getGoalValue().add(value));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        BigDecimal execute = new BigDecimal(areaDetail.getGoalExecute2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t3_1_1.setGoalExecute(t3_1_1.getGoalExecute().add(execute));
                    }
                } else if (areaDetail.getGoalType().equals("3-1-2")) {
                    t3_1_2.setPosition(areaDetail.getPosition());
                    if (areaDetail.getGoalValue2() != null) {
                        BigDecimal value = new BigDecimal(areaDetail.getGoalValue2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t3_1_2.setGoalValue(t3_1_2.getGoalValue().add(value));
                    }
                    if (areaDetail.getGoalExecute2() != null) {
                        BigDecimal execute = new BigDecimal(areaDetail.getGoalExecute2()).setScale(2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(area.getBudgetFundCentral2()))
                                .divide(tBudgetFundCentral, BigDecimal.ROUND_HALF_UP);
                        t3_1_2.setGoalExecute(t3_1_2.getGoalExecute().add(execute));
                    }
                }
            }
        }

        province.setBudgetFundCentral(tBudgetFundCentral.stripTrailingZeros().toPlainString());
        province.setBudgetFundLocal(tBudgetFundLocal.stripTrailingZeros().toPlainString());
        province.setBudgetFundOther(tBudgetFundOther.stripTrailingZeros().toPlainString());
        province.setExecuteFundCentral(tExecuteFundCentral.stripTrailingZeros().toPlainString());
        province.setExecuteFundLocal(tExecuteFundLocal.stripTrailingZeros().toPlainString());
        province.setExecuteFundOther(tExecuteFundOther.stripTrailingZeros().toPlainString());

        for (Map.Entry<String, AchievementGoal> entry : t1_1_1Map.entrySet()) {
            AchievementGoal goal = entry.getValue();
            AchievementProvinceDetail provinceDetail = new AchievementProvinceDetail();
            provinceDetail.setYear(year);
            provinceDetail.setPosition(goal.getPosition());
            provinceDetail.setGoalType("1-1-1");
            provinceDetail.setGoalName(entry.getKey());
            provinceDetail.setGoalValue(goal.getGoalValue().stripTrailingZeros().toPlainString());
            provinceDetail.setGoalExecute(goal.getGoalExecute().stripTrailingZeros().toPlainString());
            province.getDetailList().add(provinceDetail);
        }
        for (Map.Entry<String, AchievementGoal> entry : t1_1_2Map.entrySet()) {
            AchievementGoal goal = entry.getValue();
            AchievementProvinceDetail provinceDetail = new AchievementProvinceDetail();
            provinceDetail.setYear(year);
            provinceDetail.setPosition(goal.getPosition());
            provinceDetail.setGoalType("1-1-2");
            provinceDetail.setGoalName(entry.getKey());
            provinceDetail.setGoalValue(goal.getGoalValue().stripTrailingZeros().toPlainString());
            provinceDetail.setGoalExecute(goal.getGoalExecute().stripTrailingZeros().toPlainString());
            province.getDetailList().add(provinceDetail);
        }
        for (Map.Entry<String, AchievementGoal> entry : t1_1_3Map.entrySet()) {
            AchievementGoal goal = entry.getValue();
            AchievementProvinceDetail provinceDetail = new AchievementProvinceDetail();
            provinceDetail.setYear(year);
            provinceDetail.setPosition(goal.getPosition());
            provinceDetail.setGoalType("1-1-3");
            provinceDetail.setGoalName(entry.getKey());
            provinceDetail.setGoalValue(goal.getGoalValue().stripTrailingZeros().toPlainString());
            provinceDetail.setGoalExecute(goal.getGoalExecute().stripTrailingZeros().toPlainString());
            province.getDetailList().add(provinceDetail);
        }
        for (Map.Entry<String, AchievementGoal> entry : t1_1_4Map.entrySet()) {
            AchievementGoal goal = entry.getValue();
            AchievementProvinceDetail provinceDetail = new AchievementProvinceDetail();
            provinceDetail.setYear(year);
            provinceDetail.setPosition(goal.getPosition());
            provinceDetail.setGoalType("1-1-4");
            provinceDetail.setGoalName(entry.getKey());
            provinceDetail.setGoalValue(goal.getGoalValue().stripTrailingZeros().toPlainString());
            provinceDetail.setGoalExecute(goal.getGoalExecute().stripTrailingZeros().toPlainString());
            province.getDetailList().add(provinceDetail);
        }
        for (Map.Entry<String, AchievementGoal> entry : t1_1_5Map.entrySet()) {
            AchievementGoal goal = entry.getValue();
            AchievementProvinceDetail provinceDetail = new AchievementProvinceDetail();
            provinceDetail.setYear(year);
            provinceDetail.setPosition(goal.getPosition());
            provinceDetail.setGoalType("1-1-5");
            provinceDetail.setGoalName(entry.getKey());
            provinceDetail.setGoalValue(goal.getGoalValue().stripTrailingZeros().toPlainString());
            provinceDetail.setGoalExecute(goal.getGoalExecute().stripTrailingZeros().toPlainString());
            province.getDetailList().add(provinceDetail);
        }

        AchievementProvinceDetail provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t1_2_1.getPosition());
        provinceDetail.setGoalType("1-2-1");
        provinceDetail.setGoalName("工程质量合格率（%）");
        provinceDetail.setGoalValue(t1_2_1.getGoalValue().stripTrailingZeros().toPlainString());
        provinceDetail.setGoalExecute(t1_2_1.getGoalExecute().stripTrailingZeros().toPlainString());
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t1_2_2.getPosition());
        provinceDetail.setGoalType("1-2-2");
        provinceDetail.setGoalName("项目验收通过率（%）");
        provinceDetail.setGoalValue(t1_2_2.getGoalValue().stripTrailingZeros().toPlainString());
        provinceDetail.setGoalExecute(t1_2_2.getGoalExecute().stripTrailingZeros().toPlainString());
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t1_3_1.getPosition());
        provinceDetail.setGoalType("1-3-1");
        provinceDetail.setGoalName("项目按时开工率（%）");
        provinceDetail.setGoalValue(t1_3_1.getGoalValue().stripTrailingZeros().toPlainString());
        provinceDetail.setGoalExecute(t1_3_1.getGoalExecute().stripTrailingZeros().toPlainString());
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t1_3_2.getPosition());
        provinceDetail.setGoalType("1-3-2");
        provinceDetail.setGoalName("项目按时验收率（%）");
        provinceDetail.setGoalValue(t1_3_2.getGoalValue().stripTrailingZeros().toPlainString());
        provinceDetail.setGoalExecute(t1_3_2.getGoalExecute().stripTrailingZeros().toPlainString());
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t1_3_3.getPosition());
        provinceDetail.setGoalType("1-3-3");
        provinceDetail.setGoalName("年度预算执行率（≥%）");
        provinceDetail.setGoalValue(t1_3_3.getGoalValue().stripTrailingZeros().toPlainString());
        provinceDetail.setGoalExecute(t1_3_3.getGoalExecute().stripTrailingZeros().toPlainString());
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t1_4_1.getPosition());
        provinceDetail.setGoalType("1-4-1");
        provinceDetail.setGoalName("实际完成投资控制在概算内的项目比例（%）");
        provinceDetail.setGoalValue(t1_4_1.getGoalValue().stripTrailingZeros().toPlainString());
        provinceDetail.setGoalExecute(t1_4_1.getGoalExecute().stripTrailingZeros().toPlainString());
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t1_4_2.getPosition());
        provinceDetail.setGoalType("1-4-2");
        provinceDetail.setGoalName("项目调整概算程序完备率（%）");
        provinceDetail.setGoalValue(t1_4_2.getGoalValue().stripTrailingZeros().toPlainString());
        provinceDetail.setGoalExecute(t1_4_2.getGoalExecute().stripTrailingZeros().toPlainString());
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t2_1_1.getPosition());
        provinceDetail.setGoalType("2-1-1");
        provinceDetail.setGoalName("促进城镇移民小区居民人均增收额（元）");
        provinceDetail.setGoalValue(t2_1_1.getGoalValue().stripTrailingZeros().toPlainString());
        provinceDetail.setGoalExecute(t2_1_1.getGoalExecute().stripTrailingZeros().toPlainString());
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t2_1_2.getPosition());
        provinceDetail.setGoalType("2-1-2");
        provinceDetail.setGoalName("促进农村移民安置村（组）居民人均增收额（元）");
        provinceDetail.setGoalValue(t2_1_2.getGoalValue().stripTrailingZeros().toPlainString());
        provinceDetail.setGoalExecute(t2_1_2.getGoalExecute().stripTrailingZeros().toPlainString());
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t2_2_1.getPosition());
        provinceDetail.setGoalType("2-2-1");
        provinceDetail.setGoalName("城镇居民受益人口（人）");
        provinceDetail.setGoalValue(t2_2_1.getGoalValue().stripTrailingZeros().toPlainString());
        provinceDetail.setGoalExecute(t2_2_1.getGoalExecute().stripTrailingZeros().toPlainString());
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t2_2_2.getPosition());
        provinceDetail.setGoalType("2-2-2");
        provinceDetail.setGoalName("农村居民受益人口（人）");
        provinceDetail.setGoalValue(t2_2_2.getGoalValue().stripTrailingZeros().toPlainString());
        provinceDetail.setGoalExecute(t2_2_2.getGoalExecute().stripTrailingZeros().toPlainString());
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t2_3_1.getPosition());
        provinceDetail.setGoalType("2-3-1");
        provinceDetail.setGoalName("库区干流水质达到类别（类）");
        provinceDetail.setGoalValue(t2_3_1.getGoalValue().stripTrailingZeros().toPlainString());
        provinceDetail.setGoalExecute(t2_3_1.getGoalExecute().stripTrailingZeros().toPlainString());
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t2_3_2.getPosition());
        provinceDetail.setGoalType("2-3-2");
        provinceDetail.setGoalName("库区森林覆盖率（≥%）");
        provinceDetail.setGoalValue(t2_3_2.getGoalValue().stripTrailingZeros().toPlainString());
        provinceDetail.setGoalExecute(t2_3_2.getGoalExecute().stripTrailingZeros().toPlainString());
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t2_3_3.getPosition());
        provinceDetail.setGoalType("2-3-3");
        provinceDetail.setGoalName("对当地生态环境产生积极影响（是/否）");
        provinceDetail.setGoalValue("--");
        provinceDetail.setGoalExecute("--");
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t2_4_1.getPosition());
        provinceDetail.setGoalType("2-4-1");
        provinceDetail.setGoalName("项目完成后正常运行比例（%）");
        provinceDetail.setGoalValue(t2_4_1.getGoalValue().stripTrailingZeros().toPlainString());
        provinceDetail.setGoalExecute(t2_4_1.getGoalExecute().stripTrailingZeros().toPlainString());
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t2_4_2.getPosition());
        provinceDetail.setGoalType("2-4-2");
        provinceDetail.setGoalName("对移民收入增长的促进作用（是/否）");
        provinceDetail.setGoalValue("--");
        provinceDetail.setGoalExecute("--");
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t3_1_1.getPosition());
        provinceDetail.setGoalType("3-1-1");
        provinceDetail.setGoalName("移民安置区居民（含移民）满意度（≥%）");
        provinceDetail.setGoalValue(t3_1_1.getGoalValue().stripTrailingZeros().toPlainString());
        provinceDetail.setGoalExecute(t3_1_1.getGoalExecute().stripTrailingZeros().toPlainString());
        province.getDetailList().add(provinceDetail);

        provinceDetail = new AchievementProvinceDetail();
        provinceDetail.setYear(year);
        provinceDetail.setPosition(t3_1_2.getPosition());
        provinceDetail.setGoalType("3-1-2");
        provinceDetail.setGoalName("促进相关信访问题化解率（≥%）");
        provinceDetail.setGoalValue(t3_1_2.getGoalValue().stripTrailingZeros().toPlainString());
        provinceDetail.setGoalExecute(t3_1_2.getGoalExecute().stripTrailingZeros().toPlainString());
        province.getDetailList().add(provinceDetail);

        return province;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean add(AchievementProvince province) {
        super.save(province);
        for (AchievementProvinceDetail provinceDetail: province.getDetailList()) {
            provinceDetail.setAchievementId(province.getId());
            provinceDetail.setCreateBy(province.getCreateBy());
            provinceDetail.setCreateTime(province.getCreateTime());
            provinceDetailMapper.insert(provinceDetail);
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean update(AchievementProvince province) {
        super.updateById(province);

        LambdaQueryWrapper<AchievementProvinceDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AchievementProvinceDetail::getAchievementId, province.getId());
        provinceDetailMapper.delete(wrapper);
        for (AchievementProvinceDetail provinceDetail: province.getDetailList()) {
            provinceDetail.setAchievementId(province.getId());
            provinceDetail.setUpdateBy(province.getUpdateBy());
            provinceDetail.setUpdateTime(province.getUpdateTime());
            provinceDetailMapper.insert(provinceDetail);
        }

        return true;
    }

    public boolean delete(Long id) {
        AchievementProvince province = new AchievementProvince();
        province.setId(id);
        province.setDeleteTag(DELETE_TAG_1);
        return super.updateById(province);
    }

    public List<AchievementProvince> list(AchievementProvince province) {
        LambdaQueryWrapper<AchievementProvince> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AchievementProvince::getDeleteTag, DELETE_TAG_0);

        if (province.getYear() != null){
            wrapper.eq(AchievementProvince::getYear, province.getYear());
        }

        if (StringUtils.isNotEmpty(province.getTransferPaymentName())) {
            wrapper.like(AchievementProvince::getTransferPaymentName, province.getTransferPaymentName());
        }

        wrapper.orderByDesc(AchievementProvince::getCreateTime);
        return super.baseMapper.selectList(wrapper);
    }

    public AchievementProvince getById(Long id) {
        AchievementProvince province = super.getById(id);
        LambdaQueryWrapper<AchievementProvinceDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AchievementProvinceDetail::getAchievementId, province.getId());
        List<AchievementProvinceDetail> detailList = provinceDetailMapper.selectList(wrapper);
        province.setDetailList(detailList);

        return province;
    }

    public AchievementProvince getByYear(Integer year) {
        LambdaQueryWrapper<AchievementProvince> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AchievementProvince::getYear, year);
        wrapper.eq(AchievementProvince::getDeleteTag, DELETE_TAG_0);
        List<AchievementProvince> provinceList = super.baseMapper.selectList(wrapper);
        if (provinceList.isEmpty()) {
            return null;
        } else {
            return  provinceList.get(0);
        }
    }
}
