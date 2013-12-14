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

import com.jonathancolt.nicity.core.collection.IBackcall;
import com.jonathancolt.nicity.core.collection.IContain;
import com.jonathancolt.nicity.core.collection.IHaveCount;
import com.jonathancolt.nicity.io.SubFiler;
import com.jonathancolt.nicity.core.io.UIO;
import com.jonathancolt.nicity.core.lang.ASetObject;
import com.jonathancolt.nicity.core.lang.ICallback;
import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.lang.OrderedKeys;
import com.jonathancolt.nicity.core.memory.SoftIndex;
import com.jonathancolt.nicity.io.collection.ChunkFiler;

// Implements a disk backed Set :)
abstract public class IOSet extends ASetObject implements Cloneable, IHaveCount, IBackcall, IContain {
    // Used By Subclasses to change behavior
    // creates the hashcode for the key key portion
    abstract public long keyHashCode(byte[] _key, long _modulo) throws Exception;
    // extract the portion of the entry that represents the key
    abstract public byte[] entryKey(byte[] _entry) throws Exception;
    // extract the portion of the entry that represents the value
    abstract public byte[] entryValue(byte[] _entry) throws Exception;
    // check for key equality
    abstract public Object equalsKey(long _hash, byte[] _key, byte[] _oldEntry) throws Exception;
    // return null if not equal
    abstract public byte[] equals(Object _equalsKey, byte[] _key, byte[] _entry) throws Exception;
    abstract public byte[] value(byte[] _entry, Object _equalsKey, byte[] _key, byte[] _oldEntry) throws Exception;
    public void relocated(long _bfp) throws Exception {
    }
    // Ended Used By Subclasses to change behavior
    final static Object setIndexLock = new Object();
    private static SoftIndex setIndex = new SoftIndex();
    public static IOSet lookup(ChunkFiler _chunks, long _bfp) throws Exception {
        synchronized (setIndexLock) {
            return (IOSet) setIndex.get(new OrderedKeys(_chunks,_bfp));
        }
    }
    public static IOSet open(long _maxCount, IOSet _set) throws Exception {
        synchronized (setIndexLock) {
            _set.maxCount = _maxCount;
            _set.io();
            setIndex.set(_set, _set);
            return _set;
        }
    }
    public static IOSet open(IOSet _set, long _bfp) throws Exception {
        synchronized (setIndexLock) {
            IOSet set = (IOSet) setIndex.get(_set.hashObject(_bfp));
            if (set != null) {
                return set;
            }
            _set.io(_bfp);
            setIndex.set(_set, _set);
            return _set;
        }
    }
    // Instance State
    long bfp = 1;
    protected ChunkFiler chunks;
    volatile SubFiler io;
    int entrySize;
    int keySize;
    int valueSize;
    long maxCount = 0;
    transient long cachedCount = -1;
    final Object resizingLock = new Object();
    @Override
    public String toString() {
        return "BFP(" + bfp + ") Count(" + getCount() + ") MaxCount(" + maxCount + ") IO(" + io + ")";
    }
    public IOSet(ChunkFiler _chunks, int _keySize, int _valueSize) {
        chunks = _chunks;
        keySize = _keySize;
        valueSize = _valueSize;
        entrySize = keySize + valueSize;
    }
    public int keySize() { return keySize; }
    public long maxCount() { return maxCount; }

