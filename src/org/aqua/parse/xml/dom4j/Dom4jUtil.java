package org.aqua.parse.xml.dom4j;

import java.io.StringReader;

import org.aqua.parse.MarkupDataObject.DataObject;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Dom4jUtil {
    public static XMLDataObject getDataObject(String content) {
        try {
            Element root = new SAXReader().read(new StringReader(content)).getRootElement();
            return new XMLDataObject(root);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static class XMLDataObject implements DataObject{
        private Element element;

        private XMLDataObject(Element element) {
            this.element = element;
        }

        @Override
        public ObjectType getType() {
            System.out.println(element.asXML());
            return null;
        }

        @Override
        public Integer countChilds() {
            return element.elements().size();
        }

        @Override
        public DataObject getChild(String key) {
            return new XMLDataObject((Element)element.elements(key).get(0));
        }

        @Override
        public DataObject getChild(Integer index) {
            return new XMLDataObject((Element)element.elements().get(index));
        }

        @Override
        public Object getValue() {
            return element.getText();
        }
    }
}
