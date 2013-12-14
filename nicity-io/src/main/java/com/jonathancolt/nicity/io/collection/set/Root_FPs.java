package com.jonathancolt.nicity.io.collection.set;

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

import com.jonathancolt.nicity.core.io.UIO;
import com.jonathancolt.nicity.io.collection.StackFiler;

public class Root_FPs {
    final static private int fpSize = 8;//truely a constant
    StackFiler rootFP;
    public Root_FPs(String _name,int _batchSize) throws Exception {
        rootFP = StackFiler.factory(_name, fpSize, _batchSize, 0, UIO.longBytes(-1));
    }
    public RootFP getRootFP(int _i) {
        return new RootFP(this, _i);
    }
    synchronized public long get(int _i) throws Exception {
        long fp = UIO.bytesLong(rootFP.get(_i));
        System.out.println("Get FP=" + fp);
        return fp;
    }
    synchronized public void set(long _fp, int _i) throws Exception {
        System.out.println("Set FP=" + _fp);
        rootFP.set(_i, UIO.longBytes(_fp));
    }
}
