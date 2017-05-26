package com.ydp.pwgl.wx.service;

import com.ydp.face.BaseService;
import com.ydp.face.IRedisClient;
import com.ydp.face.IService;
import com.ydp.typedef.*;
import com.ydp.util.CodeUtil;
import com.ydp.util.Dao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 座位服务
 */
@Service
public class SessnpewService extends BaseService implements IService {
    public SessnpewService() {
        super(SessnpewService.class);
    }

    @Override
    public void service(Param param) throws Exception {

        DataResult result = new DataResult();
        try{
            Dao dao = getDao();
            SqlSession sqlSession = dao.getSqlSession();
            String action = param.getString("action");
            if(CodeUtil.checkParam(action)){
                result.setStatusCode(StatusCode.MissingParams);
                param.setResult(result);
                return;
            }
            if(Constant.ACTION_SEL.equals(action)){
                //根据场次ID和分区ID查询分区座位
                String sessionsid = param.getString("sessionsid");
                String zoneid = param.getString("zoneid");
                if(CodeUtil.checkParam(sessionsid,zoneid)){
                    result.setStatusCode(StatusCode.MissingParams);
                    param.setResult(result);
                    return;
                }
                Map<String, Object> resultmap = new HashMap<String, Object>();
                IRedisClient redisClient = getRedisClient();
                //先从redis取缓存数据，若没有缓存数据，则从数据库取数组，再缓存到redis
                resultmap = (Map<String, Object>) redisClient.bget("pwgl:wx:sessionsid="+sessionsid);
                if(resultmap== null || resultmap.isEmpty()){


                    List resultlist=sqlSession.selectList("sessnpew.select", param);
                    //查询票价等级
                    List pricelevel = sqlSession.selectList("sessnpew.selectPriceLevel", param);

                    resultmap.put("pricelevel", pricelevel);
                    resultmap.put("site", resultlist);
                    redisClient.bset("pwgl:wx:sessnpew", resultmap);
                }

                result.setStatusCode(StatusCode.Success);
                result.setData(resultmap);
            }

            param.setResult(result);
        }catch (Exception e){
            result.setStatusCode(StatusCode.SysException);
            param.setResult(result);

            getLogger().error("UserService " + e.getMessage());
            //系统异常
            throw new UDException(StatusCode.SysException,e);
        }
    }
}
