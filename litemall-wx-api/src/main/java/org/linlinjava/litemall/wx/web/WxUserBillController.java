package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.service.UserBillService;
import org.linlinjava.litemall.wx.service.UserEffectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户订单效果数据controller
 */
@RestController
@RequestMapping("/wx/bill")
@Validated
public class WxUserBillController {
    private final Log logger = LogFactory.getLog(WxUserBillController.class);

    @Autowired
    private UserBillService userBillService;

    /**
     * 查询账单列表
     * status 1表示待结算  2表示已结算
     * @param userId
     * @return
     */
    @GetMapping("/list")
    public Object list(@LoginUser Integer userId,
                       @RequestParam(value = "status",required = false) Integer status,
                       @RequestParam(value = "month",required = false) String month,
                       @RequestParam(required = false,defaultValue = "1") Integer pageIndex,
                       @RequestParam(required = false,defaultValue = "10") Integer pageNum) {
        return this.userBillService.userBillList(userId,status,month,pageIndex,pageNum);
    }

}