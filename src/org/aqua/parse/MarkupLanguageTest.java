package org.aqua.parse;

import org.aqua.io.file.FileUtil;
import org.aqua.parse.MarkupDataObject.DataObject;
import org.aqua.parse.json.JSONUtil;
import org.aqua.parse.xml.dom4j.Dom4jUtil;
import org.aqua.parse.yaml.snake.SnakeYamlUtil;

import test.Tester;

public class MarkupLanguageTest extends Tester {

    @Override
    public void test(String[] args) {
        DataObject dataO;
        // TODO Auto-generated method stub
//         DataObject o = MarkupDataObject.getDataObject(null, Language.YAML);
        System.out.println("xml:");
        dataO = Dom4jUtil.getDataObject(FileUtil.readFile(FileUtil.readReader("pom.xml")));
        
        System.out.println(dataO.countChilds());
        System.out.println(dataO.getChild("groupId").getValue());
        System.out.println(dataO.getChild("dependencies").getChild(2).getChild("groupId").getValue());
        
        System.out.println("yml:");

        dataO = SnakeYamlUtil.getDataObject(FileUtil.readFile(FileUtil.readReader("C:/workspace/bill/source/testcase.yml")));
        dataO = SnakeYamlUtil.getDataObject(FileUtil.readFile(FileUtil.readReader("source/messages.yml")));
//        System.out.println(dataO.countChilds());
//        System.out.println(dataO.getChild("biz").getValue());
//        System.out.println(dataO.getChild("rec").getChild("file").getChild("file_prov").getValue());
        dataO = SnakeYamlUtil.getDataObject(FileUtil.readFile(FileUtil.readReader("E:/testcase.yml")));
        System.out.println(dataO.countChilds());
        System.out.println(dataO.getChild("biz").getValue());
        System.out.println(dataO.getChild("rec").getChild("file").getChild("file_prov").getValue());
        
        
        System.out.println("json:");
        dataO = JSONUtil.getDataObject("Ҫ������json���");
        System.out.println(dataO.countChilds());
        System.out.println(dataO.getChild("biz").getValue());
        System.out.println(dataO.getChild("rec").getChild("file").getChild("file_prov").getValue());
    }

}
