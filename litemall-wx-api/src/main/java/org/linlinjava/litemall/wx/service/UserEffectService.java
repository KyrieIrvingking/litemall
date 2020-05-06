package org.linlinjava.litemall.wx.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.DateUtils;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.service.XiaolvquanEffectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

/**
 * @author: lichunyang
 * @date: Created in 16:50 2019/6/11
 * @Description:
 */
@Service
public class UserEffectService {

    private final Log logger = LogFactory.getLog(UserEffectService.class);
    @Autowired
    private XiaolvquanEffectService effectService;
    @Autowired
    private LitemallUserService userService;

    /**
     * 根据用户id和时间查询推客预估佣金、有效订单数、有效订单金额（sum后）
     * @param userId
     * @return
     */
    public Object effectSum(Integer userId){
        HashMap map = new HashMap();
        //当天的
        String startTime = DateUtils.formatByPattern(new Date(),DateUtils.DATE_FORMAT_STD);
        String endTime = DateUtils.formatByPattern(new Date(),DateUtils.DATE_FORMAT_STD);
        map.put("today",this.effectService.selectSumByByUserId(userId,startTime,endTime));
        logger.info(map);

        //近7天
        endTime = DateUtils.formatByPattern(new Date(),DateUtils.DATE_FORMAT_STD);
        startTime = DateUtils.formatByPattern( DateUtils.getPastXDateTime(new Date(),6),DateUtils.DATE_FORMAT_STD);
        map.put("week",this.effectService.selectSumByByUserId(userId,startTime,endTime));
        logger.info(map);

        //近30天
        endTime = DateUtils.formatByPattern(new Date(),DateUtils.DATE_FORMAT_STD);
        startTime = DateUtils.formatByPattern( DateUtils.getPastXDateTime(new Date(),29),DateUtils.DATE_FORMAT_STD);
        map.put("month",this.effectService.selectSumByByUserId(userId,startTime,endTime));
        logger.info(map);
        return map;
    }

    /**
     * 根据用户id和时间查询推客预估佣金、有效订单数、有效订单金额（list）
     * @param userId
     * @return
     */
    public Object effectList(Integer userId,String startTime,String endTime,Integer pageIndex,Integer pageNum){

        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        LitemallUser user = this.userService.findById(userId);
        if (user==null){
            return ResponseUtil.unlogin();
        }
        logger.info("用户订单效果数据查询");
        logger.info("userInfo："+user.toString());
        return ResponseUtil.ok(this.effectService.selectListByByUserId(userId,startTime,endTime,pageIndex,pageNum));
    }

}
