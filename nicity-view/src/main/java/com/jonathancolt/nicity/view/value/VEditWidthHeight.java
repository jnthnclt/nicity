/*
 * VEditWidthHeight.java.java
 *
 * Created on 03-12-2010 06:40:12 PM
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

import com.jonathancolt.nicity.core.memory.struct.SWH_I;
import com.jonathancolt.nicity.core.value.Value;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VButton;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.interfaces.IEvent;

/**
 *
 * @author Administrator
 */
public class VEditWidthHeight extends Viewer {

    Value<Integer> width, height;
    VPickScreenResolution resolution;

    /**
     *
     * @param _width
     * @param _height
     */
    public VEditWidthHeight(Value<Integer> _width, Value<Integer> _height) {
        width = _width;
        height = _height;
        resolution = new VPickScreenResolution(null) {

            @Override
            public void modified(SWH_I _resolution) {
                width.setValue(_resolution.w);
                height.setValue(_resolution.h);
            }
        };
        VChain c = new VChain(UV.cEW);
        c.add(new VEditValue("W:", width, "", false));
        c.add(new VEditValue(" x H:", height, "", false));
        c.add(new VButton(" + ") {
            @Override
            public void picked(IEvent _e) {
                UV.popup(this, UV.cCC, resolution.popup(false), true,true);
            }
        });
        setContent(c);
    }
}
