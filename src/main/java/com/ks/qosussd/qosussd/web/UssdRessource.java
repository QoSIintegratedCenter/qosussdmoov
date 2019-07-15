package com.ks.qosussd.qosussd.web;

import com.ks.qosussd.qosussd.core.MoovUssdResponse;
import com.ks.qosussd.qosussd.core.Option;
import com.ks.qosussd.qosussd.core.OptionsType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class UssdRessource {

    ProcessUssd processUssd = new ProcessUssd();

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
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        if (user_input.isEmpty()) {
            log.info("start USSD");
            moovUssdResponse.setText("Sélectionner un numéro puis appuyer sur envoyer");
            moovUssdResponse.setBackLink(1);
            moovUssdResponse.setHomeLink(0);
            moovUssdResponse.setScreenId(1);
            moovUssdResponse.setScreenType("menu");
            moovUssdResponse.setSessionOp("continue");

            Option option = new Option();
            option.setChoice(1);
            option.setValue("PADME ");
            OptionsType optionsType = new OptionsType();
            optionsType.getOption().add(option);
//            optionsType.getOption().add(option1);
            moovUssdResponse.setOptions(optionsType);
            log.info("MoovUssdResponse : {}", moovUssdResponse);
            return moovUssdResponse;
        } else if (Integer.parseInt(user_input) == 1) {
            log.info("Choix 1 USSD");
            moovUssdResponse.setText("PADME \n Sélectionner un numéro puis appuyer sur envoyer");
            moovUssdResponse.setBackLink(1);
            moovUssdResponse.setHomeLink(0);
            moovUssdResponse.setScreenId(1);
            moovUssdResponse.setScreenType("menu");
            moovUssdResponse.setSessionOp("continue");
            Option option = new Option();
            option.setChoice(2);
            option.setValue("Dépôt");
            Option option3 = new Option();
            option3.setChoice(3);
            option3.setValue("Rétrait");
            Option option4 = new Option();
            option4.setChoice(4);
            option4.setValue("Transfert");
            Option option5 = new Option();
            option5.setChoice(5);
            option5.setValue("Gestion des comptes");
            OptionsType optionsType = new OptionsType();
            optionsType.getOption().add(option);
            optionsType.getOption().add(option3);
            optionsType.getOption().add(option4);
            optionsType.getOption().add(option5);
//            optionsType.getOption().add(option1);
            moovUssdResponse.setOptions(optionsType);
            log.info("MoovUssdResponse : {}", moovUssdResponse);
            return moovUssdResponse;

        } else {
            log.info("Choix autre USSD");
            moovUssdResponse.setText("PADME \n vous avez choisir :" + user_input);
            moovUssdResponse.setBackLink(1);
            moovUssdResponse.setHomeLink(0);
            moovUssdResponse.setScreenId(1);
            moovUssdResponse.setScreenType("form");
            moovUssdResponse.setSessionOp("end");
            log.info("MoovUssdResponse : {}", moovUssdResponse);
            return moovUssdResponse;
        }

    }


//    MoovUssdResponse buildResponseDefault(String type, String text, )
}
