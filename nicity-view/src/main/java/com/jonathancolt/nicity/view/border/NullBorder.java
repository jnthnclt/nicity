/*
 * NullBorder.java.java
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

import com.jonathancolt.nicity.view.interfaces.IActiveBorder;
import com.jonathancolt.nicity.view.interfaces.IActiveSelectedBorder;
import com.jonathancolt.nicity.view.interfaces.IBorder;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.interfaces.ISelectedBorder;

/**
 *
 * @author Administrator
 */
public class NullBorder implements IActiveSelectedBorder {

    /**
     *
     */
    public static final IActiveSelectedBorder cNull = new NullBorder();

    private NullBorder() {
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
        return 0;
    }

    /**
     *
     * @return
     */
    @Override
    public float getY() {
        return 0;
    }

    /**
     *
     * @return
     */
    @Override
    public float getW() {
        return 0;
    }

    /**
     *
     * @return
     */
    @Override
    public float getH() {
        return 0;
    }

    /**
     *
     * @return
     */
    @Override
    public IBorder getDefaultBorder() {
        return cNull;
    }

    /**
     *
     * @return
     */
    @Override
    public IActiveBorder getActiveBorder() {
        return cNull;
    }

    /**
     *
     * @return
     */
    @Override
    public ISelectedBorder getSelectedBorder() {
        return cNull;
    }

    /**
     *
     * @return
     */
    @Override
    public IActiveSelectedBorder getActiveSelectedBorder() {
        return cNull;
    }
}
