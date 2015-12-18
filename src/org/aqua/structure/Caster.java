package org.aqua.structure;

public class Caster {
    @SuppressWarnings("unchecked")
    public static <T> T  cast(Object source, T target) {
        return (T)source;
    }
}
