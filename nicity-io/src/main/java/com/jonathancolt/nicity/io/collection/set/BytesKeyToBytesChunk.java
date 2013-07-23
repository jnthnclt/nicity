package com.jonathancolt.nicity.io.collection.set;

import com.jonathancolt.nicity.io.SubFiler;
import com.jonathancolt.nicity.core.io.UIO;
import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.lang.UByte;
import com.jonathancolt.nicity.io.collection.ChunkFiler;

/*
A set of blobs
 */
public class BytesKeyToBytesChunk {
    public static BytesKeyToBytesChunk factory(
            IOut _, int _byteKeySize, ChunkFiler _dataChunkFiler,
            final RootFP _indexBFP,
            ChunkFiler _setChunkFiler) throws Exception {
        BytesKeyToBytesChunk btbid = new BytesKeyToBytesChunk(
                _, _byteKeySize, _dataChunkFiler,
                _indexBFP,
                _setChunkFiler);
        return btbid;
    }
    private int byteKeySize;
    private IOSet_Key_Value byteKeyToBFP;
    private ChunkFiler dataChunkFiler;
    private BytesKeyToBytesChunk(
            IOut _, int _byteKeySize, ChunkFiler _dataChunkFiler,
            final RootFP _indexBFP,
            ChunkFiler _setChunkFiler) throws Exception {
        byteKeySize = _byteKeySize;
        dataChunkFiler = _dataChunkFiler;
        byteKeyToBFP = IOSet_Key_Value.factory(
                _indexBFP,
                byteKeySize, 8, 2,
                _setChunkFiler);
    }
    public int byteKeySize() {
        return byteKeySize;
    }
    // if this method returns null then there is already an instance occupying the id slot
    synchronized public void set(byte[] _key, byte[] _bytes) throws Exception {
        if (_key == null) {
            return;
        }
        if (_bytes == null) {
            return;
        }
        byte[] bfp = UIOSet.get(byteKeyToBFP, _key);
        if (bfp == null) {
            long _bfp = dataChunkFiler.newChunk(_bytes.length);
            dataChunkFiler.getFiler(_bfp).setBytes(_bytes);
            byte[] keyBytes = UByte.join(_key, UIO.longBytes(_bfp));
            UIOSet.add(byteKeyToBFP, keyBytes);
        } else {
            long _bfp = UIO.bytesLong(bfp);
            SubFiler chunk = dataChunkFiler.getFiler(_bfp);
            if (_bytes.length <= chunk.getSize()) {
                chunk.setBytes(_bytes);
            }
            else {
                long newbfp = dataChunkFiler.newChunk(_bytes.length);
                dataChunkFiler.getFiler(_bfp).setBytes(_bytes);
                dataChunkFiler.remove(_bfp);
                _bfp = newbfp;
            }
            byte[] keyBytes = UByte.join(_key, UIO.longBytes(_bfp));
            UIOSet.add(byteKeyToBFP, keyBytes);
        }
    }
    synchronized public byte[] get(byte[] _key) throws Exception {
        byte[] bfp = UIOSet.get(byteKeyToBFP, _key);
        if (bfp == null) {
            return null;
        }
        long _bfp = UIO.bytesLong(bfp);
        byte[] bytes = dataChunkFiler.getFiler(_bfp).toBytes();
        dataChunkFiler.remove(_bfp);
        return bytes;
    }
    synchronized public byte[] remove(byte[] _key) throws Exception {
        byte[] bfp = UIOSet.get(byteKeyToBFP, _key);
        if (bfp == null) {
            return null;
        }
        long _bfp = UIO.bytesLong(bfp);
        byte[] bytes = dataChunkFiler.getFiler(_bfp).toBytes();
        dataChunkFiler.remove(_bfp);
        return bytes;
    }
    @Override
    public String toString() {
        return byteKeyToBFP + " " + dataChunkFiler;
    }
}

