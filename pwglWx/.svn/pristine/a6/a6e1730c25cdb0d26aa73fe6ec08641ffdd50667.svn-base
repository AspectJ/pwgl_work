package com.ydp.pwgl.wx.controller;

import com.ydp.face.BaseController;
import com.ydp.pwgl.wx.service.ItemService;
import com.ydp.typedef.DataResult;
import com.ydp.typedef.Param;
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
@Path("/rest/item")
public class ItemController extends BaseController{

    @Autowired
    private ItemService itemService;

    public ItemController() {
        super(ItemController.class);
    }

    @GET
    @POST
    @Produces("application/json;charset=utf-8")
    @Path("/index")
    public DataResult item(@Context HttpServletRequest request, @Context HttpServletResponse response){

        Param param = new Param(request);
        try {
            itemService.service(param);
        }catch (Exception e){
            getLogger().error(e.getMessage());
            return null;
        }

        return param.getResult();
    }
}
