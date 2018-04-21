/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws;

import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author COCOE
 */
@ApplicationPath("api")
public class ServiceConfig extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(auth.JWTTokenNeededFilter.class);
        resources.add(com.ws.AreaApi.class);
        //resources.add(com.ws.Acciones.class);
        resources.add(com.ws.ClientApi.class);
        resources.add(com.ws.MetersessionApi.class);
        resources.add(com.ws.SettingsApi.class);
        resources.add(com.ws.UserApi.class);
    }
}
