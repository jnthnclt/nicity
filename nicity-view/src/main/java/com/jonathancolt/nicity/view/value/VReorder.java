/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonathancolt.nicity.view.value;

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

import com.jonathancolt.nicity.core.collection.CArray;
import com.jonathancolt.nicity.view.border.ItemBorder;
import com.jonathancolt.nicity.view.core.UDrop;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VButton;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.VIcon;
import com.jonathancolt.nicity.view.core.VPan;
import com.jonathancolt.nicity.view.core.VString;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.event.AInputEvent;
import com.jonathancolt.nicity.view.interfaces.IDropMode;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.list.AItem;
import com.jonathancolt.nicity.view.list.VList;
import com.jonathancolt.nicity.view.rpp.IRPPViewable;

/**
 *
 * @author Administrator
 */
abstract public class VReorder extends Viewer implements IRPPViewable {
    
    public static IView viewable(String[] args) {
        VReorder ro = new VReorder() {
            Object[] order = new Object[]{"five","two","seven","nine","one","a","b","c","d","e","f","g","h","i"};
            @Override
            public void ordered(Object[] _newOrder) {
                order = _newOrder;
            }

            @Override
            public Object[] order() {
                return order;
            }
        };
        ro.refresh();
        return ro;
    }

    
    /**
     *
     * @param _args
     */
    public static void main(String[] _args) {
        UV.exitFrame(new Viewer(viewable(_args)), "");
    }

    /**
     *
     * @param _newOrder
     */
    abstract public void ordered(Object[] _newOrder);
    /**
     *
     * @return
     */
    abstract public Object[] order();

    /**
     *
     * @param _view
     * @return
     */
    public IView view(Object _view) {
        return new VString(_view);
    }
    private CArray<VPosition> order = new CArray<>(VPosition.class);

    /**
     *
     */
    public VReorder() {
        VList list = new VList(order, 1);
        list.setComparator(null);
        VChain c = new VChain(UV.cSN);
        c.add(new VPan(list, 400, 600));
        c.add(new VButton("Done") {

            @Override
            public void picked(IEvent _e) {
                Object[] done = new Object[order.getCount()];
                VPosition[] all = order.getAll();
                for (int i = 0; i < all.length; i++) {
                    done[i] = all[i].getValue();
                }
                ordered(done);
            }
        });
        setContent(c);
    }

    /**
     *
     */
    public void refresh() {
        order.removeAll();
        Object[] all = order();
        VPosition[] positions = new VPosition[all.length];
        for (int i = 0; i < all.length; i++) {
            positions[i] = new VPosition(all[i]);
        }
        order.insertLast(positions);
    }

    class VPosition extends AItem {

        Object value;

        VPosition(Object _value) {
            value = _value;
            final VPosition _this = this;
            VChain c = new VChain(UV.cEW);
            c.add(VIcon.icon("drop",16));// drag spot
            c.add(UV.border(new VButton(VIcon.icon("up32x32",16)) {

                @Override
                public void picked(IEvent _e) {
                    int i = order.getIndex(_this);
                    if (i != -1) {
                        order.removeAt(i);
                        if (i - 1 >= 0) {
                            order.insertAt(_this, i - 1);
                        }
                    }
                }

                @Override
                public Object getParcel() {
                    return null;
                }

                @Override
                public IDropMode accepts(Object value, AInputEvent _e) {
                    IDropMode mode = UDrop.accepts(new Class[]{
                                VPosition.class, VPosition[].class
                            }, value);
                    return mode;
                }

                @Override
                public void dropParcel(Object dropped, IDropMode mode) {
                    if (dropped instanceof VPosition) {
                        if (dropped == _this) {
                            return;
                        }
                        int fi = order.getIndex(dropped);
                        order.removeAt(fi);
                        int ai = order.getIndex(_this);
                        if (ai - 1 >= 0) {
                            order.insertAt((VPosition)dropped, ai);
                        } else {
                            order.insertFirst((VPosition)dropped);
                        }
                    }
                }
            }, new ItemBorder()));
            c.add(UV.border(new VButton(VIcon.icon("down32x32",16)) {

                @Override
                public void picked(IEvent _e) {
                    int i = order.getIndex(_this);
                    if (i != -1) {
                        order.removeAt(i);
                        order.insertAt(_this, i + 1);
                    }
                }

                @Override
                public Object getParcel() {
                    return null;
                }

                @Override
                public IDropMode accepts(Object value, AInputEvent _e) {
                    IDropMode mode = UDrop.accepts(new Class[]{
                                VPosition.class, VPosition[].class
                            }, value);
                    return mode;
                }

                @Override
                public void dropParcel(Object dropped, IDropMode mode) {
                    if (dropped instanceof VPosition) {
                        if (dropped == _this) {
                            return;
                        }
                        int fi = order.getIndex(dropped);
                        order.removeAt(fi);
                        int ai = order.getIndex(_this);
                        order.insertAt((VPosition)dropped, ai + 1);
                    }
                }
            }, new ItemBorder()));
            c.add(view(value));
            setContent(c);
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public Object getParcel() {
            return this;
        }
    }
}
