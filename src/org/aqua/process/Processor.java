package org.aqua.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.aqua.AquaConstants;

public class Processor {
    public Processor(Object... sources) {
        this.commandList = new ArrayList<String>();
        this.processing = false;
        this.charset = Charset.forName("UTF-8");
//        this.charset = Charset.forName("GBK");
        this.sources = sources;
    }

    public Processor attachCommands(String... commands) {
        this.commandList.addAll(Arrays.asList(commands));
        return this;
    }

    private InputStream  stdInputStream;
    private InputStream  errInputStream;
    private OutputStream outputStream;
    private List<String> commandList;
    private boolean      processing;
    private Charset      charset;
    private Object[]     sources;

    public void handleStandardOutput(String content, Object[] sources) {
        System.out.println("STDINPUT :" + content);
    }

    public void handleErrorOutput(String content, Object[] sources) {
        System.out.println("ERRINPUT :" + content);
    }

    public String process() {
        String[] commands = new String[commandList.size()];
        commandList.toArray(commands);
        System.out.println("cmds : " + Arrays.toString(commands));
        try {
            Process process = Runtime.getRuntime().exec(commands);
            outputStream = process.getOutputStream();
            errInputStream = process.getErrorStream();
            stdInputStream = process.getInputStream();

            processing = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BufferedReader stdInputReader = new BufferedReader(new InputStreamReader(stdInputStream, charset));
                    BufferedReader errInputReader = new BufferedReader(new InputStreamReader(errInputStream, charset));
                    while (processing) {
                        try {
                            while (stdInputReader.ready()) {
                                StringBuffer standardBuffer = new StringBuffer();
                                standardBuffer.append(stdInputReader.readLine());
                                handleStandardOutput(standardBuffer.toString(), sources);
                            }
                            while (errInputReader.ready()) {
                                StringBuffer errorBuffer = new StringBuffer();
                                errorBuffer.append(errInputReader.readLine());
                                handleErrorOutput(errorBuffer.toString(), sources);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        errInputReader.close();
                        stdInputReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Scanner scanner = new Scanner(System.in);
                    OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                    while (processing) {
                        String line = scanner.nextLine();
                        System.out.println(line);
                        try {
                            writer.write(line);
                            writer.write(AquaConstants.LINE_SEPARATOR);
                            writer.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    scanner.close();
                }
            });
            // }).start();
            // TODO fatal bug got. by Alex 20141218
            int result = process.waitFor();
            System.out.println("process result : " + result);
            processing = false;
            return Integer.toString(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
