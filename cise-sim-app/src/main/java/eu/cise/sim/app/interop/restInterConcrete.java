package eu.cise.sim.app.interop;
import javax.jws.WebParam;
import javax.jws.WebService;


@WebService(endpointInterface = "eu.cise.sim.app.interop.restInter", serviceName = "restInter")
public class restInterConcrete implements restInter {

        @Override
        public String echoCISEMessageServiceREST(@WebParam(name = "text") String text) {
            System.out.println("Receiving text... "+text);
            return "HELLO: "+text;
        }

}