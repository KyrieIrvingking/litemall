package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.XiaolvquanUserEffect;

import java.util.List;

public interface XiaolvquanUserEffectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(XiaolvquanUserEffect record);

    int insertSelective(XiaolvquanUserEffect record);

    XiaolvquanUserEffect selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XiaolvquanUserEffect record);

    int updateByPrimaryKey(XiaolvquanUserEffect record);

    XiaolvquanUserEffect selectSumByByUserId(@Param("userId") Integer userId, @Param("startTime") String startTime, @Param("endTime") String endTime);
    List<XiaolvquanUserEffect> selectListByByUserId(@Param("userId") Integer userId, @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("offset") Integer offset, @Param("pageNum") Integer pageNum);

}