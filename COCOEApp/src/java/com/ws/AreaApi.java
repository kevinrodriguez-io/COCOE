/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws;

import filters.JWTTokenNeeded;
import dao.Area;
import dao.AreaRepository;
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
 * Fully working area api endpoint, it communicates with 
 * Hibernate via Repository Pattern
 * @author COCOE
 */
@Path("area")
public class AreaApi {
    
    public static final String AREA_CODE_INCREMENTAL = "AREACODEINCREMENTAL";
    
    @Context
    private UriInfo context;
    
    private AreaRepository repository = new AreaRepository();
    private SettingsRepository settingsRepository = new SettingsRepository();
    
    public AreaApi(){}
    
    @GET
    @Path("/")
    @JWTTokenNeeded
    public JsonArray All() {
        List<Area> items = repository.All("Area");
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Area item : items) {
            jsonArrayBuilder.add(Json.createObjectBuilder()
                .add("id", item.getId())
                .add("code", item.getCode())
                .add("name", item.getName())
                .add("createdDate", item.getCreatedDate().toString()));
        }
        return jsonArrayBuilder.build();
    }
    
    @GET
    @Path("{id}")
    @JWTTokenNeeded
    public JsonObject Find(@PathParam("id") String id) {
        Area item = repository.Get("Area", Integer.parseInt(id));
        return Json.createObjectBuilder()
                .add("id", item.getId())
                .add("code", item.getCode())
                .add("name", item.getName())
                .add("createdDate", item.getCreatedDate().toString()).build();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("edit")
    @JWTTokenNeeded
    public JsonObject Edit(String content) {
        JsonObject jsonObject = Json.createReader(new StringReader(content)).readObject();
        int areaId = jsonObject.getInt("id");
        Area item = repository.Get("Area", areaId);
        item.setName(jsonObject.getString("name"));
        repository.Update(item);
        return Json.createObjectBuilder()
            .add("id", item.getId())
            .add("code", item.getCode())
            .add("name", item.getName())
            .add("createdDate", item.getCreatedDate().toString()).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("create")
    @JWTTokenNeeded
    public JsonObject Create(String content) {
        JsonObject jsonObject = Json.createReader(new StringReader(content)).readObject();
        
        Settings setting = settingsRepository.Get(AREA_CODE_INCREMENTAL);
        Integer newCode = Integer.parseInt(setting.getSettingValue())+1;
        Area item = new Area(
            jsonObject.getString("name"),
            "ARE-"+newCode,
            new Date()
        );
        repository.Create(item);
        setting.setSettingValue(newCode.toString());
        settingsRepository.Update(setting);
        return Json.createObjectBuilder()
            .add("id", item.getId())
            .add("code", item.getCode())
            .add("name", item.getName())
            .add("createdDate", item.getCreatedDate().toString()).build();
    }
    
    @DELETE
    @Path("delete/{id}")
    @JWTTokenNeeded
    public JsonObject Delete(@PathParam("id") String id) {
        Area item = repository.Get("Area", Integer.parseInt(id));
        repository.Delete(item);
        return Json.createObjectBuilder()
                .add("result", true)
                .build();
    }
    
}
