package com.jonathancolt.nicity.profile.server;

/*
 * #%L
 * nicity-profile-server
 * %%
 * Copyright (C) 2013 Jonathan Colt
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.jonathancolt.nicity.profile.server.endpoints.ProfileService;
import com.sun.jersey.api.container.filter.PostReplaceFilter;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;

public class PerfServerJetty {

    public static void main(String[] args) throws Exception {
        Server server = new Server(9090);

        final ServicesCallDepthStack callDepthStack = new ServicesCallDepthStack();

        FilterHolder filterHolder = new FilterHolder(CrossOriginFilter.class);
        filterHolder.setInitParameter("allowedOrigins", "*");
        filterHolder.setInitParameter("allowedMethods", "GET, POST");

        ServletContextHandler root = new ServletContextHandler(server, "/", ServletContextHandler.NO_SESSIONS);
        //root.setErrorHandler(new SegmentsErrorHandler(objectMapper, new Log4jLoggerServerErrorRecorder(objectMapper)));

        final String JERSEY_RESOURCE_PACKAGES = "com.jonathancolt.nicity.profile.server.endpoints";
        Map<String, Object> jerseyProps = new HashMap<String, Object>() {
            {
                put(PackagesResourceConfig.PROPERTY_PACKAGES, JERSEY_RESOURCE_PACKAGES);
                put(PackagesResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, PostReplaceFilter.class.getName());
                //put(PackagesResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES, CacheControlResourceFilterFactory.class.getName());
            }
        };
        PackagesResourceConfig jerseyResourceConfig = new PackagesResourceConfig(jerseyProps);
        jerseyResourceConfig.getSingletons().add(new SingletonTypeInjectableProvider<javax.ws.rs.core.Context, AtomicBoolean>(AtomicBoolean.class, new AtomicBoolean(false)) {
        });
        jerseyResourceConfig.getSingletons().add(
            new SingletonTypeInjectableProvider<javax.ws.rs.core.Context, ProfileService>(ProfileService.class, new ProfileService(callDepthStack)) {
        });

        ServletHolder servletHolder = new ServletHolder(new ServletContainer(jerseyResourceConfig));
        root.addServlet(servletHolder, "/*");

        server.start();
        server.join();

    }
}
