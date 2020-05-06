package org.linlinjava.litemall.db.util;

public class XiaolvquanConstant {

    //xiaolvquan_user_bill_month status
    public static final Integer TO_BE_SETTLED = 1;
    public static final Integer SETTLED = 2;

    //xiaolvquan_user_account status
    public static final Integer ACCOUNT_AVAILABLE = 0;
    public static final Integer ACCOUNT_FORBIDDEN = 1;
    public static final Integer ACCOUNT_LOGOFF = 2;

    //用户等级
    public static final Integer USER_LEVEL_CONSUMER=1;//普通用户level
    public static final Integer USER_LEVEL_SPOKES_MAN=2;//普通用户level
    public static final Integer USER_LEVEL_PARTNER=3;//普通用户level

    public static final Integer HAVE_REBATE = 1;//订单表有返利

    //专栏商品来源
    public static final Integer SKU_SOURCE_JF = 1;//京粉商品
    public static final Integer SKU_SOURCE_JD = 2;//京东商家商品
}
