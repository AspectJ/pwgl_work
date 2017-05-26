package com.ydp.pwgl.service.roleMenu;

import com.ydp.face.BaseController;
import com.ydp.face.BaseService;
import com.ydp.face.IService;
import com.ydp.pwgl.service.role.UrlFilter;
import com.ydp.pwgl.service.shiro.ShiroFilterChainManager;
import com.ydp.typedef.*;
import com.ydp.util.CodeUtil;
import com.ydp.util.Dao;
import com.ydp.util.DateForamtUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Service
public class RoleMenuService extends BaseService implements IService {


    @Autowired
    private ShiroFilterChainManager shiroFilterChainManager;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private SqlSessionFactory sqlSessionFactory;


    public RoleMenuService() {
        super(RoleMenuService.class);
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

            Date now = new Date();
            //根据操作类型路由
            //查询角色权限
            if (action.equals(Constant.ACTION_SEL)){
                List resultlist=dao.getSqlSession().selectList("ptRoleMenu.select", param);
                result.setData(resultlist);
                result.setStatusCode(StatusCode.Success);

                //删除角色权限
            }else if (action.equals(Constant.ACTION_DEL)){
                dao.getSqlSession().delete("ptRole.delete", param);
                result.setStatusCode(StatusCode.Success);
                //角色赋予权限
            }else if(action.equals(Constant.ACTION_INS)){
                String roleid = param.getString("roleid");
                String menuid = param.getString("menuid");
                if(CodeUtil.checkParam(roleid,menuid)){
                    result.setStatusCode(StatusCode.UndefinedParam);
                }
                String[] menuids = menuid.split(",");
                //先删除，在添加
                SqlSession sqlSession = dao.getSqlSession();
                sqlSession.delete("ptRoleMenu.delete", param);
                param.put("menuids", menuids);
                param.put("cid", "");
                param.put("ctime", DateForamtUtil.to_yyyy_MM_dd_HH_mm_ss_str(now));
                sqlSession.insert("ptRoleMenu.insert", param);
                //注册url拦截器到shiro
                initFilterChain();
                result.setStatusCode(StatusCode.Success);

            }else{
                result.setStatusCode(StatusCode.UndefinedAction);
            }
            //将结果通过param返回
            param.setResult(result);
        }catch (Exception e){
            result.setStatusCode(StatusCode.SysException);
            param.setResult(result);

            getLogger().error("OrderService " + e.getMessage());

            //系统异常
            throw new UDException(StatusCode.SysException,e);
        }
    }

    /**
     * 注册拦截器到shiro，spring容器启动或对角色权限进行增删改时会生效
     */
    @PostConstruct
    public void initFilterChain() {

        List<UrlFilter> urlFilters= null;
        SqlSession sqlSession = null;
        try {
            sqlSession= sqlSessionFactory.openSession();
            urlFilters = sqlSession.selectList("ptRoleMenu.selectRoleUrl");
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
        shiroFilterChainManager.initFilterChains(urlFilters);
    }
}
