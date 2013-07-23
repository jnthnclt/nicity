/*
 * ViewImage.java.java
 *
 * Created on 01-03-2010 01:31:38 PM
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
package com.jonathancolt.nicity.view.image;

import com.jonathancolt.nicity.core.memory.struct.XYWH_I;
import com.jonathancolt.nicity.view.adaptor.VS;
import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.interfaces.IPaintable;
import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;

/**
 *
 * @author Administrator
 */
public final class ViewByteArrayImage implements IPaintable {

    transient Image image;
    /**
     *
     */
    protected float scaleX = 1.0f;
    /**
     *
     */
    protected float scaleY = 1.0f;
    /**
     *
     */
    protected boolean loading = false;
    /**
     *
     */
    protected float width = 0f;
    /**
     *
     */
    protected float height = 0f;

    /**
     *
     */
    public ViewByteArrayImage() {
    }

   
    /**
     *
     * @param imageData
     */
    public ViewByteArrayImage(byte[] imageData) {
        image = Toolkit.getDefaultToolkit().createImage(imageData);
        if (image == null) {
            return;
        }
        loadImage(image);
    }

    /**
     *
     * @param _rgbArray
     * @param _w
     * @param _h
     */
    public ViewByteArrayImage(int[] _rgbArray, int _w, int _h) {
        image = (Image) VS.systemImage(_w, _h, VS.c32BitARGB, _rgbArray).data(0);
        loadImage(image);
    }

    /**
     *
     * @param _scale
     */
    public void setXScale(float _scale) {
        scaleX = _scale;
    }

    /**
     *
     * @param _scale
     */
    public void setYScale(float _scale) {
        scaleY = _scale;
    }

    private static int mediaTrackerID = 0;
    private static final Component component = new Component() {
    };
    private static final MediaTracker tracker = new MediaTracker(component);

    /**
     *
     * @param image
     */
    protected void loadImage(Image image) {
        if (image == null) {
            return;
        }
        synchronized (tracker) {

            int id = getNextID();

            tracker.addImage(image, id);
            try {
                tracker.waitForID(id, 0);
            } catch (InterruptedException e) {
                System.out.println("INTERRUPTED while loading Image");
            }
            int loadStatus = tracker.statusID(id, false);
            tracker.removeImage(image, id);
            width = image.getWidth(null);
            height = image.getHeight(null);
        }
    }

    private int getNextID() {
        synchronized (tracker) {
            return ++mediaTrackerID;
        }
    }

    /**
     *
     * @return
     */
    public boolean isLoading() {
        return loading;
    }

    /**
     *
     * @return
     */
    public Image getImage() {
        return image;
    }

    /**
     *
     * @param image
     */
    public void setImage(Image image) {
        this.image = image;
        loadImage(image);
    }

    /**
     *
     * @param g
     * @param _xywh
     */
    @Override
    public void paint(ICanvas g, XYWH_I _xywh) {
        if (loading) {
            g.setColor(AColor.gray);
            g.drawString("loading", 0, 13);
        } else {
            if (_xywh == null) {
                _xywh = new XYWH_I(0, 0, -1, -1);
            }
            if (_xywh.w == -1) {
                _xywh.w = (int) (getW(null, null));
            }
            if (_xywh.h == -1) {
                _xywh.h = (int) (getH(null, null));
            }
            if (image != null) {
                g.drawImage(image, _xywh.x, _xywh.y, _xywh.w, _xywh.h, null);
            }
        }
    }

    /**
     *
     * @param _under
     * @param _over
     * @return
     */
    @Override
    public float getW(IPaintable _under, IPaintable _over) {
        return width * scaleX;
    }

    /**
     *
     * @param _under
     * @param _over
     * @return
     */
    @Override
    public float getH(IPaintable _under, IPaintable _over) {
        return height * scaleY;
    }

    public void scaleTo(int _w, int _h) {
        scaleX = (float)_w/(width);
        scaleY = (float)_h/(height);
    }
}

