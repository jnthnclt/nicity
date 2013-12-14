/*
 * TextBorder.java.java
 *
 * Created on 01-03-2010 01:31:38 PM
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
public class TextBorder implements IBorder, IActiveBorder, ISelectedBorder, IActiveSelectedBorder {

    private static float x = 1;
    private static float y = 1;
    private static float w = 1;
    private static float h = 1;

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
        w -= 1;
        h -= 1;

        g.setColor(ViewColor.cTheme);
        g.rect(false, x, y, w, h);
    }

    /**
     *
     * @param g
     * @param x
     * @param y
     * @param _w
     * @param _h
     */
    @Override
    public void paintBackground(ICanvas g, int x, int y, int _w, int _h) {
        g.setColor(AColor.white);
        g.rect(true, x, y, _w, _h);
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
        return x;
    }

    /**
     *
     * @return
     */
    @Override
    public float getY() {
        return y;
    }

    /**
     *
     * @return
     */
    @Override
    public float getW() {
        return w;
    }

    /**
     *
     * @return
     */
    @Override
    public float getH() {
        return h;
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
