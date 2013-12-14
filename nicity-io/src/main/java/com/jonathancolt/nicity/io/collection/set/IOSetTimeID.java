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

public class IOSetTimeID implements ISIO_ID {
    public static IOSetTimeID sio_id = new IOSetTimeID();
    final private Object timeLock = new Object();
    private long lastTime = 0;
    private long nanos = 0;
    public IOSetTimeID() {
    }
    public int size() {
        return 8 + 8;
    }
    public byte[] id() {
        synchronized (timeLock) {
            long time = System.currentTimeMillis();
            if (time <= lastTime) {
                nanos++;
            } else {
                nanos = 0;
            }
            lastTime = time;
            return UIO.longsBytes(new long[]{time, nanos});
        }
    }
}

