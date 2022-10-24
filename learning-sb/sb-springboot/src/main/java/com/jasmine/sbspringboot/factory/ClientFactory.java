package com.xzzz.sbspringboot.factory;//package com.jasmine.learingsb.factory;
//
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @author jasmineXz
// */
//@Component
//public class ClientFactory {
//
//    Map<String,Client> clientMap = new ConcurrentHashMap<>();
//
//    public ClientFactory(List<Client> clients) {
//        clients.forEach(client -> {
//            System.out.println(client.getClass().getName());
//            clientMap.put(client.getClass().getName(),client);
//        });
//    }
//
//    public Map<String, Client> getClientMap() {
//        return clientMap;
//    }
//}
