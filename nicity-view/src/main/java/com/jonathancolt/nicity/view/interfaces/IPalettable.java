/*
 * IPalettable.java.java
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
package com.jonathancolt.nicity.view.interfaces;

import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.view.value.VColorPalette;

/**
 *
 * @author Administrator
 */
public interface IPalettable {

    /**
     *
     * @param _
     * @param _vp
     */
    public void palette(IOut _, VColorPalette _vp);
}
