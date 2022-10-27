package com.xzzz.C1_framework.Netty.demo2_rpc.server.session;

public abstract class SessionFactory {

    private static Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }
}
