package com.jonathancolt.nicity.io;

import com.jonathancolt.nicity.core.io.IFiler;
import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamFiler extends OutputStream {

    private IFiler filer;

    public OutputStreamFiler(IFiler _filer) {
        filer = _filer;
    }

    public void eof() throws IOException {
        filer.eof();
    }

    @Override
    public void write(int b) throws IOException {
        filer.write(b);
    }

    @Override
    public void write(byte b[]) throws IOException {
        filer.write(b);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        filer.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
        filer.close();
    }
}
