/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonathancolt.nicity.io.collection;

import com.jonathancolt.nicity.core.collection.CSetOfLongs;
import com.jonathancolt.nicity.core.lang.ICallback;
import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.lang.SysOut;
import com.jonathancolt.nicity.core.lang.URandom;
import com.jonathancolt.nicity.core.time.MilliTimer;
import com.jonathancolt.nicity.io.SubFiler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class ChunkFilerTest {

    public ChunkFilerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of setup method, of class ChunkFiler.
     */
    @Test
    public void testSetup() throws Exception {
        IOut _ = new SysOut();

        String name = "ChunkFilerTest";//_arg[0];
        int it = 100;//Integer.parseInt(_arg[1]);

        System.out.println("Name: " + name);
        System.out.println("Iteration: " + it);

        final ChunkFiler chunks = ChunkFiler.factory(name);

        MilliTimer t = new MilliTimer();
        t.start();


        CSetOfLongs fps = new CSetOfLongs();
        System.out.println("Creating...");
        for (int i = 0; i < URandom.rand(it); i++) {
            byte[] randChunk = new byte[URandom.rand(1024)];
            URandom.fill(randChunk, 0, randChunk.length);
            long fp = chunks.newChunk(randChunk.length);
            chunks.getFiler(fp).setBytes(randChunk);
            fps.add(fp);
            System.out.println("fp=" + fp);
        }

        System.out.println("Reading...");
        long[] _fps = fps.getAll();
        for (long fp : _fps) {
            System.out.print("reading fp=" + fp + "....");
            byte[] data = chunks.getFiler(fp).toBytes();
            System.out.println(data.length);
        }

        System.out.println("Removing...");
        for (long fp : _fps) {
            if (URandom.rand(1d) < 0.5d) {
                chunks.remove(fp);
                System.out.println("Removed:" + fp);
            }
        }

        System.out.println("Listing...");
        chunks.allChunks(_, new ICallback<Long, Long>() {

            @Override
            public Long callback(Long _value) {
                System.out.print("fp=" + _value);
                try {
                    SubFiler filer = chunks.getFiler(_value);
                    System.out.println(filer);
                }
                catch (Exception x) {
                    x.printStackTrace();
                }
                return _value;
            }
        });


        t.stop();
        System.out.println("Elapse in " + t);
    }
}
