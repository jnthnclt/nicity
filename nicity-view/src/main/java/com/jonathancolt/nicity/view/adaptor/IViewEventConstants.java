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

import java.awt.AWTEvent;

/**
 *
 * @author jonathan
 */
public interface IViewEventConstants {
    public final static long cFocusEvent = AWTEvent.FOCUS_EVENT_MASK;
    public final static long cKeyEvent = AWTEvent.KEY_EVENT_MASK;
    public final static long cMouseEvent = AWTEvent.MOUSE_EVENT_MASK;
    public final static long cMouseMotionEvent = AWTEvent.MOUSE_MOTION_EVENT_MASK;
    public final static long cWindowEvent = AWTEvent.WINDOW_EVENT_MASK;

}
