package com.jonathancolt.nicity.io.collection;

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

import com.jonathancolt.nicity.core.collection.CArray;
import com.jonathancolt.nicity.io.Filer;
import com.jonathancolt.nicity.io.SubFiler;
import com.jonathancolt.nicity.io.SubFilers;
import com.jonathancolt.nicity.core.io.UIO;
import java.io.File;

public class StackFiler {
    protected long count = 0;
    protected long blockSize = 0;
    protected long maxCount = 0;
    private long referenceNumber = 0;
    protected SubFiler filer;
    /*
    New Call Sequence
    StackFiler stackFile = StackFiler();
    stackFile.setup(100,100);
    open(_filer);
     */
    public StackFiler() {
    }
    public void setup(long _blockSize, long _maxCount, long _referenceNumber) {
        count = 0;
        blockSize = _blockSize;
        maxCount = _maxCount;
        referenceNumber = _referenceNumber;
    }
    public static long bytesNeeded(long _maxCount, long _blockSize) {
        long bytesNeeded = 0;
        bytesNeeded += 8;//"count"
        bytesNeeded += 8;//"blockSize"
        bytesNeeded += 8;//"maxCount"
        bytesNeeded += 8;//"referenceNumber"
        bytesNeeded += (_maxCount * (8 + _blockSize));
        return bytesNeeded;
    }
    public long bytesNeeded() {
        return bytesNeeded(maxCount, blockSize);
    }
    public void open(SubFiler _filer) throws Exception {
        filer = _filer;
        synchronized (filer.lock()) {
            UIO.writeLong(filer, count, "count");
            UIO.writeLong(filer, blockSize, "blockSize");
            UIO.writeLong(filer, maxCount, "maxCount");
            UIO.writeLong(filer, referenceNumber, "referenceNumber");
            filer.seek(bytesNeeded() - 1);
            filer.write(0);
        }
    }
    /*
    Exsisting Call Sequence
    StackFiler stackFile = StackFiler(_filer);
    open();
     */
    public StackFiler(SubFiler _filer) throws Exception {
        filer = _filer;
    }
    public void open() throws Exception {
        synchronized (filer.lock()) {
            filer.seek(0);
            count = UIO.readLong(filer, "count");
            blockSize = UIO.readLong(filer, "blockSize");
            maxCount = UIO.readLong(filer, "maxCount");
            referenceNumber = UIO.readLong(filer, "referenceNumber");
        }
    }
    public long getReferenceNumber() {
        return referenceNumber;
    }
    public long getCount() {
        return count;
    }
    public byte[] get(long _index) throws Exception {
        if (count == 0) {
            return null;
        }
        if (_index < 0) {
            return null;
        }
        if (_index >= count) {
            return null;
        }
        synchronized (filer.lock()) {
            seek(_index);
            long blockLength = UIO.readLong(filer, "blockLength");
            byte[] block = new byte[(int) blockLength];
            UIO.read(filer, block);
            return block;
        }
    }
    public void set(long _index, byte[] _block) throws Exception {
        synchronized (filer.lock()) {
            if (_index >= count) {
                push(_block);
                return;
            }
            if (count == 0) {
                return;
            }
            if (_index < 0) {
                return;
            }
            seek(_index);
            long blockLength = _block.length;
            if (blockLength > blockSize) {
                throw new Exception("Block Overflow");
            }
            UIO.writeLong(filer, blockLength, "blockLength");
            UIO.write(filer, _block);
        }
    }
    public SubFiler getSegment(long _index) throws Exception {
        if (count == 0) {
            return null;
        }
        if (_index < 0) {
            return null;
        }
        if (_index >= count) {
            return null;
        }
        synchronized (filer.lock()) {
            seek(_index);
            long blockLength = UIO.readLong(filer, "blockLength");
            long fp = filer.getFilePointer();
            SubFiler _filer = filer.get(
                    fp,
                    fp + blockSize,
                    blockLength);
            return _filer;
        }
    }
    public boolean canPush() {
        return count + 1 < maxCount;
    }
    public void push(byte[] _block) throws Exception {
        synchronized (filer.lock()) {
            if (count == maxCount) {
                throw new Exception("Stack Overflow");
            }
            seek(count);
            long blockLength = _block.length;
            if (blockLength > blockSize) {
                throw new Exception("Block Overflow");
            }
            UIO.writeLong(filer, blockLength, "blockLength");
            UIO.write(filer, _block);

            filer.seek(0);
            UIO.writeLong(filer, ++count, "count");
        }
    }
    public byte[] getAllBytes() throws Exception {
        synchronized (filer.lock()) {
            if (count == 0) {
                return null;
            }
            long byteLength = count * (8 + blockSize);
            seek(0);
            byte[] popBytes = new byte[(int) byteLength];
            UIO.read(filer, popBytes);
            return popBytes;
        }
    }
    public void reset() throws Exception {
        synchronized (filer.lock()) {
            count = 0;
            filer.seek(0);
            UIO.writeLong(filer, count, "count");
        }
    }
    public byte[][] pop(long _count) throws Exception {
        if (count == 0) {
            return null;
        }
        synchronized (filer.lock()) {
            if (_count > count) {
                _count = count;
            }
            CArray pops = new CArray(byte[].class);
            for (int i = 0; i < _count; i++) {
                pops.insertLast((Object) pop());
            }
            return (byte[][]) pops.getAll();
        }
    }
    public byte[] popBytes(long _count) throws Exception {
        if (count == 0) {
            return null;
        }
        synchronized (filer.lock()) {
            if (_count > count) {
                _count = count;
            }
            long byteLength = _count * (8 + blockSize);
            seek(count - _count);
            byte[] popBytes = new byte[(int) byteLength];
            UIO.read(filer, popBytes);
            filer.seek(0);
            count -= _count;
            UIO.writeLong(filer, count, "count");
            return popBytes;
        }
    }
    public byte[] pop() throws Exception {
        if (count == 0) {
            return null;
        }
        synchronized (filer.lock()) {
            seek(count - 1);
            long blockLength = UIO.readLong(filer, "blockLength");
            byte[] block = new byte[(int) blockLength];
            UIO.read(filer, block);

            filer.seek(0);
            UIO.writeLong(filer, --count, "count");
            return block;
        }
    }
    final private void seek(long _count) throws Exception {
        // 8 for count
        // 8 for blockSize
        // 8 for maxCount
        // 8 for referenceNumber
        filer.seek(32 + (_count * (blockSize + 8)));
    }
    public static String name(String _setName) {
        return _setName + ".stack";
    }
    public static StackFiler factory(
            String _stackName,
            long _blockSize, long _maxCount, long _referenceNumber, byte[] _fill) throws Exception {
        if (new File(name(_stackName)).exists()) {
            return openInstance(_stackName);
        } else {
            StackFiler ds = newInstance(
                    _stackName,
                    _blockSize, _maxCount, _referenceNumber);
            if (_fill != null) {
                for (int i = 0; i < _maxCount; i++) {
                    ds.set(i, _fill);
                }
            }
            return ds;
        }
    }
    public static StackFiler newInstance(
            String _stackName,
            long _blockSize, long _maxCount, long _referenceNumber) throws Exception {

        Filer stackFiler = Filer.open(name(_stackName), "rw");
        SubFilers stackSegmenter = new SubFilers(name(_stackName), stackFiler);
        SubFiler stackSegment = stackSegmenter.get(0, Long.MAX_VALUE, 0);
        StackFiler stack = new StackFiler();
        stack.setup(_blockSize, _maxCount, _referenceNumber);
        stack.open(stackSegment);
        return stack;
    }
    public static StackFiler newInstance(
            SubFiler _filer,
            long _blockSize, long _maxCount, long _referenceNumber) throws Exception {
        StackFiler stack = new StackFiler();
        stack.setup(_blockSize, _maxCount, _referenceNumber);
        stack.open(_filer);
        return stack;
    }
    public static StackFiler openInstance(
            String _stackName) throws Exception {
        Filer setFiler = Filer.open(name(_stackName), "rw");
        SubFilers fio = new SubFilers(name(_stackName), setFiler);
        SubFiler fioSegment = fio.get(0, Long.MAX_VALUE, 0);
        StackFiler stack = new StackFiler(fioSegment);
        stack.open();
        return stack;
    }
    public static StackFiler openInstance(
            SubFiler _filer) throws Exception {
        StackFiler stack = new StackFiler(_filer);
        stack.open();
        return stack;
    }
}

