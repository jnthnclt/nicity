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

import com.jonathancolt.nicity.core.lang.MinMaxDouble;
import com.jonathancolt.nicity.core.lang.MinMaxInt;
import com.jonathancolt.nicity.core.memory.struct.XYWH_I;
import com.jonathancolt.nicity.core.memory.struct.XY_I;
import com.jonathancolt.nicity.core.value.Value;
import com.jonathancolt.nicity.profile.server.ProfileViewerProvider;
import com.jonathancolt.nicity.profile.server.model.Call;
import com.jonathancolt.nicity.profile.server.model.CallClass;
import com.jonathancolt.nicity.profile.server.model.CallDepth;
import com.jonathancolt.nicity.profile.server.model.ClassMethod;
import com.jonathancolt.nicity.profile.server.model.ServiceModel;
import com.jonathancolt.nicity.view.border.ViewBorder;
import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.core.AFont;
import com.jonathancolt.nicity.view.core.RigidBox;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VPan;
import com.jonathancolt.nicity.view.core.VPickList;
import com.jonathancolt.nicity.view.core.VStatus;
import com.jonathancolt.nicity.view.core.ViewColor;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.event.MouseDragged;
import com.jonathancolt.nicity.view.event.MouseMoved;
import com.jonathancolt.nicity.view.event.MouseReleased;
import com.jonathancolt.nicity.view.flavor.AFlavor;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.paint.Tunnel;
import com.jonathancolt.nicity.view.paint.UPaint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author jonathan.colt
 */
public class VCallDepthStack extends VPan {

    private RollupCalls rollupCalls = new RollupCalls();
    static AFlavor interfaceBar = new BarFlavor(8);
    static AFlavor methodBar = new BarFlavor(3);
    boolean sortOrder = true;
    private Set<String> selectedServiceNames = new HashSet<>();
    final ProfileViewerProvider profileViewerProvider;
    VStatus status;
    InterfaceArea over;
    InterfaceArea lastOver;
    InterfaceArea.MethodArea overMethod;
    InterfaceArea.MethodArea lastOverMethod;
    final ValuesHistogram valuesHistogram = new ValuesHistogram();
    Set<String> hideClass = new HashSet<>();
    Set<String> pinnedClass = new HashSet<>();
    Map<String, InterfaceArea> unique = new ConcurrentHashMap<>();
    Double shift = 0.0d;
    int caw;
    private ValueStrategy valueStrategy = new ValueStrategy() {
        @Override
        public String name() {
            return "latency";
        }

        @Override
        public long value(InterfaceArea callArea) {
            ConcurrentHashMap<String, ClassMethod> classMethods = callArea.callClass.getClassMethods();
            return Math.max(rollupCalls.getSuccesslatency(classMethods), rollupCalls.getFailedlatency(classMethods));
        }

        @Override
        public String name(long value) {
            return nameUtils.latencyString(value);
        }
    };
    private ValueStrategy stackStrategy = new ValueStrategy() {
        @Override
        public String name() {
            return "calledBy";
        }

        @Override
        public long value(InterfaceArea callArea) {
            return Math.max(callArea.calledByCount, callArea.calledByCount);
        }

        @Override
        public String name(long value) {
            return value + " calledBy";
        }
    };
    private BarStrategy barStrategy = new BarStrategy() {
        @Override
        public String name() {
            return "unknown";
        }

        @Override
        public Object value(InterfaceArea callArea) {
            return callArea.getName();
        }

        @Override
        public String name(Object value) {
            return nameUtils.shortName(value.toString());
        }
    };
    private Coloring coloring;
    VPickedBackground pickedBackground;
    NameUtils nameUtils;
    Pickable[] pickables;

