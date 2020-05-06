package org.linlinjava.litemall.db.domain;

import java.util.Date;

public class XiaolvquanCommissionConfig {
    private Integer id;

    private Integer userLevel;

    private String userLevelName;

    private Integer minInviteUsers;

    private Integer maxInviteUsers;

    private Integer promotionRate;

    private Integer recommendRate;

    private Integer nocouponRebateRate;

    private Integer nocouponRebatePromotionRate;

    private Integer nocouponPromotionRate;

    private Date addTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserLevelName() {
        return userLevelName;
    }

    public void setUserLevelName(String userLevelName) {
        this.userLevelName = userLevelName == null ? null : userLevelName.trim();
    }

    public Integer getMinInviteUsers() {
        return minInviteUsers;
    }

    public void setMinInviteUsers(Integer minInviteUsers) {
        this.minInviteUsers = minInviteUsers;
    }

    public Integer getMaxInviteUsers() {
        return maxInviteUsers;
    }

    public void setMaxInviteUsers(Integer maxInviteUsers) {
        this.maxInviteUsers = maxInviteUsers;
    }

    public Integer getPromotionRate() {
        return promotionRate;
    }

    public void setPromotionRate(Integer promotionRate) {
        this.promotionRate = promotionRate;
    }

    public Integer getRecommendRate() {
        return recommendRate;
    }

    public void setRecommendRate(Integer recommendRate) {
        this.recommendRate = recommendRate;
    }

    public Integer getNocouponRebateRate() {
        return nocouponRebateRate;
    }

    public void setNocouponRebateRate(Integer nocouponRebateRate) {
        this.nocouponRebateRate = nocouponRebateRate;
    }

    public Integer getNocouponRebatePromotionRate() {
        return nocouponRebatePromotionRate;
    }

    public void setNocouponRebatePromotionRate(Integer nocouponRebatePromotionRate) {
        this.nocouponRebatePromotionRate = nocouponRebatePromotionRate;
    }

    public Integer getNocouponPromotionRate() {
        return nocouponPromotionRate;
    }

    public void setNocouponPromotionRate(Integer nocouponPromotionRate) {
        this.nocouponPromotionRate = nocouponPromotionRate;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}