/*
 * $Revision$
 * $Date$
 *
 * Copyright (C) 1999-$year$ Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
 */
package com.jonathancolt.nicity.core.memory.struct;

/*
 * #%L
 * nicity-core
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

import java.awt.Polygon;
import java.util.Arrays;

/**
 *
 */
public class Poly_I extends Polygon {

    public int npoints;
    public int[] xpoints;
    public int[] ypoints;
    private static final int MIN_LENGTH = 4;

    public Poly_I(int[] xpoints, int[] ypoints, int npoints) {
        this.xpoints = xpoints;
        this.ypoints = ypoints;
        this.npoints = npoints;
    }

    public Poly_I() {
        xpoints = new int[MIN_LENGTH];
        ypoints = new int[MIN_LENGTH];
    }

    public void addPoint(int x, int y) {
        if (npoints >= xpoints.length || npoints >= ypoints.length) {
            int newLength = npoints * 2;
            // Make sure that newLength will be greater than MIN_LENGTH and
            // aligned to the power of 2
            if (newLength < MIN_LENGTH) {
                newLength = MIN_LENGTH;
            } else if ((newLength & (newLength - 1)) != 0) {
                newLength = Integer.highestOneBit(newLength);
            }

            xpoints = Arrays.copyOf(xpoints, newLength);
            ypoints = Arrays.copyOf(ypoints, newLength);
        }
        xpoints[npoints] = x;
        ypoints[npoints] = y;
        npoints++;
        if (bounds != null) {
            updateBounds(x, y);
        }
    }

    void updateBounds(int x, int y) {
        if (x < bounds.x) {
            bounds.width = bounds.width + (bounds.x - x);
            bounds.x = x;
        } else {
            bounds.width = Math.max(bounds.width, x - bounds.x);
        }

        if (y < bounds.y) {
            bounds.height = bounds.height + (bounds.y - y);
            bounds.y = y;
        } else {
            bounds.height = Math.max(bounds.height, y - bounds.y);
        }
    }
}
