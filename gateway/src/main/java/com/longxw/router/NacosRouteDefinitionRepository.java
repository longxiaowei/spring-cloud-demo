package com.longxw.router;

import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosConfigProperties;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.concurrent.Executor;

/**
 * @author longxw
 * @since 2019/12/10
 */
@Slf4j
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {

    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;

    private static final String DATA_ID = "longxw-gateway";

    private static final String GROUP_ID = "test";

    private static final long TIME_OUT = 1000 * 5;

    private ApplicationEventPublisher publisher;
    private NacosConfigProperties nacosConfigProperties;

    public NacosRouteDefinitionRepository(ApplicationEventPublisher publisher, NacosConfigProperties nacosConfigProperties){
        this.publisher = publisher;
        this.nacosConfigProperties = nacosConfigProperties;
        addListener();
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        try{
            String content = nacosConfigProperties.configServiceInstance().getConfig(DATA_ID, GROUP_ID, TIME_OUT);
            System.out.println(content);
            Flux<RouteDefinition> routeDefinitions = routeDefinitionLocator.getRouteDefinitions();
            System.out.println(routeDefinitions.toString());
            return Flux.fromIterable(new ArrayList<>());
        }catch (Exception e){
            log.error("获取配置失败：{}", e);
            return Flux.fromIterable(new ArrayList<>());
        }

    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }

    private void addListener() {
        try{
            nacosConfigProperties.configServiceInstance().addListener(DATA_ID, GROUP_ID, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String s) {
                    // 发布刷新路由事件
                    publisher.publishEvent(new RefreshRoutesEvent(this));
                }
            });
        }catch (NacosException e){
            log.error("添加监听失败：{}", e);
        }

    }
}
