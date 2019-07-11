package com.ks.qosussd.qosussd.core;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;


import java.io.Serializable;
import java.util.List;

@JacksonXmlRootElement
@Data
public class Response implements Serializable {
    @JacksonXmlProperty(localName = "screen_type")
    private String screenType;
    @JacksonXmlProperty
    private String text;
    @JacksonXmlProperty
    private String sessionOp;
    @JacksonXmlProperty
    private int screenId;
    @JacksonXmlProperty
    private List<Option> options;
    @JacksonXmlProperty
    private int backLink;
    @JacksonXmlProperty
    private int homeLink;

}


