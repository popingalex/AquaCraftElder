package org.aqua.parse.itext;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;

import com.lowagie.text.Anchor;
import com.lowagie.text.BadElementException;
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

public class RTFUtil {

    private Document          document;
    private RtfWriter2        writer;
    private RtfParagraphStyle rtfGsBt4;
    private RtfParagraphStyle rtfGsBt6;
    private RtfParagraphStyle rtfGsBt5;
    private Font              titleFont;
    private RtfParagraphStyle rtfGsBt1;
    private RtfParagraphStyle rtfGsBt2;
    private RtfParagraphStyle rtfGsBt3;

    public RTFUtil(String path) {
        document = new Document();
        try {
            writer = RtfWriter2.getInstance(document, new FileOutputStream(path));
            {
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
            document.open();
            {// table
                int col = 4;
                int row = 6;
                Table table = new Table(col, row);
                table.setWidths(new int[] { 20, 20, 30, 30 });
                table.setWidth(90);
                table.setAlignment(Table.ALIGN_MIDDLE);
                for (int i = 0; i < col; i++) {
                    Cell cell = new Cell();
                    cell.setBackgroundColor(Color.lightGray);
                    Paragraph para = new Paragraph("col" + i);
                    Font font = new Font(0, 10, 1);
                    font.setFamily("宋体");
                    font.setColor(Color.red);
                    para.setFont(font);
                    cell.add(para);
                    table.addCell(cell);
                }
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        switch (j) {
                        case 0:
                            Paragraph para = new Paragraph("para" + i + ":" + j);
                            para.setFont(rtfGsBt5);
                            table.addCell(para);
                            break;
                        case 1:
                            Chunk chunk = new Chunk("chunk" + i + ":" + j);
                            Anchor anchor = new Anchor(chunk);
                            anchor.setName("anchor" + i + ":" + j);
                            anchor.setReference("#" + i + ":" + j);
                            anchor.setFont(rtfGsBt5);
                            table.addCell(anchor);
                            break;
                        case 2:
                            table.addCell("");
                            break;
                        default:
                            Cell cell = new Cell("cell" + i + ":" + j);
                            cell.setRowspan(2);
                            table.addCell(cell);
                            break;
                        }
                    }
                }
                document.add(table);
            }
            {
                Font font = new Font(0, 28.0F, 1);
                font.setFamily("宋体");
                Paragraph title = new Paragraph("Head Title");
                title.setAlignment(1);
                title.setFont(font);
                document.add(title);
            }
            {
                Paragraph title = new Paragraph("title");
                title.setFont(rtfGsBt1);
                document.add(title);
            }
            {
                Paragraph title = new Paragraph("title");
                title.setFont(rtfGsBt3);
                document.add(title);
            }
            {
                Paragraph para = new Paragraph("content 1");
                para.setFont(rtfGsBt5);
                document.add(para);
            }
            {
                Paragraph para = new Paragraph("内容2");
                para.setFont(rtfGsBt5);
                document.add(para);
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }
    
    public void title(String content) {
        
    }
    
    public void head(int level, String content) {
        
    }
    
    public void print(String...content) {
        
    }
}
