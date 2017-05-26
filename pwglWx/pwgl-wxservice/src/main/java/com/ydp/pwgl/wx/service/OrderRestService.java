package com.ydp.pwgl.wx.service;

import com.ydp.face.BaseService;
import com.ydp.face.IService;
import com.ydp.typedef.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by 董亮 on 2017/5/17.
 */
@Service
public class OrderRestService extends BaseService implements IService {
    public OrderRestService() {
        super(OrderRestService.class);
    }

    @Override
    public void service(Param param) throws Exception {

    }
    /**
     * 本人相关订单情况
     * @param param
     * @return
     */
    public List<Map<String, Object>> getOrder(Param param){
        List<Map<String, Object>>  OrderExpandlist=getDao().getSqlSession().selectList("Order.getOrder",param);
        return  OrderExpandlist;
    }

    /**
     * 通过订单id获取订单详情
     * @param param
     * @return
     */
    public  List<Map<String, Object>>  getOrderXq(Param param){
        List<Map<String, Object>>  OrderExpandlist=getDao().getSqlSession().selectList("Order.getOrderXq",param);
        return  OrderExpandlist;
    }
    /**
     * 查询订单状态改变信息
     */
    public List<Map<String, Object>> getRecordOrderStatus(Param param)
    {
        List<Map<String, Object>> orderStatusList = getDao().getSqlSession().selectList("order.getRecordOrderStatus", param);
        return orderStatusList;
    }
    /**
     * 修改易得票订单表状态
     */
    public int updateOrderPayStatus(Param param) {
        return getDao().getSqlSession().update("order.updateOrderPayStatus", param);
    }
}
