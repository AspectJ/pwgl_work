package com.ydp.pwgl.service.area;

import com.ydp.face.BaseService;
import com.ydp.face.IService;
import com.ydp.typedef.*;
import com.ydp.util.CodeUtil;
import com.ydp.util.Dao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by john on 2017/5/5.
 */
@Service
public class AreaService extends BaseService implements IService {

    public AreaService() {
        super(AreaService.class);
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

            /** 查询行政区信息 */
            //根据操作类型路由
            if (action.equals(Constant.ACTION_SEL)) {
                List<Map<String ,Object>> resultList = null;

                String level = (String) param.get("level");
                /**
                 * 如果存在参数level，说明是根据地区级别查询
                 * 如果不存在level，说明是根据条件查询所有行政区信息(不根据级别区分)
                 */
                //判断参数__省市区等级
                //条件查询所有行政区信息(不根据行政区级别)
                if(CodeUtil.checkParam(level)) {
                    resultList = this.findAreaByCriteria(param);
                    Integer resultCount = this.findAreaByCriteriaCount(param);
                    result.setData(resultList);
                    result.setTotal(resultCount);
                    result.setStatusCode(StatusCode.Success);
                    param.setResult(result);
                    return;
                }

                //根据行政区级别查询信息
                //查询省级
                if("province".equals(level)) {
                     resultList = this.findProvince(param);
                }
                //查询市级或县级必须还有参数__parentid
                else {
                    //判断参数__上级id(parentid)
                    if(!param.containsKey("parentid")) {
                        result.setStatusCode(StatusCode.MissingParams);
                        param.setResult(result);
                        return;
                    }
                    //查询市级
                    if("city".equals(level)) {
                        resultList = this.findCityByProID(param);
                    }
                    //查询县级
                    else if("county".equals(level)) {
                        resultList = this.findCountyByCityID(param);
                    }
                }


                result.setStatusCode(StatusCode.Success);
                result.setData(resultList);
                param.setResult(result);

            } else if (action.equals(Constant.ACTION_DEL)) {

            } else if(action.equals(Constant.ACTION_INS)) {

            } else if(action.equals(Constant.ACTION_UPD)) {
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
     * 条件查询地区信息(不根据地区级别)
     * @param param
     * @return
     */
    public List<Map<String, Object>> findAreaByCriteria(Param param) {
        String page = (String) param.get("page");
        String pagesize  = (String) param.get("pagesize");
        if(!CodeUtil.checkParam(page, pagesize)) {
            param.put("offsetNum", (Integer.parseInt(page) - 1) * Integer.parseInt(pagesize));		//从第多少条开始查询
            param.put("limitSize", Integer.parseInt(pagesize));	//每页查询数据条数：例如10条
        }
        return dao.getSqlSession().selectList("area.findAreaByCriteria", param);
    }

    /**
     * 查询COUNT
     * @param param
     * @return
     */
    public Integer findAreaByCriteriaCount(Param param) {
        return dao.getSqlSession().selectOne("area.findAreaByCriteriaCount", param);
    }

    /**
     * 查询省级
     * @param param
     */
    public List<Map<String, Object>> findProvince(Param param) {
        return dao.getSqlSession().selectList("area.findProvince");
    }

    /**
     * 根据省份id查询城市
     * @param param
     * @return
     */
    public List<Map<String, Object>> findCityByProID(Param param) {
        return dao.getSqlSession().selectList("area.findCityByProID", param);
    }

    /**
     * 根据城市id查询县级
     * @param param
     * @return
     */
    public List<Map<String, Object>> findCountyByCityID(Param param) {
        return dao.getSqlSession().selectList("area.findCountyByCityID", param);
    }


}
