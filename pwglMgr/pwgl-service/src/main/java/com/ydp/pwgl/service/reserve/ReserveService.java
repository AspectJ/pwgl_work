package com.ydp.pwgl.service.reserve;

import com.ydp.face.BaseService;
import com.ydp.face.IService;
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
@Service("reserveService")
public class ReserveService extends BaseService implements IService {

    public ReserveService() {
        super(ReserveService.class);
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
                /** 条件查询预留票种信息 */
                List<Map<String, Object>> resultList = this.selReserveByCriteria(param);
                Integer resultCount = this.selReserveByCriteriaCount(param);

                result.setStatusCode(StatusCode.Success);
                result.setData(resultList);
                result.setTotal(resultCount);
                param.setResult(result);

            } else if (action.equals(Constant.ACTION_INS)) {
                /** 验证重复性 */
                Integer repeatCount = this.checkRepeat(param);
                if (repeatCount != null && repeatCount > 0) {
                    result.setStatusCode(StatusCode.Reserve_IS_Exsits);
                    param.setResult(result);
                    return;
                }
                /** 新增预留票种 */
                this.addReserve(param);
                Map<String, Object> reserveMap = new HashMap<String, Object>();
                reserveMap.put("reserveid", param.get("reserveid"));

                result.setData(reserveMap);
                result.setStatusCode(StatusCode.Success);
                param.setResult(result);

            } else if (action.equals(Constant.ACTION_UPD)) {
                /** 验证重复性 */
                Integer repeatCount = this.checkRepeat(param);
                if (repeatCount != null && repeatCount > 0) {
                    result.setStatusCode(StatusCode.Reserve_IS_Exsits);
                    param.setResult(result);
                    return;
                }
                /** 修改预留票种信息 */
                this.updateReserve(param);
                result.setStatusCode(StatusCode.Success);
                param.setResult(result);

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
     * 条件查询预留票种信息
     * @param param
     */
    public List<Map<String, Object>> selReserveByCriteria(Param param) {
        /** 分页参数 */
        String page = (String) param.get("page");
        String pagesize  = (String) param.get("pagesize");
        if(!CodeUtil.checkParam(page, pagesize)) {
            param.put("offsetNum", (Integer.parseInt(page) - 1) * Integer.parseInt(pagesize));		//从第多少条开始查询
            param.put("limitSize", Integer.parseInt(pagesize));	//每页查询数据条数：例如10条
        }
        List<Map<String, Object>> resultList = dao.getSqlSession().selectList("reserve.selReserveByCriteria", param);
        return resultList;
    }

    /**
     * 查询count
     * @param param
     * @return
     */
    public Integer selReserveByCriteriaCount(Param param) {
        Integer resultCount = dao.getSqlSession().selectOne("reserve.selReserveByCriteriaCount", param);
        return resultCount;
    }


    /**
     * 新增预留票种
     * @param param
     */
    public void addReserve(Param param) {
        if(!param.containsKey("reservename") || !param.containsKey("issale")
                || !param.containsKey("status") || !param.containsKey("userid")) {
            result.setStatusCode(StatusCode.MissingParams);
            param.setResult(result);
            return;
        }
        dao.getSqlSession().insert("reserve.addReserve", param);
    }

    /**
     * 校验重复性
     * @param param
     * @return
     */
    public Integer checkRepeat(Param param) {
        if(!param.containsKey("reservename")) {
            result.setStatusCode(StatusCode.MissingParams);
            param.setResult(result);
            return null;
        }
        Integer repeatCount = dao.getSqlSession().selectOne("reserve.checkRepeat", param);
        return repeatCount;
    }


    /**
     * 更改预留票种信息
     * @param param
     */
    public void updateReserve(Param param) {
        if(!param.containsKey("reservename") || !param.containsKey("issale")
                || !param.containsKey("status") || !param.containsKey("userid")
                || !param.containsKey("reserveid")) {
            result.setStatusCode(StatusCode.MissingParams);
            param.setResult(result);
            return;
        }
        dao.getSqlSession().update("reserve.updateReserve", param);
    }

}
