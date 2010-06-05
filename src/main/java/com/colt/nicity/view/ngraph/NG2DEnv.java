/*
 * NG2DEnv.java.java
 *
 * Created on 03-12-2010 06:42:41 PM
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
package com.colt.nicity.view.ngraph;

import com.colt.nicity.view.event.MouseEntered;
import com.colt.nicity.view.event.MouseExited;
import com.colt.nicity.view.value.VEditValue;
import com.colt.nicity.view.border.SolidBorder;
import com.colt.nicity.view.event.MouseDragged;
import com.colt.nicity.view.event.MouseMoved;
import com.colt.nicity.view.event.MouseReleased;
import com.colt.nicity.view.list.AItem;
import com.colt.nicity.view.list.ListController;
import com.colt.nicity.view.list.VItem;
import com.colt.nicity.view.list.VLoose;
import com.colt.nicity.asynchro.ElapseCall;
import com.colt.nicity.core.process.ICall;
import com.colt.nicity.core.collection.CArray;
import com.colt.nicity.core.collection.CSet;
import com.colt.nicity.core.lang.IOut;
import com.colt.nicity.core.lang.MinMaxDouble;
import com.colt.nicity.core.lang.MinMaxLong;
import com.colt.nicity.core.lang.UDouble;
import com.colt.nicity.core.lang.UFloat;
import com.colt.nicity.core.memory.struct.IXYZ;
import com.colt.nicity.core.memory.struct.XYZ_D;
import com.colt.nicity.core.memory.struct.XY_I;
import com.colt.nicity.core.value.Value;
import com.colt.nicity.view.core.AColor;
import com.colt.nicity.view.core.AFont;
import com.colt.nicity.view.core.UV;
import com.colt.nicity.view.core.VChain;
import com.colt.nicity.view.core.VPan;
import com.colt.nicity.view.core.VString;
import com.colt.nicity.view.core.ViewColor;
import com.colt.nicity.view.core.Viewer;
import com.colt.nicity.view.interfaces.ICanvas;
import com.colt.nicity.view.interfaces.IEnteredOrExited;
import com.colt.nicity.view.interfaces.IEvent;
import com.colt.nicity.view.interfaces.IMouseEvents;
import com.colt.nicity.view.interfaces.IMouseMotionEvents;
import com.colt.nicity.view.interfaces.IToolTip;
import com.colt.nicity.view.interfaces.IVItem;
import com.colt.nicity.view.interfaces.IView;
import java.util.HashMap;
import java.util.Map;
import linloglayout.LinLogLayout;
import linloglayout.LinLogProgress;

/**
 *
 * @author Administrator
 */
public class NG2DEnv extends Viewer {
    // called once the graph has been layed out and painted!

    /**
     *
     */
    public void refreshed() {
    }
    Object[] links = new Object[0];
    long maxNodeCount = 0;
    double minNode = 0;
    double maxNode = 1;
    long maxLinkCount = 0;
    double minLink = 0;
    double maxLink = 1;
    CSet hashNodes = new CSet();
    CSet selected = new CSet();
    CArray orderedSelection = new CArray(IVNode.class);
    /**
     *
     */
    public VLoose loose;
    CSet nodes = new CSet();
    /**
     *
     */
    public boolean inoutLinks = false;
    /**
     *
     */
    public Value attraction = new Value(1d);
    /**
     *
     */
    public Value repulsion = new Value(-2.5d);
    /**
     *
     */
    public Value iterations = new Value(new Integer(100));
    ElapseCall refresher;
    int cw = 800;
    int ch = 800;

