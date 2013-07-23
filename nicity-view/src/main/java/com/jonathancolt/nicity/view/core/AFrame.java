/*
 * AFrame.java.java
 *
 * Created on 01-03-2010 01:31:36 PM
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

import com.jonathancolt.nicity.view.awt.PFrame;
import com.jonathancolt.nicity.view.awt.PeerViewBorder;
import com.jonathancolt.nicity.view.interfaces.IPeerView;
import com.jonathancolt.nicity.view.interfaces.IPlacer;
import com.jonathancolt.nicity.view.interfaces.IView;

/**
 *
 * @author Administrator
 */
public final class AFrame extends AWindow {

    /**
     *
     */
    public AFrame() {
    }

    /**
     *
     * @param _placer
     */
    public AFrame(IPlacer _placer) {
        super(_placer);
    }

    /**
     *
     * @param _view
     */
    public AFrame(IView _view) {
        super(_view);
    }

    /**
     *
     * @param _view
     * @param _flex
     */
    public AFrame(IView _view, Flex _flex) {
        super(_view, _flex);
    }

    /**
     *
     * @param _title
     * @param _message
     */
    public AFrame(String _title, IView _message) {
        super(_message);
        setTitle(_title);
        show();
    }

    /**
     *
     * @return
     */
    @Override
    public IPeerView getPeer() {
        synchronized (getPeerLock) {
            if (peer == null) {
                PFrame _peer = new PFrame(this);
                peer = _peer;

                setBorder(null);
                _peer.setVisible(true);
                peerBorder = new PeerViewBorder(_peer);
                return _peer;
            } else {
                return peer;
            }
        }
    }
    // IView overloading
    private static float minW = 0;

    /**
     *
     * @param _isUndecorated
     */
    public void setUndecorated(boolean _isUndecorated) {
        try {
            getPeer().setUndecorated(_isUndecorated);
        } catch (Throwable x) {
            minW = 150;
        } // 1.3 compatiable
    }

    @Override
    public float getW() {
        float _w = super.getW();
        if (_w < minW) {
            return minW;
        }
        return _w;
    }

    /**
     *
     * @return
     */
    @Override
    public String getTitle() {
        return getPeer().getTitle();
    }

    /**
     *
     * @param _title
     */
    @Override
    public void setTitle(String _title) {
        getPeer().setTitle(_title);
    }
}
