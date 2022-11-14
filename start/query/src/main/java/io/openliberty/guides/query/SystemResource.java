// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.query;

import java.net.URI;
import java.util.Base64;
import java.util.Properties;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import io.openliberty.guides.query.client.SystemClient;
import io.openliberty.guides.query.client.UnknownUriExceptionMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/systems")
public class SystemResource {

    @Inject
    @ConfigProperty(name = "system.httpPort")
    private String systemHttpPort;

    @Inject
    @ConfigProperty(name = "system.user")
    private String systemUser;

    @Inject
    @ConfigProperty(name = "system.password")
    private String systemPassword;

    @Inject
    @ConfigProperty(name = "system.contextRoot")
    private String systemContextRoot;

    @GET
    @Path("/{hostname}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Properties getSystemProperties(@PathParam("hostname") String hostname) {

        SystemClient systemClient = null;
        Properties p = new Properties();

        try {
            String uriString = "http://" + hostname + ":" + systemHttpPort
                               + "/" + systemContextRoot;
            URI customURI = URI.create(uriString);
            systemClient = RestClientBuilder.newBuilder()
                .baseUri(customURI)
                .register(UnknownUriExceptionMapper.class)
                .build(SystemClient.class);
        } catch (Exception e) {
            p.put("fail", "Failed to create the client " + hostname + ".");
            return p;
        }

        String systemUserPassword = systemUser + ":" + systemPassword;
        String authHeader = "Basic "
               + Base64.getEncoder().encodeToString(systemUserPassword.getBytes());

        try {
            p.put("hostname", hostname);
            p.put("os.name", systemClient.getProperty(authHeader, "os.name"));
            p.put("java.version", systemClient.getProperty(authHeader, "java.version"));
        } catch (Exception e) {
            p.put("fail", "Failed to reach the client " + hostname + ".");
            return p;
        } finally {
            try {
                systemClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return p;
    }

}
