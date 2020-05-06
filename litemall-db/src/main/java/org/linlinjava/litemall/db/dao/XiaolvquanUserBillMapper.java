package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.XiaolvquanUserBill;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface XiaolvquanUserBillMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(XiaolvquanUserBill record);

    int insertSelective(XiaolvquanUserBill record);

    XiaolvquanUserBill selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XiaolvquanUserBill record);

    int updateByPrimaryKey(XiaolvquanUserBill record);

    List<XiaolvquanUserBill> selectByUserId(@Param("userId") Integer userId,@Param("status") Integer status,@Param("month") String month,@Param("offset") Integer offset, @Param("pageNum") Integer pageNum);

    XiaolvquanUserBill selectSumByUserId(@Param("userId") Integer userId,@Param("status") Integer status,@Param("month") String month);

}