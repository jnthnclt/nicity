package com.jonathancolt.nicity.io.collection;

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
