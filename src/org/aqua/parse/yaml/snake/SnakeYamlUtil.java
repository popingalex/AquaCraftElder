package org.aqua.parse.yaml.snake;

import java.io.StringReader;
import java.util.HashMap;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.nodes.Node;

public class SnakeYamlUtil {
    public static void main(String[] args) {
        HashMap<String, Object> m = new HashMap<String, Object>();
        m.put("123", "123");
        HashMap<String, Object> m2 = new HashMap<String, Object>();
        m2.put("456", "23123");
        m.put("re", m2);
        m2.put("fff", new String[] { "123", "333" });

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

    static class Test {
        String ob = "erer";
    }
}
