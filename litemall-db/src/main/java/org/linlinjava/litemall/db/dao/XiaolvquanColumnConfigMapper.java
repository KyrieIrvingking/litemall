package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.XiaolvquanColumnConfig;

import java.util.List;

public interface XiaolvquanColumnConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(XiaolvquanColumnConfig record);

    int insertSelective(XiaolvquanColumnConfig record);

    XiaolvquanColumnConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XiaolvquanColumnConfig record);

    int updateByPrimaryKey(XiaolvquanColumnConfig record);

    List<XiaolvquanColumnConfig> selectByColType(@Param("colType") Integer colType);
}