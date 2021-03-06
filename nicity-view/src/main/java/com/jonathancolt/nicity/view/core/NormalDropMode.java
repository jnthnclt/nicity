/*
 * NormalDropMode.java.java
 *
 * Created on 01-03-2010 01:31:36 PM
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

import com.jonathancolt.nicity.view.interfaces.IDrop;
import com.jonathancolt.nicity.view.interfaces.IDropMode;
import com.jonathancolt.nicity.view.interfaces.IEvent;

/**
 *
 * @author Administrator
 */
public class NormalDropMode implements IDropMode {

    /**
     *
     */
    public static final NormalDropMode cNormal = new NormalDropMode();
    /**
     *
     */
    public IEvent e;

    @Override
    public void drop(IDrop address, Object parcel, IEvent _e) {
        e = _e;
        address.dropParcel(parcel, this);
    }

    @Override
    public IEvent getEvent() {
        return e;
    }
}
