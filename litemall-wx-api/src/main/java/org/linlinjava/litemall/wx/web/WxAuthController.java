package org.linlinjava.litemall.wx.web;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.notify.YixinSmsSend;
import org.linlinjava.litemall.core.util.*;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.dto.UserInfo;
import org.linlinjava.litemall.wx.dto.WxLoginInfo;
import org.linlinjava.litemall.wx.service.CaptchaCodeManager;
import org.linlinjava.litemall.wx.service.UserTokenManager;
import org.linlinjava.litemall.wx.util.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.linlinjava.litemall.wx.util.WxResponseCode.AUTH_CAPTCHA_FREQUENCY;

/**
 * 鉴权服务
 */
@RestController
@RequestMapping("/wx/auth")
@Validated
public class WxAuthController {
    private final Log logger = LogFactory.getLog(WxAuthController.class);

    @Autowired
    private LitemallUserService userService;

    @Autowired
    private WxMaService wxService;

    @Autowired
    private YixinSmsSend yixinSmsSend;

    /**
     * 微信登录
     *
     * @param wxLoginInfo 请求内容，{ code: xxx, userInfo: xxx }
     * @param request     请求对象
     * @return 登录结果
     */
    @PostMapping("login_by_weixin")
    public Object loginByWeixin(@RequestBody WxLoginInfo wxLoginInfo, HttpServletRequest request) {
        String code = wxLoginInfo.getCode();
        String encDataStr = wxLoginInfo.getEncryptedData();
        String iv = wxLoginInfo.getIv();
        UserInfo userInfo = wxLoginInfo.getUserInfo();
        if (code == null || userInfo == null) {
            return ResponseUtil.badArgument();
        }

        //获取用户信息
        String sessionKey = null;
        String openId = null;
        try {
            WxMaJscode2SessionResult result = this.wxService.getUserService().getSessionInfo(code);
            sessionKey = result.getSessionKey();
            openId = result.getOpenid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sessionKey == null || openId == null) {
            return ResponseUtil.fail();
        }

        //获取手机号
        String phoneNumber = null;
        if (!StringUtils.isEmpty(encDataStr)&&!StringUtils.isEmpty(iv)){
            try {
                WxMaPhoneNumberInfo wxMaPhoneNumberInfo = this.wxService.getUserService().getPhoneNoInfo(sessionKey,encDataStr,iv);
                phoneNumber = wxMaPhoneNumberInfo.getPhoneNumber();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LitemallUser user = userService.queryByOid(openId);
        if (user == null) {
            user = new LitemallUser();
            user.setUsername(openId);
            user.setPassword(openId);
            user.setWeixinOpenid(openId);
            user.setAvatar(userInfo.getAvatarUrl());
            user.setNickname(userInfo.getNickName());
            user.setGender(userInfo.getGender());
            user.setUserLevel(1);
            user.setStatus((byte) 0);
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            user.setSessionKey(sessionKey);
            user.setMobile(phoneNumber);

            //用户直接上级userId判断逻辑
            if (userInfo.getParentUserId()!=null&&userInfo.getParentUserId()!=0){
                LitemallUser parentUser = this.userService.findById(userInfo.getParentUserId());
                if (parentUser!=null){
                    user.setParentUserId(userInfo.getParentUserId());
                }
            }
            //parentToken
            if (userInfo.getParentUserToken()!=null&&!userInfo.getParentUserToken().equals("")){
                Integer parentUserId = UserTokenManager.getUserId(userInfo.getParentUserToken());
                LitemallUser parentUser = this.userService.findById(parentUserId);
                if (parentUser!=null){
                    user.setParentUserId(parentUserId);
                }
            }
            userService.add(user);
        } else {
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            user.setSessionKey(sessionKey);
            user.setNickname(userInfo.getNickName());
            if (!StringUtils.isEmpty(phoneNumber)){
                user.setMobile(phoneNumber);
            }
            if (userService.updateById(user) == 0) {
                return ResponseUtil.updatedDataFailed();
            }
        }

        // token
        JwtHelper jwtHelper = new JwtHelper();
        Date expireDate = jwtHelper.getAfterDate(new Date(),1,0,0,0,0,0);
        String token = UserTokenManager.generateToken(user.getId(),expireDate);

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("phoneNumber", user.getMobile()==null?"":user.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        result.put("token", token);
        result.put("userInfo", userInfo);
        result.put("userLevel", user.getUserLevel());
        return ResponseUtil.ok(result);
    }

    /**
     * 请求注册验证码
     *
     * @param body 手机号码 { mobile }
     * @return
     */
    @PostMapping("regCaptcha")
    public Object registerCaptcha(@RequestBody String body) {
        String phoneNumber = JacksonUtil.parseString(body, "mobile");
        if (StringUtils.isEmpty(phoneNumber)) {
            return ResponseUtil.badArgument();
        }
        if (!RegexUtil.isMobileExact(phoneNumber)) {
            return ResponseUtil.badArgumentValue();
        }
        try {
            String code = CharUtil.getRandomNum(6);
            yixinSmsSend.SMS(phoneNumber, code);

            boolean successful = CaptchaCodeManager.addToCache(phoneNumber, code);
            if (!successful) {
                return ResponseUtil.fail(AUTH_CAPTCHA_FREQUENCY, "验证码未超时2分钟，不能发送");
            }
        }catch (Exception e){
            return ResponseUtil.fail();
        }

        return ResponseUtil.ok();
    }

    /**
     * 用户名绑定手机号
     * @param body
     * @return
     */
    @PostMapping("bindPhone")
    public Object bindPhone(@LoginUser Integer userId,@RequestBody String body) {
        String phoneNumber = JacksonUtil.parseString(body, "mobile");
        String code = JacksonUtil.parseString(body, "code");
        if (StringUtils.isEmpty(phoneNumber)) {
            return ResponseUtil.badArgument();
        }
        if (StringUtils.isEmpty(code)) {
            return ResponseUtil.badArgument();
        }
        if (!RegexUtil.isMobileExact(phoneNumber)) {
            return ResponseUtil.badArgumentValue();
        }
        LitemallUser user = userService.findById(userId);
        if (user == null){
            return ResponseUtil.badArgument();
        }

        String verifyCode = CaptchaCodeManager.getCachedCaptcha(phoneNumber);
        if (!code.equals(verifyCode)) {
            return ResponseUtil.fail(AUTH_CAPTCHA_FREQUENCY, "验证码错误");
        }
        user.setMobile(phoneNumber);
        userService.updateById(user);

        return ResponseUtil.ok();
    }



    //################暂不使用#####################

    @PostMapping("logout")
    public Object logout(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        return ResponseUtil.ok();
    }

    @GetMapping("info")
    public Object info(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        LitemallUser user = userService.findById(userId);
        Map<Object, Object> data = new HashMap<Object, Object>();
        data.put("nickName", user.getNickname());
        data.put("avatar", user.getAvatar());
        data.put("gender", user.getGender());
        data.put("mobile", user.getMobile());

        return ResponseUtil.ok(data);
    }
}
