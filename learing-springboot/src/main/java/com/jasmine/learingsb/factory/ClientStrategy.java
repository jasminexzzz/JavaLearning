package com.jasmine.learingsb.factory;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jasmineXz
 */
@Primary
@Component
public class ClientStrategy implements Client {

    Map<String,Client> clientMap = new ConcurrentHashMap<>();

    public ClientStrategy(List<Client> clients) {
        clients.forEach(client -> {
            System.out.println(client.getClass().getName());
            clientMap.put(client.getClass().getName(),client);
        });
    }


    @Override
    public boolean checkClient(String clientType) {
        return clientMap.get(clientType).checkClient(clientType);
    }
}
