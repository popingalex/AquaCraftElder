package org.aqua.parse.poi;

import test.Tester;

public class ExcelTest extends Tester {
    static String path = "C:\\Users\\Administrator\\git\\AT_RF_Client\\source\\话单文件名称及各记录字段统计.xlsx";
    @Override
    public void test(String[] args) {
        new Excel().read(path);
        
    }

}