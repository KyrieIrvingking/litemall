package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.wx.annotation.LoginUser;
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
@RequestMapping("/wx/effect")
@Validated
public class WxUserEffectController {
    private final Log logger = LogFactory.getLog(WxUserEffectController.class);

    @Autowired
    private UserEffectService effectService;

    /**
     * 查询推客预估佣金、有效订单数、有效订单金额 列表
     * @param userId
     * @return
     */
    @GetMapping("list")
    public Object list(@LoginUser Integer userId,
                       @RequestParam(value = "startTime") String startTime,
                       @RequestParam(value = "endTime") String endTime,
                       @RequestParam(required = false,defaultValue = "1") Integer pageIndex,
                       @RequestParam(required = false,defaultValue = "10") Integer pageNum) {
        return this.effectService.effectList(userId,startTime,endTime,pageIndex,pageNum);
    }

}