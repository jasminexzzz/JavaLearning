package com.jasmine.C1_framework.Netty.demo_final;


public class SerializerUtil {

    public static byte[] serializationContent(XzProtocolInfo info) {
        return info.getContent().getBytes();
    }

    public static String deserializationContent(byte[] bytes) {
        if (bytes.length != 0) {
            return new String(bytes);
        }
        return null;
    }
}
