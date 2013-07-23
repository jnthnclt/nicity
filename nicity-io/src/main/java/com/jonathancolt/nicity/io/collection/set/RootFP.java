package com.jonathancolt.nicity.io.collection.set;

public class RootFP {
    private Root_FPs rootFPs;
    private int index;
    public RootFP(Root_FPs _rootFPs,int _index) {
        rootFPs = _rootFPs;
        index = _index;
    }
    public long getLong() throws Exception { return rootFPs.get(index); }
    public void setLong(long _fp) throws Exception  { rootFPs.set(_fp,index); }
}
