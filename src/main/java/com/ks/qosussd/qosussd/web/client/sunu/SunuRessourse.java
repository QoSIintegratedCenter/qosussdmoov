package com.ks.qosussd.qosussd.web.client.sunu;

import com.ks.qosussd.qosussd.core.MoovUssdResponse;
import com.ks.qosussd.qosussd.core.SharedComponent;
import com.ks.qosussd.qosussd.core.SubscriberInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.ks.qosussd.qosussd.web.client.padme.UssdRessource.getDefaultSub;
import static com.ks.qosussd.qosussd.web.client.sunu.SunuProcess.activeSunuSessions;

@RestController
@Slf4j
public class SunuRessourse {

    SunuProcess sunuProcess = new SunuProcess();
    SharedComponent share = new SharedComponent();

    @GetMapping("sunuussd")
    public MoovUssdResponse startMoovUssd(@RequestParam String sc, @RequestParam(required = true) String msisdn, @RequestParam(required = false) String user_input, @RequestParam(required = false) String lang, @RequestParam String session_id, @RequestParam(required = false) int req_no, @RequestParam(required = false) String screen_id) {
        MoovUssdResponse moovUssdResponse = null;
//        log.info("userInput {}", user_input);
        SubscriberInfo sub = null;
        if ((user_input.isEmpty() && activeSunuSessions.get(msisdn) == null) || (user_input.isEmpty() && activeSunuSessions.get(msisdn) != null && !activeSunuSessions.get(msisdn).getSessionId().equals(session_id))) {
            sub = new SubscriberInfo();
            sub.setMsisdn(msisdn);
            getDefaultSub(sc, user_input, lang, session_id, req_no, screen_id, sub);
            sub.setMenuLevel(0);
            activeSunuSessions.put(msisdn, sub);
            log.info("start  SUNU USSD");
            moovUssdResponse = sunuProcess.welcomLevel(sub);
//            log.info("MoovUssdResponse : {}", moovUssdResponse);
            return moovUssdResponse;
        } else return share.endOperation("Merci d'avoir préferer notre service");
    }
}
