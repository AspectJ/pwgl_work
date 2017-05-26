package com.ydp.pwgl.service.venue;

import com.ydp.face.BaseService;
import com.ydp.face.IService;
import com.ydp.pwgl.entity.Venue;
import com.ydp.typedef.*;
import com.ydp.util.Dao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by john on 2017/4/26.
 */
@Service("venueService")
public class VenueService extends BaseService implements IService {

    public VenueService() {
        super(VenueService.class);
    }

    private Dao dao = null;

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

            //根据操作类型路由
            if (action.equals(Constant.ACTION_SEL)) {
                /** 查询场馆信息 */
                List<Venue> resultList = this.findAllVenue(param);

                result.setData(resultList);
                result.setStatusCode(StatusCode.Success);
                param.setResult(result);

            } else if (action.equals(Constant.ACTION_INS)) {
                /** 幂等性去重 */
                Integer repeatCount = this.checkRepeat(param);
                if (repeatCount != null && repeatCount > 0) {
                    result.setStatusCode(StatusCode.Venue_IS_Exsits);
                    param.setResult(result);
                    return;
                }

                /** 新增场馆信息 */
                this.addVenue(param);
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("venueid", param.get("venueid"));
                result.setData(resultMap);
                result.setStatusCode(StatusCode.Success);
                param.setResult(result);

            } else if (action.equals(Constant.ACTION_UPD)) {
                /** 更改场馆信息 */
                /* 幂等性去重 */
                Integer repeatCount = this.checkRepeat(param);
                if (repeatCount != null && repeatCount > 0) {
                    result.setStatusCode(StatusCode.Venue_IS_Exsits);
                    param.setResult(result);
                    return;
                }

                this.updateVenue(param);
                result.setStatusCode(StatusCode.Success);
                param.setResult(result);


            } else if (action.equals(Constant.Action_CHSTATUS)) {
                /** 更改场馆状态 */
                this.changeStatus(param);
                result.setStatusCode(StatusCode.Success);
                param.setResult(result);

            } else if (action.equals(Constant.ACTION_DEL)) {
                /** 删除场馆信息 */
                this.deleteVenue(param);
                result.setStatusCode(StatusCode.Success);
                param.setResult(result);
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
     * 查询所有可用场馆
     */
    public List<Venue> findAllVenue(Param param) {
        List<Venue> resultList = dao.getSqlSession().selectList("venue.findAllVenue", param);
        return resultList;
    }


    /**
     * 新增场馆信息
     */
    public void addVenue(Param param) {
        dao.getSqlSession().insert("venue.addVenue", param);
    }

    /**
     * 幂等性去重
     */
    public Integer checkRepeat(Param param) {
        return dao.getSqlSession().selectOne("venue.checkRepeat", param);
    }

    /**
     * 修改场馆信息
     * @param param
     */
    public void updateVenue(Param param) {
        dao.getSqlSession().update("venue.updateVenue", param);
    }

    /**
     * 更改场馆状态
     * @param param
     */
    public void changeStatus(Param param) {
        dao.getSqlSession().update("venue.changeStatus", param);
    }

    /**
     * 删除场馆信息
     * @param param
     */
    public void deleteVenue(Param param) {
        dao.getSqlSession().delete("venue.deleteVenue", param);
    }


}
