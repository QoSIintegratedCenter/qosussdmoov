package com.ks.qosussd.qosussd.web.mtn;

import com.ks.qosussd.qosussd.soapdto.UssdRequest;
import com.ks.qosussd.qosussd.soapdto.UssdResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
@RequestMapping("mtn")
@Slf4j
public class MtnRessource {

    @PostMapping( value = "start", produces = MediaType.APPLICATION_XML_VALUE, consumes =MediaType.APPLICATION_XML_VALUE )
    public UssdResponse start(@RequestBody UssdRequest ussdRequest){
        log.info("start MTN USSD ");

        log.info("data {}", ussdRequest);
        UssdResponse ussdResponse = new UssdResponse();
        ussdResponse.setApplicationResponse("Welcome to mtn");
        ussdResponse.setMsisdn(ussdRequest.getMsisdn());

        return new UssdResponse();
    }

}