    /**
     *
     * @param _w
     * @param _h
     */
    public NG2DEnv(int _w, int _h) {
        cw = _w;
        ch = _h;

        loose = new VLoose(new ListController(nodes)) {

            public void mend() {
                enableFlag(UV.cRepair);//??
                super.mend();
            }

            public void mouseReleased(final MouseReleased _e) {
                super.mouseReleased(_e);
                if (_e.isRightClick()) {
                    UV.popup(this, _e, NG2DEnv.this.popupView(), true);
                }
            }

            public void paintBackground(ICanvas _g, int _x, int _y, int _w, int _h) {
                super.paintBackground(_g, _x, _y, _w, _h);
                //_g.setColor(ViewColor.cVisualizeTheme);

                _g.setAlpha(0.1f, 0);
                for (IVItem i : getItems()) {
                    _g.setColor(ViewColor.cVisualizeThemeFont);
                    int cx = (int) (i.getX() + (i.getW() / 2));
                    int cy = (int) (i.getY() + (i.getH() / 2));
                    int max = (int) Math.max(i.getW(), i.getH());
                    int steps = max / 3;
                    for (int m = 0; m < steps; m++) {
                        _g.oval(true, _x + cx - (max / 2), _y + cy - (max / 2), max, max);
                        max -= steps;
                        steps += steps;
                        if (max / 2 < 1) {
                            break;
                        }
                    }
                }
                _g.setAlpha(1f, 0);

                for (int i = 0; i < links.length; i++) {
                    if (links[i] == null) {
                        continue;
                    }
                    NGLink link = (NGLink) links[i];
                    IView from = (IView) nodes.get(link.key(0));
                    IView to = (IView) nodes.get(link.key(1));
                    if (from == null) {
                        continue;
                    }
                    if (to == null) {
                        continue;
                    }
                    double rank = (double) link.count / (double) maxLinkCount;

                    if (inoutLinks) {
                        XY_I fp = UV.toPoint(from, UV.cEE);
                        XY_I tp = UV.toPoint(to, UV.cWW);
                        LinkDrawer linkDrawer = (LinkDrawer) link.key(2);
                        linkDrawer.draw(_g, fp, tp, rank, 8);
                    } else {
                        XY_I _fp = UV.toPoint(from, UV.cCC);
                        XY_I _tp = UV.toPoint(to, UV.cCC);

                        XY_I fp = UV.toPerimeterPoint(from, _fp, _tp);
                        XY_I tp = UV.toPerimeterPoint(to, _tp, _fp);

                        LinkDrawer linkDrawer = (LinkDrawer) link.key(2);
                        linkDrawer.draw(_g, fp, tp, rank, 8);
                    }
                }

            }
        };
        loose.spans(UV.cXNSEW);
        loose.setBorder(new SolidBorder(ViewColor.cVisualizeTheme));

        VPan pan = new VPan(loose, _w, _h) {

            public void paintBackground(ICanvas _g, int _x, int _y, int _w, int _h) {
                super.paintBackground(_g, _x, _y, _w, _h);
                if (cw != _w || ch != _h) {
                    cw = _w;
                    ch = _h;
                    refresher.signal();
                }
            }
        };
        setContent(pan);
        setBorder(new SolidBorder(ViewColor.cVisualizeTheme));

        refresher = new ElapseCall(null, 500, new ICall() {

            public void invoke(IOut _) {
                _refresh(_);
            }
        });
    }

    /**
     *
     * @param _
     * @param _dos
     */
    public void set(IOut _, NG _dos) {
        set(_, _dos.nodes.getAll(Object.class), _dos.maxNodeCount, _dos.links.getAll(Object.class), _dos.maxLinkCount);
    }

    // _nodes instanceof IVNode
    // _links instanceof DoLink
    /**
     *
     * @param _
     * @param _nodes
     * @param _maxNodeCount
     * @param _links
     * @param _maxLinkCount
     */
    public void set(IOut _, Object[] _nodes, long _maxNodeCount, Object[] _links, long _maxLinkCount) {
        maxNodeCount = _maxNodeCount;
        maxLinkCount = _maxLinkCount;
        setNodes(_nodes);
        setLinks(_links);
        //refresher.signal();//?? 4/28/09 switched to inline refresh
        _refresh(_);
    }

    /**
     *
     */
    public void deselectAll() {
        orderedSelection.removeAll();
        selected.removeAll();
    }

