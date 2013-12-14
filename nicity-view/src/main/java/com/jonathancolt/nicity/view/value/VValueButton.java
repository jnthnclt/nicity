/*
 * VValueButton.java.java
 *
 * Created on 01-03-2010 01:34:45 PM
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

import com.jonathancolt.nicity.view.core.Placer;
import com.jonathancolt.nicity.view.core.StringsPopup;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VButton;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.VString;
import com.jonathancolt.nicity.view.core.ViewString;
import com.jonathancolt.nicity.view.interfaces.IEvent;

/**
 *
 * @author Administrator
 */
public class VValueButton extends VButton {

    Object value;
    Object[] values;

    /**
     *
     * @param _pre
     * @param _post
     * @param _initial
     * @param _values
     */
    public VValueButton(String _pre, String _post, Object _initial, Object[] _values) {
        super();
        value = _initial;
        values = _values;
        VChain chain = new VChain(UV.cEW);
        if (_pre != null) {
            chain.add(new ViewString(_pre));
        }
        chain.add(new VString(this));
        if (_post != null) {
            chain.add(new ViewString(_post));
        }
        setPlacer(new Placer(chain));
    }

    /**
     *
     * @return
     */
    public int getInt() {
        try {
            return Integer.parseInt((String) value);
        } catch (Exception x) {
            x.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param _value
     */
    public void setValue(Object _value) {
        value = _value;
    }

    /**
     *
     * @return
     */
    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value == null) {
            return "null";
        }
        return value.toString();
    }

    /**
     *
     * @param _e
     */
    @Override
    public void picked(IEvent _e) {
        StringsPopup popup = new StringsPopup(values) {

            @Override
            public void setValue(Object _value) {
                VValueButton.this.setValue((String) _value);
            }
        };
        popup.show(this, UV.cCC);
    }
}
