/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ks.qosussd.qosussd.soapdto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 *
 * @author ptrack
 */
//@XmlRootElement(name = "response")
@JacksonXmlRootElement(localName = "response")
public class UssdResponse {

    //@XmlElement(required = true)
    @JacksonXmlProperty
    private String msisdn;
    //@XmlElement(required = true)
    private String applicationResponse;
    
    //@XmlElement(required = true)
    @JacksonXmlProperty
    private Freeflow freeflow;
    
    //private String appDrivenMenuCode;

    /**
     * @return the msisdn
     */
//    @XmlElement
    public String getMsisdn() {
        return msisdn;
    }

    /**
     * @param msisdn the msisdn to set
     */
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    /**
     * @return the applicationResponse
     */
//    @XmlElement
    public String getApplicationResponse() {
        return applicationResponse;
    }

    /**
     * @param applicationResponse the applicationResponse to set
     */
    public void setApplicationResponse(String applicationResponse) {
        this.applicationResponse = applicationResponse;
    }

    /**
     * @return the freeflow
     */
//    @XmlElement
    public Freeflow getFreeflow() {
        return freeflow;
    }

    /**
     * @param freeflow the freeflow to set
     */
    public void setFreeflow(Freeflow freeflow) {
        this.freeflow = freeflow;
    }

    @Override
    public String toString() {
        return msisdn + "|" + freeflow + "|" + applicationResponse.replaceAll("\n", " ");
    }
    
}