    /**
     *
     * @param _location
     */
    public void setPicked(IVNode _location) {
        if (selected.get(_location) == null) {
            orderedSelection.insertLast(_location);
            selected.add(_location);
        } else {
            selected.xor(_location);
            orderedSelection.removeAt(_location);
        }
    }

    /**
     *
     * @param _location
     */
    public void setSelected(IVNode _location) {
    }

    /**
     *
     * @param _nodes
     */
    public void setNodes(Object[] _nodes) {
        hashNodes.removeAll();
        hashNodes.add(_nodes);
    }

    /**
     *
     * @param _links
     */
    public void setLinks(Object[] _links) {
        links = _links;
    }

    /**
     *
     */
    public void clearNodes() {
        hashNodes.removeAll();
    }

    /**
     *
     * @return
     */
    public IView popupView() {
        VChain c = new VChain(UV.cSWNW);
        c.add(new VEditValue("Attraction:", attraction, "", false));
        c.add(new VEditValue("Repulsion:", repulsion, "", false));
        c.add(new VEditValue("Iterations:", iterations, "", false));
        VItem b = new VItem("Refresh") {

            @Override
            public void picked(IEvent _e) {
                getRootView().dispose();
                NG2DEnv.this.refresher.signal();
            }
        };
        c.add(b);
        return c;
    }

    /**
     *
     */
    public void refresh() {
        refresher.signal();
    }

    private void _refresh(final IOut _) {

        CSet toPaint = new CSet();

        //Layout

        MinMaxLong mmcount = new MinMaxLong();
        mmcount.value(0);
        for (int i = 0; i < links.length; i++) {
            mmcount.value(((NGLink) links[i]).count);
        }
        Map<Object, Map<Object, Float>> graph = new HashMap<Object, Map<Object, Float>>();
        for (int i = 0; i < links.length; i++) {
            NGLink link = (NGLink) links[i];
            Object source = link.key(0);
            Object target = link.key(1);
            float weight = (float) mmcount.zeroToOne(link.count);
            if (graph.get(source) == null) {
                graph.put(source, new HashMap<Object, Float>());
            }
            graph.get(source).put(target, weight);
        }

        Map<Object, float[]> nodePosition = new HashMap<Object, float[]>();
        Map<Object, Float> nodeDiameter = new HashMap<Object, Float>();
        LinLogProgress llp = new LinLogProgress() {

            public void out(double _count, double _outof) {
                _.out(_count, _outof);
            }

            public void out(Object... _value) {
                _.out(_value);
            }

            public boolean canceled() {
                return _.canceled();
            }
        };

        LinLogLayout.layout(llp,
                graph, nodePosition, nodeDiameter,
                attraction.doubleValue(),
                repulsion.doubleValue(),
                iterations.intValue(),
                false);

        MinMaxDouble xp = new MinMaxDouble();
        MinMaxDouble yp = new MinMaxDouble();
        MinMaxDouble zp = new MinMaxDouble();
        MinMaxDouble mmd = new MinMaxDouble();
        for (Object node : nodePosition.keySet()) {
            float[] position = nodePosition.get(node);
            float diameter = nodeDiameter.get(node);
            mmd.value(diameter);
            xp.value(position[0]);
            yp.value(position[1]);
            zp.value(position[2]);
        }

        double factor = Math.sqrt((1d * 1d) / 2d);
        for (Object node : nodePosition.keySet()) {
            float[] position = nodePosition.get(node);
            double cx = xp.zeroToOne(position[0]);
            double cy = yp.zeroToOne(position[1]);
            double cz = zp.zeroToOne(position[2]);
            float diameter = nodeDiameter.get(node);
            diameter = (float) mmd.zeroToOne(diameter);


            int fontSize = 14;//+(int)(diameter*16);
            DoLocation l = new DoLocation((IVNode) node, fontSize);
            double rank = (double) (((IVNode) node).getCount()) / (double) maxNodeCount;
            double percent = UDouble.percent(rank, 0, 1);
            percent = 1d - UDouble.constrain(percent, 0, 1 - Double.MIN_VALUE);
            AColor color = AColor.getWarmToCool(percent);
            l.color = color;
            toPaint.add(l);

            l.x(cx);
            l.y(cy);
            l.z(cz);

            if (Double.isNaN(l.z())) {
                l.z(0);
            }
        }

        nodes.removeAll();
        nodes.add(toPaint.getAll(Object.class));
        loose.listModified(_);
        _.out("");
        refreshed();
    }

