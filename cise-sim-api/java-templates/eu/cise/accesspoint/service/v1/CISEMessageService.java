package eu.cise.accesspoint.service.v1;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.3.3
 * 2020-05-06T18:12:29.415+02:00
 * Generated source version: 3.3.3
 *
 */
@WebServiceClient(name = "CISEMessageService",
                  wsdlLocation = "file:/home/poaless/IdeaProjects/cise-sim-github/cise-sim-api/src/main/resources/META-INF/CISEMessageService.wsdl",
                  targetNamespace = "http://www.cise.eu/accesspoint/service/v1/")
public class CISEMessageService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://www.cise.eu/accesspoint/service/v1/", "CISEMessageService");
    public final static QName CISEMessageServiceSoapPort = new QName("http://www.cise.eu/accesspoint/service/v1/", "CISEMessageServiceSoapPort");
    static {
        URL url = null;
        try {
            url = new URL("file:/home/poaless/IdeaProjects/cise-sim-github/cise-sim-api/src/main/resources/META-INF/CISEMessageService.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(CISEMessageService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/home/poaless/IdeaProjects/cise-sim-github/cise-sim-api/src/main/resources/META-INF/CISEMessageService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public CISEMessageService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public CISEMessageService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CISEMessageService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public CISEMessageService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public CISEMessageService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public CISEMessageService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns CISEMessageServiceSoapImpl
     */
    @WebEndpoint(name = "CISEMessageServiceSoapPort")
    public CISEMessageServiceSoapImpl getCISEMessageServiceSoapPort() {
        return super.getPort(CISEMessageServiceSoapPort, CISEMessageServiceSoapImpl.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CISEMessageServiceSoapImpl
     */
    @WebEndpoint(name = "CISEMessageServiceSoapPort")
    public CISEMessageServiceSoapImpl getCISEMessageServiceSoapPort(WebServiceFeature... features) {
        return super.getPort(CISEMessageServiceSoapPort, CISEMessageServiceSoapImpl.class, features);
    }

}
