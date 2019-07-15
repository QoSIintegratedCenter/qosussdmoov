package com.ks.qosussd.qosussd.web;

import com.ks.qosussd.qosussd.core.MoovUssdResponse;
import com.ks.qosussd.qosussd.core.SubscriberInfo;

import java.util.concurrent.ConcurrentHashMap;

public class ProcessUssd {

    public static final ConcurrentHashMap<String, SubscriberInfo> activeSessions = new ConcurrentHashMap<>();

   MoovUssdResponse welcomLevel(){
       MoovUssdResponse moovUssdResponse = new MoovUssdResponse();

       return moovUssdResponse;
   }
}
