package com.ks.qosussd.qosussd.padme;

import com.ks.qosussd.qosussd.core.SubscriberInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import static com.ks.qosussd.qosussd.core.Constants.*;
import static com.ks.qosussd.qosussd.core.Utilities.*;
import static com.ks.qosussd.qosussd.web.ProcessUssd.oldSessions;

@Slf4j
public class ApiConnect {
    private Date startDate = new Date();
    private TaskScheduler taskScheduler = new ConcurrentTaskScheduler();
    private ScheduledFuture<?> scheduledFuture;
    RestTemplate restTemplate = new RestTemplate();

    public void checkMomoTransation(Map map, ScheduledFuture<?> scheduledFuture) {
        log.info("State start {} end  {} ", this.startDate, new Date());
        log.info("Shedulde {} ", scheduledFuture.isCancelled());
        RestTemplate restTemplate = new RestTemplate();
//        this.startDate.getMinutes()
        if (new Date().getTime() - this.startDate.getTime() > 5 * 60 * 1000) {
            log.info("cancel sheduler time out data ==> {} ", map.get("msisdn"));
            scheduledFuture.cancel(true);
        } else {
            try {
                Map res = restTemplate.exchange(getProp("momo_moov_gettransactionstatus"), HttpMethod.POST, new HttpEntity<Map>(map, createHeaders(getProp("momo_moov_username"), getProp("momo_moov_password"))), Map.class).getBody();

                if (res.get("responsecode").equals("0")) {
                    log.info("Transaction  for {} response {} ", map.get("msisdn"), res);
                    scheduledFuture.cancel(true);
                    postDataToPadmeDatabase(map);
                }

            } catch (Exception e) {
                log.error("Error to sent request payement {} ", e.getMessage());
            }
        }
    }

    public void postDataToPadmeDatabase(Map map) {
        Map transData = new HashMap();
         /* const transData = {
                'origine': this.config.getDefaultPhoneNumber(),
                'codCuenta': this.acountNumber,
                'codSistema': 'AH',
                'refTransQos': refTransQos,
                'estPrisEnCompte': 0,
                'fecha': new Date().toISOString().slice(0, 16),
                'tipoTrans': tipoTrans,
                'monto': +monto,
                'frais': this.config.getFraisTrans,
                'montoNeto': monto + this.config.getFraisTrans,
                'observation': obs,
                'telefono': this.config.getDefaultPhoneNumber(),
                'typeOperation': typeOperation
        };*/
        SubscriberInfo customer = oldSessions.get(map.get("msisdn"));
        log.info("customer {}", customer);
        Map accountInfo = new HashMap();
        String type = "";
        String observation = "";
        String tipoTrans = "";
        String ref = "";
        if (customer.getSubParams().get("option1").equals(DEPOT)) {
            type = "Depot";
            ref = (String) map.get("transref");
            if (customer.getSubParams().get("option2").equals(EPARGNE)) {
                tipoTrans = "103";
                observation = "Dépôt sur compte épargne";
                accountInfo = getAccountInfo(getProp("epargne_account") + customer.getMsisdn());
            } else if (customer.getSubParams().get("option2").equals(COURANT)) {
                tipoTrans = "104";
                observation = "Dépôt sur compte courant";
                accountInfo = getAccountInfo(getProp("operation_account") + customer.getMsisdn());
            }
        } else if (customer.getSubParams().get("option1").equals(RETRAIT)) {
            log.info("Retrait Option transation");
            type = "Retrait";
            ref = randomAlphaNumeric();

            if (customer.getSubParams().get("option2").equals(EPARGNE)) {
                tipoTrans = "103";
                observation = "Rétrait sur compte épargne";
                accountInfo = getAccountInfo(getProp("epargne_account") + customer.getMsisdn());
            } else if (customer.getSubParams().get("option2").equals(COURANT)) {
                tipoTrans = "104";
                observation = "Rétrait sur compte courant";
                accountInfo = getAccountInfo(getProp("operation_account") + customer.getMsisdn());
            }
        }

        transData.put("origine", customer.getMsisdn());
        transData.put("codCuenta", accountInfo.get("codCuenta"));
        transData.put("codSistema", "AH");
        transData.put("refTransQos", ref);
        transData.put("estPrisEnCompte", 0);
        transData.put("fecha", LocalDateTime.now().toString());
        transData.put("tipoTrans", tipoTrans);
        transData.put("monto", customer.getAmount());
        transData.put("frais", 200);
        transData.put("observation", observation);
        transData.put("typeOperation", type);
        transData.put("telefono", customer.getMsisdn());
        transData.put("terminal", "MOOV_USSD");
        transData.put("montoNeto", customer.getAmount().add(new BigDecimal(200)));
        System.out.println(transData);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-type", "Application/json");
        try {
            Map res = restTemplate.exchange(getProp("transaction"), HttpMethod.POST, new HttpEntity<Map>(transData, httpHeaders), Map.class).getBody();
//                restTemplate.postForObject(getProp("transaction"), transData, Map.class);
            if (res != null) {
                log.info("transation save successful : {}", res);
                if (customer.getSubParams().get("option1").equals(RETRAIT)) {
                    startPadmeChecking(transData);
                }
            }
        } catch (Exception e) {
            log.error("Error " + e.getMessage());
        }

    }

