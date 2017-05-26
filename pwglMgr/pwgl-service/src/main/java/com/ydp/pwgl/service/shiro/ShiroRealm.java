package com.ydp.pwgl.service.shiro;

import com.ydp.face.BaseService;
import com.ydp.pwgl.service.user.UserService;
import com.ydp.typedef.Param;
import com.ydp.util.Dao;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShiroRealm extends AuthorizingRealm{

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	/**
	 * 用户认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		
		String username = upToken.getUsername();
		Param param = new Param();
		param.put("username", username);
		Dao dao = userService.getDao();
		Map<String,Object> userMap=dao.getSqlSession().selectOne("ptUser.select",param);

		if(userMap == null || userMap.isEmpty()){
			throw new UnknownAccountException("用户不存在");
		}else {
			String status = userMap.get("status") == null ? "0" : userMap.get("status").toString();
			String role_status = userMap.get("role_status") == null?"0":userMap.get("role_status").toString();
			if("0".equals(status)){
				throw new LockedAccountException("用户未被启用或未被审核");
			}
			/*if("0".equals(role_status)){
				throw new DisabledAccountException("用户所属角色状态不可用");
			}*/
		}
		//以下信息是从数据库中获取的.
		//1). principal: 认证的实体信息. 可以是 username, 也可以是数据表对应的用户的实体类对象. 
		Object principal = username;
		//2). credentials: 密码. 
		Object credentials = userMap.get("pass");
		//3). 盐值. 
		ByteSource credentialsSalt = ByteSource.Util.bytes(username);
		//4). realmName: 当前 realm 对象的 name. 调用父类的 getName() 方法即可
		String realmName = this.getName();
		
		//加盐
		AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
		return authenticationInfo;
	}
	
	/**
	 * 用户授权
	 */
	@SuppressWarnings({ "unchecked","rawtypes" })
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		String username=(String) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		Set<String> roleSet=null;
		//Set<String> permissionSet=new HashSet<String>();
		Dao dao = userService.getDao();
		Map<String,Object> roleMap=dao.getSqlSession().selectOne("ptUserRole.selectUserRole",username);
		if(roleMap !=null && !roleMap.isEmpty()){
			roleSet=new HashSet(roleMap.values());
			/*List<Map<String,Object>> permissionList=userDao.getPermission((Integer)roleMap.get("roleid"));
			for(Map<String,Object> permissionMap : permissionList){
				permissionSet.add((String)permissionMap.get("permission"));
			}*/
		}
		authorizationInfo.setRoles(roleSet);
		//authorizationInfo.setStringPermissions(permissionSet);
		return authorizationInfo;
	}


	/**
	 * 清除授权缓存
	 * @param principals
     */
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}


}
