package org.linlinjava.litemall.db.dao;

import org.linlinjava.litemall.db.domain.XiaolvquanCategory;

import java.util.HashMap;
import java.util.List;

public interface XiaolvquanCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(XiaolvquanCategory record);

    int insertSelective(XiaolvquanCategory record);

    XiaolvquanCategory selectByPrimaryKey(Integer id);

    List<XiaolvquanCategory> selectByQuery(HashMap map);

    int updateByPrimaryKeySelective(XiaolvquanCategory record);

    int updateByPrimaryKey(XiaolvquanCategory record);
}