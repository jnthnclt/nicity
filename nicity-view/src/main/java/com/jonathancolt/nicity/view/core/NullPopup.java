/*
 * NullPopup.java.java
 *
 * Created on 01-03-2010 01:31:33 PM
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

import com.jonathancolt.nicity.core.memory.struct.XY_I;
import com.jonathancolt.nicity.view.interfaces.IPopup;
import com.jonathancolt.nicity.view.interfaces.IView;

/**
 *
 * @author Administrator
 */
public class NullPopup implements IPopup {

    /**
     *
     */
    public static final IPopup cNull = new NullPopup();

    /**
     *
     * @param popupParent
     */
    public void setPopupParent(IView popupParent) {
    }

    /**
     *
     * @return
     */
    public IView getPopupParent() {
        return NullView.cNull;
    }

    @Override
    public void showPopup(XY_I locationOnScreen, IView popupParent) {
    }

    @Override
    public void hidePopup() {
    }
}
