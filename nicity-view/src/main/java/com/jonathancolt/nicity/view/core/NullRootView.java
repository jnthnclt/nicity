/*
 * NullRootView.java.java
 *
 * Created on 01-03-2010 01:31:37 PM
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

import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.view.interfaces.IPeerView;
import com.jonathancolt.nicity.view.interfaces.IRootView;
import com.jonathancolt.nicity.view.interfaces.IView;

/**
 *
 * @author Administrator
 */
public class NullRootView extends NullView implements IRootView {

    /**
     *
     */
    public static final IRootView cNull = new NullRootView();

    @Override
    public IPeerView getPeerView() {
        return null;
    }

    @Override
    public void setMouseWheelFocus(long _who, IView _mouseWheelFocus) {
    }

    @Override
    public IView getFocusedView(long _who) {
        return NullView.cNull;
    }

    @Override
    public void setFocusedView(long _who, IView view) {
    }

    @Override
    public IView getHardFocusedView(long _who) {
        return NullView.cNull;
    }

    @Override
    public void setHardFocusedView(long _who, IView view) {
    }

    @Override
    public void processEvent(IOut _,PrimativeEvent event) {
    }

    @Override
    public void toFront() {
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void addToRepaint(IView _view) {
    }

    /**
     *
     * @param _modal
     * @return
     */
    public IRootView aquireModal(IRootView _modal) {
        return this;
    }

    /**
     *
     * @param _modal
     * @return
     */
    public IRootView releaseModal(IRootView _modal) {
        return this;
    }

    @Override
    public void maximize() {
    }

    @Override
    public void iconify() {
    }
}
