/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws;

import dao.LoginRequest;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author COCOE
 */
@Path("UserApi")
public class UserApi {
    
    @Context
    private UriInfo context;
    
    public UserApi(){}
      
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login")
    public JsonObject Login(String content) {
        JsonObject object = Json.createReader(new StringReader(content)).readObject();
        String username = object.getString("username");
        String password = object.getString("password");
        if (username == "admin" && password == "sanpedro123!") {
            return Json.createObjectBuilder()
                .add("result", true)
                .add("message", "Successful login").build();
        } else {
            return Json.createObjectBuilder()
                .add("result", false)
                .add("message", "Failed login").build();
        }
    }
    
}
