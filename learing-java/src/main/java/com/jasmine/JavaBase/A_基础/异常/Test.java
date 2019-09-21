package com.jasmine.JavaBase.A_基础.异常;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public void errorStr() throws NullPointerException {
        try {
            List l = new ArrayList();
            l.get(2);
        } catch (NullPointerException e) {
            throw new NullPointerException("空指针");
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Test().errorStr();
    }
}
