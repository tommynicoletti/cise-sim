package eu.cise.emulator.api;

import com.fasterxml.jackson.databind.JsonNode;
import eu.cise.emulator.MessageProcessor;
import eu.cise.emulator.SendParam;
import eu.cise.emulator.api.helpers.SendParamsReader;
import eu.cise.emulator.api.resources.WebAPIMessageResource;
import eu.cise.emulator.io.MessageStorage;
import eu.cise.emulator.templates.Template;
import eu.cise.emulator.templates.TemplateLoader;
import eu.cise.emulator.utils.Pair;
import eu.cise.servicemodel.v1.message.Acknowledgement;
import eu.cise.servicemodel.v1.message.Message;
import eu.eucise.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

import static eu.eucise.helpers.PushBuilder.newPush;

public class DefaultMessageAPI implements MessageAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebAPIMessageResource.class);
    private final MessageStorage messageStorage;
    private final MessageProcessor messageProcessor;
    private final XmlMapper xmlMapper;
    private final TemplateLoader templateLoader;

    DefaultMessageAPI(MessageProcessor messageProcessor,
                      MessageStorage messageStorage,
                      TemplateLoader templateLoader, XmlMapper xmlMapper) {

        this.messageProcessor = messageProcessor;
        this.messageStorage = messageStorage;
        this.xmlMapper = xmlMapper;
        this.templateLoader = templateLoader;

        LOGGER.debug(" Initialize the MessageAPI with default type implementation {} using message processor of type {}", this.getClass(), (messageProcessor != null ? messageProcessor.getClass() : ""));
    }

    @Override
    public MessageApiDto send(String templateId, JsonNode params) {
        LOGGER.debug("send is passed through api templateId: {}, params: {}", templateId, params);
        Template template = templateLoader.loadTemplate(templateId);
        String xmlContent = template.getTemplateContent();
        SendParam sendParam = new SendParamsReader().extractParams(params);
        Message message = xmlMapper.fromXML(xmlContent);

        try {
            Pair<Acknowledgement, Message> sendResposnse = messageProcessor.send(message, sendParam);
            return new MessageApiDto(Response.Status.ACCEPTED.getStatusCode(), null, xmlMapper.toXML(sendResposnse.getA()), xmlMapper.toXML(sendResposnse.getB()));
        } catch (Exception e) {
            LOGGER.error("error in Api send", e);
            return new MessageApiDto(Response.Status.BAD_REQUEST.getStatusCode(), "Send Error: " + e.getMessage(), "", "");
        }
    }

    @Override
    public Acknowledgement receive(String content) {
        LOGGER.debug("receive is receiving through api : {}", content.substring(0, 200));
        try {
            Message message = xmlMapper.fromXML(content);
            return messageProcessor.receive(message);
        } catch (Exception e) {
            LOGGER.error("error in api send", e);
            throw new RuntimeException("Exception while receiving a message that should be handled.", e);
        }
    }

    @Override
    public MessageApiDto getLastStoredMessage() {
        return (MessageApiDto) messageStorage.read();
    }

    @Override
    public MessageApiDto preview(SendParam param, String templateHash) {
        //TODO: read the template from fileStorage
        Message message = newPush().build();
        Message preview = messageProcessor.preview(message, param);
        return new MessageApiDto(Response.Status.OK.getStatusCode(), "", "", xmlMapper.toXML(preview));
    }

    public boolean consumeStoredMessage(MessageApiDto storedMessage) {
        //TODO: read the template from fileStorage
        return messageStorage.delete(storedMessage);
    }


}
