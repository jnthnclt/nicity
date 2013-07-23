/*
 * $Revision$
 * $Date$
 *
 * Copyright (C) 1999-$year$ Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
 */
package com.jonathancolt.nicity.profile.server.endpoints;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 */
@Path("/")
public class PerfServiceEndpoint {
    
    static ObjectMapper mapper = new ObjectMapper();
    @Context
    PerfService perfService;

    @GET
    @Path("/hello")
    public String hello() {
        return "yo";
    }

    @POST
    @Path("/latents")
    public String latents(String latents) throws IOException {
        perfService.getCallDepthStack().call(mapper.readValue(latents, LatentSample.class));
        return "yo";
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
