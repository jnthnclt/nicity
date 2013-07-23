package com.jonathancolt.nicity.io.collection.set;

import com.jonathancolt.nicity.core.io.UIO;
import com.jonathancolt.nicity.core.lang.ICallback;
import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.lang.SysOut;
import com.jonathancolt.nicity.core.lang.URandom;
import com.jonathancolt.nicity.io.collection.ChunkFiler;

public class IOSet_Key_IOSet_Test {
    public static void main(final String[] _args) {

        new Thread() {
            @Override
            public void run() {
                IOut _ = new SysOut();
                try {

                    Root_FPs rootFPs = new Root_FPs("RootFPs", 1);//??
                    ChunkFiler chunks = ChunkFiler.factory("SIO_Key_SIO");//Disk
                    IOSet_Key_IOSet sks = IOSet_Key_IOSet.factory(_, rootFPs.getRootFP(0), 8, 8, 0, 2, chunks);

                    int sets = 1000;

                    //adding
                    for (long i = 1; i < sets; i++) {
                        IOSet sio = sks.getSIO(UIO.longBytes(1+URandom.rand(100)));
                        for (long j = 1; j < URandom.rand(10); j++) {
                            UIOSet.add(sio, UIO.longBytes(j));
                        }
                        System.out.println("Count:" + sio);
                    }

                    // reading
                    for (long i = 1; i < sets; i++) {
                        IOSet sio = sks.getSIO(UIO.longBytes(i+URandom.rand(100)));
                        System.out.println(sio + "->");
                        System.out.println(UIOSet.contains(sio, UIO.longBytes(i)));
                        sio.backcall(_,new ICallback() {
                            public Object callback(Object _value) {
                                System.out.print(UIO.bytesLong((byte[]) _value) + ",");
                                return _value;
                            }
                        });
                        System.out.println();
                    }
                    //removing
                    for (long i = 1; i < sets; i++) {
                        byte[] siok = UIO.longBytes(1+URandom.rand(100));
                        IOSet sio = sks.hasSIO(siok);
                        if (sio != null) {
                            for (long j = 1; j < URandom.rand(10); j++) {
                                UIOSet.remove(sio, UIO.longBytes(j));
                                System.out.println("Removed Value "+j);
                            }
                            if (sio.getCount() == 0) {
                                sks.removeSIO(siok);
                                System.out.println("Removed Set");
                            }
                        }

                    }

                   

                } catch (Exception x) {
                    _.out(x);
                }
                System.exit(0);
            }
        }.start();
    }
}
