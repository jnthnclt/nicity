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
public class MethodSample {

    private final long called;
    private final long successlatency;
    private final long failed;
    private final long failedlatency;

    @JsonCreator
    public MethodSample(@JsonProperty("called") long called,
            @JsonProperty("successlatency") long successlatency,
            @JsonProperty("failed") long failed,
            @JsonProperty("failedlatency") long failedlatency) {
        this.called = called;
        this.successlatency = successlatency;
        this.failed = failed;
        this.failedlatency = failedlatency;
    }

    public long getCalled() {
        return called;
    }

    public long getSuccesslatency() {
        return successlatency;
    }

    public long getFailed() {
        return failed;
    }

    public long getFailedlatency() {
        return failedlatency;
    }

    @Override
    public String toString() {
        return "MethodSample{" + "called=" + called + ", successlatency=" + successlatency + ", failed=" + failed + ", failedlatency=" + failedlatency + '}';
    }
}
