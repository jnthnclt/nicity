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

import com.jonathancolt.nicity.core.collection.CSet;
import com.jonathancolt.nicity.core.collection.keyed.KeyedValue;
import com.jonathancolt.nicity.io.UFile;
import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.lang.SysOut;
import com.jonathancolt.nicity.core.lang.UString;
import com.jonathancolt.nicity.core.lang.UText;
import com.jonathancolt.nicity.core.time.MilliTimer;
import com.jonathancolt.nicity.io.collection.ChunkFiler;
import java.io.File;

public class ByteKeyToIntKey_Test {
    public static void main(final String[] _args) {

        new Thread() {
            @Override
            public void run() {
                IOut _ = new SysOut();
                try {


                    Root_FPs rootFPs = new Root_FPs("RootFPs", 3);//??

                    ChunkFiler setBlockFile = ChunkFiler.newInstance("ByteToIntDSet");//Disk
                    ChunkFiler dataBlockFile = ChunkFiler.newInstance("ByteToIntDData");//Disk

                    BytesToProbabilisticKey btp = BytesToProbabilisticKey.factory(
                            _, 8, dataBlockFile,
                            rootFPs.getRootFP(0),
                            setBlockFile);

                    ByteKeyToIntKey bti = ByteKeyToIntKey.factory(
                            _, btp.byteKeySize(),
                            rootFPs.getRootFP(1),
                            setBlockFile,
                            rootFPs.getRootFP(2),
                            setBlockFile);



                    CSet<KeyedValue<Object,byte[]>> wordToKey = new CSet<KeyedValue<Object,byte[]>>();
                    CSet<KeyedValue<Object,Integer>> keyToID = new CSet<KeyedValue<Object,Integer>>();

                    int count = 0;
                    int idCount = 0;
                    int collisionCount = 0;
                    File[] all = UFile.allFiles(_, new File("C:/JavaProjects/ColtProjects/cdk-lib"));

                    MilliTimer sw = new MilliTimer();
                    sw.reset();
                    sw.start();
                    for (int a = 0; a < all.length; a++) {
                        if (!"java".equals(UFile.getExtension(all[a].toString()))) {
                            continue;
                        }
                        _.out(a, all.length);
                        _.out(all[a]);
                        count++;
                        String f = UText.toString(all[a]);
                        String[] tokens = UString.tokenize(_, f);
                        int[] ids = new int[tokens.length];
                        for (int t = 0; t < tokens.length; t++) {
                            if (t % 1000 == 0) {
                                _.out(t, tokens.length);
                                _.out(t);
                            }
                            ids[t] = -1;
                            byte[] key = KeyedValue.get(wordToKey,tokens[t]);
                            if (key == null) {
                                byte[] payload = tokens[t].getBytes();
                                key = btp.toByteKey(payload);
                                if (key == null) {
                                    collisionCount++;
                                    continue;
                                }
                                KeyedValue.add(wordToKey,tokens[t], key);
                            }
                            
                            Integer id = KeyedValue.get(keyToID,key);
                            if (id == null) {
                                id = new Integer(bti.addByteKey(key));
                                keyToID.add(new KeyedValue(key,id));
                                System.out.println(id + " " + tokens[t]);
                                idCount++;
                            }
                            ids[t] = id.intValue();
                        }

                    }
                    sw.stop();
                    System.out.println("Load msecs = " + sw.duration() + " ");
                    System.out.println("File Count = " + count + " ");
                    System.out.println("Word Count = " + idCount + " ");
                    System.out.println("Collisions = " + collisionCount + " ");


                } catch (Exception x) {
                    x.printStackTrace();
                    _.out(x);
                }
                System.exit(0);

            }
        }.start();

    }
}
