package com.longxw.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DemoController {
    @RequestMapping(value = "/search/repositories", method = RequestMethod.GET)
    public String searchRepo(){
        log.info("come in consul-provider Method:searchRepo");
        return "success";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(){
        log.info("come in consul-provider Method:test");
        throw new RuntimeException("需要妹子救命");
    }

}
