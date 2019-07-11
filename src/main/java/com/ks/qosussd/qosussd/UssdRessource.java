package com.ks.qosussd.qosussd;

import com.ks.qosussd.qosussd.core.Option;
import com.ks.qosussd.qosussd.core.OptionsType;
import com.ks.qosussd.qosussd.core.Response;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UssdRessource {

    @GetMapping(name = "/test2", produces = MediaType.APPLICATION_XML_VALUE)
    private Response responseTest() {
        Response response = new Response();
        response.setText("text ussd");
        response.setBackLink(1);
        response.setHomeLink(0);
        response.setScreenId(1);
        response.setScreenType("form");

        Option option = new Option();
        option.setChoice(1);
        option.setValue("Momo ");
        Option option1 = new Option();
        option1.setChoice(2);
        option1.setValue("Moov ");
        List<Option> optionList = new ArrayList<>();
        optionList.add(option);
        optionList.add(option1);
        OptionsType optionsType = new OptionsType();
        optionsType.getOption().add(option);
        optionsType.getOption().add(option1);
        response.setOptions(optionsType);
        return response;
    }
}
