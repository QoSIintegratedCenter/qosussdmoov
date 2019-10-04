package com.ks.qosussd.qosussd.web.mtn;

import com.ks.qosussd.qosussd.soapdto.Freeflow;
import com.ks.qosussd.qosussd.soapdto.UssdRequest;
import com.ks.qosussd.qosussd.soapdto.UssdResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ks.qosussd.qosussd.core.Constants.LOG_MTN;

@RestController
@RequestMapping("mtn")
@Slf4j
public class MtnRessource {

    /**
     * Main class for ussd mtn modèle
     *
     * @param ussdRequest
     * @return
     */

    @PostMapping( value = "start", produces = MediaType.APPLICATION_XML_VALUE, consumes =MediaType.APPLICATION_XML_VALUE )
    public UssdResponse start(@RequestBody UssdRequest ussdRequest) {
        log.info(LOG_MTN + "start MTN USSD build by Jacques KOMACLO for Qos");

        log.info(LOG_MTN + "data requset {}", ussdRequest);
        UssdResponse ussdResponse = new UssdResponse();
        ussdResponse.setApplicationResponse("Welcome to mtn Qos USSD");
        ussdResponse.setMsisdn(ussdRequest.getMsisdn());
        Freeflow freeflow = new Freeflow();
        freeflow.setFreeflowState("FB");
        ussdResponse.setFreeflow(freeflow);

        return ussdResponse;
    }

}
