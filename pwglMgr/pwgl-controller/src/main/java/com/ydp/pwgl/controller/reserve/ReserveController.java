package com.ydp.pwgl.controller.reserve;

import com.ydp.face.BaseController;
import com.ydp.pwgl.service.reserve.ReserveService;
import com.ydp.pwgl.service.role.RoleService;
import com.ydp.typedef.DataResult;
import com.ydp.typedef.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.io.IOException;

@Controller
@Path("/rest/reserve")
public class ReserveController extends BaseController {

    @Autowired
    private ReserveService reserveService;

    public ReserveController() {
        super(ReserveController.class);
    }

    @GET
    @POST
    @Path("/index")
    @Produces("application/json;charset=UTF-8")
    public DataResult role(@Context HttpServletRequest request,
                           @Context HttpServletResponse response) throws ServletException,
            IOException {

        Param param = new Param(request);
        try {
            reserveService.service(param);
        }catch (Exception e){
            getLogger().error(e.getMessage());
            return null;
        }

        return param.getResult();
    }

}
