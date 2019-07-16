package com.ks.qosussd.qosussd.web;

import com.ks.qosussd.qosussd.core.MoovUssdResponse;
import com.ks.qosussd.qosussd.core.Option;
import com.ks.qosussd.qosussd.core.OptionsType;
import com.ks.qosussd.qosussd.core.SubscriberInfo;

import java.util.concurrent.ConcurrentHashMap;

public class ProcessUssd {

    public static final ConcurrentHashMap<String, SubscriberInfo> activeSessions = new ConcurrentHashMap<>();

   MoovUssdResponse welcomLevel(SubscriberInfo sub){
       MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
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
       return moovUssdResponse;
   }


   MoovUssdResponse moovLevel1(SubscriberInfo sub){
       MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
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

       return moovUssdResponse;
   }
}
