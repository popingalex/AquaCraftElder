package org.aqua.io.interaction;

import java.util.Scanner;

import test.Tester;

public class InteractionTest extends Tester {

    @Override
    public void test(String[] args) {
        Scanner sc = new Scanner(System.in, "GBK");
        for(String s = sc.nextLine();s.length() != 0;s = sc.nextLine()) {
            System.out.println("["+s+"]");
        }
    }
}
