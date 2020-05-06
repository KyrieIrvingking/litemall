package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.domain.XiaolvquanUserAccount;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.service.UserAccountService;
import org.linlinjava.litemall.wx.service.UserEffectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户账户信息controller
 */
@RestController
@RequestMapping("/wx/account")
@Validated
public class WxUserAccountController {
    private final Log logger = LogFactory.getLog(WxUserAccountController.class);

    @Autowired
    private UserAccountService accountService;

    /**
     * 根据用户id查询账户信息
     * @param userId
     * @return
     */
    @GetMapping("/info")
    public Object list(@LoginUser Integer userId) {
        return this.accountService.getUserAccount(userId);
    }

    /**
     * 添加用户账户信息
     * @param userId
     * @param userAccount
     * @return
     */
    @PostMapping("/add")
    public Object add(@LoginUser Integer userId, @RequestBody XiaolvquanUserAccount userAccount) {
        return this.accountService.addUserAccount(userId,userAccount);
    }

    /**
     * 更新用户账户信息
     * @param userId
     * @param userAccount
     * @return
     */
    @PostMapping("/update")
    public Object update(@LoginUser Integer userId, @RequestBody XiaolvquanUserAccount userAccount) {
        return this.accountService.updateUserAccount(userId,userAccount);
    }

}