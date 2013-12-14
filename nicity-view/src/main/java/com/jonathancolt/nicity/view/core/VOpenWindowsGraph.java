/*
 * VOpenWindowsGraph.java.java
 *
 * Created on 01-03-2010 01:33:51 PM
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
package com.jonathancolt.nicity.view.core;

/*
 * #%L
 * nicity-view
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

import com.jonathancolt.nicity.core.collection.CSet;
import com.jonathancolt.nicity.core.collection.keyed.KeyedCSet;
import com.jonathancolt.nicity.core.collection.keyed.KeyedValue;
import com.jonathancolt.nicity.view.adaptor.VS;
import com.jonathancolt.nicity.view.border.WindowBorder;
import com.jonathancolt.nicity.view.image.IImage;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IRootView;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.list.VItem;
import com.jonathancolt.nicity.view.ngraph.IVNode;
import com.jonathancolt.nicity.view.ngraph.LinkDrawer;
import com.jonathancolt.nicity.view.ngraph.NG;
import com.jonathancolt.nicity.view.ngraph.NG2DEnv;
import com.jonathancolt.nicity.view.ngraph.ULinkDrawer;
import com.jonathancolt.nicity.view.paint.UPaint;

/**
 *
 * @author Administrator
 */
public class VOpenWindowsGraph {

    /**
     *
     * @param _a
     * @param _v
     * @param _name
     */
    public static void frame(IRootView _a, IView _v, Object _name) {
        UV.frame(_v, _name);
        VChain c = new VChain(UV.cSN);
        c.add(UV.border(new VItem(new VString("....", UV.fonts[UV.cSmall])) {

            @Override
            public void picked(IEvent _e) {
                VOpenWindowsGraph.toFront(getRootView());
            }
        }, new WindowBorder(ViewColor.cTheme, 0)));
        c.add(_v);
        IRootView b = UV.frame(c, _name);
        openedBy(_a, b);
    }
    private static CSet graph = new CSet();
    private static CSet open = new CSet();

    /**
     *
     * @param _a
     * @param _b
     */
    public static void openedBy(IRootView _a, IRootView _b) {
        KeyedCSet.add(graph, _b, _a);
        open.add(_a);
        open.add(_b);
    }
    private static VStatus status = new VStatus();
    private static VFloater floatGraph = new VFloater("Open Windows Graph", status) {

        @Override
        public void toFront() {
            VOpenWindowsGraph.refreshGraph();
            super.toFront();
        }
    };
    private static IRootView lastRoot;

    /**
     *
     * @param _root
     */
    public static void toFront(IRootView _root) {
        lastRoot = _root;
        floatGraph.toFront();
    }

    /**
     *
     */
    public static void refreshGraph() {
        int _w = 1000;
        int _h = 1000;
        status.out("Refreshing...");
        NG2DEnv env;
        env = new NG2DEnv(_w, _h) {

            @Override
            public void setPicked(IVNode _node) {
                super.setPicked(_node);
                floatGraph.close();
                ((AWindow) _node.key()).toFront();
                deselectAll();
            }
        };
        // clean out disposed or invalid
        for (Object a : open.getAll(Object.class)) {
            if (a instanceof AWindow) {
                if (((AWindow) a).isValid()) {
                    continue;
                }
            }
            open.remove(a);
        }
        double p = Math.sqrt(open.getCount());
        int w = (int) (_w / (2 * p));
        int h = (int) (_h / (2 * p));


        NG ng = new NG();
        CSet views = new CSet();
        for (Object a : open.getAll(Object.class)) {
            AWindow aw = (AWindow) a;
            IView vaw = viewGraphNode(aw, w, h, views);
            if (vaw == null) {
                KeyedCSet.removeAll(graph, aw);
                continue;
            }
            Object[] nexts = KeyedCSet.getAll(graph, aw);
            if (nexts == null || nexts.length == 0) {
                ng.order(vaw, aw, 1, vaw, aw, 1, 1, gl);
            } else {
                for (Object n : nexts) {
                    AWindow nw = (AWindow) n;
                    IView vnw = viewGraphNode(nw, w, h, views);
                    if (vnw == null) {
                        KeyedCSet.remove(graph, nw, aw);
                        continue;
                    }
                    ng.order(vaw, aw, 1, vnw, nw, 1, 1, gl);
                }
            }

        }
        env.set(status, ng);
        status.out("");
        status.setView(env);
    }
    static private LinkDrawer gl = ULinkDrawer.directedLine("Link", AColor.gray);

    static private IView viewGraphNode(final AWindow _step, int _w, int _h, CSet _cache) {
        if (!_step.isValid()) {
            KeyedValue.remove(_cache, _step);
            return null;
        }
        IView got = (IView) KeyedValue.get(_cache, _step);
        if (got != null) {
            return got;
        }
        VChain sn = new VChain(UV.cSWNW);
        sn.add(new RigidBox(_w, _h) {

            IImage cache;

            @Override
            public void paintBorder(ICanvas _g, int _x, int _y, int _w, int _h) {
                if (cache != null) {
                    _g.drawImage(cache, _x, _y, _w, _h, null);
                } else {
                    IImage bi = UV.toImage(_step);
                    cache = VS.systemImage(_w, _h, VS.c32BitARGB);
                    ICanvas g = cache.canvas(_g.who());
                    g.drawImage(bi, _x, _y, _w, _h, null);
                    g.dispose();
                    _g.drawImage(cache, _x, _y, _w, _h, null);
                }
                if (lastRoot == _step) {
                    UPaint.accent(_g, _x, _y, _h, _h, AColor.lightBlue, ViewColor.cTheme);
                }
            }
        });

        VChain m = new VChain(UV.cSWNW);
        m.add(UV.zone(sn));
        m.setBorder(new WindowBorder(ViewColor.cTheme, 2));

        KeyedValue.add(_cache, m, _step);
        return m;
    }
}
