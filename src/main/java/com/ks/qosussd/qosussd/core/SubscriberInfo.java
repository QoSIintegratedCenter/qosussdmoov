package com.ks.qosussd.qosussd.core;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;

public class SubscriberInfo implements Serializable {
    private String msisdn;
    private int menuLevel;
    private String sc;
    private String userInput;
    private String lang;
    private int req_no;
    private String screenId;
    private BigDecimal amount;
    private String accountNo;
    private String sessionId;
    private HashMap<String, Object> subParams = new HashMap<>();

    public String getMsisdn() {
        return msisdn;
    }

    public void incrementMenuLevel() {
        menuLevel++;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public int getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(int menuLevel) {
        this.menuLevel = menuLevel;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public HashMap<String, Object> getSubParams() {
        return subParams;
    }

    public void setSubParams(HashMap<String, Object> subParams) {
        this.subParams = subParams;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getReq_no() {
        return req_no;
    }

    public void setReq_no(int req_no) {
        this.req_no = req_no;
    }

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "SubscriberInfo{" +
                "msisdn='" + msisdn + '\'' +
                ", menuLevel=" + menuLevel +
                ", sc='" + sc + '\'' +
                ", userInput='" + userInput + '\'' +
                ", lang='" + lang + '\'' +
                ", req_no=" + req_no +
                ", screenId='" + screenId + '\'' +
                ", amount=" + amount +
                ", accountNo='" + accountNo + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", subParams=" + subParams +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscriberInfo)) return false;
        SubscriberInfo that = (SubscriberInfo) o;
        return getMenuLevel() == that.getMenuLevel() &&
                getReq_no() == that.getReq_no() &&
                Objects.equals(getMsisdn(), that.getMsisdn()) &&
                Objects.equals(getSc(), that.getSc()) &&
                Objects.equals(getUserInput(), that.getUserInput()) &&
                Objects.equals(getLang(), that.getLang()) &&
                Objects.equals(getScreenId(), that.getScreenId()) &&
                Objects.equals(getAmount(), that.getAmount()) &&
                Objects.equals(getAccountNo(), that.getAccountNo()) &&
                Objects.equals(getSessionId(), that.getSessionId()) &&
                Objects.equals(getSubParams(), that.getSubParams());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getMsisdn(), getMenuLevel(), getSc(), getUserInput(), getLang(), getReq_no(), getScreenId(), getAmount(), getAccountNo(), getSessionId(), getSubParams());
    }

}
