package com.ks.qosussd.qosussd.core;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Option implements Serializable {
    @JacksonXmlProperty(isAttribute = true)
    private int choice;
    @JacksonXmlText
    private String value;
}
