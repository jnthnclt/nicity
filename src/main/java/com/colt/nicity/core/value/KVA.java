/*
 * KVA.java.java
 *
 * Created on 03-12-2010 06:29:57 PM
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

package com.colt.nicity.core.value;

import com.colt.nicity.core.collection.CArray;
import com.colt.nicity.core.process.IAsyncResponse;

public class KVA extends KV {
    public KVA(Object _key) {
        super(_key,new CArray(KV.class));
    }
    public void add(int _index,KV... _kvs) {

    }
    public void remove(int _index,KV... _kvs) {

    }
    public void take(int _index,KV _kv,IAsyncResponse<KV> _took) {

    }
    public void copy(int _index,KV _kv,IAsyncResponse<KV> _copied) {
        
    }
}