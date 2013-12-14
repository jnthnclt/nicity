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

public class IOSetLongID implements ISIO_ID {
    
    final static private int blockSize = 8 + 8;//truely a constant
    static final int cBatchIDIndex = 0;
    static final int cCountIDIndex = 1;
    
    private String name;
    private long batchSize = 2;
    private StackFiler stack;
    final private Object nextIDLock = new Object();
    
    public IOSetLongID(
            String _name,
            int _batchSize) throws Exception {
        name = _name;
        batchSize = _batchSize;
        stack = StackFiler.factory(name, blockSize, 1, 0,null);
    }
    
    public int size() {
        return 8;
    }
    
    public byte[] id() {
        return UIO.longBytes(nextID());
    }
    
    public long nextID() {
        long _nextID = 0;
        synchronized (nextIDLock) {
            try {
                long[] batch = get();
                long nextID = _nextID(batch);
                if (nextID == 0) {
                    batch = getNextBatch();
                    nextID = _nextID(batch);
                }
                _nextID = nextID;
            } catch (Exception x) {
                x.printStackTrace();
                _nextID = 0;
            }
        }
        return _nextID;
    }
    
    public void nextBatch() {
        synchronized (nextIDLock) {
            try {
                set(getNextBatch());
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
    }
    
    private long[] get() throws Exception {
        return UIO.unpackLongs(stack.get(0));
    }
    
    private void set(long[] _batch) throws Exception {
        stack.set(0, UIO.packLongs(_batch));
    }
    
    private long _nextID(long[] _batch) {
        if (_batch == null) {
            return 0;
        }
        try {
            if (_batch[cCountIDIndex] >= batchSize) {
                return 0;
            }
            long id = _batch[cCountIDIndex] + (_batch[cBatchIDIndex] * batchSize);
            _batch[cCountIDIndex]++;
            set(_batch);
            return id;
        } catch (Exception x) {
            x.printStackTrace();
            return 0;
        }
    }
    
    private long[] getNextBatch() throws Exception {
        long[] batch = get();
        if (batch == null) {
            batch = new long[]{1, 1};
        }
        batch[cBatchIDIndex]++;
        batch[cCountIDIndex] = 0;
        set(batch);
        return batch;
    }
}
