package com.ydp.pwgl.wx.service;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ydp.face.BaseService;
import com.ydp.face.IService;
import com.ydp.typedef.Constant;
import com.ydp.typedef.DataResult;
import com.ydp.typedef.Param;
import com.ydp.typedef.StatusCode;
import com.ydp.typedef.UDException;
import com.ydp.util.CodeUtil;
import com.ydp.util.DateForamtUtil;

@Service
public class UserService extends BaseService implements IService {

	public UserService() {
		super(UserService.class);
	}

	@Override
	
	public void service(Param param) throws Exception {
		DataResult result = new DataResult();

		try {
			//从请求参数获取操作类型
            String action = param.get("action").toString();

            //根据操作类型路由
            if (action.equals(Constant.ACTION_SEL)) {
            	
            }else if (action.equals(Constant.ACTION_INS)){
            	
            }else if (action.equals(Constant.ACTION_DEL)){
            	
            }else if (action.equals(Constant.ACTION_UPD)){
            	
            }else{
            	result.setStatusCode(StatusCode.UndefinedAction);
            }
            
            param.setResult(result);
		} catch (Exception e) {
			result.setStatusCode(StatusCode.SysException);
			param.setResult(result);
			
			getLogger().error("UserService " + e.getMessage());
			//系统异常
			throw new UDException(StatusCode.SysException,e);
		}
	}
	
	/**
	 * 检测是否用户存在
	 * @param openid
	 * @return
	 */
	public Integer GetUserByOpenId(String openid){
		return getDao().getSqlSession().selectOne("UserMapper.getUserid", openid);
	}
	
	/**
	 * 新用户
	 * @param param
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer NewUser(Param param) {
		
		Integer n = 0;
		n = getDao().getSqlSession().insert("UserMapper.InsertUser", param);
		if (n == 0) return 0;
		
		Integer uid = param.getInteger("uid");		
		n = getDao().getSqlSession().insert("UserMapper.InsertExtendUser", param);
		
		return uid;
	}
	
	/**
	 * 写短信任务
	 * @param param
	 */
	public int InsertSmsTask(Param param) throws UDException{
		return  getDao().getSqlSession().insert("UserMapper.InsertSms", param);
	}
	
	/**
	 * 获取短信内容
	 * @param param
	 * @return
	 */
	public String GetSms(Param param){
		return getDao().getSqlSession().selectOne("UserMapper.GetSms",param);
	}
	
	/**
	 * 修改身份证信息
	 * @param param
	 */
	public int UpdateIdentifyNo(Param param){
		return getDao().getSqlSession().update("UserMapper.updateIdentifyNo", param);
	}
	
	/**
	 * 绑定会员信息
	 * @param param
	 * 返回新的userid
	 */
	public int BindUserGrade(Param param){
		Object obj = getDao().getSqlSession().selectOne("UserMapper.selUserId", param);
		if (obj == null) return 0;
		
		param.put("uidnew", obj.toString());
		return getDao().getSqlSession().update("UserMapper.bindUserGrade", param);
	}
	
	/**
	 * 删除短信内容
	 * @param param
	 * @return
	 */
	public int DelSms(String mobilenumber){
		return  getDao().getSqlSession().delete("UserMapper.DelSms",mobilenumber);
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	public void CheckLevel(Param param) throws Exception {

		long ltimestamp = Long.parseLong(param.getString("timestamp"));
		String smscreatetime = GetSms(param);
		if (!CodeUtil.checkParam(smscreatetime)) {
			Date dcurr = DateForamtUtil
					.to_yyyy_MM_dd_HH_mm_ss_date(smscreatetime);
			
			// 删除对应的短信task
			DelSms(param.getString("mobilenumber"));
			
			if (ltimestamp - dcurr.getTime() / 1000 <= 60000) {
				if (BindUserGrade(param)<=0)
					throw new UDException(StatusCode.BindMemberFailed);
			}else{
				throw new UDException(StatusCode.SmsContentTimeout);
			}
		} else {
			throw new UDException(StatusCode.SmsContentError);
		}
	}
	
}
