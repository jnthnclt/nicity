/*
 * VEditNumericColor.java.java
 *
 * Created on 03-12-2010 06:42:03 PM
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

import com.jonathancolt.nicity.core.observer.AObserver;
import com.jonathancolt.nicity.core.observer.Change;
import com.jonathancolt.nicity.core.observer.IObservable;
import com.jonathancolt.nicity.core.value.Value;
import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VBox;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.VString;
import com.jonathancolt.nicity.view.core.ViewColor;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.list.VItem;

/**
 *
 * @author Administrator
 */
public class VEditNumericColor extends Viewer {
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        ViewColor.onGray();
        UV.exitFrame(new VEditNumericColor("color", new Value<>(new AColor(.5f, 0.5f, 0.5f))), "test");
    }
    
    Object name;
    Value<AColor> color;
    AObserver irgbaChanged;

    /**
     *
     * @param _name
     * @param _color
     */
    public VEditNumericColor(Object _name, Value<AColor> _color) {
        name = _name;
        color = _color;

        AColor c = color.getValue();
        final Value<Integer> ired = new Value<>(new Integer(c.getR()));
        final Value<Integer> igreen = new Value<>(new Integer(c.getG()));
        final Value<Integer> iblue = new Value<>(new Integer(c.getB()));
        final Value<Integer> ialp = new Value<>(new Integer(c.getA()));
        final Value<String> ihex = new Value<String>(c.getHexRGB()) {

            @Override
            public String getValue() {
                AColor c = color.getValue();
                return c.getHexRGB();
            }

            @Override
            public void setValue(String _value) {
                String hex = _value;
                if (hex.length() == 6) {
                    int r = Integer.parseInt(hex.substring(0, 2), 16);
                    int g = Integer.parseInt(hex.substring(2, 4), 16);
                    int b = Integer.parseInt(hex.substring(4, 6), 16);
                    AColor c = new AColor(r, g, b);
                    color.setValue(c);
                    ired.value(c.getR());
                    igreen.value(c.getG());
                    iblue.value(c.getB());
                    ialp.value(c.getA());
                    VEditNumericColor.this.paint();
                }
            }
        };

        irgbaChanged = new AObserver() {

            @Override
            public void change(Change _change) {
                AColor c = new AColor(ired.intValue(), igreen.intValue(), iblue.intValue(), ialp.intValue());
                color.setValue(c);
                ihex.value(c.getHexRGB());
                ired.value(c.getR());
                igreen.value(c.getG());
                iblue.value(c.getB());
                ialp.value(c.getA());
                VEditNumericColor.this.paint();
            }

            @Override
            public void bound(IObservable _observable) {
            }

            @Override
            public void released(IObservable _observable) {
            }
        };

        ired.bind(irgbaChanged);
        igreen.bind(irgbaChanged);
        iblue.bind(irgbaChanged);
        ialp.bind(irgbaChanged);

        VChain vc = new VChain(UV.cNENW);
        if (name != null) {
            vc.add(new VString(name));
        }
        vc.add(new VBox(100, 32, color));
        vc.add(new VEditValue("R:", ired, "", false, 40));
        vc.add(new VEditValue("G:", igreen, "", false, 40));
        vc.add(new VEditValue("B:", iblue, "", false, 40));
        vc.add(new VEditValue("A:", ialp, "", false, 40));
        vc.add(new VEditValue("Hex:", ihex, "", false, 60));
        vc.add(new VItem("*") {

            @Override
            public void picked(IEvent _e) {
                AColor c = AColor.randomPastel(127, 127);
                color.setValue(c);
            }
        });
        setContent(vc);
    }
}

