package org.aqua.resource;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

public abstract class KeyedPool<K, O> {
    private GenericKeyedObjectPool<K, O> pool;
    public KeyedPool() {
        BaseKeyedPooledObjectFactory<K, O> factory = new BaseKeyedPooledObjectFactory<K, O>() {
            @Override
            public O create(K key) throws Exception {
                return KeyedPool.this.create(key);
            }
            @Override
            public PooledObject<O> wrap(O object) {
                return new DefaultPooledObject<O>(object);
            }
        };
        GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
        config.setMaxTotalPerKey(256 * 256 * 256);
        pool = new GenericKeyedObjectPool<K, O>(factory, config);
    }
    protected abstract O create(K key);
    protected abstract K keyof(O object);
    public final O borrow(K key) {
        try {
            return pool.borrowObject(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public final void store(O object) {
        pool.returnObject(keyof(object), object);
    }
}
