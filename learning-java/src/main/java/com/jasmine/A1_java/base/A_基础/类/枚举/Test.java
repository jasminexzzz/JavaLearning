package com.jasmine.A1_java.base.A_基础.类.枚举;

public class Test {
    public static void main(String[] args) {
        Game g = new Game();
        g.setName("神秘海域");
        g.setType(TypeEnum.AVG.getTypeId());

        System.out.println(TypeEnum.AVG.getTypeId());
        System.out.println(TypeEnum.AVG.getTypeName());

        System.out.println(SingleEnum.C.getId());

    }
}

