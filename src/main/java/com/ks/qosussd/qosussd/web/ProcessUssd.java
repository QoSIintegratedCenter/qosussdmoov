package com.ks.qosussd.qosussd.web;

import com.ks.qosussd.qosussd.core.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import static com.ks.qosussd.qosussd.core.Constants.*;

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


    MoovUssdResponse moovLevel1Depot(SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText("Selectionner un numero puis appuyer sur envoyer \n Type de compte");
        moovUssdResponse.setScreenType("menu");
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE);
        Option option = new Option();
        option.setChoice(1);
        option.setValue("Epargne a vue");
        Option option3 = new Option();
        option3.setChoice(2);
        option3.setValue("Plan tontine");
        Option option4 = new Option();
        option4.setChoice(3);
        option4.setValue("Compte courant");
        OptionsType optionsType = new OptionsType();
        optionsType.getOption().add(option);
        optionsType.getOption().add(option3);
        optionsType.getOption().add(option4);
        moovUssdResponse.setOptions(optionsType);


        return moovUssdResponse;
    }

    boolean checkNumberExist(String phoneNumber) {
        boolean existe = true;
        RestTemplate restTemplate = new RestTemplate();


        return existe;
    }

    public MoovUssdResponse moovLevel1DepotCompte(SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText("Veuillez saisir le montant :");
        moovUssdResponse.setScreenType("form");
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE);
        return moovUssdResponse;

    }

    public MoovUssdResponse moovLevel1ResumEpargne(SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Transfert de ")
                .append(sub.getAmount())
                .append(" fcfa de votre compte momo sur votre compte ")
                .append(sub.getSubParams().get("option2"))
                .append(".\n")
                .append("Frais : 200 fcfa ")
                .append("Total: ").append(sub.getAmount().add(new BigDecimal(200)))
                .append("\n");
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder.append("Transfert de ")
                .append(sub.getAmount())
                .append(" fcfa de votre compte ")
                .append(sub.getSubParams().get("option2"))
                .append("sur votre compte Momo.\n")
                .append("Frais : 200 fcfa ")
                .append("Total: ").append(sub.getAmount().add(new BigDecimal(200)))
                .append("\n");
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        if (sub.getSubParams().get("option2") == DEPOT) {
            moovUssdResponse.setText(stringBuilder.toString());
        } else if (sub.getSubParams().get("option2") == RETRAIT) {
            moovUssdResponse.setText(stringBuilder2.toString());
        }

        moovUssdResponse.setScreenType("menu");
        Option option = new Option();
        option.setChoice(1);
        option.setValue("Confirmer");
        Option option1 = new Option();
        option1.setChoice(2);
        option1.setValue("Annuler");
        OptionsType optionsType = new OptionsType();
        optionsType.getOption().add(option);
        optionsType.getOption().add(option1);
        moovUssdResponse.setOptions(optionsType);
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE);
        return moovUssdResponse;
    }


    public MoovUssdResponse moovLevel3Retrait(SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Transfert de ")
                .append(sub.getAmount())
                .append(" fcfa de votre compte ")
                .append(sub.getSubParams().get("option2"))
                .append("sur votre compte Momo.\n")
                .append("Frais : 200 fcfa ")
                .append("Total: ").append(sub.getAmount().add(new BigDecimal(200)))
                .append("\n Entrer votre pin PADME pour continuer");
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText(stringBuilder.toString());
        moovUssdResponse.setScreenType("form");
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE);
        return moovUssdResponse;
    }

    public MoovUssdResponse defaultException() {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText("Mauvais choix, merci de reessayer");
        moovUssdResponse.setScreenType("form");
        moovUssdResponse.setSessionOp(TypeOperation.END);
        return moovUssdResponse;
    }

    public MoovUssdResponse endDeposit() {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText("Merci de valider momo pour confirmer votre operation");
        moovUssdResponse.setScreenType("form");
        moovUssdResponse.setSessionOp(TypeOperation.END);
        return moovUssdResponse;
    }

    public MoovUssdResponse endOperation(String text) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText(text);
        moovUssdResponse.setScreenType("form");
        moovUssdResponse.setSessionOp(TypeOperation.END);
        return moovUssdResponse;
    }

    public MoovUssdResponse moovLevel1Retrait(SubscriberInfo sub) {

        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText("Selectionner un numero puis appuyer sur envoyer \n Type de compte");
        moovUssdResponse.setScreenType("menu");
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE);
        Option option = new Option();
        option.setChoice(1);
        option.setValue("Epargne a vue");
        Option option4 = new Option();
        option4.setChoice(3);
        option4.setValue("Compte courant");
        OptionsType optionsType = new OptionsType();
        optionsType.getOption().add(option);
        optionsType.getOption().add(option4);
        moovUssdResponse.setOptions(optionsType);
        return moovUssdResponse;
    }

    public boolean checkValidUserPadme(String user_input, SubscriberInfo sub) {
        boolean isvalid = false;
        return isvalid;
    }
}
