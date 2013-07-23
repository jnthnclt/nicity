/*
 * GlueAWTGraphicsToCanvas.java.java
 *
 * Created on 01-03-2010 01:31:35 PM
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
package com.jonathancolt.nicity.view.canvas;

import com.jonathancolt.nicity.view.adaptor.IPath;
import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.core.AFont;
import com.jonathancolt.nicity.view.flavor.AFlavor;
import com.jonathancolt.nicity.view.image.IImage;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.File;

/**
 *
 * @author Administrator
 */
public class GlueAWTGraphicsToCanvas implements ICanvas {

    private long who = 0;
    private Graphics g;

    /**
     *
     * @param _who
     * @param _g
     */
    public GlueAWTGraphicsToCanvas(long _who, Graphics _g) {
        who = _who;
        g = _g;
        ((Graphics2D) g).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        ((Graphics2D) g).setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

    /**
     *
     * @return
     */
    public Graphics graphics() {
        return g;
    }

    /**
     *
     * @return
     */
    @Override
    public long who() {
        return who;
    }

    /**
     *
     */
    @Override
    public void dispose() {
        g.dispose();
    }

    /**
     *
     * @param _fill
     * @param x
     * @param y
     * @param w
     * @param h
     * @param start
     * @param arcAngle
     */
    @Override
    public void arc(boolean _fill, int x, int y, int w, int h, int start, int arcAngle) {
        if (_fill) {
            g.fillArc(x, y, w, h, start, arcAngle);
        } else {
            g.drawArc(x, y, w, h, start, arcAngle);
        }
    }

    /**
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    @Override
    public void line(int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
    }

    /**
     *
     * @param _fill
     * @param x
     * @param y
     * @param w
     * @param h
     */
    @Override
    public void oval(boolean _fill, int x, int y, int w, int h) {
        if (_fill) {
            g.fillOval(x, y, w, h);
        } else {
            g.drawOval(x, y, w, h);
        }
    }

    /**
     *
     * @param _fill
     * @param xPoints
     * @param yPoints
     * @param nPoints
     */
    @Override
    public void polygon(boolean _fill, int[] xPoints, int[] yPoints, int nPoints) {
        if (_fill) {
            g.fillPolygon(xPoints, yPoints, nPoints);
        } else {
            g.drawPolygon(xPoints, yPoints, nPoints);
        }
    }

    /**
     *
     * @param xPoints
     * @param yPoints
     * @param nPoints
     */
    @Override
    public void polyline(int[] xPoints, int[] yPoints, int nPoints) {
        g.drawPolyline(xPoints, yPoints, nPoints);
    }

    /**
     *
     * @param _fill
     * @param x
     * @param y
     * @param w
     * @param h
     */
    @Override
    public void rect(boolean _fill, int x, int y, int w, int h) {
        if (_fill) {
            g.fillRect(x, y, w, h);
        } else {
            g.drawRect(x, y, w, h);
        }
    }

    /**
     *
     * @param _fill
     * @param x
     * @param y
     * @param w
     * @param h
     * @param arcWidth
     * @param arcHeight
     */
    @Override
    public void roundRect(boolean _fill, int x, int y, int w, int h, int arcWidth, int arcHeight) {
        if (_fill) {
            g.fillRoundRect(x, y, w, h, arcWidth, arcHeight);
        } else {
            g.drawRoundRect(x, y, w, h, arcWidth, arcHeight);
        }
    }

    /**
     *
     * @param str
     * @param x
     * @param y
     */
    @Override
    public void drawString(String str, int x, int y) {
        g.drawString(str, x, y);
    }

    /**
     *
     * @param x
     * @param y
     * @param w
     * @param h
     */
    @Override
    public void setClip(int x, int y, int w, int h) {
        g.setClip(x, y, w, h);
    }

    /**
     *
     * @param _color
     */
    @Override
    public void setColor(AColor _color) {
        g.setColor(new Color(_color.intValue(), true));
    }

    /**
     *
     * @param _font
     */
    @Override
    public void setFont(AFont _font) {
        g.setFont((Font)_font.getFont().getNativeFont());
    }

    /**
     *
     * @return
     */
    @Override
    public Object getClip() {
        return g.getClip();
    }

    /**
     *
     * @param _clip
     */
    @Override
    public void setClip(Object _clip) {
        g.setClip((Shape) _clip);
    }

    /**
     *
     * @param _image
     * @param _x
     * @param _y
     * @param _observer
     */
    @Override
    public void drawImage(Object _image, int _x, int _y, Object _observer) {
        if (_image instanceof IImage) {
            _image = (Image) ((IImage) _image).data(0);
        }
        g.drawImage((Image) _image, _x, _y, (ImageObserver) _observer);
    }

    /**
     *
     * @param _image
     * @param _x
     * @param _y
     * @param _w
     * @param _h
     * @param _observer
     */
    @Override
    public void drawImage(Object _image, int _x, int _y, int _w, int _h, Object _observer) {
        if (_image instanceof IImage) {
            _image = (Image) ((IImage) _image).data(0);
        }
        g.drawImage((Image) _image, _x, _y, _w, _h, (ImageObserver) _observer);
    }

    /**
     *
     * @param _image
     * @param _dx1
     * @param _dy1
     * @param _dx2
     * @param _dy2
     * @param _sx1
     * @param _sy1
     * @param _sx2
     * @param _sy2
     * @param _observer
     */
    @Override
    public void drawImage(Object _image,
            int _dx1, int _dy1, int _dx2, int _dy2,
            int _sx1, int _sy1, int _sx2, int _sy2,
            Object _observer) {
        if (_image instanceof IImage) {
            _image = (Image) ((IImage) _image).data(0);
        }
        g.drawImage((Image) _image, _dx1, _dy1, _dx2, _dy2, _sx1, _sy1, _sx2, _sy2, (ImageObserver) _observer);
    }

    /**
     *
     * @param _fill
     */
    @Override
    public void fill(Object _fill) {
        if (_fill instanceof IPath) {
            _fill = ((IPath)_fill).getRawPath();
        }
        ((Graphics2D) g).fill((Shape) _fill);
    }

    /**
     *
     * @param _draw
     */
    @Override
    public void draw(Object _draw) {
        if (_draw instanceof IPath) {
            _draw = ((IPath)_draw).getRawPath();
        }
        ((Graphics2D) g).draw((Shape) _draw);
    }

    /**
     *
     * @param _draw
     */
    @Override
    public void setPaint(Object _draw) {
        if (_draw instanceof IPath) {
            _draw = ((IPath)_draw).getRawPath();
        }
        ((Graphics2D) g).setPaint((Paint) _draw);
    }

    /**
     *
     * @param _angle
     */
    @Override
    public void rotate(double _angle) {
        ((Graphics2D) g).rotate(_angle);
    }

    /**
     * 
     * @return
     */
    @Override
    public Object getTransform() {
        return ((Graphics2D) g).getTransform();
    }

    /**
     *
     * @param theta
     * @param fx
     * @param fy
     */
    @Override
    public void rotate(double theta, double fx, double fy) {
        ((Graphics2D) g).rotate(theta, fx, fy);
    }

    /**
     *
     * @param restore
     */
    @Override
    public void setTransform(Object restore) {
        ((Graphics2D) g).setTransform((AffineTransform) restore);
    }

    /**
     *
     * @param x
     * @param y
     */
    public void translate(int x, int y) {
        g.translate(x, y);
    }

    /**
     *
     * @param x
     * @param y
     */
    @Override
    public void translate(double x, double y) {
        ((Graphics2D) g).translate(x, y);
    }

    /**
     *
     * @param _alpha
     * @param _composite
     */
    @Override
    public void setAlpha(float _alpha, int _composite) {
        int rule = _composite;
        if (rule == 0) {
            rule = ICanvas.cSRC_OVER;
        }
        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(rule, _alpha));
    }

