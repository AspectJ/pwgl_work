package com.ydp.pwgl.wx.service;

import com.ydp.face.BaseService;
import com.ydp.face.IService;
import com.ydp.typedef.*;
import com.ydp.util.CodeUtil;
import com.ydp.util.Dao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 项目
 */
@Service
public class ItemService extends BaseService implements IService {

    public ItemService() {
        super(ItemService.class);
    }

    @Override
    public void service(Param param) throws Exception {
        DataResult result = new DataResult();
        Dao dao = getDao();

        try {
            //从请求参数获取操作类型
            String action = param.get("action").toString();
            if(CodeUtil.checkParam(action)){
                result.setStatusCode(StatusCode.MissingParams);
                param.setResult(result);
                return;
            }

            //根据操作类型路由
            //获取项目详情
            if (action.equals(Constant.ACTION_SEL)) {
                String itemid = param.getString("itemid");
                if(CodeUtil.checkParam(itemid)){
                    result.setStatusCode(StatusCode.MissingParams);
                    param.setResult(result);
                    return;
                }
                List<Map<String,Object>> resultlist=null;
                //先从redis获取缓存，若没有，再从数据库获取，并存入redis
                resultlist= (List<Map<String, Object>>) getRedisClient().bget("pwgl:wx:item:"+itemid);
                if(resultlist==null || resultlist.size()<=0){
                    resultlist=dao.getSqlSession().selectList("item.select", param);
                    getRedisClient().bset("pwgl:wx:item:"+itemid, resultlist);
                }
                result.setData(resultlist);
                result.setTotal(resultlist.size());
                result.setStatusCode(StatusCode.Success);
            }else if (action.equals(Constant.ACTION_INS)){

            }else if (action.equals(Constant.ACTION_DEL)){

            }else if (action.equals(Constant.ACTION_UPD)){

            }else{
                result.setStatusCode(StatusCode.UndefinedAction);
            }

            param.setResult(result);
        } catch (Exception e) {
            result.setStatusCode(StatusCode.SysException);
            param.setResult(result);

            getLogger().error("UserService " + e.getMessage());
            //系统异常
            throw new UDException(StatusCode.SysException,e);
        }


    }
}
