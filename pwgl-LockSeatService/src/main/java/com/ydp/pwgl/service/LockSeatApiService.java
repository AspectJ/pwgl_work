package com.ydp.pwgl.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ydp.face.BaseService;
import com.ydp.face.IService;
import com.ydp.pwgl.constant.Constant;
import com.ydp.pwgl.dubbo.entity.Seat;
import com.ydp.pwgl.dubbo.faces.ILockSeatApiService;
import com.ydp.pwgl.dubbo.faces.ISessionsMgrService;
import com.ydp.typedef.DataResult;
import com.ydp.typedef.Param;
import com.ydp.typedef.StatusCode;
import com.ydp.typedef.UDException;
import com.ydp.util.CodeUtil;
import com.ydp.util.Dao;
import com.ydp.util.DateForamtUtil;

@Service
public class LockSeatApiService extends BaseService implements IService,
		ILockSeatApiService {
	@Autowired
	private LockSeatApiRedisService lockSeatApiRedisService;

	// 座位状态
	public static final String PEWSTATUS_UNDEFINED = "";
	public static final String PEWSTATUS_CANSELL = "1";
	public static final String PEWSTATUS_NOSELL = "2";
	public static final String PEWSTATUS_SELLED = "3";

	public LockSeatApiService() {
		super(LockSeatApiService.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void service(Param param) throws Exception {
		// TODO Auto-generated method stub

	}

	
	@Override
	public DataResult GetSeatMap(String sessionsid) {
		DataResult result = new DataResult();

		List<Seat> lst = lockSeatApiRedisService.GetSeatMap(sessionsid);
		if (lst == null) {
			result.setStatusCode(StatusCode.SessionsSeatNull);
		} else {
			result.setStatusCode(StatusCode.Success);
			result.setData(lst);
		}

		return result;
	}

	/**
	 * 更新座位状态
	 * 
	 * @param param
	 */
	private int UpdateLockSeat(Param param) {
		return getDao().getSqlSession().update("LocakSeatApiMapper.updateSeat",
				param);
	}
	


	/**
	 * 锁座
	 * 
	 * @param itemid
	 * @param pewid
	 */
	@Override
	public DataResult LockSeat(String sessionsid, String pewid)
			throws Exception {
		DataResult result = new DataResult();
		Seat seat = null;
		
		if (!getRedisClient().sismember(Constant.RUNNINGSESSIONS , sessionsid)){
			result.setStatusCode(StatusCode.SessionsStoped);
			return result;		
		}

		seat = lockSeatApiRedisService.GetSeat(sessionsid, pewid);

		if (seat == null) {
			result.setStatusCode(StatusCode.SeatNotFound);
			return result;
		}

		if (!CodeUtil.checkParam(seat.getStatus())
				&& !seat.getStatus().equals("1")) {
			result.setStatusCode(StatusCode.SeatDisabled);
			return result;
		}

		if (!CodeUtil.checkParam(seat.getPewstatus())
				&& !seat.getPewstatus().equals(PEWSTATUS_CANSELL)) {
			result.setStatusCode(StatusCode.SeatLocked);
			return result;
		}

		// 设置被锁定设置不可售状态
		String original_status = seat.getPewstatus();
		seat.setPewstatus(PEWSTATUS_NOSELL);
		boolean bret = lockSeatApiRedisService.SetSeat(sessionsid, pewid, seat);

		if (bret) {

			// 写数据库
			Param param = new Param();
			param.put("sessionsid", sessionsid);
			param.put("pewid", pewid);
			param.put("pewstatus", seat.getPewstatus());
			if (UpdateLockSeat(param) > 0){
				result.setStatusCode(StatusCode.Success);
				
				//将座位写入未付款队列,超过15分钟将由定时器释放
				if (!lockSeatApiRedisService.AddUnPaySeatQueue(sessionsid, pewid)){
					getLogger().error("AddUnPaySeatQueue({},{}) failed",sessionsid,pewid);
					
					//数据库回退
					//param.put("pewstatus", original_status);
					//UpdateLockSeat(param);
					
					//缓存状态回退
					//seat.setPewstatus(original_status);
					//lockSeatApiRedisService.SetSeat(sessionsid, pewid, seat);
					//result.setStatusCode(StatusCode.SeatLockFailed);
					
					return UnLockSeat(sessionsid,pewid);
				}
			}
			else {
				getLogger().error("写锁座数据库状态失败,座位缓存准备回退到初始状态");
				seat.setPewstatus(original_status);
				bret = lockSeatApiRedisService.SetSeat(sessionsid, pewid, seat);
				result.setStatusCode(StatusCode.SeatLockFailed);
			}

		} else {
			getLogger().error("写锁座缓存状态失败");
			result.setStatusCode(StatusCode.SeatLockFailed);
		}

		return result;
	}

	/**
	 * 释放座位
	 * 
	 * @param itemid
	 * @param pewid
	 * @return
	 */
	public DataResult UnLockSeat(String sessionsid, String pewid)
			throws Exception {
		DataResult result = new DataResult();
		Seat seat = null;
		seat = lockSeatApiRedisService.GetSeat(sessionsid, pewid);

		if (seat == null) {
			result.setStatusCode(StatusCode.SeatNotFound);
			return result;
		}
		
		if (seat.getPewstatus().equals(PEWSTATUS_UNDEFINED)) {
			result.setStatusCode(StatusCode.InvalidParam);
			return result;
		}

		if (seat.getPewstatus().equals(PEWSTATUS_CANSELL)) {
			result.setStatusCode(StatusCode.SeatCanSell);
			return result;
		}

		if (seat.getPewstatus().equals(PEWSTATUS_SELLED)) {
			result.setStatusCode(StatusCode.SeatHasSelled);
			return result;
		}

		// 设置被锁定设置解锁状态
		String original_status = seat.getPewstatus();
		seat.setPewstatus(PEWSTATUS_CANSELL);
		boolean bret = lockSeatApiRedisService.SetSeat(sessionsid, pewid, seat);

		if (bret) {

			// 写数据库
			Param param = new Param();
			param.put("sessionsid", sessionsid);
			param.put("pewid", pewid);
			param.put("pewstatus", seat.getPewstatus());
			if (UpdateLockSeat(param) > 0)
				result.setStatusCode(StatusCode.Success);
			else {
				getLogger().error("写锁座数据库状态失败,座位缓存准备回退到初始状态");
				seat.setPewstatus(original_status);
				bret = lockSeatApiRedisService.SetSeat(sessionsid, pewid, seat);
				result.setStatusCode(StatusCode.SeatUnLockFailed);
			}

		} else {
			getLogger().error("写锁座缓存状态失败");
			result.setStatusCode(StatusCode.SeatUnLockFailed);
		}

		return result;
	}
	
	public DataResult OKLockSeat(String sessionsid,String pewid) throws Exception{
		DataResult result = new DataResult();
		Seat seat = null;
		seat = lockSeatApiRedisService.GetSeat(sessionsid, pewid);

		if (seat == null) {
			result.setStatusCode(StatusCode.SeatNotFound);
			return result;
		}
		
		if (seat.getPewstatus().equals(PEWSTATUS_UNDEFINED)) {
			result.setStatusCode(StatusCode.InvalidParam);
			return result;
		}

		if (seat.getPewstatus().equals(PEWSTATUS_CANSELL)) {
			result.setStatusCode(StatusCode.SeatCanSell);
			return result;
		}

		if (seat.getPewstatus().equals(PEWSTATUS_SELLED)) {
			result.setStatusCode(StatusCode.SeatHasSelled);
			return result;
		}

		// 设置被锁定设置解锁状态
		String original_status = seat.getPewstatus();
		seat.setPewstatus(PEWSTATUS_SELLED);
		boolean bret = lockSeatApiRedisService.SetSeat(sessionsid, pewid, seat);

		if (bret) {

			// 写数据库
			Param param = new Param();
			param.put("sessionsid", sessionsid);
			param.put("pewid", pewid);
			param.put("pewstatus", seat.getPewstatus());
			if (UpdateLockSeat(param) > 0)
				result.setStatusCode(StatusCode.Success);
			else {
				getLogger().error("写锁座数据库状态失败,座位缓存准备回退到初始状态");
				seat.setPewstatus(original_status);
				bret = lockSeatApiRedisService.SetSeat(sessionsid, pewid, seat);
				result.setStatusCode(StatusCode.SeatOkLockFailed);
			}

		} else {
			getLogger().error("写锁座缓存状态失败");
			result.setStatusCode(StatusCode.SeatOkLockFailed);
		}

		return result;		
	}
	
	
	/**
	 * 释放过期座位
	 * 
	 * @param param
	 */
	public void UnLockUnPaySeatQueue(long timeIntervalSeconds){
		Set<String> keys = lockSeatApiRedisService.UnLockUnPaySeatQueue(timeIntervalSeconds);
		long end = System.currentTimeMillis() - (timeIntervalSeconds * 1000);
		
		if (keys == null || keys.size() == 0) return ;
		
		for (String orgkey : keys) {
			String key[] = orgkey.split(":");
			if (key.length <2) break;
			String skey = key[0] + ":" + key[1];
			Set<String> values = getRedisClient().zrangebyscore(skey, 0, end);
			for (String pewid : values){
				try {
					DataResult result = UnLockSeat(key[1],pewid);
					getLogger().info("释放过期座位:{}:{},result:{}",key[1],pewid,result);
				} catch (Exception e) {
					getLogger().error(e.getMessage());
				}
			}
			
			redisClient.zremrangbyscore(skey, 0, end);
		}
	}
}
