/*
 * VMode.java.java
 *
 * Created on 03-12-2010 06:39:14 PM
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
package com.jonathancolt.nicity.view.value;

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

import com.jonathancolt.nicity.core.value.Value;
import com.jonathancolt.nicity.view.core.NullView;
import com.jonathancolt.nicity.view.interfaces.IView;

/**
 *
 * @author Administrator
 */
public class VMode extends AVMode {

    private IView[] modes;

    /**
     *
     * @param _mode
     * @param _modes
     */
    public VMode(Value _mode, IView... _modes) {
        super(_mode);
        modes = _modes;
        refresh();
    }

    /**
     *
     * @param _index
     * @return
     */
    @Override
    public IView indexView(int _index) {
        if (modes == null || modes.length == 0) {
            return NullView.cNull;
        }
        return modes[_index];
    }

    /**
     *
     * @return
     */
    @Override
    public int maxIndex() {
        return modes.length;
    }
}
