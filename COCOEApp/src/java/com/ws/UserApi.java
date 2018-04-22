/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws;

import filters.JWTTokenNeeded;
import dao.Area;
import dao.User;
import dao.UserRepository;
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
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.json.JsonArray;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import util.HashUtil;
import util.SimpleKeyGenerator;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

/**
 * Here is where the JWT magic happens
 * @author COCOE
 */
@Path("user")
public class UserApi {
    
    @Context
    private UriInfo context;
    
    private UserRepository repository = new UserRepository();
    
    public UserApi(){}
    
    // -------------------------------------------
    //
    //  AUTH METHODS FOR JWT
    //
    // -------------------------------------------
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login")
    public Response Login(String content) {
        
        JsonObject object = Json.createReader(new StringReader(content)).readObject();

        String username = object.getString("userName");
        String password = object.getString("password");
        String passwordHash = HashUtil.HashToSHA256Base64(password);
        
        if (ValidateUserPasswordCombination(username, passwordHash)) {
            Key key = new SimpleKeyGenerator().generateKey();
            String jwtToken = Jwts.builder()
                    .setSubject(username)
                    .setIssuer(context.getAbsolutePath().toString())
                    .setIssuedAt(new Date())
                    .setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
                    .signWith(SignatureAlgorithm.HS512, key)
                    .compact();
            return Response
                    .ok()
                    .header(AUTHORIZATION, "Bearer " + jwtToken)
                    .entity("{ \"Message\": \"Login successful\", \"Bearer\": \""+jwtToken+"\" }")
                    .build();
        } else {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("{\"Message\": \"Login failed\"}")
                    .build();
        }
        
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("register")
    public Response Register(String content) {
        
        JsonObject object = Json.createReader(new StringReader(content)).readObject();
        
        String username = object.getString("userName");
        String password = object.getString("password");
        String passwordHash = HashUtil.HashToSHA256Base64(password);
        String name = object.getString("name");
        String lastName = object.getString("lastName");
        
        if (repository.All("User").stream().anyMatch(I->I.getUserName().equals(username))) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity("{\"Message\": \"User already exists\"}")
                    .build();
        }
        
        repository.Create(new User(username, passwordHash, name, lastName, "EMPLOYEE", new Date(), new Date()));
        
        Key key = new SimpleKeyGenerator().generateKey();
        String jwtToken = Jwts.builder()
            .setSubject(username)
            .setIssuer(context.getAbsolutePath().toString())
            .setIssuedAt(new Date())
            .setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
            .signWith(SignatureAlgorithm.HS512, key)
            .compact();
        
        return Response
                .ok()
                .header(AUTHORIZATION, "Bearer " + jwtToken)
                .entity("{ \"Message\": \"Register successful\", \"Bearer\": \""+jwtToken+"\" }")
                .build();
        
    }
    
    // -------------------------------------------
    //
    //  CRUD METHODS
    //
    // -------------------------------------------
    
    @GET
    @Path("/")
    @JWTTokenNeeded
    public JsonArray All() {
        List<User> items = repository.All("User");
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (User item : items) {
            jsonArrayBuilder.add(Json.createObjectBuilder()
                .add("id", item.getId())
                .add("userName", item.getUserName())
                .add("name", item.getName())
                .add("lastName", item.getLastName())
                .add("role", item.getRole())
                .add("createdDate", item.getCreatedDate().toString())
                .add("lastLoginDate", item.getLastLoginDate().toString())
            );
        }
        return jsonArrayBuilder.build();
    }
    
    @GET
    @Path("{id}")
    @JWTTokenNeeded
    public JsonObject Find(@PathParam("id") String id) {
        User item = repository.Get("User", Integer.parseInt(id));
        return Json.createObjectBuilder()
                .add("id", item.getId())
                .add("userName", item.getUserName())
                .add("name", item.getName())
                .add("lastName", item.getLastName())
                .add("role", item.getRole())
                .add("createdDate", item.getCreatedDate().toString())
                .add("lastLoginDate", item.getLastLoginDate().toString()
            ).build();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("edit")
    @JWTTokenNeeded
    public JsonObject Edit(String content) {
        JsonObject jsonObject = Json.createReader(new StringReader(content)).readObject();
        User item = repository.Get("User", jsonObject.getInt("id"));
        item.setName(jsonObject.getString("name"));
        item.setLastName(jsonObject.getString("lastName"));
        item.setRole(jsonObject.getString("role"));
        repository.Update(item);
        return jsonObject;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("create")
    @JWTTokenNeeded
    public JsonObject Create(String content) {
        JsonObject jsonObject = Json.createReader(new StringReader(content)).readObject();
        
        User user = new User(
            jsonObject.getString("userName"), 
            HashUtil.HashToSHA256Base64(jsonObject.getString("password")), 
            jsonObject.getString("name"), 
            jsonObject.getString("lastName"), 
            jsonObject.getString("role"), 
            new Date()
        );
       
        repository.Create(user);
        return jsonObject;
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("delete/{id}")
    @JWTTokenNeeded
    public JsonObject Delete(@PathParam("id") String id) {
        repository.Delete(repository.Get("User", Integer.parseInt(id)));
        return Json.createObjectBuilder()
                .add("result", true)
                .build();
    }
    
    // -------------------------------------------
    //
    //  Private parts u///u
    //
    // -------------------------------------------
    
    private Boolean ValidateUserPasswordCombination(String user, String password) {
        return repository.All("User").stream()
            .filter(U -> 
                U.getUserName().equals(user) && 
                U.getPasswordHash().equals(password)
            ).count() > 0;
    }
    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
}
