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
import com.jonathancolt.nicity.core.lang.UByte;
import com.jonathancolt.nicity.io.collection.ChunkFiler;

public class IOSet_Bytes extends IOSet {
    public static IOSet_Bytes factory(
            final RootFP _sbsBFP, int _initialMaxCount,
            ChunkFiler _setFile, ISIO_ID _sio_id, ChunkFiler _chunks, IOSet_ID_BFP _sio_id_bfp) throws Exception {
        IOSet_Bytes sbs = new IOSet_Bytes(_setFile, _sio_id, _chunks, _sio_id_bfp) {
            @Override
            public void relocated(long _bfp) throws Exception {
                super.relocated(_bfp);
                _sbsBFP.setLong(_bfp);
            }
        };
        long bfp = _sbsBFP.getLong();
        if (bfp == -1) {
            sbs = (IOSet_Bytes) IOSet.open(_initialMaxCount, sbs);
        } else {
            sbs = (IOSet_Bytes) IOSet.open(sbs, bfp);
        }
        return sbs;
    }
    
    private ChunkFiler byteChunks;
    private ISIO_ID sio_id;
    private IOSet_ID_BFP sio_id_bfp;
    public IOSet_Bytes(ChunkFiler _setFile, ISIO_ID _sio_id, ChunkFiler _chunks, IOSet_ID_BFP _sio_id_bfp) {
        super(_setFile, 2 * 8, (8+_sio_id.size()));
        sio_id = _sio_id;
        byteChunks = _chunks;
        sio_id_bfp = _sio_id_bfp;
    }
    
    // EntrySize: 8 hashCode, 8 checksum, 8 blockFilePointer, _sio_id.size() aka id
    static int cHashCodeStart = 0;
    static int cChecksumStart = 1*8;
    static int cBFPStart = 2*8;
    static int cIdStart = 3*8;
    
    @Override
    public long keyHashCode(byte[] _key, long _modulo) throws Exception {
        long checksum = UIOSet.hash(_key, 0, _key.length, Long.MAX_VALUE);
        return UIOSet.hash(UIO.longBytes(checksum), 0, 8, _modulo);
    }
    
    @Override
    public byte[] entryKey(byte[] _entry) throws Exception {
        return byteChunks.getFiler(UIO.bytesLong(_entry,cBFPStart)).toBytes();// returns bytes for cBFPStart
    }
    
    @Override
    public byte[] entryValue(byte[] _entry) throws Exception {
        return UByte.copy(_entry, cIdStart, sio_id.size());// returns cMillis and cNanos aka id
    }
    
    @Override
    public Object equalsKey(long _hash, byte[] _key, byte[] _oldEntry) throws Exception {
        long checksum = 0;
        if (_oldEntry == null) {
            checksum = UIOSet.hash(_key, 0, _key.length, Long.MAX_VALUE);// us entire key for checksum
        } else {
            checksum = UIO.bytesLong(_oldEntry, 8);
        }
        return UIO.longsBytes(new long[]{_hash, checksum});
    }
    
    // return null if not equal
    @Override
    public byte[] equals(Object _equalsKey, byte[] _key, byte[] _entry) throws Exception {
        byte[] equalsKey = (byte[])_equalsKey;
        if (UByte.equals(_entry, 0, equalsKey, 0, 8 * 2)) { // are cHashCodeStart and cCheckSum equal
            byte[] key = byteChunks.getFiler(UIO.bytesLong(_entry, 8 * 2)).toBytes();// get bytes for cBFPStart
            
            if (UByte.equals(_key, key)) {// are key and existing key equal
                return UByte.copy(_entry, cIdStart, sio_id.size());// returns cMillis and cNanos aka id
            }
        }
        return null;
    }
    
    @Override
    public byte[] value(byte[] _entry, Object _equalsKey, byte[] _key, byte[] _oldEntry) throws Exception {
        byte[] equalsKey = (byte[])_equalsKey;
        UByte.copy(equalsKey, 0, _entry, 0, 8 * 2);
        if (_oldEntry == null) {
            long fp = byteChunks.newChunk(_key.length);// creates new entry
            byteChunks.getFiler(fp).setBytes(_key);
            UIO.longBytes(fp, _entry, 8 * 2);// sets _entrys cBFPStart
            byte[] vid = sio_id.id();// create new id
            sio_id_bfp.add(vid, fp); // callback to bind id to bfp
            UByte.copy(vid, 0, _entry, cIdStart, sio_id.size());// sets _entrys cTime cNanos aka id
            return UByte.copy(_entry, cIdStart, sio_id.size()); // returns _entrys cTime cNanos aka id
        } else {
            UByte.copy(_oldEntry, cBFPStart, _entry, cBFPStart, 8+sio_id.size());// sets _entrys cBFPStart cTime and cNanos from _oldEntry
            return UByte.copy(_entry,cIdStart,sio_id.size());// returns _entrys cTime cNanos aka id
        }
    }
}// end IOSet_Bytes

