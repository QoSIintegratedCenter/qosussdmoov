package com.ks.qosussd.qosussd.web;

import com.ks.qosussd.qosussd.core.*;
import com.ks.qosussd.qosussd.padme.ApiConnect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.ks.qosussd.qosussd.core.Utilities.*;

@Slf4j
public class ProcessUssd {

    public static final ConcurrentHashMap<String, SubscriberInfo> activeSessions = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, SubscriberInfo> oldSessions = new ConcurrentHashMap<>();

    MoovUssdResponse welcomLevel(SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setText("Sélectionner un numéro puis appuyer sur envoyer");
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setScreenType("menu");
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE.getType());

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
            moovUssdResponse.setSessionOp(TypeOperation.CONTINUE.getType());
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
            optionsType.getOption().add(option4);
            optionsType.getOption().add(option2);
            optionsType.getOption().add(option5);
            optionsType.getOption().add(option6);
            moovUssdResponse.setOptions(optionsType);

        } else {
            moovUssdResponse.setText("Ce numero n'a pas un compte chez padme");
            moovUssdResponse.setScreenType("form");
            activeSessions.remove(sub.getMsisdn());
            moovUssdResponse.setSessionOp(TypeOperation.END.getType());
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
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE.getType());
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


    public MoovUssdResponse moovLevel1DepotCompte(SubscriberInfo sub) {
        String text = "Veuillez saisir le montant :";
        return getMoovUssdResponse(text, "form", TypeOperation.CONTINUE.getType(), Integer.parseInt(sub.getScreenId()));

    }

    private MoovUssdResponse getMoovUssdResponse(String text, String type, String typeOperation, int screenId) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(screenId);
        moovUssdResponse.setText(text);
        moovUssdResponse.setScreenType(type);
        moovUssdResponse.setSessionOp(typeOperation);
        return moovUssdResponse;
    }

    public MoovUssdResponse enterAmount(SubscriberInfo sub) {
        String text = "Veuillez saisir le montant :";
        return getMoovUssdResponse(text, "form", TypeOperation.CONTINUE.getType(), Integer.parseInt(sub.getScreenId()));

    }

    public MoovUssdResponse moovLevel1ResumEpargne(SubscriberInfo sub) {
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
                .append("\n");
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
       /* if (sub.getSubParams().get("option2") == DEPOT) {

        } else if (sub.getSubParams().get("option2") == RETRAIT) {
            moovUssdResponse.setText(stringBuilder2.toString());
        }* /
        moovUssdResponse.setText(stringBuilder.toString());
        moovUssdResponse.setScreenType("menu");
        */
        MoovUssdResponse moovUssdResponse = getMoovUssdResponse(stringBuilder.toString(), "menu", TypeOperation.CONTINUE.getType(), Integer.parseInt(sub.getScreenId()));
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
//        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE);
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
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE.getType());
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
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE.getType());
        return moovUssdResponse;
    }

    public MoovUssdResponse padmeConfirmOption(String text, SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText(text);
        moovUssdResponse.setScreenType("form");
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE.getType());
        return moovUssdResponse;
    }

    public MoovUssdResponse defaultException() {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText("Mauvais choix, merci de reessayer");
        moovUssdResponse.setScreenType("form");
        moovUssdResponse.setSessionOp(TypeOperation.END.getType());
        return moovUssdResponse;
    }

    public MoovUssdResponse endDeposit() {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText("Merci de valider momo pour confirmer votre operation");
        moovUssdResponse.setScreenType("form");
        moovUssdResponse.setSessionOp(TypeOperation.END.getType());
        return moovUssdResponse;
    }

    public MoovUssdResponse endOperation(String text) {
        MoovUssdResponse moovUssdResponse = getMoovUssdResponse(text, "form", TypeOperation.END.getType(), 1);

        return moovUssdResponse;
    }

    public MoovUssdResponse moovLevel1Retrait(SubscriberInfo sub) {

        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText("Selectionner un numero puis appuyer sur envoyer \n Type de compte");
        moovUssdResponse.setScreenType("menu");
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE.getType());
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

        Map res = restTemplate.getForObject(getProp("pamde.check_client") + sub.getMsisdn() + "/"+ user_input, Map.class);
        log.info("Response : {}", res);
        if (res != null && res.get("telephono") != null) {
            isvalid = true;
        }

        return isvalid;
    }

    public MoovUssdResponse moovLevel1Credit(SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText("Selectionner un numero puis appuyer sur envoyer \n Type de compte");
        moovUssdResponse.setScreenType("menu");
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE.getType());
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
        Map infoCredit = getInfoCredit(sub);
        Option option = new Option();
        MoovUssdResponse moovUssdResponse = getMoovUssdResponse("Selectionner un numero puis appuyer sur envoyer \n Type de remboursement", "menu", TypeOperation.CONTINUE.getType(), Integer.parseInt(sub.getScreenId()));
        option.setChoice(1);
        sub.getSubParams().put(Constants.REGURALISER, infoCredit.get("restePourSolde"));
        sub.getSubParams().put(Constants.ECHEANCE, infoCredit.get("montantEcheance"));
        option.setValue("Montant a payer pour regulariser :" + infoCredit.get("restePourSolde") + " fcfa");
        Option option3 = new Option();
        option3.setChoice(2);
        option3.setValue("Prochaine echeance  " + infoCredit.get("montantEcheance") + " fcfa ");
        Option option4 = new Option();
        option4.setChoice(3);
        option4.setValue("Autre montant a payer");
        OptionsType optionsTypeC = new OptionsType();
        optionsTypeC.getOption().add(option);
        optionsTypeC.getOption().add(option3);
        optionsTypeC.getOption().add(option4);
        moovUssdResponse.setOptions(optionsTypeC);
        return moovUssdResponse;
    }


    public MoovUssdResponse debitAccount(SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setBackLink(1);
        moovUssdResponse.setHomeLink(0);
        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText("Selectionner un numero puis appuyer sur envoyer \n Type de compte");
        moovUssdResponse.setScreenType("menu");
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE.getType());
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
            oldSessions.put(sub.getMsisdn(), sub);
            Map data = new HashMap();
               /*const data = {
                    'msisdn': this.config.getDefaultPhoneNumber(),
                    'amount': +amont + this.config.getFraisTrans,
                    'firstname': 'padme',
                    'lastname': 'Qos',
                    'clientid': this.config.getclientId,
                    'transref': this.config.makeid()
                        };*/
            data.put("msisdn", sub.getMsisdn());
            data.put("firstname", "padme");
            data.put("lastname", "Qos");
            data.put("clientid", getProp("momo_moov_clientId"));
            data.put("transref", randomAlphaNumeric());
            data.put("amount", sub.getAmount().add(new BigDecimal(200)));
            RestTemplate restTemplate = new RestTemplate();


           /* restTemplate.getInterceptors().add(
                    new BasicAuthorizationInterceptor(getProp("momo_moov_username"), getProp("momo_moov_password")));*/
