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

import com.jonathancolt.nicity.profile.server.model.ClassMethod;
import java.util.Map;

/**
 *
 * @author jonathan
 */
public class RollupCalls {

    public long getCalled(Map<String, ClassMethod> classMethods) {
        long v = 0;
        for (ClassMethod classMethod : classMethods.values()) {
            v += classMethod.getMethodSample().getCalled();
        }
        return v;
    }

    public long getSuccesslatency(Map<String, ClassMethod> classMethods) {
        long v = 0;
        for (ClassMethod classMethod : classMethods.values()) {
            v += classMethod.getMethodSample().getSuccesslatency();
        }
        return v;
    }

    public long getFailed(Map<String, ClassMethod> classMethods) {
        long v = 0;
        for (ClassMethod classMethod : classMethods.values()) {
            v += classMethod.getMethodSample().getFailed();
        }
        return v;
    }

    public long getFailedlatency(Map<String, ClassMethod> classMethods) {
        long v = 0;
        for (ClassMethod classMethod : classMethods.values()) {
            v += classMethod.getMethodSample().getFailedlatency();
        }
        return v;
    }
}
