/*
 * AViewable.java.java
 *
 * Created on 01-03-2010 01:31:34 PM
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

import com.jonathancolt.nicity.core.lang.ASetObject;
import com.jonathancolt.nicity.core.memory.struct.WH_F;
import com.jonathancolt.nicity.core.memory.struct.XYWH_I;
import com.jonathancolt.nicity.core.memory.struct.XY_I;
import com.jonathancolt.nicity.view.adaptor.IViewEventConstants;
import com.jonathancolt.nicity.view.border.NullBorder;
import com.jonathancolt.nicity.view.border.UBorder;
import com.jonathancolt.nicity.view.event.AViewEvent;
import com.jonathancolt.nicity.view.event.UEvent;
import com.jonathancolt.nicity.view.interfaces.IBorder;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IFocusEvents;
import com.jonathancolt.nicity.view.interfaces.IKeyEvents;
import com.jonathancolt.nicity.view.interfaces.IMouseEvents;
import com.jonathancolt.nicity.view.interfaces.IMouseMotionEvents;
import com.jonathancolt.nicity.view.interfaces.IPlacer;
import com.jonathancolt.nicity.view.interfaces.IPlacers;
import com.jonathancolt.nicity.view.interfaces.IPopup;
import com.jonathancolt.nicity.view.interfaces.IRootView;
import com.jonathancolt.nicity.view.interfaces.IToolTip;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.interfaces.IWindowEvents;

/**
 *
 * @author Administrator
 */
public abstract class AViewable extends ASetObject implements IView {

    /**
     *
     */
    public AViewable() {
    }

    /**
     *
     * @return
     */
    @Override
    public Object hashObject() {
        return this;
    }

    @Override
    public IRootView getRootView() {
        return getParentView().getRootView();
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
        return this;
    }

    @Override
    public void setView(IView view) {
        throw new RuntimeException("Unsupported");
    }

    @Override
    public IView getContent() {
        return this;
    }

    @Override
    public void setContent(IView view) {
    }

    @Override
    public IPlacer getPlacer() {
        return NullPlacer.cNull;
    }

    @Override
    public void setPlacer(IPlacer placer) {
    }

    @Override
    public IPlacers getPlacers() {
        return NullPlacers.cNull;
    }

    @Override
    public void setPlacers(IPlacers placers) {
        throw new RuntimeException("Unsupported");
    }

    // public void place(IView _child);
    @Override
    public IView place(IView _child, Place _place) {
        throw new RuntimeException("Unsupported");
    }

    @Override
    public IView place(IView _child, Place _place, Flex _flex) {
        throw new RuntimeException("Unsupported");
    }

    @Override
    public IView place(IView _child, Place _place, Flex _interior, Flex _exterior) {
        throw new RuntimeException("Unsupported");
    }

    @Override
    public void add(IPlacer placer) {
        throw new RuntimeException("Unsupported");
    }

    @Override
    public IPlacer find(IView view) {
        throw new RuntimeException("Unsupported");
    }

