package org.linlinjava.litemall.db.domain;

import java.util.Date;

public class XiaolvquanUserBill {
    private Integer id;

    private Integer userId;

    private Float promotionActualFee;

    private Float recommendActualFee;

    private Float promotionEstimateFee;

    private Float recommendEstimateFee;

    private Float rebateEstimateFee;

    private Float rebateActualFee;

    private Integer status;

    private Integer addBanlance;

    private String month;

    private Date updatetime;

    public Float getRebateEstimateFee() {
        return rebateEstimateFee;
    }

    public void setRebateEstimateFee(Float rebateEstimateFee) {
        this.rebateEstimateFee = rebateEstimateFee;
    }

    public Float getRebateActualFee() {
        return rebateActualFee;
    }

    public void setRebateActualFee(Float rebateActualFee) {
        this.rebateActualFee = rebateActualFee;
    }

    public Float getPromotionEstimateFee() {
        return promotionEstimateFee;
    }

    public void setPromotionEstimateFee(Float promotionEstimateFee) {
        this.promotionEstimateFee = promotionEstimateFee;
    }

    public Float getRecommendEstimateFee() {
        return recommendEstimateFee;
    }

    public void setRecommendEstimateFee(Float recommendEstimateFee) {
        this.recommendEstimateFee = recommendEstimateFee;
    }

    public Integer getAddBanlance() {
        return addBanlance;
    }

    public void setAddBanlance(Integer addBanlance) {
        this.addBanlance = addBanlance;
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

    public Float getPromotionActualFee() {
        return promotionActualFee;
    }

    public void setPromotionActualFee(Float promotionActualFee) {
        this.promotionActualFee = promotionActualFee;
    }

    public Float getRecommendActualFee() {
        return recommendActualFee;
    }

    public void setRecommendActualFee(Float recommendActualFee) {
        this.recommendActualFee = recommendActualFee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month == null ? null : month.trim();
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}