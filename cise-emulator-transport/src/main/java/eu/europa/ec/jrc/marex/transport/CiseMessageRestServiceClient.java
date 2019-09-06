package eu.europa.ec.jrc.marex.transport;

import eu.cise.accesspoint.service.v1.CISEMessageServiceSoapImpl;
import eu.cise.servicemodel.v1.message.Acknowledgement;
import eu.cise.servicemodel.v1.message.Message;

import javax.ws.rs.core.Configuration;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class CiseMessageRestServiceClient {
    private URL wsdlURL;
    private String serviceURI;
    private String LocalPartURI ;


    public void CiseMessageRestServiceClient (String urlString, String uriString,String localPart) throws MalformedURLException, URISyntaxException {
        wsdlURL = new URL(urlString);
        serviceURI = uriString;
        LocalPartURI = localPart;
    }

    public Acknowledgement send(Message message){
        Acknowledgement resultAcknowledgement= new Acknowledgement();
        try {

            QName SERVICE_NAME = new QName(this.serviceURI, this.LocalPartURI);
            Service service = Service.create(this.wsdlURL, SERVICE_NAME);
            CISEMessageServiceSoapImpl client = service.getPort(CISEMessageServiceSoapImpl.class);
             resultAcknowledgement = client.send(message);
        } catch (Exception e){

        }
        return resultAcknowledgement;
    }


    public Configuration getConfiguration() {
        return null;
    }

}


/* other option use a dispatch api https://cxf.apache.org/docs/how-do-i-develop-a-client.html
import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
...

URL wsdlURL = new URL("http://localhost/hello?wsdl");
Service service = Service.create(wsdlURL, new QName("HelloService"));
Dispatch<Source> disp = service.createDispatch(new QName("HelloPort"), Source.class, Service.Mode.PAYLOAD);

Source request = new StreamSource("<hello/>")
Source response = disp.invoke(request);
 */