package com.xzzz.sbreactor.stream;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyf
 */
@Data
public class User {

    private Integer id;
    private String name;
    private List<String> addr;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<User> init() {

        List<User> users = new ArrayList<>();

        User u1 = new User(1, "A");
        User u2 = new User(2, "B");
        User u3 = new User(3, "C");
        User u4 = new User(4, "D");

        u1.setAddr(CollUtil.newArrayList("a1", "a2"));
        u2.setAddr(CollUtil.newArrayList("b1", "b2"));
        u3.setAddr(CollUtil.newArrayList("c1", "c2"));
        u4.setAddr(CollUtil.newArrayList("d1", "d2"));

        users.add(u1);
        users.add(u2);
        users.add(u3);
        users.add(u4);

        return users;
    }

}
