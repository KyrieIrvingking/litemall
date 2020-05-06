package org.linlinjava.litemall.db.dao;

import org.linlinjava.litemall.db.domain.XiaolvquanBanner;

public interface XiaolvquanBannerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(XiaolvquanBanner record);

    int insertSelective(XiaolvquanBanner record);

    XiaolvquanBanner selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XiaolvquanBanner record);

    int updateByPrimaryKeyWithBLOBs(XiaolvquanBanner record);

    int updateByPrimaryKey(XiaolvquanBanner record);
}