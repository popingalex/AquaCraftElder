package org.aqua.parse.json;

import java.io.StringReader;

import org.aqua.parse.MarkupDataObject.DataObject;
import org.aqua.parse.MarkupDataObject.DataObject.ObjectType;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.SequenceNode;

public class JSONUtil{
	
	 public static JSONDataObject getDataObject(String content) {
//	        try {
//	            Element root = new SAXReader().read(new StringReader(content)).getRootElement();
	            return new JSONDataObject(new JSONUtil().compose(new StringReader(content)));//此处应该有json的包导入
//	        } catch (DocumentException e) {
//	            e.printStackTrace();
//	        }
//	        return null;
	    }
	private JSONUtil jsonutil;
	  private JSONUtil compose(StringReader stringReader) {
		// TODO Auto-generated method stub
		return null;
	}
	public static class JSONDataObject implements DataObject {
	        private JSONUtil jsonutil;
            private String key;
	        private JSONDataObject(JSONUtil jsonutil) {
	            this.jsonutil = jsonutil;
	        }

			@Override
			public ObjectType getType() {
				return jsonutil.getDataObject(getKey()).getType();
				 }

			@Override
			public Integer countChilds() {
				
				return jsonutil.getJsonutil().getDataObject(jsonutil);
			}

			@Override
			public DataObject getChild(String key) {
				
				return jsonutil.getDataObject(key).getChild(getKey());
			}

			@Override
			public DataObject getChild(Integer index) {
				return jsonutil.getDataObject(key).getChild(index);
			}

			@Override
			public Object getValue() {
				return jsonutil.equals(getValue());
			}

			@Override
			public String getKey() {
				
				return jsonutil.getClass().getName();
			}
			}

		
			public JSONUtil getJsonutil() {
				return getJsonutil();
			}

			public void setJsonutil(JSONUtil jsonutil) {
				this.jsonutil = jsonutil;
			}
	  
	public int getDataObject(JSONUtil jsonutil) {
		return jsonutil.getDataObject(getJsonutil());
	} 
}
