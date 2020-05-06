package org.linlinjava.litemall.core.dto;

public class UserQueryReq {

    private Integer[] userLevel;
    private Integer pageIndex = 1;
    private Integer pageNum = 10;

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer[] getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer[] userLevel) {
        this.userLevel = userLevel;
    }
}
