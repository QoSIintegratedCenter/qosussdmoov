package com.ks.qosussd.qosussd.core;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

//@Data
@JacksonXmlRootElement

public class OptionsType {
    @JacksonXmlElementWrapper(useWrapping = false)
    protected List<Option> option;

    public List<Option> getOption() {
        if (option == null) {
            option = new ArrayList<Option>();
        }
        return this.option;
    }

    public void setOption(List<Option> option) {
        this.option = option;
    }

    public void addOption(Option option1) {
        if (option == null) {
            option = new ArrayList<Option>();
        }
        this.option.add(option1);
    }
}
