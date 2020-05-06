package org.linlinjava.litemall.db.domain;

import java.util.Date;

public class XiaolvquanBanner {
    private Integer id;

    private String name;

    private String bannerUrl;

    private String detailTop;

    private String detailPic;

    private Byte position;

    private Date addTime;

    private Date updateTime;

    private Boolean deleted;

    private String detailText;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl == null ? null : bannerUrl.trim();
    }

    public String getDetailTop() {
        return detailTop;
    }

    public void setDetailTop(String detailTop) {
        this.detailTop = detailTop == null ? null : detailTop.trim();
    }

    public String getDetailPic() {
        return detailPic;
    }

    public void setDetailPic(String detailPic) {
        this.detailPic = detailPic == null ? null : detailPic.trim();
    }

    public Byte getPosition() {
        return position;
    }

    public void setPosition(Byte position) {
        this.position = position;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDetailText() {
        return detailText;
    }

    public void setDetailText(String detailText) {
        this.detailText = detailText == null ? null : detailText.trim();
    }
}