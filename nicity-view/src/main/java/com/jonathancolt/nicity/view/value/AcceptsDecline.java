/*
 * AcceptsDecline.java.java
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

import com.jonathancolt.nicity.view.border.PopupBorder;
import com.jonathancolt.nicity.view.core.AWindow;
import com.jonathancolt.nicity.view.core.Placer;
import com.jonathancolt.nicity.view.core.RigidBox;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VButton;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.ViewText;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.event.WindowActivated;
import com.jonathancolt.nicity.view.event.WindowClosed;
import com.jonathancolt.nicity.view.event.WindowDeactivated;
import com.jonathancolt.nicity.view.event.WindowDeiconified;
import com.jonathancolt.nicity.view.event.WindowIconified;
import com.jonathancolt.nicity.view.event.WindowOpened;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.interfaces.IWindowEvents;

/**
 *
 * @author Administrator
 */
public class AcceptsDecline extends Viewer implements IWindowEvents {

    String[] message;
    String declineString = " decline ";

    /**
     *
     * @param _explain
     * @param _accepts
     */
    public AcceptsDecline(String _explain, String[] _accepts) {
        this(new String[]{_explain}, _accepts, " decline ");
    }

    /**
     *
     * @param _explain
     * @param _accepts
     */
    public AcceptsDecline(String[] _explain, String[] _accepts) {
        this(_explain, _accepts, " decline ");
    }

    /**
     *
     * @param _explain
     * @param _accepts
     * @param _declineString
     */
    public AcceptsDecline(String[] _explain, String[] _accepts, String _declineString) {

        declineString = _declineString;
        VChain chain = new VChain(UV.cSWNW);


        if (_explain != null) {
            VChain viewer = new VChain(UV.cEW, new ViewText(_explain));
            viewer.setBorder(new PopupBorder(null, 20));
            viewer.spans(UV.cXEW);
            chain.add(viewer);
        }

        VChain menu = new VChain(UV.cEW);

        for (int i = 0; i < _accepts.length; i++) {
            final String name = _accepts[i];
            final int accepted = i;
            VButton accept = new VButton(name) {

                @Override
                public void picked(IEvent _e) {
                    _accept(name, accepted);
                    if (window != null) {
                        window.dispose();
                    }
                }
            };
            menu.add(accept);
            menu.add(new RigidBox(20, 20));
        }

        VButton decline = new VButton(declineString) {

            @Override
            public void picked(IEvent _e) {
                _decline();
                if (window != null) {
                    window.dispose();
                }
            }
        };

        menu.add(decline);
        menu.spans(UV.cXEW);
        menu.setBorder(new PopupBorder(null, 20));
        chain.add(menu, UV.cSN);
        setPlacer(new Placer(chain));
    }

    private void pleaseWait() {
        VChain chain = new VChain(UV.cSN);
        String[] _explain = new String[]{
            "Please Wait the system is ",
            "processing your response..."
        };
        VChain viewer = new VChain(UV.cEW, new ViewText(_explain));
        viewer.setBorder(new PopupBorder(null, 20));
        viewer.spans(UV.cXEW);
        chain.add(viewer);
        setPlacer(new Placer(chain));
        paint();
    }
    private boolean choosed = false;

    private void _accept(String _name, int _number) {
        if (choosed) {
            return;
        }
        choosed = true;
        pleaseWait();
        accept(_name, _number);
        if (window != null) {
            window.dispose();
        }
    }

    /**
     *
     * @param _name
     * @param _id
     */
    public void accept(String _name, int _id) {
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
        return " Choice ";
    }
    AWindow window;

    /**
     *
     * @param _centerRelativeTo
     */
    public void toFront(IView _centerRelativeTo) {
        UV.popup(_centerRelativeTo, UV.cCC, this, true, true);
        /*if (window == null) {
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
