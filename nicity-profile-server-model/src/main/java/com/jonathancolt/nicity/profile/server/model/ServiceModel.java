/*
 * Copyright 2013 jonathan.
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
package com.jonathancolt.nicity.profile.server.model;

/*
 * #%L
 * nicity-profile-server-model
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

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author jonathan
 */
public class ServiceModel {

    private final long timestampEpochMillis;
    private final String serviceName;
    private final CallDepth[] callDepths;

    @JsonCreator
    public ServiceModel(@JsonProperty("timestampEpochMillis") long timestampEpochMillis,
            @JsonProperty("serviceName") String serviceName,
            @JsonProperty("callDepths") CallDepth[] callDepths) {
        this.timestampEpochMillis = timestampEpochMillis;
        this.serviceName = serviceName;
        this.callDepths = callDepths;
    }

    public long getTimestampEpochMillis() {
        return timestampEpochMillis;
    }

    public String getServiceName() {
        return serviceName;
    }

    public CallDepth[] getCallDepths() {
        return callDepths;
    }

    @Override
    public String toString() {
        return "ServiceModel{" + "timestampEpochMillis=" + timestampEpochMillis + ", serviceName=" + serviceName + ", callDepths=" + callDepths + '}';
    }
}
