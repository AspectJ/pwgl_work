package com.ydp.pwgl.service.pricelevel;

import com.ydp.face.BaseService;
import com.ydp.face.IService;
import com.ydp.pwgl.entity.PriceLevel;
import com.ydp.typedef.*;
import com.ydp.util.Dao;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by john on 2017/4/27.
 */
@Service
public class PriceLevelService extends BaseService implements IService {

    public PriceLevelService() {
        super(PriceLevelService.class);
    }

    Dao dao = null;

    @Override
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
                /** 查询全部票价等级 */
                List<PriceLevel> resultList = this.findAllPriceLevel();

                result.setData(resultList);
                result.setStatusCode(StatusCode.Success);
                param.setResult(result);


            } else if (action.equals(Constant.ACTION_DEL)) {
                /** 删除票价等级 */
                this.deletePriceLevelById(param);

                result.setStatusCode(StatusCode.Success);
                param.setResult(result);

            } else if (action.equals(Constant.ACTION_INS)) {
                /** 幂等性操作--去重 */
                Integer repeatCount = this.checkRepeat(param);
                if(repeatCount != null && repeatCount > 0) {
                    result.setStatusCode(StatusCode.PriceLevel_IS_Exsits);
                    param.setResult(result);
                    return;
                }
                /** 新增票价等级 */
                this.addPriceLevel(param);

                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("pricelevelid", param.get("priceLevelID"));
                result.setData(resultMap);

                result.setStatusCode(StatusCode.Success);
                param.setResult(result);

            } else if(action.equals(Constant.ACTION_UPD)) {
                /** 更改票价等级信息 */
                /** 幂等性操作--去重 */
                Integer repeatCount = this.checkRepeat(param);
                if(repeatCount != null && repeatCount > 0) {
                    result.setStatusCode(StatusCode.PriceLevel_IS_Exsits);
                    param.setResult(result);
                    return;
                }
                this.updatePriceLevelById(param);

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
     * 新增票价等级
     * @param param
     */
    public void addPriceLevel(Param param) {
        dao.getSqlSession().insert("priceLevel.addPriceLevel", param);
        /*System.out.println(param.get("priceLevelID"));*/
    }

    /**
     * 查询全部票价等级
     */
    public List<PriceLevel> findAllPriceLevel() {
        List<PriceLevel> resultList = dao.getSqlSession().selectList("priceLevel.findAllPriceLevel");
        return resultList;
    }

    /**
     * 更改票价等级信息
     * @param param
     */
    public void updatePriceLevelById(Param param) {
        dao.getSqlSession().update("priceLevel.updatePriceLevelById", param);
    }

    /**
     * 幂等性操作--去重
     * @param param
     * @return
     */
    public Integer checkRepeat(Param param) {
        Integer repeatCount = dao.getSqlSession().selectOne("priceLevel.checkRepeat", param);
        return repeatCount;
    }

    /**
     * 删除票价等级
     * @param param
     */
    public void deletePriceLevelById(Param param) {
        dao.getSqlSession().delete("priceLevel.deletePriceLevelById", param);
    }
}
