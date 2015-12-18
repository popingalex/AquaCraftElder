package demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class Mass {

    public static void main(String[] args) {
        Integer a = null;
        a = get(2, null);
        System.out.println(a);
    }
    
    public static <T> T  get(Object o, T t) {
        return (T)o;
    }
    
}
