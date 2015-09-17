package test;

import java.util.LinkedList;

public class Demo {
    public static void main(String[] args) {
        LinkedList<Tester> testerList = new LinkedList<Tester>();

//        testerList.add(new TestUTFinGBK());
//        testerList.add(new EventUtilTest());
//        testerList.add(new InteractionTest());
//        testerList.add(new ProcessorTest());
//        testerList.add(new org.aqua.file.parse.poi.ExcelTest());
//        testerList.add(new org.aqua.graph.ocr.OCRTest());
//        testerList.add(new org.aqua.parse.MarkupLanguageTest());
        testerList.add(new org.aqua.structure.space.SpaceTester());
        for (Tester tester : testerList) {
            String testerName = tester.getClass().getName();
            System.out.println("===== [Demo - before  " + testerName + "] =====");
            tester.beforeTest(args);
            System.out.println("===== [Demo - process " + testerName + "] =====");
            tester.test(args);
            System.out.println("===== [Demo - after   " + testerName + "] =====");
            tester.afterTest(args);
        }
    }
}
