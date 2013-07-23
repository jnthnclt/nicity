package com.jonathancolt.nicity.io.collection.set;

import com.jonathancolt.nicity.core.collection.CArray;
import com.jonathancolt.nicity.io.SubFiler;
import com.jonathancolt.nicity.core.io.UIO;
import com.jonathancolt.nicity.core.lang.ICallback;
import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.time.MilliTimer;

public class UIOSet {
    // Hash Methods
    static public boolean contains(final IOSet _io, byte[] _key) throws Exception {
        return (get(_io, _key) != null);
    }
    // the equals( function in IOSet dictates what is returned assuming the key exisit
    // else get should return null
    static public byte[] get(final IOSet _io, byte[] _key) throws Exception {
        if (_key == null || _key.length == 0) {
            return null;
        }
        synchronized (_io.lock()) {
            long modulo = _io.maxCount;
            long hash = _io.keyHashCode(_key, modulo);
            return _get(_io, modulo, hash, _key);
        }
    }
    // the value( function in IOSet dictates what is returned
    static final public byte[] add(final IOSet _io, byte[] _key) throws Exception {
        if (_key == null || _key.length == 0) {
            return null;
        }
        synchronized (_io.lock()) {
            synchronized (_io.resizingLock) {
                long modulo = _io.maxCount;
                long hash = _io.keyHashCode(_key, modulo);
                byte[] result = _add(_io, modulo, hash, _key, null, true);
                resizeIfNecessary(_io, modulo);
                return result;
            }
        }
    }
    // return true if entry existed and was removed; else false means entry did not exist
    static final public boolean remove(final IOSet _io, byte[] _key) throws Exception {
        if (_key == null || _key.length == 0) {
            return true;
        }
        synchronized (_io.lock()) {
            long modulo = _io.maxCount;
            long hash = _io.keyHashCode(_key, modulo);
            return remove(_io, modulo, hash, _key, true);
        }
    }
    // Warning!
    // it is possible entries will not be returned by backcall and/or they may be returned more than once
    static final public void entriesbackcall(final IOSet _io, IOut _, ICallback _callback) {
        try {
            long count = _io.getCount();
            for (int i = 0; i < (_io.maxCount); i++) {
                if (count <= 0) {
                    break;
                }
                byte[] entry = null;
                synchronized (_io.lock()) {
                    byte status = status(_io, i);
                    if (status == cSkip) {
                        continue;
                    }
                    if (status == cNull) {
                        continue;
                    }
                    entry = entry(_io, i);
                }
                byte[] entryValue = _io.entryValue(entry);
                Object back = _callback.callback(entryValue);
                if (back != entryValue) {
                    break;
                }
                count--;
                if (_.canceled()) {
                    break;
                }
            }
            return;
        } catch (Exception x) {
            return;
        }
    }
    // Warning!
    // it is possible entries will not be returned by backcall and/or they may be returned more than once
    static final public void keysBackcall(final IOSet _io, IOut _, ICallback _callback) {
        try {
            long count = _io.getCount();
            for (int i = 0; i < _io.maxCount; i++) {
                if (count <= 0) {
                    break;
                }
                byte[] entry = null;
                synchronized (_io.lock()) {
                    byte status = status(_io, i);
                    if (status == cSkip) {
                        continue;
                    }
                    if (status == cNull) {
                        continue;
                    }
                    entry = entry(_io, i);
                }
                byte[] entryKey = _io.entryKey(entry);
                Object back = _callback.callback(entryKey);
                if (back != entryKey) {
                    break;
                }
                count--;
                if (_.canceled()) {
                    break;
                }
            }
            return;
        } catch (Exception x) {
            return;
        }
    }
    // Warning!
    // it is possible entries will not be returned by backcall and/or they may be returned more than once
    static final public Object[] getAllKeys(final IOSet _io, Class _class, IOut _, final ICallback _callback) {
        final CArray array = new CArray(_class);
        keysBackcall(_io, _, new ICallback() {
            public Object callback(Object _value) {
                array.insertLast(_callback.callback(_value));
                return _value;
            }
        });
        return array.getAll();
    }
    static final public void removeAll(final IOSet _io) throws Exception {//!!to do: shrink instead of retaining maxCount?
        synchronized (_io.lock()) {
            _io.cachedCount = 0;
            _io.io.seek(0);
            UIO.writeLong(_io.io, 0, "count");
            for (long i = 0; i < _io.maxCount; i++) {
                status(_io, i, cNull);
            }
        }
    }
    // *
    // *
    // *  private statics from here on
    // *
    // *
    static final private byte[] _get(final IOSet _io, long _modulo, long _hash, byte[] _key) throws Exception {
        Object equalsKey = _io.equalsKey(_hash, _key, null);
        long startIndex = _hash;
        for (long i = 0; i < _modulo; startIndex = (++startIndex) % _modulo, i++) {
            byte status = status(_io, startIndex);
            if (status == cSkip) {
                continue;
            }
            if (status == cNull) {
                return null;
            }
            if (status != cExists) {
                throw new RuntimeException("unexpected get status=" + status); //!! bug
            }
            byte[] entry = entry(_io, startIndex);
            byte[] result = _io.equals(equalsKey, _key, entry);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
    static final private byte[] _add(final IOSet _io, long _modulo, long _hash, byte[] _key, byte[] _oldEntry, boolean _incCount) throws Exception {
        Object equalsKey = _io.equalsKey(_hash, _key, _oldEntry);
        long startIndex = _hash;
        for (long i = 0; i < _modulo; startIndex = (++startIndex) % _modulo, i++) {
            byte status = status(_io, startIndex);
            if (status == cExists) {
                byte[] entry = entry(_io, startIndex);
                byte[] result = _io.equals(equalsKey, _key, entry);
                if (result != null) {
                    byte[] value = _io.value(entry, equalsKey, _key, entry);
                    write(_io.io, _io.entrySize, startIndex, entry);
                    return value;
                }
            } else if (status == cNull) {
                byte[] entry = new byte[_io.entrySize];
                byte[] value = _io.value(entry, equalsKey, _key, _oldEntry);
                write(_io.io, _io.entrySize, startIndex, entry);
                if (_incCount) {
                    _io.incCount(1);
                }
                return value;
            }
        }
        return null;
    }
    static final private boolean remove(IOSet _io, long _modulo, long _hash, byte[] _key, boolean _decCount) throws Exception {
        Object equalsKey = _io.equalsKey(_hash, _key, null);
        long startIndex = _hash;
        for (long i = 0; i < _modulo; startIndex = (++startIndex) % _modulo, i++) {
            byte status = status(_io, startIndex);
            if (status == cSkip) {
                continue;
            }
            if (status == cNull) {
                return false;
            }
            if (status != cExists) {
                throw new Exception("unexpected remove status=" + status); //!! bug
            }
            byte[] entry = entry(_io, startIndex);
            byte[] result = _io.equals(equalsKey, _key, entry);
            if (result != null) {
                long next = (startIndex + 1) % _modulo;
                long prior = (startIndex - 1) % _modulo;
                if (status(_io, next) == cNull) {
                    status(_io, startIndex, cNull);
                    for (long j = 0; j < _modulo; prior = (--prior + _modulo) % _modulo, j++) {
                        if (status(_io, prior) != cSkip) {
                            break;
                        }
                        status(_io, prior, cNull);
                    }
                } else {
                    status(_io, startIndex, cSkip);
                }

                if (_decCount) {
                    _io.decCount(1);
                }
                return true;
            }
        }
        return false;
    }
    static final private boolean resizeIfNecessary(final IOSet _io, long _modulo) throws Exception {
        long count = _io.getCount();
        if (count < (_modulo * 0.5d)) {
            return false; // typical case, so code is fast return before locking
        }
        final long _newModulo = _modulo * 2;


        System.out.println("Resizing..." + _modulo + " -> " + _newModulo);
        IOSet newIO = (IOSet) _io.clone(_newModulo);
        synchronized (newIO.lock()) {

            MilliTimer sw = new MilliTimer();
            sw.start();
            long update = count / 16;
            long moved = 0;
            int ci = 0;
            Object[] cache = new Object[64];
            for (int i = 0; moved < count && i < _modulo; i++) {
                update--;
                if (update == 0) {
                    //_.out(moved + "->" + count);
                    //_.out((int) moved, (int) count);
                    update = count / 16;
                }
                byte status = status(_io, i);
                if (status == cSkip || status == cNull) {
                    continue;
                }
                if (status != cExists) {
                    throw new RuntimeException("Invalid Status:" + status);
                }
                cache[ci++] = entry(_io, i);
                if (ci == cache.length) {
                    _resize(cache, ci, newIO, _newModulo);
                    ci = 0;
                }
                moved++;
            }
            if (ci > 0) {
                _resize(cache, ci, newIO, _newModulo);
            }
            sw.stop();
            newIO.cachedCount = count;
            _io.io(newIO);

            if (count > 4000) {
                System.out.println("Rehashed " + count + " in " + sw.duration() + " " + ((double) count) / (double) ((sw.duration() + 1) / 1000d));
            }
        }
        return true;

    }
    static private final void _resize(Object[] _cache, int _ci, IOSet _newIO, long _newModulo) throws Exception {
        long[] newHashs = new long[_ci];
        for (int i = 0; i < _ci; i++) {
            //newHashs[i] = _newIO.keyHashCode(null, _newModulo, (byte[]) _cache[i]);
            newHashs[i] = _newIO.keyHashCode((byte[]) _cache[i], _newModulo);
        }
        //!! to do sort so seek are sequencial and forward
        for (int i = 0; i < _ci; i++) {
            _add(_newIO, _newModulo, newHashs[i], null, (byte[]) _cache[i], false);
        }
    }
    // Instance to Statics
    static final public byte[] entry(final IOSet _io, long _index) throws Exception {
        return read(_io.io, _io.entrySize, _index);
    }
    static final public byte status(final IOSet _io, long _index) throws Exception {
        return readStatus(_io.io, _io.entrySize, _index);
    }
    static final private void status(final IOSet _io, long _index, byte _status) throws Exception {
        writeStatus(_io.io, _io.entrySize, _index, _status);
    }
    // Statics IO
    static final public byte cNull = (byte) 0;
    static final public byte cSkip = (byte) 1;
    static final public byte cExists = (byte) 2;
    static final private void writeStatus(SubFiler _io, int _entrySize, long _index, byte _status) throws Exception {
        seek(_io, _entrySize, _index, 0);
        UIO.writeByte(_io, _status, "status");
    }
    static final private void write(SubFiler _io, int _entrySize, long index, byte[] _entry) throws Exception {
        writeStatus(_io, _entrySize, index, cExists);
        UIO.write(_io, _entry);
    }
    static final private byte readStatus(SubFiler _io, int _entrySize, long _index) throws Exception {
        seek(_io, _entrySize, _index, 0);
        return UIO.readByte(_io, "status");
    }
    static final private byte[] read(SubFiler _io, int _entrySize, long _index) throws Exception {
        seek(_io, _entrySize, _index, 1);
        byte[] key = new byte[_entrySize];
        UIO.read(_io, key);
        return key;
    }
    static final private void seek(SubFiler _io, int _entrySize, long _index, long _offset) throws Exception {
        _io.seek(IOSet.headerSize + (_index * (_entrySize + 1)) + _offset);
    }
    // Static Hashing
    static final public long hash(byte[] _key, int _start, int _length, long _modulo) {
        long randMult = 0x5DEECE66DL;
        long randAdd = 0xBL;
        long randMask = (1L << 48) - 1;
        long seed = _modulo;
        long result = 0;
        for (int i = _start; i < _start + _length; i++) {
            long x = (seed * randMult + randAdd) & randMask;
            seed = x;
            x %= _modulo;
            result += (_key[i] + 128) * x;
        }
        if (result < 0) {
            result = -result;
        }
        return result % (_modulo - 1);
    }
    // Static Hashing
    static final public byte[] hashID(byte[] _key, int _start, int _length, int _size) {
        long randMult = 0x5DEECE66DL;
        long randAdd = 0xBL;
        long randMask = (1L << 48) - 1;
        long seed = 112375;
        int ri = 0;
        long[] result = new long[_size];
        for (int i = _start; i < _start + _length; i++) {
            long x = (seed * randMult + randAdd) & randMask;
            seed = x;
            result[ri] += (_key[i] + 128) * x;
            ri++;
            if (ri >= _size) {
                ri = 0;
            }
        }
        return UIO.longsBytes(result);
    }
}

