package org.aqua.thread.event.impl;

public interface EventCase<E extends Enum<?>> {
    public final static String CASE_DEFAULT = "default";
    public int toInteger();

    public enum DefaultCase implements EventCase<DefaultCase> {
        DEFAULT {
            @Override
            public String toString() {
                return CASE_DEFAULT;
            }

            @Override
            public int toInteger() {
                return 0;
            }
        }
    }
}
