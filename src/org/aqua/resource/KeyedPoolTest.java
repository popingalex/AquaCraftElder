package org.aqua.resource;

import test.Tester;

public class KeyedPoolTest extends Tester {

    @Override
    public void test(String[] args) {
        KeyedPool<String, Integer> o = new KeyedPool<String, Integer>() {
            
            @Override
            protected String keyof(Integer object) {
                return object.toString();
            }
            
            @Override
            protected Integer create(String key) {
                return Integer.valueOf(key);
            }
        };
        
        int a = o.borrow("123");
        System.out.println("A:"+a);
    }

}
