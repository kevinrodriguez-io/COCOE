/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws;

import filters.JWTTokenNeeded;
import dao.Client;
import dao.Clientmetering;
import dao.ClientmeteringRepository;
import java.io.StringReader;
import java.util.Date;
import java.util.List;
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
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import javax.ws.rs.core.Response;

/**
 * Fully working client api endpoint, it communicates with 
 * Hibernate via Repository Pattern
 * @author COCOE
 */
@Path("clientmetering")
public class ClientmeteringApi {
    
    @Context
    private UriInfo context;
    
    private ClientmeteringRepository repository = new ClientmeteringRepository();
    
    public ClientmeteringApi(){}
    
    @GET
    @Path("/")
    @JWTTokenNeeded
    public JsonArray All() {
        List<Clientmetering> items = repository.All("Clientmetering");
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Clientmetering item : items) {
            jsonArrayBuilder.add(Json.createObjectBuilder()
                .add("id", item.getId())
                .add("meterSessionId", item.getMeterSessionId())
                .add("clientId", item.getClientId())
                .add("amount", item.getAmount())
                .add("uomId", item.getUomId())
                .add("meteringDate", item.getMeteringDate().toString())
                .add("billed", item.isBilled())
                .add("createdDate", item.getCreatedDate().toString())
            );
        }
        return jsonArrayBuilder.build();
    }
    
    @GET
    @Path("/byclient/{clientid}")
    @JWTTokenNeeded
    public JsonArray AllByClient(@PathParam("clientid") String clientid) {
        Integer clientId = Integer.parseInt(clientid);
        List<Clientmetering> items = repository.AllByClient(clientId);
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Clientmetering item : items) {
            jsonArrayBuilder.add(Json.createObjectBuilder()
                .add("id", item.getId())
                .add("meterSessionId", item.getMeterSessionId())
                .add("clientId", item.getClientId())
                .add("amount", item.getAmount())
                .add("uomId", item.getUomId())
                .add("meteringDate", item.getMeteringDate().toString())
                .add("billed", item.isBilled())
                .add("createdDate", item.getCreatedDate().toString())
            );
        }
        return jsonArrayBuilder.build();
    }
    
    @GET
    @Path("{id}")
    @JWTTokenNeeded
    public JsonObject Find(@PathParam("id") String id) {
        Clientmetering item = repository.Get("Clientmetering", Integer.parseInt(id));
        return Json.createObjectBuilder()
                .add("id", item.getId())
                .add("meterSessionId", item.getMeterSessionId())
                .add("clientId", item.getClientId())
                .add("amount", item.getAmount())
                .add("uomId", item.getUomId())
                .add("meteringDate", item.getMeteringDate().toString())
                .add("billed", item.isBilled())
                .add("createdDate", item.getCreatedDate().toString()).build();
    }
    
    @GET
    @Path("/byclient/{clientid}/bymetersession/{metersessionid}")
    @Produces(APPLICATION_JSON)
    @JWTTokenNeeded
    public Response FindByClientAndByMeterSession(
        @PathParam("clientid") String clientid,
        @PathParam("metersessionid") String metersessionid
    ) {
        Integer clientId = Integer.parseInt(clientid);
        Integer metersessionId = Integer.parseInt(metersessionid);
        Clientmetering item = repository.FindByClientAndByMeterSession(clientId, metersessionId);
        if (item != null) {
            JsonObject json = Json.createObjectBuilder()
                    .add("id", item.getId())
                    .add("meterSessionId", item.getMeterSessionId())
                    .add("clientId", item.getClientId())
                    .add("amount", item.getAmount())
                    .add("uomId", item.getUomId())
                    .add("meteringDate", item.getMeteringDate().toString())
                    .add("billed", item.isBilled())
                    .add("createdDate", item.getCreatedDate().toString()).build();
            return Response.ok(json.toString()).build();
        } else {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("edit")
    @JWTTokenNeeded
    public JsonObject Edit(String content) {
        JsonObject jsonObject = Json.createReader(new StringReader(content)).readObject();
        Clientmetering item = repository.Get("Clientmetering", Integer.parseInt(jsonObject.getString("id")));
        item.setAmount(jsonObject.getInt("amount"));
        //item.setUomId(jsonObject.getInt("uomId"));
        repository.Update(item);
        return Json.createObjectBuilder()
            .add("id", item.getId())
            .add("meterSessionId", item.getMeterSessionId())
            .add("clientId", item.getClientId())
            .add("amount", item.getAmount())
            .add("uomId", item.getUomId())
            .add("meteringDate", item.getMeteringDate().toString())
            .add("billed", item.isBilled())
            .add("createdDate", item.getCreatedDate().toString()).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("create")
    @JWTTokenNeeded
    public JsonObject Create(String content) {
        JsonObject jsonObject = Json.createReader(new StringReader(content)).readObject();
        Clientmetering item = new Clientmetering(
                jsonObject.getInt("meterSessionId"), 
                jsonObject.getInt("clientId"), 
                jsonObject.getInt("amount"),
                1,
                //jsonObject.getInt("uomId"), 
                new Date(), 
                false, 
                new Date()
        );
        repository.Create(item);
        return Json.createObjectBuilder()
            .add("id", item.getId())
            .add("meterSessionId", item.getMeterSessionId())
            .add("clientId", item.getClientId())
            .add("amount", item.getAmount())
            .add("uomId", item.getUomId())
            .add("meteringDate", item.getMeteringDate().toString())
            .add("billed", item.isBilled())
            .add("createdDate", item.getCreatedDate().toString()).build();
    }
    
    @DELETE
    @Path("delete/{id}")
    @JWTTokenNeeded
    public JsonObject Delete(@PathParam("id") String id) {
        repository.Delete(repository.Get("Clientmetering", Integer.parseInt(id)));
        return Json.createObjectBuilder()
                .add("result", true)
                .build();
    }
    
}
