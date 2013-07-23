package com.jonathancolt.nicity.io.collection.set;

import com.jonathancolt.nicity.core.io.UIO;
import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.lang.UByte;
import com.jonathancolt.nicity.io.collection.ChunkFiler;

/*
Dictionary key to ID and ID to key
 */
public class ByteKeyToIntKey {
    public static ByteKeyToIntKey factory(
            IOut _, int _keySize,
            RootFP _keyToIDBFP,
            ChunkFiler _keyToIDChunkFiler,
            RootFP _IDToKeyBFP,
            ChunkFiler _IDToKeyChunkFiler) throws Exception {
        ByteKeyToIntKey btbid = new ByteKeyToIntKey(
                _, _keySize,
                _keyToIDBFP, _keyToIDChunkFiler,
                _IDToKeyBFP, _IDToKeyChunkFiler);
        return btbid;
    }
    private IOSet_Key_Value keyToID;
    private IOSet_Key_Value IDToKey;
    private ByteKeyToIntKey(
            IOut _, int _keySize,
            final RootFP _keyToIDBFP,
            ChunkFiler _keyToIDChunkFiler,
            final RootFP _IDToKeyBFP,
            ChunkFiler _IDToKeyChunkFiler) throws Exception {

        keyToID = IOSet_Key_Value.factory(
                _keyToIDBFP,
                _keySize, 4, 2,
                _keyToIDChunkFiler);

        IDToKey = IOSet_Key_Value.factory(
                _IDToKeyBFP,
                4, _keySize, 2,
                _IDToKeyChunkFiler);
    }
    @Override
    public String toString() {
        return keyToID + " " + IDToKey;
    }
    synchronized public int addByteKey(byte[] _key) throws Exception {
        if (_key == null) {
            return 0;
        }
        byte[] id = UIOSet.get(keyToID, _key);
        if (id != null) {
            return UIO.bytesInt(id);
        }
        id = UIO.intBytes((int) keyToID.getCount());
        UIOSet.add(keyToID, UByte.join(_key, id));
        UIOSet.add(IDToKey, UByte.join(id, _key));
        return UIO.bytesInt(id);
    }
    synchronized public int lookupByteKey(byte[] _key) throws Exception {
        if (_key == null) {
            return -1;
        }
        byte[] id = UIOSet.get(keyToID, _key);
        if (id == null) {
            return -1;
        }
        return UIO.bytesInt(id);
    }
    synchronized public byte[] toByteKey(int _id) throws Exception {
        if (_id == -1) {
            return null;
        }
        byte[] key = UIOSet.get(IDToKey, UIO.intBytes(_id));
        if (key == null) {
            return null;
        }
        return key;
    }
    synchronized public void removeIntKey(int _id) throws Exception {
        if (_id + 1 != keyToID.getCount()) {
            return;
        }
        byte[] id = UIO.intBytes(_id);
        byte[] key = UIOSet.get(IDToKey, id);
        UIOSet.remove(keyToID, key);
        UIOSet.remove(IDToKey, id);
    }
}

