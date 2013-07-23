package com.jonathancolt.nicity.io.collection.set;

import com.jonathancolt.nicity.core.io.UIO;
import com.jonathancolt.nicity.io.collection.StackFiler;

public class Root_FPs {
    final static private int fpSize = 8;//truely a constant
    StackFiler rootFP;
    public Root_FPs(String _name,int _batchSize) throws Exception {
        rootFP = StackFiler.factory(_name, fpSize, _batchSize, 0, UIO.longBytes(-1));
    }
    public RootFP getRootFP(int _i) {
        return new RootFP(this, _i);
    }
    synchronized public long get(int _i) throws Exception {
        long fp = UIO.bytesLong(rootFP.get(_i));
        System.out.println("Get FP=" + fp);
        return fp;
    }
    synchronized public void set(long _fp, int _i) throws Exception {
        System.out.println("Set FP=" + _fp);
        rootFP.set(_i, UIO.longBytes(_fp));
    }
}
