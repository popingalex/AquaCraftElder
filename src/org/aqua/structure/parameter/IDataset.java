package org.aqua.structure.parameter;


public interface IDataset {
    public void setValue(String name, Object value);
    public Object getObject(String name);
    public String getString(String name);
    public Integer getInteger(String name);
}
