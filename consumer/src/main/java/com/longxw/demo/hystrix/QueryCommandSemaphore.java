package com.longxw.demo.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class QueryCommandSemaphore extends HystrixCommand<String> {

    private RestTemplate restTemplate;

    public QueryCommandSemaphore(RestTemplate restTemplate){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("consul-provider"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("test"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerRequestVolumeThreshold(2)
                        .withCircuitBreakerSleepWindowInMilliseconds(1000*50)
                        .withCircuitBreakerErrorThresholdPercentage(10)
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE) //设置使用 信号量隔离
                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(10)));//最大并发请求量
        this.restTemplate = restTemplate;
    }

    @Override
    protected String run() throws Exception {
        return restTemplate.getForObject("http://localhost:8800/test",String.class);
    }

    @Override
    protected String getFallback() {
        log.info("QueryCommandSemaphore->getFallback");
        return "QueryCommandSemaphore->getFallback";
    }
}
