package com.ydp.dubbo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.mortbay.jetty.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class DubboService {
	private static Logger logger = LoggerFactory.getLogger(DubboService.class);
	
	public static void main(String[] args) {
		String path;

		try {
			
			
			// 读取配置文件
			if (DubboService.class.getClassLoader().getResource("") != null) {
				path = DubboService.class.getClassLoader().getResource("")
						.getPath().substring(1);
			} else {
				// 读取项目路径
				File directory = new File("");// 参数为空
				path = directory.getCanonicalPath() + "/";
			}
			
			//加载日志配置文件
			PropertyConfigurator.configureAndWatch(path + "log4j.properties", 60000);
			
			//Properties pro = new Properties();
			//InputStream in = new FileInputStream(path + "conf/jdbc.properties");
			//pro.load(in);
			//in.close();
			
			//pro = new Properties();
			//InputStream in = new FileInputStream(path + "conf/redis.properties");
			//pro.load(in);
			//in.close();
			
			//in =  DubboService.class.getClassLoader().getResourceAsStream("conf/spring-db.xml");		
			new  ClassPathXmlApplicationContext("classpath*:conf/spring-*.xml");
			
			Server server = new Server();
			server.start();
			server.join();
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
