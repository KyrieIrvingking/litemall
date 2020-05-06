package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.domain.XiaolvquanWithdrawHistory;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务
 */
@RestController
@RequestMapping("/wx/crash")
@Validated
public class WxWithdrawController {
    private final Log logger = LogFactory.getLog(WxWithdrawController.class);

    @Autowired
    private WithdrawService crashService;

    /**
     * 提现申请接口
     * @param userId
     * @return
     */
    @PostMapping("apply")
    public Object crash(@LoginUser Integer userId,@RequestBody XiaolvquanWithdrawHistory withdrawHistory) {
        //写入提现记录，成功表示已经申请
        return this.crashService.applyCrash(userId,withdrawHistory);
    }

    /**
     * 提现记录列表接口，默认查询所有状态
     * @param userId
     * @return
     */
    @GetMapping("list")
    public Object crash(@LoginUser Integer userId,
                        @RequestParam(value = "status",defaultValue = "0") Integer status,
                        @RequestParam(required = false,defaultValue = "1") Integer pageIndex,
                        @RequestParam(required = false,defaultValue = "10") Integer pageNum) {
        //提现记录列表
        return this.crashService.crashHistoryList(userId,status,pageIndex,pageNum);
    }

}