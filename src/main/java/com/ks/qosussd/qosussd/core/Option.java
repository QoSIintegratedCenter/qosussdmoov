package com.ks.qosussd.qosussd.core;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

import java.io.Serializable;

@Data
public class Option implements Serializable {
    @JacksonXmlProperty(isAttribute = true)
    private int choice;
    @JacksonXmlText
    private String value;
}
