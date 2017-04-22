/*
 * Created by Alex Martin on 2017.04.20  * 
 * Copyright © 2017 Alex Martin. All rights reserved. * 
 */
package com.mycompany.webservices.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author alexmartin
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

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
        resources.add(com.mycompany.webservices.service.MeetingFacadeREST.class);
        resources.add(com.mycompany.webservices.service.MeetingUsersFacadeREST.class);
        resources.add(com.mycompany.webservices.service.UserFacadeREST.class);
    }
    
}
