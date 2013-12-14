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

/**
 *
 * @author Administrator
 */
public class Trail {

    private Object[] trail;
    private int first;
    private int last;
    private int current;
    private int max;
    private int count;

    /**
     *
     */
    public Trail() {
        this(8);
    }

    /**
     *
     * @param _max
     */
    public Trail(int _max) {
        if (_max < 1) {
            max = 8;
        }
        else {
            max = _max;
        }
        trail = new Object[max];
        first = 0;
        last = 0;
        current = 0;
        count = 0;
    }

    /**
     *
     * @return
     */
    public Object[] trail() {
        return trail;
    }

    /**
     *
     * @param _object
     */
    public void mark(Object _object) {
        if (current() == _object) {
            return;
        }
        if (count == 0) {
            trail[current] = _object;
            count++;
            return;
        }

        if (count >= max) { // could grow here, but we choose to recycle instead
            last = (max + (++last)) % max;
            first = (max + (++first)) % max;
        }
        else {
            if (current == last) {
                last = (max + (++last)) % max;
            }
            count++;
        }

        current = (max + (++current)) % max;
        trail[current] = _object;
    }

    /**
     *
     * @return
     */
    public Object current() {
        return trail[current];
    }

    /**
     *
     * @return
     */
    public Object backward() {
        if (count == 0) {
            return null;
        }
        if (count > 1) {
            current = (max + (--current)) % max;
            count--;
        }
        return trail[current];
    }

    /**
     *
     * @return
     */
    public Object forward() {
        if (count == 0) {
            return null;
        }
        if (count < max) {
            current = (max + (++current)) % max;
            count++;
        }
        return trail[current];
    }
}
