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
import static com.ks.qosussd.qosussd.web.ProcessUssd.oldSessions;

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
            getDefaultSub(sc, user_input, lang, session_id, req_no, screen_id, sub);
            sub.setMenuLevel(0);
            activeSessions.put(msisdn, sub);
            log.info("start USSD");
            moovUssdResponse = processUssd.welcomLevel(sub);
            log.info("MoovUssdResponse : {}", moovUssdResponse);
            return moovUssdResponse;
        } else {
            sub = activeSessions.get(msisdn);
            getDefaultSub(sc, user_input, lang, session_id, req_no, screen_id, sub);
            log.info("sub");
            switch (sub.getMenuLevel()) {
                case 0:
                    log.info("Choix 1 USSD");
//                    new ApiConnect().startChecking(new HashMap());
                    log.info("Start sub {} ", activeSessions.get(msisdn));
                    sub.incrementMenuLevel();
                    moovUssdResponse = processUssd.moovLevel1(activeSessions.get(msisdn));
                    log.info("MoovUssdResponse : {}", moovUssdResponse);
//                    log.info("sub : {}");
                    System.out.println(sub);
                    return moovUssdResponse;
                case 1:
                    log.info("choix nivaux " + sub.getMenuLevel());
                    select = Integer.parseInt(user_input);
                    sub.incrementMenuLevel();
//                    log.info("choix niveux apres incre " + sub.getMenuLevel());
                    if (select == 1) {
                        log.info("choix depot ");
                        sub.getSubParams().put("option1", DEPOT);
                        return processUssd.moovLevel1Depot(sub);
                    } else if (select == 2) {
                        log.info("choix retrait ");
                        sub.getSubParams().put("option1", RETRAIT);
//                        sub.incrementMenuLevel();
                        return processUssd.moovLevel1Retrait(sub);
                    } else if (select == 3) {
                        log.info("choix Credit ");
                        sub.getSubParams().put("option1", CREDIT);
//                        sub.incrementMenuLevel();
                        return processUssd.moovLevel1Credit(sub);
                    } else if (select == 4) {
                        log.info("choix transfert ");
                        sub.getSubParams().put("option1", TRANSFERT);
                        return processUssd.moovLevel1Transfert(sub);
                    } else if (select == 5) {
                        log.info("choix gestion commpte ");
                        sub.getSubParams().put("option1", GESTION_ACCOUNT);
                        return processUssd.startManageAccount(sub);
                    } else {
                        return processUssd.moovLevel1Depot(sub);
                    }
                case 2:
                    log.info("choix niveux {} ", sub.getMenuLevel());
                    select = Integer.parseInt(user_input);
                    sub.incrementMenuLevel();
                    if (select == 1 && sub.getSubParams().get("option1") == DEPOT) {
                        log.info("choix depot plus epargne ");
                        sub.getSubParams().put("option2", EPARGNE);
                        return processUssd.moovLevel1DepotCompte(sub);
                    } else if (select == 2 && sub.getSubParams().get("option1").equals(DEPOT)) {
                        log.info("choix depot plus plant tontine ");
                        sub.getSubParams().put("option2", PLAN_TONTINE);
                        return processUssd.moovLevel1DepotCompte(sub);

                    } else if (select == 3 && sub.getSubParams().get("option1").equals(DEPOT)) {
                        log.info("choix depot plus courant ");
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
                    }
                    if (select == 1 && sub.getSubParams().get("option1") == CREDIT) {
                        log.info("choix credi plus {} ", REMBOURSEMENT);
                        sub.getSubParams().put("option2", REMBOURSEMENT);
                        return processUssd.moovLevel2Credit(sub);
                    }
                    if (select == 2 && sub.getSubParams().get("option1") == CREDIT) {
                        log.info("choix credi plus {} ", DEMANDE_CREDIT);
                        sub.getSubParams().put("option2", DEMANDE_CREDIT);
                        return processUssd.enterAmount(sub);
                    }
                    if (select == 3 && sub.getSubParams().get("option1") == CREDIT) {
                        log.info("choix credi plus {} ", ETAT_CREDIT);
                        sub.getSubParams().put("option2", ETAT_CREDIT);
                        String infoCredi = processUssd.infoCredit(sub);
                        activeSessions.remove(sub.getMsisdn());
                        return processUssd.endOperation(infoCredi);
                    }
                    if (select == 1 && sub.getSubParams().get("option1") == TRANSFERT) {
                        log.info("choix Transfert: epargne");
                        sub.getSubParams().put("option2", EPARGNE);

                        return processUssd.fromAccoundTransfert("evp", sub);
                    }
                    if (select == 2 && sub.getSubParams().get("option1") == TRANSFERT) {
                        log.info("choix transfert: courant  ");
                        sub.getSubParams().put("option2", COURANT);

                        return processUssd.fromAccoundTransfert("crt", sub);
                    }
                    if (select == 3 && sub.getSubParams().get("option1") == TRANSFERT) {
                        log.info("choix transfert: compte tiers ");
                        sub.getSubParams().put("option2", COMPTE_TIERS);

                        return processUssd.toAccountTransfertTiers(sub);
                    }
                    if (select == 1 && sub.getSubParams().get("option1") == GESTION_ACCOUNT) {
                        log.info("choix gestion compte: solde ");
                        sub.getSubParams().put("option2", SOLDE);
                        return processUssd.soldForAccount(sub);
                    }
                    if (select == 2 && sub.getSubParams().get("option1") == GESTION_ACCOUNT) {
                        log.info("choix gestion compte: Terme condition ");
                        sub.getSubParams().put("option2", "tc");
                        return processUssd.termeAndCondition(sub);
                    } else {
                        activeSessions.remove(sub.getMsisdn());
                        return processUssd.defaultException();
                    }
                case 3:
                    log.info("Amount: {} ", user_input);

                    sub.incrementMenuLevel();
                    if (sub.getSubParams().get("option1") == RETRAIT) {
                        sub.setAmount(new BigDecimal(user_input));
                        sub.setUserInput(user_input);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Transfert de ")
                                .append(sub.getAmount())
                                .append(" fcfa de votre compte ")
                                .append(sub.getSubParams().get("option2"))
                                .append(" sur votre compte Momo.\n")
                                .append("Frais : 200 fcfa ")
                                .append("Total: ").append(sub.getAmount().add(new BigDecimal(200)));
                        return processUssd.momoConfirmOption(stringBuilder.toString(), sub);
                    } else if (sub.getSubParams().get("option1") == DEPOT) {
                        sub.setAmount(new BigDecimal(user_input));
                        sub.setUserInput(user_input);
                        return processUssd.moovLevel1ResumEpargne(sub);
                    } else if (sub.getSubParams().get("option1") == CREDIT) {
                        if (sub.getSubParams().get("option2").equals(REMBOURSEMENT)) {
                            select = Integer.parseInt(user_input);
                            if (select == 1) {
                                log.info("Credit rembousement regulariser");
                                sub.getSubParams().put("option3", REGURALISER);
                                sub.setAmount(new BigDecimal((int) sub.getSubParams().get(REGURALISER)));
                                return processUssd.debitAccount(sub);
                            }
                            if (select == 2) {
                                sub.getSubParams().put("option3", ECHEANCE);
                                log.info("Credit rembousement echeance");
                                sub.setAmount(new BigDecimal((int) sub.getSubParams().get(ECHEANCE)));
                                return processUssd.debitAccount(sub);
                            }
                            if (select == 3) {
                                log.info("Credit rembousement autre montant");
                                sub.getSubParams().put("option3", AUTRE_MONTANT);
                                return processUssd.moovLevel1DepotCompte(sub);
                            }
                        }
                        if ((sub.getSubParams().get("option2").equals(DEMANDE_CREDIT))) {
                            String res = "Nous accusons reception de votre demande de credit. Un agent de PADME vous contactera sous peu. \n" +
                                    "Nous vous remercions d’avoir utiliser le service push-pull de PADME.";
                            activeSessions.remove(sub.getMsisdn());
                            return processUssd.endOperation(res);
                        }
//                        return processUssd.moovLevel1ResumEpargne(sub);
                    }
                    if (sub.getSubParams().get("option1") == TRANSFERT) {
                        if (select == 1 && sub.getSubParams().get("option2").equals(EPARGNE) || sub.getSubParams().get("option2").equals(COURANT)) {
                            log.info("choix Transfert: choix account");
                            if (sub.getSubParams().get("option2").equals(EPARGNE)) {
                                sub.getSubParams().put("option3", COURANT);
                            } else if (sub.getSubParams().get("option2").equals(COURANT)) {
                                sub.getSubParams().put("option3", EPARGNE);
                            } else {
                                sub.getSubParams().put("option3", EPARGNE);
                            }
                            return processUssd.enterAmount(sub);
                        } else {
                            return processUssd.fromAccoundTransfert("cmptiers", sub);
                        }


                    }
                    if ((sub.getSubParams().get("option1").equals(GESTION_ACCOUNT))) {
                        if ((sub.getSubParams().get("option2").equals(SOLDE))) {
                            String text = "le solde de votre compte plan tontine est de xxxxxx fcfa";
                            activeSessions.remove(sub.getMsisdn());
                            return processUssd.endOperation(text);
                        } else {
                            String text = "Exemplaire de termes et conditions";
                            activeSessions.remove(sub.getMsisdn());
                            return processUssd.endOperation(text);
                        }
                    }


                case 4:
                    log.info("processe");
                    sub.incrementMenuLevel();
                    if (sub.getSubParams().get("option1") == DEPOT) {
                        return processUssd.getMoovUssdResponseConfirm(user_input, sub);
                    } else if (sub.getSubParams().get("option1") == RETRAIT) {
//                        processUssd.checkValidUserPadme(user_input, sub);
                        if (Integer.parseInt(user_input) == 1) {
                            String txt = "";
                            if (processUssd.checkAccounAvailable(sub)) {
                                oldSessions.put(sub.getMsisdn(), sub);
                                txt = "Votre operation est en cours de validation Merci.";
                                processUssd.retraitProcess(sub);
                            } else {
                                txt = "Solde insuffisant";
                            }

                            activeSessions.remove(sub.getMsisdn());
                            return processUssd.endOperation(txt);
                        } else {
                            activeSessions.remove(sub.getMsisdn());
                            return processUssd.endOperation("Operation annuler avec succee.");
                        }
                    } else if (sub.getSubParams().get("option1").equals(CREDIT)) {
                        if (sub.getSubParams().get("option2").equals(REMBOURSEMENT)) {
                            log.info("Credit rembousement");
                            if (sub.getSubParams().get("option3").equals(REGURALISER) || sub.getSubParams().get("option3").equals(ECHEANCE)) {
                                select = Integer.parseInt(user_input);
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Remboursement de ")
                                        .append(sub.getAmount())
                                        .append(" fcfa, frais 200 fcfa \n Total : " + sub.getAmount().add(new BigDecimal(200)));

                                if (select == 1) {
                                    log.info("Credit rembousement option momo");
                                    sub.getSubParams().put("option4", "momo");
                                    return processUssd.momoConfirmOption(stringBuilder.toString(), sub);
                                }
                                if (select == 2) {
                                    log.info("Credit rembousement option momo epargne");
                                    sub.getSubParams().put("option4", "padme");
                                    return processUssd.momoConfirmOption(stringBuilder.toString(), sub);
                                }
                            }
                            if (sub.getSubParams().get("option3").equals(AUTRE_MONTANT)) {
                                sub.setAmount(new BigDecimal(user_input));
                                return processUssd.debitAccount(sub);
                            }

                        }

                    } else if (sub.getSubParams().get("option1") == TRANSFERT) {
                        if (select == 1) {
                            sub.getSubParams().put("option3", EPARGNE);
                        } else if (select == 2) {
                            sub.getSubParams().put("option3", COURANT);
                        } else {
                            sub.getSubParams().put("option3", PLAN_TONTINE);
                        }
                        return processUssd.enterAmount(sub);
                    } else {
                        activeSessions.remove(sub.getMsisdn());
                        return processUssd.endOperation("Mauvaise choix");
                    }
//                    activeSessions.remove(sub.getMsisdn());
//                    return processUssd.endDeposit();
                case 5:
                    log.info("case 5");
                    sub.incrementMenuLevel();
                    if (sub.getSubParams().get("option1").equals(CREDIT)) {
                        if (sub.getSubParams().get("option2").equals(REMBOURSEMENT)) {
                            if (sub.getSubParams().get("option3").equals(REGURALISER) || sub.getSubParams().get("option3").equals(ECHEANCE)) {
                                return processUssd.getMoovUssdResponseConfirm(user_input, sub);
                            }
                            if (sub.getSubParams().get("option3").equals(AUTRE_MONTANT)) {
                                select = Integer.parseInt(user_input);
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Remboursement de ")
                                        .append(sub.getAmount())
                                        .append(" fcfa, frais 200 fcfa Total : ")
                                        .append(sub.getAmount().add(new BigDecimal(200)))
                                        .append(" fcfa.");

                                if (select == 1) {
                                    log.info("Credit rembousement option momo");
                                    sub.getSubParams().put("option4", "momo");
                                    return processUssd.momoConfirmOption(stringBuilder.toString(), sub);
                                }
                                if (select == 2) {
                                    sub.getSubParams().put("option4", "padme");
                                    log.info("Credit rembousement option momo epargne");
                                    return processUssd.momoConfirmOption(stringBuilder.toString(), sub);
                                }
                            }

                        }


                    }
                case 6:
                    log.info("case 6");
                    sub.incrementMenuLevel();
                    if (sub.getSubParams().get("option1").equals(CREDIT)) {
                        if (sub.getSubParams().get("option2").equals(REMBOURSEMENT)) {
                            select = Integer.parseInt(user_input);
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Remboursement de ")
                                    .append("xxxx")
                                    .append(" fcfa, frais 200 fcfa Total : ");
                            if (sub.getSubParams().get("option3").equals(AUTRE_MONTANT)) {
                                return processUssd.getMoovUssdResponseConfirm(user_input, sub);
                            }
                        }


                    }
                case 7:
                    log.info("case 7");

                    if (sub.getSubParams().get("option1").equals(CREDIT)) {
                        if (sub.getSubParams().get("option2").equals(REMBOURSEMENT)) {
                            if (sub.getSubParams().get("option3").equals(AUTRE_MONTANT)) {

                            }
                        }
                    }
                default:

                    log.info("Choix autre USSD");
                    moovUssdResponse = new MoovUssdResponse();
                    moovUssdResponse.setText("PADME \n vous avez faire un mauvais chois ");
                    moovUssdResponse.setBackLink(1);
                    moovUssdResponse.setHomeLink(0);
                    moovUssdResponse.setScreenId(1);
                    moovUssdResponse.setScreenType("form");
                    moovUssdResponse.setSessionOp(TypeOperation.END.getType());
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

    private static void getDefaultSub(String sc, String user_input, String lang, String session_id, int req_no, String screen_id, SubscriberInfo sub) {
        sub.setSc(sc);
        sub.setLang(lang);
        sub.setReq_no(req_no);
        sub.setUserInput(user_input);
        sub.setSessionId(session_id);
        sub.setScreenId(screen_id);
    }


//    MoovUssdResponse buildResponseDefault(String type, String text, )
}
