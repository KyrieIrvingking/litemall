package org.linlinjava.litemall.db.domain;

import java.math.BigDecimal;
import java.util.Date;

public class XiaolvquanUserEffect {
    private Integer id;

    private Integer userId;

    private Double estimateFee;

    private Double actualFee;

    private Double promotionEstimateFee;

    private Double promotionActualFee;

    private Double recommendEstimateFee;

    private Double recommendActualFee;

    private Double estimateCosPrice;

    private Double actualCosPrice;

    private Integer orderNum;

    private Double orderPrice;

    private Double rebateEstimateFee;

    private Double rebateActualFee;

    private String day;

    private Date updatetime;

    public Double getRebateEstimateFee() {
        return rebateEstimateFee;
    }

    public void setRebateEstimateFee(Double rebateEstimateFee) {
        this.rebateEstimateFee = rebateEstimateFee;
    }

    public Double getRebateActualFee() {
        return rebateActualFee;
    }

    public void setRebateActualFee(Double rebateActualFee) {
        this.rebateActualFee = rebateActualFee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getEstimateFee() {
        return estimateFee;
    }

    public void setEstimateFee(Double estimateFee) {
        this.estimateFee = estimateFee;
    }

    public Double getActualFee() {
        return actualFee;
    }

    public void setActualFee(Double actualFee) {
        this.actualFee = actualFee;
    }

    public Double getPromotionEstimateFee() {
        return promotionEstimateFee;
    }

    public void setPromotionEstimateFee(Double promotionEstimateFee) {
        this.promotionEstimateFee = promotionEstimateFee;
    }

    public Double getPromotionActualFee() {
        return promotionActualFee;
    }

    public void setPromotionActualFee(Double promotionActualFee) {
        this.promotionActualFee = promotionActualFee;
    }

    public Double getRecommendEstimateFee() {
        return recommendEstimateFee;
    }

    public void setRecommendEstimateFee(Double recommendEstimateFee) {
        this.recommendEstimateFee = recommendEstimateFee;
    }

    public Double getRecommendActualFee() {
        return recommendActualFee;
    }

    public void setRecommendActualFee(Double recommendActualFee) {
        this.recommendActualFee = recommendActualFee;
    }

    public Double getEstimateCosPrice() {
        return estimateCosPrice;
    }

    public void setEstimateCosPrice(Double estimateCosPrice) {
        this.estimateCosPrice = estimateCosPrice;
    }

    public Double getActualCosPrice() {
        return actualCosPrice;
    }

    public void setActualCosPrice(Double actualCosPrice) {
        this.actualCosPrice = actualCosPrice;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}