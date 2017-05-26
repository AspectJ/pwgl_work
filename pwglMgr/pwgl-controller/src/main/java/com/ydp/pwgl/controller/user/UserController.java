package com.ydp.pwgl.controller.user;

import com.ydp.face.BaseController;
import com.ydp.pwgl.service.user.UserService;
import com.ydp.typedef.DataResult;
import com.ydp.typedef.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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
 * Created by 29632 on 2017/4/28.
 */
@Controller
@Path("rest/user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    public UserController() {
        super(UserController.class);
    }

    @GET
    @POST
    @Path("/index")
    @Produces("application/json;charset=UTF-8")
    public DataResult user(@Context HttpServletRequest request,
                                @Context HttpServletResponse response) throws ServletException,
            IOException {

        Param param = new Param(request);
        try {
            userService.service(param);
        }catch (Exception e){
            getLogger().error(e.getMessage());
            return null;
        }

        return param.getResult();
    }

    @GET
    @POST
    @Path("/login")
    @Produces("application/json;charset=utf-8")
    public DataResult login(@Context HttpServletRequest request,@Context HttpServletResponse response){
        Param param = new Param(request);
        try {
            userService.login(param);
        }catch (Exception e){
            getLogger().error(e.getMessage());
            return null;
        }
        return param.getResult();
    }


}
