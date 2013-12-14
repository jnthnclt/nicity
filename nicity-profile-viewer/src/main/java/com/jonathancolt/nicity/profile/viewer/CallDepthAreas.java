/*
 * Copyright 2013 jonathan.
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
package com.jonathancolt.nicity.profile.viewer;

/*
 * #%L
 * nicity-profile-viewer
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

import com.jonathancolt.nicity.core.lang.MinMaxLong;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.ViewColor;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.paint.UPaint;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jonathan
 */
public class CallDepthAreas {
    Map<String, InterfaceArea> areas = new HashMap<>();
    String name = "";
    int x;
    int y;
    int w;
    int h;
    InterfaceArea[] paintAreas;
    long total;
    ValueStrategy valueStrategy;
    ValueStrategy stackStrategy;
    ValuesHistogram valuesHistogram;

    public CallDepthAreas(ValueStrategy valueStrategy, ValueStrategy stackStrategy, ValuesHistogram valuesHistogram) {
        this.valueStrategy = valueStrategy;
        this.stackStrategy = stackStrategy;
        this.valuesHistogram = valuesHistogram;
    }

    public void assignArea(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void add(String className, InterfaceArea callArea) {
        areas.put(className, callArea);
    }

    public long normalize() {
        long max = 0;
        for (InterfaceArea callArea : areas.values()) {
            long v = valueStrategy.value(callArea);
            callArea.value = v;
            if (max < v) {
                max = v;
            }
            total += v;
        }
        for (InterfaceArea callArea : areas.values()) {
            callArea.maxValue = max;
        }
        return max;
    }

    public void histogram(long maxValue) {
        for (InterfaceArea callArea : areas.values()) {
            valuesHistogram.value(MinMaxLong.zeroToOne(0, maxValue, callArea.value));
        }
    }

    // expected normalize() to have been called
    public void stack() {
        paintAreas = areas.values().toArray(new InterfaceArea[0]);
        Arrays.sort(paintAreas, new Comparator<InterfaceArea>() {
            @Override
            public int compare(InterfaceArea o1, InterfaceArea o2) {
                return -new Long(stackStrategy.value(o1)).compareTo(stackStrategy.value(o2));
            }
        });
    }

    // expected stack() to have been called
    public void size() {
        int ay = y;
        for (InterfaceArea interfaceArea : paintAreas) {
            int ah = (int) (h * MinMaxLong.zeroToOne(0, total, interfaceArea.value));
            interfaceArea.x = x;
            interfaceArea.y = ay;
            interfaceArea.w = w;
            interfaceArea.h = ah;
            ay += ah;
        }
        for (InterfaceArea interfaceArea : paintAreas) {
            interfaceArea.sizeMethods();
        }
    }

    // expected size() to hae been called
    public void paint(ICanvas _g) {
        _g.setColor(ViewColor.cThemeFont);
        _g.oval(true, x + w, y + h, 7, 7);
        _g.setFont(UV.fonts[UV.cTitle]);
        UPaint.string(_g, name, UV.fonts[UV.cTitle], x + w, y + h, UV.cWW, 45);
    }

}
