/*
 * $Revision$
 * $Date$
 *
 * Copyright (C) 1999-$year$ Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
 */
package com.jonathancolt.nicity.profile.viewer.http;

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

import com.jonathancolt.nicity.profile.server.ProfileViewerProvider;
import com.jonathancolt.nicity.profile.server.model.ServiceModel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 */
public class HttpProfileViewerProvider implements ProfileViewerProvider {

    private final JsonApacheHttpClient jsonApacheHttpClient;

    public HttpProfileViewerProvider(String hostName, int port) {
        HttpClient httpClient = createApacheClient(hostName, port, 10, 30000);
        jsonApacheHttpClient = new JsonApacheHttpClient(new ObjectMapper(), httpClient, new HashMap<String, String>());
    }

    @Override
    public ServiceModel getServiceModel(String serviceName) {
        URI uri;
        try {
            uri= new URI("/service/model?serviceName="+serviceName, false, "UTF-8");
        } catch(URIException | NullPointerException x ) {
            throw new RuntimeException(x);
        }
        return jsonApacheHttpClient.get(uri.toString(), ServiceModel.class);

    }

    @Override
    public List<String> getServiceNames() {
        return Arrays.asList(jsonApacheHttpClient.get("/service/names", String[].class));
    }

    private org.apache.commons.httpclient.HttpClient createApacheClient(String host, int port, int maxConnections, int socketTimeoutInMillis) {

        HttpConnectionManager connectionManager = createConnectionManager(maxConnections);

        org.apache.commons.httpclient.HttpClient client =
                new org.apache.commons.httpclient.HttpClient(connectionManager);
        client.getParams().setParameter(HttpMethodParams.COOKIE_POLICY, CookiePolicy.RFC_2109);
        client.getParams().setParameter(HttpMethodParams.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        client.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, false);
        client.getParams().setBooleanParameter(HttpConnectionParams.STALE_CONNECTION_CHECK, true);
        client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT,
                socketTimeoutInMillis > 0 ? socketTimeoutInMillis : 0);
        client.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,
                socketTimeoutInMillis > 0 ? socketTimeoutInMillis : 0);


        HostConfiguration hostConfiguration = new HostConfiguration();
        configureSsl(hostConfiguration, host, port);
        configureProxy(hostConfiguration);

        client.setHostConfiguration(hostConfiguration);
        return client;

    }

    private void configureProxy(HostConfiguration hostConfiguration) {
//        HttpClientProxyConfig httpClientProxyConfig = locateConfig(HttpClientProxyConfig.class, null);
//        if (httpClientProxyConfig != null) {
//            hostConfiguration.setProxy(httpClientProxyConfig.getProxyHost(), httpClientProxyConfig.getProxyPort());
//            if (hasValidProxyUsernameAndPasswordSettings(httpClientProxyConfig)) {
//                HttpState state = new HttpState();
//                state.setProxyCredentials(AuthScope.ANY, new UsernamePasswordCredentials(httpClientProxyConfig.getProxyUsername(),
//                        httpClientProxyConfig.getProxyPassword()));
//                httpClient.setState(state);
//            }
//        }
    }

    private void configureSsl(HostConfiguration hostConfiguration, String host, int port) throws IllegalStateException {
//        HttpClientSSLConfig httpClientSSLConfig = locateConfig(HttpClientSSLConfig.class, null);
//        if (httpClientSSLConfig != null) {
//            Protocol sslProtocol;
//            if (httpClientSSLConfig.getCustomSSLSocketFactory() != null) {
//                sslProtocol = new Protocol(HTTPS_PROTOCOL,
//                        new CustomSecureProtocolSocketFactory(httpClientSSLConfig.getCustomSSLSocketFactory()), SSL_PORT);
//            } else {
//                sslProtocol = Protocol.getProtocol(HTTPS_PROTOCOL);
//            }
//            hostConfiguration.setHost(host, port, sslProtocol);
//            httpClient.setUsingSSL();
//        } else {
        hostConfiguration.setHost(host, port);
//        }
    }

    private HttpConnectionManager createConnectionManager(int maxConnections) {
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        if (maxConnections > 0) {
            connectionManager.getParams().setMaxTotalConnections(maxConnections);
        }
        return connectionManager;
    }
}
