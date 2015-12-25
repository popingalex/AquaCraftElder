package demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class Mass {

    public static void main(String[] args) {
        System.out.println(new int[]{1,2,3}.equals(new int[]{1,2,3}));
    }
    
    public static <T> T  get(Object o, T t) {
        return (T)o;
    }
    
}
