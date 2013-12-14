/*
 * VColorWheelItem.java.java
 *
 * Created on 03-12-2010 06:41:24 PM
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

import com.jonathancolt.nicity.core.value.IValue;
import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.core.RigidBox;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.VString;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.list.AItem;

/**
 *
 * @author Administrator
 */
public class VColorWheelItem extends AItem {

    IValue colorValue;

    /**
     *
     * @param _name
     * @param _size
     * @param _value
     */
    public VColorWheelItem(Object _name, int _size, IValue _value) {
        colorValue = _value;
        IView name = null;
        if (_name == null) {
            _name = "";
        }
        if (_name instanceof IView) {
            name = (IView) name;
        } else {
            name = new VString(_name);
        }

        VChain c = new VChain(UV.cEW);
        c.add(new RigidBox(_size, _size) {

            @Override
            public void paintBorder(ICanvas _g, int _x, int _y, int _w, int _h) {
                AColor color = (AColor) colorValue.getValue();
                _g.setColor(color);
                _g.rect(true, _x, _y, _w, _h);
            }
        });
        c.add(name);
        setContent(c);
    }

    /**
     *
     * @param _e
     */
    @Override
    public void picked(IEvent _e) {
        VColorWheel colorWheel = new VColorWheel(colorValue, 300) {

            @Override
            public void selected(AColor _color) {
                getRootView().dispose();
                value.setValue(_color);//??
            }
        };
        UV.popup(this, _e, colorWheel, true,true);
    }
}
