package org.linlinjava.litemall.wx.web;

import org.linlinjava.litemall.core.api.JdCommodityDto;
import org.linlinjava.litemall.core.dto.ColumnCommodityReq;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.XiaolvquanColumnConfig;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.service.ColumnService;
import org.linlinjava.litemall.wx.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/wx/column")
@Validated
public class WxColumnController {

    @Autowired
    private ColumnService columnService;
    @Autowired
    private CommodityService commodityService;

    /**
     * 获取专题列表
     * @param userId
     * @param colType
     * @return
     */
    @PostMapping("/config/list")
    public Object getColumnCommodityList(
            @LoginUser Integer userId,
            @RequestParam Integer colType) {
        if (userId==null){
            return ResponseUtil.unlogin();
        }
        //查询专栏列表
        List<XiaolvquanColumnConfig> columnConfigs = this.columnService.selectByColType(colType);
        return ResponseUtil.ok(columnConfigs);
    }

    /**
     * 专题商品列表请求接口
     * @param userId
     * @param columnCommodityReq
     * @return
     */
    @PostMapping("/commodity/list")
    public Object getColumnCommodityList(
            @LoginUser Integer userId,
            @RequestBody ColumnCommodityReq columnCommodityReq) {
        if (userId==null){
            return ResponseUtil.unlogin();
        }
        //查询专栏商品数据
        return this.commodityService.getColumnCommodityByPage(columnCommodityReq,userId);
    }
}