    public VCallDepthStack(NameUtils nameUtils,
            ProfileViewerProvider profileViewerProvider,
            Coloring coloring,
            float _w, float _h) {
        super(new RigidBox(1, 1), _w, _h);
        this.nameUtils = nameUtils;
        this.pickedBackground = new VPickedBackground(this);
        this.profileViewerProvider = profileViewerProvider;
        this.coloring = coloring;

        pickables = new Pickable[]{
            new Pickable(0.5f, 0.01f, UV.cNC) {
                @Override
                public XY_I bounds() {
                    return new XY_I((int) UV.fonts[UV.cLarge].getW(toString()) + 16, (int) UV.fonts[UV.cLarge].getH(toString()));
                }

                @Override
                public void picked(int x, int y) {
                    VPickList vPickList = new VPickList(VCallDepthStack.this.profileViewerProvider.getServiceNames().toArray(), new Value() {
                        @Override
                        public void setValue(Object _value) {
                            super.setValue(_value);
                            VCallDepthStack.this.getSelectedServiceNames().clear();
                            if (VCallDepthStack.this.getSelectedServiceNames().contains(_value)) {
                                VCallDepthStack.this.getSelectedServiceNames().remove(_value);
                            } else {
                                VCallDepthStack.this.getSelectedServiceNames().add((String) _value);
                            }
                        }
                    });
                    UV.popup(VCallDepthStack.this, new XY_I(x, y), new Viewer(vPickList), true, true);
                }

                @Override
                public void paintPickage(ICanvas _g, int _x, int _y, int _w, int _h, boolean isOver) {
                    interfaceBar.paintFlavor(_g, _x, _y, _w, _h, over ? AColor.grayBlue : AColor.darkGray);
                    _g.setColor(AColor.white);
                    _g.setFont(UV.fonts[UV.cLarge]);
                    UPaint.string(_g, toString(), UV.fonts[UV.cLarge], _x + 8, _y - 4, UV.cSWSW);
                }

                @Override
                public String toString() {
                    Iterator<String> iterator = VCallDepthStack.this.getSelectedServiceNames().iterator();
                    if (iterator.hasNext()) {
                        return iterator.next().toString();
                    } else {
                        return "Pick Service";
                    }
                }
            },
            new Pickable(0.05f, 0.95f, UV.cCC) {
                @Override
                public XY_I bounds() {
                    return new XY_I(32, 32);
                }

                @Override
                public void picked(int x, int y) {
                    UV.popup(VCallDepthStack.this, new XY_I(x, y), new Viewer(VCallDepthStack.this.pickedBackground.colorings), true, true);
                }

                @Override
                public void paintPickage(ICanvas _g, int _x, int _y, int _w, int _h, boolean isOver) {
                    interfaceBar.paintFlavor(_g, _x, _y, _w, _h, over ? AColor.grayBlue : AColor.darkGray);
                }
            },
            new Pickable(0.5f, 0.95f, UV.cCC) {
                @Override
                public XY_I bounds() {
                    return new XY_I((int) UV.fonts[UV.cTitle].getW(toString()) + 16, (int) UV.fonts[UV.cTitle].getH(toString()));
                }

                @Override
                public void picked(int x, int y) {
                    UV.popup(VCallDepthStack.this, new XY_I(x, y), new Viewer(VCallDepthStack.this.pickedBackground.barStrat), true, true);
                }

                @Override
                public void paintPickage(ICanvas _g, int _x, int _y, int _w, int _h, boolean isOver) {
                    interfaceBar.paintFlavor(_g, _x, _y, _w, _h, over ? AColor.grayBlue : AColor.darkGray);

                    _g.setColor(AColor.white);
                    _g.setFont(UV.fonts[UV.cTitle]);
                    UPaint.string(_g, toString(), UV.fonts[UV.cTitle], _x + 8, _y - 4, UV.cSWSW);
                }

                public String toString() {
                    BarStrategy value = (BarStrategy) VCallDepthStack.this.pickedBackground.barStrat.getValue();
                    return "Bars:" + value.name();
                }
            },
            new Pickable(0.5f, 0.05f, UV.cCC) {
                @Override
                public XY_I bounds() {
                    ValueStrategy value = (ValueStrategy) VCallDepthStack.this.pickedBackground.valueStrat.getValue();
                    String name = "Values:" + value.name();
                    return new XY_I((int) UV.fonts[UV.cTitle].getW(name) + 16, (int) UV.fonts[UV.cTitle].getH(name));
                }

                @Override
                public void picked(int x, int y) {
                    UV.popup(VCallDepthStack.this, new XY_I(x, y), new Viewer(VCallDepthStack.this.pickedBackground.valueStrat), true, true);
                }

                @Override
                public void paintPickage(ICanvas _g, int _x, int _y, int _w, int _h, boolean isOver) {
                    interfaceBar.paintFlavor(_g, _x, _y, _w, _h, over ? AColor.grayBlue : AColor.darkGray);
                    ValueStrategy value = (ValueStrategy) VCallDepthStack.this.pickedBackground.valueStrat.getValue();
                    String name = "Values:" + value.name();
                    _g.setColor(AColor.white);
                    _g.setFont(UV.fonts[UV.cTitle]);
                    UPaint.string(_g, name, UV.fonts[UV.cTitle], _x + 8, _y - 4, UV.cSWSW);
                }
            },
            new Pickable(0.05f, 0.5f, UV.cCC) {
                @Override
                public XY_I bounds() {
                    ValueStrategy value = (ValueStrategy) VCallDepthStack.this.pickedBackground.stackStrat.getValue();
                    String name = "Stack:" + value.name();
                    return new XY_I((int) UV.fonts[UV.cTitle].getW(name) + 16, (int) UV.fonts[UV.cTitle].getH(name));
                }

                @Override
                public void picked(int x, int y) {
                    UV.popup(VCallDepthStack.this, new XY_I(x, y), new Viewer(VCallDepthStack.this.pickedBackground.stackStrat), true, true);
                }

                @Override
                public void paintPickage(ICanvas _g, int _x, int _y, int _w, int _h, boolean isOver) {
                    interfaceBar.paintFlavor(_g, _x, _y, _w, _h, over ? AColor.grayBlue : AColor.darkGray);
                    ValueStrategy value = (ValueStrategy) VCallDepthStack.this.pickedBackground.stackStrat.getValue();
                    String name = "Stack:" + value.name();
                    _g.setColor(AColor.white);
                    _g.setFont(UV.fonts[UV.cTitle]);
                    UPaint.string(_g, name, UV.fonts[UV.cTitle], _x + 8, _y - 4, UV.cSWSW);
                }
            }
        };
        setBorder(new ViewBorder());
    }

