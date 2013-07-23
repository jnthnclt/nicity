/*
 * Copyright 2013 jonathan.colt.
 *
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
 */
package com.jonathancolt.nicity.profile.visualize;

import com.jonathancolt.nicity.core.value.Value;
import com.jonathancolt.nicity.view.border.PopupBorder;
import com.jonathancolt.nicity.view.core.Place;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.VPickList;
import com.jonathancolt.nicity.view.core.VPopupButton;
import com.jonathancolt.nicity.view.core.VString;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.interfaces.IViewable;
import com.jonathancolt.nicity.view.value.VAlwaysOver;
import com.jonathancolt.nicity.view.value.VSlider;
import java.util.Iterator;

/**
 *
 */
public class VLatency extends Viewer {

    private final VCallDepthStack callDepthStack;

    public VLatency(final VCallDepthStack callDepthStack) {
        this.callDepthStack = callDepthStack;
        VChain c = new VChain(UV.cSWNW);
        c.add(new VPopupButton(new VString(new Object() {
            @Override
            public String toString() {
                Iterator<String> iterator = callDepthStack.selectedServiceNames.iterator();
                if (iterator.hasNext()) {
                    return iterator.next().toString();
                } else {
                    return "Pick Service";
                }
            }
        }, UV.fonts[UV.cText]), new IViewable() {
            public IView getView() {
                VPickList vPickList = new VPickList(callDepthStack.servicesCallDepthStack.getServiceNames().toArray(), new Value() {
                    @Override
                    public void setValue(Object _value) {
                        super.setValue(_value);
                        callDepthStack.selectedServiceNames.clear();
                        if (callDepthStack.selectedServiceNames.contains(_value)) {
                            callDepthStack.selectedServiceNames.remove(_value);
                        } else {
                            callDepthStack.selectedServiceNames.add((String) _value);
                        }
                    }
                });
                return vPickList;
            }
        }));
        c.add(new VAlwaysOver(UV.border(callDepthStack.pickedBackground, new PopupBorder(8)), callDepthStack, new Place(UV.cSESE, -16, -16)));

        c.add(new VSlider(new Value<Double>(0.0d) {
            @Override
            public void setValue(Double _value) {
                super.setValue(_value); //To change body of generated methods, choose Tools | Templates.
                callDepthStack.shift = value;
            }
        }, "", 1, 32, true));
        setContent(c);
    }
}
