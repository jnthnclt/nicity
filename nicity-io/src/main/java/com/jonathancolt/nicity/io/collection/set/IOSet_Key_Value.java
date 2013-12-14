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

import com.jonathancolt.nicity.core.lang.UByte;
import com.jonathancolt.nicity.io.collection.ChunkFiler;


/*
 * This supports a set of keyed values
 *
 * BlockFile blockFile = BlockFile.instance("test","a");
 * This is the partitian which holds the data.
 */
public class IOSet_Key_Value extends IOSet {
    public static IOSet_Key_Value factory(
            final RootFP _skvBFP,
            int _keySize,
            int _valueSize,
            int _initialMaxCount,
            ChunkFiler _blocks) throws Exception {
        IOSet_Key_Value skv = new IOSet_Key_Value(_blocks, _keySize, _valueSize) {
            @Override
            public void relocated(long _bfp) throws Exception {
                super.relocated(_bfp);
                _skvBFP.setLong(_bfp);
            }
        };
        long bfp = _skvBFP.getLong();
        if (bfp == -1) {
            skv = (IOSet_Key_Value) IOSet.open(_initialMaxCount, skv);
        } else {
            skv = (IOSet_Key_Value) IOSet.open(skv, bfp);
        }
        return skv;
    }
    
    public IOSet_Key_Value(ChunkFiler _blocks, int _keySize, int _valueSize) {
        super(_blocks, _keySize, _valueSize);
    }
    
    @Override
    public long keyHashCode(byte[] _key, long _modulo) throws Exception {
        //if (_oldEntry == null) {
            return UIOSet.hash(_key, 0, keySize, _modulo);
        //} else {
        //    return UIOSet.hash(_oldEntry, 0, keySize, _modulo);
       // }
    }
    
    @Override
    public byte[] entryKey(byte[] _entry) throws Exception {
        return UByte.copy(_entry, 0, keySize);
    }
    
    @Override
    public byte[] entryValue(byte[] _entry) throws Exception {
        if (valueSize == 0) return null;
        return UByte.copy(_entry, keySize, valueSize);
    }
    
    @Override
    public Object equalsKey(long _hash, byte[] _key, byte[] _oldEntry) throws Exception {
        return null;
    }
    
    // return null if not equal
    @Override
    public byte[] equals(Object _equalsKey, byte[] _key, byte[] _entry) throws Exception {
        if (UByte.equals(_key, 0, _entry, 0, keySize)) {// ok if _key equals first part of entry
            return UByte.copy(_entry, keySize, valueSize);// return value
        } else {
            return null;
        }
    }
    
    @Override
    public byte[] value(byte[] _entry, Object _equalsKey, byte[] _key, byte[] _oldEntry) throws Exception {
        if (_oldEntry == null) {
            UByte.copy(_key, _entry);
        } else {
            if (_key == null) {
                UByte.copy(_oldEntry, _entry);
            } else {
                UByte.copy(_oldEntry, 0, _entry, 0, keySize);
                UByte.copy(_key, keySize, _entry, keySize, valueSize);
            }
        }
        return UByte.copy(_entry, keySize, valueSize);
    }
}// end IOSet_Key_Value