    public Set<String> getSelectedServiceNames() {
        return selectedServiceNames;
    }

    public void setValueStrategy(ValueStrategy valueStrategy) {
        this.valueStrategy = valueStrategy;
        paint();
    }

    public void setStackStrategy(ValueStrategy stackStrategy) {
        this.stackStrategy = stackStrategy;
        paint();
    }

    public void setBarStrategy(BarStrategy barStrategy) {
        this.barStrategy = barStrategy;
        paint();
    }

    public void setColoring(Coloring coloring) {
        this.coloring = coloring;
        paint();
    }

    public String selectedServiceName() {
        Iterator<String> iterator = selectedServiceNames.iterator();
        if (iterator.hasNext()) {
               return iterator.next();
        } else {
            return null;
        }
    }

    @Override
    public void paintBorder(ICanvas _g, int _x, int _y, int _w, int _h) {
        super.paintBorder(_g, _x, _y, _w, _h); //To change body of generated methods, choose Tools | Templates.
        valuesHistogram.reset();
        int ox = _x;
        int oy = _y;
        int ow = _w;
        int oh = _h;


        String serviceName = selectedServiceName();
        if (serviceName != null) {

            ServiceModel serviceModel;
            try {
                serviceModel = profileViewerProvider.getServiceModel(serviceName);
                status.setStatus("");
            } catch (Exception x) {
                x.printStackTrace();
                status.setStatus(x.toString());
                return;
            }
            int totalDepth = serviceModel.getCallDepths().length;
            if (totalDepth == 0) {
                return;
            }

            unique.clear();
            for (CallDepth callDepth : serviceModel.getCallDepths()) {
                if (callDepth == null) {
                    continue;
                }

                CallClass[] callClasses = callDepth.getClassNameToCallClass().values().toArray(new CallClass[0]);
                int d = 0;
                for (CallClass callClass : callClasses) {
                    String className = callClass.getName();
                    if (!hideClass.contains(className)) {
                        InterfaceArea callArea = unique.get(className);
                        if (callArea == null) {
                            callArea = new InterfaceArea(callClass, valueStrategy, pinnedClass);
                            unique.put(className, callArea);
                        }
                        callArea.joinCallsTo(d, callClass);
                    }
                    d++;
                }
            }

            for (InterfaceArea area : unique.values()) {
                for (Call call : area.callsTo) {
                    InterfaceArea called = unique.get(call.getClassName());
                    if (called != null) {
                        called.calledByCount++;
                    }
                }
            }

            Map<String, Long> names = new HashMap<>();
            long maxAvg = 0;
            for (InterfaceArea area : unique.values()) {
                Object v = barStrategy.value(area);
                if (v instanceof Long) {
                    maxAvg = Math.max(maxAvg, (Long) v);
                } else {
                    if (!names.containsKey(area.getName())) {
                        names.put(v.toString(), maxAvg);
                        maxAvg++;
                    }
                }
            }

            List<String> keys = new ArrayList<>(names.keySet());
            Collections.sort(keys);
            for (int i = 0; i < keys.size(); i++) {
                names.put(keys.get(i), (long) i);
            }


            List<CallDepthAreas> bars = new LinkedList<>();
            for (int i = 0; i < maxAvg + 1; i++) {
                bars.add(new CallDepthAreas(valueStrategy,stackStrategy,valuesHistogram));
            }


            for (InterfaceArea area : unique.values()) {
                long depth;
                Object v = barStrategy.value(area);
                if (v instanceof Long) {
                    depth = maxAvg - (Long) v;
                } else {
                    depth = names.get(v.toString());
                }
                CallDepthAreas callDepthAreas = bars.get((int) depth);
                callDepthAreas.add(area.getName(), area);
                callDepthAreas.name = barStrategy.name(v);
            }

            for (Iterator<CallDepthAreas> it = bars.iterator(); it.hasNext();) {
                CallDepthAreas callDepthAreas = it.next();
                if (callDepthAreas.areas.isEmpty()) {
                    it.remove();
                }
            }


            _x += (16 * 20);
            _y += (16 * 10);
            _w -= (16 * 40);
            _h -= (16 * 20);


            int widthPerDepth = 32;
            int gap = Math.max(96, ((_w - bars.size() * widthPerDepth) / bars.size()));


            CallDepthAreas[] callDepthAreas = bars.toArray(new CallDepthAreas[bars.size()]);
            caw = (callDepthAreas.length * (widthPerDepth + gap));
            _x -= (shift * (caw - _w));
            int x = _x;

            Tunnel t = new Tunnel(x, _y, caw, _h, 0.8);
            _g.setColor(AColor.gray);
            t.drawTunnel(_g);

            //t.paintWaveform(NullView.cNull, barColor, new double[]{0.5,0.1,0.7,0.0,1.0}, 0.5f, 0.5, 0.5, 0.1, 0.1, 0, 1, 10, _g, caw, _h);

            if (sortOrder) {
                for (int i = 0; i < callDepthAreas.length; i++) {
                    callDepthAreas[i].assignArea(x, _y, widthPerDepth, _h);
                    x += widthPerDepth + gap;
                }
            } else {
                for (int i = callDepthAreas.length - 1; i > -1; i--) {
                    callDepthAreas[i].assignArea(x, _y, widthPerDepth, _h);
                    x += widthPerDepth + gap;
                }
            }

            long maxV = 0;
            for (CallDepthAreas callDepthArea : callDepthAreas) {
                maxV = Math.max(maxV, callDepthArea.normalize());
                callDepthArea.stack();
                callDepthArea.size();
            }

            for (CallDepthAreas callDepthArea : callDepthAreas) {
                callDepthArea.histogram(maxV);
            }

            if (overMethod != null) {
                lastOverMethod = overMethod;
            }
            overMethod = null;

            if (over != null) {
                lastOver = over;
            }
            over = null;
            for (InterfaceArea callArea : unique.values()) {
                callArea.color = coloring.value(callArea, maxV);
                if (new XYWH_I(callArea.x, callArea.y, callArea.w, callArea.h).contains(mp)) {
                    over = callArea;
                    for (InterfaceArea.MethodArea methodArea : callArea.methodAreas) {
                        if (new XYWH_I(methodArea.x, methodArea.y, methodArea.w, methodArea.h).contains(mp)) {
                            overMethod = methodArea;
                        }
                    }
                }
            }
            for (InterfaceArea callArea : unique.values()) {
                callArea.paint(_g, over);
            }



            List<Line> lines = new LinkedList<>();
            for (InterfaceArea area : unique.values()) {
                if (hideClass.contains(area.getName())) {
                    continue;
                }
                List<InterfaceArea.MethodArea> toAreas = new LinkedList<>();
                int totalH = 0;
                for (Call call : area.callsTo) {
                    InterfaceArea called = unique.get(call.getClassName());
                    if (called != null) {
                        called.calledByCount++;
                        InterfaceArea.MethodArea methodArea = called.mapMethodAreas.get(call.getMethodName());
                        if (methodArea == null) {
                            System.out.println("Missing method for " + call.getClassName() + " " + call.getMethodName());
                        } else {
                            toAreas.add(methodArea);
                            totalH += methodArea.h;
                        }
                    } else {
                        System.out.println("Missing area for " + call.getClassName());
                    }
                }

                double s = 0;
                for (InterfaceArea.MethodArea toArea : toAreas) {
                    double ph = MinMaxDouble.zeroToOne(0, totalH, toArea.h);
                    XYWH_I fromRect = area.rect(s, ph);
                    s += ph;
                    XYWH_I toRect = new XYWH_I(toArea.x, toArea.y, toArea.w, toArea.h);
                    lines.add(new Line(area, fromRect, toArea, toRect, pinnedClass));
                }
            }


            for (Line l : lines) {
                l.paint(_g, _x, _y, _w, _h, over, overMethod);
            }


            for (int i = 0; i < callDepthAreas.length; i++) {
                callDepthAreas[i].paint(_g);
            }


            if (over != null || lastOver != null) {
                InterfaceArea paint = over;
                if (over == null) {
                    paint = lastOver;
                }

                XY_I op = new XY_I(paint.x + ((paint.w / 4) * 3), paint.y + (paint.h / 2));
                if (overMethod != null) {
                    op = new XY_I(overMethod.x + (overMethod.w / 2), overMethod.y + (overMethod.h / 2));
                }

                XYWH_I rect = paint(_g, op,
                        new XY_I(op.x + (16 * 5), op.y - (16 * 5)),
                        nameUtils.simpleInterfaceName(paint),
                        UV.fonts[UV.cText], paint.color);

                if (overMethod != null) {
                    paint(_g, new XY_I(rect.x + rect.w, rect.y + rect.h - 8),
                            new XY_I(rect.x + rect.w + 16, rect.y + rect.h - 8),
                            nameUtils.simpleMethodName(overMethod),
                            UV.fonts[UV.cText], AColor.gray);
                }

            }

            int bh = widthPerDepth;
            int bx = ox + bh;
            int by = oy + bh;
            int bw = 160;

            for (int i = 0; i < valuesHistogram.histogram.length; i++) {
                if (valuesHistogram.histogram[i] == 0) {
                    continue;
                }
                interfaceBar.paintFlavor(_g, bx, by, bw, bh, AColor.darkGray);
                interfaceBar.paintFlavor(_g, bx + 4, by + 4, bh + (int) ((bw - bh) * (MinMaxInt.zeroToOne(0, valuesHistogram.max, valuesHistogram.histogram[i]))) - 8, bh - 8, AColor.getWarmToCool(1f - (float) (i + 1) / (float) valuesHistogram.histogram.length));

                _g.setFont(UV.fonts[UV.cText]);
                _g.setColor(ViewColor.cThemeFont);
                UPaint.string(_g, "" + valuesHistogram.histogram[i], UV.fonts[UV.cText], bx + 8, by - 4, UV.cWW, 0);

                by += bh;
            }
        }

        for (Pickable p : pickables) {
            p.paint(_g, ox, oy, ow, oh);
        }

    }

