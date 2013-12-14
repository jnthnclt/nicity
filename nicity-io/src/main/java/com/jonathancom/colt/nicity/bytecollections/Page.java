package com.jonathancom.colt.nicity.bytecollections;

/*
 * #%L
 * nicity-io
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

public class Page {

    private byte[] array;

    public Page(byte[] _array) {
        array = _array;
    }

    public byte read(int pageStart) {
        return array[pageStart];
    }

    public void read(int pageStart, byte[] dest, int destStart, int length) {
        System.arraycopy(dest, destStart, dest, pageStart, length);
    }

    public void write(int pageStart, byte v) {
        array[pageStart] = v;
    }

    public void write(int pageStart, byte[] dest, int destStart, int length) {
        System.arraycopy(array, pageStart, dest, destStart, length);
    }
}