    final synchronized private void io() throws Exception {
        if (maxCount < 2) {
            throw new RuntimeException("Invalid MaxCount:" + maxCount);
        }
        if (io != null) {
            return;
        }
        long size = bytesNeeded(maxCount, entrySize);
        long _bfp = chunks.newChunk(size);
        synchronized (resizingLock) {
            bfp = _bfp;
            SubFiler _io = chunks.getFiler(bfp);
            synchronized (_io.lock()) {
                io = _io;
                flushHeader(io, 0, maxCount);
            }
            relocated(bfp);
        }
    }
    final synchronized void io(long _bfp) throws Exception {
        if (io != null) {
            return;
        }
        synchronized (resizingLock) {
            bfp = _bfp;
            SubFiler _io = chunks.getFiler(_bfp);
            synchronized (_io.lock()) {
                io = _io;
                readHeader(io);
            }
        }
    }
    final public Object lock() {
        return io.lock();
    }
    final public Object hashObject(long _bfp) {
        if (_bfp < 0) {
            throw new RuntimeException("Uninitialize SIO");
        }
        return new OrderedKeys(chunks, _bfp);
    }
    final public Object hashObject() {
        if (bfp < 0) {
            throw new RuntimeException("Uninitialize SIO");
        }
        return new OrderedKeys(chunks, bfp);
    }
    // IContain
    public boolean contains(Object _value) {
        try {
            return UIOSet.contains(this, (byte[]) _value);
        } catch (Exception x) {
            x.printStackTrace();//!!
            return false;
        }
    }
    // IBackcall
    public void backcall(IOut _, ICallback _callback) {
        UIOSet.keysBackcall(this, _, _callback);
    }
    // Statics
    static final int headerSize = 8 + 8;
    static final long bytesNeeded(long _maxCount, int _entrySize) {
        return headerSize + ((_maxCount) * (_entrySize + 1));
    }
    final private void flushHeader(SubFiler _io, long _count, long _maxCount) throws Exception {
        cachedCount = _count;
        _io.seek(0);
        UIO.writeLong(_io, _count, "count");
        UIO.writeLong(_io, _maxCount, "maxCount");
    }
    final private void readHeader(SubFiler _io) throws Exception {
        synchronized (_io.lock()) {
            _io.seek(0);
            cachedCount = UIO.readLong(_io, "count");
            maxCount = UIO.readLong(_io, "maxCount");
        }
    }
    // Count Methods
    // IHaveCount
    public long getCount() {
        synchronized (io.lock()) {
            try {
                long count = cachedCount;
                if (count < 1) { // need to get from disk and store in cache
                    io.seek(0);
                    count = UIO.readLong(io, "count");
                    cachedCount = count;
                }
                return count;
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }
    }
    @Override
    public Object clone() {
        try {
            IOSet sio = (IOSet) super.clone();
            sio.io = null;
            sio.bfp = -1;
            return sio;
        } catch (CloneNotSupportedException x) {
            throw new InternalError();
        }
    }
    public IOSet clone(long _maxCount) throws Exception {
        IOSet clone = (IOSet) clone();
        clone.maxCount = _maxCount;
        if (clone.maxCount < 2) {
            throw new RuntimeException("Invalid MaxCount:" + clone.maxCount);
        }
        long size = bytesNeeded(clone.maxCount, clone.entrySize);
        long _bfp = clone.chunks.newChunk(size);
        synchronized (resizingLock) {
            clone.bfp = _bfp;
            SubFiler _io = clone.chunks.getFiler(_bfp);
            synchronized (_io.lock()) {
                _io.seek(0);
                for (int i = 0; i < _io.getSize(); i++) {
                    _io.write(0);//?? 8-16-09
                }
                clone.io = _io;
                flushHeader(clone.io, 0, clone.maxCount);
            }
        }
        return clone;
    }
    final synchronized void io(IOSet _io) throws Exception {
        SubFiler oldIO = io;
        synchronized (setIndexLock) {
            setIndex.remove(this);
            bfp = _io.bfp;
            io = _io.io;
            maxCount = _io.maxCount;
            flushHeader(io, _io.cachedCount, maxCount);
            relocated(bfp);
            setIndex.set(this, this);
        }
        //oldIO.setCachedCount(-1);
        //oldIO.free();//??

    }
    final void incCount(long _amount) throws Exception {
        long count = 0;
        synchronized(io.lock()) {
            cachedCount += _amount;
            count = cachedCount;
            io.seek(0);
            UIO.writeLong(io, count, "count");
        }
    }
    final void decCount(long _amount) throws Exception {
        long count = 0;
        synchronized(io.lock()) {
            cachedCount -= _amount;
            count = cachedCount;
            io.seek(0);
            UIO.writeLong(io, count, "count");
        }
    }
}
