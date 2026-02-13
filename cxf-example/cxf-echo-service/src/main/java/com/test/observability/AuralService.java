package com.test.observability;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/aural")
public interface AuralService {

    @POST
    @Path("/hear")
    String hear(String msg);
}
