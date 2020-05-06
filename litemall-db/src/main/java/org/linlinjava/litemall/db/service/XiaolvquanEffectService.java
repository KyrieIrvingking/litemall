package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.XiaolvquanUserEffectMapper;
import org.linlinjava.litemall.db.domain.XiaolvquanUserEffect;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author: lichunyang
 * @date: Created in 15:21 2019/6/11
 * @Description:
 */
@Service
public class XiaolvquanEffectService {

    @Resource
    private XiaolvquanUserEffectMapper xiaolvquanUserEffectMapper;

    /**
     * 根据用户id和时间查询推客预估佣金、有效订单数、有效订单金额（sum后）
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    public XiaolvquanUserEffect selectSumByByUserId(Integer userId, String startTime, String endTime){
        return this.xiaolvquanUserEffectMapper.selectSumByByUserId(userId,startTime,endTime);
    }

    /**
     * 根据用户id和时间查询推客预估佣金、有效订单数、有效订单金额（list）
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<XiaolvquanUserEffect> selectListByByUserId(Integer userId, String startTime, String endTime,int pageIndex,int pageNum){
        List<XiaolvquanUserEffect> userEffects = this.xiaolvquanUserEffectMapper.selectListByByUserId(userId,startTime,endTime,(pageIndex-1)*pageNum,pageNum);
        SimpleDateFormat preivious = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd");
        try {
            for (XiaolvquanUserEffect effect:userEffects){
                effect.setDay(now.format(preivious.parse(effect.getDay())));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return userEffects;
    }

}
