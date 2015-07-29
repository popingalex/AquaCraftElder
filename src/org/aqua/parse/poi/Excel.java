package org.aqua.parse.poi;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.aqua.io.file.FileUtil;

public class Excel {
    public void read(String path) {
        System.out.println(path);
        Workbook wb = null;
        
        try {
        if (path.endsWith("-xls")) {
                wb = new HSSFWorkbook(FileUtil.readFileStream(path));
        } else if (path.endsWith("xlsx")) {
            wb = new XSSFWorkbook();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println(wb.getNumberOfSheets());
    }
}