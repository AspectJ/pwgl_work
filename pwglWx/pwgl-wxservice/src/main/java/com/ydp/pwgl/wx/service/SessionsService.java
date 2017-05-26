package com.ydp.pwgl.wx.service;

import com.ydp.face.BaseService;
import com.ydp.face.IService;
import com.ydp.typedef.*;
import com.ydp.util.CodeUtil;
import com.ydp.util.Dao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 场次服务
 */
@Service
public class SessionsService extends BaseService implements IService{

    public SessionsService() {
        super(SessionsService.class);
    }

    @Override
    public void service(Param param) throws Exception {
        DataResult result = new DataResult();
        Dao dao = getDao();
        try {
            String action = param.getString("action");
            if(CodeUtil.checkParam(action)){
                result.setStatusCode(StatusCode.MissingParams);
                param.setResult(result);
                return;
            }
            if(Constant.ACTION_SEL.equals(action)){
                List list=dao.getSqlSession().selectList("sessions.select", param);
            }
        }catch (Exception e){
            result.setStatusCode(StatusCode.SysException);
            param.setResult(result);

            getLogger().error("UserService " + e.getMessage());
            //系统异常
            throw new UDException(StatusCode.SysException,e);

        }
    }
}
