package org.aqua.io.charset;

import org.aqua.net.RequestWorker;
import org.aqua.parse.json.jackson.JacksonJsonUtil;

import test.Tester;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class UTFinGBKTest extends Tester {
    @Override
    public void test(String[] args) {
        // String content = FileUtil.readFile("C:/test.txt");
        // System.out.println(content);
//        RequestWorker worker = new RequestWorker("http://20.26.17.182:9997/sendTask");
        RequestWorker worker = new RequestWorker("http://20.26.17.182:9994/sendScene");
        worker.setQueryParameter("taskId", "74");
        worker.setQueryParameter("sceneId", "458");
        String content = worker.Get().getResponseContent();

        ObjectNode objectNode = JacksonJsonUtil.getJava(content, ObjectNode.class);
        System.out.println(JacksonJsonUtil.getJson(objectNode));
//        FileUtil.makeFile("C:/" + objectNode.get("name").textValue() + ".txt", JacksonJsonUtil.getJson(objectNode));
    }
}
