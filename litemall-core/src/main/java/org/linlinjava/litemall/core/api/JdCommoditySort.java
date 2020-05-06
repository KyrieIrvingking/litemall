package org.linlinjava.litemall.core.api;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JdCommoditySort {

    public static final String SORT_NAME_PRICE="price";
    public static final String SORT_NAME_COMMISSION_SHARE="commissionShare";
    public static final String SORT_NAME_COMMISSION="commission";
    public static final String SORT_NAME_ORDER_COUNT="inOrderCount30Days";
    public static final String SORT_NAME_ORDER_COUNT_SKU="inOrderCount30DaysSku";
    public static final String SORT_NAME_COMMENTS="comments";
    public static final String SORT_NAME_GOOD_COMMENTS="goodComments";
    public static final String SORT_DESC="desc";
    public static final String SORT_ASC="asc";

    /**
     * 适用于高级搜索接口，且带有skuids的搜索排序
     * @param list
     * @param sortName
     * @param sort
     * @return
     */
    public List<JdCommodityDto> sort(List<JdCommodityDto> list,String sortName, String sort){
        Collections.sort(list, new Comparator<JdCommodityDto>(){
            public int compare(JdCommodityDto o1, JdCommodityDto o2) {
                double v1=0;
                double v2=0;
                switch (sortName){
                    case SORT_NAME_PRICE:
                        v1=Double.valueOf(o1.getDiscountPrice());//根据券后价排序
                        v2=Double.valueOf(o2.getDiscountPrice());//根据券后价排序
                        break;
                    case SORT_NAME_COMMISSION_SHARE:
                        v1=Double.valueOf(o1.getCommissionRate());//根据佣金比例排序
                        v2=Double.valueOf(o2.getCommissionRate());//根据佣金比例排序
                        break;
                    case SORT_NAME_COMMISSION:
                        v1=Double.valueOf(o1.getCommission());//根据佣金排序
                        v2=Double.valueOf(o2.getCommission());//根据佣金排序
                        break;
                    case SORT_NAME_ORDER_COUNT:
                        v1=Double.valueOf(o1.getInOrderCount30Days());//根据订单数排序
                        v2=Double.valueOf(o2.getInOrderCount30Days());//根据订单数排序
                        break;
                    case SORT_NAME_ORDER_COUNT_SKU:
                        v1=Double.valueOf(o1.getInOrderCount30Days());//默认所有的订单数都按照30日引单数排序，不考虑sku维度，因为搜索接口没有这个数据
                        v2=Double.valueOf(o2.getInOrderCount30Days());
                        break;
                    case SORT_NAME_COMMENTS:
                        v1=Double.valueOf(o1.getComments());//根据评论数排序
                        v2=Double.valueOf(o2.getComments());//根据评论数排序
                        break;
                    case SORT_NAME_GOOD_COMMENTS:
                        v1=Double.valueOf(o1.getGoodComments());//根据好评数排序
                        v2=Double.valueOf(o2.getGoodComments());//根据好评数排序
                        break;
                    default:
                        v1=Double.valueOf(o1.getDiscountPrice());//根据券后价排序
                        v2=Double.valueOf(o2.getDiscountPrice());//根据券后价排序
                }
                //升序
                if (sort.equals(SORT_ASC)){
                    if( v1>v2 ){
                        return 1;
                    }
                    if(v1==v2){
                        return 0;
                    }
                    return -1;
                }else {
                    //降序
                    if( v1<v2 ){
                        return 1;
                    }
                    if(v1==v2){
                        return 0;
                    }
                    return -1;
                }
            }
        });
        return list;
    }

    /**
     * 固定位排序
     * @param list
     * @return
     */
    public List<JdCommodityDto> sortPosition(List<JdCommodityDto> list){
        Collections.sort(list, new Comparator<JdCommodityDto>(){
            public int compare(JdCommodityDto o1, JdCommodityDto o2) {
                int v1=Integer.valueOf(o1.getPostion());
                int v2=Integer.valueOf(o2.getPostion());
                //升序
                if( v1>v2 ){
                    return 1;
                }
                if(v1==v2){
                    return 0;
                }
                return -1;
            }
        });
        return list;
    }

    public List<JdCommodityDto> sortByPosition(List<JdCommodityDto> list, HashMap<Integer,JdCommodityDto> map){
        List<JdCommodityDto> jdCommodityDtos = new ArrayList<>();
        int index = 0;
        int length = list.size()+map.size();
        for (int i=0;i<length;i++){
            if (map.get(i+1)!=null){
                jdCommodityDtos.add(map.get(i+1));
            }else{
                if (list==null||list.size()==0||index>list.size()-1) {
                    length++;
                    continue;
                }
                JdCommodityDto dto = list.get(index);
                jdCommodityDtos.add(dto);
                index++;
            }
        }
        return jdCommodityDtos;
    }
}
