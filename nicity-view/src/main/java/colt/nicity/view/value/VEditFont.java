/*
 * VEditFont.java.java
 *
 * Created on 03-12-2010 06:41:49 PM
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
package colt.nicity.view.value;

import colt.nicity.core.value.Value;
import colt.nicity.view.core.AFont;
import colt.nicity.view.core.UV;
import colt.nicity.view.core.ViewColor;
import colt.nicity.view.core.Viewer;
import colt.nicity.view.interfaces.IView;
import colt.nicity.view.rpp.IRPPViewable;

/**
 *
 * @author Administrator
 */
public class VEditFont extends Viewer implements IRPPViewable {
    
    public static IView viewable(String[] args) {
        ViewColor.onGray();
        return new VEditFont(new Value<>(UV.fonts[UV.cText]));
    }
    /**
     *
     * @param _args
     */
    public static void main(String[] _args) {
        ViewColor.onGray();
        //VOpenWindowsGraph.frame(null, new Viewer(new VEditFont(new Value<AFont>(UV.fonts[UV.cText]))), "");
        UV.exitFrame(new Viewer(viewable(_args)), "");
    }
    Value<AFont> font;

    /**
     *
     * @param _font
     */
    public VEditFont(Value<AFont> _font) {
        font = _font;
        setContent(new VFontBrowser(font));
    }
}
