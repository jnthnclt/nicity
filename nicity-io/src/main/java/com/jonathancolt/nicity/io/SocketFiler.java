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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketFiler implements IFiler {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    public static long totalReadByteCount;
    public static long totalWriteByteCount;
    public long readByteCount;
    public long writeByteCount;
    public SocketFiler(Socket _socket, long _readTimeOut) throws IOException {

        try {
            socket = _socket;
            socket.setSoTimeout((int) _readTimeOut);
            //try {
            //    socket.setTcpNoDelay(true);
            //} catch (Exception x) {}
            in = new BufferedInputStream(socket.getInputStream(), socket.getReceiveBufferSize());
            out = new BufferedOutputStream(socket.getOutputStream(), socket.getSendBufferSize());
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error creating socket connection");
        }
    }
    
    public void closeIO() {
        try {
            close();
        } catch (Throwable x) {
        }
    }
    
    public boolean isClosed() {
        try {
            return isIOClosed();
        } catch (Throwable x) {
            return true;
        }
    }
    
    public boolean isIOClosed() throws Exception {
        return socket.isClosed();
    }
    
    public Object lock() {
        return this;
    }
    
    public int read() throws IOException {
        readByteCount++;
        totalReadByteCount++;
        return in.read();
    }
    
    public int read(byte b[]) throws IOException {
        //if (readTrace) { readTrace = false; UTrace.traceCaller(100); }
        int off = 0;
        int len = b.length;
        int n = 0;
        while (n < len) {
            int count = in.read(b, off + n, len - n);
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
    
    public int read(byte b[], int _offset, int _len) throws IOException {
        int len = in.read(b, _offset, _len);
        readByteCount += len;
        totalReadByteCount += len;
        return len;
    }
    
    public void write(int b) throws IOException {
        writeByteCount++;
        totalWriteByteCount++;
        out.write(b);
    }
    
    public void write(byte b[]) throws IOException {
        if (b != null) {
            writeByteCount += b.length;
            totalWriteByteCount += b.length;
        }
        out.write(b);
    }
    
    public void write(byte b[], int _offset, int _len) throws IOException {
        if (b != null) {
            writeByteCount += b.length;
            totalWriteByteCount += b.length;
        }
        out.write(b, _offset, _len);
    }
    
    // !! may remove some of the below
    public void seek(long position) throws IOException {
        throw new IOException("Not supported");
    }
    public long skip(long position) throws IOException {
        throw new IOException("Not supported");
    }
    public long length() throws IOException {
        throw new IOException("Not supported");
    }
    public void setLength(long len) throws IOException {
        throw new IOException("Not supported");
    }
    public long getFilePointer() throws IOException {
        throw new IOException("Not supported");
    }
    public void eof() throws IOException {
        throw new IOException("Not supported");
    }
    public void close() throws IOException {
        IOException xs = null;
        try {
            in.close();
        } catch (Exception x) {
            xs = new IOException();
        }
        try {
            out.close();
        } catch (Exception x) {
            xs = new IOException();
        }
        try {
            socket.close();
        } catch (Exception x) {
            xs = new IOException();
        }
        if (xs != null) {
            throw xs;
        }
    }
    public void flush() throws IOException {
        out.flush();
    }
}
