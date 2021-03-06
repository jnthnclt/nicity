package com.jonathancolt.nicity.io;

/*
 * #%L
 * nicity-io
 * %%
 * Copyright (C) 2013 Jonathan Colt
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.jonathancolt.nicity.core.io.IFiler;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * All of the methods are intentionally left unsynchronized. Up to caller to do
 * the right thing using the Object returned by lock()
 *
 */
public class DiskFiler extends RandomAccessFile implements IFiler {
    public static long totalFilesOpenCount;
    public static long totalReadByteCount;
    public static long totalWriteByteCount;
    public static long totalSeeksCount;
    public long readByteCount;
    public long writeByteCount;
    String fileName;
    public DiskFiler(String name, String mode) throws IOException {
        super(name, mode);
        fileName = name;
    }
    public DiskFiler(File file, String mode) throws IOException {
        super(file.getPath(), mode);
        fileName = file.getPath();
    }
    @Override
    public String toString() {
        try {
            return "R:" + (readByteCount / 1024) + "kb W:" + (writeByteCount / 1024) + "kb " + fileName + " " + (length() / 1024) + "kb";
        } catch (Exception x) {
            return "R:" + (readByteCount / 1024) + "kb W:" + (writeByteCount / 1024) + "kb " + fileName;
        }
    }
    @Override
    public void close() throws IOException {
        super.close();
    }
    public Object lock() {
        return this;
    }
    public long skip(long position) throws IOException {
        long fp = getFilePointer();
        if (position == 0) {
            return fp;
        } else if (position < 0) {
            if (-position > fp) {
                throw new IOException("Skipped off the end of the beginning");
            }
            seek(fp + position);
        } else {
            if (Long.MAX_VALUE - fp > position) {
                seek(fp + position);
            } else {
                throw new IOException("Skipped off the end of the World");
            }
        }
        return getFilePointer();
    }
    @Override
    public void seek(long _fp) throws IOException {
        totalSeeksCount++;
        super.seek(_fp);
    }
    @Override
    public int read() throws IOException {
        readByteCount++;
        totalReadByteCount++;
        return super.read();
    }
    @Override
    public int read(byte b[]) throws IOException {
        int off = 0;
        int len = b.length;
        int n = 0;
        while (n < len) {
            int count = super.read(b, off + n, len - n);
            if (count < 0) {
                len = n;
                if (n == 0) {
                    len = -1;
                }
                break;
            }
            n += count;
        }
        readByteCount += len;
        totalReadByteCount += len;
        return len;
    }
    @Override
    public int read(byte b[], int _offset, int _len) throws IOException {
        int len = super.read(b, _offset, _len);
        readByteCount += len;
        totalReadByteCount += len;
        return len;
    }
    @Override
    public void write(int b) throws IOException {
        writeByteCount++;
        totalWriteByteCount++;
        super.write(b);
    }
    @Override
    public void write(byte[] b) throws IOException {
        if (b == null) {
            writeByteCount += b.length;
            totalWriteByteCount += b.length;
        }
        super.write(b);
    }
    @Override
    public void write(byte[] b, int _offset, int _len) throws IOException {
        super.write(b, _offset, _len);
        writeByteCount += _len;
        totalWriteByteCount += _len;
    }
    public void eof() throws IOException {
        setLength(getFilePointer());
    }
    public void flush() throws IOException {
    }
}
