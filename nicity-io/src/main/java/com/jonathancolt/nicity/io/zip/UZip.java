/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonathancolt.nicity.io.zip;

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

import com.jonathancolt.nicity.io.MemoryFiler;
import com.jonathancolt.nicity.io.OutputStreamFiler;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UZip {

    public static byte[] zip(byte[] _data, String _name) {

        try {
            MemoryFiler out = new MemoryFiler();
            ZipOutputStream zipOut = new ZipOutputStream(new OutputStreamFiler(out));
            zipOut.setLevel(Deflater.BEST_COMPRESSION);
            MemoryFiler f = new MemoryFiler(_data);
            ZipEntry ze = new ZipEntry("a");
            zipOut.putNextEntry(ze);

            int i = -1;
            byte[] buf = new byte[1024 * 100];
            while (( i = f.read(buf) ) > 0) {
                zipOut.write(buf, 0, i);
            }
            f.close();
            zipOut.closeEntry();
            zipOut.close();
            return out.getBytes();
        }
        catch (Exception x) {
            x.printStackTrace();
            return new byte[0];
        }
    }
}
