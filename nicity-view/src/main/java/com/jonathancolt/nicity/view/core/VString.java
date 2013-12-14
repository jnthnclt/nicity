/*
 * VString.java.java
 *
 * Created on 01-03-2010 01:31:40 PM
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
package com.jonathancolt.nicity.view.core;

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

import com.jonathancolt.nicity.core.memory.struct.XYWH_I;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.interfaces.IView;

/**
 *
 * @author Administrator
 */
public class VString extends Viewer {

    //private String toString;
    private float min = -1;
    private float max = -1;
    private float width, height;
    private ViewString vs;

    /**
     *
     */
    public VString() {
        this(null, null, null);
    }

    /**
     *
     * @param _text
     */
    public VString(Object _text) {
        this(_text, null, null);
    }

    /**
     *
     * @param _text
     * @param _color
     */
    public VString(Object _text, AColor _color) {
        this(_text, null, _color);
    }

    /**
     *
     * @param _text
     * @param _font
     */
    public VString(Object _text, AFont _font) {
        this(_text, _font, null);
    }

    /**
     *
     * @param _text
     * @param _font
     * @param _color
     */
    public VString(Object _text, AFont _font, AColor _color) {
        vs = new ViewString(_text, _font, _color);
        setPlacer(new Placer(vs));
        super.layoutInterior();
        width = getWidth();
        height = getHeight();
    }

    @Override
    public String toString() {
        return vs.toString();
    }

    @Override
    public void mend() {
        enableFlag(UV.cRepair);//??
        super.mend();
    }

    /**
     *
     * @return
     */
    public float getWidth() {
        IView content = getContent();
        float _w = content.getW();
        if (min >= 0 && _w < min) {
            _w = min;
        }
        if (max >= 0 && _w > max) {
            _w = max;
        }
        return _w;
    }

    /**
     *
     * @return
     */
    public float getHeight() {
        IView content = getContent();
        float _h = content.getH();
        return _h;
    }

    /**
     *
     * @param _min
     * @param _max
     */
    public void setWRange(float _min, float _max) {
        min = _min;
        max = _max;
    }

    /**
     *
     * @param _text
     */
    public void setText(Object _text) {
        vs.setText(_text);
    }

    /**
     *
     * @return
     */
    public ViewString getViewString() {
        return vs;
    }

    @Override
    public float getW() {
        if (min >= 0 && width < min) {
            return min + border.getW();
        }
        if (max >= 0 && width > max) {
            return max + border.getW();
        }
        return width + border.getW();
    }

    @Override
    public float getH() {
        return height;
    }

    @Override
    public void paintBody(ICanvas g, Layer _layer, int mode, XYWH_I _painted) {
        super.paintBody(g, _layer, mode, _painted);
        float _w = getWidth();
        float _h = getHeight();
        if (_w != width || _h != height) {
            width = _w;
            height = _h;
            IView _parent = parent.get();
            _parent.paint();
            paint();
        }
    }
}
