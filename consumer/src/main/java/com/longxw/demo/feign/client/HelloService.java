package com.longxw.demo.feign.client;

import com.longxw.demo.feign.fallback.HelloServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "consul-provider",fallback = HelloServiceFallBack.class)
public interface HelloService {
    @RequestMapping(value = "/search/repositories", method = RequestMethod.GET)
    String searchRepo();

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    String test();
}
