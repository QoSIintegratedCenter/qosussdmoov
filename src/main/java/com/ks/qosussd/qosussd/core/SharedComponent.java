package com.ks.qosussd.qosussd.core;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SharedComponent {


    public MoovUssdResponse endOperation(String text) {
        return getMoovUssdResponseEnd(text);
    }

    public MoovUssdResponse getMoovUssdResponseEnd(String text) {
        MoovUssdResponse moovUssdResponse = new MoovUssdResponse();
//        moovUssdResponse.setScreenId(screenId);
        moovUssdResponse.setText(text);
        moovUssdResponse.setBackLink(0);
        moovUssdResponse.setScreenType("form");
        moovUssdResponse.setSessionOp(TypeOperation.END.getType());
        return moovUssdResponse;
    }
}
