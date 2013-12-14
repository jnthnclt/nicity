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
