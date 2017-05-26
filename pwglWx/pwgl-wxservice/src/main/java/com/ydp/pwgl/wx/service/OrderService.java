package com.ydp.pwgl.wx.service;

import com.ydp.face.BaseService;
import com.ydp.face.IRedisClient;
import com.ydp.face.IService;
import com.ydp.pwgl.dubbo.faces.ILockSeatApiService;
import com.ydp.pwgl.dubbo.faces.ISessionsMgrService;
import com.ydp.typedef.DataResult;
import com.ydp.typedef.Param;
import com.ydp.typedef.StatusCode;
import com.ydp.typedef.UDException;
import com.ydp.util.CodeUtil;
import com.ydp.util.Dao;
import com.ydp.util.DateForamtUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.WordUtils;
import org.apache.http.impl.cookie.RFC2965DiscardAttributeHandler;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.java2d.pipe.SpanIterator;

import javax.rmi.CORBA.Util;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderService extends BaseService implements IService {

	@Autowired
	ILockSeatApiService lockSeatApiService;
	@Autowired
	ISessionsMgrService sessionsMgrService;
	@Autowired
	private IRedisClient redisClient;

	private static final String ORDER_FORMAT = "00";
	private static final String NOT_PAY_ORDER_FIX = "pwgl:notPayOrder:userid:";	//	未支付订单缓存key前缀
	private static final String PAY_ORDER_FIX = "pwgl:payOrder:userid:";	//用户已支付订单缓存key前缀

	public void init(Param param) {
		/*String sessionsid = param.getString("sessionsid");
		DataResult result=sessionsMgrService.InitSeatMap(sessionsid);
		param.setResult(result);*/


	}

	//订单状态
	public static enum ORDERSTATUS {UNPAY,HASPAY,OUTTICKET,RETURNMONEY,CANCEL,SENDOUT} ;

	public OrderService() {
		super(OrderService.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void service(Param param) throws Exception {
		DataResult result = new DataResult();
		Dao dao = getDao();
		try {
			String pewid = param.getString("pewid");
		} catch (Exception e) {
			result.setStatusCode(StatusCode.SysException);
			param.setResult(result);

			getLogger().error("UserService " + e.getMessage());
			//系统异常
			throw new UDException(StatusCode.SysException,e);
		}

	}

	/**
	 * 生成未支付订单信息，缓存到redis
	 * @param param
	 * @return
	 */
	public void placeAnOrder(Param param) throws Exception {
		DataResult result = new DataResult();
		Date now = new Date();
		String key = "pwgl:orderIncr";
		String pewids = param.getString("pewids");
		String pewnames = param.getString("pewnames");
		String sessionsid = param.getString("sessionsid");
		String userid = param.getString("userid");
		String itemid = param.getString("itemid");
		String itemname = param.getString("itemname");
		String venueid = param.getString("venueid");
		String venuename = param.getString("venuename");
		String playtime = param.getString("playtime");
		if(CodeUtil.checkParam(pewids,sessionsid,userid,pewnames,itemid,itemname,venueid,venuename,playtime)){
			result.setStatusCode(StatusCode.MissingParams);
			param.setResult(result);
			return;
		}
		//先查询该用户下是否有未支付订单，若有未支付订单，则不能生成新订单
		Map<String, Object> map = (Map<String, Object>) redisClient.bget(NOT_PAY_ORDER_FIX+userid);
		if(map!= null && !map.isEmpty()){
			String status = (String) map.get("status");
			if("0".equals(status)){
				result.setStatusCode(StatusCode.ExistNoPayOrder);
				param.setResult(result);
				return;
			}
		}
		String[] pewid_arr = pewids.split(",");
		double price=0;
		for(String pewid:pewid_arr){
			//锁座,若锁座不成功，则返回
			result=lockSeatApiService.LockSeat(sessionsid, pewid);
			if(result.getCode()!=0){
				param.setResult(result);
				return;
			}
			 price += getPrice(pewid,userid);
		}
		try {

			//生成订单信息，格式：yyyyMMddhhmmssSSS000-yyyyMMddhhmmssSSS999，每毫秒1000个订单号
			Long orderIncr=redisClient.incr(key);
			if(orderIncr>=100){
                redisClient.del(key);
                orderIncr = redisClient.incr(key);
            }
			String orderStr = new DecimalFormat(ORDER_FORMAT).format(orderIncr);
			String orderNum = new SimpleDateFormat("yyyyMMddhhmmssSSS").format(now)+orderStr;
			Long orderid = Long.parseLong(orderNum);
			Map<String, Object> resultmap = new HashMap<String, Object>();
			resultmap.put("sessionsid",sessionsid);
			resultmap.put("pewids", pewids);
			resultmap.put("pewnames", pewnames);
			resultmap.put("itemid", itemid);
			resultmap.put("itemname", itemname);
			resultmap.put("venueid", venueid);
			resultmap.put("venuename", venuename);
			resultmap.put("playtime", playtime);
			resultmap.put("orderid",orderid);
			resultmap.put("status","0" );
			resultmap.put("price", price);
			resultmap.put("odtime", DateForamtUtil.to_yyyy_MM_dd_HH_mm_ss_str(now));
			//生成的订单为未支付订单，缓存到redis，过期时间15分钟
			redisClient.setex(NOT_PAY_ORDER_FIX+userid,900,resultmap);
			result.setData(resultmap);
			result.setStatusCode(StatusCode.Success);
			result.setTotal(1);
			param.setResult(result);
		} catch (Exception e) {
			for(String pewid:pewid_arr){
				//释放座位
				lockSeatApiService.UnLockSeat(sessionsid, pewid);
			}
			//删除订单信息
			redisClient.del(NOT_PAY_ORDER_FIX+userid);
		}
		return ;
	}

	//手动取消订单，释放座位
	public void cancelOrder(Param param) throws Exception {
		DataResult result = new DataResult();
		String sessionsid = param.getString("sessionsid");
		String pewids = param.getString("pewids");
		String userid = param.getString("userid");
		if(CodeUtil.checkParam(sessionsid,pewids,userid)){
			result.setStatusCode(StatusCode.MissingParams);
			param.setResult(result);
			return;
		}
		//删除缓存订单信息
		redisClient.del(NOT_PAY_ORDER_FIX+userid);
		//释放座位
		String[] pewids_arr = pewids.split(",");
		for (String pewid : pewids_arr) {
			result=lockSeatApiService.UnLockSeat(sessionsid,pewid);
		}
		result.setStatusCode(StatusCode.Success);
		param.setResult(result);
	}

	/**
	 * 支付成功后，删除缓存中未支付订单信息
	 * @param userid
	 * @throws Exception
	 */
	public void delNoPayOrder(String userid) throws Exception{
		redisClient.del(NOT_PAY_ORDER_FIX+userid);
	}

	/**
	 * 获取用户订单
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void getOrder(Param param) throws Exception{
		DataResult result = new DataResult();
		String userid = param.getString("userid");
		//从缓存查询未支付订单
		Map<String,Object> notPayOrder= (Map<String, Object>) redisClient.bget(NOT_PAY_ORDER_FIX+userid);
		//获取用户已支付订单，先从缓存获取，若缓存没有，则从数据库获取，缓存到redis,过期时间5个小时
		List<Map<String, Object>> orderlist = (List<Map<String, Object>>) redisClient.bget(PAY_ORDER_FIX + userid);
		if(orderlist==null || orderlist.isEmpty()){
			Map<String, Object> parammap = new HashMap<String, Object>();
			parammap.put("userid", userid);
			parammap.put("odstatus", "1");
			orderlist= getDao().getSqlSession().selectList("Order.getOrder",parammap);
			if(orderlist!=null && !orderlist.isEmpty()){
				redisClient.setex(PAY_ORDER_FIX+userid,18000,orderlist);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("notpay", notPayOrder);
		map.put("pay", orderlist);
		result.setStatusCode(StatusCode.Success);
		result.setData(map);
		result.setTotal(orderlist.size()+(notPayOrder==null?0:1));
		param.setResult(result);
	}

	private double getPrice(String pweid, String userid) {
		return 50.2;
	}

	/**
	 * 支付完成后，更新用户支付缓存
	 * @param userid
	 */
	public void updatePayOrderCache(String userid){

	}


}
