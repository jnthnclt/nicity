package com.jonathancolt.nicity.profile.server;

/*
 * #%L
 * nicity-profile-viewer
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

import com.jonathancolt.nicity.profile.server.endpoints.ProfileViewerService;
import com.jonathancolt.nicity.profile.viewer.Heat;
import com.jonathancolt.nicity.profile.viewer.NameUtils;
import com.jonathancolt.nicity.profile.viewer.VCallDepthStack;
import com.jonathancolt.nicity.profile.viewer.VLatency;
import com.jonathancolt.nicity.profile.viewer.http.HttpProfileViewerProvider;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.ViewColor;
import com.jonathancolt.nicity.view.rpp.RPPWindow;
import com.sun.jersey.api.container.filter.PostReplaceFilter;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;

/**
 * Hello world!
 *
 */
public class ProfileViewerServerJetty {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ViewColor.onBlack();
        String profileServerHost = "localhost";
        int profileServerPort = 9090;

        HttpProfileViewerProvider httpProfileViewerProvider = new HttpProfileViewerProvider(profileServerHost, profileServerPort);

        NameUtils nameUtils = new NameUtils();

        VCallDepthStack vCallDepthStack = new VCallDepthStack(nameUtils, httpProfileViewerProvider, new Heat(), 1000, 800);
        VLatency vLatency = new VLatency(vCallDepthStack);
        UV.exitFrame(vLatency, "Latent Viewer");
        ProfileViewerService profileViewerService = new ProfileViewerService(vCallDepthStack);

        vCallDepthStack = new VCallDepthStack(nameUtils, httpProfileViewerProvider, new Heat(), 1000, 800);
        vLatency = new VLatency(vCallDepthStack);
        RPPWindow rppWindow = new RPPWindow(vLatency);

        FilterHolder filterHolder = new FilterHolder(CrossOriginFilter.class);
        filterHolder.setInitParameter("allowedOrigins", "*");
        filterHolder.setInitParameter("allowedMethods", "GET, POST");

        ServletContextHandler root = new ServletContextHandler(server, "/", ServletContextHandler.NO_SESSIONS);
        //root.setErrorHandler(new SegmentsErrorHandler(objectMapper, new Log4jLoggerServerErrorRecorder(objectMapper)));

        final String JERSEY_RESOURCE_PACKAGES = "com.jonathancolt.nicity.profile.server.endpoints,com.jonathancolt.nicity.view.server.endpoints";
        Map<String, Object> jerseyProps = new HashMap<String, Object>() {
            {
                put(PackagesResourceConfig.PROPERTY_PACKAGES, JERSEY_RESOURCE_PACKAGES);
                put(PackagesResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, PostReplaceFilter.class.getName());
                put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
                //put(PackagesResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES, CacheControlResourceFilterFactory.class.getName());
            }
        };


        PackagesResourceConfig jerseyResourceConfig = new PackagesResourceConfig(jerseyProps);
        jerseyResourceConfig.getSingletons().add(new SingletonTypeInjectableProvider<javax.ws.rs.core.Context, AtomicBoolean>(AtomicBoolean.class, new AtomicBoolean(false)) {
        });
        jerseyResourceConfig.getSingletons().add(
            new SingletonTypeInjectableProvider<javax.ws.rs.core.Context, ProfileViewerService>(ProfileViewerService.class, profileViewerService) {
        });
        jerseyResourceConfig.getSingletons().add(
            new SingletonTypeInjectableProvider<javax.ws.rs.core.Context, RPPWindow>(RPPWindow.class, rppWindow) {
        });

        ConcurrentSkipListMap<String, ServletContextHandler> servletContextHandlers = new ConcurrentSkipListMap<>();

        String context = "/";
        ServletHolder servletHolder = new ServletHolder(new ServletContainer(jerseyResourceConfig));
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        servletContextHandler.setContextPath("/");
        servletContextHandler.addServlet(servletHolder, (context.endsWith("/")) ? context + "*" : context + "/*");
        servletContextHandlers.put(context, servletContextHandler);


        //root.addServlet(servletHolder, "/*");

        List<Handler> handlers = new LinkedList<Handler>(servletContextHandlers.values());
        HandlerList handlerList = new HandlerList();
        handlerList.setHandlers(handlers.toArray(new Handler[handlers.size()]));
        server.setHandler(handlerList);

        server.start();
        server.join();

    }
}