    XYWH_I paint(ICanvas _g, XY_I from, XY_I at, String[] message, AFont font, AColor background) {

        _g.setFont(font);
        float mw = 0;
        float mh = 0;
        for (String m : message) {
            mw = Math.max(mw, font.getW(m));
            mh += font.getH(m);
        }

        _g.setColor(background);
        _g.setAlpha(0.9f, 0);
        _g.roundRect(true, at.x - 8, at.y - (int) (mh) - 8, (int) mw, 16 + (int) mh, 8, 8);
        _g.roundRect(false, at.x - 8, at.y - (int) (mh) - 8, (int) mw, 16 + (int) mh, 8, 8);
        _g.setAlpha(1f, 0);

        _g.setColor(ViewColor.cThemeFont);
        float _y = at.y;
        for (String m : message) {
            float sh = font.getH(m);
            UPaint.string(_g, m, font, at.x, (int) _y - (int) (mh), UV.cWW);
            _y += sh;
        }

        _g.line(from.x, from.y, at.x, at.y);

        _g.setColor(AColor.orange);
        _g.oval(true, from.x - 4, from.y - 4, 8, 8);
        _g.oval(true, at.x - 4, at.y - 4, 8, 8);
        return new XYWH_I(at.x - 8, at.y - (int) (mh) - 8, (int) mw, 16 + (int) mh);
    }

