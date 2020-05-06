package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.XiaolvquanColumnGoods;
import org.linlinjava.litemall.db.domain.XiaolvquanColumnGoodsKey;

import java.util.List;

public interface XiaolvquanColumnGoodsMapper {
    int deleteByPrimaryKey(XiaolvquanColumnGoodsKey key);

    int insert(XiaolvquanColumnGoods record);

    int insertSelective(XiaolvquanColumnGoods record);

    XiaolvquanColumnGoods selectByPrimaryKey(XiaolvquanColumnGoodsKey key);

    int updateByPrimaryKeySelective(XiaolvquanColumnGoods record);

    int updateByPrimaryKeyWithBLOBs(XiaolvquanColumnGoods record);

    int updateByPrimaryKey(XiaolvquanColumnGoods record);

    XiaolvquanColumnGoods selectById(Integer id);
    List<XiaolvquanColumnGoods> selectByColumnId(@Param("columnId") Integer columnId);
}