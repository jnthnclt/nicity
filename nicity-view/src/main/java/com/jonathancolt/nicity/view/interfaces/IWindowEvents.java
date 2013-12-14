/*
 * IWindowEvents.java.java
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
package com.jonathancolt.nicity.view.interfaces;

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

import com.jonathancolt.nicity.view.event.WindowActivated;
import com.jonathancolt.nicity.view.event.WindowClosed;
import com.jonathancolt.nicity.view.event.WindowDeactivated;
import com.jonathancolt.nicity.view.event.WindowDeiconified;
import com.jonathancolt.nicity.view.event.WindowIconified;
import com.jonathancolt.nicity.view.event.WindowOpened;

/**
 *
 * @author Administrator
 */
public interface IWindowEvents {
    /**
     *
     * @param _e
     */
    public void windowOpened(WindowOpened _e);
    /**
     *
     * @param _e
     */
    public void windowClosed(WindowClosed _e);
    /**
     *
     * @param _e
     */
    public void windowActivated(WindowActivated _e);
    /**
     *
     * @param _e
     */
    public void windowDeactivated(WindowDeactivated _e);
    /**
     *
     * @param _e
     */
    public void windowIconified(WindowIconified _e);
    /**
     *
     * @param _e
     */
    public void windowDeiconified(WindowDeiconified _e);
}
