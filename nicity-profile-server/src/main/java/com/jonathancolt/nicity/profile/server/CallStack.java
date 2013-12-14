/*
 * $Revision$
 * $Date$
 *
 * Copyright (C) 1999-$year$ Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
 */
package com.jonathancolt.nicity.profile.server;

/*
 * #%L
 * nicity-profile-server
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

import com.jonathancolt.nicity.profile.sample.LatentSample;
import com.jonathancolt.nicity.profile.sample.LatentSample.LatentNode;
import com.jonathancolt.nicity.profile.server.model.Call;
import com.jonathancolt.nicity.profile.server.model.CallClass;
import com.jonathancolt.nicity.profile.server.model.CallDepth;
import com.jonathancolt.nicity.profile.server.model.ClassMethod;
import com.jonathancolt.nicity.profile.server.model.MethodSample;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class CallStack {

    private final String serviceName;
    private final ConcurrentHashMap<Integer, CallDepth> callsAtDepth = new ConcurrentHashMap<>();
    private long lastSampleTimestampEpochMillis;

    public CallStack(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public void call(LatentSample latentSample) {
        lastSampleTimestampEpochMillis = System.currentTimeMillis();
        LatentNode from = latentSample.from;
        LatentNode to = latentSample.to;

        CallDepth fromDepth = getCallDepth(from.stackDepth);
        CallClass fromClass = getOrCreateCallClass(fromDepth.getClassNameToCallClass(), getClassName(from));
        ClassMethod fromMethod = getOrCreateClassMethod(fromClass.getClassMethods(), from.methodName);
        fromMethod.getStackDepths().add(from.stackDepth);
        fromMethod.setMethodSample(new MethodSample(from.callCount,
                from.callLatency,
                from.failedCount,
                from.failedLatency));

        if (to != null) {

            fromClass.getCalls().add(new Call(getClassName(to), to.methodName));

            CallDepth toDepth = getCallDepth(to.stackDepth);
            CallClass toClass = getOrCreateCallClass(toDepth.getClassNameToCallClass(), getClassName(to));
            ClassMethod toMethod = getOrCreateClassMethod(toClass.getClassMethods(), to.methodName);
            toMethod.getStackDepths().add(to.stackDepth);
            toMethod.setMethodSample(new MethodSample(to.callCount,
                to.callLatency,
                to.failedCount,
                to.failedLatency));
        }
    }

     private CallClass getOrCreateCallClass(ConcurrentHashMap<String, CallClass> classNameToCallClass, String className) {
        CallClass node = classNameToCallClass.get(className);
        if (node == null) {
            node = new CallClass(className, new HashSet<Call>(), new ConcurrentHashMap<String, ClassMethod>());
            CallClass had = classNameToCallClass.putIfAbsent(className, node);
            if (had != null) {
                return had;
            }
        }
        return node;
    }


    private ClassMethod getOrCreateClassMethod(ConcurrentHashMap<String, ClassMethod> classMethods, String methodName) {
        ClassMethod classMethod = classMethods.get(methodName);
        if (classMethod == null) {
            classMethod = new ClassMethod(methodName);
            ClassMethod had = classMethods.putIfAbsent(methodName, classMethod);
            if (had != null) {
                return had;
            }
        }
        return classMethod;
    }

    private String getClassName(LatentNode node) {
        return node.interfaceName;
    }

    private CallDepth getCallDepth(int depth) {
        CallDepth got = callsAtDepth.get(depth);
        if (got == null) {
            got = new CallDepth(new ConcurrentHashMap<String, CallClass>());
            CallDepth had = callsAtDepth.putIfAbsent(depth, got);
            if (had != null) {
                return had;
            }
        }
        return got;
    }

    CallDepth[] getCopy() {
        List<Integer> keySet = new LinkedList<>(callsAtDepth.keySet());
        Collections.sort(keySet);
        CallDepth[] callDepths = new CallDepth[keySet.size()];
        for (int i = 0; i < callDepths.length; i++) {
            callDepths[i] = callsAtDepth.get(keySet.get(i));
        }
        return callDepths;
    }
}
