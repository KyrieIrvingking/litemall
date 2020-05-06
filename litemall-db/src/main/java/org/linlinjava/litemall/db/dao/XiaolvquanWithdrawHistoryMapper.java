package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.XiaolvquanWithdrawHistory;

import java.util.List;

public interface XiaolvquanWithdrawHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(XiaolvquanWithdrawHistory record);

    int insertSelective(XiaolvquanWithdrawHistory record);

    XiaolvquanWithdrawHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XiaolvquanWithdrawHistory record);

    int updateByPrimaryKey(XiaolvquanWithdrawHistory record);

    List<XiaolvquanWithdrawHistory> selectByUserId(@Param("userId") Integer userId, @Param("status") Integer status,@Param("offset") Integer offset, @Param("pageNum") Integer pageNum);
    Integer calCumulativeSettlementAmount(@Param("userId") Integer userId,@Param("status") Integer status);
}