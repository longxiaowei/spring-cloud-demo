package com.longxw.demo.feign.fallback;

import com.longxw.demo.feign.client.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelloServiceFallBack implements HelloService {

    public String searchRepo(){
        log.info("coming in HelloServiceFallBack method: searchRepo");
        return "HelloServiceFallBack";
    }

    public String test() {
        log.info("coming in HelloServiceFallBack method: test");
        return "HelloServiceFallBack";
    }
}
