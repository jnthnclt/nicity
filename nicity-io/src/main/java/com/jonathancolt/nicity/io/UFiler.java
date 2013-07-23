package com.jonathancolt.nicity.io;

import com.jonathancolt.nicity.core.io.UIO;
import com.jonathancolt.nicity.core.io.IFiler;
import com.jonathancolt.nicity.core.lang.IOut;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;

public class UFiler {
    // Expects IFiler to already be buffered
    public static void copyTo(IOut _, File _file, IFiler _to) throws Exception {
        long l = _file.length();
        UIO.writeLong(_to, l, "Length");
        if (l > 0) {
            InputStreamFiler fs = new InputStreamFiler(Filer.open(_file, IFiler.cRead));
            BufferedInputStream _from = new BufferedInputStream(fs, 16384);
            for (long i = 0; i < l; i++) {
                if (i % 1024 == 0) {
                    _.out(i, l);
                }
                _to.write(_from.read());
            }
            _from.close();
        }
    }
    // Expects IFiler to already be buffered
    public static void copyTo(IOut _, IFiler _from, File _file) throws Exception {
        File parent = _file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        long l = UIO.readLong(_from, "Length");
        OutputStreamFiler of = new OutputStreamFiler(Filer.open(_file, IFiler.cReadWrite));
        of.eof();
        if (l > 0) {
            BufferedOutputStream _to = new BufferedOutputStream(of, 16384);
            for (long i = 0; i < l; i++) {
                if (i % 1024 == 0) {
                    _.out(i, l);
                }
                _to.write(_from.read());
            }
            _to.close();
        }
    }
}
