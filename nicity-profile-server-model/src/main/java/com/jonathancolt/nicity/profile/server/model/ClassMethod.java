/*
 * $Revision$
 * $Date$
 *
 * Copyright (C) 1999-$year$ Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
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

import java.util.HashSet;
import java.util.Set;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 */
public class ClassMethod {

    private final String methodName;
    private Set<Integer> stackDepths = new HashSet<>();
    private MethodSample methodSample;

    @JsonCreator
    public ClassMethod(@JsonProperty("methodName") String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Set<Integer> getStackDepths() {
        return stackDepths;
    }

    public void setStackDepths(Set<Integer> stackDepths) {
        this.stackDepths = stackDepths;
    }

    public MethodSample getMethodSample() {
        return methodSample;
    }

    public void setMethodSample(MethodSample methodSample) {
        this.methodSample = methodSample;
    }

    @Override
    public String toString() {
        return "ClassMethod{" + "methodName=" + methodName + ", stackDepths=" + stackDepths + ", methodSample=" + methodSample + '}';
    }
}
