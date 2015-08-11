package org.aqua.parse;

import org.aqua.io.file.FileUtil;
import org.aqua.parse.json.JSONUtil;
import org.aqua.parse.xml.dom4j.Dom4jUtil;
import org.aqua.parse.yaml.snake.SnakeYamlUtil;

public class MarkupDataObject {
    /**
     * 鏍囪璇█绉嶇被
     * @author Alex Xu
     *
     */
    public static enum Language {
        JSON, XML, YAML
    }
    /**
     * 鑾峰緱鏌愮被鍨嬬殑鏍硅妭鐐�
     * @param path
     * @param type
     * @return
     */
    public static DataObject getDataObject(String path, Language type) {
        switch (type) {
        case JSON:
            return JSONUtil.getDataObject(FileUtil.readFile(path));
        case XML:
            return Dom4jUtil.getDataObject(FileUtil.readFile(FileUtil.readReader(path)));
        case YAML:
            return SnakeYamlUtil.getDataObject(FileUtil.readFile(FileUtil.readReader(path)));
        }
        return null;
    }
    /**
     * 鏁版嵁鑺傜偣
     * @author Alex Xu
     *
     */
    public static interface DataObject {
        public enum ObjectType {
            Map, List, Value
        }
        /**
         * 鑾峰緱璇ヨ妭鐐圭殑绫诲瀷
         * @return
         */
        public ObjectType getType();
        /**
         * 鑾峰緱瀛楁/瀛愯妭鐐圭殑璁℃暟
         * @return
         */
        public Integer countChilds();
        /**
         * 鏍规嵁key鑾峰緱瀛楁/瀛愯妭鐐�
         * @param key
         * @return
         */
        public DataObject getChild(String key);
        /**
         * 鏍规嵁index鑾峰緱瀛楁/瀛愯妭鐐�
         * @param index
         * @return
         */
        public DataObject getChild(Integer index);
        /**
         * 鑾峰緱璇ヨ妭鐐圭殑value
         * @return
         */
        public Object getValue();
        public String getKey();
    }
}
