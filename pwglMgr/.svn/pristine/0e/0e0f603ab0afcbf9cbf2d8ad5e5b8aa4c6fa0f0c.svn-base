package com.ydp.pwgl.controller.venue;

import com.ydp.face.BaseController;
import com.ydp.pwgl.service.venue.VenueService;
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
 * Created by john on 2017/4/26.
 */
@Service
@Path("/rest/venue")
public class VenueController extends BaseController {


    public VenueController() {
        super(VenueController.class);
    }

    @Autowired
    private VenueService venueService;

    @GET
    @POST
    @Path("/index")
    @Produces("application/json;charset=UTF-8")
    public DataResult select(@Context HttpServletRequest request,
                             @Context HttpServletResponse response) throws ServletException,
            IOException {

        Param param = new Param(request);
        try {
            venueService.service(param);
            System.out.println("123");
        } catch (Exception e) {
            return null;
        }

        return param.getResult();
    }
}
