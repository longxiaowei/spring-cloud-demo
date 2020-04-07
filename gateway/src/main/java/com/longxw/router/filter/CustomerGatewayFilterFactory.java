package com.longxw.router.filter;

import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.style.ToStringCreator;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * 自定义 filter 工厂
 * 1. 定义 filter需要的参数类，即config类
 * 2. 实现 apply 方法
 * 3. 将 filter 工厂注入到 spring容器中
 * @author longxw
 * @since 2020/4/7
 */
public class CustomerGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomerGatewayFilterFactory.Config> {
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            /**
             * filter 逻辑
             */
            ServerHttpRequest request = exchange.getRequest();
            String value = request.getHeaders().getFirst(config.key);
            if(config.getValue().equalsIgnoreCase(value)){
                return chain.filter(exchange);
            }
            throw new RuntimeException("无权访问");
        };
    }

    @Validated
    @Data
    public static class Config{
        @NotEmpty
        private String key;
        @NotEmpty
        private String value;

        @Override
        public String toString() {
            return new ToStringCreator(this)
                    .append("key", key)
                    .append("value", value)
                    .toString();
        }
    }
}
