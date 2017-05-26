package com.ydp.pwgl.controller.roleMenu;

import com.ydp.face.BaseController;
import com.ydp.pwgl.service.roleMenu.RoleMenuService;
import com.ydp.typedef.DataResult;
import com.ydp.typedef.Param;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

@Controller
@Path("/rest/rolemenu")
public class RoleMenuController extends BaseController {

    @Autowired
    private RoleMenuService roleMenuService;

    public RoleMenuController() {
        super(RoleMenuController.class);
    }

    @GET
    @POST
    @Produces("application/json;charset=UTF-8")
    @Path("/index")
    public DataResult rolemenu(@Context HttpServletRequest request, @Context HttpServletResponse response){

        Param param = new Param(request);
        try {
            roleMenuService.service(param);
        }catch (Exception e){
            getLogger().error(e.getMessage());
            return null;
        }

        return param.getResult();
    }

}
