package com.jonathancolt.nicity.io;

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

import com.jonathancolt.nicity.core.io.IFiler;
import com.jonathancolt.nicity.core.lang.ASetObject;
import com.jonathancolt.nicity.core.lang.OrderedKeys;
import com.jonathancolt.nicity.core.memory.SoftIndex;


/*
This class segments a single Filer into filerResource filers where
each filerResource filer restates fp = 0. It only allows one filerResource filer
at a time to be in control. It is the responsibility of the 
programmer to remove the filerResource filers as the become stale.
 */
public class SubFilers extends ASetObject {
    final static SoftIndex<SubFiler,OrderedKeys,Object> subFilers = new SoftIndex<SubFiler,OrderedKeys,Object>("Sub Filers Index");
    Object name;
    IFiler filer;
    public SubFilers(Object _name,IFiler _filer) {
        name = _name;
        filer = _filer;
    }
    public Object hashObject() {
        return filer;
    }
    @Override
    public String toString() {
        return "SubFilers:"+name;
    }
    public SubFiler get(long _startOfFP, long _endOfFP, long _count) {
        SubFiler subFiler = null;
        synchronized (subFilers) {
            OrderedKeys key = USubFiler.key(this,_startOfFP, _endOfFP);
            subFiler = subFilers.get(key);
            if (subFiler == null) {
                subFiler = new SubFiler(this, _startOfFP, _endOfFP, _count);
                subFilers.set(subFiler, key);
            }
        }
        return subFiler;

    }
}

