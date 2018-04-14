/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws;

import dao.Area;
import dao.AreaRepository;
import dao.LoginRequest;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author COCOE
 */
@Path("AreaApi")
public class AreaApi {
    
    @Context
    private UriInfo context;
    
    private AreaRepository repository = new AreaRepository();
    
    public AreaApi(){}
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    public JsonArray All() {
        List<Area> items = repository.All("area");
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
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public JsonObject Find(@PathParam("id") String id) {
        Area item = repository.Get("area", Integer.parseInt(id));
        return Json.createObjectBuilder()
                .add("id", item.getId())
                .add("code", item.getCode())
                .add("name", item.getName())
                .add("createdDate", item.getCreatedDate().toString()).build();
    }
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("edit")
    public JsonObject Edit(String content) {
        JsonObject jsonObject = Json.createReader(new StringReader(content)).readObject();
        Area item = new Area();
        item.setId(jsonObject.getInt("id"));
        item.setCode(jsonObject.getString("code"));
        item.setName(jsonObject.getString("name"));
        try {
            item.setCreatedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("createdDate")));
        } catch (ParseException ex) {
            System.err.println(ex.getMessage());
        }
        repository.Update(item);
        return jsonObject;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("create")
    public JsonObject Create(String content) {
        JsonObject jsonObject = Json.createReader(new StringReader(content)).readObject();
        Area item = new Area();
        item.setId(jsonObject.getInt("id"));
        item.setCode(jsonObject.getString("code"));
        item.setName(jsonObject.getString("name"));
        try {
            item.setCreatedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("createdDate")));
        } catch (ParseException ex) {
            System.err.println(ex.getMessage());
        }
        repository.Create(item);
        return jsonObject;
    }
    
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("delete")
    public JsonObject Delete(String content) {
        JsonObject jsonObject = Json.createReader(new StringReader(content)).readObject();
        Area item = new Area();
        item.setId(jsonObject.getInt("id"));
        item.setCode(jsonObject.getString("code"));
        item.setName(jsonObject.getString("name"));
        try {
            item.setCreatedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("createdDate")));
        } catch (ParseException ex) {
            System.err.println(ex.getMessage());
        }
        repository.Delete(item);
        return Json.createObjectBuilder()
                .add("result", true)
                .build();
    }
    
}
