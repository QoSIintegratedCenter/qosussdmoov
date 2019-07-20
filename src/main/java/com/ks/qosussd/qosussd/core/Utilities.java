package com.ks.qosussd.qosussd.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class Utilities {
    protected static Environment env;

    @Autowired
    public void setEnv(Environment _env) {
        env = _env;

    }

    public static String getProp(String prop) {
        return env.getProperty(prop);
    }
}
