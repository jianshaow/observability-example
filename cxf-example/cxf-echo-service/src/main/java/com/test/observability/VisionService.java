package com.test.observability;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/vision")
public interface VisionService {

    @POST
    @Path("/see")
    void see();
}
