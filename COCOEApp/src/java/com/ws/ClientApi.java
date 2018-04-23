/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws;

import filters.JWTTokenNeeded;
import dao.Client;
import dao.ClientRepository;
import dao.Settings;
import dao.SettingsRepository;
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
import javax.ws.rs.core.MediaType;

/**
 * Fully working client api endpoint, it communicates with 
 * Hibernate via Repository Pattern
 * @author COCOE
 */
@Path("client")
public class ClientApi {
    
    public static final String CLIENT_CODE_INCREMENTAL = "CLIENTCODEINCREMENTAL";
    
    @Context
    private UriInfo context;
    
    private ClientRepository repository = new ClientRepository();
    private SettingsRepository settingsRepository = new SettingsRepository();
    
    public ClientApi(){}
    
    @GET
    @Path("/")
    @JWTTokenNeeded
    public JsonArray All() {
        List<Client> items = repository.All("Client");
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Client item : items) {
            jsonArrayBuilder.add(Json.createObjectBuilder()
                .add("id", item.getId())
                .add("code", item.getCode())
                .add("name", item.getName())
                .add("lastName", item.getLastName())
                .add("active", item.isActive())
                .add("areaid", item.getAreaid())
                .add("direction", item.getDirection())
                .add("createdDate", item.getCreatedDate().toString())
                .add("lastBillingDate", item.getLastBillingDate().toString()));
        }
        return jsonArrayBuilder.build();
    }
    
    @GET
    @Path("/byarea/{areaid}")
    @JWTTokenNeeded
    public JsonArray ByArea(@PathParam("areaid") String areaid) {
        Integer areaId = Integer.parseInt(areaid);
        List<Client> items = repository.AllByArea(areaId);
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Client item : items) {
            jsonArrayBuilder.add(Json.createObjectBuilder()
                .add("id", item.getId())
                .add("code", item.getCode())
                .add("name", item.getName())
                .add("lastName", item.getLastName())
                .add("active", item.isActive())
                .add("areaid", item.getAreaid())
                .add("direction", item.getDirection())
                .add("createdDate", item.getCreatedDate().toString())
                .add("lastBillingDate", item.getLastBillingDate().toString()));
        }
        return jsonArrayBuilder.build();
    }
    
    @GET
    @Path("{id}")
    @JWTTokenNeeded
    public JsonObject Find(@PathParam("id") String id) {
        Client item = repository.Get("Client", Integer.parseInt(id));
        return Json.createObjectBuilder()
                .add("id", item.getId())
                .add("code", item.getCode())
                .add("name", item.getName())
                .add("lastName", item.getLastName())
                .add("active", item.isActive())
                .add("areaid", item.getAreaid())
                .add("direction", item.getDirection())
                .add("createdDate", item.getCreatedDate().toString())
                .add("lastBillingDate", item.getLastBillingDate().toString()).build();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("edit")
    @JWTTokenNeeded
    public JsonObject Edit(String content) {
        JsonObject jsonObject = Json.createReader(new StringReader(content)).readObject();
        Client item = repository.Get("Client", jsonObject.getInt("id"));
        item.setName(jsonObject.getString("name"));
        item.setLastName(jsonObject.getString("lastName"));
        item.setActive(jsonObject.getBoolean("active"));
        item.setAreaid(jsonObject.getInt("areaid"));
        item.setDirection(jsonObject.getString("direction"));
        repository.Update(item);
        return Json.createObjectBuilder()
            .add("id", item.getId())
            .add("code", item.getCode())
            .add("name", item.getName())
            .add("lastName", item.getLastName())
            .add("active", item.isActive())
            .add("areaid", item.getAreaid())
            .add("direction", item.getDirection())
            .add("createdDate", item.getCreatedDate().toString())
            .add("lastBillingDate", item.getLastBillingDate().toString()).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("create")
    @JWTTokenNeeded
    public JsonObject Create(String content) {
        JsonObject jsonObject = Json.createReader(new StringReader(content)).readObject();
        Settings setting = settingsRepository.Get(CLIENT_CODE_INCREMENTAL);
        Integer newCode = Integer.parseInt(setting.getSettingValue())+1;
        Client item = new Client("CTE-"+newCode.toString(), 
                jsonObject.getString("name"),
                jsonObject.getString("lastName"), 
                jsonObject.getBoolean("active"), 
                jsonObject.getInt("areaid"), 
                jsonObject.getString("direction"), new Date(), new Date()
        );
        repository.Create(item);
        setting.setSettingValue(newCode.toString());
        settingsRepository.Update(setting);
        return Json.createObjectBuilder()
            .add("id", item.getId())
            .add("code", item.getCode())
            .add("name", item.getName())
            .add("lastName", item.getLastName())
            .add("active", item.isActive())
            .add("areaid", item.getAreaid())
            .add("direction", item.getDirection())
            .add("createdDate", item.getCreatedDate().toString())
            .add("lastBillingDate", item.getLastBillingDate().toString()).build();
    }
    
    @DELETE
    @Path("delete/{id}")
    @JWTTokenNeeded
    public JsonObject Delete(@PathParam("id") String id) {
        repository.Delete(repository.Get("Client", Integer.parseInt(id)));
        return Json.createObjectBuilder()
                .add("result", true)
                .build();
    }
    
}
