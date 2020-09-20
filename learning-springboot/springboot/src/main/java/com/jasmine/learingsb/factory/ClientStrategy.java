package com.jasmine.learingsb.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jasmineXz
 */
@Slf4j
@Primary
@Component
public class ClientStrategy implements Client {
    private final Map<String,Client> clientMap = new ConcurrentHashMap<>();

    public ClientStrategy(List<Client> clients) {
        log.trace("############################## 策略类 [开始] ##############################");
        clients.forEach(client -> {
            log.trace(client.getClass().getName());
            clientMap.put(client.getClass().getName(),client);
        });
        log.trace("############################## 策略类 [结束] ##############################");
    }

    @Override
    public boolean checkClient(String clientType) {
        return clientMap.get(clientType).checkClient(clientType);
    }
}
