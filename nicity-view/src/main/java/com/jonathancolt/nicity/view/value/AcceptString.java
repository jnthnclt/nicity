/*
 * AcceptString.java.java
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

import com.jonathancolt.nicity.view.border.ButtonBorder;
import com.jonathancolt.nicity.view.border.PopupBorder;
import com.jonathancolt.nicity.view.border.TextBorder;
import com.jonathancolt.nicity.view.core.AWindow;
import com.jonathancolt.nicity.view.core.EditString;
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
public class AcceptString extends Viewer implements IWindowEvents {

    String[] message;
    EditString input;

    /**
     *
     * @param _explain
     * @param _string
     */
    public AcceptString(String _explain, String _string) {
        this(new String[]{_explain}, _string);
    }

    /**
     *
     * @param _explain
     * @param _string
     */
    public AcceptString(String[] _explain, String _string) {

        VChain chain = new VChain(UV.cSN);
        if (_explain != null) {
            VChain viewer = new VChain(UV.cEW, new ViewText(_explain));
            viewer.setBorder(new PopupBorder(null, 20));
            viewer.spans(UV.cXEW);
            chain.add(viewer);
        }


        input = new EditString(_string, 200, 800);
        input.setBorder(new TextBorder());
        Viewer inputViewer = new Viewer(input);
        inputViewer.setBorder(new TextBorder());

        VButton accept = new VButton(" Accept ") {

            @Override
            public void picked(IEvent _e) {
                _accept(input.toString());
                if (window != null) {
                    window.dispose();
                }
            }
        };
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
        menu.add(accept);
        menu.add(new RigidBox(20, 20));
        menu.add(decline);
        menu.spans(UV.cXEW);
        menu.setBorder(new PopupBorder(null, 20));

        chain.add(inputViewer);
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
