package com.jonathancolt.nicity.core.util;

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

import com.jonathancolt.nicity.core.lang.UArray;
import com.jonathancolt.nicity.core.value.Value;
import java.util.Arrays;

/**
 *
 * @author Administrator
 */
public class Odometer {

    /**
     *
     */
    public int roleOver = 0;
    private int index = 0;
    private Object[] values;
    private Odometer next;
    private Value done;

    /**
     *
     * @param _values
     */
    public Odometer(Object[] _values) {
        values = Arrays.copyOf(_values, _values.length);
    }

    /**
     *
     * @param _index
     */
    public void setIndex(int _index) {
        if (_index < 0) {
            _index = 0;
        }
        if (_index >= values.length) {
            _index = values.length - 1;
        }
        index = _index;
    }

    /**
     *
     * @param _odometer
     * @return
     */
    public Odometer setNext(Odometer _odometer) {
        next = _odometer;
        return next;
    }

    /**
     *
     * @return
     */
    public Odometer getNext() {
        return next;
    }

    /**
     *
     * @param _done
     */
    public void setDone(Value _done) {
        done = _done;
    }

    /**
     *
     * @return
     */
    public boolean inc() {
        index++;
        if (index >= values.length) {
            roleOver++;
            index = 0;
            if (next == null) {
                if (done != null) {
                    done.setValue(Long.valueOf(System.currentTimeMillis()));
                }
                return false;
            }
            return next.inc();
        }
        return true;
    }

    /**
     *
     * @return
     */
    public Object[] toArray() {
        if (next == null) {
            return new Object[]{values[index]};
        }
        else {
            return UArray.push(next.toArray(), values[index]);
        }
    }
}
