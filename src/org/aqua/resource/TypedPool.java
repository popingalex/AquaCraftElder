package org.aqua.resource;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

public abstract class TypedPool<Type> {
    private GenericKeyedObjectPool<Class<? extends Type>, Type>   pool;
    private KeyedPooledObjectFactory<Class<? extends Type>, Type> factory;

    public TypedPool() {
        factory = new BaseKeyedPooledObjectFactory<Class<? extends Type>, Type>() {
            /**
             * 根据类型的Class对象生成实例
             */
            @Override
            public Type create(Class<? extends Type> type) throws Exception {
                return TypedPool.this.create(type);
            }
            /**
             * 根据
             */
            @Override
            public PooledObject<Type> wrap(Type value) {
                return new DefaultPooledObject<Type>(value);
            }
        };
        GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
        config.setMaxTotalPerKey(0xFFFFFF);
        pool = new GenericKeyedObjectPool<Class<? extends Type>, Type>(factory, config);
    }

    protected Type create(Class<? extends Type> type) throws Exception {
        return type.newInstance();
    }
    /**
     * 根据实例获得其Class
     * @param object
     * @return
     */
    protected abstract Class<? extends Type> typeof(Type object);

    public <Subtype extends Type> Subtype fetch(Class<Subtype> type) {
        try {
            return type.cast(pool.borrowObject(type));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void store(Type object) {
        pool.returnObject(typeof(object), object);
    }
}
