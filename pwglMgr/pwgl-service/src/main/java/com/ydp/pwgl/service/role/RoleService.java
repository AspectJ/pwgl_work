package com.ydp.pwgl.service.role;

import com.ydp.face.BaseService;
import com.ydp.face.IService;
import com.ydp.pwgl.service.shiro.ShiroFilterChainManager;
import com.ydp.typedef.*;
import com.ydp.util.CodeUtil;
import com.ydp.util.Dao;
import com.ydp.util.DateForamtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RoleService extends BaseService implements IService{

    @Autowired
    private ShiroFilterChainManager shiroFilterChainManager;

    public RoleService() {
        super(RoleService.class);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void service(Param param) throws Exception {

        Dao dao = getDao();
        DataResult result = new DataResult();
        try{
            //从请求参数获取操作类型
            String action = param.get("action").toString();
            if (action == null){
                result.setStatusCode(StatusCode.UndefinedParam);
                param.setResult(result);
                return;
            }

            //开启redis连接 选择0 索引库,默认为0库，可以不调用
            //getRedisClient().open(0);

			/*
			Set<String> list = getRedisClient().keys("*");

		    Object keys[] = list.toArray();
		    for (int i=0;i<keys.length;i++){
		    	if (keys[i].equals("userrole"))
		         System.out.println("List of stored keys:: "+ keys[i].toString());
		    }
		*/

            Date now = new Date();
            //根据操作类型路由
            //查询角色
            if (action.equals(Constant.ACTION_SEL)){
                List resultlist=dao.getSqlSession().selectList("ptRole.select", param);
                result.setData(resultlist);
                result.setStatusCode(StatusCode.Success);

                //删除角色
            }else if (action.equals(Constant.ACTION_DEL)){
                dao.getSqlSession().delete("ptRole.delete", param);
                result.setStatusCode(StatusCode.Success);
                //创建角色
            }else if(action.equals(Constant.ACTION_INS)){
                String rolename = param.getString("rolename");
                String roles = param.getString("roles");
                String roletype = param.getString("roletype");
                if(CodeUtil.checkParam(rolename,roles,roletype)){
                    result.setStatusCode(StatusCode.UndefinedParam);
                }
                param.put("cid", "");
                param.put("ctime", DateForamtUtil.to_yyyy_MM_dd_HH_mm_ss_str(now));
                dao.getSqlSession().insert("ptRole.insert", param);
                result.setStatusCode(StatusCode.Success);

            }else if(action.equals(Constant.ACTION_UPD)){
                param.put("mtime", DateForamtUtil.to_yyyy_MM_dd_HH_mm_ss_str(now));
                param.put("mid", "");
                dao.getSqlSession().update("ptRole.update", param);
                result.setStatusCode(StatusCode.Success);
            }else{
                result.setStatusCode(StatusCode.UndefinedAction);
            }
            //将结果通过param返回
            param.setResult(result);
        }catch (Exception e){
            result.setStatusCode(StatusCode.SysException);
            param.setResult(result);

            getLogger().error("RoleService " + e.getMessage());

            //系统异常
            throw new UDException(StatusCode.SysException,e);
        }

    }
}
