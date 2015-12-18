package org.aqua.parse.itext;

import com.lowagie.text.Anchor;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.document.RtfDocumentSettings;
import com.lowagie.text.rtf.style.RtfParagraphStyle;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public class RTFExample {
    private Document          document;
    private Font              titleFont;
    private RtfParagraphStyle rtfGsBt1;
    private RtfParagraphStyle rtfGsBt2;
    private RtfParagraphStyle rtfGsBt3;
    private RtfParagraphStyle rtfGsBt4;
    private RtfParagraphStyle rtfGsBt5;
    private RtfParagraphStyle rtfGsBt6;
    private int               h1_seq = 1;
    private int               h2_seq = 1;
    private int               h3_seq = 1;
    private int               h4_seq = 1;
    private int               h5_seq = 1;

    public RTFExample(String filename) {
        this.document = new Document();
        RtfWriter2 writer;
        try {
            writer = RtfWriter2.getInstance(this.document, new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.document.open();
        this.titleFont = new Font(0, 10.0F, 1);
        this.titleFont.setFamily("宋体");

        this.rtfGsBt1 = RtfParagraphStyle.STYLE_HEADING_1;
        this.rtfGsBt1.setAlignment(0);
        this.rtfGsBt1.setStyle(1);
        this.rtfGsBt1.setFamily("宋体");
        this.rtfGsBt1.setSize(24.0F);
        this.rtfGsBt1.setFontName("宋体");
        this.rtfGsBt1.setSpacingBefore(10);
        this.rtfGsBt1.setSpacingAfter(10);

        this.rtfGsBt2 = RtfParagraphStyle.STYLE_HEADING_2;
        this.rtfGsBt2.setAlignment(0);
        this.rtfGsBt2.setStyle(1);
        this.rtfGsBt2.setSize(20.0F);
        this.rtfGsBt2.setFontName("宋体");
        this.rtfGsBt2.setSpacingBefore(10);
        this.rtfGsBt2.setSpacingAfter(10);

        this.rtfGsBt3 = RtfParagraphStyle.STYLE_HEADING_3;
        this.rtfGsBt3.setAlignment(0);
        this.rtfGsBt3.setStyle(1);
        this.rtfGsBt3.setSize(18.0F);
        this.rtfGsBt3.setFontName("宋体");
        this.rtfGsBt3.setSpacingBefore(10);
        this.rtfGsBt3.setSpacingAfter(10);

        RtfDocumentSettings settings = writer.getDocumentSettings();
        RtfParagraphStyle heading_4 = new RtfParagraphStyle("heading 4", "Normal");
        RtfParagraphStyle heading_5 = new RtfParagraphStyle("heading 5", "Normal");
        heading_4.setAlignment(0);
        heading_4.setStyle(1);
        heading_4.setSize(16.0F);
        heading_4.setFontName("宋体");
        heading_4.setSpacingBefore(10);
        heading_4.setSpacingAfter(10);
        heading_5.setAlignment(0);
        heading_5.setStyle(0);
        heading_5.setSize(12.0F);
        heading_5.setFontName("宋体");
        heading_5.setSpacingBefore(10);
        heading_5.setSpacingAfter(10);

        settings.registerParagraphStyle(heading_4);
        settings.registerParagraphStyle(heading_5);

        this.rtfGsBt4 = heading_4;
        this.rtfGsBt6 = heading_5;

        this.rtfGsBt5 = RtfParagraphStyle.STYLE_NORMAL;
        this.rtfGsBt5.setAlignment(0);
        this.rtfGsBt5.setStyle(0);
        this.rtfGsBt5.setSize(10.0F);
        this.rtfGsBt5.setFontName("宋体");
    }

    private void addTableTitleCell(Table table, String name) {
        Cell celltmp = new Cell();
        celltmp.setBackgroundColor(new Color(196, 196, 196));
        Paragraph p = new Paragraph(name);
        if (name.equals("响应时长（秒）") || name.equals("失败步骤名称") || name.equals("失败步骤信息详情")) {
            Font f = new Font(0, 10.0F, 1);
            f.setFamily("宋体");
            f.setColor(new Color(255, 0, 0));
            p.setFont(f);
        } else {
            p.setFont(this.titleFont);
        }
        celltmp.add(p);
        table.addCell(celltmp);
    }

    public void createCaseTable(String[][] cases, int col_size) throws DocumentException, MalformedURLException {
        Table table = new Table(col_size, cases.length);
        String[] titles = { "案例名称", "数据名称", "数据说明", "结果" };
        String[] titles_failDetail = { "案例名称", "数据名称", "数据说明", "失败步骤名称", "失败步骤信息详情", "响应时长（秒）" };

        int[] width = { 20, 25, 45, 10 };
        int[] width2 = { 25, 35, 40 };
        int[] width_failDetail = { 15, 15, 14, 16, 30, 10 };
        if (col_size == 4) {
            table.setWidths(width);
        } else if (col_size == 6) {
            table.setWidths(width_failDetail);
            titles = titles_failDetail;
        } else {
            table.setWidths(width2);
        }
        table.setWidth(95.0F);
        table.setAlignment(5);
        for (int i = 0; (i < col_size) && (i < titles.length); ++i) {
            addTableTitleCell(table, titles[i]);
        }

        String module_name = "";
        int module_add = 0;
        Cell module_cell = null;
        for (int i = 0; i < cases.length; ++i) {
            for (int j = 0; (j < cases[i].length) && (j < col_size); ++j) {
                if ((j != 0) && (j != 3) && (j != 1) && (j != 4) && (j != 5)) {
                    Paragraph p = new Paragraph(cases[i][j]);
                    p.setFont(this.rtfGsBt5);
                    table.addCell(p);
                } else if (j == 1) {
                    Chunk c = new Chunk(cases[i][j]);
                    Anchor a = new Anchor(c);

                    a.setName(cases[i][j]);

                    a.setReference("#" + cases[i][j]);
                    a.setFont(this.rtfGsBt5);

                    table.addCell(a);
                } else if (j == 3) {
                    if (col_size == 4) {
                        if (cases[i][j].equals("10")) {
                            Paragraph p = new Paragraph("成功");
                            Font f = new Font(this.rtfGsBt5);
                            f.setColor(new Color(0, 0, 0));
                            p.setFont(f);
                            table.addCell(p);
                        } else if (cases[i][j].equals("11")) {
                            Paragraph p = new Paragraph("失败");
                            Font f = new Font(this.rtfGsBt5);
                            f.setColor(new Color(255, 0, 0));
                            p.setFont(f);
                            table.addCell(p);
                        } else if (cases[i][j].equals("12")) {
                            Paragraph p = new Paragraph("超时");
                            Font f = new Font(this.rtfGsBt5);
                            f.setColor(new Color(255, 0, 0));
                            p.setFont(f);
                            table.addCell(p);
                        } else if (cases[i][j].equals("5")) {
                            Paragraph p = new Paragraph("启动");
                            Font f = new Font(this.rtfGsBt5);
                            f.setColor(new Color(255, 0, 0));
                            p.setFont(f);
                            table.addCell(p);
                        } else if (cases[i][j].equals("0")) {
                            Paragraph p = new Paragraph("未执行");
                            Font f = new Font(this.rtfGsBt5);
                            f.setColor(new Color(0, 0, 232));
                            p.setFont(f);
                            table.addCell(p);
                        }
                    } else {
                        Cell celltmp = new Cell();
                        Paragraph p = new Paragraph(cases[i][j]);
                        Font f = new Font();
                        f.setColor(new Color(255, 0, 0));
                        p.setFont(f);
                        celltmp.add(p);
                        table.addCell(celltmp);
                    }
                } else if (j == 4) {
                    Cell celltmp = new Cell();
                    Paragraph p = new Paragraph(cases[i][j]);
                    Font f = new Font();
                    f.setColor(new Color(255, 0, 0));
                    p.setFont(f);
                    celltmp.add(p);
                    table.addCell(celltmp);
                } else if (j == 5) {
                    Cell celltmp = new Cell();
                    Paragraph p = new Paragraph(cases[i][j]);
                    Font f = new Font();
                    f.setColor(new Color(255, 0, 0));
                    p.setFont(f);
                    celltmp.add(p);
                    table.addCell(celltmp);
                } else if (module_add < 1) {
                    module_add = 1;
                    module_name = cases[i][j];
                    Paragraph p = new Paragraph(cases[i][j]);
                    p.setFont(this.rtfGsBt5);
                    module_cell = new Cell(p);
                    table.addCell(module_cell);
                } else if (cases[i][j].equals(module_name)) {
                    ++module_add;
                    table.addCell("");
                } else {
                    module_cell.setRowspan(module_add);
                    module_add = 1;
                    module_name = cases[i][j];
                    Paragraph p = new Paragraph(cases[i][j]);
                    p.setFont(this.rtfGsBt5);
                    module_cell = new Cell(p);
                    table.addCell(module_cell);
                }

            }

            if (cases[i].length < col_size) {
                for (int j = 0; j < col_size - cases[i].length; ++j) {
                    table.addCell("");
                }
            }
        }
        if (module_add > 1) {
            module_cell.setRowspan(module_add);
        }

        this.document.add(table);
    }

    public void createScriptTable(String[][] scripts) throws DocumentException, IOException {
        int col_size = 3;

        Table table = new Table(col_size, scripts.length);

        int[] width = { 60, 20, 20 };
        table.setWidths(width);
        table.setWidth(95.0F);
        table.setAlignment(5);

        addTableTitleCell(table, "测试案例名称");
        addTableTitleCell(table, "成功测试案例数");
        addTableTitleCell(table, "失败测试案例数");

        for (int i = 0; i < scripts.length; ++i) {
            for (int j = 0; (j < scripts[i].length) && (j < col_size); ++j) {
                Paragraph p = new Paragraph(scripts[i][j]);
                p.setFont(this.rtfGsBt6);
                Cell c = new Cell(p);
                table.addCell(c);
            }
            if (scripts[i].length < col_size) {
                for (int j = 0; j < col_size - scripts[i].length; ++j) {
                    Paragraph p = new Paragraph("");
                    p.setFont(this.rtfGsBt6);
                    Cell c = new Cell(p);
                    table.addCell(c);
                }

            }

        }

        this.document.add(table);
    }

    public void writeStepTable(String[][] steps, int col_size) throws DocumentException {
        Table table = new Table(col_size, steps.length);
        String[] titles = { "测试步骤ID", "测试步骤名称", "预期结果", "测试信息", "耗时(秒)", "结果" };

        int[] width = { 10, 20, 15, 35, 10, 10 };
        table.setWidths(width);
        table.setWidth(95.0F);
        table.setAlignment(5);

        for (int i = 0; (i < col_size) && (i < titles.length); ++i) {
            addTableTitleCell(table, titles[i]);
        }

        for (int i = 0; i < steps.length; ++i) {
            Font f = new Font(this.rtfGsBt5);

            if (steps[i][5].equals("10"))
                f.setColor(0, 0, 0);
            else {
                f.setColor(255, 0, 0);
            }
            for (int j = 0; (j < steps[i].length) && (j < col_size); ++j) {
                Paragraph p;
                if (steps[i][j] != null) {
                    p = new Paragraph(steps[i][j]);
                } else {
                    p = new Paragraph("无");
                }

                if (j == 5) {
                    if (steps[i][j].equals("10"))
                        p = new Paragraph("成功");
                    else if (steps[i][j].equals("11")) {
                        p = new Paragraph("失败");
                    } else if (steps[i][j].equals("12")) {
                        p = new Paragraph("超时");
                    } else if (steps[i][j].equals("5")) {
                        p = new Paragraph("启动");
                    }
                }

                p.setFont(f);
                table.addCell(p);
            }

        }

        this.document.add(table);
    }

    public void close() {
        this.document.close();
    }

    public void head_title(String titlename) throws DocumentException {
        Paragraph title = new Paragraph(titlename);
        title.setAlignment(1);
        Font title_font = new Font(0, 28.0F, 1);
        title_font.setFamily("宋体");
        title.setFont(title_font);
        this.document.add(title);
    }

    public void h1(String titlename) throws DocumentException {
        Paragraph title = new Paragraph("第" + this.h1_seq + "章 " + titlename);
        title.setFont(this.rtfGsBt1);
        this.document.add(title);
        this.h1_seq += 1;
        this.h2_seq = 1;
        this.h3_seq = 1;
        this.h4_seq = 1;
        this.h5_seq = 1;
    }

    public void h2(String titlename) throws DocumentException {
        Paragraph title = new Paragraph(this.h1_seq - 1 + "." + this.h2_seq + " " + titlename);
        title.setFont(this.rtfGsBt2);
        title.getFont().setStyle(1);

        this.document.add(title);
        this.h2_seq += 1;
        this.h3_seq = 1;
        this.h4_seq = 1;
        this.h5_seq = 1;
    }

    public void h3(String titlename) throws DocumentException {
        Paragraph title = new Paragraph(this.h1_seq - 1 + "." + (this.h2_seq - 1) + " " + this.h3_seq + " " + titlename);
        title.setFont(this.rtfGsBt3);

        this.document.add(title);
        this.h3_seq += 1;
        this.h4_seq = 1;
        this.h5_seq = 1;
    }

    public void h4(String titlename) throws DocumentException {
        Paragraph title = new Paragraph(this.h1_seq - 1 + "." + this.h2_seq + " " + titlename);
        title.setFont(this.rtfGsBt4);
        title.getFont().setStyle(1);

        this.document.add(title);
        this.h2_seq += 1;
    }

    public void h5(String titlename) throws DocumentException {
        Paragraph title = new Paragraph(this.h1_seq - 1 + "." + (this.h2_seq - 1) + " " + (this.h3_seq - 1) + " "
                + (this.h4_seq - 1) + " " + this.h5_seq + " " + titlename);
        title.setFont(this.rtfGsBt6);

        this.document.add(title);
        this.h5_seq += 1;
    }

    public void context_RunInfo(String[] context) throws DocumentException {
        if (context.length >= 0) {
            for (int i = 0; i < context.length; i++) {
                Paragraph page = new Paragraph(context[i]);
                page.setFont(this.rtfGsBt5);
                this.document.add(page);
            }
        }

    }

    public void context(String context) throws DocumentException {
        Paragraph page = new Paragraph(context);
        page.setFont(this.rtfGsBt5);
        this.document.add(page);

    }
}
