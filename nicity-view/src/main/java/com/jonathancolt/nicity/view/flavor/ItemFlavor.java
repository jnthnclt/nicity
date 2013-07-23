/*
 * ItemFlavor.java.java
 *
 * Created on 01-30-2010 11:00:15 AM
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
package com.jonathancolt.nicity.view.flavor;

import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.interfaces.ICanvas;

/**
 *
 * @author Administrator
 */
public class ItemFlavor extends AFlavor {

    /**
     *
     * @param _g
     * @param _x
     * @param _y
     * @param _w
     * @param _h
     * @param _color
     */
    @Override
    public void paintFlavor(ICanvas _g, int _x, int _y, int _w, int _h, AColor _color) {
        _g.setColor(_color);
        _g.rect(true, _x, _y, _w, _h);
    }
}