    /**
     *
     * @param _from
     * @param _fx
     * @param _fy
     * @param _to
     * @param _tx
     * @param _ty
     */
    @Override
    public void setGradient(AColor _from, int _fx, int _fy, AColor _to, int _tx, int _ty) {
        GradientPaint gp = new GradientPaint(_fx, _fy, new Color(_from.intValue(), true), _tx, _ty, new Color(_to.intValue(), true));
        ((Graphics2D) g).setPaint(gp);
    }

    @Override
    public void drawImage(File _image, int _x, int _y, int _w, int _h) {
        Image image = Toolkit.getDefaultToolkit().getImage(_image.getAbsolutePath());
        if (image != null) {
            loadImage(image);
        }
        drawImage(image, _x, _y, _w, _h, null);
    }
    private static int mediaTrackerID = 0;
    private static final Component component = new Component() {
    };
    private static final MediaTracker tracker = new MediaTracker(component);

    private void loadImage(Image image) {
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
            tracker.removeImage(image, id);
        }
    }

    private int getNextID() {
        synchronized (tracker) {
            return ++mediaTrackerID;
        }
    }

    @Override
    public void paintFlavor(AFlavor flavor, int _x, int _y, int _w, int _h, AColor _color) {
        if (flavor == null) {
            return;
        }
        flavor.paintFlavor(this, _x, _y, _w, _h, _color);
    }
}
