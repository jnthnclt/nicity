/*
 * Input3D.java.java
 *
 * Created on 01-03-2010 01:34:24 PM
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
package com.jonathancolt.nicity.view.list;

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

import com.jonathancolt.nicity.core.lang.BitMasks;
import com.jonathancolt.nicity.core.lang.LongBits;
import com.jonathancolt.nicity.view.adaptor.IKeyEventConstants;
import com.jonathancolt.nicity.view.border.ButtonBorder;
import com.jonathancolt.nicity.view.core.Placer;
import com.jonathancolt.nicity.view.event.AInputEvent;
import com.jonathancolt.nicity.view.event.KeyPressed;
import com.jonathancolt.nicity.view.event.KeyReleased;
import com.jonathancolt.nicity.view.event.KeyTyped;
import com.jonathancolt.nicity.view.event.MouseDragged;
import com.jonathancolt.nicity.view.event.MouseMoved;
import com.jonathancolt.nicity.view.interfaces.IKeyEvents;
import com.jonathancolt.nicity.view.interfaces.IMouseMotionEvents;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.threeD.IObject3D;


/**
 *
 * @author Administrator
 */
public class Input3D extends AItem implements IMouseMotionEvents, IKeyEvents {
    /**
     *
     */
    public static final double oneDegree = Math.toRadians(1.0);
    
    /**
     *
     */
    public static long cWalk = BitMasks.c1;
    /**
     *
     */
    public static long cPan = BitMasks.c2;
    /**
     *
     */
    public static long cLook = BitMasks.c3;
   
    IView repair;
    
    // Input is an Object3D movement controller.
    
    private IObject3D it;
    
    /**
     *
     * @param _it
     * @param _view
     * @param _repair
     */
    public Input3D(IObject3D _it, IView _view, IView _repair) {
        it = _it;
        repair = _repair;
        setPlacer(new Placer(_view));
        setBorder(new ButtonBorder());
    }
    
    /**
     *
     * @param _it
     */
    public void set(IObject3D _it) {
        it = _it;
    }
    
    /**
     *
     * @param _x
     * @param _y
     * @param _mode
     */
    public void change(double _x, double _y, long _mode) {
        if (LongBits.hasBit(_mode, cLook)) {
            it.change(0, 0, 0, Math.toRadians(_x), Math.toRadians(_y), 0);
        }
        if (LongBits.hasBit(_mode, cPan)) {
            it.change(_x, _y, 0, 0, 0, 0);
        }
        if (LongBits.hasBit(_mode, cWalk)) {
            it.change(0, 0, _y, 0, Math.toRadians(_x), 0);
        }
        repair.paint();
    }
    
    private static long mode(AInputEvent _e) {
        if (_e.isShiftDown()) {
            return cLook;
        } else if (_e.isControlDown()) {
            return cPan;
        } else {
            return cWalk;
        }
    }
    
    // IMouseMotionEvents
    
    /**
     *
     * @param _e
     */
    @Override
    public void mouseMoved(MouseMoved _e) {
    }
    /**
     *
     * @param _e
     */
    @Override
    public void mouseDragged(MouseDragged _e) {
        double deltaX = _e.getDeltaX();
        double deltaY = _e.getDeltaY();
        change(deltaX, deltaY, mode(_e));
    }
   
    // IKeyEvents
    
    /**
     *
     */
    public double step = 5;
    /**
     *
     * @param _e
     */
    @Override
    public void keyPressed(KeyPressed _e) {
        int code = _e.getKeyCode();
        if (code == IKeyEventConstants.cRight) {
            change(step, 0, mode(_e));
        }
        if (code == IKeyEventConstants.cLeft) {
            change(-step, 0, mode(_e));
        }
        if (code == IKeyEventConstants.cUp) {
            change(0, step, mode(_e));
        }
        if (code == IKeyEventConstants.cDown) {
            change(0, -step, mode(_e));
        }
    }
    /**
     *
     * @param e
     */
    @Override
    public void keyReleased(KeyReleased e) {
    }
    /**
     *
     * @param e
     */
    @Override
    public void keyTyped(KeyTyped e) {
    }
    
}
