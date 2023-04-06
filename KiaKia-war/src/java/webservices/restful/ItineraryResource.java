/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices.restful;

import entity.DayItinerary;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.json.JsonObject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.ItinerarySessionBeanLocal;

/**
 * REST Web Service
 *
 * @author Natalienovaela
 */
@Path("/trips/{tripId}/itineraries")
public class ItineraryResource {

    @EJB
    private ItinerarySessionBeanLocal ItinerarySessionBeanLocal;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<DayItinerary> createItinerary(@PathParam("tripId") Long tripId, JsonObject request) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
        
        try {
        Date startDate = formatter.parse(formatter.format(formatter.parse(request.getString("startDate"))));
        Date endDate = formatter.parse(formatter.format(formatter.parse(request.getString("endDate"))));
        
        return ItinerarySessionBeanLocal.createItineraries(startDate, endDate, tripId);
        
        } catch(ParseException ex) {
            throw new BadRequestException("Invalid date format. Use yyyy-MM-dd");
        }
        
    }

}
