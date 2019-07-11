package com.ks.qosussd.qosussd;

import com.ks.qosussd.qosussd.core.Option;
import com.ks.qosussd.qosussd.core.OptionsType;
import com.ks.qosussd.qosussd.core.Response;
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

    @GetMapping(name = "/test2", produces = MediaType.APPLICATION_XML_VALUE)
    private Response responseTest() {
        Response response = new Response();
        response.setText("text ussd");
        response.setBackLink(1);
        response.setHomeLink(0);
        response.setScreenId(1);
        response.setScreenType("form");

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
        response.setOptions(optionsType);
        return response;
    }

    @GetMapping("padmeussd")
    public Response startMoovUssd(@RequestParam String sc, @RequestParam(required = true) String msisdn, @RequestParam String user_input, String lang, @RequestParam String session_id, @RequestParam int req_no, @RequestParam String screen_id) {
        Response response = new Response();
        if (user_input.isEmpty()) {
            log.info("start USSD");
            response.setText("Sélectionner un numéro puis appuyer sur envoyer");
            response.setBackLink(1);
            response.setHomeLink(0);
            response.setScreenId(1);
            response.setScreenType("menu");

            Option option = new Option();
            option.setChoice(1);
            option.setValue("PADME ");
            OptionsType optionsType = new OptionsType();
            optionsType.getOption().add(option);
//            optionsType.getOption().add(option1);
            response.setOptions(optionsType);
            log.info("Response : {}", response);
            return response;
        } else if (Integer.parseInt(user_input) == 1) {
            log.info("start USSD");
            response.setText("PADME \n Sélectionner un numéro puis appuyer sur envoyer");
            response.setBackLink(1);
            response.setHomeLink(0);
            response.setScreenId(1);
            response.setScreenType("menu");
            Option option = new Option();
            option.setChoice(2);
            option.setValue("Dépôt");
            Option option3 = new Option();
            option.setChoice(3);
            option.setValue("Rétrait");
            Option option4 = new Option();
            option.setChoice(4);
            option.setValue("Transfert");
            Option option5 = new Option();
            option.setChoice(5);
            option.setValue("Gestion des comptes");
            OptionsType optionsType = new OptionsType();
            optionsType.getOption().add(option);
            optionsType.getOption().add(option3);
            optionsType.getOption().add(option4);
            optionsType.getOption().add(option5);
//            optionsType.getOption().add(option1);
            response.setOptions(optionsType);
            log.info("Response : {}", response);
            return response;

        }

        return response;
    }


//    Response buildResponseDefault(String type, String text, )
}
