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
public class Dent {
    
    /**
     *
     */
    public String indent = "";
    /**
     *
     */
    public int depth = 0;
    /**
     *
     */
    public char[] fill = new char[]{'\t'};
    private char[] dent = new char[0];

    /**
     *
     */
    public Dent() {
    }

    /**
     *
     * @param _fill
     */
    public Dent(char _fill) {
        this(new char[]{_fill}, 0);
    }

    /**
     *
     * @param _fill
     * @param _depth
     */
    public Dent(char _fill, int _depth) {
        this(new char[]{_fill}, _depth);
    }

    /**
     *
     * @param _fill
     */
    public Dent(char[] _fill) {
        this(_fill, 0);
    }

    /**
     *
     * @param _fill
     * @param _depth
     */
    public Dent(char[] _fill, int _depth) {
        fill = _fill;
        indent(_depth);
    }

    /**
     *
     */
    public void inc() {
        indent(depth + 1);
    }

    /**
     *
     */
    public void dec() {
        indent(depth - 1);
    }

    /**
     *
     * @param _depth
     */
    public void indent(int _depth) {
        if (_depth < 0) {
            _depth = 0;
        }
        depth = _depth;

        int l = fill.length;
        dent = new char[depth * l];

        for (int i = 0; i < depth; i++) {
            System.arraycopy(fill, 0, dent, i * l, l);
        }

        indent = new String(dent);
    }

    @Override
    public String toString() {
        return indent;
    }

    /**
     *
     * @return
     */
    public byte[] toBytes() {
        return indent.getBytes();
    }
}