    private void startPadmeChecking(Map transData) {
        this.startDate = new Date();
        Duration duration = Duration.ofMillis(5000L);
        log.info("starDate padme {}", this.startDate);
//        ScheduledFuture<?> finalScheduledFuture = scheduledFuture;
        this.scheduledFuture = this.taskScheduler.scheduleAtFixedRate(() -> checkPadmeStatus(transData, this.scheduledFuture), duration);

    }

    public void startChecking(Map map) {
//      ScheduledFuture<?> scheduledFuture = null;
        this.startDate = new Date();
        Duration duration = Duration.ofMillis(5000L);
        log.info("starDate {}", this.startDate);
//        ScheduledFuture<?> finalScheduledFuture = scheduledFuture;
        this.scheduledFuture = this.taskScheduler.scheduleAtFixedRate(() -> checkMomoTransation(map, this.scheduledFuture), duration);
    }


    public Map getAccountInfo(String url) {

        RestTemplate restTemplate = new RestTemplate();
        Map res = new HashMap();

        try {
//
            res = restTemplate.getForObject(url, Map.class);
            log.info("Get infos : {}", res);
            return res;
        } catch (Exception e) {
            log.error("Error to get infos credit : {}", e);
            return res;
        }
    }

    public void checkPadmeStatus(Map map, ScheduledFuture<?> scheduledFuture) {
        RestTemplate restTemplate = new RestTemplate();
        Map res = new HashMap();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-type", "Application/json");
        try {

            res = restTemplate.getForObject(getProp("transaction_padme_status") + "/" + map.get("origine") + "/" + map.get("refTransQos"), Map.class);
            log.info("Get transstatu : {}", res);
            if (res.get("transfertResponseCode").equals("00")) {
                makedeposite(map);
                scheduledFuture.cancel(true);
                oldSessions.remove(map.get("origine"));
            } else if (new Date().getTime() - this.startDate.getTime() > 5 * 60 * 1000) {
                log.info("cancel sheduler time out data ==> {} ", map.get("origine"));
                scheduledFuture.cancel(true);
            }

        } catch (Exception e) {
            log.error("Error to get infos credit : {}", e);

        }
    }

    public void makedeposite(Map map) {
        Map data = new HashMap();
        data.put("msisdn", map.get("origine"));
        data.put("firstname", "padme");
        data.put("lastname", "Qos");
        data.put("clientid", getProp("momo_moov_clientId"));
        data.put("transref", map.get("refTransQos"));
        data.put("amount", map.get("monto"));
        RestTemplate restTemplate = new RestTemplate();
        try {
            Map res = restTemplate.exchange(getProp("momo_moov_deposit"), HttpMethod.POST, new HttpEntity<Map>(data, createHeaders(getProp("momo_moov_username"), getProp("momo_moov_password"))), Map.class).getBody();
            log.info("response make deposite {} ", res);

        } catch (Exception e) {
            log.error("Error to sent request payement {} ", e.getMessage());
        }
    }
}
