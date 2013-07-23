package com.jonathancolt.nicity.io.collection.set;

import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.lang.SysOut;
import com.jonathancolt.nicity.core.lang.URandom;
import com.jonathancolt.nicity.io.collection.ChunkFiler;

public class IOSet_Key_Value_Test {
    public static void main(final String[] _arg) {

        new Thread() {
            @Override
            public void run() {
                IOut _ = new SysOut();
                try {

                    Root_FPs rootFPs = new Root_FPs("RootFPs",2);//??
                    ChunkFiler chunks = ChunkFiler.factory("SIO_Key_Value");//Disk
                    IOSet set = IOSet_Key_Value.factory(rootFPs.getRootFP(0), 8, 8, 2, chunks);

                    int run = URandom.rand(256);
                    double m = URandom.rand(1d);
                    if (m < 0.3d) {
                        System.out.println("Write...."+run);
                        for (int i = 0; i < run; i++) {
                            byte[] record = new byte[8 + 8];
                            for (int r = 0; r < 8; r++) {
                                record[r] = (byte)URandom.rand(256);
                            }
                            for (int r = 8; r < 16; r++) {
                                record[r] = (byte) i;
                            }
                            byte[] id = UIOSet.add(set, record);
                            for (int t = 0; t < id.length; t++) {
                                System.out.println("w="+id[t] + " " + record[t]);
                            }
                        }
                    }
                    else if (m < 0.6d) {
                        System.out.println("Read...."+run);
                        for (int i = 0; i < run; i++) {
                            byte[] key = new byte[16];
                            for (int r = 0; r < key.length; r++) {
                                key[r] = (byte) URandom.rand(256);
                            }
                            byte[] value = UIOSet.get(set, key);
                            if (value != null) {
                                for (int t = 0; t < value.length; t++) {
                                    System.out.println("r="+value[t] + " " + key[t]);
                                }
                            }
                        }
                    }
                    else {
                        System.out.println("Remove...."+run);
                        for (int i = 0; i < run; i++) {
                            byte[] key = new byte[16];
                            for (int r = 0; r < key.length; r++) {
                                key[r] = (byte) URandom.rand(256);
                            }
                            if (UIOSet.remove(set, key)) {
                                System.out.println(key);
                            }
                        }
                    }
                    
                    

                } catch (Exception x) {
                    x.printStackTrace();
                    _.out(x);
                }
                System.exit(0);

            }
        }.start();

    }
}
