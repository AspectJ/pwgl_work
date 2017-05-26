package com.ydp.pwgl.dubbo.faces;

import com.ydp.typedef.DataResult;

public interface ISessionsMgrService {
	/**
	 * 初始化场次座位表(后台调用)
	 * @param sessionsid
	 * @param endtime
	 * @return
	 */
	 public DataResult InitSeatMap(String sessionsid);
	 
	 /**
	  * 删除场次座位表(后台调用,一般时间到期后会自动清除)
	  * @param sessionsid
	  * @return
	  */
	 public DataResult DelSeatMap(String sessionsid);
	 
	 /**
	  * 场次状态发生改变通知
	  * @param sessionsid
	  * @param state
	  * @return
	  */
	 public DataResult SessionsStateOnChange(String sessionsid,String state);
}
