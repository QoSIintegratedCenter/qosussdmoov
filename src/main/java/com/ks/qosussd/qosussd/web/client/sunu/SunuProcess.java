package com.ks.qosussd.qosussd.web.client.sunu;

import com.ks.qosussd.qosussd.core.*;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SunuProcess {

    public static final ConcurrentHashMap<String, SubscriberInfo> activeSunuSessions = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, SubscriberInfo> oldSunuSessions = new ConcurrentHashMap<>();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    MoovUssdResponse welcomLevel(SubscriberInfo sub) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
        moovUssdResponse.setText("Welcome to sunu ");

        moovUssdResponse.setScreenId(Integer.parseInt(sub.getScreenId()));
        moovUssdResponse.setScreenType("menu");
        moovUssdResponse.setSessionOp(TypeOperation.CONTINUE.getType());

        Option option = new Option();
        option.setChoice("1.");
        option.setValue("Sunu");
        OptionsType optionsType = new OptionsType();
        optionsType.getOption().add(option);
        moovUssdResponse.setOptions(optionsType);
        return moovUssdResponse;
    }


}
