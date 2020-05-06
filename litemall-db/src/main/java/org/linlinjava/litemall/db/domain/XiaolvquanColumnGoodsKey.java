package org.linlinjava.litemall.db.domain;

public class XiaolvquanColumnGoodsKey {
    private Integer columnid;

    private String skuid;

    public Integer getColumnid() {
        return columnid;
    }

    public void setColumnid(Integer columnid) {
        this.columnid = columnid;
    }

    public String getSkuid() {
        return skuid;
    }

    public void setSkuid(String skuid) {
        this.skuid = skuid == null ? null : skuid.trim();
    }
}