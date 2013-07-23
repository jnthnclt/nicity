package com.jonathancolt.nicity.io;

import com.jonathancolt.nicity.core.io.IFiler;
import com.jonathancolt.nicity.core.lang.ASetObject;
import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.lang.OrderedKeys;
import java.io.IOException;
import java.util.Arrays;

/*
 * This class segments a single Filer into segment filers where
 * each segment filer restates fp = 0. It only allows one segment filer
 * at a time to be in control. It is the responsibility of the
 * programmer to remove the segment filers as the become stale.
 */
public class SubFiler extends ASetObject implements IFiler {

    private SubFilers subFilers;
    private IFiler filer;
    private long startOfFP;
    private long count;
    private long endOfFP;
    private OrderedKeys key;

    public SubFiler(
            SubFilers _subFilers, long _startOfFP, long _endOfFP, long _count) {
        subFilers = _subFilers;
        filer = _subFilers.filer;
        startOfFP = _startOfFP;
        endOfFP = _endOfFP;
        key = USubFiler.key(subFilers, _startOfFP, _endOfFP);
        count = _count;
    }

    @Override
    final public Object hashObject() {
        return key;
    }

    @Override
    public Object lock() {
        return subFilers;
    }

    @Override
    public String toString() {
        return "SOF=" + startOfFP + " EOF:" + endOfFP + " Count=" + count;
    }

    final public long getSize() throws IOException {
        if (isFileBacked()) {
            return length();
        }
        return endOfFP - startOfFP;
    }

    final public boolean isFileBacked() {
        return (endOfFP == Long.MAX_VALUE);
    }

    final public byte[] toBytes() throws IOException {
        synchronized (lock()) {
            byte[] b = new byte[(int) count];
            seek(0);
            read(b);
            return b;
        }
    }

    public void setBytes(byte[] _bytes) throws IOException {
        synchronized (lock()) {
            seek(0);
            write(_bytes);
            count = _bytes.length;
        }
    }

    final public void fill(IOut _, byte _v) throws IOException {
        byte[] zerosMax = new byte[(int) Math.pow(2, 16)]; // 65536 max used until min needed
        Arrays.fill(zerosMax, _v);
        synchronized (lock()) {
            seek(0);//!! optimize
            long segmentLength = getSize();
            long size = segmentLength;
            while (size + zerosMax.length < segmentLength) {
                write(zerosMax);
                size += zerosMax.length;
            }
            for (long i = size; i < segmentLength; i++) {
                write(_v);
            }
        }
    }

    final public boolean validFilePostion(long _position) {
        return (endOfFP - startOfFP) < _position;
    }

    final public long startOfFP() {
        return startOfFP;
    }

    final public long count() {
        return count;
    }

    final public long endOfFP() {
        return endOfFP;
    }

    @Override
    final public int read() throws IOException {
        return filer.read();
    }

    @Override
    final public int read(byte[] b) throws IOException {
        return filer.read(b);
    }

    @Override
    public synchronized int read(byte b[], int _offset, int _len) throws IOException {
        return filer.read(b, _offset, _len);
    }

    @Override
    final public void write(int b) throws IOException {
        filer.write(b);
    }

    @Override
    final public void write(byte[] b) throws IOException {

        filer.write(b);
    }

    @Override
    public synchronized void write(byte b[], int _offset, int _len) throws IOException {
        filer.write(b, _offset, _len);
    }

    @Override
    final public void seek(long position) throws IOException {
        synchronized (lock()) {
            if (position > endOfFP - startOfFP) {
                throw new IOException("seek overflow " + position + " " + this);
            }
            filer.seek(startOfFP + position);
        }
    }

    @Override
    final public long skip(long position) throws IOException {

        return filer.skip(position);
    }

    @Override
    final public long length() throws IOException {

        if (isFileBacked()) {
            return filer.length();
        }
        return endOfFP - startOfFP;
    }

    @Override
    final public void setLength(long len) throws IOException {

        if (isFileBacked()) {
            filer.setLength(len);
        } else {
            throw new IOException("try to modified a fixed length filer");
        }
    }

    @Override
    final public long getFilePointer() throws IOException {

        long fp = filer.getFilePointer();
        if (fp < startOfFP) {
            throw new IOException("seek misalignment " + fp + " < " + startOfFP);
        }
        if (fp > endOfFP) {
            throw new IOException("seek misalignment " + fp + " > " + endOfFP);
        }
        return fp - startOfFP;
    }

    @Override
    final public void eof() throws IOException {
        synchronized (lock()) {

            if (isFileBacked()) {
                filer.eof();
            } else {
                filer.seek(endOfFP - startOfFP);
            }
        }
    }

    @Override
    final public void close() throws IOException {

        if (isFileBacked()) {
            filer.close();
        }
    }

    @Override
    final public void flush() throws IOException {

        filer.flush();
    }

    public SubFiler get(long _startOfFP, long _endOfFP, long _count) {
        return subFilers.get(_startOfFP, _endOfFP, _count);
    }
}
