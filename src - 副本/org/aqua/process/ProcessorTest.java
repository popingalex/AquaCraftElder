package org.aqua.process;

import java.util.Arrays;

import test.Tester;

public class ProcessorTest extends Tester{

    @Override
    public void test(String[] args) {
//        new Processor().attachCommands("ruby").attachCommands("D:/workspace/rails/Test/niltest.rb").process();
        new Processor().attachCommands("java").process();
//        test(new Object[]{1,2,23});
//        Object[] arg = new Object[]{1,2,23};
//        test(arg);
    }
    
    public void test(Object...srcs) {
        System.out.println(srcs.length);
        System.out.println(Arrays.toString(srcs));
    }
}
