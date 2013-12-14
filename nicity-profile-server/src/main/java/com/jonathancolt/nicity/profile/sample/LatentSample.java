/*
 * $Revision$
 * $Date$
 *
 * Copyright (C) 1999-$year$ Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
 */
package com.jonathancolt.nicity.profile.sample;

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

/**
 *
 */
public class LatentSample {

    public String clusterName;
    public String serviceName;
    public String serviceVersion;
    public long sampleTimestampEpochMillis;
    public LatentNode from;
    public LatentNode to;

    public static class LatentNode {

        public int stackDepth;
        public String interfaceName;
        public String className;
        public String methodName;
        public long failedCount;
        public long failedLatency;
        public long callCount;
        public long callLatency;

        @Override
        public String toString() {
            return "LatentNode{"
                    + "stackDepth=" + stackDepth
                    + ", interfaceName=" + interfaceName
                    + ", className=" + className
                    + ", methodName=" + methodName
                    + ", failedCount=" + failedCount
                    + ", failedLatency=" + failedLatency
                    + ", callCount=" + callCount
                    + ", callLatency=" + callLatency + '}';
        }
    }

    @Override
    public String toString() {
        return "LatentSample{"
                + "clusterName=" + clusterName
                + ", serviceName=" + serviceName
                + ", serviceVersion=" + serviceVersion
                + ", sampleTimestampEpochMillis=" + sampleTimestampEpochMillis
                + ", from=" + from
                + ", to=" + to + '}';
    }
}
