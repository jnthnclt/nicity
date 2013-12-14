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

import com.jonathancolt.nicity.core.lang.MinMaxFloat;
import com.jonathancolt.nicity.core.lang.MinMaxLong;
import com.jonathancolt.nicity.view.core.AColor;

/**
 *
 */
public class Grayscale implements Coloring {

    @Override
    public AColor value(InterfaceArea callArea, long maxV) {
        double rank = 1d - (MinMaxLong.zeroToOne(0, maxV, callArea.value));
        return new AColor(1f - MinMaxFloat.zeroToOne(-0.5f, 1.5f, (float) rank));
    }
}
