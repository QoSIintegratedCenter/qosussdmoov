package com.ks.qosussd.qosussd.padme;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import sun.rmi.runtime.Log;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Slf4j
public class ApiConnect {
    private Date startDate = new Date();
    private TaskScheduler taskScheduler = new ConcurrentTaskScheduler();
    private ScheduledFuture<?> scheduledFuture;

    public void checkMomoTransation(Map map, ScheduledFuture<?> scheduledFuture) {
        log.info("State start {} end  {} ", this.startDate, new Date());
        log.info("Shedulde {} ", scheduledFuture.isCancelled());
//        this.startDate.getMinutes()
        if (new Date().getTime() - this.startDate.getTime() >20*1000) {
            log.info("cancel sheduler ");
            scheduledFuture.cancel(true);
        }
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
