package com.xzzz.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SbGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(
                "sentinel1",
                r -> r.path("/sentinel/**")
                    .and().weight("group1",1)
                    .filters(f -> f.stripPrefix(1))
                    .uri("http://localhost:12346")
            )
            .route(
                "sentinel2",
                r -> r.path("/sentinel/**")
                    .and().weight("group1",1)
                    .filters(f -> f.stripPrefix(1))
                    .uri("http://localhost:12347")
            )
            .build();
    }

}
