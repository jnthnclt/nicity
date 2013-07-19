/*
 * NullPlacers.java.java
 *
 * Created on 01-03-2010 01:31:39 PM
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
package colt.nicity.view.core;

import colt.nicity.core.memory.struct.WH_F;
import colt.nicity.core.memory.struct.XYWH_I;
import colt.nicity.view.event.AViewEvent;
import colt.nicity.view.interfaces.ICanvas;
import colt.nicity.view.interfaces.IPlacer;
import colt.nicity.view.interfaces.IPlacers;
import colt.nicity.view.interfaces.IView;

/**
 *
 * @author Administrator
 */
public class NullPlacers implements IPlacers {

    /**
     *
     */
    public static final IPlacers cNull = new NullPlacers();

    @Override
    public IView disbatchEventToPlacers(IView parent, AViewEvent e) {
        return NullView.cNull;
    }

    @Override
    public void placeInside(IView parent, IView anchor, WH_F size, Flex _flex) {
    }

    @Override
    public void paintPlacers(IView _parent, ICanvas g, Layer _layer, int mode, XYWH_I _painted) {
    }

    @Override
    public IView transferFocusToChild(long _who) {
        return NullView.cNull;
    }

    @Override
    public IPlacer addPlacer(IPlacer value) {
        return NullPlacer.cNull;
    }

    @Override
    public IPlacer findPlacer(IPlacer value) {
        return NullPlacer.cNull;
    }

    @Override
    public IPlacer findView(IView view) {
        return NullPlacer.cNull;
    }

    @Override
    public IPlacer removePlacer(IPlacer value) {
        return NullPlacer.cNull;
    }

    @Override
    public IPlacer removeView(IView view) {
        return NullPlacer.cNull;
    }

    @Override
    public int size() {
        return 0;
    }

    /**
     *
     * @return
     */
    public boolean isArray() {
        return false;
    }

    /**
     *
     * @return
     */
    public Object getPlacers() {
        return NullPlacer.cNull;
    }

    @Override
    public Object[] toArray() {
        return new IPlacer[0];
    }

    @Override
    public void clear() {
    }
}
