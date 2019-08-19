package com.ks.qosussd.qosussd.web;

import com.ks.qosussd.qosussd.core.*;
import com.ks.qosussd.qosussd.padme.ApiConnect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.ks.qosussd.qosussd.core.Constants.*;
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

    public MoovUssdResponse getMoovUssdResponseEnd(String text) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
//        moovUssdResponse.setScreenId(screenId);
        moovUssdResponse.setText(text);
        moovUssdResponse.setScreenType("form");
        moovUssdResponse.setSessionOp(TypeOperation.END.getType());
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
        return getUssdResponse(stringBuilder.toString(), sub);
    }

    public MoovUssdResponse momoConfirmOption(String text, SubscriberInfo sub) {
        return getUssdResponse(text, sub);
    }

    private MoovUssdResponse getUssdResponse(String text, SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = getMoovUssdResponse(text, "menu", TypeOperation.CONTINUE.getType(), Integer.parseInt(sub.getScreenId()));
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
        return moovUssdResponse;
    }


/*
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
*/

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
//        moovUssdResponse.setScreenId(1);
        moovUssdResponse.setText("Mauvais choix, merci de reessayer");
        moovUssdResponse.setScreenType("form");
        moovUssdResponse.setSessionOp(TypeOperation.END.getType());
        return moovUssdResponse;
    }


    public MoovUssdResponse endOperation(String text) {
        return getMoovUssdResponseEnd(text);
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

        Map res = restTemplate.getForObject(getProp("pamde.check_client") + sub.getMsisdn() + "/" + user_input, Map.class);
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
        MoovUssdResponse moovUssdResponse = getMoovUssdResponse("Selectionner un numero puis appuyer sur envoyer \n Type de compte", "menu", TypeOperation.CONTINUE.getType(), Integer.parseInt(sub.getScreenId()));

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
            data.put("msisdn", sub.getMsisdn());
            data.put("firstname", "padme");
            data.put("lastname", "Qos");
            data.put("clientid", getProp("momo_moov_clientId"));
            data.put("transref", randomAlphaNumeric());
            data.put("amount", sub.getAmount());
            RestTemplate restTemplate = new RestTemplate();
            if (sub.getSubParams().get("option1").equals(DEPOT) || sub.getSubParams().get("option2").equals(DEPOT_TIERS)) {
                log.info("Option depot  ou depot tiers");
                sendMomoRequest(data);
                return endOperation("Merci de poursuivre l'operation avec momo");
            }

            if (sub.getSubParams().get("option4").equals("momo")) {
                log.info("Option momo");

                sendMomoRequest(data);
                return endOperation("Merci de poursuivre l'operation avec momo");
            } else if (sub.getSubParams().get("option4").equals("padme")) {
                log.info("Option epargne");
                // verifié le compte
                Map accoount = new ApiConnect().getAccountInfo("epargne_account" + sub.getMsisdn());

                return endOperation("Operation effectuee avec succee");
            }
            return endOperation("Operation non effectuee.");

        } else {
            // add check padme verifie id
            activeSessions.remove(sub.getMsisdn());
            return endOperation("Operation annuler avec succes");
        }
    }

    public MoovUssdResponse transfertProcess(String user_input, SubscriberInfo sub) {
        int select = Integer.parseInt(user_input);
        log.info("Transfert process");
        if (select == 1) {
            SubscriberInfo customer = sub;
            activeSessions.remove(sub.getMsisdn());
            oldSessions.put(sub.getMsisdn(), sub);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-type", "Application/json");
            Map fromAccount = new HashMap();
            Map toAccount = new HashMap();
            Map transData = new HashMap();
            String ref = randomAlphaNumeric();
            Map transDatato = new HashMap();
            if (sub.getSubParams().get("option3").equals(EPARGNE)) {
                log.info("from epargne");
                fromAccount = new ApiConnect().getAccountInfo(getProp("epargne_account") + customer.getMsisdn());
                toAccount = new ApiConnect().getAccountInfo(getProp("operation_account") + customer.getMsisdn());


            } else {
                log.info("from courant");
                fromAccount = new ApiConnect().getAccountInfo(getProp("operation_account") + customer.getMsisdn());
                toAccount = new ApiConnect().getAccountInfo(getProp("epargne_account") + customer.getMsisdn());

            }
            if (fromAccount != null) {
                if (new BigDecimal(fromAccount.get("saldoCuenta").toString()).compareTo(sub.getAmount().add(new BigDecimal(200))) > 0) {
                    LocalDateTime now = LocalDateTime.now();
                    transData.put("origine", customer.getMsisdn());
                    transData.put("codCuenta", fromAccount.get("codCuenta"));
                    transData.put("refTransQos", ref);
                    transData.put("codSistema", "AH");
                    transData.put("estPrisEnCompte", 0);
                    transData.put("fecha", now.toString());
                    transData.put("tipoTrans", 1);
                    transData.put("monto", customer.getAmount());
                    transData.put("frais", 200);
                    transData.put("observation", "Retrait sur le compte " + sub.getSubParams().get("option3"));
                    transData.put("typeOperation", "Retrait");
                    transData.put("telefono", customer.getMsisdn());
                    transData.put("terminal", "MOOV USSD");
                    transData.put("montoNeto", customer.getAmount().add(new BigDecimal(200)));
                    transData.put("origine", customer.getMsisdn());

                    transDatato.put("codCuenta", toAccount.get("codCuenta"));
                    transDatato.put("codSistema", "AH");
                    transDatato.put("refTransQos", ref);
                    transDatato.put("fecha", now.toString());
                    transDatato.put("estPrisEnCompte", 0);
                    transDatato.put("tipoTrans", 2);
                    transDatato.put("monto", customer.getAmount());
                    transDatato.put("frais", 0);
                    transDatato.put("observation", "Depot sur le compte " + sub.getSubParams().get("option2"));
                    transDatato.put("typeOperation", "Depot");
                    transDatato.put("telefono", customer.getMsisdn());
                    transDatato.put("terminal", "MOOV USSD");
                    transDatato.put("montoNeto", customer.getAmount());
                    RestTemplate restTemplate = new RestTemplate();
//                    RestTemplate restTemplate1 = new RestTemplate();
                    log.info("from data: {} to data: {}", transData, transDatato);
                    try {
                        Map res = restTemplate.exchange(getProp("transaction"), HttpMethod.POST, new HttpEntity<Map>(transData, httpHeaders), Map.class).getBody();
                        Map res2 = restTemplate.exchange(getProp("transaction"), HttpMethod.POST, new HttpEntity<Map>(transDatato, httpHeaders), Map.class).getBody();
//                        log.info("Result from data: {} to data: {}", res, res2);
                        log.info("Transfert effectué avec succes");
                        return endOperation("Operatio  effectuee avec succes.");

                    } catch (Exception e) {
                        log.info("Erreur lors de la transfert " + e);
                        return endOperation("Un erreur s est produite, reesayer");
                    }
                } else endOperation("Solde insufissant");

            } else {
                return endOperation("Un erreur s est produite, reesayer");
            }


        } else {
            // add check padme verifie id
            activeSessions.remove(sub.getMsisdn());
            return endOperation("Operation annuler avec succes");
        }
        return endOperation("Un erreur s est produite, reesayer");

    }

    private void sendMomoRequest(Map data) {
        RestTemplate restTemplate = new RestTemplate();
        try {
//                Map res = restTemplate.postForObject(getProp("momo_moov_requestpayement"), data, Map.class);
//                Map res = restTemplate.postForObject(getProp("momo_moov_requestpayement"), data, Map.class);
            Map res = restTemplate.exchange(getProp("momo_moov_requestpayement"), HttpMethod.POST, new HttpEntity<Map>(data, createHeaders(getProp("momo_moov_username"), getProp("momo_moov_password"))), Map.class).getBody();
            log.info("response payement {} ", res);
            // responsecode":
            if (res.get("responsecode").equals("0")) {
                new ApiConnect().postDataToPadmeDatabase(data);
                log.info("Depot  success");
            } else {
                log.info("Une erreur s est produite merci de reesayer");
//                return endOperation();
            }

        } catch (Exception e) {
            log.error("Error to sent request payement {} ", e.getMessage());
            activeSessions.remove(data.get("msisdn"));
//            return endOperation("Une erreur s est produite merci de reesayer");
        }
//        return endOperation("Merci de poursuivre l'operation avec momo");
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
        String phoneNumber = sub.getMsisdn();
        if (sub.getSubParams().get("option1").equals(OPERATION_TIERS)) {
            phoneNumber = sub.getSubParams().get("PHONE_TIERS").toString();
        }

        try {
//            System.out.println(getProp("pamde.check_client") + phoneNumber);
            res = restTemplate.getForObject(getProp("infocredit") + phoneNumber, Map.class);
            log.info("Get credit infos : {}", res);
            return res;
        } catch (Exception e) {
            log.error("Error to get infos credit : {}", e);
            return res;
        }
    }

    boolean checkNumberExist(String phoneNumber) {
        boolean existe = false;
        RestTemplate restTemplate = new RestTemplate();

        // comment for test

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

    @Async
    public void retraitProcess(SubscriberInfo sub) {

        // recuperer le com
        Map data = new HashMap();
        data.put("msisdn", sub.getMsisdn());
        data.put("firstname", "padme");
        data.put("lastname", "Qos");
        data.put("clientid", getProp("momo_moov_clientId"));
        data.put("amount", sub.getAmount().add(new BigDecimal(200)));
        new ApiConnect().postDataToPadmeDatabase(data);


    }

    public boolean checkAccounAvailable(SubscriberInfo sub) {
        boolean isavailable = true;

        return isavailable;
    }

    /**
     * transfert process
     *
     * @param sub
     * @return
     */
    public MoovUssdResponse moovLevel1Transfert(SubscriberInfo sub) {
        String text = "Selectionner un numero puis appuyer sur envoyer \n Transferer sur votre compte :";
        MoovUssdResponse moovUssdResponse = getMoovUssdResponse(text, "menu", TypeOperation.CONTINUE.getType(), Integer.parseInt(sub.getScreenId()));
        Option option1 = new Option();
        option1.setChoice(1);
        option1.setValue("Epargne a vue");
        Option option2 = new Option();
        option2.setChoice(2);
        option2.setValue("Courant");
        Option option3 = new Option(3, "Compte tiers a PADME");
        OptionsType optionsType = new OptionsType();
        optionsType.getOption().add(option1);
        optionsType.getOption().add(option2);
        optionsType.addOption(option3);
        moovUssdResponse.setOptions(optionsType);
        return moovUssdResponse;
    }

    public MoovUssdResponse fromAccoundTransfert(String evp, SubscriberInfo sub) {
        String text = "Selectionner un numero puis appuyer sur envoyer \n A partir de votre compte :";
        MoovUssdResponse moovUssdResponse = getMoovUssdResponse(text, "menu", TypeOperation.CONTINUE.getType(), Integer.parseInt(sub.getScreenId()));
        Option option1 = new Option();
        option1.setChoice(1);
        option1.setValue("Epargne a vue");
        Option option2 = new Option();
        option2.setChoice(1);
        option2.setValue("Courant");
        OptionsType optionsType = new OptionsType();
        if (evp.equals(EPARGNE)) {
            optionsType.getOption().add(option2);
        } else if (evp.equals(COURANT)) {
            optionsType.getOption().add(option1);
        } else {
            optionsType.getOption().add(option1);
            optionsType.addOption(new Option(2, "Courant"));
            optionsType.addOption(new Option(3, "Plan tontine"));
        }
        moovUssdResponse.setOptions(optionsType);

        return moovUssdResponse;
    }

    public MoovUssdResponse toAccountTransfertTiers(SubscriberInfo sub) {
        String text = "Veuillez saisir le numero du tiers :";
        MoovUssdResponse moovUssdResponse = getMoovUssdResponse(text, "form", TypeOperation.CONTINUE.getType(), Integer.parseInt(sub.getScreenId()));
        return moovUssdResponse;
    }

    public MoovUssdResponse startManageAccount(SubscriberInfo sub) {
        String text = "Gestion des comptes: ";
        MoovUssdResponse moovUssdResponse = getMoovUssdResponse(text, "menu", TypeOperation.CONTINUE.getType(), Integer.parseInt(sub.getScreenId()));
        OptionsType optionsType = new OptionsType();
        optionsType.addOption(new Option(1, "Solde"));
        optionsType.addOption(new Option(2, "Termes et condition"));
        moovUssdResponse.setOptions(optionsType);
        return moovUssdResponse;
    }

    public MoovUssdResponse soldForAccount(SubscriberInfo sub, String type, Map map) {
        String text = "Selectionner le compte :";
        if (type.equals("dp")) {
            text = "Selectionner le compte de " + map.get("nombreCompleto") + " sur lequel vous voulez effectue le depot";
        }

        MoovUssdResponse moovUssdResponse = getMoovUssdResponse(text, "menu", TypeOperation.CONTINUE.getType(), Integer.parseInt(sub.getScreenId()));
        OptionsType optionsType = new OptionsType();
        optionsType.addOption(new Option(1, " Compte epargne"));
        optionsType.addOption(new Option(2, "Compte plan tontine"));
        optionsType.addOption(new Option(3, "Compte courant"));
        moovUssdResponse.setOptions(optionsType);
        return moovUssdResponse;
    }

    public MoovUssdResponse termeAndCondition(SubscriberInfo sub) {
        String text = "Les 4 prochaines pages afficheront les termes et les conditions.";
        MoovUssdResponse moovUssdResponse = getMoovUssdResponse(text, "menu", TypeOperation.CONTINUE.getType(), Integer.parseInt(sub.getScreenId()));
        OptionsType optionsType = new OptionsType();
        optionsType.addOption(new Option(1, " Suivant"));
        moovUssdResponse.setOptions(optionsType);
        return moovUssdResponse;
    }

    public MoovUssdResponse startOperationTiers(SubscriberInfo sub) {
        String text = "Operations pour tiers";
        MoovUssdResponse moovUssdResponse = getMoovUssdResponse(text, "menu", TypeOperation.CONTINUE.getType(), Integer.parseInt(sub.getScreenId()));
        OptionsType optionsType = new OptionsType();
        optionsType.addOption(new Option(1, "Depot sur compte de tiers"));
        optionsType.addOption(new Option(2, "Remboursement sur compte de tiers"));
        moovUssdResponse.setOptions(optionsType);
        return moovUssdResponse;
    }

    public MoovUssdResponse tiersAccount(SubscriberInfo sub) {
        String text = "Veuillez saisir le numero du tiers";
        MoovUssdResponse moovUssdResponse = getMoovUssdResponse(text, "form", TypeOperation.CONTINUE.getType(), Integer.parseInt(sub.getScreenId()));
        return moovUssdResponse;
    }

}
