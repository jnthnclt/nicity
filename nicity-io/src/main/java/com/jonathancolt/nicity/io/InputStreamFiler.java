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
import java.io.InputStream;

public class InputStreamFiler extends InputStream {
    private IFiler filer;
    public InputStreamFiler(IFiler _filer) throws IOException {
        filer = _filer;
    }
    public int read() throws IOException {
        return filer.read();
    }
    @Override
    public int read(byte b[]) throws IOException {
        return filer.read(b);
    }
    @Override
    public int read(byte b[], int off, int len) throws IOException {
        return filer.read(b, off, len);
    }
    @Override
    public long skip(long n) throws IOException {
        return filer.skip(n);
    }
    @Override
    public void close() throws IOException {
        filer.close();
    }
    public void flush() throws IOException {
    }
    long mfp = 0;
    @Override
    public synchronized void mark(int readlimit) {
        try
        {
            mfp = filer.getFilePointer();
        }
        catch (Exception x)
        {
        }
    }
    @Override
    public synchronized void reset() throws IOException {
        filer.seek(mfp);
    }
    @Override
    public boolean markSupported() {
        return true;
    }
}