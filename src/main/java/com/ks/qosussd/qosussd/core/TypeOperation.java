package com.ks.qosussd.qosussd.core;

public enum TypeOperation {
    CONTINUE("continue"),
    NEXT("new"),
    END("end");
    private String type;

    private TypeOperation(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


}