//            HttpEntity<String> request = new HttpEntity<String>(createHeaders(getProp("momo_moov_username"), getProp("momo_moov_password")));
            try {
//                Map res = restTemplate.postForObject(getProp("momo_moov_requestpayement"), data, Map.class);
//                Map res = restTemplate.postForObject(getProp("momo_moov_requestpayement"), data, Map.class);
                Map res = restTemplate.exchange(getProp("momo_moov_requestpayement"), HttpMethod.POST, new HttpEntity<Map>(data, createHeaders(getProp("momo_moov_username"), getProp("momo_moov_password"))), Map.class).getBody();
                log.info("response payement {} ", res);
                if (res.get("responsecode").equals("01")) {
                    new ApiConnect().startChecking(data);
                }

            } catch (Exception e) {
                log.error("Error to sent request payement {} ", e.getMessage());
            }

            return endOperation("Merci de continuer l'operation en validant votre momo");
        } else {
            // add check padme verifie id
            activeSessions.remove(sub.getMsisdn());
            return endOperation("Operation annuler avec succes");
        }
    }

    public String infoCredit(SubscriberInfo sub) {
        Map infoCredit = getInfoCredit(sub);
        StringBuilder builder = new StringBuilder();
        builder.append("Etat du crédit :\n" +
                "\n" +
                "Montant du crédit : " + infoCredit.get("montoDesembolso") + "\n" +
                "Montant échéance : " + infoCredit.get("montantEcheance") + "\n" +
                "Montant impayé : " + infoCredit.get("montantImpaye") + "\n" +
                "Reste à solder : " + infoCredit.get("restePourSolde") + "\n" +
                "Date de la dernière échance : " + infoCredit.get("dateDerniereEcheance"));
        return builder.toString();
    }

    private Map getInfoCredit(SubscriberInfo sub) {

        RestTemplate restTemplate = new RestTemplate();
        Map res = new HashMap();

        try {
//            System.out.println(getProp("pamde.check_client") + phoneNumber);
            res = restTemplate.getForObject(getProp("infocredit") + sub.getMsisdn(), Map.class);
            log.info("Get credit infos : {}", res);
            return res;
        } catch (Exception e) {
            log.error("Error to get infos credit : {}", e);
            return res;
        }
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

    public void retraitProcess(SubscriberInfo sub) {

        // recuperer le com

    }

    public boolean checkAccounAvailable(SubscriberInfo sub) {
        boolean isavailable = true;

        return isavailable;
    }
}
