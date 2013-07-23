package com.jonathancolt.nicity.io.collection.set;

import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.lang.SysOut;
import com.jonathancolt.nicity.core.lang.UText;
import com.jonathancolt.nicity.core.time.MilliTimer;
import com.jonathancolt.nicity.io.collection.ChunkFiler;
import java.io.File;

public class BytesToProbabilisticKey_Test {
    public static void main(final String[] _args) {

        new Thread() {
            @Override
            public void run() {
                IOut _ = new SysOut();
                try {

                    Root_FPs rootFPs = new Root_FPs("RootFPs", 2);//??

                    ChunkFiler setBlockFile = ChunkFiler.newInstance("ByteToByteIDSet");//Disk

                    BytesToProbabilisticKey btb = BytesToProbabilisticKey.factory(
                            _, 8, setBlockFile,
                            rootFPs.getRootFP(0),
                            setBlockFile);

                    String[] strings = new String[]{
                        "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"
                    };
                    if (_args.length > 0) {
                        strings = UText.loadTextFile(new File(_args[0]));
                    }
                    System.out.println(strings.length);
                    MilliTimer sw = new MilliTimer();
                    sw.start();
                    long collisionCount = 0;
                    long uniqueCount = 0;
                    for (int i = 0; i < strings.length; i++) {
                        if (i % 5000 == 0) {
                            if (_.canceled()) {
                                break;
                            }
                            _.out(strings[i]);
                            _.out(i, strings.length);
                        }
                        if (strings[i] == null || strings[i].length() == 0) {
                            continue;
                        }
                        byte[] payload = strings[i].getBytes();
                        byte[] id = btb.toByteKey(payload);
                        if (id != null) {
                            byte[] _payload = btb.toBytes(id);
                            System.out.println(strings[i]+" -> "+new String(_payload));
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
                    sw.stop();
                    System.out.println(sw + " Unique" + uniqueCount + " Collisions:" + collisionCount);


                } catch (Exception x) {
                    x.printStackTrace();
                    _.out(x);
                }
                System.exit(0);

            }
        }.start();

    }
}
