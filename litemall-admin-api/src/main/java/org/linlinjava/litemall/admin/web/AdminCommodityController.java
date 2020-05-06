package org.linlinjava.litemall.admin.web;

import jd.union.open.goods.jingfen.query.request.JFGoodsReq;
import jd.union.open.goods.query.request.GoodsReq;
import jd.union.open.goods.seckill.query.request.SecKillGoodsReq;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.api.Jd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/commodity")
@Validated
public class AdminCommodityController {

    @Autowired
    private Jd jd;
    /**
     * 获取京粉商品列表
     * @param jfGoodsReq
     * @return
     */
    @RequiresPermissions("admin:commodity:list")
    @RequiresPermissionsDesc(menu={"商品管理" , "京粉商品"}, button="查询")
    @PostMapping("/jingfei/list")
    public Object list(@RequestBody JFGoodsReq jfGoodsReq) {
        //查询列表数据
        return jd.jingfenGoodsQuery(jfGoodsReq);
    }

    /**
     * 高级接口，支持商品搜索
     * @param goodsReq
     * @return
     */
    @RequiresPermissions("admin:commodity:list")
    @RequiresPermissionsDesc(menu={"商品管理" , "京粉商品"}, button="查询")
    @PostMapping("/advance/search")
    public Object advanceSearchByKeyword(@RequestBody GoodsReq goodsReq) {
        //查询列表数据
        return jd.searchGoodsByKeyword(goodsReq);
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
        return jd.seckillGoodsQueryRank(secKillGoodsReq);
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
        return jd.couponQuery(couponUrls);
    }
}
