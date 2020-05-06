package org.linlinjava.litemall.core.api;

import java.io.Serializable;

public class JdCommodityDto implements Serializable {

    private long skuId;
    private Object imageInfo;
    private String skuName;
    private Double jdPrice;
    private Double discount;  //券面额
    private Double discountPrice;  //券后价格
    private Integer platformType;  //券使用平台
    private Integer bindType;  //券种类
    private String link; //券地址
    private long getStartTime;
    private long getEndTime;
    private long useStartTime;
    private long useEndTime;
    private double commission; //佣金
    private String commissionStr; //b保留小数后的佣金
    private double commissionRate;
    private long inOrderCount30Days;
    private long inOrderCount30DaysSku;
    private String materialUrl;
    private Integer isHot;
    private Integer priceType;  //最低价格类型，1：无线价格；2：拼购价格； 3：秒杀价格
    private Long pingouCount; //拼购数量
    private Long comments; //评论数
    private Double goodComments; //好评率

    private boolean hasRebate;//是否有返利，针对商品是否有优惠券
    private String rebate;//返利金额，自购买并且没有优惠券的情况下
    private String rebatePrice;//返利价

    private String memberCommission;//会员推广佣金，仅用于普通用户

    private Integer columnGoodsId;//专题商品主键id
    private Integer postion;//专题商品固定位
    private String note;//专题商品备注

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getPostion() {
        return postion;
    }

    public void setPostion(Integer postion) {
        this.postion = postion;
    }

    public Integer getColumnGoodsId() {
        return columnGoodsId;
    }

    public void setColumnGoodsId(Integer columnGoodsId) {
        this.columnGoodsId = columnGoodsId;
    }

    public String getMemberCommission() {
        return memberCommission;
    }

    public void setMemberCommission(String memberCommission) {
        this.memberCommission = memberCommission;
    }

    public String getRebatePrice() {
        return rebatePrice;
    }

    public void setRebatePrice(String rebatePrice) {
        this.rebatePrice = rebatePrice;
    }

    public boolean isHasRebate() {
        return hasRebate;
    }

    public void setHasRebate(boolean hasRebate) {
        this.hasRebate = hasRebate;
    }

    public String getRebate() {
        return rebate;
    }

    public void setRebate(String rebate) {
        this.rebate = rebate;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public Double getGoodComments() {
        return goodComments;
    }

    public void setGoodComments(Double goodComments) {
        this.goodComments = goodComments;
    }

    public String getCommissionStr() {
        return commissionStr;
    }

    public void setCommissionStr(String commissionStr) {
        this.commissionStr = commissionStr;
    }

    public Long getPingouCount() {
        return pingouCount;
    }

    public void setPingouCount(Long pingouCount) {
        this.pingouCount = pingouCount;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getSkuId() {
        return skuId;
    }

    public void setSkuId(long skuId) {
        this.skuId = skuId;
    }

    public Object getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(Object imageInfo) {
        this.imageInfo = imageInfo;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Double getJdPrice() {
        return jdPrice;
    }

    public void setJdPrice(Double jdPrice) {
        this.jdPrice = jdPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getPlatformType() {
        return platformType;
    }

    public void setPlatformType(Integer platformType) {
        this.platformType = platformType;
    }

    public Integer getBindType() {
        return bindType;
    }

    public void setBindType(Integer bindType) {
        this.bindType = bindType;
    }

    public long getGetStartTime() {
        return getStartTime;
    }

    public void setGetStartTime(long getStartTime) {
        this.getStartTime = getStartTime;
    }

    public long getGetEndTime() {
        return getEndTime;
    }

    public void setGetEndTime(long getEndTime) {
        this.getEndTime = getEndTime;
    }

    public long getUseStartTime() {
        return useStartTime;
    }

    public void setUseStartTime(long useStartTime) {
        this.useStartTime = useStartTime;
    }

    public long getUseEndTime() {
        return useEndTime;
    }

    public void setUseEndTime(long useEndTime) {
        this.useEndTime = useEndTime;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public long getInOrderCount30Days() {
        return inOrderCount30Days;
    }

    public void setInOrderCount30Days(long inOrderCount30Days) {
        this.inOrderCount30Days = inOrderCount30Days;
    }

    public String getMaterialUrl() {
        return materialUrl;
    }

    public void setMaterialUrl(String materialUrl) {
        this.materialUrl = materialUrl;
    }

    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }

    public long getInOrderCount30DaysSku() {
        return inOrderCount30DaysSku;
    }

    public void setInOrderCount30DaysSku(long inOrderCount30DaysSku) {
        this.inOrderCount30DaysSku = inOrderCount30DaysSku;
    }
}
