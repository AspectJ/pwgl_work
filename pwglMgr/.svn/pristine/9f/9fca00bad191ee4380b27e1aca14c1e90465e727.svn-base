package com.ydp.pwgl.controller.setpolicy;

import com.ydp.face.BaseController;
import com.ydp.pwgl.service.reserve.ReserveService;
import com.ydp.pwgl.setpolicy.SetPolicyService;
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
@Path("/rest/setPolicy")
public class SetPolicyController extends BaseController {

    @Autowired
    private SetPolicyService setPolicyService;

    public SetPolicyController() {
        super(SetPolicyController.class);
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
            setPolicyService.service(param);
        }catch (Exception e){
            getLogger().error(e.getMessage());
            return null;
        }

        return param.getResult();
    }

}
