package com.ks.qosussd.qosussd.core;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@JacksonXmlRootElement(localName = "response")
@Data
@JsonPropertyOrder({"screenType", "text", "options", "backLink", "sessionOp", "screenId"})
@ToString
public class MoovUssdResponse implements Serializable {
    @JacksonXmlProperty(localName = "screen_type")
    private String screenType;
    @JacksonXmlProperty(localName = "text")
    private String text;
    @JacksonXmlProperty(localName = "session_op")
    private String sessionOp;
    @JacksonXmlProperty(localName = "screen_id")
    private int screenId;
    @JacksonXmlProperty
    private OptionsType options;
    @JacksonXmlProperty(localName = "back_link")
    private int backLink;
    @JacksonXmlProperty(localName = "home_link")
    private int homeLink;

}


