/*
 * VPreviewColor.java.java
 *
 * Created on 03-12-2010 06:38:42 PM
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
import com.jonathancolt.nicity.view.core.RigidBox;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.event.MouseEntered;
import com.jonathancolt.nicity.view.event.MouseExited;
import com.jonathancolt.nicity.view.event.MousePressed;
import com.jonathancolt.nicity.view.event.MouseReleased;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.interfaces.IMouseEvents;
import com.jonathancolt.nicity.view.paint.UPaint;

/**
 *
 * @author Administrator
 */
public class VPreviewColor extends Viewer implements IMouseEvents, IObserver {
    
    public static void main(String[] _args) {
        VPreviewColor v = new VPreviewColor(new Value("Name"),new Value(AColor.blue),128,128);
        UV.exitFrame(v, VPreviewColor.class.getSimpleName());
    }

    private Value name;
    private Value color;

    /**
     *
     * @param _name
     * @param _color
     * @param _w
     * @param _h
     */
    public VPreviewColor(Value _name, Value _color, int _w, int _h) {
        name = _name;
        color = _color;
        setContent(new RigidBox(_w, _h));
        if (name != null) {
            name.bind(this);
        }
        color.bind(this);
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
        UPaint.drawCheckerBoard(g, _x, _y, _w, _h, 16, AColor.black, AColor.white);
        g.setColor((AColor) color.getValue());
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
            AColor c = (AColor) color.getValue();
            g.setColor(c.invert().bw());
            UPaint.string(g, name.toString(), UV.fonts[UV.cText], _x + (_w / 2), _y + (_h / 2), UV.cCC);
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

    /**
     *
     * @param e
     */
    @Override
    public void mouseEntered(MouseEntered e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseExited(MouseExited e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void mousePressed(MousePressed e) {
        UV.popup(this, e, UV.zone(new VEditNumericColor(name, color)), true,true);
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseReleased e) {
    }
}
