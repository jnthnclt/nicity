package com.jonathancolt.nicity.io.collection.set;

import com.jonathancolt.nicity.core.io.UIO;
import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.lang.UByte;
import com.jonathancolt.nicity.io.collection.ChunkFiler;

/*

This is a set which uses a key to get to another set.

final PreferenceFile preferences = new PreferenceFile(new File("testPreferences.txt"));
This is a sky hook which points to the root set.

BlockFile blockFile = BlockFile.instance("test","a");//Disk
This is the partitianl which holds the data

 */
public class IOSet_Key_IOSet extends IOSet_Key_Value {
   
    public static IOSet_Key_IOSet factory(
            IOut _,
            final RootFP _sksBFP,
            int _keySize,
            int _sioKeySize,int _sioValueSize,
            int _initialMaxCount,
            ChunkFiler _blocks
    ) throws Exception {
        IOSet_Key_IOSet sks = new IOSet_Key_IOSet(_blocks, _keySize, _sioKeySize, _sioValueSize) {
            @Override
            public void relocated(long _bfp) throws Exception {
                super.relocated(_bfp);
                _sksBFP.setLong(_bfp);
            }
        };
        long bfp = _sksBFP.getLong();
        _.out("SIO: " + _sksBFP + " Loading." + bfp);
        if (bfp == -1) {
            sks = (IOSet_Key_IOSet) IOSet.open(_initialMaxCount, sks);
        } else {
            sks = (IOSet_Key_IOSet) IOSet.open(sks, bfp);
        }
        _.out("SIO: " + _sksBFP + " Ready." + bfp);
        return sks;
    }
    
    private int initialSize = 4;
    private int sioKeySize;
    private int sioValueSize;
    
    public IOSet_Key_IOSet(ChunkFiler _blocks, int _keySize, int _sioKeySize, int _sioValueSize) {
        super(_blocks, _keySize, 8);
        sioKeySize = _sioKeySize;
        sioValueSize = _sioValueSize;
    }
    
    public synchronized IOSet getSIO(final byte[] _key) throws Exception {
        synchronized (setIndexLock) {
            IOSet set = null;
            byte[] got = UIOSet.get(this, _key);
            long fp = (got == null) ? -1 : UIO.bytesLong(got);
            if (fp != -1) {
                set = IOSet.lookup(chunks, fp);
            }
            if (set == null) {
                set = new IOSet_Key_Value(chunks, sioKeySize, sioValueSize) {
                    @Override
                    public void relocated(long _bfp) throws Exception {
                        super.relocated(_bfp);
                        UIOSet.add(IOSet_Key_IOSet.this, UByte.join(_key, UIO.longBytes(_bfp)));
                    }
                };
                if (fp == -1) {
                    set = IOSet.open(initialSize, set);
                } else {
                    set = IOSet.open(set, fp);
                }
            }
            return set;
        }
    }
    
    public synchronized IOSet hasSIO(final byte[] _key) throws Exception {
        synchronized (setIndexLock) {
            IOSet set = null;
            byte[] got = UIOSet.get(this, _key);
            long fp = (got == null) ? -1 : UIO.bytesLong(got);
            if (fp != -1) {
                set = IOSet.lookup(chunks, fp);
                if (set == null) {
                    set = new IOSet_Key_Value(chunks, sioKeySize, sioValueSize) {
                        @Override
                        public void relocated(long _bfp) throws Exception {
                            super.relocated(_bfp);
                            UIOSet.add(IOSet_Key_IOSet.this, UByte.join(_key, UIO.longBytes(_bfp)));
                        }
                    };
                    set = IOSet.open(set, fp);
                }
            }
            return set;
        }
    }
    
    // Supports Equivelent of contains
    public synchronized boolean hasSIO(final IOSet _parent, final byte[] _key) throws Exception {
        synchronized (setIndexLock) {
            byte[] got = UIOSet.get(_parent, _key);
            long fp = (got == null) ? -1 : UIO.bytesLong(got);
            if (fp != -1) {
                return true;
            }
            return false;
        }
    }
    
    public synchronized void removeSIO(byte[] _key) throws Exception {
        byte[] value = UIOSet.get(this, _key);
        if (value == null) {
            return;
        }
        IOSet sio = hasSIO(_key);
        if (sio != null) UIOSet.remove(this, _key);
    }
    
}

