package com.ydp.pwgl.dubbo.faces;

import java.util.List;

import com.ydp.pwgl.dubbo.entity.Seat;
import com.ydp.typedef.DataResult;
import com.ydp.typedef.StatusCode;

public interface ILockSeatApiService { 
	 /**
	  * 锁座-预锁座,15分钟内未支付还可以被释放
	  * @param sessionsid
	  * @param pewid
	  * @return
	  */
	 public DataResult LockSeat(String sessionsid,String pewid) throws Exception;
	 
	 /**
	  * 释放座位-只能解锁处于预锁座状态的座位
	  * @param sessionsid
	  * @param pewid
	  * @return
	  * @throws Exception
	  */
	 public DataResult UnLockSeat(String sessionsid,String pewid) throws Exception;
	 
	 
	 /**
	  * 确认锁座-支付成功后调用,不可释放座位
	  * @param sessionsid
	  * @param pewid
	  * @return
	  * @throws Exception
	  */
	 public DataResult OKLockSeat(String sessionsid,String pewid) throws Exception;
	 

	 /**
	  * 供接口调用获取座位数据
	  * @param sessionsid
	  * @return
	  */
	 public DataResult GetSeatMap(String sessionsid);
	 
	 /**
	  * 释放过期座位
	  * @param timeIntervalSeconds
	  */
	 public void UnLockUnPaySeatQueue(long timeIntervalSeconds);

	 
}

