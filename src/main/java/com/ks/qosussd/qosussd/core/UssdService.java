package com.ks.qosussd.qosussd.core;


import com.ks.qosussd.qosussd.config.ConfigProperties;
import com.ks.qosussd.qosussd.soapdto.UssdRequest;
import com.ks.qosussd.qosussd.soapdto.UssdResponse;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by dinesh on 3/9/17.
 */
@Controller
public class UssdService {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UssdService.class);
    // Service messages
    private static final String SERVICE_EXIT_CODE = "000";
    private static final String SERVICE_PREV_CODE = "0";
    private static final String SERVICE_INIT_CODE = "#123#";
    private static final String SERVICE_ELECTRONICS_CODE = "1";
    private static final String SERVICE_COSMETICS_CODE = "2";
    private static final String SERVICE_HOUSEHOLDS_CODE = "3";
    private static final String REQUEST_SEND_URL = "http://localhost:7000/ussd/send";
    private static final String OPERATION_MT_CONT = "FC";
    private static final String OPERATION_MT_FIN = "mt-fin";

    ConfigProperties propertyReader = new ConfigProperties();

    // List to store the states of the menus
    private ArrayList<String> menuStates = new ArrayList<>();

    @RequestMapping("/qosussd")
    @ResponseBody
    public void onReceivedUssd(@RequestBody UssdRequest moUssdReq) {
        try {
            processRequest(moUssdReq);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // Process all kinds of requests to customer
    private void processRequest(UssdRequest moUssdReq) throws MalformedURLException {
        log.info("come : {} ", moUssdReq);
        UssdResponse mtUssdReq;
        String destinationAddress = moUssdReq.getMsisdn();
        if (menuStates.size() > 0) {
            switch (moUssdReq.getSubscriberInput()) {
                case "1":
                    log.info("select 1 : {} ", menuStates);
                    mtUssdReq = padmeHomeLevel(propertyReader.getConfigValue("welcome.page"), moUssdReq.getSessionId(), OPERATION_MT_CONT, destinationAddress);
                    menuStates.add("welcome.page");

                    break;
                case SERVICE_EXIT_CODE:
                    log.info("select exit : {} ", menuStates);
                    mtUssdReq = generateMTRequest(propertyReader.getConfigValue("exit.page"), moUssdReq.getSessionId(), OPERATION_MT_FIN, destinationAddress);
                    menuStates.clear();
                    break;
//                case SERVICE_ELECTRONICS_CODE:
//                    mtUssdReq = generateMTRequest(propertyReader.getConfigValue("electronics.page"), moUssdReq.getSessionId(), OPERATION_MT_CONT, destinationAddress);
//                    menuStates.add("electronics.page");
//                    break;
//                case SERVICE_COSMETICS_CODE:
//                    mtUssdReq = generateMTRequest(propertyReader.getConfigValue("cosmetics.page"), moUssdReq.getSessionId(), OPERATION_MT_CONT, destinationAddress);
//                    menuStates.add("cosmetics.page");
//                    break;
//                case SERVICE_HOUSEHOLDS_CODE:
//                    mtUssdReq = generateMTRequest(propertyReader.getConfigValue("households.page"), moUssdReq.getSessionId(), OPERATION_MT_CONT, destinationAddress);
//                    menuStates.add("households.page");
//                    break;
                case SERVICE_PREV_CODE:
                    mtUssdReq = padmeHomeLevel(backOperation(), moUssdReq.getSessionId(), OPERATION_MT_CONT, destinationAddress);
                    break;
                default:
                    mtUssdReq = padmeHomeLevel(propertyReader.getConfigValue("error.page"), moUssdReq.getSessionId(), OPERATION_MT_CONT, destinationAddress);
                    menuStates.add("error.page");
            }
        } else if (menuStates.size() == 1) {
            log.info("select 1 : leve1 {} ", menuStates);
           mtUssdReq = padmeHomeLevel(propertyReader.getConfigValue("page.level1"), moUssdReq.getSessionId(), OPERATION_MT_CONT, destinationAddress);
            menuStates.add("page.level1");

        } else {
            mtUssdReq = generateMTRequest(propertyReader.getConfigValue("error.page"), moUssdReq.getSessionId(), OPERATION_MT_CONT, destinationAddress);
            menuStates.add("error.page");
        }

//        UssdRequestSender ussdRequestSender = new UssdRequestSender(new URL(REQUEST_SEND_URL));
//        ussdRequestSender.sendUssdRequest(mtUssdReq);
        System.out.println(menuStates);
        return;
    }

    // Generate request to the customer
    private UssdResponse generateMTRequest(String message, String sessionId, String operation, String destinationAddress) {
        UssdResponse mtUssdReq = new UssdResponse();
//        mtUssdReq.setApplicationId("APP_000001");
//        mtUssdReq.setPassword("dfc0333b82a8e01f500e7e37188f97eo");
//        mtUssdReq.setApplicationResponse(message);
//        mtUssdReq.setSessionId(sessionId);
//        mtUssdReq.setUssdOperation(operation);
//        mtUssdReq.setDestinationAddress(destinationAddress);
        return mtUssdReq;
    }

    private UssdResponse padmeHomeLevel(String message, String sessionId, String operation, String destinationAddress) {
        UssdResponse mtUssdReq = new UssdResponse();
        mtUssdReq.setApplicationResponse(message);
        mtUssdReq.setMsisdn(destinationAddress);
        mtUssdReq.setFreeflow("FC");
        return mtUssdReq;
    }
    private UssdResponse padmeLevel1(String message, String sessionId, String operation, String destinationAddress) {
        UssdResponse mtUssdReq = new UssdResponse();
        mtUssdReq.setApplicationResponse(message);
        mtUssdReq.setMsisdn(destinationAddress);
        mtUssdReq.setFreeflow(operation);
        return mtUssdReq;
    }

    // Functionality of the back command
    private String backOperation() {
        String prevState = propertyReader.getConfigValue("welcome.page");
        System.out.println(menuStates.size());
        if (menuStates.size() > 0 && (menuStates.size() - 1) != 0) {
            prevState = propertyReader.getConfigValue(menuStates.get(menuStates.size() - 2));
            menuStates.remove(menuStates.size() - 1);
        }
        return prevState;
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }
}
