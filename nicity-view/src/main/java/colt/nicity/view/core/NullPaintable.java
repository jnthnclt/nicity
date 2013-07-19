/*
 * NullPaintable.java.java
 *
 * Created on 01-03-2010 01:31:34 PM
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
package colt.nicity.view.core;

import colt.nicity.core.memory.struct.XYWH_I;
import colt.nicity.view.interfaces.ICanvas;
import colt.nicity.view.interfaces.IPaintable;

/**
 *
 * @author Administrator
 */
public class NullPaintable implements IPaintable {

    /**
     *
     */
    public static final NullPaintable cNull = new NullPaintable();

    private NullPaintable() {
    }

    
    @Override
    public void paint(ICanvas g, XYWH_I xywh) {
    }

    @Override
    public float getW(IPaintable _under, IPaintable _over) {
        return 0;
    }

    @Override
    public float getH(IPaintable _under, IPaintable _over) {
        return 0;
    }
}
