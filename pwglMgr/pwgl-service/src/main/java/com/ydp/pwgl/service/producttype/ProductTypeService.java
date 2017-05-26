package com.ydp.pwgl.service.producttype;

import com.ydp.face.BaseService;
import com.ydp.face.IService;
import com.ydp.pwgl.entity.ProductType;
import com.ydp.typedef.*;
import com.ydp.util.Dao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by john on 2017/4/26.
 */
@Service
public class ProductTypeService extends BaseService implements IService {

    public ProductTypeService() {
        super(ProductTypeService.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void service(Param param) throws Exception {
        Dao dao = getDao();
        DataResult result = new DataResult();

        try {
            //从请求参数获取操作类型
            String action = param.get("action").toString();

            //写日志
            getLogger().debug(action);

            //根据操作类型路由
            if (action.equals(Constant.ACTION_SEL)) {

                List<ProductType> resultList = this.findProductType(dao);

                result.setData(resultList);
                result.setStatusCode(StatusCode.Success);
                param.setResult(result);


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


    public List<ProductType> findProductType(Dao dao) {
        List<ProductType> resultList = dao.getSqlSession().selectList("ptProductType.findProductType");
        return resultList;
    }
}
