/*
 * VPreviewFont.java.java
 *
 * Created on 03-12-2010 06:38:49 PM
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

import com.jonathancolt.nicity.core.observer.Change;
import com.jonathancolt.nicity.core.observer.IObservable;
import com.jonathancolt.nicity.core.observer.IObserver;
import com.jonathancolt.nicity.core.value.Value;
import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.core.AFont;
import com.jonathancolt.nicity.view.core.RigidBox;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.ViewColor;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.paint.UPaint;

/**
 *
 * @author Administrator
 */
public class VPreviewFont extends Viewer implements IObserver {
    
    public static void main(String[] _args) {
        VPreviewFont v = new VPreviewFont(new Value("Name"),new Value(UV.fonts[UV.cText]),new Value(AColor.blue),128,128);
        UV.exitFrame(v, VPreviewColor.class.getSimpleName());
    }

    private Value name, font, color;

    /**
     *
     * @param _name
     * @param _font
     * @param _color
     * @param _w
     * @param _h
     */
    public VPreviewFont(Value _name, Value _font, Value _color, int _w, int _h) {
        name = _name;
        font = _font;
        setContent(new RigidBox(_w, _h));
        if (name != null) {
            name.bind(this);
        }
        font.bind(this);
        if (color != null) {
            color.bind(this);
        }
    }

    /**
     *
     * @param g
     * @param _x
     * @param _y
     * @param _w
     * @param _h
     */
    @Override
    public void paintBackground(ICanvas g, int _x, int _y, int _w, int _h) {
        super.paintBackground(g, _x, _y, _w, _h);
        AColor c = ViewColor.cThemeFont;
        if (color != null) {
            c = (AColor) color.getValue();
        }
        g.setColor(c.invert().bw());
        g.rect(true, _x, _y, _w, _h);
    }

    /**
     *
     * @param g
     * @param _x
     * @param _y
     * @param _w
     * @param _h
     */
    @Override
    public void paintBorder(ICanvas g, int _x, int _y, int _w, int _h) {
        super.paintBorder(g, _x, _y, _w, _h);
        if (name != null) {
            AColor c = ViewColor.cThemeFont;
            if (color != null) {
                c = (AColor) color.getValue();
            }
            g.setFont((AFont) font.getValue());
            UPaint.string(g, name.toString(), (AFont) font.getValue(), _x + (_w / 2), _y + (_h / 2), UV.cCC);
        }
    }

    /**
     *
     * @param _change
     */
    @Override
    public void change(Change _change) {
        paint();
    }

    /**
     *
     * @param _observable
     */
    @Override
    public void bound(IObservable _observable) {
        paint();
    }

    /**
     *
     * @param _observable
     */
    @Override
    public void released(IObservable _observable) {
        paint();
    }
}

