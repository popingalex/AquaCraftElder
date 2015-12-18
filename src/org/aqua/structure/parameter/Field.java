package org.aqua.structure.parameter;

import org.aqua.structure.parameter.DatasetTemplet.Type;

class Field {
    private final Type type;
    private Object     value;
    Field(Type type) {
        this.type = type;
    }
    void reset() {
        switch (type) {
        case Int:
            value = 0;
            break;
        case Obj:
            value = null;
            break;
        case Str:
            value = new String();
            break;
        }
    }
    Object getValue(Type type) {
        if (this.type == type) {
            return value;
        } else {
            throw new ClassCastException("Not a(an) " + type.toString());
        }
    }
    void setValue(Object value) {
        if (type.getTypeClass().isInstance(value)) {
            this.value = value;
        } else {
            throw new ClassCastException("Not a(an) " + type.toString());
        }
    }

    Field copy() {
        return new Field(type);
    }
}