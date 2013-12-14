/*
 * AObservable.java.java
 *
 * Created on 03-12-2010 11:26:10 PM
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
package com.jonathancolt.nicity.core.observer;

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

import com.jonathancolt.nicity.core.lang.ASetObject;

/**
 *
 * @author Administrator
 */
public abstract class AObservable extends ASetObject implements IObservable {

    /**
     *
     * @return
     */
    @Override
    public Object hashObject() {
        return this;
    }
    private Observers observers = null;

    /**
     *
     * @return
     */
    @Override
    public boolean isBeingObserved() {
        Observers _local = observers;
        if (_local == null) {
            return false;
        }
        return _local.numberOfObservers() > 0;
    }

    /**
     *
     * @param _observer
     * @return
     */
    @Override
    public IObserver bind(IObserver _observer) {
        Observers _local = null;
        synchronized (this) {
            _local = observers;
            if (_local == null) {
                observers = new Observers(this) {

                    @Override
                    public void dettach() {
                        synchronized (AObservable.this) {
                            observers = null;
                        }
                    }
                };
                _local = observers;
            }
        }
        return _local.bind(_observer);
    }

    /**
     *
     * @param _observer
     */
    @Override
    public void release(IObserver _observer) {
        Observers _local = null;
        synchronized (this) {
            _local = observers;
            if (_local == null) {
                return;
            }
        }
        _local.release(_observer);
    }

    /**
     *
     * @param _key
     * @param _value
     */
    final public void change(Object _key, Object _value) {
        Observers _local = null;
        synchronized (this) {
            _local = observers;
        }
        if (_local != null) {
            _local.broadcast(new Change(_key, _value, this));
        }
    }
}
