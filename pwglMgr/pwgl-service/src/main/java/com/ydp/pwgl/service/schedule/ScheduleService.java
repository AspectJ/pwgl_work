package com.ydp.pwgl.service.schedule;

import com.ydp.face.BaseService;
import com.ydp.face.IService;
import com.ydp.pwgl.entity.Schedle;
import com.ydp.typedef.*;
import com.ydp.util.CodeUtil;
import com.ydp.util.Dao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 董亮 on 2017/5/5.
 */
@Service
public class ScheduleService extends BaseService implements IService {

    public ScheduleService() {
        super(ScheduleService.class);
    }

    Dao dao = null;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void service(Param param) throws Exception {
        this.dao = getDao();
        DataResult result = new DataResult();
        try {
            //从请求参数获取操作类型
            String action = param.get("action").toString();
            //写日志
            getLogger().debug(action);
            //从请求参数获取操作类型
            if (action == null) {
                result.setStatusCode(StatusCode.UndefinedParam);
                param.setResult(result);
                return;
            }
                if(action.equals(Constant.ACTION_SEL)) {
                    List<Schedle> schedules = getPlans(param);
                    result.setData(schedules);
                    result.setStatusCode(StatusCode.Success);
                    param.setResult(result);
                }

        }catch (Exception e) {
            result.setStatusCode(StatusCode.SysException);
            //系统异常
            throw new UDException(StatusCode.SysException, e);
        }
    }

    /**
     * 根据剧院显示排期信息
     * @param param
     */
    public  List<Schedle>    getPlans(Param param){
        String page = (String) param.get("page");
        String pagesize  = (String) param.get("pagesize");
        if(!CodeUtil.checkParam(page, pagesize)) {
            param.put("offsetNum", (Integer.parseInt(page) - 1) * Integer.parseInt(pagesize));		//从第多少条开始查询
            param.put("limitSize", Integer.parseInt(pagesize));	//每页查询数据条数：例如10条
        }
        List<Schedle>  ItemList=dao.getSqlSession().selectList("schedule.getPlans",param);
        return  ItemList;
    }

}
