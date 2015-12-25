package org.aqua.struct.parameter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DatasetTemplet {
    private static Map<Class<?>, Map<String, Field>> templetMap = new HashMap<Class<?>, Map<String, Field>>();

    public static Dataset generateDataset(Class<?> cls) {
        if (templetMap.containsKey(cls)) {
            Map<String, Field> datamap = new HashMap<String, Field>();
            for (Entry<String, Field> entry : templetMap.get(cls).entrySet()) {
                datamap.put(entry.getKey(), entry.getValue().copy());
            }
            return new Dataset(datamap);
        } else {
            return new Dataset(new HashMap<String, Field>());
        }
    }

    private Map<String, Field> datamap;

    public DatasetTemplet() {
        datamap = new HashMap<String, Field>();
    }

    public void registerField(String name, Type type) {
        datamap.put(name, new Field(type));
    }

    public void zip(Class<?> cls) {
        templetMap.put(cls, datamap);
    }
    
    public enum Type {
        Int, Obj, Str;
        public Class<?> getTypeClass() {
            switch (this) {
            case Int:
                return Integer.class;
            case Obj:
                return Object.class;
            case Str:
                return String.class;
            }
            return null;
        }
    }
}
