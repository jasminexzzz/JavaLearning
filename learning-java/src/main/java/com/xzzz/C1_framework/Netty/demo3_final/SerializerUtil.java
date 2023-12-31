package com.xzzz.C1_framework.Netty.demo3_final;


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
