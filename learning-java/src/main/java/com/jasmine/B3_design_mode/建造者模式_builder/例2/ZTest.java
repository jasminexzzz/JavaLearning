package com.jasmine.B3_design_mode.建造者模式_builder.例2;

/**
 * @author jasmineXz
 */
public class ZTest {
    public static void main(String[] args) {
        UserBuilder user = UserBuilder.builder()
            .userId(1)
            .userName("123")
            .pwd("33333")
            .age(25)
                .build();
        System.out.println(user.toString());
    }
}
