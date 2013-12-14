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

import com.jonathancolt.nicity.profile.viewer.VCallDepthStack;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.image.IImage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 */
@Path("/")
public class ProfileViewerServiceEndpoint {

    static ObjectMapper mapper = new ObjectMapper();
    @Context
    ProfileViewerService profileViewerService;

    @GET
    @Path("/hello")
    public String hello() {
        return "yo";
    }


    @Path("/png/snapshot")
    @GET
    public Response getFullImage() throws IOException {
        System.out.println("Rendering PNG");
        VCallDepthStack callDepthStack = profileViewerService.getCallDepthStack();
        IImage i = UV.toImage(callDepthStack);
        System.out.println("image:"+i);

        BufferedImage image = (BufferedImage) i.data(0);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageData = baos.toByteArray();

        // uncomment line below to send non-streamed
        return Response.ok(imageData, "image/png").build();

        //uncomment line below to send streamed
        //return Response.ok(new ByteArrayInputStream(imageData)).build();
    }
}
