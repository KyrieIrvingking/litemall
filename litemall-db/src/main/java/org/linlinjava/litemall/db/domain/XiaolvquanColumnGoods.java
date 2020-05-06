package org.linlinjava.litemall.db.domain;

public class XiaolvquanColumnGoods extends XiaolvquanColumnGoodsKey {
    private Integer id;

    private String couponlink;

    private Integer skusource;

    private Integer position;

    private Integer isvalid;

    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCouponlink() {
        return couponlink;
    }

    public void setCouponlink(String couponlink) {
        this.couponlink = couponlink == null ? null : couponlink.trim();
    }

    public Integer getSkusource() {
        return skusource;
    }

    public void setSkusource(Integer skusource) {
        this.skusource = skusource;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(Integer isvalid) {
        this.isvalid = isvalid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }
}