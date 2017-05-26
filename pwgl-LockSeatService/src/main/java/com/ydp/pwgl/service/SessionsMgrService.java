package com.ydp.pwgl.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ydp.face.BaseService;
import com.ydp.face.IService;
import com.ydp.pwgl.constant.Constant;
import com.ydp.pwgl.dubbo.entity.Seat;
import com.ydp.pwgl.dubbo.faces.ISessionsMgrService;
import com.ydp.typedef.DataResult;
import com.ydp.typedef.Param;
import com.ydp.typedef.StatusCode;
import com.ydp.util.CodeUtil;
import com.ydp.util.DateForamtUtil;

@Service
public class SessionsMgrService extends BaseService implements IService,
		ISessionsMgrService {
	
	@Autowired
	private LockSeatApiRedisService lockSeatApiRedisService;

	public SessionsMgrService() {
		super(SessionsMgrService.class);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 加载排期座位表至缓存(后台调用)
	 * 
	 * @param sessionsid
	 * @param endtime
	 *            :过期时间,设置为场次结束时间
	 */
	@Override
	public DataResult InitSeatMap(String sessionsid) {
		DataResult result = new DataResult();
		
		//查询sessins信息
		Param param = new Param();
		param.put("sessionsid", sessionsid);
		param.put("status","1");
		param.put("delstatus","0");
		
		List sessions = getDao().getSqlSession().selectList("LocakSeatApiMapper.getSessions", param);

		//result.setStatusCode(StatusCode.Success);
		
		if (sessions == null  || sessions.size() == 0) {
			result.setStatusCode(StatusCode.Sessions_ISNOT_Exsits);
			return result;
		}
		
		//售票结束时间
		String endtime = String.valueOf(((Map)sessions.get(0)).get("endtime"));
		List<Seat> lst = getDao().getSqlSession().selectList(
				"LocakSeatApiMapper.getSeatBySessionsId", sessionsid);
		if (lst != null) {
			Date dend;
			try {
				dend = DateForamtUtil.to_yyyy_MM_dd_HH_mm_ss_date(endtime);
			} catch (ParseException e) {
				getLogger().error("InitSeatMap:{}", e.getMessage());
				return result;
			}

			int expired = (int) (dend.getTime() / 1000);
			lockSeatApiRedisService.InitSeatMap(sessionsid, lst, expired);
			result.setStatusCode(StatusCode.Success);
			getLogger().error("初始化获取座位排期数据:成功,过期时间为:{}秒", expired);
		} else {
			result.setStatusCode(StatusCode.SessionsSeatNull);
			getLogger().error("初始化获取座位排期数据:{}", StatusCode.SessionsSeatNull);
		}

		return result;
	}

	/**
	 * 后台发起演出结束命令,清空缓存
	 */
	@Override
	public DataResult DelSeatMap(String sessionsid) {
		boolean ret = lockSeatApiRedisService.DelSeatMap(sessionsid);
		SessionsStateOnChange(sessionsid,"0");
		return new DataResult(ret?StatusCode.Success:StatusCode.SessionsSeatDelDailed);
	}

	@Override
	public void service(Param param) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public DataResult SessionsStateOnChange(String sessionsid, String state) {
		if (state.equals("1"))
			getRedisClient().sadd(Constant.RUNNINGSESSIONS, sessionsid);
		else
			getRedisClient().srem(Constant.RUNNINGSESSIONS, sessionsid);
		
		return new DataResult(StatusCode.Success);
	}

}
