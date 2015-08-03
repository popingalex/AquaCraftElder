package org.aqua.parse.yaml.snake;

import java.io.StringReader;

import org.aqua.parse.MarkupDataObject.DataObject;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;

public class SnakeYamlUtil {
    public static YamlDataObject getDataObject(String content) {
        return new YamlDataObject(new Yaml().compose(new StringReader(content)));
    }
   
    public static class YamlDataObject implements DataObject {
        private Node node;

        private YamlDataObject(Node node) {
            this.node = node;
        }

        @Override
        public ObjectType getType() {
            switch (node.getNodeId()) {
            case mapping:
                return ObjectType.Map;
            case sequence:
                return ObjectType.List;
            case scalar:
                return ObjectType.Value;
            default:
                return null;
            }
        }

        @Override
        public Integer countChilds() {
            switch (node.getNodeId()) {
            case mapping:
                return ((MappingNode) node).getValue().size();
            case sequence:
                return ((SequenceNode) node).getValue().size();
            default:
                return 0;
            }
        }

        @Override
        public DataObject getChild(String key) {
            for (NodeTuple t : ((MappingNode) node).getValue()) {
                if (((ScalarNode) t.getKeyNode()).getValue().equals(key)) {
                    return new YamlDataObject(t.getValueNode());
                }
            }
            return null;
        }

        @Override
        public DataObject getChild(Integer index) {
            switch (node.getNodeId()) {
            case mapping:
                if (((MappingNode) node).getValue().size() > index) {
                    return new YamlDataObject(((MappingNode) node).getValue().get(index).getValueNode());
                }
            case sequence:
                if (((SequenceNode) node).getValue().size() > index) {
                    return new YamlDataObject(((SequenceNode) node).getValue().get(index));
                }
            default:
                return null;
            }
        }

        @Override
        public Object getValue() {
            return getKey();
        }

        @Override
        public String getKey() {
            return ((ScalarNode)node).getValue();
        }
    }
}
