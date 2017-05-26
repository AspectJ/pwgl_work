package com.ydp.pwgl.service.hall;

import com.ydp.face.BaseService;
import com.ydp.face.IService;
import com.ydp.pwgl.entity.Venue;
import com.ydp.typedef.*;
import com.ydp.util.CodeUtil;
import com.ydp.util.Dao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by john on 2017/4/26.
 */
@Service("hallService")
public class HallService extends BaseService implements IService {

    public HallService() {
        super(HallService.class);
    }

    private Dao dao = null;
    DataResult result = null;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void service(Param param) throws Exception {
        this.dao = getDao();
        result = new DataResult();

        try {
            //从请求参数获取操作类型
            String action = param.get("action").toString();

            //写日志
            getLogger().debug(action);

            //根据操作类型路由
            if (action.equals(Constant.ACTION_SEL)) {
                /** 条件查询演出厅信息 */
                List<Map<String, Object>> resultList = this.findHallByCriteria(param);
                Integer resultCount = this.findHallByCriteriaCount(param);
                result.setData(resultList);
                result.setTotal(resultCount);
                result.setStatusCode(StatusCode.Success);
                param.setResult(result);

            } else if (action.equals(Constant.ACTION_INS)) {

            } else if (action.equals(Constant.ACTION_UPD)) {

            } else if (action.equals(Constant.Action_CHSTATUS)) {

            } else if (action.equals(Constant.ACTION_DEL)) {

            } else {
                result.setStatusCode(StatusCode.UndefinedAction);
            }
        } catch (Exception e) {
            result.setStatusCode(StatusCode.SysException);
            //系统异常
            throw new UDException(StatusCode.SysException, e);
        }
    }


    /**
     * 条件查询演出厅信息
     */
    public List<Map<String, Object>> findHallByCriteria(Param param) {
        String page = (String) param.get("page");
        String pagesize  = (String) param.get("pagesize");
        if(!CodeUtil.checkParam(page, pagesize)) {
            param.put("offsetNum", (Integer.parseInt(page) - 1) * Integer.parseInt(pagesize));		//从第多少条开始查询
            param.put("limitSize", Integer.parseInt(pagesize));	//每页查询数据条数：例如10条
        }
        System.out.println(param.get("hallname"));
        List<Map<String, Object>> resultList = dao.getSqlSession().selectList("hall.findHallByCriteria", param);
        return resultList;
    }

    /**
     * 查询count
     * @param param
     * @return
     */
    public Integer findHallByCriteriaCount(Param param) {
        Integer resultCount = dao.getSqlSession().selectOne("hall.findHallByCriteriaCount", param);
        return resultCount;
    }




}
