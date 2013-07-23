/*
 * $Revision$
 * $Date$
 *
 * Copyright (C) 1999-$year$ Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
 */
package com.jonathancolt.nicity.profile.visualize;

import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.flavor.AFlavor;
import com.jonathancolt.nicity.view.interfaces.ICanvas;

public class BarFlavor extends AFlavor {
    
    private final int r;

    public BarFlavor(int r) {
        this.r = r;
    }
    
    public void paintFlavor(ICanvas _g, int _x, int _y, int _w, int _h, AColor _color) {
        AColor color = _color;
        _g.setColor(color);
        _g.roundRect(true, _x, _y, _w, _h, r, r);
        _g.setColor(color.darken(0.5f));
        _g.roundRect(false, _x, _y, _w, _h, r, r);
    }

}
