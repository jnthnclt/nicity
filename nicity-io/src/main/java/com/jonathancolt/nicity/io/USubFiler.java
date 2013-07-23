package com.jonathancolt.nicity.io;

import com.jonathancolt.nicity.core.lang.OrderedKeys;

public class USubFiler {
    public static OrderedKeys key(SubFilers _sfs, long _startOfFP, long _endOfFP) {
        try {
            return new OrderedKeys(_sfs, _startOfFP, _endOfFP);
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    // if buffersize < 1 means as fast as you can
    public static void transfer(SubFiler from, SubFiler to, long _bufferSize) throws Exception {
        long minCount  = from.getSize();
        long byteCount = _bufferSize;

        if (_bufferSize < 1) {
            byteCount = 1024 * 1024;    // 1MB
        }

        if (minCount < byteCount) {
            byteCount = minCount;
        }

        byte[] chunk      = new byte[(int) byteCount];
        long   chunkCount = minCount / byteCount;

        for (long i = 0; i < chunkCount; i++) {
            synchronized (from.lock()) {
                from.seek(i * byteCount);
                from.read(chunk);
            }

            synchronized (to.lock()) {
                to.seek(i * byteCount);
                to.write(chunk);
            }
        }

        long remainderCount = minCount - (chunkCount * byteCount);

        if (remainderCount != 0) {
            chunk = new byte[(int) remainderCount];

            synchronized (from.lock()) {
                from.seek(chunkCount * byteCount);
                from.read(chunk);
            }

            synchronized (to.lock()) {
                to.seek(chunkCount * byteCount);
                to.write(chunk);
            }
        }
    }
}
