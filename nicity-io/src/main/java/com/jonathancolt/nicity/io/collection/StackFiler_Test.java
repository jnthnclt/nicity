package com.jonathancolt.nicity.io.collection;

import com.jonathancolt.nicity.core.io.UIO;
import com.jonathancolt.nicity.core.lang.URandom;
import com.jonathancolt.nicity.core.time.MilliTimer;

public class StackFiler_Test {
    public static void main(String[] _arg) {

        try {
            String name = "test.stack";//_arg[0];
            int it = 10;//Integer.parseInt(_arg[1]);

            System.out.println("Name: " + name);
            System.out.println("Iteration: " + it);


            MilliTimer t = new MilliTimer();
            t.start();

            StackFiler stack = StackFiler.factory(name, 8, it, 0, null);

            System.out.println("Contains:"+stack.getCount());

            long pc = URandom.rand(it);
            System.out.println("Pushing.." + pc);
            for (long i = 0; i < pc; i++) {
                long push = URandom.rand(Integer.MAX_VALUE) * URandom.rand(Integer.MAX_VALUE);
                System.out.print(i+"):"+push+",");
                try {
                    stack.push(UIO.longBytes(push));
                }catch(Exception x) {
                    x.printStackTrace();
                }
            }
            System.out.println("Pushed.." + pc);


            pc = URandom.rand(it);
            System.out.println("Popping..." + pc);
            for (long i = 0; i < pc; i++) {
                System.out.print(i+"):"+UIO.bytesLong(stack.pop())+",");
            }
            System.out.println("Popped..." + pc);

            System.out.println("Contains:"+stack.getCount());

            t.stop();
            System.out.println("Created in " + t);

            System.exit(0);
        } catch (Exception x) {
            x.printStackTrace();
        }

    }
}
