package org.aqua.parse.yaml.snake;

import java.io.StringReader;
import java.util.HashMap;

import org.aqua.parse.MarkupDataObject.DataObject;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;

public class SnakeYamlUtil {
    public static void main(String[] args) {
        HashMap<String, Object> m = new HashMap<String, Object>();
        m.put("123", "C:/workspace/billing/err_verifile");
        HashMap<String, Object> m2 = new HashMap<String, Object>();
        m2.put("456", -1);
        m.put("re", m2);
        m2.put("fff", new String[] { "123", "+1" });

        DumperOptions opt = new DumperOptions();
        opt.setDefaultFlowStyle(FlowStyle.BLOCK);
        Yaml y = new Yaml(opt);

        String s = y.dump(m);
        System.out.println(s);
        Node n = y.compose(new StringReader(s));
        System.out.println(n.getEndMark());
        System.out.println(n.getTag());
        System.out.println(n.getNodeId());
        System.out.println(n.getType());
    }

    public static YamlDataObject getDataObject(String content) {
        return null;
    }
    
    public static class YamlDataObject implements DataObject{

        @Override
        public ObjectType getType() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Integer countChilds() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public DataObject getChild(String key) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public DataObject getChild(Integer index) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object getValue() {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
