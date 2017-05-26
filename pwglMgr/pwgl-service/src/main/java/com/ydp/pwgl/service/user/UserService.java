package com.ydp.pwgl.service.user;

import com.ydp.face.BaseService;
import com.ydp.face.IService;
import com.ydp.typedef.*;
import com.ydp.util.CodeUtil;
import com.ydp.util.Dao;
import com.ydp.util.DateForamtUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService implements IService{

    public UserService() {
        super(UserService.class);
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
                //查询用户
            if (action.equals(Constant.ACTION_SEL)){
                List lst = dao.getSqlSession().selectList("ptUser.select",param);
                result.setData(lst);
                result.setStatusCode(StatusCode.Success);
                //删除用户
            }else if (action.equals(Constant.ACTION_DEL)){
                dao.getSqlSession().delete("ptUser.remove", param);
                result.setStatusCode(StatusCode.Success);
                //创建用户
            }else if(action.equals(Constant.ACTION_INS)){
                String username = param.getString("username");
                String pass = param.getString("pass");
                String realname = param.getString("realname");
                String telephone = param.getString("telephone");
                //检查参数
                if(CodeUtil.checkParam(username,pass,realname,telephone)){
                    result.setStatusCode(StatusCode.UndefinedParam);
                    param.setResult(result);
                    return;
                }
                //密码加密，明文密码+用户名
                param.put("pass", CodeUtil.MD5_Encrypt(username,pass));
                String ctime = DateForamtUtil.to_yyyy_MM_dd_HH_mm_ss_str(now);
                param.put("ctime", ctime);
                //param.put("cid", );

                //查询用户名或手机号是否已经注册
                Map<String, Object> paramsmap = new HashMap<String, Object>();
                paramsmap.put("username", username);
                paramsmap.put("telephone", telephone);
                List<Map<String,Object>> resultlist=dao.getSqlSession().selectList("ptUser.select", paramsmap);
                if(resultlist!=null && !resultlist.isEmpty()){
                    for (Map<String, Object> map : resultlist) {
                        String name = map.get("username") == null ? "" : map.get("username").toString();
                        if(!CodeUtil.checkParam(name)){
                            result.setStatusCode(StatusCode.UsernameRegistered);
                            param.setResult(result);
                            return;
                        }
                        String phone = map.get("telephone") == null ? "" : map.get("telephone").toString();
                        if(!CodeUtil.checkParam(phone)){
                            result.setStatusCode(StatusCode.TelephoneRegistered);
                            param.setResult(result);
                            return;
                        }
                    }
                }
                dao.getSqlSession().insert("ptUser.save",param);
                result.setStatusCode(StatusCode.Success);
                //更新用户
            }else if(action.equals(Constant.ACTION_UPD)){
                String pass = param.getString("pass");
                String username = param.getString("username");
                //若只修改密码，从数据库查询用户名，作为加密的盐
                if(CodeUtil.checkParam(username)){
                    Map<String,Object> resultMap= dao.getSqlSession().selectOne("ptUser.select", param);
                    username = resultMap.get("username").toString();
                }else {
                    //检查重名
                    Map<String, Object> paramsmap = new HashMap<String, Object>();
                    paramsmap.put("username", username);
                    List list = dao.getSqlSession().selectList("ptUser.select", paramsmap);
                    if(list!=null &&list.size()>0){
                        result.setStatusCode(StatusCode.UsernameRegistered);
                        param.setResult(result);
                        return;
                    }
                }
                //修改密码
                if(!CodeUtil.checkParam(pass)){
                    param.put("pass", CodeUtil.MD5_Encrypt(username,pass));
                }
                dao.getSqlSession().update("ptUser.update", param);
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

    public void login(Param param){
        Dao dao = getDao();
        DataResult result = new DataResult();

        Map<String, Object> userMap = null;
        try {
            SqlSession sqlSession = dao.getSqlSession();
            Date now = new Date();

            String username = param.get("username")==null?"":param.get("username").toString();
            String pass = param.get("pass")==null?"":param.get("pass").toString();
            if (CodeUtil.checkParam(username, pass)) {
                result.setStatusCode(StatusCode.UsernameOrPassNotNull);
                param.setResult(result);
                return;
            }
            username = URLDecoder.decode(username, "UTF-8");
            Subject subject = SecurityUtils.getSubject();
            //若已经登录了，则登出，重新登录
            if (subject.isAuthenticated()) {
                subject.logout();
            }
            UsernamePasswordToken token = new UsernamePasswordToken(username, pass);
            try {
                subject.login(token);
                // 查询该用户信息
                userMap = sqlSession.selectOne("ptUser.select", param);
                Session session = subject.getSession(false);
                session.setAttribute("userid", userMap.get("userid"));
                session.setAttribute("username", username);

                //更改该用户的最后一次登录时间
                Map<String, Object> paramsMap = new HashMap<String, Object>();
                paramsMap.put("lasttime", DateForamtUtil.to_yyyy_MM_dd_HH_mm_ss_str(now));
                paramsMap.put("mtime", DateForamtUtil.to_yyyy_MM_dd_HH_mm_ss_str(now));
                paramsMap.put("userid", userMap.get("userid"));
                sqlSession.update("ptUser.update", paramsMap);

                //查询当前用户的角色信息及其权限
               /* Role role = roleDao.findRoleByRoleid((int) userMap.get("roleid"));
                userMap.put("role", role);

                resultJson.put("data", userMap);*/

            } catch (UnknownAccountException uae) { // 用户不存在
                result.setStatusCode(StatusCode.UsernameNotExsits);
            } catch (IncorrectCredentialsException ice) { // 密码错误
                result.setStatusCode(StatusCode.UsernameOrPassError);
            } catch (LockedAccountException lae) { // 用户被锁定
                result.setStatusCode(StatusCode.UserStatusFail);
            } catch (DisabledAccountException dae) {//用户所属角色状态不可用
                result.setStatusCode(StatusCode.UserRoleStatusFail);
            } catch (AuthenticationException ae) {
                result.setStatusCode(StatusCode.UserLoginFail);
            }
            param.setResult(result);
        }catch (Exception e){
            result.setStatusCode(StatusCode.SysException);
            param.setResult(result);

            getLogger().error("UserService " + e.getMessage());

            //系统异常
            throw new UDException(StatusCode.SysException,e);
        }

    }

}
