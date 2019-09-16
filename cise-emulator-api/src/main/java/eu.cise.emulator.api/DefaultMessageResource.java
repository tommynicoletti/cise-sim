package eu.cise.emulator.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DefaultMessageResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMessageResource.class);
    private final MsgWitParamMapper msgWitParamMapper;


    DefaultMessageResource(MsgWitParamMapper msgWitParamMapper) {
        this.msgWitParamMapper = msgWitParamMapper;
        LOGGER.info("DefaultMessageResource");
    }

    @POST
    @Path("/messages")
    public Response messageCreate(JsonNode msgWithParams) {
        LOGGER.info("messageCreate with param: {}", msgWithParams);
        msgWitParamMapper.map(msgWithParams);
        return Response.status(Response.Status.CREATED).build();
    }

}
