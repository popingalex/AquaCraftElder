package org.aqua.struct.parameter;

import java.util.Map;

import org.aqua.struct.parameter.DatasetTemplet.Type;

public class Dataset implements IDataset {
    private Map<String, Field> datamap;

    Dataset(Map<String, Field> parameterMap) {
        this.datamap = parameterMap;
    }
    @Override
    public void setValue(String name, Object value) {
        if (!datamap.containsKey(name)) {
            throw new NullPointerException("No such field " + name);
        } else {
            datamap.get(name).setValue(value);
        }
    }
    public Object getValue(String name, Type type) {
        if (!datamap.containsKey(name)) {
            throw new NullPointerException("No such field " + name);
        } else {
            return datamap.get(name).getValue(type);
        }
    }
    @Override
    public Object getObject(String name) {
        return getValue(name, Type.Obj);
    }
    @Override
    public String getString(String name) {
        return (String) getValue(name, Type.Str);
    }
    @Override
    public Integer getInteger(String name) {
        return (Integer) getValue(name, Type.Int);
    }
}
