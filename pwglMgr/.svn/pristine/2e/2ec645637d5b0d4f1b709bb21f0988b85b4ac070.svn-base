package com.ydp.pwgl.setpolicy;

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
@Service("setPolicyService")
public class SetPolicyService extends BaseService implements IService {

    public SetPolicyService() {
        super(SetPolicyService.class);
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
                /** 条件查询优惠策略 */
                List<Map<String, Object>> resultList = this.findSetPolicyByCriteria(param);
                Integer resultCount = this.findSetPolicyByCriteriaCount(param);

                result.setData(resultList);
                result.setTotal(resultCount);
                result.setStatusCode(StatusCode.Success);
                param.setResult(result);

            } else if (action.equals(Constant.ACTION_INS)) {
                /** 验证重复性 */
                Integer repeatCount = this.checkRepeat(param);
                if (repeatCount != null && repeatCount > 0) {
                    result.setStatusCode(StatusCode.SetPolicy_IS_Exsits);
                    param.setResult(result);
                    return;
                }

                /** 新增销售策略 */
                this.addSetPolicy(param);
                Map<String, Object> setPolicyMap = new HashMap<String, Object>();
                setPolicyMap.put("setpolicyid", param.get("setpolicyid"));

                result.setData(setPolicyMap);
                result.setStatusCode(StatusCode.Success);
                param.setResult(result);

            } else if (action.equals(Constant.ACTION_UPD)) {
                /** 验证重复性 */
                Integer repeatCount = this.checkRepeat(param);
                if (repeatCount != null && repeatCount > 0) {
                    result.setStatusCode(StatusCode.SetPolicy_IS_Exsits);
                    param.setResult(result);
                    return;
                }

                /** 修改销售策略 */
                this.updateSetPolicy(param);
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
     * 条件查询销售策略
     * @param param
     * @return
     */
    public List<Map<String, Object>> findSetPolicyByCriteria(Param param) {
        /** 分页参数 */
        String page = (String) param.get("page");
        String pagesize  = (String) param.get("pagesize");
        if(!CodeUtil.checkParam(page, pagesize)) {
            param.put("offsetNum", (Integer.parseInt(page) - 1) * Integer.parseInt(pagesize));		//从第多少条开始查询
            param.put("limitSize", Integer.parseInt(pagesize));	//每页查询数据条数：例如10条
        }
        List<Map<String, Object>> resultList = dao.getSqlSession().selectList("setPolicy.findSetPolicyByCriteria", param);
        return resultList;
    }

    /**
     * 查询count
     * @param param
     * @return
     */
    public Integer findSetPolicyByCriteriaCount(Param param) {
        Integer resultCount = dao.getSqlSession().selectOne("setPolicy.findSetPolicyByCriteriaCount", param);
        return resultCount;
    }


    /**
     * 新增销售策略
     * @param param
     */
    public void addSetPolicy(Param param) {
        if(!param.containsKey("policyname") || !param.containsKey("venueid") || !param.containsKey("type")
                || !param.containsKey("rule") || !param.containsKey("user_gradeid") || !param.containsKey("limit_num")
                || !param.containsKey("status") || !param.containsKey("sold_num") || !param.containsKey("all_num")
                || !param.containsKey("setshow") || !param.containsKey("starttime") || !param.containsKey("endtime")
                || !param.containsKey("userid")) {

            result.setStatusCode(StatusCode.MissingParams);
            param.setResult(result);
            return;
        }
        dao.getSqlSession().insert("setPolicy.addSetPolicy", param);
    }


    /**
     * 验证重复性
     * @param param
     * @return
     */
    public Integer checkRepeat(Param param) {
        if(!param.containsKey("policyname")) {
            result.setStatusCode(StatusCode.MissingParams);
            param.setResult(result);
            return null;
        }
        Integer repeatCount = dao.getSqlSession().selectOne("setPolicy.checkRepeat", param);
        return repeatCount;
    }


    /**
     * 更改销售策略
     * @param param
     */
    private void updateSetPolicy(Param param) {
        if(!param.containsKey("policyname") || !param.containsKey("venueid") || !param.containsKey("type")
                || !param.containsKey("rule") || !param.containsKey("user_gradeid") || !param.containsKey("limit_num")
                || !param.containsKey("status") || !param.containsKey("sold_num") || !param.containsKey("all_num")
                || !param.containsKey("setshow") || !param.containsKey("starttime") || !param.containsKey("endtime")
                || !param.containsKey("userid") || !param.containsKey("setpolicyid")) {

            result.setStatusCode(StatusCode.MissingParams);
            param.setResult(result);
            return;
        }
        dao.getSqlSession().update("setPolicy.updateSetPolicy", param);
    }



}
