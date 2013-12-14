/*
 * LockValue.java.java
 *
 * Created on 03-12-2010 09:04:12 PM
 *
 * Copyright 2010 Jonathan Colt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jonathancolt.nicity.core.value;

/*
 * #%L
 * nicity-core
 * %%
 * Copyright (C) 2013 Jonathan Colt
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 *
 * @author Administrator
 * @param <V>
 */
public class LockValue<V> extends Value {

    static private Object cNull = new Object();
    final private Object lock = new Object();

    /**
     *
     * @return
     */
    final public Object lock() {
        return lock;
    }

    /**
     *
     */
    public LockValue() {
        super(cNull);
    }

    /**
     *
     * @param _value
     */
    public LockValue(V _value) {
        super(_value);
    }

    /**
     *
     * @return
     */
    @Override
    public V getValue() {
        synchronized (lock) {
            Object _value = super.getValue();
            if (_value == cNull) {
                try {
                    lock.wait();
                } catch (Exception x) {
                }
                _value = super.getValue();
            }
            return (V)_value;
        }
    }

    /**
     *
     */
    public void reset() {
        synchronized (lock) {
            super.setValue(cNull);
            lock.notifyAll();
        }
    }

    /**
     *
     * @param _value
     */
    @Override
    public void setValue(Object _value) {
        synchronized (lock) {
            Object got = super.getValue();
            if (got == cNull) {
                super.setValue(_value);
                lock.notifyAll();
            } else {
                try {
                    lock.wait();
                } catch (Exception x) {
                }
                setValue(_value);
            }
        }
    }
}


