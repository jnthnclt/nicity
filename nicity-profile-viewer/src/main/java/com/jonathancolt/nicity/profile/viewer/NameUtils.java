/*
 * $Revision$
 * $Date$
 *
 * Copyright (C) 1999-$year$ Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
 */
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

import com.jonathancolt.nicity.core.lang.UString;
import com.jonathancolt.nicity.profile.server.model.ClassMethod;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class NameUtils {

    private RollupCalls rollupCalls = new RollupCalls();

    String[] simpleInterfaceName(InterfaceArea callArea) {
        if (callArea == null) return new String[0];
        String shortClassName = shortName(callArea.getName());
        ConcurrentHashMap<String, ClassMethod> classMethods = callArea.callClass.getClassMethods();
        String[] ls = new String[]{
            shortClassName,
            UString.toString(callArea.depths.toArray(),","),
            "latency: " + latencyString(rollupCalls.getSuccesslatency(classMethods)),
            "called: " + rollupCalls.getCalled(classMethods),
            "total: " + latencyString(rollupCalls.getCalled(classMethods) * rollupCalls.getSuccesslatency(classMethods)),
            "calledBy: " + callArea.calledByCount
            };
        return ls;
    }

    String[] simpleMethodName(InterfaceArea.MethodArea methodArea) {
        if (methodArea == null) return new String[0];
        String longMethodName = methodArea.classMethod.getMethodName();
        int startOfArgs = longMethodName.indexOf('(');
        String methodName = longMethodName.substring(0, startOfArgs);
        String args = longMethodName.substring(startOfArgs, longMethodName.indexOf(")"));
        String[] ls = new String[]{
            methodName+"("+UString.toString(simpleArgs(args),", ")+")",
            "latency: " + latencyString(methodArea.classMethod.getMethodSample().getSuccesslatency()),
            "called: " + methodArea.classMethod.getMethodSample().getCalled(),
            "total: " + latencyString(methodArea.classMethod.getMethodSample().getCalled() * methodArea.classMethod.getMethodSample().getSuccesslatency())};
        return ls;
    }

    String[] simpleArgs(String args) {
        String[] as = args.split(",");
        for (int i = 0; i < as.length; i++) {
            as[i] = shortName(as[i]);
        }
        return as;
    }

    String shortName(String string) {
        int index = string.lastIndexOf('.');
        if (index != -1) {
            return string.substring(index + 1);
        } else {
            return string;
        }
    }

    String latencyString(long nanos) {
        long micros = TimeUnit.NANOSECONDS.toMicros(nanos);
        if (micros == 0) {
            return nanos + " nanos";
        }
        long millis = TimeUnit.MICROSECONDS.toMillis(micros);
        if (millis == 0) {
            return micros + " micro";
        }
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        if (seconds == 0) {
            return millis + " millis";
        }
        long minutes = TimeUnit.MILLISECONDS.toMinutes(seconds);
        if (minutes == 0) {
            return seconds + " seconds";
        }
        return minutes + " minutes";
    }

    long nanosToIndex(long nanos) {
        if (nanos < TimeUnit.NANOSECONDS.toNanos(10)) {
            return 0L;
        } else if (nanos < TimeUnit.NANOSECONDS.toNanos(100)) {
            return 1L;
        } else if (nanos < TimeUnit.MICROSECONDS.toNanos(1)) {
            return 2L;
        } else if (nanos < TimeUnit.MICROSECONDS.toNanos(10)) {
            return 3L;
        } else if (nanos < TimeUnit.MICROSECONDS.toNanos(100)) {
            return 4L;
        } else if (nanos < TimeUnit.MILLISECONDS.toNanos(1)) {
            return 5L;
        } else if (nanos < TimeUnit.MILLISECONDS.toNanos(10)) {
            return 6L;
        } else if (nanos < TimeUnit.MILLISECONDS.toNanos(100)) {
            return 7L;
        } else if (nanos < TimeUnit.SECONDS.toNanos(1)) {
            return 8L;
        } else if (nanos < TimeUnit.SECONDS.toNanos(10)) {
            return 9L;
        } else if (nanos < TimeUnit.SECONDS.toNanos(100)) {
            return 10L;
        } else {
            return 11L;
        }
    }

    String nanoIndexToString(long index) {
        if (index == 0) {
            return "< 10 nanos";
        }
        if (index == 1) {
            return "< 100 nanos";
        }
        if (index == 2) {
            return "< 1 micro";
        }
        if (index == 3) {
            return "< 10 micro";
        }
        if (index == 4) {
            return "< 100 micro";
        }
        if (index == 5) {
            return "< 1 millis";
        }
        if (index == 6) {
            return "< 10 millis";
        }
        if (index == 7) {
            return "< 100 millis";
        }
        if (index == 8) {
            return "< 1 seconds";
        }
        if (index == 9) {
            return "< 10 seconds";
        }
        if (index == 10) {
            return "< 100 seconds";
        }
        return " > 100 seconds";
    }

    long calledOrderOfMagnitudeIndex(long called) {
        if (called < 10) {
            return 0L;
        }
        if (called < 100) {
            return 1L;
        }
        if (called < 1000) {
            return 2L;
        }
        if (called < 10000) {
            return 3L;
        }
        if (called < 100000) {
            return 4L;
        }
        if (called < 1000000) {
            return 5L;
        }
        if (called < 10000000) {
            return 6L;
        }
        if (called < 100000000) {
            return 7L;
        }
        return 8L;
    }

    String orderOfMagnitudeIndexToString(long calledIndex) {
        if (calledIndex == 0L) {
            return "< 10 times";
        }
        if (calledIndex == 1L) {
            return "< 100 times";
        }
        if (calledIndex == 2L) {
            return "< 1,000 times";
        }
        if (calledIndex == 3L) {
            return "< 10,0000 times";
        }
        if (calledIndex == 4L) {
            return "< 100,000 times";
        }
        if (calledIndex == 5L) {
            return "< 1,000,000 times";
        }
        if (calledIndex == 6L) {
            return "< 10,000,000 times";
        }
        if (calledIndex == 7L) {
            return "< 100,000,000 times";
        }
        return " > 1,000,000,000 times";
    }
}
