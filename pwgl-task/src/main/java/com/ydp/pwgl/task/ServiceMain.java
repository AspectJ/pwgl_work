package com.ydp.pwgl.task;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ydp.pwgl.task.job.UnLockUnPaySeatJob;

public class ServiceMain {
	private static Logger logger = LoggerFactory.getLogger(ServiceMain.class);
	public static void main(String argv[]) {
		String path = "";
		try {
			// 读取配置文件
			if (ServiceMain.class.getClassLoader().getResource("") != null) {
				path = ServiceMain.class.getClassLoader().getResource("")
						.getPath().substring(1);
			} else {
				// 读取项目路径
				File directory = new File("");// 参数为空
				path = directory.getCanonicalPath() + "/";
			}

			// 加载日志配置文件
			PropertyConfigurator.configureAndWatch(path
					+ "conf/log4j.properties", 60000);
			
			String job_name = "定时取消未支付的订单座位";
			logger.info("【系统启动】开始(每5分钟输出一次)...");
			QuartzManager.addJob(job_name, UnLockUnPaySeatJob.class,
					"0/5 * * * * ?");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
