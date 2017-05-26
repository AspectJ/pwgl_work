package com.ydp.pwgl.controller.schedule;

import com.ydp.face.BaseController;
import com.ydp.pwgl.service.schedule.ScheduleService;
import com.ydp.typedef.DataResult;
import com.ydp.typedef.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * Created by 董亮 on 2017/5/5.
 */
@Service
@Path("/rest/schedule")
public class ScheduleController extends BaseController {
    public ScheduleController() {
        super(ScheduleController.class);
    }
    @Autowired
    private  ScheduleService  scheduleService;
    @GET
    @POST
    @Path("/index")
    @Produces("application/json;charset=UTF-8")
    public DataResult select(@Context HttpServletRequest request,
                             @Context HttpServletResponse response) throws ServletException,
            IOException {

        Param param = new Param(request);
        try {
            scheduleService.service(param);
        } catch (Exception e) {
            return null;
        }

        return param.getResult();
    }
}
