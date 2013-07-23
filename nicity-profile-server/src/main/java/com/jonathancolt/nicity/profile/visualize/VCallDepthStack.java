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

import com.jonathancolt.nicity.core.lang.MinMaxDouble;
import com.jonathancolt.nicity.core.lang.MinMaxInt;
import com.jonathancolt.nicity.core.lang.MinMaxLong;
import com.jonathancolt.nicity.core.memory.struct.XYWH_I;
import com.jonathancolt.nicity.core.memory.struct.XY_I;
import com.jonathancolt.nicity.profile.model.Call;
import com.jonathancolt.nicity.profile.model.CallClass;
import com.jonathancolt.nicity.profile.model.CallDepth;
import com.jonathancolt.nicity.profile.model.ClassMethod;
import com.jonathancolt.nicity.profile.model.ServicesCallDepthStack;
import com.jonathancolt.nicity.view.adaptor.IPath;
import com.jonathancolt.nicity.view.adaptor.VS;
import com.jonathancolt.nicity.view.border.ViewBorder;
import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.core.AFont;
import com.jonathancolt.nicity.view.core.RigidBox;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.VPan;
import com.jonathancolt.nicity.view.core.ViewColor;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.event.MouseMoved;
import com.jonathancolt.nicity.view.event.MouseReleased;
import com.jonathancolt.nicity.view.flavor.AFlavor;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.list.VItem;
import com.jonathancolt.nicity.view.paint.UPaint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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

    static AFlavor interfaceBar = new BarFlavor(8);
    static AFlavor methodBar = new BarFlavor(3);
    boolean sortOrder = true;
    Set<String> selectedServiceNames = new HashSet<String>();
    final ServicesCallDepthStack servicesCallDepthStack;
    InterfaceArea over;
    InterfaceArea lastOver;
    InterfaceArea.MethodArea overMethod;
    InterfaceArea.MethodArea lastOverMethod;
    final ValuesHistogram valuesHistogram = new ValuesHistogram();
    Set<String> hideClass = new HashSet<String>();
    Set<String> pinnedClass = new HashSet<String>();
    Map<String, InterfaceArea> unique = new ConcurrentHashMap<String, InterfaceArea>();
    Double shift = 0.0d;
    private ValueStrategy valueStrategy = new ValueStrategy() {
        public long value(InterfaceArea callArea) {
            return Math.max(callArea.callClass.getSuccesslatency(), callArea.callClass.getFailedlatency());
        }

        public String name(long value) {
            return nameUtils.latencyString(value);
        }
    };
    private ValueStrategy stackStrategy = new ValueStrategy() {
        public long value(InterfaceArea callArea) {
            return Math.max(callArea.calledByCount, callArea.calledByCount);
        }

        public String name(long value) {
            return value + " calledBy";
        }
    };
    private BarStrategy barStrategy = new BarStrategy() {
        public Object value(VCallDepthStack.InterfaceArea callArea) {
            return callArea.getName();
        }

        public String name(Object value) {
            return nameUtils.shortName(value.toString());
        }
    };
    private Coloring coloring;
    VPickedBackground pickedBackground;
    NameUtils nameUtils;

    public VCallDepthStack(NameUtils nameUtils,
            ServicesCallDepthStack callDepthStack,
            Coloring coloring,
            float _w, float _h) {
        super(new RigidBox(1, 1), _w, _h);
        this.nameUtils = nameUtils;
        this.pickedBackground = new VPickedBackground(this);
        this.servicesCallDepthStack = callDepthStack;
        this.coloring = coloring;
        setBorder(new ViewBorder());
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

    @Override
    public void paintBorder(ICanvas _g, int _x, int _y, int _w, int _h) {
        super.paintBorder(_g, _x, _y, _w, _h); //To change body of generated methods, choose Tools | Templates.
        valuesHistogram.reset();
        int ox = _x;
        int oy = _y;
        int ow = _w;
        int oh = _h;


        Iterator<String> iterator = selectedServiceNames.iterator();
        if (!iterator.hasNext()) {
            return;
        }

        CallDepth[] copy = servicesCallDepthStack.getCopy(iterator.next());
        int totalDepth = copy.length;
        if (totalDepth == 0) {
            return;
        }

        unique.clear();
        for (CallDepth callDepth:copy) {
            if (callDepth == null) {
                continue;
            }

            CallClass[] callClasses = callDepth.getCopy();
            int d = 0;
            for (CallClass callClass : callClasses) {
                String className = callClass.getName();
                if (!hideClass.contains(className)) {
                    InterfaceArea callArea = unique.get(className);
                    if (callArea == null) {
                        callArea = new InterfaceArea(callClass);
                        unique.put(className, callArea);
                    }
                    callArea.joinCallsTo(d,callClass);
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

        Map<String, Long> names = new HashMap<String, Long>();
        long maxAvg = 0;
        for (InterfaceArea area : unique.values()) {
            Object v = barStrategy.value(area);
            if (v instanceof Long) {
                maxAvg = Math.max(maxAvg, (Long)v);
            } else {
                if (!names.containsKey(area.getName())) {
                    names.put(v.toString(), maxAvg);
                    maxAvg++;
                }
            }
        }

        List<String> keys = new ArrayList<String>(names.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            names.put(keys.get(i), (long) i);
        }


        List<CallDepthAreas> bars = new LinkedList<CallDepthAreas>();
        for (int i = 0; i < maxAvg + 1; i++) {
            bars.add(new CallDepthAreas());
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
        int gap = Math.max(96, ((_w - (int) (bars.size() * widthPerDepth)) / bars.size()));


        CallDepthAreas[] callDepthAreas = bars.toArray(new CallDepthAreas[bars.size()]);
        _x -= (shift * ((callDepthAreas.length * (widthPerDepth + gap)) - _w));
        int x = _x;
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
                for(InterfaceArea.MethodArea methodArea:callArea.methodAreas) {
                    if (new XYWH_I(methodArea.x, methodArea.y, methodArea.w, methodArea.h).contains(mp)) {
                        overMethod = methodArea;
                    }
                }
            }
        }
        for (InterfaceArea callArea : unique.values()) {
            callArea.paint(_g);
        }



        List<Line> lines = new LinkedList<Line>();
        for (InterfaceArea area : unique.values()) {
            if (hideClass.contains(area.getName())) {
                continue;
            }
            List<InterfaceArea.MethodArea> toAreas = new LinkedList<InterfaceArea.MethodArea>();
            int totalH = 0;
            for (Call call : area.callsTo) {
                InterfaceArea called = unique.get(call.getClassName());
                if (called != null) {
                    called.calledByCount++;
                    InterfaceArea.MethodArea methodArea = called.mapMethodAreas.get(call.getMethodName());
                    if (methodArea == null) {
                        System.out.println("Missing method for "+call.getClassName()+" "+call.getMethodName());
                    } else {
                        toAreas.add(methodArea);
                        totalH += methodArea.h;
                    }
                } else {
                    System.out.println("Missing area for "+call.getClassName());
                }
            }

            double s = 0;
            for (InterfaceArea.MethodArea toArea : toAreas) {
                double ph = MinMaxDouble.zeroToOne(0, totalH, toArea.h);
                XYWH_I fromRect = area.rect(s, ph);
                s += ph;
                XYWH_I toRect = new XYWH_I(toArea.x,toArea.y,toArea.w,toArea.h);
                lines.add(new Line(area, fromRect, toArea, toRect));
            }
        }


        for (Line l : lines) {
            l.paint(_g, _x, _y, _w, _h);
        }


        for (int i = 0; i < callDepthAreas.length; i++) {
            callDepthAreas[i].paint(_g);
        }


        if (over != null || lastOver != null) {
            InterfaceArea paint = over;
            if (over == null) {
                paint = lastOver;
            }

            XY_I op = new XY_I(paint.x + ((paint.w / 4)*3), paint.y + (paint.h / 2));
            if (overMethod != null) {
                op = new XY_I(overMethod.x + (overMethod.w / 2), overMethod.y + (overMethod.h / 2));
            }

            XYWH_I rect = paint(_g, op,
                    new XY_I(op.x + (16 * 5), op.y - (16 * 5)),
                    nameUtils.simpleInterfaceName(paint),
                    UV.fonts[UV.cText], paint.color);

            if (overMethod != null) {
                paint(_g, new XY_I(rect.x+rect.w,rect.y+rect.h-8),
                        new XY_I(rect.x+rect.w+16,rect.y+rect.h-8),
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

    XYWH_I paint(ICanvas _g, XY_I from, XY_I at, String[] message, AFont font,AColor background) {

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

    class Line {

        final InterfaceArea fromArea;
        final XYWH_I fromRect;
        final InterfaceArea.MethodArea toArea;
        final XYWH_I toRect;

        public Line(InterfaceArea fromArea, XYWH_I fromRect, InterfaceArea.MethodArea toArea, XYWH_I toRect) {
            this.fromArea = fromArea;
            this.fromRect = fromRect;
            this.toArea = toArea;
            this.toRect = toRect;
        }

        void paint(ICanvas _g, int _x, int _y, int _w, int _h) {
            if (overMethod == null &&
                (over == toArea.interfaceArea ||
                over == fromArea ||
                pinnedClass.contains(fromArea.getName()) ||
                pinnedClass.contains(toArea.interfaceArea.getName()))) {
                _g.setAlpha(1f, 0);
                _g.setColor(toArea.interfaceArea.color.brighter());

            } else if (overMethod != null && overMethod == toArea) {
                _g.setAlpha(1f, 0);
                _g.setColor(toArea.interfaceArea.color.brighter());
            } else {
                _g.setColor(AColor.gray);
                _g.setAlpha(0.25f, 0);
            }

            IPath path = VS.path();
            if (fromArea == toArea.interfaceArea) {
                fromToSelf(_h, path, _y, _g);
            } else if (fromArea.x > toArea.x) {
                fromToBehind(_h, path, _y, _g);
            } else {
                fromToAhead(path, _g);
            }

            interfaceBar.paintFlavor(_g, fromRect.x + fromRect.w, fromRect.y, 4, fromRect.h, toArea.interfaceArea.color);
            _g.setAlpha(1f, 0);


        }

        private void fromToAhead(IPath path, ICanvas _g) {
            UPaint.moon(path, new XY_I(fromRect.x + fromRect.w, fromRect.y + (fromRect.h / 2)), new XY_I(toRect.x, toRect.y + (toRect.h / 2)), -6, 6);
            _g.fill(path);
            _g.setColor(AColor.darkGray);
            _g.draw(path);
        }

        private void fromToBehind(int _h, IPath path, int _y, ICanvas _g) {
            int pad = 20 + (int) (48 * (1f - ((float) fromRect.y / (float) _h)));

            path.moveTo(fromRect.x + fromRect.w, fromRect.y);
            path.curveTo(fromRect.x + fromRect.w, fromRect.y,
                    fromRect.x + fromRect.w + pad, fromRect.y,
                    fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2) + pad);

            path.lineTo(fromRect.x + fromRect.w + pad, _y + _h + pad);
            path.lineTo(fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2) + pad);

            path.curveTo(fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2) + pad,
                    fromRect.x + fromRect.w + pad / 2, fromRect.y + (fromRect.h / 2),
                    fromRect.x + fromRect.w, fromRect.y + fromRect.h);

            path.closePath();
            _g.fill(path);

            path = VS.path();
            path.moveTo(fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2) + pad);
            path.lineTo(fromRect.x + fromRect.w + pad, _y + _h + pad);
            path.curveTo(fromRect.x + fromRect.w + pad, _y + _h + pad,
                    fromRect.x + fromRect.w + pad, _y + _h + pad + pad,
                    fromRect.x + fromRect.w, _y + _h + pad + pad);

            path.lineTo(toRect.x, _y + _h + pad + pad);

            path.curveTo(toRect.x, _y + _h + pad + pad,
                    toRect.x - pad, _y + _h + pad + pad,
                    toRect.x - pad, _y + _h + pad);

            path.lineTo(toRect.x - pad, toRect.y + (toRect.h / 2) + pad);
            path.curveTo(toRect.x - pad, toRect.y + (toRect.h / 2) + pad,
                    toRect.x - pad, toRect.y + (toRect.h / 2),
                    toRect.x, toRect.y + (toRect.h / 2));
            UPaint.arrowHead(path, toRect.x, toRect.y + (toRect.h / 2), 270, 10, 45);
            _g.draw(path);
        }

        private void fromToSelf(int _h, IPath path, int _y, ICanvas _g) {
            int pad = 20 + (int) (48 * ((float) fromRect.y / (float) _h));

            path.moveTo(fromRect.x + fromRect.w, fromRect.y);
            path.curveTo(fromRect.x + fromRect.w, fromRect.y,
                    fromRect.x + fromRect.w + pad / 2, fromRect.y + (fromRect.h / 2),
                    fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2) - pad);

            path.lineTo(fromRect.x + fromRect.w + pad, _y - pad);
            path.lineTo(fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2) - pad);

            path.curveTo(fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2) - pad,
                    fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2),
                    fromRect.x + fromRect.w, fromRect.y + (fromRect.h));

            path.closePath();
            _g.fill(path);

            path = VS.path();
            path.moveTo(fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2) - pad);
            path.lineTo(fromRect.x + fromRect.w + pad, _y - pad);
            path.curveTo(fromRect.x + fromRect.w + pad, _y - pad,
                    fromRect.x + fromRect.w + pad, _y - (pad + pad),
                    fromRect.x + fromRect.w, _y - (pad + pad));

            path.lineTo(toRect.x, _y - (pad + pad));

            path.curveTo(toRect.x - pad, _y - (pad + pad),
                    toRect.x - pad, _y - (pad + pad),
                    toRect.x - pad, _y - pad);

            path.lineTo(toRect.x - pad, toRect.y + (toRect.h / 2) - pad);

            path.curveTo(toRect.x - pad, toRect.y + (toRect.h / 2) - pad,
                    toRect.x - pad, toRect.y + (toRect.h / 2),
                    toRect.x, toRect.y + (toRect.h / 2));
            UPaint.arrowHead(path, toRect.x, toRect.y + (toRect.h / 2), 270, 10, 45);
            _g.draw(path);
        }
    }

    @Override
    public void mouseReleased(MouseReleased _e) {
        XY_I rp = _e.getPoint();
        for (InterfaceArea callArea : unique.values()) {
            if (new XYWH_I(callArea.x, callArea.y, callArea.w, callArea.h).contains(rp)) {
                if (_e.getClickCount() == 2) {
                    hideClass.add(callArea.getName());
                    return;
                } else {
                    UV.popup(this, _e, new VPicked(callArea), true, true);
                    return;
                }
            }
        }
    }

    class VPicked extends Viewer {

        public VPicked(final InterfaceArea callArea) {
            VChain c = new VChain(UV.cSWNW);
            if (pinnedClass.contains(callArea.getName())) {
                c.add(new VItem("un-pin") {
                    @Override
                    public void picked(IEvent _e) {
                        super.picked(_e); //To change body of generated methods, choose Tools | Templates.
                        pinnedClass.remove(callArea.getName());
                        getRootView().dispose();
                    }
                });
            } else {
                c.add(new VItem("pin") {
                    @Override
                    public void picked(IEvent _e) {
                        super.picked(_e); //To change body of generated methods, choose Tools | Templates.
                        pinnedClass.add(callArea.getName());
                        getRootView().dispose();
                    }
                });
            }

            c.add(new VItem("hide") {
                @Override
                public void picked(IEvent _e) {
                    super.picked(_e); //To change body of generated methods, choose Tools | Templates.
                    hideClass.add(callArea.getName());
                    pinnedClass.remove(callArea.getName());
                    getRootView().dispose();
                }
            });

            c.add(new VItem("showall") {
                @Override
                public void picked(IEvent _e) {
                    super.picked(_e); //To change body of generated methods, choose Tools | Templates.
                    hideClass.clear();
                    getRootView().dispose();
                }
            });
            setContent(c);
        }
    }
    XY_I mp = new XY_I(0, 0);

    @Override
    public void mouseMoved(MouseMoved _e) {
        super.mouseMoved(_e); //To change body of generated methods, choose Tools | Templates.
        mp = _e.getPoint();
        repair();
        paint();
    }

    class CallDepthAreas {

        Map<String, InterfaceArea> areas = new HashMap<String, InterfaceArea>();
        String name = "";
        int x;
        int y;
        int w;
        int h;
        InterfaceArea[] paintAreas;
        long total;

        public CallDepthAreas() {
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

    class InterfaceArea {

        final CallClass callClass;
        Set<Integer> depths = Collections.newSetFromMap(new ConcurrentHashMap<Integer,Boolean>());
        int x;
        int y;
        int w;
        int h;
        long value;
        long maxValue;
        AColor color;
        int calledByCount;
        Set<Call> callsTo = new HashSet<Call>();
        ConcurrentHashMap<String, ClassMethod> classMethods = new ConcurrentHashMap<String, ClassMethod>();

        public InterfaceArea(CallClass callClass) {
            this.callClass = callClass;
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

        void paint(ICanvas _g) {
            _g.setAlpha(1f, 0);
            InterfaceArea _over = over;
            if (_over != null || pinnedClass.size() > 0) {
                if (_over != null && _over.callClass != null && !this.getName().equals(_over.getName())) {
                    _g.setAlpha(0.25f, 0);
                }
            }

            interfaceBar.paintFlavor(_g, x, y, w, h, color);


            _g.setColor(ViewColor.cThemeFont);
            _g.oval(true, x + w, y + h, 7, 7);
            _g.setFont(UV.fonts[UV.cText]);
            UPaint.string(_g, "    " + valueStrategy.name(value), UV.fonts[UV.cSmall], x + w, y, UV.cWW, 45);

            for(MethodArea methodArea:methodAreas) {
                methodArea.paint(_g);
            }

            _g.setAlpha(1f, 0);

        }
        MethodArea[] methodAreas = new MethodArea[0];
        Map<String,MethodArea> mapMethodAreas = new HashMap<String, MethodArea>();
        private void sizeMethods() {
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
                methodAreas[i].w = (w/2);
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
                this.value = 1;//classMethod.getSuccesslatency();
            }

            public void paint(ICanvas _g) {
                methodBar.paintFlavor(_g, x, y, w, h, AColor.gray);
                if (interfaceArea.depths.contains(0)) {
                    _g.setColor(AColor.gray);
                    _g.line(x+(w/2)-32, y+(h/2),x+(w/2)-3, y+(h/2));
                    _g.setColor(AColor.green);
                    _g.oval(true, x+(w/2)-32, y+(h/2)-3, 6, 6);
                }
            }
        }
    }
}
