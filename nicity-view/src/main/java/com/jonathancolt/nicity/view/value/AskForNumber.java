/*
 * AskForNumber.java.java
 *
 * Created on 03-12-2010 06:40:54 PM
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

import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.value.LockValue;
import com.jonathancolt.nicity.core.value.Value;
import com.jonathancolt.nicity.view.border.ViewBorder;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.VString;
import com.jonathancolt.nicity.view.list.AItem;

/**
 *
 * @author Administrator
 */
public class AskForNumber extends AItem {

    Value number;

    /**
     *
     * @param _label
     * @param _initial
     */
    public AskForNumber(Object _label, Value _initial) {
        number = _initial;
        VChain c = new VChain(UV.cEW);
        c.add(new VString(_label));
        c.add(new VEditValue(number));
        setContent(c);
        setBorder(new ViewBorder());
    }

    /**
     *
     * @param _message
     * @param _
     * @return
     */
    public Value askFor(String[] _message, IOut _) {
        final LockValue modal = new LockValue();
        new AcceptDecline(_message, this) {

            @Override
            public void accept() {
                modal.setValue(new Integer(1));
            }

            @Override
            public void decline() {
                modal.setValue(new Integer(0));
            }
        }.toFront(null);
        int mode = ((Integer) modal.getValue()).intValue();
        if (mode == 1) {
            return number;
        }
        return null;
    }
}
