package com.jasmine.sbreactor.reactor;

import com.jasmine.sbreactor.stream.User;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author wangyf
 */
public class FluxTest {

    private static final List<User> users = User.init();

    public static void main(String[] args) {
        System.out.println(Flux.fromStream(users.stream()).next().map(user -> user.getId()).block());
    }
}
