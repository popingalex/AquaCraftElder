package org.aqua.graph.ocr;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.aqua.io.file.FileUtil;

import test.Tester;

public class OCRTest extends Tester {
    JFrame        ocrFrame;
    Rectangle     srcBound;
    BufferedImage srcImg;
    MouseListener mouseListener;
    Point         p1, p2;
    @Override
    public void test(String[] args) {
        ocrFrame = new JFrame() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.drawImage(srcImg, srcBound.x, srcBound.y, srcBound.width, srcBound.height, null);

                if (null != p1) {
                    g.drawOval(p1.x - 2, p1.y - 2, 4, 4);
                }
                if (null != p2) {
                    g.drawOval(p2.x - 2, p2.y - 2, 4, 4);
                }
                if (null != p1 && null != p2) {
                    g.drawRect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
                }

            }
        };
        mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    p1 = e.getPoint();
                    break;
                case MouseEvent.BUTTON3:
                    p2 = e.getPoint();
                    BufferedImage rst = srcImg.getSubimage(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);

                    try {
                        ImageIO.write(rst, "jpg", new FileOutputStream("temp/img_ocr.jpg"));
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    System.out.println(parse("temp/img_ocr.jpg"));
                    break;
                }
                ocrFrame.repaint();
            }
        };
        ocrFrame.addMouseListener(mouseListener);
        File file = new File("source/img_ocr_2.jpg");
        try {
            srcImg = ImageIO.read(file);

            int width = srcImg.getWidth();
            int height = srcImg.getHeight();

            int[] pixelArray = new int[height * width];
            srcImg.getRGB(0, 0, width, height, pixelArray, 0, width);
            int[][] srcHistogram = new int[3][256];
            for (int color : pixelArray) {
                srcHistogram[0][color >> 16 & 0xff]++;
                srcHistogram[1][color >> 8 & 0xff]++;
                srcHistogram[2][color >> 0 & 0xff]++;
            }

            int[][] dstHistogram = getHE(srcHistogram, width * height);

            int cr, cg, cb;

            for (int i = 0; i < pixelArray.length; i++) {
                cr = pixelArray[i] >> 16 & 0xff;
                cg = pixelArray[i] >> 8 & 0xff;
                cb = pixelArray[i] >> 0 & 0xff;

                cr = dstHistogram[0][cr];
                cg = dstHistogram[0][cg];
                cb = dstHistogram[0][cb];

                pixelArray[i] = 0xff << 24 | cr << 16 | cg << 8 | cb;
            }
            srcImg.setRGB(0, 0, width, height, pixelArray, 0, width);

            srcBound = new Rectangle(8, 8, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ocrFrame.setVisible(true);
        ocrFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Insets i = ocrFrame.getInsets();
        ocrFrame.setSize(srcImg.getWidth() + i.left + i.right, srcImg.getHeight() + i.top + i.bottom);

    }

    public int[][] getHE(int[][] srcHistogram, int pixelCount) {
        int[][] dstHistogram = new int[3][256];
        for (int l = 0; l < 256; l++) {
            for (int d = 0; d < 3; d++) {
                double sum = 0;
                for (int i = 0; i < l; i++) {
                    sum += (double) srcHistogram[0][i] / pixelCount;
                }
                dstHistogram[d][l] = (int) (sum * 255);
            }
        }
        return dstHistogram;
    }

    public String parse(String path) {
        // -l chi_sim
        String exe = "C:\\Program Files (x86)\\Tesseract-OCR\\tesseract";
        ProcessBuilder builder = new ProcessBuilder(exe, path, "result/out_ocr");
//        ProcessBuilder builder = new ProcessBuilder(exe, path, "result/out_ocr", "-l", "chi_sim");
        try {
            builder.start().waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return FileUtil.readFile(FileUtil.readReader("result/out_ocr.txt"));
    }
}
