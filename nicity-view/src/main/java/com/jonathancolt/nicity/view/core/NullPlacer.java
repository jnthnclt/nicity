/*
 * NullPlacer.java.java
 *
 * Created on 01-03-2010 01:31:35 PM
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

import com.jonathancolt.nicity.core.memory.struct.WH_F;
import com.jonathancolt.nicity.core.memory.struct.XYWH_I;
import com.jonathancolt.nicity.view.event.AViewEvent;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.interfaces.IPlacer;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.interfaces.IViewable;

/**
 *
 * @author Administrator
 */
public class NullPlacer implements IPlacer {

    /**
     *
     */
    public static final IPlacer cNull = new NullPlacer();

    @Override
    public IPlacer getPlacer() {
        return cNull;
    }

    @Override
    public IViewable getViewable() {
        return null;
    }

    @Override
    public void setViewable(IViewable viewable) {
    }

    @Override
    public Place getPlace() {
        return UV.cOrigin;
    }

    @Override
    public void setPlace(Place _place) {
    }

    @Override
    public void placeInside(IView parent, WH_F size, Flex _flex) {
    }

    @Override
    public void placeInside(IView parent, IView view, WH_F size, Flex _flex) {
    }

    @Override
    public void paintPlacer(IView _parent, ICanvas g, Layer _layer, int mode, XYWH_I _painted) {
    }

    @Override
    public IView disbatchEvent(IView parent, AViewEvent e) {
        return NullView.cNull;
    }

    @Override
    public IView transferFocusToChild(long _who) {
        return NullView.cNull;
    }

    // IViewable
    @Override
    public IView getView() {
        return NullView.cNull;
    }
}
