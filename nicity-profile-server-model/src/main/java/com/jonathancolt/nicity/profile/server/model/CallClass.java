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

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author jonathan.colt
 */
public class CallClass {

    private final String name;
    private final Set<Call> calls;
    private final ConcurrentHashMap<String, ClassMethod> classMethods;

    @JsonCreator
    public CallClass(@JsonProperty("className") String className,
            @JsonProperty("calls") Set<Call> calls,
            @JsonProperty("classMethods") ConcurrentHashMap<String, ClassMethod> classMethods) {
        this.name = (className == null) ? "unnamedClass" : className;
        this.calls = calls;
        this.classMethods = classMethods;
    }

    public String getName() {
        return name;
    }

    public Set<Call> getCalls() {
        return calls;
    }

    public ConcurrentHashMap<String, ClassMethod> getClassMethods() {
        return classMethods;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CallClass other = (CallClass) obj;
        if (this.name != other.name && (this.name == null || !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }
}
