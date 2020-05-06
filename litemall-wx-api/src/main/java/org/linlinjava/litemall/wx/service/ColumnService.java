package org.linlinjava.litemall.wx.service;

import org.linlinjava.litemall.db.dao.XiaolvquanColumnConfigMapper;
import org.linlinjava.litemall.db.domain.XiaolvquanColumnConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
/**
 * 专栏配置服务，支持爆品推荐、专题、tab位
 */
public class ColumnService {
    @Resource
    private XiaolvquanColumnConfigMapper columnConfigMapper;

    /**
     * 根据专题类型查询配置列表
     * @param colType  1表示爆品推荐，2表示专题，3表示tab栏目
     * @return
     */
    public List<XiaolvquanColumnConfig> selectByColType(Integer colType){
        return this.columnConfigMapper.selectByColType(colType);
    }
}
