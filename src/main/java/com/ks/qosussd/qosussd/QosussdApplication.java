package com.ks.qosussd.qosussd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class QosussdApplication {

    public static void main(String[] args) {
        SpringApplication.run(QosussdApplication.class, args);
    }

    @GetMapping("test")
    String test() {
        return "test ok ";
    }
}
