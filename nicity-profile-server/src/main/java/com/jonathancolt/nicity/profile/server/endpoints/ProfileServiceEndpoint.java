/*
 * $Revision$
 * $Date$
 *
 * Copyright (C) 1999-$year$ Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
 */
package com.jonathancolt.nicity.profile.server.endpoints;

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

import com.jonathancolt.nicity.profile.sample.LatentSample;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VPan;
import com.jonathancolt.nicity.view.core.VString;
import com.jonathancolt.nicity.view.image.IImage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;


@Path("/")
public class ProfileServiceEndpoint {

    static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
    }
    @Context
    ProfileService perfService;

    @GET
    @Path("/hello")
    public String hello() {
        return "yo";
    }

    @POST
    @Path("/latents")
    public String latents(String latents) throws IOException {
        perfService.getCallDepthStack().call(mapper.readValue(latents, LatentSample.class));
        //System.out.println("latents:"+latents);
        return "yo";
    }

    @Path("/service/names")
    @GET
    public String getServiceNames() throws IOException {
        System.out.println("getServiceNames:"+perfService.getCallDepthStack().getServiceNames());
        return mapper.writeValueAsString(perfService.getCallDepthStack().getServiceNames());
    }

    @Path("/service/model")
    @GET
    public String getServiceModel(@QueryParam("serviceName") String serviceName) throws IOException {
        System.out.println("getServiceModel for serviceName:"+serviceName);
        return mapper.writeValueAsString(perfService.getCallDepthStack().getCopy(serviceName));
    }

    @Path("/view")
    @GET
    @Produces("image/png")
    public Response getFullImage() throws IOException {

        IImage i = UV.toImage(new VPan(new VString("hello"), 300, 300));

        BufferedImage image = (BufferedImage) i.data(0);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageData = baos.toByteArray();

        // uncomment line below to send non-streamed
        // return Response.ok(imageData).build();

        //uncomment line below to send streamed
        return Response.ok(new ByteArrayInputStream(imageData)).build();
    }
}
