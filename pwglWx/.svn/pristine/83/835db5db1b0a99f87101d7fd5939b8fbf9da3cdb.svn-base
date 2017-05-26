package com.ydp.pwgl.wx.controller;

import com.ydp.face.BaseController;
import com.ydp.pwgl.wx.service.OrderService;
import com.ydp.typedef.DataResult;
import com.ydp.typedef.Param;
import com.ydp.typedef.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

@Service
@Path("/rest/order")
public class OrderController extends BaseController {

	@Autowired
	OrderService orderService;

	public OrderController() {
		super(OrderController.class);
		// TODO Auto-generated constructor stub
	}


	/**
	 * 生成订单
	 * @param request
	 * @param response
	 * @return
	 */
	@GET
	@POST
	@Produces("application/json;charset=utf-8")
	@Path("/placeOrder")
	public DataResult placeOrder(@Context HttpServletRequest request,@Context HttpServletResponse response){
		DataResult result = new DataResult();
		Param param = new Param(request);

		try {
			orderService.placeAnOrder(param);
			result = param.getResult();
		} catch (Exception e) {
			getLogger().error(e.getMessage());
			result.setStatusCode(StatusCode.SysException);
		}
		return result;

	}

	@GET
	@POST
	@Produces("application/json;charset=utf-8")
	@Path("/init")
	public DataResult init(@Context HttpServletRequest request,@Context HttpServletResponse response){
		Param param = new Param(request);
		orderService.init(param);
		DataResult result = param.getResult();
		return result;


	}

	/**
	 * 取消订单
	 * @param request
	 * @param response
	 * @return
	 */
	@GET
	@POST
	@Produces("application/json;charset=utf-8")
	@Path("/cancelOrder")
	public DataResult cancelOrder(@Context HttpServletRequest request,@Context HttpServletResponse response){
		DataResult result = new DataResult();
		Param param = new Param(request);
		getLogger().debug("list entered!!!");
		
		try {
			orderService.cancelOrder(param);
			result = param.getResult();
		} catch (Exception e) {
			getLogger().error(e.getMessage());
			result.setStatusCode(StatusCode.SysException);
		}
		return result;
	}

	/**
	 * 获取用户订单
	 * @param request
	 * @param response
	 * @return
	 */
	@GET
	@POST
	@Produces("application/json;charset=utf-8")
	@Path("/getOrder")
	public DataResult getOrder(@Context HttpServletRequest request ,@Context HttpServletResponse response){
		DataResult result = new DataResult();
		Param param = new Param(request);
		try {
			orderService.getOrder(param);
			result = param.getResult();
		} catch (Exception e) {
			getLogger().error(e.getMessage());
			result.setStatusCode(StatusCode.SysException);
		}
		return result;
	}
}
