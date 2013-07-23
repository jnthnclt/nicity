package com.jonathancolt.nicity.io.collection;

import com.jonathancolt.nicity.io.SubFiler;

public class UChunkFiler {


    // if buffersize < 1 means as fast as you can
    static public boolean copy(ChunkFiler _from,long _fromChunkFP,ChunkFiler _to,long _toChunkFP, long _bufferSize, long _size) throws Exception {

        SubFiler from = _from.getFiler(_fromChunkFP);
        SubFiler to = _to.getFiler(_toChunkFP);

        long hack = (_size / 2) + 24;
        long fromCount = hack; // from.getSize()-1;//!!dg because sometimes from.getSize()-1 gets EOF exception during copy
        long toCount = hack; // to.getSize()-1;

        if (fromCount > 0) {
            if (toCount < 1) {
                toCount = fromCount;
            }

            long minCount = Math.min(fromCount, toCount);
            if (minCount > to.getSize()) {
                throw new Exception("Overflow: copying bigger into smaller");
            }


            long byteCount = _bufferSize;
            if (_bufferSize < 1) {
                byteCount = 1024 * 1024;//1MB
            }
            if (minCount < byteCount) {
                byteCount = minCount;
            }


            byte[] chunk = new byte[(int) byteCount];
            long chunkCount = minCount / byteCount;

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
        return true;
    }
}
