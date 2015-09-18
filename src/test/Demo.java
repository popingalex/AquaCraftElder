package test;

import java.util.LinkedList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.ConsoleAppender;

public class Demo {
    public static void main(String[] args) {
        Logger logger = Logger.getRootLogger();
        logger.addAppender(new ConsoleAppender(new SimpleLayout()));
        logger.setLevel(Level.INFO);
        LinkedList<Tester> testerList = new LinkedList<Tester>();

        // testerList.add(new TestUTFinGBK());
        // testerList.add(new EventUtilTest());
        // testerList.add(new InteractionTest());
        // testerList.add(new ProcessorTest());
        // testerList.add(new org.aqua.file.parse.poi.ExcelTest());
        // testerList.add(new org.aqua.graph.ocr.OCRTest());
        // testerList.add(new org.aqua.parse.MarkupLanguageTest());
        testerList.add(new org.aqua.structure.space.SpaceTester());
        // testerList.add(new org.aqua.resource.KeyedPoolTest());

        for (Tester tester : testerList) {
            String testerName = tester.getClass().getName();
            logger.info("===== [Demo - before  " + testerName + "] =====");
            tester.beforeTest(args);
            logger.info("===== [Demo - process " + testerName + "] =====");
            tester.test(args);
            logger.info("===== [Demo - after   " + testerName + "] =====");
            tester.afterTest(args);
        }
    }
}
