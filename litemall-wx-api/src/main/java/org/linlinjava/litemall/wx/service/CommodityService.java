package org.linlinjava.litemall.wx.service;

import com.alibaba.fastjson.JSON;
import jd.union.open.goods.query.request.GoodsReq;
import jd.union.open.promotion.bysubunionid.get.request.PromotionCodeReq;
import jd.union.open.promotion.bysubunionid.get.response.UnionOpenPromotionBysubunionidGetResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.api.Jd;
import org.linlinjava.litemall.core.api.JdCommodityDto;
import org.linlinjava.litemall.core.dto.ColumnCommodityReq;
import org.linlinjava.litemall.core.qcode.QCodeService;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.dao.XiaolvquanColumnGoodsMapper;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.domain.XiaolvquanColumnGoods;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lichunyang
 * @date: Created in 16:50 2019/6/11
 * @Description:
 */
@Service
public class CommodityService {

    private final Log logger = LogFactory.getLog(CommodityService.class);

    @Autowired
    private QCodeService qCodeService;
    @Resource
    private Jd jd;
    @Resource
    private XiaolvquanColumnGoodsMapper columnGoodsMapper;
    @Resource
    private LitemallUserService userService;


    /**
     * 获取推量链接二维码图片地址
     * @param promotionCodeReq
     * @return
     */
    public Object getShareLinkWxCode(PromotionCodeReq promotionCodeReq){
        try {
            UnionOpenPromotionBysubunionidGetResponse response = jd.getLinkBySubunionid(promotionCodeReq);
            if (response!=null&&response.getData()!=null){
                String shortUrl = response.getData().getShortURL();
                String code = shortUrl.substring(shortUrl.length()-6,shortUrl.length());
                logger.info("shortUrl:"+shortUrl);
                logger.info("code:"+code);
                String imageUrl = qCodeService.createUrlImage(shortUrl,code);
                HashMap map = new HashMap();
                map.put("imageUrl",imageUrl);
                return ResponseUtil.ok(map);
            }else {
                return ResponseUtil.fail(400,"无法获取推广链接");
            }
        }catch (Exception e){
            return ResponseUtil.fail(500,"生成推广链接二维码失败");
        }
    }

    /**
     * 批量转链接
     * @param linkArr
     * @return
     */
    public List batchTranslink(String[] linkArr,Integer userId){
        LitemallUser user = userService.findById(userId);
        //根据短链接查询商品
        List<String> shortLinkList = new ArrayList<>();
        for (String link:linkArr){
            GoodsReq goodsReq = new GoodsReq();
            goodsReq.setKeyword(link);
            HashMap map = jd.searchGoodsByKeywordRank(goodsReq,null,userId);
            List<JdCommodityDto> jdCommodityDtos = JSON.parseArray(JSON.toJSONString(map.get("data")), JdCommodityDto.class);
            if (!jdCommodityDtos.isEmpty()){
                JdCommodityDto jdCommodityDto = jdCommodityDtos.get(0);
                String materialId = jdCommodityDto.getMaterialUrl();
                String couponUrl = jdCommodityDto.getLink();
                //转链接
                String thirdPos = jdCommodityDto.getLink()==null?"1":"2";
                String subUnionId = userId.toString() + "_0"  + "_"+thirdPos+ "_"+user.getUserLevel();
                PromotionCodeReq promotionCodeReq = new PromotionCodeReq();
                promotionCodeReq.setMaterialId(materialId);
                promotionCodeReq.setCouponUrl(couponUrl);
                promotionCodeReq.setSubUnionId(subUnionId);
                //调用转链接接口
                UnionOpenPromotionBysubunionidGetResponse promotionBysubunionidGetResponse = jd.getLinkBySubunionid(promotionCodeReq);
                if (promotionBysubunionidGetResponse.getData()!=null){
                    String shortUrl = promotionBysubunionidGetResponse.getData().getShortURL();
                    shortLinkList.add(shortUrl);
                }else {
                    shortLinkList.add("");
                }
            }else {
                shortLinkList.add("");
            }
        }
        return shortLinkList;
    }

    /**
     * 获取专栏商品数据，需要加缓存
     * @param columnCommodityReq
     * @return
     */
    public List<JdCommodityDto> getColumnCommodityList(ColumnCommodityReq columnCommodityReq,Integer userId){

        //根据专栏id从数据库查询专栏商品
        List<XiaolvquanColumnGoods> columnGoodsList = this.columnGoodsMapper.selectByColumnId(columnCommodityReq.getColumnId());
        if (columnGoodsList==null||columnGoodsList.size()==0){
            return null;
        }
        Long[] skuidArr = new Long[columnGoodsList.size()];
        Map columnCommodityMap = new HashMap<String,String>();
        for (int i=0;i<columnGoodsList.size();i++){
            XiaolvquanColumnGoods goods = columnGoodsList.get(i);
            skuidArr[i] = Long.parseLong(goods.getSkuid());
            //判断来源是否是京东商家
            columnCommodityMap.put(goods.getSkuid(),JSON.toJSONString(goods));
        }
        //根据skuIds调用京东高级搜索接口
        GoodsReq goodsReq = new GoodsReq();
        goodsReq.setSkuIds(skuidArr);
        goodsReq.setSort(columnCommodityReq.getSort());
        goodsReq.setSortName(columnCommodityReq.getSortName());
        HashMap map = jd.searchGoodsByKeywordRank(goodsReq,columnCommodityMap,userId);
        List<JdCommodityDto> jdCommodityDtos = JSON.parseArray(JSON.toJSONString(map.get("data")), JdCommodityDto.class);
        return jdCommodityDtos;
    }

    /**
     * 专栏商品分页
     * @param columnCommodityReq
     * @return
     */
    @Cacheable(value = "myCache",keyGenerator = "cacheKeyGenerator")
    public Object getColumnCommodityByPage(ColumnCommodityReq columnCommodityReq,Integer userId){

        try {
            List<JdCommodityDto> jdCommodityDtos = this.getColumnCommodityList(columnCommodityReq,userId);
            List<JdCommodityDto> commodityDtos = new ArrayList<JdCommodityDto>();
            int pageIndex = columnCommodityReq.getPageIndex();
            int pageNum = columnCommodityReq.getPageNum();
            if (jdCommodityDtos==null||jdCommodityDtos.isEmpty()){
                ResponseUtil.ok();
            }
            //获取页数
            for (int i=(pageIndex-1)*pageNum;i<pageIndex*pageNum;i++){
                if (i>jdCommodityDtos.size()-1){
                    break;
                }
                commodityDtos.add(jdCommodityDtos.get(i));
            }
            return ResponseUtil.ok(commodityDtos,jdCommodityDtos.size());
        }catch (Exception e){
            return ResponseUtil.fail(500,"失败");
        }
    }



}
