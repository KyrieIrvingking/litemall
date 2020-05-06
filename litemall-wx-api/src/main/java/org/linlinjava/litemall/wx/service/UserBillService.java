package org.linlinjava.litemall.wx.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.DateUtils;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.domain.XiaolvquanUserBill;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.service.XiaolvquanUserBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * @author: lichunyang
 * @date: Created in 16:50 2019/6/11
 * @Description:
 */
@Service
public class UserBillService {

    private final Log logger = LogFactory.getLog(UserBillService.class);
    @Autowired
    private XiaolvquanUserBillService userBillService;
    @Autowired
    private LitemallUserService userService;
    DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    /**
     * 根据用户id和时间查询推客预估佣金、有效订单数、有效订单金额（list）
     * @param userId
     * @return
     */
    public Object userBillList(Integer userId,Integer status,String month,Integer pageIndex,Integer pageNum){

        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        LitemallUser user = this.userService.findById(userId);
        if (user==null){
            return ResponseUtil.unlogin();
        }
        logger.info("用户账单列表查询");
        logger.info("userInfo："+user.toString());
        return ResponseUtil.ok(this.userBillService.selectByUserId(userId,status,month,pageIndex,pageNum));
    }

    /**
     * 查询本月预估结算和下月预估结算金额
     * @param userId
     * @return
     */
    public HashMap userBillSum(Integer userId){
        HashMap map = new HashMap();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        String currentMonthStr = DateUtils.format(cal.getTime(),"yyyy-MM");
        XiaolvquanUserBill currentMonth = userBillService.selectSumByUserId(userId,0,currentMonthStr);
        if (currentMonth!=null){
            map.put("currentMonth",decimalFormat.format(currentMonth.getPromotionEstimateFee()+currentMonth.getRecommendEstimateFee()+currentMonth.getRecommendEstimateFee()));
        }

        String nextMonthStr = DateUtils.format(new Date(),"yyyy-MM");
        XiaolvquanUserBill nextMonth = userBillService.selectSumByUserId(userId,0,nextMonthStr);
        if (nextMonth!=null){
            map.put("nextMonth",decimalFormat.format(nextMonth.getPromotionEstimateFee()+nextMonth.getRecommendEstimateFee()+nextMonth.getRebateEstimateFee()));
        }
        return map;
    }

}
