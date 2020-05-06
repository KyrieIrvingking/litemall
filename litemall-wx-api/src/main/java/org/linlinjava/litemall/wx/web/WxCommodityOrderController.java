package org.linlinjava.litemall.wx.web;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.service.CommodityOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: lichunyang
 * @date: Created in 14:07 2019/6/10
 * @Description:
 */
@RestController
@RequestMapping("/wx/commodity/order")
@Validated
public class WxCommodityOrderController {
	private final Log logger = LogFactory.getLog(WxCommodityOrderController.class);

	@Autowired
	private CommodityOrderService commoidtyOrderService;

	/**
	 * 历史订单明细查询接口
	 * @param userId
	 * @param pageIndex
	 * @param pageNum
	 * @return
	 */
	@PostMapping("/history")
	public Object history(@LoginUser Integer userId,
						  @RequestParam(value = "pageIndex",defaultValue = "1") Integer pageIndex,
						  @RequestParam(value = "pageNum",defaultValue = "10") Integer pageNum,
						  @RequestBody(required = false) Integer[] validCode) {

                return commoidtyOrderService.orderHistoryList(userId,pageIndex,pageNum,validCode);
	}

}