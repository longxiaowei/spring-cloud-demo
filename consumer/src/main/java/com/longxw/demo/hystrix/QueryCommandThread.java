package com.longxw.demo.hystrix;

import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class QueryCommandThread extends HystrixCommand<String> {

    private RestTemplate restTemplate;

    public QueryCommandThread(RestTemplate restTemplate){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("consul-provider"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("test"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("QueryCommandThread"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerRequestVolumeThreshold(2)////至少有2个请求，熔断器才进行错误率的计算
                        .withCircuitBreakerSleepWindowInMilliseconds(1000*50)//熔断器中断请求50秒后会进入半打开状态,放部分流量过去重试
                        .withCircuitBreakerErrorThresholdPercentage(10)//错误率达到50开启熔断保护
                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(10)));//最大并发请求量
        this.restTemplate = restTemplate;
    }

    @Override
    protected String run() throws Exception {
        System.out.println(Thread.currentThread().getId());
        return restTemplate.getForObject("http://localhost:8800/test",String.class);
    }

    @Override
    protected String getFallback() {
        System.out.println(Thread.currentThread().getId());
        log.info("QueryCommandSemaphore->getFallback");
        return "QueryCommandSemaphore->getFallback";
    }

}
