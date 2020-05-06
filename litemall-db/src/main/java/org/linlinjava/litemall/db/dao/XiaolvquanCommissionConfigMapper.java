package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.XiaolvquanCommissionConfig;

public interface XiaolvquanCommissionConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(XiaolvquanCommissionConfig record);

    int insertSelective(XiaolvquanCommissionConfig record);

    XiaolvquanCommissionConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XiaolvquanCommissionConfig record);

    int updateByPrimaryKey(XiaolvquanCommissionConfig record);

    XiaolvquanCommissionConfig selectByUserLevel(@Param("userLevel") Integer userLevel);
}