package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.XiaolvquanWithdrawHistory;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.service.BannerService;
import org.linlinjava.litemall.wx.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

/**
 * banner 配置
 */
@RestController
@RequestMapping("/wx/banner")
@Validated
public class WxBannerController {
    private final Log logger = LogFactory.getLog(WxBannerController.class);

    @Autowired
    private BannerService bannerService;


    /**
     * 获取banner内容
     * @param userId
     * @param Id
     * @return Object
     */
    @GetMapping("content/get")
    public Object getContentAndTranslink(@LoginUser Integer userId,
                                         @NotNull Integer Id){
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        return this.bannerService.getContentAndTranslink(Id,userId);
    }

}