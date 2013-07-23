/*
 * NullView.java.java
 *
 * Created on 01-03-2010 01:31:36 PM
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

import com.jonathancolt.nicity.core.memory.struct.WH_F;
import com.jonathancolt.nicity.core.memory.struct.XYWH_I;
import com.jonathancolt.nicity.core.memory.struct.XY_I;
import com.jonathancolt.nicity.view.border.NullBorder;
import com.jonathancolt.nicity.view.event.AViewEvent;
import com.jonathancolt.nicity.view.interfaces.IBorder;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IPlacer;
import com.jonathancolt.nicity.view.interfaces.IPlacers;
import com.jonathancolt.nicity.view.interfaces.IPopup;
import com.jonathancolt.nicity.view.interfaces.IRootView;
import com.jonathancolt.nicity.view.interfaces.IToolTip;
import com.jonathancolt.nicity.view.interfaces.IView;

/**
 *
 * @author Administrator
 */
public class NullView implements IView {

    /**
     *
     */
    public static final NullView cNull = new NullView();

    @Override
    public IRootView getRootView() {
        return NullRootView.cNull;
    }

    @Override
    public IView getParentView() {
        return NullView.cNull;
    }

    @Override
    public void setParentView(IView parent) {
    }

    @Override
    public IView getView() {
        return NullView.cNull;
    }

    @Override
    public void setView(IView view) {
    }

    @Override
    public IPlacer getPlacer() {
        return NullPlacer.cNull;
    }

    @Override
    public void setPlacer(IPlacer placer) {
    }

    @Override
    public IView getContent() {
        return NullView.cNull;
    }

    @Override
    public void setContent(IView view) {
    }

    @Override
    public IPlacers getPlacers() {
        return NullPlacers.cNull;
    }

    @Override
    public void setPlacers(IPlacers placers) {
    }

    @Override
    public IView place(IView _child, Place _place) {
        return NullView.cNull;
    }

    @Override
    public IView place(IView _child, Place _place, Flex _flex) {
        return NullView.cNull;
    }

    @Override
    public IView place(IView _child, Place _place, Flex _interior, Flex _exterior) {
        return NullView.cNull;
    }

    @Override
    public void add(IPlacer placer) {
    }

    @Override
    public IPlacer find(IView view) {
        return NullPlacer.cNull;
    }

    @Override
    public IPlacer remove(IPlacer placer) {
        return NullPlacer.cNull;
    }

    @Override
    public void grabFocus(long _who) {
    }

    @Override
    public void grabHardFocus(long _who) {
    }

    @Override
    public void releaseHardFocus(long _who) {
    }

    @Override
    public IView transferFocusToParent(long _who) {
        return NullView.cNull;
    }

    @Override
    public IView transferFocusToChild(long _who) {
        return NullView.cNull;
    }

    @Override
    public IView transferFocusToNearestNeighbor(long _who, int direction) {
        return NullView.cNull;
    }

    @Override
    public IView spans(int spanMasks) {
        return NullView.cNull;
    }

    @Override
    public void unspans(int spanMasks) {
    }

    @Override
    public void disableFlag(int flagMask) {
    }

    @Override
    public void enableFlag(int flagsToEnable) {
    }

    @Override
    public boolean hasFlag(int flag) {
        return false;
    }

    @Override
    public void layoutInterior() {
    }

    @Override
    public void layoutAllInterior() {
    }

    @Override
    public void layoutInterior(Flex _flex) {
    }

    @Override
    public void layoutInterior(IView parent, Flex _flex) {
    }

    @Override
    public void layoutExterior(WH_F size, Flex _flex) {
    }

    @Override
    public void paint(IView _parent, ICanvas g, Layer _layer, int mode, XYWH_I _painted) {
    }

    @Override
    public void paintBackground(ICanvas g, int _x, int _y, int _w, int _h) {
    }

    @Override
    public void paintBody(ICanvas g, Layer _layer, int mode, XYWH_I _painted) {
    }

    @Override
    public void paintBorder(ICanvas g, int _x, int _y, int _w, int _h) {
    }

    @Override
    public void mend() {
    }

    @Override
    public void repair() {
    }

    @Override
    public void flush() {
    }

    @Override
    public void paint() {
    }

    @Override
    public void scrollTo(float _x, float _y, float _w, float _h) {
    }

    @Override
    public boolean isVisible(int _x, int _y, int _w, int _h) {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    // Location
    /**
     *
     * @return
     */
    public XY_I getNE() {
        return new XY_I(0, 0);
    }

    /**
     *
     * @return
     */
    public XY_I getNW() {
        return new XY_I(0, 0);
    }

    /**
     *
     * @return
     */
    public XY_I getSE() {
        return new XY_I(0, 0);
    }

    /**
     *
     * @return
     */
    public XY_I getSW() {
        return new XY_I(0, 0);
    }

    /**
     *
     * @return
     */
    public XY_I getCenter() {
        return new XY_I(0, 0);
    }

    /**
     *
     * @return
     */
    public XY_I getLocation() {
        return new XY_I(0, 0);
    }

    @Override
    public XY_I getLocationOnScreen() {
        return new XY_I(0, 0);
    }

    @Override
    public XY_I getLocationInWindow() {
        return new XY_I(0, 0);
    }

    @Override
    public WH_F getSize() {
        return new WH_F(0, 0);
    }

    /**
     *
     * @return
     */
    public XYWH_I getBounds() {
        return new XYWH_I(0, 0, 0, 0);
    }

    @Override
    public XYWH_I getEventBounds() {
        return new XYWH_I(0, 0, 0, 0);
    }

    @Override
    public float getOriginX() {
        return getX();
    }

    @Override
    public float getOriginY() {
        return getY();
    }

    @Override
    public float getX() {
        return 0.0f;
    }

    @Override
    public float getY() {
        return 0.0f;
    }

    @Override
    public float getW() {
        return 0.0f;
    }

    @Override
    public float getH() {
        return 0.0f;
    }

    @Override
    public void setLocation(float x, float y) {
    }

    @Override
    public void setX(float x) {
    }

    @Override
    public void setY(float y) {
    }

    // Events
    @Override
    public IView disbatchEvent(IView _parent, AViewEvent _event) {
        return NullView.cNull;
    }

    @Override
    public void promoteEvent(IEvent _task) {
    }

    @Override
    public IToolTip getToolTip() {
        return NullToolTip.cNull;
    }

    @Override
    public void setToolTip(IToolTip _toolTip) {
    }

    @Override
    public IBorder getBorder() {
        return NullBorder.cNull;
    }

    @Override
    public void setBorder(IBorder border) {
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public void selectBorder() {
    }

    @Override
    public void deselectBorder() {
    }

    @Override
    public void activateBorder() {
    }

    @Override
    public void deactivateBorder(IView view) {
    }

    @Override
    public IPopup getPopup() {
        return NullPopup.cNull;
    }

    @Override
    public void setPopup(IPopup popup) {
    }

    @Override
    public long getEventMask() {
        return 0;
    }

    @Override
    public void enableEvents(long tasksToEnable) {
    }

    @Override
    public void disableEvents(long tasksToDisable) {
    }

    @Override
    public boolean isEventEnabled(long isMask) {
        return false;
    }

    @Override
    public Object processEvent(IEvent task) {
        return null;
    }

    @Override
    public void frame(IView _view, Object _title) {
    }
}
