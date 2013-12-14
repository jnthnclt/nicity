package com.jonathancolt.nicity.profile.viewer;

/*
 * #%L
 * nicity-profile-viewer
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

public class CircularArray {

    int size;
    int p;
    long[] timestamps;
    long[] signal;
    double[] rate;
    long currentCount;
    long lastV = Long.MAX_VALUE;
    double ratePerSec = 0;
    long lastTime = 0;

    CircularArray(int size) {
        this.size = size;
        this.timestamps = new long[size];
        this.signal = new long[size];
        this.rate = new double[size];
        p = 0;
    }

    public long[] rawSignal() {
        int lp = p;
        long[] copy = new long[size];
        for (int i = 0; i < size; i++) {
            copy[i] = signal[(lp + i) % size];
        }
        return copy;
    }

    public double[] rawRate() {
        int lp = p;
        double[] copy = new double[size];
        for (int i = 0; i < size; i++) {
            copy[i] = rate[(lp + i) % size];
        }
        return copy;
    }

    public void push(long time, long v) {
        signal[p] = v;
        currentCount = v;
        if (lastV == Long.MAX_VALUE) {
            lastTime = time;
            lastV = v;
        } else {
            long delta = v - lastV;
            ratePerSec = Math.abs((delta / (time - lastTime)) * 1000.0d);
            rate[p] = ratePerSec;
            lastTime = time;
            lastV = v;
        }
        p++;
        p %= size;
    }

}
