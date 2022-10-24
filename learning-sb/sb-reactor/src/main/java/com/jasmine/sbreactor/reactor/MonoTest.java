package com.xzzz.sbreactor.reactor;

import reactor.core.publisher.Mono;

/**
 * @author wangyf
 */
public class MonoTest {

    public static void main(String[] args) {
        Mono<Integer> m = Mono.just(1);
        System.out.println(m.block());
    }
}
