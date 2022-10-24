package com.xzzz.B3_design_mode.代理模式_Proxy.动态代理.例子三;

/**
 * service实现类
 * @author : jasmineXz
 */
public class UserServiceImpl implements UserService {
    @Override
    public void save(User u) {
        System.out.println("UserServiceImpl :---> 保存了一个用户:"+u.getName());
    }

    @Override
    public User getUser(User user) {
        User u = new User("wang",1);
        System.out.println("UserServiceImpl :---> 查到了一个用户");
        return u;
    }
}
