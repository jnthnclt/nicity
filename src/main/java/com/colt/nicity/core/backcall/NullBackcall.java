/*
 * NullBackcall.java.java
 *
 * Created on 03-12-2010 10:35:38 PM
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
package com.colt.nicity.core.backcall;

import com.colt.nicity.core.collection.IBackcall;
import com.colt.nicity.core.lang.ICallback;
import com.colt.nicity.core.lang.IOut;

/**
 *
 * @param <V> 
 * @author Administrator
 */
public class NullBackcall<V> implements IBackcall<V> {

    /**
     *
     */
    public static final IBackcall cNull = new NullBackcall();

    @Override
    public void backcall(IOut _, ICallback<V, V> _callback) {
    }
}
