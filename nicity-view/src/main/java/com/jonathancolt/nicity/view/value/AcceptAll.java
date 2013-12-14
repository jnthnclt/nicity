/*
 * AcceptAll.java.java
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

import com.jonathancolt.nicity.core.collection.IBackcall;
import com.jonathancolt.nicity.view.border.ButtonBorder;
import com.jonathancolt.nicity.view.border.PopupBorder;
import com.jonathancolt.nicity.view.core.AWindow;
import com.jonathancolt.nicity.view.core.Placer;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VButton;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.VPan;
import com.jonathancolt.nicity.view.core.VString;
import com.jonathancolt.nicity.view.core.ViewText;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.event.WindowActivated;
import com.jonathancolt.nicity.view.event.WindowClosed;
import com.jonathancolt.nicity.view.event.WindowDeactivated;
import com.jonathancolt.nicity.view.event.WindowDeiconified;
import com.jonathancolt.nicity.view.event.WindowIconified;
import com.jonathancolt.nicity.view.event.WindowOpened;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IVItem;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.interfaces.IWindowEvents;
import com.jonathancolt.nicity.view.list.VItem;
import com.jonathancolt.nicity.view.list.VList;

/**
 *
 * @author Administrator
 */
public class AcceptAll extends Viewer implements IWindowEvents {

    String[] message;
    IBackcall backcall;

    /**
     *
     * @param _explain
     * @param _all
     */
    public AcceptAll(String _explain, Object[] _all) {
        this(new String[]{_explain}, _all);
    }

    /**
     *
     * @param _explain
     * @param _all
     */
    public AcceptAll(String[] _explain, Object[] _all) {
        backcall = null;

        VChain chain = new VChain(UV.cSN);
        if (_explain != null) {
            VChain viewer = new VChain(UV.cEW, new ViewText(_explain));
            viewer.setBorder(new PopupBorder(null, 20));
            viewer.spans(UV.cXEW);
            chain.add(viewer);
        }

        VList list = new VList(backcall, 1) {

            @Override
            public IVItem vItem(Object _value) {
                return new VItem(new VString(_value), _value) {

                    @Override
                    public void picked(IEvent _e) {
                        _accept(value);
                    }

                    @Override
                    public void selected(IEvent _e) {
                        _accept(value);
                    }
                };
            }
        };
        VPan accept = new VPan(list, 320, 240);

        VButton decline = new VButton(" Decline ") {

            @Override
            public void picked(IEvent _e) {
                _decline();
                if (window != null) {
                    window.dispose();
                }
            }
        };

        VChain menu = new VChain(UV.cEW);
        menu.add(decline);
        menu.spans(UV.cXEW);
        menu.setBorder(new PopupBorder(null, 20));

        chain.add(accept);
        chain.add(menu);


        setPlacer(new Placer(chain));
    }

    private void pleaseWait() {
        VChain chain = new VChain(UV.cSN);
        String[] _explain = new String[]{
            "Please Wait the system ",
            "is processing your response"
        };
        VChain viewer = new VChain(UV.cEW, new ViewText(_explain));
        viewer.setBorder(new PopupBorder(null, 20));
        viewer.spans(UV.cXEW);
        chain.add(viewer);
        setPlacer(new Placer(chain));
        setBorder(new ButtonBorder());
        paint();
    }
    private boolean choosed = false;

    private void _accept(Object _accepted) {
        if (choosed) {
            return;
        }
        choosed = true;
        pleaseWait();
        accept(_accepted);
        if (window != null) {
            window.dispose();
        }
    }

    /**
     *
     * @param _accepted
     */
    public void accept(Object _accepted) {
    }

    private void _decline() {
        if (choosed) {
            return;
        }
        choosed = true;
        pleaseWait();
        decline();
        if (window != null) {
            window.dispose();
        }
    }

    /**
     *
     */
    public void decline() {
    }

    @Override
    public String toString() {
        return "Please Help :)";
    }
    AWindow window;

    /**
     *
     * @param _centerRelativeTo
     */
    public void toFront(IView _centerRelativeTo) {
        UV.popup(_centerRelativeTo, UV.cCC, this, true, true);
        /*
        if (window == null) {
            VFrame frameViewer = new VFrame(this, this);
            window = new AWindow(frameViewer);
            window.setTitle(this.toString());
            if (_centerRelativeTo == null) {
                UV.centerWindow(window);
            } else {
                UV.centerWindowRelativeToView(window, _centerRelativeTo);
            }
            window.show();
        } else {
            window.toFront();
        }*/
    }

    // IWindowEvents
    /**
     *
     * @param _e
     */
    @Override
    public void windowOpened(WindowOpened _e) {
    }

    /**
     *
     * @param _e
     */
    @Override
    public void windowClosed(WindowClosed _e) {
        window = null;
        _decline();
    }

    /**
     *
     * @param _e
     */
    @Override
    public void windowActivated(WindowActivated _e) {
    }

    /**
     *
     * @param _e
     */
    @Override
    public void windowDeactivated(WindowDeactivated _e) {
    }

    /**
     *
     * @param _e
     */
    @Override
    public void windowIconified(WindowIconified _e) {
    }

    /**
     *
     * @param _e
     */
    @Override
    public void windowDeiconified(WindowDeiconified _e) {
    }
}
