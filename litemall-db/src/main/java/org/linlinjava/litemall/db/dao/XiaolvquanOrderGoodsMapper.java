package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.XiaolvquanOrderGoods;

import java.util.List;

public interface XiaolvquanOrderGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(XiaolvquanOrderGoods record);

    int insertSelective(XiaolvquanOrderGoods record);

    XiaolvquanOrderGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XiaolvquanOrderGoods record);

    int updateByPrimaryKey(XiaolvquanOrderGoods record);

    List<String> selectOrderIdListByUserId(@Param("userId") Integer userId, @Param("startTime") Long startTime, @Param("endTime") Long endTime,
                                            @Param("offset") Integer offset, @Param("pageNum") Integer pageNum, @Param("validCode") Integer[] validCode,
                                            @Param("buyUserId") Integer buyUserId);
}