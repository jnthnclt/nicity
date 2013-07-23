/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonathancolt.nicity.io.zip;

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
