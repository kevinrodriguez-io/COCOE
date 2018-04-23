/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws;

import filters.JWTTokenNeeded;
import dao.Settings;
import dao.SettingsRepository;
import java.io.StringReader;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * Fully working area api endpoint, it communicates with 
 * Hibernate via Repository Pattern
 * @author COCOE
 */
@Path("settings")
public class SettingsApi {
    
    @Context
    private UriInfo context;
    
    private SettingsRepository repository = new SettingsRepository();
    
    public SettingsApi(){}
    
    @GET
    @Path("/")
    @JWTTokenNeeded
    public JsonArray All() {
        List<Settings> items = repository.All("Settings");
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Settings item : items) {
            jsonArrayBuilder.add(Json.createObjectBuilder()
                .add("settingKey", item.getSettingKey())
                .add("settingValue", item.getSettingValue())
            );
        }
        return jsonArrayBuilder.build();
    }
    
    @GET
    @Path("{id}")
    @JWTTokenNeeded
    public JsonObject Find(@PathParam("id") String id) {
        Settings item = repository.Get("Settings", Integer.parseInt(id));
        return Json.createObjectBuilder()
            .add("settingKey", item.getSettingKey())
            .add("settingValue", item.getSettingValue())
            .build();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("edit")
    @JWTTokenNeeded
    public JsonObject Edit(String content) {
        JsonObject jsonObject = Json.createReader(new StringReader(content)).readObject();
        Settings item = repository.Get("Settings", Integer.parseInt(jsonObject.getString("settingKey")));
        item.setSettingValue(jsonObject.getString("settingValue"));
        repository.Update(item);
        return jsonObject;
    }
    
}
