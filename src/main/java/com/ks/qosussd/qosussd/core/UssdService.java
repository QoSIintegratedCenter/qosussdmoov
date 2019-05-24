package com.ks.qosussd.qosussd.core;


import com.ks.qosussd.qosussd.config.ConfigProperties;
import hms.kite.samples.api.TapException;
import hms.kite.samples.api.ussd.UssdRequestSender;
import hms.kite.samples.api.ussd.messages.MoUssdReq;
import hms.kite.samples.api.ussd.messages.MtUssdReq;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dinesh on 3/9/17.
 */
@Controller
public class UssdService {

    // Service messages
    private static final String SERVICE_EXIT_CODE = "000";
    private static final String SERVICE_PREV_CODE = "999";
    private static final String SERVICE_INIT_CODE = "#123#";
    private static final String SERVICE_ELECTRONICS_CODE = "1";
    private static final String SERVICE_COSMETICS_CODE = "2";
    private static final String SERVICE_HOUSEHOLDS_CODE = "3";
    private static final String REQUEST_SEND_URL = "http://localhost:7000/ussd/send";
    private static final String OPERATION_MT_CONT = "mt-cont";
    private static final String OPERATION_MT_FIN = "mt-fin";

    ConfigProperties propertyReader = new ConfigProperties();

    // List to store the states of the menus
    private ArrayList<String> menuStates = new ArrayList<>();

    @RequestMapping("/qosussd")
    @ResponseBody
    public void onReceivedUssd(@RequestBody MoUssdReq moUssdReq) {
        try {
            try {
                processRequest(moUssdReq);
            } catch (TapException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // Process all kinds of requests to customer
    private void processRequest(MoUssdReq moUssdReq) throws MalformedURLException, TapException {
        MtUssdReq mtUssdReq;
        String destinationAddress = moUssdReq.getSourceAddress();
        if (menuStates.size() > 0) {
            switch (moUssdReq.getMessage()) {
                case SERVICE_INIT_CODE:
                    mtUssdReq = generateMTRequest(propertyReader.getConfigValue("welcome.page"), moUssdReq.getSessionId(), OPERATION_MT_CONT, destinationAddress);
                    menuStates.add("welcome.page");
                    break;
                case SERVICE_EXIT_CODE:
                    mtUssdReq = generateMTRequest(propertyReader.getConfigValue("exit.page"), moUssdReq.getSessionId(), OPERATION_MT_FIN, destinationAddress);
                    menuStates.clear();
                    break;
                case SERVICE_ELECTRONICS_CODE:
                    mtUssdReq = generateMTRequest(propertyReader.getConfigValue("electronics.page"), moUssdReq.getSessionId(), OPERATION_MT_CONT, destinationAddress);
                    menuStates.add("electronics.page");
                    break;
                case SERVICE_COSMETICS_CODE:
                    mtUssdReq = generateMTRequest(propertyReader.getConfigValue("cosmetics.page"), moUssdReq.getSessionId(), OPERATION_MT_CONT, destinationAddress);
                    menuStates.add("cosmetics.page");
                    break;
                case SERVICE_HOUSEHOLDS_CODE:
                    mtUssdReq = generateMTRequest(propertyReader.getConfigValue("households.page"), moUssdReq.getSessionId(), OPERATION_MT_CONT, destinationAddress);
                    menuStates.add("households.page");
                    break;
                case SERVICE_PREV_CODE:
                    mtUssdReq = generateMTRequest(backOperation(), moUssdReq.getSessionId(), OPERATION_MT_CONT, destinationAddress);
                    break;
                default:
                    mtUssdReq = generateMTRequest(propertyReader.getConfigValue("error.page"), moUssdReq.getSessionId(), OPERATION_MT_CONT, destinationAddress);
                    menuStates.add("error.page");
            }
        } else {
            mtUssdReq = generateMTRequest(propertyReader.getConfigValue("error.page"), moUssdReq.getSessionId(), OPERATION_MT_CONT, destinationAddress);
            menuStates.add("error.page");
        }

        UssdRequestSender ussdRequestSender = new UssdRequestSender(new URL(REQUEST_SEND_URL));
        ussdRequestSender.sendUssdRequest(mtUssdReq);
        System.out.println(menuStates);
    }

    // Generate request to the customer
    private MtUssdReq generateMTRequest(String message, String sessionId, String operation, String destinationAddress) {
        MtUssdReq mtUssdReq = new MtUssdReq();
        mtUssdReq.setApplicationId("APP_000001");
        mtUssdReq.setPassword("dfc0333b82a8e01f500e7e37188f97eo");
        mtUssdReq.setMessage(message);
        mtUssdReq.setSessionId(sessionId);
        mtUssdReq.setUssdOperation(operation);
        mtUssdReq.setDestinationAddress(destinationAddress);
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
}
