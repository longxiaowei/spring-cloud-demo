package com.longxw.demo.controller;

import com.longxw.demo.config.ConsumerConfig;
import com.longxw.demo.feign.client.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
@Slf4j
public class HelloController {

    @Autowired
    HelloService helloService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    String searchRepo(){
        log.info("come in consul-consumer -> searchRepo");
        helloService.searchRepo();
        return "success";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    String test(){
        log.info("come in consul-consumer -> test");
        helloService.test();
        return "success";
    }

}
