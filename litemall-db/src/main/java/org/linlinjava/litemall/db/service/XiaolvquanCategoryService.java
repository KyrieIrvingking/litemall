package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.XiaolvquanCategoryMapper;
import org.linlinjava.litemall.db.domain.XiaolvquanCategory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class XiaolvquanCategoryService {

    @Resource
    private XiaolvquanCategoryMapper xiaolvquanCategoryMapper;

    /**
     * 根据位置进行生效查询
     * @param map
     * @return
     */
    public List<XiaolvquanCategory> selectCategoryByPostion(HashMap map){
        return this.xiaolvquanCategoryMapper.selectByQuery(map);
    }
}
