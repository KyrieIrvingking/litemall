package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.XiaolvquanOrderGoodsMapper;
import org.linlinjava.litemall.db.dao.XiaolvquanOrderMapper;
import org.linlinjava.litemall.db.domain.XiaolvquanOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: lichunyang
 * @date: Created in 15:21 2019/6/11
 * @Description:
 */
@Service
public class XiaolvquanOrderService {

    @Resource
    private XiaolvquanOrderMapper xiaolvquanOrderMapper;
    @Resource
    private XiaolvquanOrderGoodsMapper xiaolvquanOrderGoodsMapper;


    /**
     * 根据推客id、实时查询当日订单和商品信息列表
     * @param userId
     * @return
     */
    public List<XiaolvquanOrder> selectTodayOrderByUserId(Integer userId,Integer pageIndex,Integer pageNum,Integer[] validCode,Integer bugUserId){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //当天0点
        Long startTime = calendar.getTimeInMillis();
        //当前时间
        calendar.setTime(new Date());
        Long endTime = calendar.getTimeInMillis();
        return this.selectCommonHistoryOrder(userId,startTime,endTime,(pageIndex-1)*pageNum,pageNum,validCode,bugUserId);
    }

    /**
     * 根据推客id、查询当日订单和商品信息历史列表
     * @param userId
     * @param pageIndex
     * @param pageNum
     * @return
     */
    public List<XiaolvquanOrder> selectHistoryOrderByUserId(Integer userId,Integer pageIndex,Integer pageNum,Integer[] validCode,Integer bugUserId){
        return this.selectCommonHistoryOrder(userId,null,null,(pageIndex-1)*pageNum,pageNum,validCode,bugUserId);
    }

    public List<XiaolvquanOrder> selectCommonHistoryOrder(Integer userId,Long startTime,Long endTime,Integer offset,Integer pageNum,Integer[] validCode,Integer bugUserId){
        List<String> orderIdList =  this.xiaolvquanOrderGoodsMapper.selectOrderIdListByUserId(userId,startTime,endTime,offset,pageNum,validCode,bugUserId);
        if (orderIdList==null||orderIdList.size()==0){
            return new ArrayList<>();
        }
        return this.xiaolvquanOrderMapper.selectOrderByOrderIdList(orderIdList);
    }

}
