package org.linlinjava.litemall.db.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class XiaolvquanOrder implements Serializable {
    private Integer id;

    private String orderid;

    private String parentid;

    private Integer orderemt;

    private String paymonth;

    private Integer plus;

    private Integer popid;

    private String unionid;

    private String ext1;

    private Integer validcode;

    private String ordertime;

    private Long finishtime;

    private Date updatetime;

    private List<XiaolvquanOrderGoods> goodsList;

    public List<XiaolvquanOrderGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<XiaolvquanOrderGoods> goodsList) {
        this.goodsList = goodsList;
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

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public Integer getOrderemt() {
        return orderemt;
    }

    public void setOrderemt(Integer orderemt) {
        this.orderemt = orderemt;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getPaymonth() {
        return paymonth;
    }

    public void setPaymonth(String paymonth) {
        this.paymonth = paymonth == null ? null : paymonth.trim();
    }

    public Integer getPlus() {
        return plus;
    }

    public void setPlus(Integer plus) {
        this.plus = plus;
    }

    public Integer getPopid() {
        return popid;
    }

    public void setPopid(Integer popid) {
        this.popid = popid;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1 == null ? null : ext1.trim();
    }

    public Integer getValidcode() {
        return validcode;
    }

    public void setValidcode(Integer validcode) {
        this.validcode = validcode;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public Long getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(Long finishtime) {
        this.finishtime = finishtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}