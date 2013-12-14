/*
 * Copyright 2013 jonathan.colt.
 *
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
import com.jonathancolt.nicity.profile.server.model.CallDepth;
import com.jonathancolt.nicity.profile.server.model.ServiceModel;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author jonathan.colt
 */
public class ServicesCallDepthStack {

    private ConcurrentHashMap<String, CallStack> depthStacks = new ConcurrentHashMap<>();

    public List<String> getServiceNames() {
        LinkedList<String> serviceNames = new LinkedList<>(depthStacks.keySet());
        Collections.sort(serviceNames);
        return serviceNames;
    }

    public void call(LatentSample latentSample) {
        String key = latentSample.clusterName + " " + latentSample.serviceName + " " + latentSample.serviceVersion;
        getOrCreateCallStack(key).call(latentSample);
    }

    public ServiceModel getCopy(String serviceName) {
        CallStack got = depthStacks.get(serviceName);
        if (got == null) {
            return new ServiceModel(System.currentTimeMillis(), "null", new CallDepth[0]);
        } else {
            return new ServiceModel(System.currentTimeMillis(), serviceName, got.getCopy());
        }
    }

    private CallStack getOrCreateCallStack(String serviceName) {
        CallStack got = depthStacks.get(serviceName);
        if (got == null) {
            got = new CallStack(serviceName);
            CallStack had = depthStacks.putIfAbsent(serviceName, got);
            if (had != null) {
                got = had;
            }
        }
        return got;
    }
}
