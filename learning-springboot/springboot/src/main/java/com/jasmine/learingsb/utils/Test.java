package com.jasmine.learingsb.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

public class Test {
    public static void main(String[] args) {

        RSA rsa = new RSA();
        //获得私钥
        String privateKey = rsa.getPrivateKeyBase64();
        //获得公钥
        String publicKey = rsa.getPublicKeyBase64();

        System.out.println(privateKey);

        System.out.println("==============================");

        System.out.println(publicKey);

        byte[] encrypt = rsa.encrypt(StrUtil.bytes("jasmine test", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);

        byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);

        System.out.println(StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

    }
}
