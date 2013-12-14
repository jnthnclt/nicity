/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author jonathan
 */
public class ExtractPayload extends AExtractor {

    @Override
    public byte[] extract(long _startIndex, int _keySize, int _payloadSize, byte[] _array) {
        byte[] p = new byte[_payloadSize];
        System.arraycopy(_array, (int) _startIndex + 1 + _keySize, p, 0, _payloadSize);
        return p;
    }
    
}
