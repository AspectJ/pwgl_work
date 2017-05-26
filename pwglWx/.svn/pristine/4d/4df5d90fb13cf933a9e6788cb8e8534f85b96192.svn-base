package com.ydp.pwgl.wx;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


public class InitService extends HttpServlet {
	@Override
	public void init() throws ServletException {
		// 读取配置文件
		String path = getServletConfig().getServletContext().getRealPath("/")
				+ "/WEB-INF/classes/";

		Config.confpath = path;
		

		// 加载配置文件
		Config.LoadProperties();
	}

}
