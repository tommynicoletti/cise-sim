package eu.cise.emulator.app.cli;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import eu.cise.emulator.app.CiseEmuApplication;
import eu.cise.emulator.app.context.SimConfig;
import eu.cise.servicemodel.v1.message.Message;
import eu.cise.signature.SignatureService;
import eu.cise.signature.SignatureServiceBuilder;
import eu.eucise.xml.DefaultXmlMapper;
import eu.eucise.xml.XmlMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


public class CommandLineSenderTest {


    private WireMock mockCiseNode;
    private String simappPropertiesPath;
    private String messagePushPath;

    @Rule
    public WireMockRule server = new WireMockRule(WireMockConfiguration.options().port(64738), true);


    @Before
    public void setup() {

        simappPropertiesPath = getClass().getResource("/simapp.properties").getPath();
        messagePushPath = getClass().getResource("/AisModifiedPush.xml").getPath();
    }


    @Test
    public void is_sending_a_message_from_cli() throws Exception {
        server.stubFor(post(urlEqualTo("/mock-node"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<Push />")));
        CiseEmuApplication.main(new String[]{"sender", "-c" , simappPropertiesPath,"-s", messagePushPath});
    }

    @Test
    public void is_sending_correct_applicationxml_message_from_cli() throws Exception {
        server.stubFor(post(urlEqualTo("/mock-node"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<Push />")));
        CiseEmuApplication.main(new String[]{"sender", "-c" , simappPropertiesPath,"-s", messagePushPath});
        server.verify(postRequestedFor(urlEqualTo("/mock-node")).withHeader("Content-Type", equalTo("application/xml")));
    }
    @Test
    public void is_sending_correct_signed_message_from_cli() throws Exception {
        server.stubFor(post(urlEqualTo("/mock-node"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<Push />")));
        CiseEmuApplication.main(new String[]{"sender", "-c" , simappPropertiesPath,"-s", messagePushPath});
        List<ServeEvent> allServeEvents = getAllServeEvents();
        ServeEvent test= allServeEvents.get(allServeEvents.size()-1);
        String contentBodyString=test.getRequest().getBodyAsString();
        XmlMapper refmapper = new DefaultXmlMapper();
        Message aMessage = refmapper.<Message>fromXML(contentBodyString);
        SignatureServiceBuilder signBuilder = SignatureServiceBuilder.newSignatureService(refmapper);

        //SimConfig config = SimConfig.createMyConfig(simappPropertiesPath.);

        String keyStoreFileName = "private.jks", certificatePassword = "cisecise", certificateKey = "sim",keyStorePassword = "cisecise", counterpartCertificateKey = "tpz";
                    SignatureService signature = signBuilder
                    .withKeyStoreName((String) keyStoreFileName)
                    .withKeyStorePassword((String) certificatePassword)
                    .withPrivateKeyAlias((String) certificateKey)
                    .withPrivateKeyPassword((String) keyStorePassword)
                    .build();
            signature.verify(aMessage);

    }


    @Test
    public void is_sending_a_valid_message_from_cli() throws Exception {
        server.stubFor(post(urlEqualTo("/mock-node"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<Push />")));


        CiseEmuApplication.main(new String[]{"sender", "-c" , simappPropertiesPath,"-s", messagePushPath});
    }

    @After
    public void teardown(){
        WireMock.resetAllRequests();
    }

}
