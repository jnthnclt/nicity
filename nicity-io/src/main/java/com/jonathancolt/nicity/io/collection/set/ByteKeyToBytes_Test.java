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

import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.lang.SysOut;

public class ByteKeyToBytes_Test {
    public static void main(final String[] _args) {

        new Thread() {
            @Override
            public void run() {
                IOut _ = new SysOut();
                try {
                    /*

                    Root_FPs rootFPs = new Root_FPs("RootFPs", 3);//??
                    ChunkFiler setChunkFiler = ChunkFiler.newInstance("ByteKetToBytesSet");//Disk
                    ChunkFiler dataChunkFiler = ChunkFiler.newInstance("ByteKetToBytesData");//Disk

                    ByteKeyToBytes btb = ByteKeyToBytes.factory(
                            _, 8, dataChunkFiler,
                            PreferenceValue.value(preferences, "ByteKetToBytesSetBFP"),
                            setChunkFiler);

                    String[] strings = new String[]{
                        "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"
                    };
                    if (_args.length > 0) {
                        strings = UText.loadTextFile(new File(_args[0]));
                    }
                    System.out.println(strings.length);
                    long collisionCount = 0;
                    long uniqueCount = 0;
                    for (int i = 0; i < strings.length; i++) {
                        if (_.canceled()) {
                            break;
                        }
                        _.out(strings[i]);
                        _.out(i, strings.length);
                        if (strings[i] == null || strings[i].length() == 0) {
                            continue;
                        }
                        byte[] payload = strings[i].getBytes();
                        byte[] id = btb.toID(payload);
                        if (id != null) {
                            byte[] _payload = btb.toBytes(id);
                            //System.out.println(strings[i]+" -> "+new String(_payload));
                            uniqueCount++;
                        } else {
                            id = btb.toKey(payload);
                            byte[] _payload = btb.toBytes(id);
                            for (int p = 0; p < id.length; p++) {
                                System.out.print(id[p]);
                            }
                            System.out.println(" " + strings[i] + " -> " + new String(_payload));
                            collisionCount++;
                        }
                    }
                    System.out.println(uniqueCount + " " + collisionCount);
                    */


                } catch (Exception x) {
                    x.printStackTrace();
                    _.out(x);
                }
                System.exit(0);

            }
        }.start();

    }
}
