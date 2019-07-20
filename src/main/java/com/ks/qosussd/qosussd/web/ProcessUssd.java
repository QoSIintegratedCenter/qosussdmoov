package com.ks.qosussd.qosussd.web;

import com.ks.qosussd.qosussd.core.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.ks.qosussd.qosussd.core.Utilities.getProp;

@Slf4j
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
            option4.setValue("Credit");
            Option option2 = new Option();
            option2.setChoice(4);
            option2.setValue("Transfert");
            Option option5 = new Option();
            option5.setChoice(5);
            option5.setValue("Gestion des comptes");
            Option option6 = new Option();
            option6.setChoice(6);
            option6.setValue("Operation pour tiers");
            OptionsType optionsType = new OptionsType();
            optionsType.getOption().add(option);
            optionsType.getOption().add(option3);
            optionsType.getOption().add(option2);
            optionsType.getOption().add(option4);
            optionsType.getOption().add(option5);
            optionsType.getOption().add(option6);
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


        try {
            System.out.println(getProp("pamde.check_client") + phoneNumber);

            Map res = restTemplate.getForObject(getProp("pamde.check_client") + phoneNumber, Map.class);
            System.out.println(res);
            if (res == null) {
                existe = false;
            } else {
                existe = true;
                log.info("Response : {}", res);
            }
        } catch (Exception e) {
            log.error("error : {}", e);
            existe = false;
        }


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

    public MoovUssdResponse enterAmount(SubscriberInfo sub) {
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
      /*  StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder.append("Transfert de ")
                .append(sub.getAmount())
                .append(" fcfa de votre compte ")
                .append(sub.getSubParams().get("option2"))
                .append("sur votre compte Momo.\n")
                .append("Frais : 200 fcfa ")
                .append("Total: ").append(sub.getAmount().add(new BigDecimal(200)))
                .append("\n");*/
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
       /* if (sub.getSubParams().get("option2") == DEPOT) {

        } else if (sub.getSubParams().get("option2") == RETRAIT) {
            moovUssdResponse.setText(stringBuilder2.toString());
        }*/
        moovUssdResponse.setText(stringBuilder.toString());
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

    public MoovUssdResponse momoConfirmOption(String text, SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText(text);
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
                .append(" sur votre compte Momo.\n")
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

    public MoovUssdResponse padmeConfirmOption(String text, SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText(text);
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
        Option option2 = new Option();
        option2.setChoice(2);
        option2.setValue("Compte courant");
        OptionsType optionsType = new OptionsType();
        optionsType.getOption().add(option);
        optionsType.getOption().add(option2);
        moovUssdResponse.setOptions(optionsType);
        return moovUssdResponse;
    }

    public boolean checkValidUserPadme(String user_input, SubscriberInfo sub) {
        boolean isvalid = false;
        RestTemplate restTemplate = new RestTemplate();

        Map res = (Map) restTemplate.getForEntity(getProp("pamde.check_client") + sub.getMsisdn(), Map.class);
        log.info("Response : {}", res);

        return isvalid;
    }

    public MoovUssdResponse moovLevel1Credit(SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText("Selectionner un numero puis appuyer sur envoyer \n Type de compte");
        moovUssdResponse.setScreenType("menu");
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE);
        Option option = new Option();
        option.setChoice(1);
        option.setValue("Remboursement");
        Option option3 = new Option();
        option3.setChoice(2);
        option3.setValue("Demande de crédit");
        Option option4 = new Option();
        option4.setChoice(3);
        option4.setValue("Etat du crédit");
        OptionsType optionsType = new OptionsType();
        optionsType.getOption().add(option);
        optionsType.getOption().add(option3);
        optionsType.getOption().add(option4);
        moovUssdResponse.setOptions(optionsType);


        return moovUssdResponse;
    }

    public MoovUssdResponse moovLevel2Credit(SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText("Selectionner un numero puis appuyer sur envoyer \n Type de compte");
        moovUssdResponse.setScreenType("menu");
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE);
        Option option = new Option();
        option.setChoice(1);
        option.setValue("Montant a payer pour regulariser : 100 fcfa");
        Option option3 = new Option();
        option3.setChoice(2);
        option3.setValue("Prochaine echeance  : 50 fcfa (Exigible le jj-mm-aaaa)");
        Option option4 = new Option();
        option4.setChoice(3);
        option4.setValue("Autre montant à payer");
        OptionsType optionsType = new OptionsType();
        optionsType.getOption().add(option);
        optionsType.getOption().add(option3);
        optionsType.getOption().add(option4);
        moovUssdResponse.setOptions(optionsType);
        return moovUssdResponse;
    }

    public MoovUssdResponse debitAccount(SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText("Selectionner un numero puis appuyer sur envoyer \n Type de compte");
        moovUssdResponse.setScreenType("menu");
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE);
        Option option = new Option();
        option.setChoice(1);
        option.setValue("Momo");
        Option option2 = new Option();
        option2.setChoice(2);
        option2.setValue("Epargne a vue");
        OptionsType optionsType = new OptionsType();
        optionsType.getOption().add(option);
        optionsType.getOption().add(option2);
        moovUssdResponse.setOptions(optionsType);
        return moovUssdResponse;
    }


    public MoovUssdResponse getMoovUssdResponseConfirm(String user_input, SubscriberInfo sub) {
        int select = Integer.parseInt(user_input);
        if (select == 1) {
            activeSessions.remove(sub.getMsisdn());
            return endOperation("Merci de continuer l'operation en validant votre momo");
        } else {
            // add check padme verifie id
            activeSessions.remove(sub.getMsisdn());
            return endOperation("Operation annuler avec succes");
        }
    }

    public String infoCredit(SubscriberInfo sub) {
        return "info credit";
    }
}
