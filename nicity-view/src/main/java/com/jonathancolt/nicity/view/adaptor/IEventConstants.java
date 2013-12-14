/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonathancolt.nicity.view.adaptor;

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

import java.awt.Event;
import java.awt.event.InputEvent;

/**
 *
 * @author jonathan
 */
public interface IEventConstants {
    public static final int cShiftMask = Event.SHIFT_MASK;
    public static final int cCrtlMask = Event.CTRL_MASK;
    public static final int cMetaMask = Event.META_MASK;
    public static final int cAltMask = Event.ALT_MASK;
    public static final int cButton1DownMask = InputEvent.BUTTON1_DOWN_MASK;
    public static final int cButton1Mask = InputEvent.BUTTON1_MASK;
    public static final int cButton2Mask = InputEvent.BUTTON2_MASK;
    public static final int cButton3Mask = InputEvent.BUTTON3_MASK;
}
