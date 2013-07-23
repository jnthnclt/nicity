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
package com.jonathancolt.nicity.view.ngraph;

import com.jonathancolt.nicity.asynchro.ElapseCall;
import com.jonathancolt.nicity.core.collection.CArray;
import com.jonathancolt.nicity.core.collection.CSet;
import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.lang.MinMaxDouble;
import com.jonathancolt.nicity.core.lang.MinMaxLong;
import com.jonathancolt.nicity.core.lang.NullOut;
import com.jonathancolt.nicity.core.lang.UDouble;
import com.jonathancolt.nicity.core.lang.UFloat;
import com.jonathancolt.nicity.core.memory.struct.IXYZ;
import com.jonathancolt.nicity.core.memory.struct.XYZ_D;
import com.jonathancolt.nicity.core.memory.struct.XY_I;
import com.jonathancolt.nicity.core.process.ICall;
import com.jonathancolt.nicity.core.value.Value;
import com.jonathancolt.nicity.view.adaptor.IFontConstants;
import com.jonathancolt.nicity.view.border.SolidBorder;
import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.core.AFont;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.VPan;
import com.jonathancolt.nicity.view.core.VString;
import com.jonathancolt.nicity.view.core.ViewColor;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.event.MouseDragged;
import com.jonathancolt.nicity.view.event.MouseEntered;
import com.jonathancolt.nicity.view.event.MouseExited;
import com.jonathancolt.nicity.view.event.MouseMoved;
import com.jonathancolt.nicity.view.event.MouseReleased;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.interfaces.IEnteredOrExited;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IMouseEvents;
import com.jonathancolt.nicity.view.interfaces.IMouseMotionEvents;
import com.jonathancolt.nicity.view.interfaces.IToolTip;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.list.AItem;
import com.jonathancolt.nicity.view.list.ListController;
import com.jonathancolt.nicity.view.list.VItem;
import com.jonathancolt.nicity.view.list.VLoose;
import com.jonathancolt.nicity.view.value.VEditValue;
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
            @Override
            public void mend() {
                enableFlag(UV.cRepair);//??
                super.mend();
            }

            @Override
            public void mouseReleased(final MouseReleased _e) {
                super.mouseReleased(_e);
                if (_e.isRightClick()) {
                    UV.popup(this, _e, NG2DEnv.this.popupView(), true, true);
                }
            }

            @Override
            public void paintBackground(ICanvas _g, int _x, int _y, int _w, int _h) {
                super.paintBackground(_g, _x, _y, _w, _h);
                _g.setColor(ViewColor.cVisualizeTheme);

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
            @Override
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

        refresher = new ElapseCall(NullOut.cNull, 500, new ICall() {
            @Override
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
        Map<Object, Map<Object, Float>> graph = new HashMap<>();
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

        Map<Object, float[]> nodePosition = new HashMap<>();
        Map<Object, Float> nodeDiameter = new HashMap<>();
        LinLogProgress llp = new LinLogProgress() {
            @Override
            public void out(double _count, double _outof) {
                _.out(_count, _outof);
            }

            @Override
            public void out(Object... _value) {
                _.out(_value);
            }

            @Override
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
                VString s = new VString(key, new AFont(IFontConstants.cPlain, _fontSize), ViewColor.cVisualizeThemeFont);
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

        @Override
        public float getX() {
            return UFloat.clamp((float) (cw * x()) - (getW() / 2), 16, cw - getW() - 16);
        }

        @Override
        public float getY() {
            return UFloat.clamp((float) (ch * y()) - (getH() / 2), 16, ch - getH() - 16);
        }

        @Override
        public IToolTip getToolTip() {
            if (key.value() instanceof IView) {
                return ((IView) key.value()).getToolTip();
            }
            return super.getToolTip();
        }

        @Override
        public String toString() {
            return key.toString();
        }

        @Override
        public void picked(IEvent _e) {
            setPicked(key);
        }

        @Override
        public void selected(IEvent _e) {
            setSelected(key);
        }

        @Override
        public Object getValue() {
            return key;
        }

        @Override
        public void mouseReleased(final MouseReleased _e) {
            super.mouseReleased(_e);
            if (_e.isRightClick() && _e.isSingleClick()) {
                UV.popup(this, _e, NG2DEnv.this.popupView(), true, true);
            }
            loose.paint();
        }
        // IXYZ

        @Override
        public double x() {
            return xyz.x();
        }

        @Override
        public double y() {
            return xyz.y();
        }

        @Override
        public double z() {
            return xyz.z();
        }

        @Override
        public void x(double _px) {
            xyz.x(_px);
        }

        @Override
        public void y(double _py) {
            xyz.y(_py);
        }

        @Override
        public void z(double _pz) {
            xyz.z(_pz);
        }

        // Comparable
        @Override
        public int compareTo(Object other) {
            double thisVal = z();
            double otherVal = ((IXYZ) other).z();
            return (otherVal < thisVal ? -1 : (otherVal == thisVal ? 0 : 1));
        }

        // IMouseMotionEvents
        @Override
        public void mouseMoved(MouseMoved e) {
        }

        @Override
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
