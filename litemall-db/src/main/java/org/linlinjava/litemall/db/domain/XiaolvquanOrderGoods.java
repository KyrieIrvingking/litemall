package org.linlinjava.litemall.db.domain;

import java.io.Serializable;
import java.util.Date;

public class XiaolvquanOrderGoods implements Serializable {
    private Integer id;

    private String orderid;

    private String skuid;

    private String skuname;

    private Integer cid1;

    private Integer cid2;

    private Integer cid3;

    private Integer skunum;

    private Integer skureturnnum;

    private Integer frozenskunum;

    private Double price;

    private Double actualfee;

    private Double actualcosprice;

    private Double estimatecosprice;

    private Double estimatefee;

    private String estimatefeeStr;

    private Float commissionrate;

    private Float finalrate;

    private String ext1;

    private String paymonth;

    private String pid;

    private Integer popid;

    private Integer positionid;

    private String siteid;

    private Float subsiderate;

    private String subunionid;

    private Float subsidyrate;

    private Integer tracetype;

    private String unionalias;

    private String uniontag;

    private Integer uniontrafficgroup;

    private Integer validcode;

    private String userid;

    private Integer xiaolvquancommissionrate;

    private Date updatetime;

    private Object imageInfo;

    private Integer buyUserId;//购买用户id

    private Integer isRebate;//是否返利

    private String rebate;//返利金额

    private Integer orderUserLevel;//用户下单时的等级

    public Integer getOrderUserLevel() {
        return orderUserLevel;
    }

    public void setOrderUserLevel(Integer orderUserLevel) {
        this.orderUserLevel = orderUserLevel;
    }

    public String getRebate() {
        return rebate;
    }

    public void setRebate(String rebate) {
        this.rebate = rebate;
    }

    public Integer getBuyUserId() {
        return buyUserId;
    }

    public void setBuyUserId(Integer buyUserId) {
        this.buyUserId = buyUserId;
    }

    public Integer getIsRebate() {
        return isRebate;
    }

    public void setIsRebate(Integer isRebate) {
        this.isRebate = isRebate;
    }

    public String getEstimatefeeStr() {
        return estimatefeeStr;
    }

    public void setEstimatefeeStr(String estimatefeeStr) {
        this.estimatefeeStr = estimatefeeStr;
    }

    public Object getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(Object imageInfo) {
        this.imageInfo = imageInfo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getSkuid() {
        return skuid;
    }

    public void setSkuid(String skuid) {
        this.skuid = skuid;
    }

    public String getSkuname() {
        return skuname;
    }

    public void setSkuname(String skuname) {
        this.skuname = skuname;
    }

    public Integer getCid1() {
        return cid1;
    }

    public void setCid1(Integer cid1) {
        this.cid1 = cid1;
    }

    public Integer getCid2() {
        return cid2;
    }

    public void setCid2(Integer cid2) {
        this.cid2 = cid2;
    }

    public Integer getCid3() {
        return cid3;
    }

    public void setCid3(Integer cid3) {
        this.cid3 = cid3;
    }

    public Integer getSkunum() {
        return skunum;
    }

    public void setSkunum(Integer skunum) {
        this.skunum = skunum;
    }

    public Integer getSkureturnnum() {
        return skureturnnum;
    }

    public void setSkureturnnum(Integer skureturnnum) {
        this.skureturnnum = skureturnnum;
    }

    public Integer getFrozenskunum() {
        return frozenskunum;
    }

    public void setFrozenskunum(Integer frozenskunum) {
        this.frozenskunum = frozenskunum;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getActualfee() {
        return actualfee;
    }

    public void setActualfee(Double actualfee) {
        this.actualfee = actualfee;
    }

    public Double getActualcosprice() {
        return actualcosprice;
    }

    public void setActualcosprice(Double actualcosprice) {
        this.actualcosprice = actualcosprice;
    }

    public Double getEstimatecosprice() {
        return estimatecosprice;
    }

    public void setEstimatecosprice(Double estimatecosprice) {
        this.estimatecosprice = estimatecosprice;
    }

    public Double getEstimatefee() {
        return estimatefee;
    }

    public void setEstimatefee(Double estimatefee) {
        this.estimatefee = estimatefee;
    }

    public Float getCommissionrate() {
        return commissionrate;
    }

    public void setCommissionrate(Float commissionrate) {
        this.commissionrate = commissionrate;
    }

    public Float getFinalrate() {
        return finalrate;
    }

    public void setFinalrate(Float finalrate) {
        this.finalrate = finalrate;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getPaymonth() {
        return paymonth;
    }

    public void setPaymonth(String paymonth) {
        this.paymonth = paymonth;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getPopid() {
        return popid;
    }

    public void setPopid(Integer popid) {
        this.popid = popid;
    }

    public Integer getPositionid() {
        return positionid;
    }

    public void setPositionid(Integer positionid) {
        this.positionid = positionid;
    }

    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }

    public Float getSubsiderate() {
        return subsiderate;
    }

    public void setSubsiderate(Float subsiderate) {
        this.subsiderate = subsiderate;
    }

    public String getSubunionid() {
        return subunionid;
    }

    public void setSubunionid(String subunionid) {
        this.subunionid = subunionid;
    }

    public Float getSubsidyrate() {
        return subsidyrate;
    }

    public void setSubsidyrate(Float subsidyrate) {
        this.subsidyrate = subsidyrate;
    }

    public Integer getTracetype() {
        return tracetype;
    }

    public void setTracetype(Integer tracetype) {
        this.tracetype = tracetype;
    }

    public String getUnionalias() {
        return unionalias;
    }

    public void setUnionalias(String unionalias) {
        this.unionalias = unionalias;
    }

    public String getUniontag() {
        return uniontag;
    }

    public void setUniontag(String uniontag) {
        this.uniontag = uniontag;
    }

    public Integer getUniontrafficgroup() {
        return uniontrafficgroup;
    }

    public void setUniontrafficgroup(Integer uniontrafficgroup) {
        this.uniontrafficgroup = uniontrafficgroup;
    }

    public Integer getValidcode() {
        return validcode;
    }

    public void setValidcode(Integer validcode) {
        this.validcode = validcode;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getXiaolvquancommissionrate() {
        return xiaolvquancommissionrate;
    }

    public void setXiaolvquancommissionrate(Integer xiaolvquancommissionrate) {
        this.xiaolvquancommissionrate = xiaolvquancommissionrate;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}