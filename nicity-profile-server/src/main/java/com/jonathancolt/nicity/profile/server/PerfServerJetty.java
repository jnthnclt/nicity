package com.jonathancolt.nicity.profile.server;

import com.jonathancolt.nicity.profile.model.ServicesCallDepthStack;
import com.jonathancolt.nicity.profile.server.endpoints.PerfService;
import com.jonathancolt.nicity.profile.visualize.Heat;
import com.jonathancolt.nicity.profile.visualize.NameUtils;
import com.jonathancolt.nicity.profile.visualize.VCallDepthStack;
import com.jonathancolt.nicity.profile.visualize.VLatency;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.ViewColor;
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

/**
 * Hello world!
 *
 */
public class PerfServerJetty {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ViewColor.onBlack();
        final ServicesCallDepthStack callDepthStack = new ServicesCallDepthStack();
        NameUtils nameUtils = new NameUtils();

        final VCallDepthStack vCallDepthStack = new VCallDepthStack(nameUtils, callDepthStack, new Heat(), 1000, 800);
        UV.exitFrame(new VLatency(vCallDepthStack), "Latent Server");

        FilterHolder filterHolder = new FilterHolder(CrossOriginFilter.class);
        filterHolder.setInitParameter("allowedOrigins", "*");
        filterHolder.setInitParameter("allowedMethods", "GET, POST");

        ServletContextHandler root = new ServletContextHandler(server, "/", ServletContextHandler.NO_SESSIONS);
        //root.setErrorHandler(new SegmentsErrorHandler(objectMapper, new Log4jLoggerServerErrorRecorder(objectMapper)));

        final String JERSEY_RESOURCE_PACKAGES = "colt.nicity.performance.server.endpoints";
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
            new SingletonTypeInjectableProvider<javax.ws.rs.core.Context, PerfService>(PerfService.class, new PerfService(callDepthStack)) {
        });

        ServletHolder servletHolder = new ServletHolder(new ServletContainer(jerseyResourceConfig));

        root.addServlet(servletHolder, "/*");

        server.start();
        server.join();

    }
}
