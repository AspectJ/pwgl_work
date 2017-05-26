package com.ydp.pwgl.service.ticketface;

import com.ydp.face.BaseService;
import com.ydp.face.IService;
import com.ydp.typedef.*;
import com.ydp.util.CodeUtil;
import com.ydp.util.Dao;
import com.ydp.util.IdUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by john on 2017/5/10.
 */
@Service
public class TicketFaceService extends BaseService implements IService {

    public TicketFaceService() {
        super(TicketFaceService.class);
    }

    private Dao dao;
    DataResult result;

    @Transactional(rollbackFor = Exception.class)
    @Override
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
                /** 条件查询票样信息 */
                List<Map<String, Object>> resultList = this.selTicketFaceByCriteria(param);
                Integer resultCount = this.selTicketFaceByCriteriaCount(param);
                result.setStatusCode(StatusCode.Success);
                result.setData(resultList);
                result.setTotal(resultCount);
                param.setResult(result);

            } else if (action.equals(Constant.ACTION_DEL)) {

            } else if(action.equals(Constant.ACTION_INS)) {
                /** 去重 */
                Integer repeatCount = this.checkRepeat(param);
                if (repeatCount != null && repeatCount > 0) {
                    result.setStatusCode(StatusCode.TicketFace_IS_Exsits);
                    param.setResult(result);
                    return;
                }
                /** 新建票样 */
                String ticketfaceid = this.addTicketFace(param);
                result.setStatusCode(StatusCode.Success);
                result.setData(new HashMap().put("ticketfaceid", ticketfaceid));
                param.setResult(result);

            } else if(action.equals(Constant.ACTION_UPD)) {
                /** 更改票样 */
                this.updateTicketFace(param);
                result.setStatusCode(StatusCode.Success);
                param.setResult(result);

            }
            else {
                result.setStatusCode(StatusCode.UndefinedAction);
            }
        } catch (Exception e) {
            result.setStatusCode(StatusCode.SysException);
            //系统异常
            throw new UDException(StatusCode.SysException, e);
        }
    }


    /**
     * 条件查询票样信息
     * @param param
     */
    public List<Map<String, Object>> selTicketFaceByCriteria(Param param) {
        //分页参数
        String page = (String) param.get("page");
        String pagesize  = (String) param.get("pagesize");
        if(!CodeUtil.checkParam(page, pagesize)) {
            param.put("offsetNum", (Integer.parseInt(page) - 1) * Integer.parseInt(pagesize));		//从第多少条开始查询
            param.put("limitSize", Integer.parseInt(pagesize));	//每页查询数据条数：例如10条
        }
        List<Map<String, Object>> resultList = dao.getSqlSession().selectList("ticketface.selTicketFaceByCriteria", param);
        return resultList;
    }

    /**
     * 查询count
     * @param param
     * @return
     */
    public Integer selTicketFaceByCriteriaCount(Param param) {
        Integer resultCount = dao.getSqlSession().selectOne("ticketface.selTicketFaceByCriteriaCount", param);
        return resultCount;
    }

    /**
     * 新建票样
     */
    public String addTicketFace(Param param) {
        /** 校验参数 */
        if(!param.containsKey("ticketname") || !param.containsKey("height") || !param.containsKey("width")
                || !param.containsKey("venueid") || !param.containsKey("bgurl") || !param.containsKey("content")
                || !param.containsKey("userid")) {
            result.setStatusCode(StatusCode.MissingParams);
            param.setResult(result);
            return null;
        }
        //票版id
        String ticketfaceid = IdUtil.createThirteenId();
        param.put("ticketfaceid", ticketfaceid);
        dao.getSqlSession().insert("ticketface.addTicketFace", param);
        return ticketfaceid;
    }


    /**
     * 幂等性去重
     * @param param
     * @return
     */
    public Integer checkRepeat(Param param) {
        if(!param.containsKey("ticketname")) {
            result.setStatusCode(StatusCode.MissingParams);
            param.setResult(result);
            return null;
        }
        Integer repeatCount = dao.getSqlSession().selectOne("ticketface.checkRepeat", param);
        return repeatCount;
    }

    /**
     * 更改票样
     * @param param
     */
    public void updateTicketFace(Param param) {
        /** 校验参数 */
        /** 校验参数 */
        if(!param.containsKey("ticketname") || !param.containsKey("height") || !param.containsKey("width")
                || !param.containsKey("venueid") || !param.containsKey("bgurl") || !param.containsKey("content")
                || !param.containsKey("userid") || !param.containsKey("ticketfaceid")) {
            result.setStatusCode(StatusCode.MissingParams);
            param.setResult(result);
            return;
        }

        dao.getSqlSession().update("ticketface.updateTicketFace", param);
    }

}
