package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.XiaolvquanUserAccount;

public interface XiaolvquanUserAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(XiaolvquanUserAccount record);

    int insertSelective(XiaolvquanUserAccount record);

    XiaolvquanUserAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XiaolvquanUserAccount record);

    int updateByPrimaryKey(XiaolvquanUserAccount record);

    XiaolvquanUserAccount selectByUserId(@Param("userId") Integer userId,@Param("status") Integer status);
}