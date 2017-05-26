package com.ydp.pwgl.service.Channel;

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
@Service("channelService")
public class ChannelService extends BaseService implements IService {

    public ChannelService() {
        super(ChannelService.class);
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
                /** 查询渠道商 */
                List<Map<String, Object>> resultList = this.findChannel(param);
                Integer resultCount = this.findChannelCount(param);
                result.setStatusCode(StatusCode.Success);
                result.setData(resultList);
                result.setTotal(resultCount);
                param.setResult(result);

            } else if (action.equals(Constant.ACTION_INS)) {
                /** 去重 */
                Integer repeatCount = this.checkRepeat(param);
                if (repeatCount != null && repeatCount > 0) {
                    result.setStatusCode(StatusCode.Channel_IS_Exsits);
                    param.setResult(result);
                    return;
                }

                /** 新增渠道商 */
                this.addChannel(param);
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("channelid", (Integer) param.get("channelid"));

                result.setData(resultMap);
                result.setStatusCode(StatusCode.Success);
                param.setResult(result);

            } else if (action.equals(Constant.ACTION_UPD)) {
                /** 去重 */
                Integer repeatCount = this.checkRepeat(param);
                if (repeatCount != null && repeatCount > 0) {
                    result.setStatusCode(StatusCode.Channel_IS_Exsits);
                    param.setResult(result);
                    return;
                }

                /** 修改渠道商信息 */
                this.updateChannel(param);

                result.setStatusCode(StatusCode.Success);
                param.setResult(result);

            } else if (action.equals(Constant.Action_CHSTATUS)) {

            } else if (action.equals(Constant.ACTION_DEL)) {
                /** 删除渠道商 */
                this.deleteChannel(param);
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
     * 渠道商查询
     * @return
     */
    public List<Map<String, Object>> findChannel(Param param) {
        /** 分页 */
        String page = (String) param.get("page");
        String pagesize  = (String) param.get("pagesize");
        if(!CodeUtil.checkParam(page, pagesize)) {
            param.put("offsetNum", (Integer.parseInt(page) - 1) * Integer.parseInt(pagesize));		//从第多少条开始查询
            param.put("limitSize", Integer.parseInt(pagesize));	//每页查询数据条数：例如10条
        }
        List<Map<String, Object>> resultList = dao.getSqlSession().selectList("channel.findChannel");
        return resultList;
    }


    /**
     * 查询count
     * @param param
     * @return
     */
    public Integer findChannelCount(Param param) {
        Integer resultCount = dao.getSqlSession().selectOne("channel.findChannelCount", param);
        return resultCount;
    }

    /**
     * 新增渠道
     */
    public void addChannel(Param param) {
        /** 缺少参数 */
        if(!param.containsKey("channelname") || !param.containsKey("userid")) {
            result.setStatusCode(StatusCode.MissingParams);
            param.setResult(result);
            return;
        }
        dao.getSqlSession().insert("channel.addChannel", param);
    }


    /**
     * 修改渠道
     * @param param
     */
    public void updateChannel(Param param) {
        /** 缺少参数 */
        if(!param.containsKey("channelname") || !param.containsKey("userid") || !param.containsKey("channelid")) {
            result.setStatusCode(StatusCode.MissingParams);
            param.setResult(result);
            return;
        }
        dao.getSqlSession().update("channel.updateChannel", param);
    }

    /**
     * 查重
     * @param param
     * @return
     */
    public Integer checkRepeat(Param param) {
        Integer repeatCount = dao.getSqlSession().selectOne("channel.checkRepeat", param);
        return repeatCount;
    }

    /**
     * 删除渠道商
     * @param param
     */
    public void deleteChannel(Param param) {
        /** 缺少参数 */
        if(!param.containsKey("channelid")) {
            result.setStatusCode(StatusCode.MissingParams);
            param.setResult(result);
            return;
        }
        dao.getSqlSession().delete("channel.deleteChannel", param);
    }

}
