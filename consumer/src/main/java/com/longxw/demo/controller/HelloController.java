package com.longxw.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author longxw
 * @since 2019/12/10
 */
@RestController
@RequestMapping("/hello")
@Slf4j
public class HelloController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(){
        log.info("come in consul-consumer -> test");
        return "success";
    }

}
