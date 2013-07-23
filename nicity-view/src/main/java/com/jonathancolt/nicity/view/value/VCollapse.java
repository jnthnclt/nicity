/*
 * VCollapse.java.java
 *
 * Created on 01-03-2010 01:34:45 PM
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

import com.jonathancolt.nicity.core.collection.CSet;
import com.jonathancolt.nicity.core.lang.ICallback;
import com.jonathancolt.nicity.core.lang.UCompare;
import com.jonathancolt.nicity.view.border.ButtonBorder;
import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.VString;
import com.jonathancolt.nicity.view.core.ViewColor;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.list.AItem;

/**
 *
 * @author Administrator
 */
public class VCollapse extends AItem implements Comparable {

    /**
     *
     * @return
     */
    public VChain handleAndContent() {
        return null;
    }
    /**
     *
     */
    protected Object name;
    /**
     *
     */
    protected ICallback picked;
    /**
     *
     */
    protected ICallback contentCallback;
    /**
     *
     */
    protected boolean open = false;
    /**
     *
     */
    protected boolean vertical = false;
    /**
     *
     */
    protected IView content;
    /**
     *
     */
    protected int order = -1;
    /**
     *
     */
    protected int openSpans = 0;
    /**
     *
     */
    protected int closeSpans = 0;
    /**
     *
     */
    public CSet openTogether = new CSet();
    /**
     *
     */
    public CSet closeTogether = new CSet();
    /**
     *
     */
    public CSet closeWhenOpening = new CSet();
    /**
     *
     */
    public CSet openWhenClosing = new CSet();
    /**
     *
     */
    public AColor dark = ViewColor.cThemeShadow;
    /**
     *
     */
    public AColor color = ViewColor.cTheme;
    /**
     *
     */
    public AColor light = ViewColor.cThemeHighlight;

    /**
     *
     * @param _name
     * @param _contentCallback
     * @param _open
     */
    public VCollapse(Object _name, final IView view, boolean _open) {
        this(_name, new ICallback<Object, Object>() {

            @Override
            public Object callback(Object i) {
                return view;
            }
        }, _open, false);
    }

    /**
     *
     * @param _name
     * @param _contentCallback
     * @param _open
     */
    public VCollapse(Object _name, ICallback _contentCallback, boolean _open) {
        this(_name, _contentCallback, _open, false);
    }

    /**
     *
     * @param _name
     * @param _contentCallback
     * @param _open
     * @param _openSpans
     * @param _closeSpans
     */
    public VCollapse(Object _name, ICallback _contentCallback, boolean _open, int _openSpans, int _closeSpans) {
        this(_name, _contentCallback, _open, _openSpans, _closeSpans, ViewColor.cThemeShadow, ViewColor.cTheme, ViewColor.cThemeHighlight);
    }

    /**
     *
     * @param _name
     * @param _contentCallback
     * @param _open
     * @param _openSpans
     * @param _closeSpans
     * @param _dark
     * @param _color
     * @param _light
     */
    public VCollapse(Object _name, ICallback _contentCallback, boolean _open, int _openSpans, int _closeSpans,
            AColor _dark, AColor _color, AColor _light) {
        name = _name;
        contentCallback = _contentCallback;
        open = _open;
        openSpans = _openSpans;
        closeSpans = _closeSpans;
        dark = _dark;
        color = _color;
        light = _light;
        rebuild(null);
    }

    /**
     *
     * @param _name
     * @param _contentCallback
     * @param _open
     * @param _vertical
     */
    public VCollapse(Object _name, ICallback _contentCallback, boolean _open, boolean _vertical) {
        name = _name;
        contentCallback = _contentCallback;
        open = _open;
        vertical = _vertical;
        rebuild(null);
    }



    /**
     *
     * @return
     */
    public boolean isTrue() {
        return open == true;
    }

    /**
     *
     */
    public void open() {
        if (open) {
            return;
        }
        open = true;
        changed();
    }

    /**
     *
     */
    public void close() {
        if (!open) {
            return;
        }
        open = false;
        changed();
    }

    /**
     *
     */
    public void changed() {
        if (open) {
            handleOthers(openTogether, true, false);
            handleOthers(closeWhenOpening, true, false);
        } else {
            handleOthers(closeTogether, true, false);
            handleOthers(openWhenClosing, false, true);
        }
        rebuild(null);
    }

    /**
     *
     * @param _content
     */
    public void rebuild(IView _content) {
        if (picked != null) {
            picked.callback(this);
        }
        VChain c = handleAndContent();
        if (c == null) {
            if (vertical) {
                c = new VChain(UV.cNENW);
            } else {
                c = new VChain(UV.cSWNW);
            }
        }
        Handle handle = new Handle(_content);
        c.add(handle);
        if (_content != null || open) {
            open = true;
            if (_content == null) {
                _content = (IView) contentCallback.callback(this);
            }
            if (_content == null) {
                _content = new VString("Generating Contents", color.invert());
            }
            c.add(_content);
        }

        Viewer m = new Viewer(c);
        if (!open) {
            if (closeSpans != 0) {//!! hacky

                c.unspans(openSpans);
                m.unspans(openSpans);
                unspans(openSpans);

                c.spans(closeSpans);
                m.spans(closeSpans);
                spans(closeSpans);
            }
        } else {
            if (openSpans != 0) {//!! hacky

                c.unspans(closeSpans);
                m.unspans(closeSpans);
                unspans(closeSpans);

                c.spans(openSpans);
                m.spans(openSpans);
                spans(openSpans);
            }
        }
        setBorder(null);
        setContent(m);
        paint();
    }

    private IView openable() {
        VString v = new VString(" + ", UV.fonts[UV.cSmall]) {

            @Override
            public IView spans(int _spans) {
                return this;
            }
        };
        v.setBorder(new ButtonBorder(1));
        return v;
    }

    private IView closeable() {
        VString v = new VString(" - ", UV.fonts[UV.cSmall]) {

            @Override
            public IView spans(int _spans) {
                return this;
            }
        };
        v.setBorder(new ButtonBorder(1));
        return v;
    }

    class Handle extends AItem {

        Handle(IView _content) {
            if (open) {
                if (name instanceof IView) {
                    VChain c = new VChain(UV.cEW, closeable(), (IView) name);
                    c.spans(openSpans);
                    setContent(c);
                } else {
                    setContent(new VChain(UV.cEW, closeable(), new VString(name, color.invert().bw())));
                }
            } else {
                if (name instanceof IView) {
                    VChain c = new VChain(UV.cEW, openable(), (IView) name);
                    c.spans(closeSpans);
                    setContent(c);
                } else {
                    setContent(new VChain(UV.cEW, openable(), new VString(name, color.invert().bw())));
                }
            }
        }

        @Override
        public Object getParcel() {
            return VCollapse.this.getParcel();
        }

        @Override
        public void picked(IEvent _e) {
            open = !open;
            changed();
        }

        @Override
        public void selected(IEvent _e) {
            picked(_e);
        }
    }

    private void handleOthers(CSet _set, boolean _ifState, boolean _desiredState) {
        Object[] all = _set.getAll(Object.class);
        for (Object a : all) {
            VCollapse c = (VCollapse) a;
            if (c.open == _ifState) {
                c.open = _desiredState;
                c.rebuild(null);
            }
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Object getValue() {
        return this;
    }

    // Comparable
    @Override
    public int compareTo(Object o) {
        return (int) UCompare.compare(this.order, ((VCollapse) o).order);
    }
}
