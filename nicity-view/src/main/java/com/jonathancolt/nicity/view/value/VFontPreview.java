/*
 * VFontPreview.java.java
 *
 * Created on 03-12-2010 06:39:43 PM
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

import com.jonathancolt.nicity.core.value.Value;
import com.jonathancolt.nicity.view.adaptor.IFontConstants;
import com.jonathancolt.nicity.view.border.ViewBorder;
import com.jonathancolt.nicity.view.core.AFont;
import com.jonathancolt.nicity.view.core.DragAndDrop;
import com.jonathancolt.nicity.view.core.PickupAndDrop;
import com.jonathancolt.nicity.view.core.Placer;
import com.jonathancolt.nicity.view.core.UDrop;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.ViewString;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.event.AInputEvent;
import com.jonathancolt.nicity.view.event.MouseDragged;
import com.jonathancolt.nicity.view.event.MouseEntered;
import com.jonathancolt.nicity.view.event.MouseExited;
import com.jonathancolt.nicity.view.event.MouseMoved;
import com.jonathancolt.nicity.view.event.MousePressed;
import com.jonathancolt.nicity.view.event.MouseReleased;
import com.jonathancolt.nicity.view.interfaces.IDrag;
import com.jonathancolt.nicity.view.interfaces.IDrop;
import com.jonathancolt.nicity.view.interfaces.IDropMode;
import com.jonathancolt.nicity.view.interfaces.IMouseEvents;
import com.jonathancolt.nicity.view.interfaces.IMouseMotionEvents;

/**
 *
 * @author Administrator
 */
public class VFontPreview extends Viewer implements IDrag, IDrop, IMouseEvents, IMouseMotionEvents {

    /**
     *
     */
    protected ViewString fontString;

    /**
     *
     * @param _font
     * @param _title
     */
    public VFontPreview(AFont _font, String _title) {
        super();
        if (_font == null) {
            _font = new AFont(IFontConstants.cPlain, 12);
        }
        if (_title == null) {
            _title = "null";
        }

        fontString = new ViewString(_title, _font);
        ViewString fontName = new ViewString(_font.getFontName());
        VChain preview = new VChain(UV.cEW, fontName, fontString);
        preview.spans(UV.cXE);

        setPlacer(new Placer(preview));
        setBorder(new ViewBorder());
        spans(UV.cXE);
    }

    /**
     *
     * @param _font
     */
    public void setFont(AFont _font) {
        if (_font == null) {
            return;
        }
        fontString.getFont().setFont(_font.getFont());
        repair();
        flush();
    }

    /**
     *
     * @return
     */
    public AFont getFont() {
        return fontString.getFont();
    }

    // IDrag
    /**
     *
     * @return
     */
    @Override
    public Object getParcel() {
        return new VFontPreview(fontString.getFont(), fontString.getText());
    }

    // IDrop
    /**
     *
     * @param object
     * @param _e
     * @return
     */
    @Override
    public IDropMode accepts(Object object, AInputEvent _e) {
        return UDrop.accepts(new Class[]{VFontPreview.class}, object);
    }

    /**
     *
     * @param object
     * @param mode
     */
    @Override
    public void dropParcel(Object object, IDropMode mode) {
        if (object == this) {
            return;
        }
        if (object instanceof VFontPreview) {
            setFont(((VFontPreview) object).getFont());
        }
    }

    // IMouseEvents
    /**
     *
     * @param e
     */
    @Override
    public void mouseEntered(MouseEntered e) {
        DragAndDrop.cDefault.mouseEntered(e);
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseExited(MouseExited e) {
        DragAndDrop.cDefault.mouseExited(e);
    }

    /**
     *
     * @param e
     */
    @Override
    public void mousePressed(MousePressed e) {
        DragAndDrop.cDefault.mousePressed(e);
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseReleased e) {
        if (PickupAndDrop.cDefault.event(e)) {
            return;
        }
        DragAndDrop.cDefault.mouseReleased(e);
        VFontBrowser fb = new VFontBrowser(new Value(getFont()), fontString.getText());
        UV.frame(fb, " FontBrowser ");
    }

    // IMouseMotionEvents
    /**
     *
     * @param e
     */
    @Override
    public void mouseMoved(MouseMoved e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseDragged(MouseDragged e) {
        DragAndDrop.cDefault.mouseDragged(e);
    }
}
