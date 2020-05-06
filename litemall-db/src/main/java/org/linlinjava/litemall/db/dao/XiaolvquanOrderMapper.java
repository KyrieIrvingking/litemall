package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.XiaolvquanOrder;

import java.util.List;

public interface XiaolvquanOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(XiaolvquanOrder record);

    int insertSelective(XiaolvquanOrder record);

    XiaolvquanOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XiaolvquanOrder record);

    int updateByPrimaryKey(XiaolvquanOrder record);

    List<XiaolvquanOrder> selectOrderByUserId(@Param("userId") Integer userId, @Param("startTime") Long startTime, @Param("endTime") Long endTime,
                                              @Param("offset") Integer offset, @Param("pageNum") Integer pageNum, @Param("validCode") Integer[] validCode,
                                              @Param("buyUserId") Integer buyUserId);

    List<XiaolvquanOrder> selectOrderByOrderIdList(@Param("orderIds") List<String> orderIds);
}