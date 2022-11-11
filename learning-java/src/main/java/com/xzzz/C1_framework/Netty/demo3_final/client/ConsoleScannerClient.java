package com.xzzz.C1_framework.Netty.demo3_final.client;

import java.util.Scanner;

public class ConsoleScannerClient {

    public static void main(String[] args) {
        XzClient.start("127.0.0.1", 6666);
        Scanner sc = new Scanner(System.in);
        if (sc.nextBoolean()) {
            XzClient.send(sc.next());
        }
    }
}
