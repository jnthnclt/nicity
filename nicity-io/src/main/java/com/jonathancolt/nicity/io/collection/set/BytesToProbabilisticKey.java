package com.jonathancolt.nicity.io.collection.set;

import com.jonathancolt.nicity.core.io.UIO;
import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.lang.UByte;
import com.jonathancolt.nicity.io.collection.ChunkFiler;

/*
Take a chunk of bytes turn it into a probalistic key.
 */
public class BytesToProbabilisticKey {
    
    public static BytesToProbabilisticKey factory(
            IOut _, int _byteKeySize, ChunkFiler _dataChunkFiler,
            RootFP _indexBFP,
            ChunkFiler _setChunkFiler) throws Exception {
        BytesToProbabilisticKey btbid = new BytesToProbabilisticKey(
                _, _byteKeySize, _dataChunkFiler,
                _indexBFP,
                _setChunkFiler);
        return btbid;
    }
    
    private int byteKeySize;
    private IOSet_Key_Value index;
    private ChunkFiler dataChunkFiler;
    
    private BytesToProbabilisticKey(
            IOut _, int _byteKeySize, ChunkFiler _dataChunkFiler,
            RootFP _indexBFP,
            ChunkFiler _setChunkFiler) throws Exception {
        byteKeySize = _byteKeySize;
        dataChunkFiler = _dataChunkFiler;
        index = IOSet_Key_Value.factory(
                _indexBFP,
                byteKeySize(), 8, 2,
                _setChunkFiler);
    }
    
    public int byteKeySize() {
        return byteKeySize * 8;
    }
    
    // Probabilistic key
    synchronized public byte[] toKey(byte[] _bytes) throws Exception {
        if (_bytes == null) {
            return null;
        }
        if (_bytes.length == 0) {
            return null;
        }
        return UIOSet.hashID(_bytes, 0, _bytes.length, byteKeySize);
    }
    
    // if this method returns null then there is already an instance occupying the id slot
    
    synchronized public byte[] toByteKey(byte[] _bytes) throws Exception {
        byte[] key = toKey(_bytes);
        if (key == null) {
            return null;
        }
        if (UIOSet.contains(index, key)) {
            byte[] bfp = UIOSet.get(index, key);
            byte[] bytes = dataChunkFiler.getFiler(UIO.bytesLong(bfp)).toBytes();
            if (!UByte.equals(bytes, _bytes)) {
                return null;
            }
        } else {
            long bfp = dataChunkFiler.newChunk(_bytes.length);
            dataChunkFiler.getFiler(bfp).setBytes(_bytes);
            byte[] keybfp = UByte.join(key, UIO.longBytes(bfp));
            UIOSet.add(index, keybfp);
        }
        return key;
    }
    
    synchronized public byte[] toBytes(byte[] _byteKey) throws Exception {
        byte[] bfp = UIOSet.get(index, _byteKey);
        if (bfp == null) {
            return null;
        }
        return dataChunkFiler.getFiler(UIO.bytesLong(bfp)).toBytes();
    }
    
    synchronized public byte[] removeID(byte[] _byteKey) throws Exception {
        byte[] _bfp = UIOSet.get(index, _byteKey);
        long bfp = UIO.bytesLong(_bfp);
        byte[] bytes = dataChunkFiler.getFiler(bfp).toBytes();
        if (UIOSet.remove(index, _byteKey)) {
            dataChunkFiler.remove(bfp);
        }
        return bytes;
    }
    
    @Override
    public String toString() {
        return index + " " + dataChunkFiler;
    }
}

