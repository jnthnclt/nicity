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

import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.flavor.AFlavor;
import com.jonathancolt.nicity.view.interfaces.ICanvas;

public class BarFlavor extends AFlavor {
    
    private final int r;

    public BarFlavor(int r) {
        this.r = r;
    }
    
    @Override
    public void paintFlavor(ICanvas _g, int _x, int _y, int _w, int _h, AColor _color) {
        AColor color = _color;
        _g.setColor(color);
        _g.roundRect(true, _x, _y, _w, _h, r, r);
        _g.setColor(color.darken(0.5f));
        _g.roundRect(false, _x, _y, _w, _h, r, r);
    }

}
