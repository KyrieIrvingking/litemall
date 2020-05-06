package org.linlinjava.litemall.wx.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.domain.XiaolvquanUserAccount;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.service.XiaolvquanUserAccountService;
import org.linlinjava.litemall.db.util.XiaolvquanConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: lichunyang
 * @date: Created in 16:50 2019/6/11
 * @Description:
 */
@Service
public class UserAccountService {

    private final Log logger = LogFactory.getLog(UserAccountService.class);
    @Autowired
    private XiaolvquanUserAccountService accountService;
    @Autowired
    private LitemallUserService userService;

    /**
     * 新增用户账户信息
     * @param userId
     * @param userAccount
     * @return
     */
    public Object addUserAccount(Integer userId,XiaolvquanUserAccount userAccount){
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        LitemallUser user = this.userService.findById(userId);
        if (user==null){
            return ResponseUtil.unlogin();
        }
        logger.info("新增账户信息");
        logger.info("userInfo："+user.toString());
        if (StringUtils.isBlank(userAccount.getAccountNo())||StringUtils.isBlank(userAccount.getRealName())){
            return ResponseUtil.fail(400,"账户信息不完整");
        }
        if (accountService.selectByUserId(userId,XiaolvquanConstant.ACCOUNT_AVAILABLE)!=null){
            return ResponseUtil.fail(400,"存在可使用账户，不允许添加");
        }
        try {
            userAccount.setUserId(userId);
            accountService.add(userAccount);
        }catch (Exception e){
            logger.fatal("新增用户账户信息失败！"+e.getMessage());
            return ResponseUtil.fail(500,"新增用户账户信息失败，请联系管理员！");
        }
        return ResponseUtil.ok();
    }

    /**
     * 更新用户账户信息
     * @param userId
     * @return
     */
    public Object updateUserAccount(Integer userId,XiaolvquanUserAccount userAccount){
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        LitemallUser user = this.userService.findById(userId);
        if (user==null){
            return ResponseUtil.unlogin();
        }
        logger.info("更新账户信息");
        logger.info("userInfo："+user.toString());
        XiaolvquanUserAccount account = accountService.selectByUserId(userId,XiaolvquanConstant.ACCOUNT_AVAILABLE);
        if (account==null){
            return ResponseUtil.fail(404,"账户信息不存在");
        }
        try {
            userAccount.setUserId(userId);
            if (StringUtils.isNotBlank(userAccount.getAccountNo())) account.setAccountNo(userAccount.getAccountNo());
            if (StringUtils.isNotBlank(userAccount.getRealName())) account.setRealName(userAccount.getRealName());
            accountService.update(account);
        }catch (Exception e){
            logger.fatal("更新用户账户信息失败！"+e.getMessage());
            return ResponseUtil.fail(500,"更新用户账户信息失败，请联系管理员！");
        }
        return ResponseUtil.ok();
    }

    /**
     * 根据用户id查询账户信息
     * @param userId
     * @return
     */
    public Object getUserAccount(Integer userId){
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        LitemallUser user = this.userService.findById(userId);
        if (user==null){
            return ResponseUtil.unlogin();
        }
        logger.info("查询账户信息");
        logger.info("userInfo："+user.toString());
        return ResponseUtil.ok(this.accountService.selectByUserId(userId,null));
    }
}