    class DoLocation extends AItem implements IXYZ, Comparable, IMouseEvents, IMouseMotionEvents {

        IVNode key;
        AColor color = null;
        IXYZ xyz;

        DoLocation(IVNode _key, int _fontSize) {
            key = _key;
            if (key.value() instanceof IXYZ) {
                xyz = (IXYZ) key.value();
            } else {
                xyz = new XYZ_D(0, 0, 0);
            }
            VChain c = new VChain(UV.cEW);
            if (key.value() instanceof IView) {
                c.add((IView) key.value());
            } else if (key.value() instanceof IEnteredOrExited) {
                c.add(((IEnteredOrExited) key.value()).exitedView());
            } else {
                VString s = new VString(key, new AFont(AFont.cPlain, _fontSize), ViewColor.cVisualizeThemeFont);
                c.add(s);
            }
            setContent(c);
            setBorder(null);
        }

        @Override
        public void mouseEntered(MouseEntered e) {
            super.mouseEntered(e);

            if (!UV.isChild(this, (IView) e.getExited())) {
                if (key.value() instanceof IEnteredOrExited) {
                    setView(((IEnteredOrExited) key.value()).enteredView());
                }
            }
        }

        @Override
        public void mouseExited(MouseExited e) {
            super.mouseExited(e);
            if (!UV.isChild(this, (IView) e.getEntered())) {
                if (key.value() instanceof IEnteredOrExited) {
                    setView(((IEnteredOrExited) key.value()).exitedView());
                }
            }
        }

        public float getX() {
            return UFloat.clamp((float) (cw * x()) - (getW() / 2), 16, cw - getW() - 16);
        }

        public float getY() {
            return UFloat.clamp((float) (ch * y()) - (getH() / 2), 16, ch - getH() - 16);
        }

        public IToolTip getToolTip() {
            if (key.value() instanceof IView) {
                return ((IView) key.value()).getToolTip();
            }
            return super.getToolTip();
        }

        public String toString() {
            return key.toString();
        }

        public void picked(IEvent _e) {
            setPicked(key);
        }

        public void selected(IEvent _e) {
            setSelected(key);
        }

        public Object getValue() {
            return key;
        }

        public void mouseReleased(final MouseReleased _e) {
            super.mouseReleased(_e);
            if (_e.isRightClick() && _e.isSingleClick()) {
                UV.popup(this, _e, NG2DEnv.this.popupView(), true);
            }
            loose.layoutInterior();
        }
        // IXYZ
        
        public double x() {
            return xyz.x();
        }
        public double y() {
            return xyz.y();
        }
        public double z() {
            return xyz.z();
        }



        public void x(double _px) {
            xyz.x(_px);
        }
        public void y(double _py) {
            xyz.y(_py);
        }
        public void z(double _pz) {
            xyz.z(_pz);
        }
        
        // Comparable
        
        public int compareTo(Object other) {
            double thisVal = z();
            double otherVal = ((IXYZ) other).z();
            return (otherVal < thisVal ? -1 : (otherVal == thisVal ? 0 : 1));
        }

        // IMouseMotionEvents
        public void mouseMoved(MouseMoved e) {
        }

        public void mouseDragged(MouseDragged e) {
            int dx = e.getDeltaX();
            int dy = e.getDeltaY();

            double xp = (double) dx / (double) cw;
            double yp = (double) dy / (double) ch;
            xyz.x(xyz.x() + xp);
            xyz.y(xyz.y() + yp);
            paint();
        }
    }
}

