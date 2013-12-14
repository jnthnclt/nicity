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
package com.jonathancolt.nicity.profile.server.model.alerts;

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

/**
 *
 * @author jonathan
 */
public class AlertKey {
    private final String cluster;
    private final String serviceName;
    private final String serviceInstance;
    private final String serviceVersion;
    private final String interfaceName;
    private final String className;
    private final String methodsName;
    private final Trigger[] triggers;

    public AlertKey(String cluster, String serviceName, String serviceInstance, String serviceVersion, String interfaceName, String className, String methodsName, Trigger[] triggers) {
        this.cluster = cluster;
        this.serviceName = serviceName;
        this.serviceInstance = serviceInstance;
        this.serviceVersion = serviceVersion;
        this.interfaceName = interfaceName;
        this.className = className;
        this.methodsName = methodsName;
        this.triggers = triggers;
    }
    
}
