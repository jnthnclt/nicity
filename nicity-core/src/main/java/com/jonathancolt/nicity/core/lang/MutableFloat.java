/*
 * MutableFloat.java.java
 *
 * Created on 12-29-2009 03:57:00 PM
 *
 * Copyright 2009 Jonathan Colt
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
package com.jonathancolt.nicity.core.lang;

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
 */
public class MutableFloat extends ASetObject implements Comparable {
    private float unit = 0;
    /**
     *
     */
    public MutableFloat() {
    }
    /**
     *
     * @param _unit
     */
    public MutableFloat(float _unit) {
        unit = _unit;
    }
    /**
     *
     * @return
     */
    @Override
    public Object hashObject() {
        return unit;
    }
    /**
     *
     * @return
     */
    public float floatValue() {
        return unit;
    }
    /**
     *
     * @return
     */
    public float get() {
        return unit;
    }
    /**
     *
     * @param _val
     */
    public void set(float _val) {
        unit = _val;
    }
    /**
     *
     * @param _val
     */
    public void min(float _val) {
        if (_val < unit) {
            unit = _val;
        }
    }
    /**
     *
     * @param _val
     */
    public void max(float _val) {
        if (_val > unit) {
            unit = _val;
        }
    }
    /**
     *
     * @param _amount
     */
    public void inc(float _amount) {
        unit += _amount;
    }
    /**
     *
     */
    public void inc() {
        unit++;
    }
    /**
     *
     */
    public void dec() {
        unit--;
    }
    /**
     *
     * @param _amount
     */
    public void multiply(float _amount) {
        unit *= _amount;
    }
    /**
     *
     * @param _amount
     */
    public void divide(float _amount) {
        unit /= _amount;
    }
    /**
     *
     */
    public void reset() {
        unit = 0;
    }
    @Override
    public String toString() {
        Object o = hashObject();
        if (o instanceof Integer) {
            return o.toString();
        }
        return o.toString() + "=" + unit;
    }
    @Override
    public int compareTo(Object o) {
        float a = this.unit;
        float b = ((MutableFloat) o).unit;
        return (a < b ? -1 : (a == b ? 0 : 1));
    }
}
