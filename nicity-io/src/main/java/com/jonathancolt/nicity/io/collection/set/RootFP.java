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

public class RootFP {
    private Root_FPs rootFPs;
    private int index;
    public RootFP(Root_FPs _rootFPs,int _index) {
        rootFPs = _rootFPs;
        index = _index;
    }
    public long getLong() throws Exception { return rootFPs.get(index); }
    public void setLong(long _fp) throws Exception  { rootFPs.set(_fp,index); }
}
