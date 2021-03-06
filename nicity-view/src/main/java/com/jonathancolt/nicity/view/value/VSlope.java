/*
 * VSlope.java.java
 *
 * Created on 03-12-2010 06:38:16 PM
 *
 * Copyright 2010 Jonathan Colt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jonathancolt.nicity.view.value;

/*
 * #%L
 * nicity-view
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

import com.jonathancolt.nicity.core.value.Value;
import com.jonathancolt.nicity.view.border.SolidBorder;
import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.core.Placer;
import com.jonathancolt.nicity.view.core.RigidBox;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.interfaces.ICanvas;

/**
 *
 * @author Administrator
 */
public class VSlope extends Viewer {

    /**
     *
     */
    public Value<Double> slope = null;
    /**
     *
     */
    public AColor color = AColor.red;

    /**
     *
     * @param _slope
     * @param _w
     * @param _h
     */
    public VSlope(Value<Double> _slope, int _w, int _h) {
        slope = _slope;
        setPlacer(new Placer(new RigidBox(_w, _h)));
        setBorder(new SolidBorder(AColor.black));
    }

    /**
     *
     * @param _g
     * @param _x
     * @param _y
     * @param _w
     * @param _h
     */
    @Override
    public void paintBackground(ICanvas _g, int _x, int _y, int _w, int _h) {
        super.paintBackground(_g, _x, _y, _w, _h);

        _g.setColor(color);

        double s = -slope.doubleValue();
        int x = (int) (_w * Math.cos(s * (Math.PI / 2)));
        int y = (_h / 2) + (int) ((_h / 2) * Math.sin(s * (Math.PI / 2)));
        _g.line(_x + 0, _y + _h / 2, _x + x, _y + y);
    }
}
