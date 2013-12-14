package com.jonathancolt.nicity.io.collection.set;

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
import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.lang.SysOut;
import com.jonathancolt.nicity.core.lang.UByte;
import com.jonathancolt.nicity.core.lang.URandom;
import com.jonathancolt.nicity.io.collection.ChunkFiler;

public class IOSet_Bytes_Test {
    
    public static void main(final String[] _arg) {

        new Thread() {
            @Override
            public void run() {
                IOut _ = new SysOut();
                try {
                    //SIO_TimeID sio_id = new SIO_TimeID();
                    IOSetLongID sio_id = new IOSetLongID("SIOBytesIds", 1000);

                    ChunkFiler data = ChunkFiler.newInstance("SIOBytesData");//Disk

                    Root_FPs rootFPs = new Root_FPs("SIOBytesRootFPs", 2);//??

                    final IOSet_Key_Value idToFp = IOSet_Key_Value.factory(rootFPs.getRootFP(0), sio_id.size(), 8, 2, data);

                    IOSet_ID_BFP sio_id_bfp = new IOSet_ID_BFP() {
                        @Override
                        public void add(byte[] _id, long _bfp) throws Exception {
                            UIOSet.add(idToFp, UByte.join(_id, UIO.longBytes(_bfp)));
                        }
                    };
                    IOSet set = IOSet_Bytes.factory(
                        rootFPs.getRootFP(1),
                        2, data, sio_id, data,
                        sio_id_bfp
                    );


                    int run = URandom.rand(256);
                    for (int i = 0; i < run; i++) {
                        byte[] record = new byte[5];
                        for (int r = 0; r < record.length; r++) {
                            record[r] = (byte) (65 + URandom.rand(2));
                        }
                        byte[] id = UIOSet.add(set, record);
                        System.out.println(i+" Added:"+UIO.bytesLong(id)+"\t-\t"+new String(record));
                    }
                    System.out.println(set);
                    for (int i = 0; i < run; i++) {
                        byte[] record = new byte[5];
                        for (int r = 0; r < record.length; r++) {
                            record[r] = (byte) (65 + URandom.rand(2));
                        }
                        byte[] id = UIOSet.get(set, record);
                        if (id != null) {
                            System.out.println(i + " Got:" +UIO.bytesLong(id) + "\t-\t" + new String(record));
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
