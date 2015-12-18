package demo;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public abstract class Tester {
    protected Logger logger = Logger.getLogger(getClass());

    protected void debug(Class<?> clazz) {
        Logger.getLogger(clazz).setLevel(Level.DEBUG);
    }
    protected void error(Class<?> clazz) {
        Logger.getLogger(clazz).setLevel(Level.ERROR);
    }
    protected void info(Class<?> clazz) {
        Logger.getLogger(clazz).setLevel(Level.INFO);
    }
    public void beforeTest(String[] args) {
    }
    public abstract void test(String[] args);
    public void afterTest(String[] args) {
    }
}
