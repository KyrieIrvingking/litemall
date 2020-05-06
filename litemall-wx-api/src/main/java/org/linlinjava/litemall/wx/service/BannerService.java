package org.linlinjava.litemall.wx.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.qcode.QCodeService;
import org.linlinjava.litemall.core.util.DateUtils;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.dao.XiaolvquanBannerMapper;
import org.linlinjava.litemall.db.dao.XiaolvquanUserEffectMapper;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.domain.XiaolvquanBanner;
import org.linlinjava.litemall.db.domain.XiaolvquanUserBill;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.service.XiaolvquanUserBillService;
import org.linlinjava.litemall.wx.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: liming
 * @date: Created in 16:50 2019/6/11
 * @Description:
 */
@Service
public class BannerService {

    private final Log logger = LogFactory.getLog(BannerService.class);
    @Autowired
    private XiaolvquanUserBillService userBillService;
    @Autowired
    private LitemallUserService userService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private BannerService bannerService;

    @Resource
    private XiaolvquanBannerMapper xiaolvquanBannerMapper;




    /**
     * 查询banner内容，并转链
     * @param Id
     * @return
     */
    public Object getContentAndTranslink(Integer Id,Integer userId) {
        List<String> shortLinkList = new ArrayList<>();
        List<String> linkList = new ArrayList<>();
        XiaolvquanBanner xiaolvquanBanner = new XiaolvquanBanner();
        xiaolvquanBanner = this.getBannerContentById(Id);
        String  detailText = xiaolvquanBanner.getDetailText();
        String[] lines = detailText.split("\r?\n");
        Pattern pattern = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:]+");
        HashMap map = new HashMap();
        for (int i = 0; i < lines.length; i++) {
            Matcher matcher = pattern.matcher(lines[i]);
            StringBuffer buffer = new StringBuffer();
            while (matcher.find()) {
                buffer.append(matcher.group());
                String url = buffer.toString();
                linkList.add(url);
            }
        }
        String[] linkArr = linkList.toArray(new String[linkList.size()]);
        shortLinkList = commodityService.batchTranslink(linkArr,userId);
        logger.info(linkList);
        logger.info(shortLinkList);
        for (int i = 0; i < linkList.size(); i++) {
            String url = linkList.get(i);
            String convertUrl = shortLinkList.get(i);
            if ("".equals(convertUrl)){
                convertUrl = "（链接失效）";
            }
            detailText= detailText.replace(url,convertUrl);
        }
        xiaolvquanBanner.setDetailText(detailText);
        return xiaolvquanBanner;
    }
    public XiaolvquanBanner getBannerContentById (Integer Id){
        return this.xiaolvquanBannerMapper.selectByPrimaryKey(Id);
    }

}
