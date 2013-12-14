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

/**
 *
 */
class ValuesHistogram {
    int[] histogram = new int[16];
    int max = 0;

    public ValuesHistogram() {
    }

    void reset() {
        max = 0;
        for (int i = 0; i < histogram.length; i++) {
            histogram[i] = 0;
        }
    }

    void value(double value) {
        int bucket = (int) (value * (histogram.length - 1));
        histogram[bucket]++;
        max = Math.max(max, histogram[bucket]);
    }

}
