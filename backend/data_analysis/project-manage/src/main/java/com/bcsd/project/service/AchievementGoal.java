package com.bcsd.project.service;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class AchievementGoal {
    private BigDecimal goalValue = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
    private BigDecimal goalExecute = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
    private Integer position;

    public AchievementGoal() {
    }


    public AchievementGoal(Integer position) {
        this.position = position;
    }
}
