/*
 * MinMaxInt.java.java
 *
 * Created on 12-27-2009 03:12:00 PM
 *
 * Copyright 2009 Jonathan Colt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.colt.nicity.core.lang;

public class MinMaxInt {
    public int min = Integer.MAX_VALUE;
    public int max = -Integer.MAX_VALUE;
    public int minIndex = -1;
    public int maxIndex = -1;
    private float sum = 0;
    private int count = 0;
    public MinMaxInt() {
    }
    public MinMaxInt(int _min, int _max) {
        min = _min;
        max = _max;
    }
    public int min() {
        return min;
    }
    public int max() {
        return max;
    }
    public int value(int _int) {
        sum += _int;
        if (_int > max) {
            max = _int;
            maxIndex = count;
        }
        if (_int < min) {
            min = _int;
            minIndex = count;
        }
        count++;
        return _int;
    }
    public void reset() {
        min = Integer.MAX_VALUE;
        max = -Integer.MAX_VALUE;

        minIndex = -1;
        maxIndex = -1;

        sum = 0;
        count = 0;
    }
    public boolean inclusivelyContained(int _value) {
        if (count == 0) {
            return false;
        }
        if (_value < min) {
            return false;
        }
        if (_value > max) {
            return false;
        }
        return true;
    }
    public double std(int _value) {
        double mean = Math.pow(mean(), 2);
        double value = Math.pow((double) _value, 2);
        double variation = Math.max(mean, value) - Math.min(mean, value);
        return Math.sqrt(variation);
    }
    public int samples() {
        return count;
    }
    public double mean() {
        return sum / (int) count;
    }
    public int range() {
        return max() - min();
    }
    public int middle() {
        return min() + (range() / 2);
    }
    public double zeroToOne(int _int) {
        return zeroToOne(min, max, _int);
    }
    public int unzeroToOne(double _int) {
        return unzeroToOne(min, max, _int);
    }
    @Override
    public String toString() {
        return "Min:" + min + " Max:" + max;
    }
    public static double zeroToOne(int _min, int _max, int _int) {
        if (_max == _min) {
            if (_int == _min) {
                return 0;
            }
            if (_int > _max) {
                return Double.MAX_VALUE;
            }
            return -Double.MAX_VALUE;
        }
        return (double) (_int - _min) / (double) (_max - _min);
    }
    public static int unzeroToOne(int _min, int _max, double _p) {
        int d = _max - _min;
        int pd = (int) (d * _p);
        return _min + pd;
    }
}
