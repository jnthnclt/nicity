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
import com.jonathancolt.nicity.core.memory.struct.XYWH_I;
import com.jonathancolt.nicity.profile.server.model.Call;
import com.jonathancolt.nicity.profile.server.model.CallClass;
import com.jonathancolt.nicity.profile.server.model.ClassMethod;
import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.ViewColor;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.paint.UPaint;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author jonathan
 */
public class InterfaceArea {
    final CallClass callClass;
    final ValueStrategy valueStrategy;
    final Set<String> pinnedClass;
    Set<Integer> depths = Collections.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>());
    int x;
    int y;
    int w;
    int h;
    long value;
    long maxValue;
    AColor color;
    int calledByCount;
    Set<Call> callsTo = new HashSet<>();
    ConcurrentHashMap<String, ClassMethod> classMethods = new ConcurrentHashMap<>();

    public InterfaceArea(CallClass callClass, ValueStrategy valueStrategy, Set<String> pinnedClass) {
        this.callClass = callClass;
        this.valueStrategy = valueStrategy;
        this.pinnedClass = pinnedClass;
        callsTo.addAll(callClass.getCalls());
        classMethods.putAll(callClass.getClassMethods());
    }

    String getName() {
        return callClass.getName();
    }

    public int averageDepth() {
        int sum = 0;
        for (int d : depths) {
            sum += d;
        }
        return sum / depths.size();
    }

    public void joinCallsTo(int depth, CallClass add) {
        depths.add(depth);
        callsTo.addAll(add.getCalls());
        classMethods.putAll(add.getClassMethods());
    }

    XYWH_I rect(double s, double ph) {
        int _y = y + (int) (h * s);
        int _h = (int) (h * ph);
        return new XYWH_I(x, _y, w, _h);
    }

    void paint(ICanvas _g, InterfaceArea _over) {
        _g.setAlpha(1f, 0);
        if (_over != null || pinnedClass.size() > 0) {
            if (_over != null && _over.callClass != null && !this.getName().equals(_over.getName())) {
                _g.setAlpha(0.25f, 0);
            }
        }
        VCallDepthStack.interfaceBar.paintFlavor(_g, x, y, w, h, color);
        _g.setColor(ViewColor.cThemeFont);
        _g.oval(true, x + w, y + h, 7, 7);
        _g.setFont(UV.fonts[UV.cText]);
        UPaint.string(_g, "    " + valueStrategy.name(value), UV.fonts[UV.cSmall], x + w, y, UV.cWW, 45);
        for (MethodArea methodArea : methodAreas) {
            methodArea.paint(_g);
        }
        _g.setAlpha(1f, 0);
    }
    MethodArea[] methodAreas = new MethodArea[0];
    Map<String, MethodArea> mapMethodAreas = new HashMap<>();

    public void sizeMethods() {
        ClassMethod[] cms = classMethods.values().toArray(new ClassMethod[0]);
        methodAreas = new MethodArea[cms.length];
        long total = 0;
        for (int i = 0; i < methodAreas.length; i++) {
            methodAreas[i] = new MethodArea(this, cms[i]);
            mapMethodAreas.put(methodAreas[i].classMethod.getMethodName(), methodAreas[i]);
            total += methodAreas[i].value;
        }
        long t = 0;
        for (int i = 0; i < methodAreas.length; i++) {
            long v = methodAreas[i].value;
            float p = (float) t / (float) total;
            float ph = (float) MinMaxLong.zeroToOne(0, total, v);
            methodAreas[i].x = x;
            methodAreas[i].y = y + (int) (h * p);
            methodAreas[i].w = (w / 2);
            methodAreas[i].h = (int) (ph * h);
            t += v;
        }
    }

    class MethodArea {

        InterfaceArea interfaceArea;
        ClassMethod classMethod;
        int x;
        int y;
        int w;
        int h;
        long value;

        public MethodArea(InterfaceArea interfaceArea, ClassMethod classMethod) {
            this.interfaceArea = interfaceArea;
            this.classMethod = classMethod;
            this.value = 1; //classMethod.getSuccesslatency();
        }

        public void paint(ICanvas _g) {
            VCallDepthStack.methodBar.paintFlavor(_g, x, y, w, h, AColor.gray);
            if (interfaceArea.depths.contains(0)) {
                _g.setColor(AColor.gray);
                _g.line(x + (w / 2) - 32, y + (h / 2), x + (w / 2) - 3, y + (h / 2));
                _g.setColor(AColor.green);
                _g.oval(true, x + (w / 2) - 32, y + (h / 2) - 3, 6, 6);
            }
        }
    }

}
