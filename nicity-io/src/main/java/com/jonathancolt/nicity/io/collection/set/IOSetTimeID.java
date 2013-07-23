package com.jonathancolt.nicity.io.collection.set;

import com.jonathancolt.nicity.core.io.UIO;

public class IOSetTimeID implements ISIO_ID {
    public static IOSetTimeID sio_id = new IOSetTimeID();
    final private Object timeLock = new Object();
    private long lastTime = 0;
    private long nanos = 0;
    public IOSetTimeID() {
    }
    public int size() {
        return 8 + 8;
    }
    public byte[] id() {
        synchronized (timeLock) {
            long time = System.currentTimeMillis();
            if (time <= lastTime) {
                nanos++;
            } else {
                nanos = 0;
            }
            lastTime = time;
            return UIO.longsBytes(new long[]{time, nanos});
        }
    }
}

