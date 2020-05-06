package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.XiaolvquanUserBillMapper;
import org.linlinjava.litemall.db.domain.XiaolvquanUserBill;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户账号信息service
 * @author: lichunyang
 * @date: Created in 15:12 2019/6/11
 * @Description:
 */
@Service
public class XiaolvquanUserBillService {

    @Resource
    private XiaolvquanUserBillMapper userBillMapper;

    /**
     * 根据用户id查询账单列表，包括用户实际推荐收入和用户实际推广收入，并且按月区分是否已经结算
     * @param userId
     * @return
     */
    public List<XiaolvquanUserBill> selectByUserId(Integer userId, Integer status, String month,int pageIndex,int pageNum){
        return this.userBillMapper.selectByUserId(userId,status,month,(pageIndex-1)*pageNum,pageNum);
    }

    /**
     * 按月、结算状态、用户id计算用户实际推广收入和用户推荐收入sum
     * @param userId
     * @param status
     * @param month
     * @return
     */
    public XiaolvquanUserBill selectSumByUserId(Integer userId, Integer status, String month){
        return this.userBillMapper.selectSumByUserId(userId,status,month);
    }

}
