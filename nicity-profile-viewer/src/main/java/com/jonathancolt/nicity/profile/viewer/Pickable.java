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

import com.jonathancolt.nicity.core.memory.struct.XYWH_I;
import com.jonathancolt.nicity.core.memory.struct.XY_I;
import com.jonathancolt.nicity.view.core.Place;
import com.jonathancolt.nicity.view.interfaces.ICanvas;

/**
 *
 * @author jonathan
 */
public abstract class Pickable {
    private float pw;
    private float ph;
    private Place place;
    boolean over = false;
    XYWH_I paintBounds = new XYWH_I(0, 0, 0, 0);

    public Pickable(float pw, float ph, Place place) {
        this.pw = pw;
        this.ph = ph;
        this.place = place;
    }

    public abstract XY_I bounds();

    public abstract void picked(int x, int y);

    public abstract void paintPickage(ICanvas _g, int _x, int _y, int _w, int _h, boolean isOver);

    public void paint(ICanvas _g, int _x, int _y, int _w, int _h) {
        XY_I bounds = bounds();
        int cx = (int) (_x + (_w * pw));
        int cy = (int) (_y + (_h * ph));
        int x = cx - (bounds.x / 2);
        int y = cy - (bounds.y / 2);
        int w = bounds.x;
        int h = bounds.y;
        XY_I ul = place.place(x, y, w, h);
        paintBounds = new XYWH_I(ul.x, ul.y, w, h);
        paintPickage(_g, ul.x, ul.y, w, h, false);
    }

    public boolean over(int x, int y) {
        if (paintBounds.contains(new XY_I(x, y))) {
            over = true;
        } else {
            over = false;
        }
        return over;
    }

}
