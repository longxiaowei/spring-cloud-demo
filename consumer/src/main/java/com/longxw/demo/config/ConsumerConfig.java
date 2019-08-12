package com.longxw.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/*@Component
@Configuration*/
public class ConsumerConfig {

    public static String code;

    //@Value("${test.config}")
    private void setCode(String code) {
        ConsumerConfig.code = code;
    }
}
