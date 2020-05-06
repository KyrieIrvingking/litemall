package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.XiaolvquanWithdrawHistoryMapper;
import org.linlinjava.litemall.db.domain.XiaolvquanWithdrawHistory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author: lichunyang
 * @date: Created in 15:12 2019/6/11
 * @Description:
 */
@Service
public class XiaolvquanWithdrawHistoryService {

    @Resource
    private XiaolvquanWithdrawHistoryMapper withdrawHistoryMapper;

    /**
     * 根据用户id查询提现历史列表
     * @param userId
     * @return
     */
    public List<XiaolvquanWithdrawHistory> withdrawHistoryListByUserId(int userId,int status,int pageIndex,int pageNum){
        return this.withdrawHistoryMapper.selectByUserId(userId,status,(pageIndex-1)*pageNum,pageNum);
    }

    /**
     * 计算累积提现金额
     * @param userId
     * @param status
     * @return
     */
    public Integer calCumulativeSettlementAmount(int userId,int status){
        return this.withdrawHistoryMapper.calCumulativeSettlementAmount(userId,status);
    }

    /**
     * 根据id查询提现记录
     * @param id
     * @return
     */
    public XiaolvquanWithdrawHistory withdrawHistoryListById(int id){
        return this.withdrawHistoryMapper.selectByPrimaryKey(id);
    }

    /**
     * 提现申请插入
     * @param withdrawHistory
     */
    public void add(XiaolvquanWithdrawHistory withdrawHistory){
        withdrawHistory.setApplyTime(new Date());
        this.withdrawHistoryMapper.insert(withdrawHistory);
    }

}
