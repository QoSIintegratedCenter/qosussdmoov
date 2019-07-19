package com.ks.qosussd.qosussd.web;

import com.ks.qosussd.qosussd.core.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.ks.qosussd.qosussd.core.Constants.*;
import static com.ks.qosussd.qosussd.web.ProcessUssd.activeSessions;

@RestController
@Slf4j
public class UssdRessource {

    ProcessUssd processUssd = new ProcessUssd();
    int select;

    @GetMapping(name = "/test2", produces = MediaType.APPLICATION_XML_VALUE)
    private MoovUssdResponse responseTest() {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setText("text ussd");
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setScreenType("form");

        Option option = new Option();
        option.setChoice(1);
        option.setValue("Momo ");
        Option option1 = new Option();
        option1.setChoice(2);
        option1.setValue("Moov ");
        List<Option> optionList = new ArrayList<>();
        optionList.add(option);
        optionList.add(option1);
        OptionsType optionsType = new OptionsType();
        optionsType.getOption().add(option);
        optionsType.getOption().add(option1);
        moovUssdResponse.setOptions(optionsType);
        return moovUssdResponse;
    }

    @GetMapping("padmeussd")
    public MoovUssdResponse startMoovUssd(@RequestParam String sc, @RequestParam(required = true) String msisdn, @RequestParam(required = false) String user_input, @RequestParam(required = false) String lang, @RequestParam String session_id, @RequestParam(required = false) int req_no, @RequestParam(required = false) String screen_id) {
        MoovUssdResponse moovUssdResponse = null;
        SubscriberInfo sub = null;
        if (user_input.isEmpty()) {
            sub = new SubscriberInfo();
            sub.setMsisdn(msisdn);
            sub.setSc(sc);
            sub.setLang(lang);
            sub.setReq_no(req_no);
            sub.setUserInput(user_input);
            sub.setSessionId(session_id);
            sub.setScreenId(screen_id);
            sub.setMenuLevel(0);
            activeSessions.put(msisdn, sub);
            log.info("start USSD");
            moovUssdResponse = processUssd.welcomLevel(sub);
            log.info("MoovUssdResponse : {}", moovUssdResponse);
            return moovUssdResponse;
        } else {
            sub = activeSessions.get(msisdn);
            log.info("sub");
            switch (sub.getMenuLevel()) {
                case 0:
                    log.info("Choix 1 USSD");
                    log.info("Start sub {} ", activeSessions.get(msisdn));
                    sub.incrementMenuLevel();
                    moovUssdResponse = processUssd.moovLevel1(activeSessions.get(msisdn));
                    log.info("MoovUssdResponse : {}", moovUssdResponse);
//                    log.info("sub : {}");
                    System.out.println(sub);
                    return moovUssdResponse;
                case 1:
                    log.info("choix niveux 1");
                    select = Integer.parseInt(user_input);
                    if (select == 1) {
                        log.info("choix depot ");
                        sub.getSubParams().put("option1", DEPOT);
                        sub.incrementMenuLevel();
                        return processUssd.moovLevel1Depot(sub);
                    } else if (select == 2) {
                        log.info("choix depot ");
                        sub.getSubParams().put("option1", RETRAIT);
                        sub.incrementMenuLevel();
                        return processUssd.moovLevel1Retrait(sub);
                    } else {
                        return processUssd.moovLevel1Depot(sub);
                    }
                case 2:
                    log.info("choix niveux 2");
                    select = Integer.parseInt(user_input);
                    sub.incrementMenuLevel();
                   /* switch (select){
                        case 1:
                            switch (sub.getSubParams().get("option1").toString()){
                                case DEPOT :
                            }
                    }*/
                    if (select == 1 && sub.getSubParams().get("option1") == DEPOT) {
                        log.info("choix depot plus {} ", sub.getSubParams().get("option1"));
                        sub.getSubParams().put("option2", EPARGNE);
                        return processUssd.moovLevel1DepotCompte(sub);
                    } else if (select == 2 && sub.getSubParams().get("option1") == DEPOT) {
                        sub.getSubParams().put("option2", PLAN_TONTINE);
                        return processUssd.moovLevel1DepotCompte(sub);

                    } else if (select == 3 && sub.getSubParams().get("option1") == DEPOT) {
                        sub.getSubParams().put("option2", COURANT);
                        return processUssd.moovLevel1DepotCompte(sub);
                    }
                    if (select == 1 && sub.getSubParams().get("option1") == RETRAIT) {
                        log.info("choix retrait plus {} ", sub.getSubParams().get("option1"));
                        sub.getSubParams().put("option2", EPARGNE);
                        return processUssd.moovLevel1DepotCompte(sub);
                    }
                    if (select == 2 && sub.getSubParams().get("option1") == RETRAIT) {
                        log.info("choix retrait plus {} ", sub.getSubParams().get("option1"));
                        sub.getSubParams().put("option2", COURANT);
                        return processUssd.moovLevel1DepotCompte(sub);
                    } else {
                        activeSessions.remove(sub.getMsisdn());
                        return processUssd.defaultException();
                    }
                case 3:
                    log.info("Amount: {} ", user_input);
                    sub.setAmount(new BigDecimal(user_input));
                    sub.setUserInput(user_input);
                    sub.incrementMenuLevel();
                    if (sub.getSubParams().get("option1") == RETRAIT) {
                        return processUssd.moovLevel3Retrait(sub);
                    } else
                        return processUssd.moovLevel1ResumEpargne(sub);

                case 4:
                    log.info("processe");
                    if (sub.getSubParams().get("option1") == DEPOT){
                        select = Integer.parseInt(user_input);
                    }else if(sub.getSubParams().get("option1") == RETRAIT){
                        processUssd.checkValidUserPadme(user_input, sub);
                    }
                    activeSessions.remove(sub.getMsisdn());
                    return processUssd.endDeposit();
                default:

                    log.info("Choix autre USSD");
                    moovUssdResponse = new MoovUssdResponse();
                    moovUssdResponse.setText("PADME \n vous avez faire un mauvais chois ");
                    moovUssdResponse.setBackLink(1);
                    moovUssdResponse.setHomeLink(0);
                    moovUssdResponse.setScreenId(1);
                    moovUssdResponse.setScreenType("form");
                    moovUssdResponse.setSessionOp(TypeOperation.END);
                    log.info("MoovUssdResponse : {}", moovUssdResponse);
                    return moovUssdResponse;
            }
           /* if (Integer.parseInt(user_input) == 1 && sub.getMenuLevel() == 0) {
                log.info("Choix 1 USSD");
                log.info("Start sub {} ", activeSessions.get(msisdn));
                sub.incrementMenuLevel();
                moovUssdResponse = processUssd.moovLevel1(activeSessions.get(msisdn));
                log.info("MoovUssdResponse : {}", moovUssdResponse);
                log.info("sub : {}");
                System.out.println(sub);
                return moovUssdResponse;

            } else if (Integer.parseInt(user_input) == 1 && sub.getMenuLevel() == 1) {
                sub = activeSessions.get(msisdn);

                if (sub.getMenuLevel() == 1) {
                    log.info("choix niveux 1");
                    if (Integer.parseInt(user_input) == 1) {
                        log.info("choix depot ");
                        sub.getSubParams().put("option1", "Depot");
                        sub.incrementMenuLevel();
                        return processUssd.moovLevel1Depot(sub);
                    } else {
                        return processUssd.moovLevel1Depot(sub);
                    }
                } else {
                    return moovUssdResponse;
                }
            } else {
                log.info("Choix autre USSD");
                moovUssdResponse.setText("PADME \n vous avez choisir :" + user_input);
                moovUssdResponse.setBackLink(1);
                moovUssdResponse.setHomeLink(0);
                moovUssdResponse.setScreenId(1);
                moovUssdResponse.setScreenType("form");
                moovUssdResponse.setSessionOp(TypeOperation.END);
                log.info("MoovUssdResponse : {}", moovUssdResponse);
                return moovUssdResponse;
            }*/
        }


    }


//    MoovUssdResponse buildResponseDefault(String type, String text, )
}
