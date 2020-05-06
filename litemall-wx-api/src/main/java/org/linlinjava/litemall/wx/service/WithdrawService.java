package org.linlinjava.litemall.wx.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.domain.XiaolvquanUserAccount;
import org.linlinjava.litemall.db.domain.XiaolvquanWithdrawHistory;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.service.XiaolvquanUserAccountService;
import org.linlinjava.litemall.db.service.XiaolvquanWithdrawHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: lichunyang
 * @date: Created in 16:50 2019/6/11
 * @Description:
 */
@Service
public class WithdrawService {

    private final Log logger = LogFactory.getLog(WithdrawService.class);
    @Autowired
    private XiaolvquanWithdrawHistoryService withdrawHistoryService;
    @Autowired
    private LitemallUserService userService;
    @Autowired
    private XiaolvquanUserAccountService accountService;

    /**
     * 申请提现
     * @param userId
     * @return
     */
    public Object applyCrash(Integer userId,XiaolvquanWithdrawHistory withdrawHistory){

        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        LitemallUser user = this.userService.findById(userId);
        if (user==null){
            return ResponseUtil.unlogin();
        }
        logger.info("申请提现");
        logger.info("userInfo："+user.toString());
        //判断用户账户信息是否完整
        if (StringUtils.isBlank(withdrawHistory.getAccountNo())||
                StringUtils.isBlank(withdrawHistory.getRealName())){
            return ResponseUtil.fail(400,"请核对账户信息是否完整！");
        }
        //账户信息逻辑处理
        XiaolvquanUserAccount account = this.accountService.selectByUserId(userId,0);
        if (account==null){
            //新增
            account = new XiaolvquanUserAccount();
            account.setUserId(userId);
            account.setAccountNo(withdrawHistory.getAccountNo());
            account.setRealName(withdrawHistory.getRealName());
            accountService.add(account);
        }else {
            //更新
            account.setUserId(userId);
            account.setAccountNo(withdrawHistory.getAccountNo());
            account.setRealName(withdrawHistory.getRealName());
            accountService.update(account);
        }
        //判断提现余额是否正常
        if (user.getBanlance()==null|| withdrawHistory.getCash()>user.getBanlance()){
            return ResponseUtil.fail(400,"可提现金额不足");
        }
        //新增提现记录
        XiaolvquanWithdrawHistory xiaolvquanWithdrawHistory = new XiaolvquanWithdrawHistory();
        xiaolvquanWithdrawHistory.setUserId(userId);
        xiaolvquanWithdrawHistory.setAccountNo(withdrawHistory.getAccountNo());
        xiaolvquanWithdrawHistory.setRealName(withdrawHistory.getRealName());
        xiaolvquanWithdrawHistory.setCurrrentBalance(user.getBanlance());
        xiaolvquanWithdrawHistory.setStatus(1);//1代表申请中
        xiaolvquanWithdrawHistory.setCash(withdrawHistory.getCash());
        try {
            withdrawHistoryService.add(xiaolvquanWithdrawHistory);
            //扣除提现余额
            user.setBanlance(user.getBanlance()-withdrawHistory.getCash());
            userService.updateById(user);
        }catch (Exception e){
            logger.fatal("提现申请失败！"+e.getMessage());
            return ResponseUtil.fail(500,"提现申请失败，请联系管理员！");
        }
        return ResponseUtil.ok();
    }

    /**
     * 提现历史列表
     * @param userId
     * @return
     */
    public Object crashHistoryList(Integer userId,Integer status,Integer pageIndex,Integer pageNum){
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        LitemallUser user = this.userService.findById(userId);
        if (user==null){
            return ResponseUtil.unlogin();
        }
        logger.info("提现历史查询");
        logger.info("userInfo："+user.toString());
        return ResponseUtil.ok(this.withdrawHistoryService.withdrawHistoryListByUserId(userId,status,pageIndex,pageNum));
    }
}
