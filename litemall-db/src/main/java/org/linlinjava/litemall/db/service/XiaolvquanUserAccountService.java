package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.XiaolvquanUserAccountMapper;
import org.linlinjava.litemall.db.dao.XiaolvquanWithdrawHistoryMapper;
import org.linlinjava.litemall.db.domain.XiaolvquanUserAccount;
import org.linlinjava.litemall.db.domain.XiaolvquanWithdrawHistory;
import org.linlinjava.litemall.db.util.XiaolvquanConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 用户账号信息service
 * @author: lichunyang
 * @date: Created in 15:12 2019/6/11
 * @Description:
 */
@Service
public class XiaolvquanUserAccountService {

    @Resource
    private XiaolvquanUserAccountMapper userAccountMapper;

    /**
     * 根据用户id查询账号信息
     * @param userId
     * @return
     */
    public XiaolvquanUserAccount selectByUserId(Integer userId,Integer status){
        return this.userAccountMapper.selectByUserId(userId,status);
    }

    /**
     * 用户账号信息新增
     * @param userAccount
     */
    public void add(XiaolvquanUserAccount userAccount){
        userAccount.setStatus(XiaolvquanConstant.ACCOUNT_AVAILABLE);
        userAccount.setAddTime(new Date());
        this.userAccountMapper.insert(userAccount);
    }

    /**
     * 更新用户账户信息
     * @param userAccount
     */
    public void update(XiaolvquanUserAccount userAccount){
        userAccount.setUpdateTime(new Date());
        this.userAccountMapper.updateByPrimaryKey(userAccount);
    }

}
