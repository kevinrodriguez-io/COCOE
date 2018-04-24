/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws;

import filters.JWTTokenNeeded;
import dao.Metersessionuser;
import dao.MetersessionuserRepository;
import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * Fully working area api endpoint, it communicates with 
 * Hibernate via Repository Pattern
 * @author COCOE
 */
@Path("metersessionuser")
public class MetersessionuserApi {
    
    @Context
    private UriInfo context;
    
    private MetersessionuserRepository repository = new MetersessionuserRepository();
    
    public MetersessionuserApi(){}
    
    @GET
    @Path("/")
    @JWTTokenNeeded
    public JsonArray All() {
        List<Metersessionuser> items = repository.All("Metersessionuser");
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Metersessionuser item : items) {
            jsonArrayBuilder.add(Json.createObjectBuilder()
                .add("id", item.getId())
                .add("metersessionid", item.getMetersessionid())
                .add("userid", item.getUserid())
                .add("createdDate", item.getCreatedDate().toString())
            );
        }
        return jsonArrayBuilder.build();
    }
    
    @GET
    @Path("/byMeterSession/{meterSessionId}")
    @JWTTokenNeeded
    public JsonArray ByMeterSession(@PathParam("meterSessionId") String id) {
        Integer meterSessionId = Integer.parseInt(id);
        
        List<Metersessionuser> items = repository.All("Metersessionuser").stream()
                .filter(I->I.getMetersessionid() == meterSessionId)
                .collect(Collectors.toList());
        
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Metersessionuser item : items) {
            jsonArrayBuilder.add(Json.createObjectBuilder()
                .add("id", item.getId())
                .add("metersessionid", item.getMetersessionid())
                .add("userid", item.getUserid())
                .add("createdDate", item.getCreatedDate().toString())
            );
        }
        return jsonArrayBuilder.build();
    }
    
    @GET
    @Path("{id}")
    @JWTTokenNeeded
    public JsonObject Find(@PathParam("id") String id) {
        Metersessionuser item = repository.Get("Metersessionuser", Integer.parseInt(id));
        return Json.createObjectBuilder()
                .add("id", item.getId())
                .add("metersessionid", item.getMetersessionid())
                .add("userid", item.getUserid())
                .add("createdDate", item.getCreatedDate().toString())
                .build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("create")
    @JWTTokenNeeded
    public JsonObject Create(String content) {
        JsonObject jsonObject = Json.createReader(new StringReader(content)).readObject();
        Metersessionuser item = new Metersessionuser(
            jsonObject.getInt("userid"),
            jsonObject.getInt("metersessionid"),
            new Date()
        );
        repository.Create(item);
        return Json.createObjectBuilder()
                .add("id", item.getId())
                .add("metersessionid", item.getMetersessionid())
                .add("userid", item.getUserid())
                .add("createdDate", item.getCreatedDate().toString())
                .build();
    }
    
    @DELETE
    @Path("delete/{id}")
    @JWTTokenNeeded
    public JsonObject Delete(@PathParam("id") String id) {
        repository.Delete(repository.Get("Metersessionuser", Integer.parseInt(id)));
        return Json.createObjectBuilder()
                .add("result", true)
                .build();
    }
    
}
