package org.linlinjava.litemall.wx.web;

import jd.union.open.goods.jingfen.query.request.JFGoodsReq;
import jd.union.open.goods.query.request.GoodsReq;
import jd.union.open.goods.seckill.query.request.SecKillGoodsReq;
import jd.union.open.promotion.bysubunionid.get.request.PromotionCodeReq;
import jd.union.open.promotion.bysubunionid.get.response.UnionOpenPromotionBysubunionidGetResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.api.Jd;
import org.linlinjava.litemall.core.api.JdCommodityDto;
import org.linlinjava.litemall.core.dto.ColumnCommodityReq;
import org.linlinjava.litemall.core.util.ObjectTransferUtil;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.dao.XiaolvquanColumnGoodsMapper;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.domain.XiaolvquanColumnGoods;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: lichunyang
 * @date: Created in 14:07 2019/6/10
 * @Description:
 */
@RestController
@RequestMapping("/wx/commodity")
@Validated
public class WxCommodityController {
	private final Log logger = LogFactory.getLog(WxCommodityController.class);

	@Autowired
	private Jd jd;
	@Autowired
	private CommodityService commodityService;
	@Autowired
	private LitemallUserService userService;
	@Resource
	private XiaolvquanColumnGoodsMapper columnGoodsMapper;
	private String subUnionId = "";

	private final static ArrayBlockingQueue<Runnable> WORK_QUEUE = new ArrayBlockingQueue<>(9);

	private final static RejectedExecutionHandler HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();

	private static ThreadPoolExecutor executorService = new ThreadPoolExecutor(16, 16, 1000, TimeUnit.MILLISECONDS, WORK_QUEUE, HANDLER);

	/**
	 * 获取京粉商品列表
	 * @param jfGoodsReq
	 * @return
	 */
	@PostMapping("/jingfei/list")
	public Object list(@LoginUser Integer userId,
                       @RequestBody JFGoodsReq jfGoodsReq) {
	    if (userId==null){
	        return ResponseUtil.unlogin();
        }
		//查询列表数据
		HashMap goodsList = jd.jingfenGoodsQueryRankTransfer(jfGoodsReq,null,userId);
		return ResponseUtil.ok(goodsList);
	}

	/**
	 * 高级接口，支持商品搜索
	 * @param goodsReq
	 * @return
	 */
	@PostMapping("/advance/search")
	public Object advanceSearchByKeyword(
            @LoginUser Integer userId,
			@RequestBody GoodsReq goodsReq,
			@RequestParam(required = false,defaultValue = "0") Integer columnGoodsId) {
	    if (userId==null){
	        return ResponseUtil.unlogin();
        }
        Map map = new HashMap();
        if (columnGoodsId!=null){
	    	//根据专栏商品id查询数据
			XiaolvquanColumnGoods columnGoods = columnGoodsMapper.selectById(columnGoodsId);
			map = ObjectTransferUtil.beanToMap(columnGoods);
		}
		//查询列表数据
		HashMap searchGoodsByKeywordRank = jd.searchGoodsByKeywordRank(goodsReq,map,userId);
		return ResponseUtil.ok(searchGoodsByKeywordRank);
	}

	/**
	 * 获取秒杀商品接口
	 * @param secKillGoodsReq
	 * @return
	 */
	@PostMapping("/seckill/list")
	public Object seckillList(
			@RequestBody SecKillGoodsReq secKillGoodsReq) {
		//查询列表数据
		HashMap seckillGoodsQueryRank = jd.seckillGoodsQueryRank(secKillGoodsReq);
		return ResponseUtil.ok(seckillGoodsQueryRank);
	}

	/**
	 * 优惠券领取情况查询接口
	 * @param couponUrls
	 * @return
	 */
	@PostMapping("coupon")
	public Object queryCoupon(
			@RequestBody String[] couponUrls) {
		//查询优惠券领取情况
		return ResponseUtil.ok(jd.couponQuery(couponUrls));
	}

	/**
	 * 获取通用推广链接接口
	 * @param promotionCodeReq
	 * @return
	 */
	@PostMapping("share/link")
	public Object list(@LoginUser Integer userId,
					   @RequestBody PromotionCodeReq promotionCodeReq,
					   @RequestParam(required = false,defaultValue = "false") boolean isBuyUser,
					   @RequestParam(required = false,defaultValue = "false") boolean isCoupon) {
		if (userId==null){
			return ResponseUtil.unlogin();
		}
		LitemallUser user = userService.findById(userId);
		//用于小程序中点击领券购买的操作，isBuyUser记录是否本人购买，isCoupon记录是否有返利
        logger.info("isBuyUser:"+isBuyUser);
        logger.info("isCoupon:"+isCoupon);
		String secondPos = isBuyUser?userId+"":"0";
		String thirdPos = isCoupon?"2":"1";
		subUnionId = userId.toString() + "_" + secondPos + "_" +thirdPos + "_"+user.getUserLevel();
		logger.info("subunionId:"+subUnionId);
	    promotionCodeReq.setSubUnionId(subUnionId);
        UnionOpenPromotionBysubunionidGetResponse response = jd.getLinkBySubunionid(promotionCodeReq);
		return response;
	}

	/**
	 * 获取通用推广链接二维码
	 * @param promotionCodeReq
	 * @return
	 */
	@PostMapping("share/link/code")
	public Object shareLinkCode(
            @LoginUser Integer userId,
			@RequestBody PromotionCodeReq promotionCodeReq,
			@RequestParam(required = false,defaultValue = "false") boolean isCoupon) {
		if (userId==null){
			return ResponseUtil.unlogin();
		}
		LitemallUser user = userService.findById(userId);
		String thirdPos = isCoupon?"2":"1";  //2表示有券没有返利  1表示无券有返利
		subUnionId = userId.toString() + "_0"  + "_"+thirdPos+ "_"+user.getUserLevel();
        promotionCodeReq.setSubUnionId(subUnionId);
		return commodityService.getShareLinkWxCode(promotionCodeReq);
	}

    /**
     * 批量转链接
     * @param userId
     * @param shortUrlArr
     * @return
     */
    @PostMapping("translink/batch")
    public Object translink(@LoginUser Integer userId,
                            @RequestBody String[] shortUrlArr) {
        if (userId==null){
            return ResponseUtil.unlogin();
        }
        return commodityService.batchTranslink(shortUrlArr,userId);
    }

	/**
	 * 专题商品列表请求接口
	 * @param userId
	 * @param columnCommodityReq
	 * @return
	 */
	@PostMapping("/column/list")
	public Object getColumnCommodityList(
	        @LoginUser Integer userId,
			@RequestBody ColumnCommodityReq columnCommodityReq) {
		if (userId==null){
            return ResponseUtil.unlogin();
		}
		//查询专栏商品数据
		return this.commodityService.getColumnCommodityByPage(columnCommodityReq,userId);
	}

	/**
	 * 高级搜索暴露接口
	 * @param goodsReq
	 * @return
	 */
	@PostMapping("api/advance/search")
	public Object advanceSearchByKeyword(@RequestBody GoodsReq goodsReq) {
		//查询列表数据
		return jd.searchGoodsByKeyword(goodsReq);
	}

}