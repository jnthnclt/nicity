/*
 * SolidBorder.java.java
 *
 * Created on 03-12-2010 06:32:19 PM
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
package com.jonathancolt.nicity.view.border;

import com.jonathancolt.nicity.core.value.IValue;
import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.core.ViewColor;
import com.jonathancolt.nicity.view.interfaces.IActiveBorder;
import com.jonathancolt.nicity.view.interfaces.IActiveSelectedBorder;
import com.jonathancolt.nicity.view.interfaces.IBorder;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.interfaces.ISelectedBorder;

/**
 *
 * @author Administrator
 */
public class SolidBorder implements IBorder, IActiveBorder, ISelectedBorder, IActiveSelectedBorder {

    private int size = 0;
    /**
     *
     */
    public Object backgroundColor = ViewColor.cTheme;

    /**
     *
     */
    public SolidBorder() {
    }

    /**
     *
     * @param _backgroundColor
     */
    public SolidBorder(Object _backgroundColor) {
        backgroundColor = _backgroundColor;
    }

    /**
     *
     * @param _backgroundColor
     * @param _pad
     */
    public SolidBorder(Object _backgroundColor, int _pad) {
        backgroundColor = _backgroundColor;
        size = _pad;
    }

    /**
     *
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     */
    @Override
    public void paintBorder(ICanvas g, int x, int y, int w, int h) {
    }

    /**
     *
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     */
    @Override
    public void paintBackground(ICanvas g, int x, int y, int w, int h) {
        if (backgroundColor instanceof AColor) {
            g.setColor(((AColor) backgroundColor));
            g.rect(true, x, y, w, h);
        } else if (backgroundColor instanceof IValue) {
            AColor color = (AColor) ((IValue) backgroundColor).getValue();
            g.setColor(color);
            g.rect(true, x, y, w, h);
        }

    }

    /**
     *
     * @return
     */
    @Override
    public boolean isActive() {
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isSelected() {
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public float getX() {
        return size;
    }

    /**
     *
     * @return
     */
    @Override
    public float getY() {
        return size;
    }

    /**
     *
     * @return
     */
    @Override
    public float getW() {
        return size;
    }

    /**
     *
     * @return
     */
    @Override
    public float getH() {
        return size;
    }

    /**
     *
     * @return
     */
    @Override
    public IBorder getDefaultBorder() {
        return this;
    }

    /**
     *
     * @return
     */
    @Override
    public IActiveBorder getActiveBorder() {
        return this;
    }

    /**
     *
     * @return
     */
    @Override
    public ISelectedBorder getSelectedBorder() {
        return this;
    }

    /**
     *
     * @return
     */
    @Override
    public IActiveSelectedBorder getActiveSelectedBorder() {
        return this;
    }
}