    @Override
    public void mouseDragged(MouseDragged _e) {
        super.mouseDragged(_e);
        double s = shift;
        s -= ((float) _e.getDeltaX() / (float) caw);
        if (s < 0) {
            s = 0;
        }
        if (s > 1) {
            s = 1;
        }
        shift = s;

    }

    @Override
    public void mouseReleased(MouseReleased _e) {
        super.mouseReleased(_e);
        XY_I rp = _e.getPoint();
        for (InterfaceArea callArea : unique.values()) {
            if (new XYWH_I(callArea.x, callArea.y, callArea.w, callArea.h).contains(rp)) {
                if (_e.getClickCount() == 2) {
                    hideClass.add(callArea.getName());
                    return;
                } else {
                    UV.popup(this, _e, new VPicked(callArea, hideClass, pinnedClass), true, true);
                    return;
                }
            }
        }

        for (Pickable p : pickables) {
            if (p.over(_e.getX(), _e.getY())) {
                p.picked(_e.getX(), _e.getY());
            }
        }
    }
    XY_I mp = new XY_I(0, 0);

    @Override
    public void mouseMoved(MouseMoved _e) {
        super.mouseMoved(_e); //To change body of generated methods, choose Tools | Templates.

        for (Pickable p : pickables) {
            p.over(_e.getX(), _e.getY());
        }
        mp = _e.getPoint();
        repair();
        paint();
    }
}
