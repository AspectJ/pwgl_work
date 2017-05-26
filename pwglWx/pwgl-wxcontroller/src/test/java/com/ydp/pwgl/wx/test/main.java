package com.ydp.pwgl.wx.test;

import java.io.File;

import net.sf.json.JSONObject;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ydp.pwgl.dubbo.faces.ILockSeatApiService;
import com.ydp.pwgl.dubbo.faces.ISessionsMgrService;
import com.ydp.typedef.DataResult;
import com.ydp.typedef.StatusCode;


public class main {
	
	private static Logger logger = LoggerFactory.getLogger(main.class);
	public  static ApplicationContext ac = null;
	static {
		String path;

		try {		
			
			// 读取配置文件
			if (main.class.getClassLoader().getResource("") != null) {
				path = main.class.getClassLoader().getResource("")
						.getPath().substring(1);
			} else {
				// 读取项目路径
				File directory = new File("");// 参数为空
				path = directory.getCanonicalPath() + "/";
			}
			
			System.out.println(path);
			
			//加载日志配置文件
			PropertyConfigurator.configureAndWatch(path + "conf/log4j.properties", 60000);
			
			//Properties pro = new Properties();
			//InputStream in = new FileInputStream(path + "conf/jdbc.properties");
			//pro.load(in);
			//in.close();
			
			//pro = new Properties();
			//InputStream in = new FileInputStream(path + "conf/redis.properties");
			//pro.load(in);
			//in.close();
			
			//in =  DubboService.class.getClassLoader().getResourceAsStream("conf/spring-db.xml");		
			ac = new  ClassPathXmlApplicationContext("classpath:conf/spring-dubbo-client.xml");
			lockSeatApiService = (ILockSeatApiService) ac.getBean("lockSeatApiService");
			sessionsMgrService = (ISessionsMgrService) ac.getBean("sessionsMgrService");

			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	
	public static ILockSeatApiService lockSeatApiService;
	public static ISessionsMgrService sessionsMgrService;
	//参数定义
	public static final String sessionsid = "13517DRNe0000001";
	public static final String pewid = "13517DRNe0000200";
	
	/**
	 * 显示结果
	 * @param obj
	 */
	public void show(Object obj){
		JSONObject jsonObj = JSONObject.fromObject(obj);	
		String s = jsonObj.toString();
		System.out.println(s);	
	}
	
	/**
	 * 测试模拟发送初始化座位图
	 */
	
	@Test
	public void testInitSeatMap(){
		DataResult result = sessionsMgrService.InitSeatMap(sessionsid);
		show(result);
	}
	
	/**
	 * 测试排期座位信息
	 */
	@Test
	public void testGetSeatMap(){
		DataResult result = lockSeatApiService.GetSeatMap(sessionsid);
		show(result);
	}
	
	
	@Test
	public void testDelSeatMap(){
		DataResult result = sessionsMgrService.DelSeatMap(sessionsid);
		show(result);		
	}
	
	@Test
	public void testSessionsStateOnChange(){
		DataResult result = sessionsMgrService.SessionsStateOnChange(/*sessionsid*/"00916GCRH0000001","1");
		show(result);	
	}
	

	/**
	 * 测试锁座
	 */
	@Test
	public void testLockSeat(){
		DataResult result = null;
		try {
			result = lockSeatApiService.LockSeat(sessionsid,pewid);
		} catch (Exception e) {
			logger.error("锁座失败:{}",e.getMessage());
			result = new DataResult();
			result.setStatusCode(StatusCode.SysException);
		}
		show(result);
	}
	
	/**
	 * 测试锁座
	 */
	@Test
	public void testUnLockSeat(){
		DataResult result = null;
		try {
			result = lockSeatApiService.UnLockSeat(sessionsid,pewid);
		} catch (Exception e) {
			logger.error("解锁失败:{}",e.getMessage());
			result = new DataResult();
			result.setStatusCode(StatusCode.SysException);
		}
		show(result);
	}
}