    @Override
    public IPlacer remove(IPlacer placer) {
        throw new RuntimeException("Unsupported");
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
        return this;
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
    public void layoutInterior(IView _parent, Flex _flex) {
    }

    @Override
    public void layoutExterior(WH_F size, Flex _flex) {
    }

    @Override
    public void paint(IView _parent, ICanvas g, Layer _layer, int _mode, XYWH_I _painted) {
        int tx = (int) (_layer.tx + _parent.getBorder().getX());
        int ty = (int) (_layer.ty + _parent.getBorder().getY());
        g.translate(tx, ty);
        paintBody(g, _layer, _mode, _painted);
        g.translate(-tx, -ty);
    }

    @Override
    public void paintBackground(ICanvas g, int _x, int _y, int _w, int _h) {
    }

    @Override
    public void paintBody(ICanvas g, Layer _layer, int mode, XYWH_I _painted) {
        if (mode == UV.cRepair) {
            _painted.union((int) _layer.x(), (int) _layer.y(), (int) _layer.w(), (int) _layer.h());
        }
    }

    @Override
    public void paintBorder(ICanvas g, int _x, int _y, int _w, int _h) {
    }

    /**
     *
     * @param _parent
     * @param g
     * @param _layer
     * @param _mode
     * @param _painted
     */
    public void paintPlacer(IView _parent, ICanvas g, Layer _layer, int _mode, XYWH_I _painted) {
        int tx = Math.round(_layer.tx + _parent.getBorder().getX());
        int ty = Math.round(_layer.ty + _parent.getBorder().getY());
        g.translate(tx, ty);
        paintBody(g, _layer, _mode, _painted);
        g.translate(-tx, -ty);
    }

    @Override
    public void mend() {
        getParentView().mend();
    }

    @Override
    public void repair() {
        getRootView().addToRepaint(this);
    }

    @Override
    public void flush() {
        getParentView().flush();
    }

    @Override
    public void paint() {
        repair();
        flush();
    }

    @Override
    public void scrollTo(float _x, float _y, float _w, float _h) {
        getParentView().scrollTo(_x + getX(), _y + getY(), _w, _h);
    }

    @Override
    public boolean isVisible(int _x, int _y, int _w, int _h) {
        return true;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void promoteEvent(IEvent _task) {
        IView parent = getParentView();
        if (parent != null) {
            parent.promoteEvent(_task);
        }
    }

    @Override
    public void frame(IView _view, Object _title) {
        IView parent = getParentView();
        if (parent != null) {
            parent.frame(_view, _title);
        }
    }

    // Location
    /**
     *
     * @return
     */
    public XY_I getNE() {
        return new XY_I((int) (getX() + getW()), (int) getY());
    }

    /**
     *
     * @return
     */
    public XY_I getNW() {
        return new XY_I((int) getX(), (int) getY());
    }

    /**
     *
     * @return
     */
    public XY_I getSE() {
        return new XY_I((int) (getX() + getW()), (int) (getY() + getH()));
    }

    /**
     *
     * @return
     */
    public XY_I getSW() {
        return new XY_I((int) getX(), (int) (getY() + getH()));
    }

    /**
     *
     * @return
     */
    public XY_I getCenter() {
        return new XY_I((int) getX() + (int) (getW() / 2), (int) getY() + (int) (getH() / 2));
    }

    /**
     *
     * @return
     */
    public XY_I getLocation() {
        return new XY_I((int) getX(), (int) getY());
    }

    @Override
    public XY_I getLocationOnScreen() {
        XY_I p = getParentView().getLocationOnScreen();
        p = new XY_I(p.x + (int) getOriginX(), p.y + (int) getOriginY());
        return p;
    }

    @Override
    public XY_I getLocationInWindow() {
        XY_I p = getParentView().getLocationInWindow();
        p = new XY_I(p.x + (int) getOriginX(), p.y + (int) getOriginY());
        return p;
    }

    /**
     *
     * @param _mousePoint
     * @return
     */
    public XY_I getScreenPoint(XY_I _mousePoint) {
        XY_I sp = getLocationOnScreen();
        sp.x += _mousePoint.x;
        sp.y += _mousePoint.y;
        return sp;
    }

    @Override
    public WH_F getSize() {
        return new WH_F(getW(), getH());
    }

    /**
     *
     * @return
     */
    public XYWH_I getBounds() {
        return new XYWH_I((int) getX(), (int) getY(), (int) getW(), (int) getH());
    }

    @Override
    public XYWH_I getEventBounds() {
        return new XYWH_I((int) getX(), (int) getY(), (int) getW(), (int) getH());
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
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public float getW() {
        return 0;
    }

    @Override
    public float getH() {
        return 0;
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

    // Placer
    /**
     *
     * @param _parent
     * @param _size
     * @param _flex
     */
    public void placeInside(IView _parent, WH_F _size, Flex _flex) {
        _size.max(getW(), getH());
    }

    // Events
    @Override
    public IView disbatchEvent(IView parent, AViewEvent event) {
        if (!isEventEnabled(event.getMask())) {
            return NullView.cNull;
        }
        event.setSource(this);
        return this;
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
        return getBorder().isActive();
    }

    @Override
    public boolean isSelected() {
        return getBorder().isSelected();
    }

    @Override
    public void selectBorder() {
        UBorder.selectBorder(this);
    }

    @Override
    public void deselectBorder() {
        UBorder.deselectBorder(this);
    }

    @Override
    public void activateBorder() {
        UBorder.activateBorder(this);
    }

    @Override
    public void deactivateBorder(IView view) {
        UBorder.deactivateBorder(this);
    }

    @Override
    public IPopup getPopup() {
        return NullPopup.cNull;
    }

    @Override
    public void setPopup(IPopup popup) {
        throw new RuntimeException("Unsupported");
    }

    @Override
    public long getEventMask() {
        long taskMask = 0;
        if (this instanceof IWindowEvents) {
            taskMask |= IViewEventConstants.cWindowEvent;
        }
        if (this instanceof IMouseEvents) {
            taskMask |= IViewEventConstants.cMouseEvent;
        }
        if (this instanceof IKeyEvents) {
            taskMask |= IViewEventConstants.cKeyEvent;
        }
        if (this instanceof IMouseMotionEvents) {
            taskMask |= IViewEventConstants.cMouseMotionEvent;
        }
        if (this instanceof IFocusEvents) {
            taskMask |= IViewEventConstants.cFocusEvent;
        }
        return taskMask;
    }

    @Override
    public void enableEvents(long tasksToEnable) {
    }

    @Override
    public void disableEvents(long tasksToDisable) {
    }

    @Override
    public boolean isEventEnabled(long isMask) {
        return (getEventMask() & isMask) == isMask;
    }

    @Override
    public Object processEvent(IEvent task) {
        return UEvent.processEvent(this, task);
    }
}
