package com.ks.qosussd.qosussd.web;

import com.ks.qosussd.qosussd.core.*;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ConcurrentHashMap;

public class ProcessUssd {

    public static final ConcurrentHashMap<String, SubscriberInfo> activeSessions = new ConcurrentHashMap<>();

    MoovUssdResponse welcomLevel(SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setText("Sélectionner un numéro puis appuyer sur envoyer");
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setScreenType("menu");
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE);

        Option option = new Option();
        option.setChoice(1);
        option.setValue("PADME ");
        OptionsType optionsType = new OptionsType();
        optionsType.getOption().add(option);
//            optionsType.getOption().add(option1);
        moovUssdResponse.setOptions(optionsType);
        return moovUssdResponse;
    }


    MoovUssdResponse moovLevel1(SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        if (checkNumberExist(sub.getMsisdn())) {
            moovUssdResponse.setText("PADME \n Selectionner un numero puis appuyer sur envoyer");
            moovUssdResponse.setScreenType("menu");
            moovUssdResponse.setSessionOp(TypeOperation.CONTINUE);
            Option option = new Option();
            option.setChoice(1);
            option.setValue("Depot");
            Option option3 = new Option();
            option3.setChoice(2);
            option3.setValue("Retrait");
            Option option4 = new Option();
            option4.setChoice(3);
            option4.setValue("Transfert");
            Option option5 = new Option();
            option5.setChoice(4);
            option5.setValue("Gestion des comptes");
            OptionsType optionsType = new OptionsType();
            optionsType.getOption().add(option);
            optionsType.getOption().add(option3);
            optionsType.getOption().add(option4);
            optionsType.getOption().add(option5);
            moovUssdResponse.setOptions(optionsType);

        } else {
            moovUssdResponse.setText("Ce numero n'a pas un compte chez padme");
            moovUssdResponse.setScreenType("form");
            moovUssdResponse.setSessionOp(TypeOperation.END);
        }


        return moovUssdResponse;
    }

    boolean checkNumberExist(String phoneNumber) {
        boolean existe = true;
        RestTemplate restTemplate = new RestTemplate();


        return existe;
    }
}
