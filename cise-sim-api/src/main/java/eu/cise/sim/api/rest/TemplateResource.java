package eu.cise.sim.api.rest;

import com.fasterxml.jackson.databind.JsonNode;
import eu.cise.sim.api.*;
import eu.cise.sim.api.representation.TemplateParams;
import eu.cise.sim.templates.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/ui/templates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TemplateResource {

    private final Logger logger;
    private final MessageAPI messageAPI;
    private final TemplateAPI templateAPI;

    public TemplateResource(MessageAPI messageAPI, TemplateAPI templateAPI) {
        this.messageAPI = messageAPI;
        this.templateAPI = templateAPI;
        this.logger = LoggerFactory.getLogger(TemplateResource.class);
    }

    @GET
    public Response getTemplates() {
        logger.debug("requesting messages templates to the server");

        TemplateListResponse templateListResponse = templateAPI.getTemplates();

        List<Template> templateList = templateListResponse.getTemplates();

        if (!templateListResponse.isOk()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new APIError(templateListResponse.getError())).build();
        }

        return Response.status(Response.Status.OK)
            .entity(templateList)
            .build();
    }


    @GET
    @Path("{templateId}")
    public Response getTemplateById(
        @PathParam("templateId") String templateId,
        @QueryParam("messageId") String messageId,
        @QueryParam("correlationId") String correlationId,
        @QueryParam("requestAck") boolean requestAck) {

        PreviewResponse previewResponse = templateAPI.preview(
            new TemplateParams(templateId, messageId, correlationId, requestAck));

        if (!previewResponse.isOk()) {
            APIError apiError = new APIError(previewResponse.getErrorMessage());
            return Response.serverError().entity(apiError).build();
        }

        return Response.ok().entity(previewResponse.getTemplate()).build();
    }

    @POST
    @Path("{templateId}")
    public Response send(@PathParam("templateId") String templateId, JsonNode msgWithParams) {
        logger.debug("sending template: {} with param: {}", templateId, msgWithParams);

        SendResponse sendResponse = messageAPI.send(templateId, msgWithParams);
        if (!sendResponse.isOk()) {
            APIError apiError = new APIError(sendResponse.getErrorMessage());
            return Response.serverError().entity(apiError).build();
        }

        return Response
            .status(Response.Status.CREATED)
            .entity(sendResponse.getContents())
            .build();
    }

}