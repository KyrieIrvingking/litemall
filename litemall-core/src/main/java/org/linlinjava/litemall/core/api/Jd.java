package org.linlinjava.litemall.core.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import jd.union.open.coupon.query.request.UnionOpenCouponQueryRequest;
import jd.union.open.coupon.query.response.CouponResp;
import jd.union.open.coupon.query.response.UnionOpenCouponQueryResponse;
import jd.union.open.goods.jingfen.query.request.JFGoodsReq;
import jd.union.open.goods.jingfen.query.request.UnionOpenGoodsJingfenQueryRequest;
import jd.union.open.goods.jingfen.query.response.CommissionInfo;
import jd.union.open.goods.jingfen.query.response.JFGoodsResp;
import jd.union.open.goods.jingfen.query.response.UnionOpenGoodsJingfenQueryResponse;
import jd.union.open.goods.query.request.GoodsReq;
import jd.union.open.goods.query.request.UnionOpenGoodsQueryRequest;
import jd.union.open.goods.query.response.GoodsResp;
import jd.union.open.goods.query.response.UnionOpenGoodsQueryResponse;
import jd.union.open.goods.query.response.UrlInfo;
import jd.union.open.goods.seckill.query.request.SecKillGoodsReq;
import jd.union.open.goods.seckill.query.request.UnionOpenGoodsSeckillQueryRequest;
import jd.union.open.goods.seckill.query.response.SecKillGoodsResp;
import jd.union.open.goods.seckill.query.response.UnionOpenGoodsSeckillQueryResponse;
import jd.union.open.promotion.bysubunionid.get.request.PromotionCodeReq;
import jd.union.open.promotion.bysubunionid.get.request.UnionOpenPromotionBysubunionidGetRequest;
import jd.union.open.promotion.bysubunionid.get.response.UnionOpenPromotionBysubunionidGetResponse;
import jd.union.open.promotion.common.get.request.UnionOpenPromotionCommonGetRequest;
import jd.union.open.promotion.common.get.response.UnionOpenPromotionCommonGetResponse;
import org.apache.commons.lang3.StringUtils;
import org.linlinjava.litemall.core.config.JdApiConfig;
import org.linlinjava.litemall.core.util.ObjectTransferUtil;
import org.linlinjava.litemall.db.dao.XiaolvquanCommissionConfigMapper;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.domain.XiaolvquanColumnGoods;
import org.linlinjava.litemall.db.domain.XiaolvquanCommissionConfig;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.XiaolvquanConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class Jd {

    @Autowired
    private JdApiConfig jdApiConfig;
    @Autowired
    private JdCommoditySort jdCommoditySort;
    @Autowired
    private LitemallUserService userService;
    @Resource
    private XiaolvquanCommissionConfigMapper xiaolvquanCommissionConfigMapper;
    DecimalFormat df = new DecimalFormat("#0.00");

    //京东原始接口

    /**
     * 京粉精选商品查询接口
     * @param goodsReq
     * @return
     */
    public UnionOpenGoodsJingfenQueryResponse jingfenGoodsQuery(JFGoodsReq goodsReq){
        HashMap map = new HashMap();
        JdClient client=new DefaultJdClient(jdApiConfig.getServerUrl(),jdApiConfig.getAccessToken(),jdApiConfig.getAppKey(),jdApiConfig.getAppSecret());

        UnionOpenGoodsJingfenQueryRequest request = new UnionOpenGoodsJingfenQueryRequest();
        request.setGoodsReq(goodsReq);
        try {
            UnionOpenGoodsJingfenQueryResponse response = client.execute(request);
            return response;
        }catch (JdException e){
            return null;
        }
    }

    /**
     * 关键词商品查询接口
     * @param goodsReq
     * @return
     */
    public UnionOpenGoodsQueryResponse searchGoodsByKeyword(GoodsReq goodsReq){
        JdClient client=new DefaultJdClient(jdApiConfig.getServerUrl(),jdApiConfig.getAccessToken(),jdApiConfig.getAppKey(),jdApiConfig.getAppSecret());

        UnionOpenGoodsQueryRequest request = new UnionOpenGoodsQueryRequest();
        request.setGoodsReqDTO(goodsReq);
        try {
            UnionOpenGoodsQueryResponse response = client.execute(request);
            return response;
        }catch (JdException e){
            return null;
        }
    }

    /**
     * 秒杀商品查询接口
     * @param secKillGoodsReq
     * @return
     */
    public UnionOpenGoodsSeckillQueryResponse seckillGoodsQuery(SecKillGoodsReq secKillGoodsReq){
        JdClient client=new DefaultJdClient(jdApiConfig.getServerUrl(),jdApiConfig.getAccessToken(),jdApiConfig.getAppKey(),jdApiConfig.getAppSecret());

        UnionOpenGoodsSeckillQueryRequest  request = new UnionOpenGoodsSeckillQueryRequest();
        request.setGoodsReq(secKillGoodsReq);
        try {
            UnionOpenGoodsSeckillQueryResponse response = client.execute(request);
            return response;
        }catch (JdException e){
            return null;
        }
    }

    /**
     * 通过subUnionId获取推广链接
     * @param promotionCodeReq
     * @return
     */
    public UnionOpenPromotionBysubunionidGetResponse getLinkBySubunionid(PromotionCodeReq promotionCodeReq){
        JdClient client=new DefaultJdClient(jdApiConfig.getServerUrl(),jdApiConfig.getAccessToken(),jdApiConfig.getAppKey(),jdApiConfig.getAppSecret());

        UnionOpenPromotionBysubunionidGetRequest request = new UnionOpenPromotionBysubunionidGetRequest();
        request.setPromotionCodeReq(promotionCodeReq);
        try {
            UnionOpenPromotionBysubunionidGetResponse response = client.execute(request);
            if (response.getData()==null){
                promotionCodeReq.setCouponUrl("");
                request.setPromotionCodeReq(promotionCodeReq);
                response = client.execute(request);
            }
            return response;
        }catch (JdException e){
            return null;
        }
    }

    /**
     * 优惠券领取情况查询接口
     * @param couponUrls  优惠券链接集合；上限10（GET请求）；上限50（POST请求或SDK调用）
     * @return
     */
    public UnionOpenCouponQueryResponse couponQuery(String[] couponUrls){
        JdClient client=new DefaultJdClient(jdApiConfig.getServerUrl(),jdApiConfig.getAccessToken(),jdApiConfig.getAppKey(),jdApiConfig.getAppSecret());
        UnionOpenCouponQueryRequest request = new UnionOpenCouponQueryRequest();
        request.setCouponUrls(couponUrls);
        try {
            UnionOpenCouponQueryResponse response = client.execute(request);
            return response;
        }catch (JdException e){
            return null;
        }
    }


    //###################排序处理后的方法###########################

    /**
     * 对京粉精选商品列表进行处理，重新设置佣金########废弃########
     * @param goodsReq
     * @return
     */
    public HashMap jingfenGoodsQueryRank(JFGoodsReq goodsReq){
        HashMap map = new HashMap();
        JdClient client=new DefaultJdClient(jdApiConfig.getServerUrl(),jdApiConfig.getAccessToken(),jdApiConfig.getAppKey(),jdApiConfig.getAppSecret());

        UnionOpenGoodsJingfenQueryRequest request = new UnionOpenGoodsJingfenQueryRequest();
        request.setGoodsReq(goodsReq);
        try {
            UnionOpenGoodsJingfenQueryResponse response = client.execute(request);
            JFGoodsResp[] originData = response.getData();
            List<JdCommodityDto> jdCommodityDtos = new ArrayList<>();
            for (JFGoodsResp jfGoodsResp:originData){
                JdCommodityDto dto = new JdCommodityDto();
                //重新封装商品信息
                dto.setSkuId(jfGoodsResp.getSkuId());
                dto.setSkuName(jfGoodsResp.getSkuName());
                dto.setInOrderCount30Days(jfGoodsResp.getInOrderCount30Days());
                dto.setInOrderCount30DaysSku(jfGoodsResp.getInOrderCount30DaysSku());
                dto.setIsHot(jfGoodsResp.getIsHot());
                dto.setMaterialUrl(jfGoodsResp.getMaterialUrl());
                dto.setImageInfo(jfGoodsResp.getImageInfo()==null?"":jfGoodsResp.getImageInfo());

                //处理商品是否能拼购
                if (jfGoodsResp.getPinGouInfo().getPingouPrice()!=null){
                    dto.setJdPrice(jfGoodsResp.getPinGouInfo().getPingouPrice());
                    dto.setPriceType(2);//拼购价格
                    dto.setPingouCount(jfGoodsResp.getPinGouInfo().getPingouTmCount());
                }else {
                    dto.setJdPrice(jfGoodsResp.getPriceInfo().getPrice());
                    dto.setPriceType(1); //无线端价格
                }

                //如果没有券，则discountPrice=jdPrice
                dto.setDiscountPrice(dto.getJdPrice());
                //处理优惠券
                dto.setDiscount(0D); //初始化
                long currentTime = System.currentTimeMillis();
                jd.union.open.goods.jingfen.query.response.Coupon[] couponArr = jfGoodsResp.getCouponInfo().getCouponList();
                for (jd.union.open.goods.jingfen.query.response.Coupon coupon:couponArr){
                    int bindType = coupon.getBindType();
                    if (bindType!=3){
                        continue;
                    }
                    if ((coupon.getUseEndTime()!=null && currentTime>coupon.getUseEndTime())
                            ||(coupon.getGetEndTime()!=null &&currentTime>coupon.getGetEndTime())){
                        continue;
                    }
                    Double discount = coupon.getDiscount();
                    if (discount>dto.getDiscount()){
                        dto.setDiscount(discount);
                        dto.setPlatformType(coupon.getPlatformType());
                        dto.setBindType(bindType);
                        if(coupon.getGetStartTime()!=null){
                            dto.setGetStartTime(coupon.getGetStartTime());
                        }
                        if(coupon.getGetEndTime()!=null){
                            dto.setGetEndTime(coupon.getGetEndTime());
                        }
                        if(coupon.getUseStartTime()!=null){
                            dto.setUseStartTime(coupon.getUseStartTime());
                        }
                        if(coupon.getUseEndTime()!=null){
                            dto.setUseEndTime(coupon.getUseEndTime());
                        }
                        dto.setDiscountPrice(dto.getJdPrice()-discount);
                        dto.setLink(coupon.getLink());
                    }
                }

                //处理分佣价格和分佣比例
                CommissionInfo commissionInfo = jfGoodsResp.getCommissionInfo();
                dto.setCommissionRate(commissionInfo.getCommissionShare()*jdApiConfig.getCommissionRate());
                dto.setCommission(dto.getDiscountPrice()*dto.getCommissionRate()/100);
                dto.setCommissionStr(df.format(dto.getCommission()));
                jdCommodityDtos.add(dto);

            }
            map.put("data",jdCommodityDtos);
            map.put("totalCount",response.getTotalCount());
            map.put("code",response.getCode());
            map.put("message",response.getMessage());
        }catch (JdException e){
            map.put("code",500);
            map.put("message","调用京东接口异常！");
        }
        return map;
    }

    /**
     * 首页商品列表，先走京粉接口，再请求高级搜索接口
     * @param goodsReq
     * @return
     */
    public HashMap jingfenGoodsQueryRankTransfer(JFGoodsReq goodsReq,HashMap columnCommodityMap,Integer userId){
        HashMap map = new HashMap();
        JdClient client=new DefaultJdClient(jdApiConfig.getServerUrl(),jdApiConfig.getAccessToken(),jdApiConfig.getAppKey(),jdApiConfig.getAppSecret());

        UnionOpenGoodsJingfenQueryRequest request = new UnionOpenGoodsJingfenQueryRequest();
        request.setGoodsReq(goodsReq);
        try {
            UnionOpenGoodsJingfenQueryResponse response = client.execute(request);
            JFGoodsResp[] originData = response.getData();
            List<Long> skuIds = new ArrayList<>();
            for (JFGoodsResp jfGoodsResp:originData){
                skuIds.add(jfGoodsResp.getSkuId());
            }
            GoodsReq queryReq = new GoodsReq();
            Long[] skuIdsArr=skuIds.toArray(new Long[skuIds.size()]);
            queryReq.setSkuIds(skuIdsArr);
            queryReq.setSort(goodsReq.getSort());
            queryReq.setSortName(goodsReq.getSortName());
            HashMap resMap = this.searchGoodsByKeywordRank(queryReq,null,userId);
            resMap.put("totalCount",response.getTotalCount());
            return resMap;
        }catch (JdException e){
            map.put("code",500);
            map.put("message","调用京东接口异常！");
            return map;
        }
    }

    /**
     * 关键词商品查询接口重设佣金比例
     * @param goodsReq
     * @return
     */
    public HashMap searchGoodsByKeywordRank(GoodsReq goodsReq,Map<String,String> columnCommodityMap,Integer userId){

        //获取用户佣金比例和返现比例
        LitemallUser user = userService.findById(userId);
        int promotionRate = 0;//有券的佣金比例
        int nocouponRebateRate = 0;//无券的总比例
        int nocouponRebatePromotionRate = 0;//无券的返利比例
        int nocouponPromotionRate = 0;//无券的佣金比例
        if (user!=null){
            promotionRate = user.getPromotionRate();
            nocouponRebateRate = user.getNocouponRebateRate();
            nocouponRebatePromotionRate = user.getNocouponRebatePromotionRate();
            nocouponPromotionRate = user.getNocouponPromotionRate();
        }

        HashMap<Integer,JdCommodityDto> columnCommodityPostionMap = new HashMap<>();

        //普通用户需要返回代言人佣金比例
        XiaolvquanCommissionConfig config = null;
        if (user.getUserLevel().equals(XiaolvquanConstant.USER_LEVEL_CONSUMER)){
            config = xiaolvquanCommissionConfigMapper.selectByPrimaryKey(XiaolvquanConstant.USER_LEVEL_SPOKES_MAN);
        }

        HashMap map = new HashMap();
        JdClient client=new DefaultJdClient(jdApiConfig.getServerUrl(),jdApiConfig.getAccessToken(),jdApiConfig.getAppKey(),jdApiConfig.getAppSecret());
        UnionOpenGoodsQueryRequest request = new UnionOpenGoodsQueryRequest();
        request.setGoodsReqDTO(goodsReq);
        try {
            UnionOpenGoodsQueryResponse response = client.execute(request);
            List<JdCommodityDto> jdCommodityDtos = new ArrayList<>();
            if(response.getData()==null){
                //商品搜索不到
                map.put("data",jdCommodityDtos);
                map.put("totalCount",response.getTotalCount());
                map.put("code",response.getCode());
                map.put("message",response.getMessage());
                return map;
            }
            GoodsResp[] originData = response.getData();
            for (GoodsResp goodsResp:originData){
                JdCommodityDto dto = new JdCommodityDto();
                //重新封装商品信息
                dto.setSkuId(goodsResp.getSkuId());
                dto.setSkuName(goodsResp.getSkuName());
                dto.setInOrderCount30Days(goodsResp.getInOrderCount30Days());
                dto.setIsHot(goodsResp.getIsHot());
                dto.setMaterialUrl(goodsResp.getMaterialUrl());
                dto.setComments(goodsResp.getComments());
                dto.setGoodComments(goodsResp.getGoodCommentsShare());
                //处理商品是否能拼购
                if (goodsResp.getPinGouInfo().getPingouPrice()!=null){
                    dto.setJdPrice(goodsResp.getPinGouInfo().getPingouPrice());
                    dto.setPriceType(2);//拼购价格
                    dto.setPingouCount(goodsResp.getPinGouInfo().getPingouTmCount());
                }else {
                    dto.setJdPrice(goodsResp.getPriceInfo().getPrice());
                    dto.setPriceType(1); //无线端价格
                }

                //把图片http地址转成https
                if (goodsResp.getImageInfo()!=null){
                    for (UrlInfo urlInfo:goodsResp.getImageInfo().getImageList()){
                        urlInfo.setUrl(urlInfo.getUrl().replace("http:","https:"));
                    }
                }
                dto.setImageInfo(goodsResp.getImageInfo()==null?new Object():goodsResp.getImageInfo());

                //如果没有券，则discountPrice=jdPrice
                dto.setDiscountPrice(dto.getJdPrice());
                //处理优惠券
                dto.setDiscount(0D); //初始化

                XiaolvquanColumnGoods goods = null;
                String skuId = dto.getSkuId()+"";
                if(columnCommodityMap!=null && columnCommodityMap.get(skuId)!=null){
                    //专题商品
                    goods = JSON.parseObject(columnCommodityMap.get(skuId),new TypeReference<XiaolvquanColumnGoods>() {});
                    dto.setPostion(goods.getPosition());
                    dto.setNote(goods.getNote());
                }else{
                    //非专题商品或者专题商品没有配置固定位
                    dto.setPostion(0);
                }
                if (goods!=null && goods.getSkusource()!=null && goods.getSkusource().equals(XiaolvquanConstant.SKU_SOURCE_JD)){
                    //专题配置京东商家商品
                    CouponResp[] couponResp = this.couponQuery(new String[]{goods.getCouponlink()}).getData();
                    if (couponResp==null||couponResp.length==0){
                        continue;
                    }
                    Double dicount = couponResp[0].getDiscount();
                    String link = goods.getCouponlink();
                    String platformType = couponResp[0].getPlatform();
                    dto.setDiscountPrice(dto.getJdPrice()-dicount);
                    dto.setLink(link);
                    dto.setDiscount(dicount);
                    //dto.setPlatformType(Integer.parseInt(platformType));
                    dto.setColumnGoodsId(goods.getId());
                }else{
                    //京粉商品
                    long currentTime = System.currentTimeMillis();
                    jd.union.open.goods.query.response.Coupon[] couponArr = goodsResp.getCouponInfo().getCouponList();
                    for (jd.union.open.goods.query.response.Coupon coupon:couponArr){
                        //是否最有券
                        if (coupon.getIsBest()==1){
                            dto.setDiscountPrice(dto.getJdPrice()-coupon.getDiscount());
                            dto.setLink(coupon.getLink());
                            dto.setDiscount(coupon.getDiscount());
                            dto.setPlatformType(coupon.getPlatformType());
                            dto.setBindType(coupon.getBindType());
                            break;
                        }
                        int bindType = coupon.getBindType();
                        if (bindType!=3||coupon.getQuota()>dto.getJdPrice()){
                            continue;
                        }
                        if ((coupon.getUseEndTime()!=null && currentTime>coupon.getUseEndTime())
                                ||(coupon.getGetEndTime()!=null &&currentTime>coupon.getGetEndTime())){
                            continue;
                        }
                        Double discount = coupon.getDiscount();
                        if (discount>dto.getDiscount()){
                            dto.setDiscount(discount);
                            dto.setPlatformType(coupon.getPlatformType());
                            dto.setBindType(bindType);
                            if(coupon.getGetStartTime()!=null){
                                dto.setGetStartTime(coupon.getGetStartTime());
                            }
                            if(coupon.getGetEndTime()!=null){
                                dto.setGetEndTime(coupon.getGetEndTime());
                            }
                            if(coupon.getUseStartTime()!=null){
                                dto.setUseStartTime(coupon.getUseStartTime());
                            }
                            if(coupon.getUseEndTime()!=null){
                                dto.setUseEndTime(coupon.getUseEndTime());
                            }
                            dto.setDiscountPrice(dto.getJdPrice()-discount);
                            dto.setLink(coupon.getLink());
                        }
                    }
                }

                //佣金和返利计算
                jd.union.open.goods.query.response.CommissionInfo commissionInfo = goodsResp.getCommissionInfo();
                if (dto.getLink()==null){
                    dto.setHasRebate(true);//没有优惠券，有返利
                    //佣金计算
                    double userCommissionRate = commissionInfo.getCommissionShare()*nocouponRebateRate/100*nocouponPromotionRate/100;
                    double userCommission = dto.getDiscountPrice()*userCommissionRate/100;
                    dto.setCommissionRate(userCommissionRate);
                    dto.setCommission(userCommission);
                    dto.setCommissionStr(df.format(userCommission));
                    //普通用户推广赚提示
                    if (user.getUserLevel().equals(XiaolvquanConstant.USER_LEVEL_CONSUMER)&&config!=null){
                        dto.setMemberCommission(df.format(dto.getDiscountPrice()*commissionInfo.getCommissionShare()/100*config.getNocouponRebateRate()/100*config.getNocouponPromotionRate()/100));
                    }
                    //返利计算
                    double rebate = dto.getJdPrice()*commissionInfo.getCommissionShare()/100*nocouponRebateRate/100*nocouponRebatePromotionRate/100;
                    double rebatePrice = dto.getJdPrice()-rebate;
                    dto.setRebate(df.format(rebate));
                    dto.setRebatePrice(df.format(rebatePrice));
                    //如果过滤有券（isCoupon=1），而实际没有有效券，则return
                    /*if (goodsReq.getIsCoupon()!=null&&goodsReq.getIsCoupon()==1){
                        continue;
                    }*/
                }else {
                    dto.setHasRebate(false); //有优惠券，没有返利
                    //佣金比例计算
                    double userCommissionRate = commissionInfo.getCommissionShare()*promotionRate/100;
                    double userCommission = dto.getDiscountPrice()*userCommissionRate/100;
                    dto.setCommissionRate(userCommissionRate);
                    dto.setCommission(userCommission);
                    dto.setCommissionStr(df.format(userCommission));
                    //普通用户推广赚提示
                    if (user.getUserLevel().equals(XiaolvquanConstant.USER_LEVEL_CONSUMER)&&config!=null){
                        dto.setMemberCommission(df.format(dto.getDiscountPrice()*commissionInfo.getCommissionShare()/100*config.getPromotionRate()/100));
                    }
                    //返利不存在
                }
                //固定位商品
                if (dto.getPostion()!=null&&dto.getPostion()!=0){
                    columnCommodityPostionMap.put(dto.getPostion(),dto);
                }else{
                    jdCommodityDtos.add(dto);
                }
            }

            //处理搜索带有skuids时，排序不生效，重新进行排序
            //转链不走
            if (goodsReq.getSkuIds()!=null&&goodsReq.getSkuIds().length>1){
                if (StringUtils.isBlank(goodsReq.getSortName())){
                    goodsReq.setSortName(JdCommoditySort.SORT_NAME_PRICE);
                }
                if (StringUtils.isBlank(goodsReq.getSort())){
                    goodsReq.setSort(JdCommoditySort.SORT_DESC);
                }
                jdCommoditySort.sort(jdCommodityDtos,goodsReq.getSortName(),goodsReq.getSort());
            }
            //固定位排序
            if (columnCommodityPostionMap.size()>0){
                map.put("data",jdCommoditySort.sortByPosition(jdCommodityDtos,columnCommodityPostionMap));
            }else{
                map.put("data",jdCommodityDtos);
            }
            map.put("totalCount",response.getTotalCount());
            map.put("code",response.getCode());
            map.put("message",response.getMessage());
        }catch (JdException e){
            map.put("code",500);
            map.put("message","调用京东接口异常！");
        }
        return map;
    }

    /**
     * 秒杀商品查询接口重设佣金比例
     * @param secKillGoodsReq
     * @return
     */
    public HashMap seckillGoodsQueryRank(SecKillGoodsReq secKillGoodsReq){
        HashMap map = new HashMap();
        JdClient client=new DefaultJdClient(jdApiConfig.getServerUrl(),jdApiConfig.getAccessToken(),jdApiConfig.getAppKey(),jdApiConfig.getAppSecret());

        UnionOpenGoodsSeckillQueryRequest  request = new UnionOpenGoodsSeckillQueryRequest();
        request.setGoodsReq(secKillGoodsReq);
        try {
            UnionOpenGoodsSeckillQueryResponse response = client.execute(request);
            SecKillGoodsResp[] originData = response.getData();
            for (SecKillGoodsResp secKillGoodsResp:originData){
                secKillGoodsResp.setCommission(secKillGoodsResp.getCommission()*jdApiConfig.getCommissionRate());
                secKillGoodsResp.setCommissionShare(secKillGoodsResp.getCommissionShare()*jdApiConfig.getCommissionRate());
            }
            map.put("data",response.getData());
            map.put("totalCount",response.getTotalCount());
            map.put("code",response.getCode());
            map.put("message",response.getMessage());
        }catch (JdException e){
            map.put("code",500);
            map.put("message","调用京东接口异常！");
        }
        return map;
    }

    /**
     * 获取通用推广链接接口  【废弃】
     * @param materialId
     * @param ext1
     * @return
     */
    public HashMap referralLink(String materialId,String ext1){
        HashMap map = new HashMap();
        JdClient client=new DefaultJdClient(jdApiConfig.getServerUrl(),jdApiConfig.getAccessToken(),jdApiConfig.getAppKey(),jdApiConfig.getAppSecret());

        UnionOpenPromotionCommonGetRequest  request = new UnionOpenPromotionCommonGetRequest();
        jd.union.open.promotion.common.get.request.PromotionCodeReq promotionCodeReq = new jd.union.open.promotion.common.get.request.PromotionCodeReq();
        promotionCodeReq.setMaterialId(materialId);
        promotionCodeReq.setSiteId(jdApiConfig.getSiteId());
        promotionCodeReq.setExt1(ext1);
        request.setPromotionCodeReq(promotionCodeReq);

        try {
            UnionOpenPromotionCommonGetResponse response = client.execute(request);
            map.put("data",response.getData());
            map.put("code",response.getCode());
            map.put("message",response.getMessage());
        }catch (JdException e){
            map.put("code",500);
            map.put("message","调用京东接口异常！");
        }
        return map;
    }
}
