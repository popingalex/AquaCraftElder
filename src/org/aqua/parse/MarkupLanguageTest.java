package org.aqua.parse;

import org.aqua.io.file.FileUtil;
import org.aqua.parse.MarkupDataObject.DataObject;
import org.aqua.parse.MarkupDataObject.Language;
import org.aqua.parse.xml.dom4j.Dom4jUtil;
import org.aqua.parse.xml.dom4j.Dom4jUtil.XMLDataObject;

import test.Tester;

public class MarkupLanguageTest extends Tester {

    @Override
    public void test(String[] args) {
        // TODO Auto-generated method stub
        // DataObject o = MarkupDataObject.getDataObject(null, Language.YAML);

        DataObject dataO = Dom4jUtil.getDataObject(FileUtil.readFile("pom.xml"));
        System.out.println(dataO.getChild("groupId").getValue());
        System.out.println(dataO.getChild("dependencies").getChild(2).getChild("groupId").getValue());
    }

}
