package org.aqua.parse;

public class MarkupLanguage {
    /**
     * 标记语言种类
     * @author Alex Xu
     *
     */
    public static enum Language {
        JSON, XML, YAML
    }
    /**
     * 获得某类型的根节点
     * @param path
     * @param type
     * @return
     */
    public static DataObject getDataObject(String path, Language type) {
        switch (type) {
        case JSON:
            return null;
        case XML:
            return null;
        case YAML:
            return null;
        }
        return null;
    }
    /**
     * 数据节点
     * @author Alex Xu
     *
     */
    public static interface DataObject {
        public enum ObjectType {
            Map, List, Value
        }
        /**
         * 获得该节点的类型
         * @return
         */
        public ObjectType getType();
        /**
         * 获得字段/子节点的计数
         * @return
         */
        public Integer countChilds();
        /**
         * 根据key获得字段/子节点
         * @param key
         * @return
         */
        public Object getChild(String key);
        /**
         * 根据index获得字段/子节点
         * @param index
         * @return
         */
        public Object getChild(Integer index);
        /**
         * 获得该节点的value
         * @return
         */
        public Object getValue();
    }
}
