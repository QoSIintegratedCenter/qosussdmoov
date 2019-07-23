package com.ks.qosussd.qosussd.padme;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.client.RestTemplate;
import sun.rmi.runtime.Log;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import static com.ks.qosussd.qosussd.core.Utilities.createHeaders;
import static com.ks.qosussd.qosussd.core.Utilities.getProp;

@Slf4j
public class ApiConnect {
    private Date startDate = new Date();
    private TaskScheduler taskScheduler = new ConcurrentTaskScheduler();
    private ScheduledFuture<?> scheduledFuture;

    public void checkMomoTransation(Map map, ScheduledFuture<?> scheduledFuture) {
        log.info("State start {} end  {} ", this.startDate, new Date());
        log.info("Shedulde {} ", scheduledFuture.isCancelled());
        RestTemplate restTemplate = new RestTemplate();
//        this.startDate.getMinutes()
        if (new Date().getTime() - this.startDate.getTime() > 20 * 1000) {
            log.info("cancel sheduler time out data ==> {} ", map.get("msisdn"));
            scheduledFuture.cancel(true);
        } else {
            try {
                Map res = restTemplate.exchange(getProp("momo_moov_gettransactionstatus"), HttpMethod.POST, new HttpEntity<Map>(map, createHeaders(getProp("momo_moov_username"), getProp("momo_moov_password"))), Map.class).getBody();

                if (res.get("responsecode").equals("00")) {
                    log.info("Transaction  for {} response {} ", map.get("msisdn"), res);
                    scheduledFuture.cancel(true);
                    postDataToPadmeDatabase(map);
                }

            } catch (Exception e) {
                log.error("Error to sent request payement {} ", e.getMessage());
            }
        }
    }

    private void postDataToPadmeDatabase(Map map) {
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


    }

    public void startChecking(Map map) {
//      ScheduledFuture<?> scheduledFuture = null;
        this.startDate = new Date();
        Duration duration = Duration.ofMillis(5000L);
        log.info("starDate {}", this.startDate);
//        ScheduledFuture<?> finalScheduledFuture = scheduledFuture;
        this.scheduledFuture = this.taskScheduler.scheduleAtFixedRate(() -> checkMomoTransation(map, this.scheduledFuture), duration);
    }
}
