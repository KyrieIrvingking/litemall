package org.linlinjava.litemall.wx.web;

import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.XiaolvquanCategory;
import org.linlinjava.litemall.db.service.XiaolvquanCategoryService;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/wx/category")
@Validated
public class WxCommodityCategoryController {

    @Resource
    private XiaolvquanCategoryService xiaolvquanCategoryService;

    @GetMapping("/list")
    public Object list(@LoginUser Integer userId) {
        //查询分类列表
        if (userId==null){
            return ResponseUtil.unlogin();
        }
        List<XiaolvquanCategory> categories = xiaolvquanCategoryService.selectCategoryByPostion(null);
        return ResponseUtil.ok(categories);
    }

}
