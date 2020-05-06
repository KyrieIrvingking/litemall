package org.linlinjava.litemall.wx.service;

import jd.union.open.goods.query.request.GoodsReq;
import jd.union.open.goods.query.response.UnionOpenGoodsQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.api.Jd;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.dao.XiaolvquanCommissionConfigMapper;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.domain.XiaolvquanCommissionConfig;
import org.linlinjava.litemall.db.domain.XiaolvquanOrder;
import org.linlinjava.litemall.db.domain.XiaolvquanOrderGoods;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.service.XiaolvquanOrderService;
import org.linlinjava.litemall.db.service.XiaolvquanWithdrawHistoryService;
import org.linlinjava.litemall.db.util.XiaolvquanConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * @author: lichunyang
 * @date: Created in 16:50 2019/6/11
 * @Description:
 */
@Service
public class CommodityOrderService {

    private final Log logger = LogFactory.getLog(CommodityOrderService.class);

    @Autowired
    private XiaolvquanOrderService xiaolvquanOrderService;
    @Autowired
    private LitemallUserService userService;
    @Autowired
    private XiaolvquanWithdrawHistoryService withdrawHistoryService;
    @Autowired
    private UserEffectService effectService;
    @Autowired
    private UserBillService userBillService;
    @Resource
    private XiaolvquanCommissionConfigMapper commissionConfigMapper;
    @Resource
    private Jd jd;
    DecimalFormat df = new DecimalFormat("#0.00");


    /**
     * 查询用户历史订单
     * @param userId
     * @return
     */
    @Cacheable(value = "myCache",keyGenerator = "cacheKeyGenerator")
    public Object orderHistoryList(Integer userId,Integer pageIndex,Integer pageNum,Integer[] validCode){
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        LitemallUser user = this.userService.findById(userId);
        if (user==null){
            return ResponseUtil.unlogin();
        }
        logger.info("查询用户历史订单列表");
        logger.info("userInfo："+user.toString());
        Integer buyUserId = 0;
        if (user.getUserLevel().equals(XiaolvquanConstant.USER_LEVEL_CONSUMER)){
            //如果是普通用户，则只查询用户自己购买过的订单
            buyUserId = userId;
        }
        List<XiaolvquanOrder> xiaolvquanOrders = this.xiaolvquanOrderService.selectHistoryOrderByUserId(userId,pageIndex,pageNum,validCode,buyUserId);

        historyOrderDto(xiaolvquanOrders);
        return ResponseUtil.ok(xiaolvquanOrders);
    }


    /**
     * 查询用户历史订单
     * @param userId
     * @return
     */
    public Object userIndex(Integer userId,Integer pageIndex,Integer pageNum){
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        LitemallUser user = this.userService.findById(userId);
        if (user==null){
            return ResponseUtil.unlogin();
        }
        HashMap map = new HashMap();
        logger.info("用户首页信息查询");
        logger.info("userInfo："+user.toString());
        logger.info("查询用户当日实时订单");
        Integer buyUserId = 0;
        if (user.getUserLevel().equals(XiaolvquanConstant.USER_LEVEL_CONSUMER)){
            //如果是普通用户，则只查询用户自己购买过的订单
            buyUserId = userId;
        }
        List<XiaolvquanOrder> historyOrderList =  this.xiaolvquanOrderService.selectTodayOrderByUserId(userId,pageIndex,pageNum,null,buyUserId);
        historyOrderDto(historyOrderList);
        map.put("historyOrderList",historyOrderList);

        logger.info("查询用户当日效果数据");
        //效果数据(预估佣金收入、预估推荐收益、有效订单量、有效订单金额)
        map.put("effectSummay",this.effectService.effectSum(userId));

        logger.info("查询用户账单、余额、用户角色、粉丝数");
        map.put("bill",this.userBillService.userBillSum(userId));
        map.put("userLevelName",user.getUserLevelName());
        map.put("userLevel",user.getUserLevel());
        map.put("balance",user.getBanlance());//可提现余额
        map.put("fensiNum",user.getFensiNum());//粉丝数
        map.put("phoneNumber",user.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));//手机号

        logger.info("查询直属下级角色和数量");
        map.put("underlingConfig",userService.selectUnderlingByParentId(userId));

        return ResponseUtil.ok(map);
    }

    /**
     * 获取订单商品的image信息，包括佣金比例/返现比例
     * @param orders
     */
    public void historyOrderDto(List<XiaolvquanOrder> orders){
        if (orders==null||orders.size()==0) return;
        for (XiaolvquanOrder order:orders){
            try {
                order.setOrdertime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Double.parseDouble(order.getOrdertime())));
            }catch (Exception e){
                e.printStackTrace();
            }
            for (XiaolvquanOrderGoods orderGood:order.getGoodsList()){
                Long skuId = Long.parseLong(orderGood.getSkuid());
                //调用搜索接口，查询图片信息
                GoodsReq goodsReq = new GoodsReq();
                goodsReq.setSkuIds(new Long[]{skuId});
                UnionOpenGoodsQueryResponse response = jd.searchGoodsByKeyword(goodsReq);
                if (response.getData()!=null && response.getData().length!=0){
                    orderGood.setImageInfo(response.getData()[0].getImageInfo());  //优化为离线
                    //根据下单时的userLevel获取佣金比例、返利比例
                    XiaolvquanCommissionConfig commissionConfig = this.commissionConfigMapper.selectByUserLevel(orderGood.getOrderUserLevel());
                    int promotionRate = commissionConfig.getPromotionRate();
                    int nocouponRebateRate = commissionConfig.getNocouponRebateRate();//无券的总比例
                    int nocouponRebatePromotionRate = commissionConfig.getNocouponRebatePromotionRate();//无券的返利比例
                    int nocouponPromotionRate = commissionConfig.getNocouponPromotionRate();//无券的佣金比例

                    //是否有返利
                    if (orderGood.getIsRebate().equals(XiaolvquanConstant.HAVE_REBATE)) {
                        //计算返利
                        double rebate = orderGood.getEstimatefee()*nocouponRebateRate/100*nocouponRebatePromotionRate/100;
                        orderGood.setRebate(df.format(rebate));
                        //计算预估佣金
                        orderGood.setEstimatefee(orderGood.getEstimatefee()*nocouponRebateRate/100*nocouponPromotionRate/100);
                        orderGood.setEstimatefeeStr(df.format(orderGood.getEstimatefee()));
                    }else{
                        //没有返利
                        //计算预估佣金
                        orderGood.setEstimatefee(orderGood.getEstimatefee()*promotionRate/100);
                        orderGood.setEstimatefeeStr(df.format(orderGood.getEstimatefee()));
                        orderGood.setRebate("0.00");
                    }
                }
            }
        }
    }

}
