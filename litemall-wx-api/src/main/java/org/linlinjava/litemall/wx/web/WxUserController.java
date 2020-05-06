package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.dto.UserQueryReq;
import org.linlinjava.litemall.core.qcode.QCodeService;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.service.CommodityOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务
 */
@RestController
@RequestMapping("/wx/user")
@Validated
public class WxUserController {
    private final Log logger = LogFactory.getLog(WxUserController.class);

    @Autowired
    private QCodeService qCodeService;
    @Autowired
    private LitemallUserService userService;
    @Autowired
    private CommodityOrderService commodityOrderService;

    /**
     * 用户首页信息接口
     * @param userId
     * @return
     */
    @GetMapping("index")
    public Object list(@LoginUser Integer userId,
                       @RequestParam(value = "pageIndex",defaultValue = "1") Integer pageIndex,
                       @RequestParam(value = "pageNum",defaultValue = "10") Integer pageNum) {
        return commodityOrderService.userIndex(userId,pageIndex,pageNum);
    }

    /**
     * 根据parentUserId和角色查询直属下级列表
     * @param userId
     * @param userQueryReq
     * @return
     */
    @PostMapping("/underling/list")
    public Object underlingList(@LoginUser Integer userId,
                                @RequestBody UserQueryReq userQueryReq){

        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        logger.info("根据登陆用户和角色查询直属下级列表，userId:"+userId);
        return ResponseUtil.ok(userService.selectUnderlingInfoListByParentIdAndUserLevel(userId,userQueryReq.getUserLevel(),userQueryReq.getPageIndex(),userQueryReq.getPageNum()));
    }

    /**
     * 生成小程序邀请码
     * @return
     */
    @GetMapping("/invite/code")
    public Object wxInviteCode(@LoginUser Integer userId,
                               @RequestParam(required = false,defaultValue = "pages/ucenter/index/index") String pageUrl){
        if (userId==null){
            return ResponseUtil.unlogin();
        }
        String imgUrl = qCodeService.createWxCodeImage(pageUrl,userId.toString());
        return ResponseUtil.ok(imgUrl);
    }

    /**
     * 生成邀请海报
     * @param userId
     * @param pageUrl
     * @return
     */
    @GetMapping("/invite/image")
    public Object wxInviteImage(@LoginUser Integer userId,
                               @RequestParam(required = false,defaultValue = "pages/ucenter/index/index") String pageUrl,
                                @RequestParam String inviteBackLogoName){
        if (userId==null){
            return ResponseUtil.unlogin();
        }
        String imgUrl = qCodeService.createWxInviteImage(pageUrl,userId,inviteBackLogoName);
        return ResponseUtil.ok(imgUrl);
    }
}