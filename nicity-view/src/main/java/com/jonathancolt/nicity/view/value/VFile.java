/*
 * VFile.java.java
 *
 * Created on 03-12-2010 06:39:57 PM
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

import com.jonathancolt.nicity.core.lang.UFile;
import com.jonathancolt.nicity.view.border.ItemBorder;
import com.jonathancolt.nicity.view.core.Placer;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.VIcon;
import com.jonathancolt.nicity.view.core.VPaintable;
import com.jonathancolt.nicity.view.core.VString;
import com.jonathancolt.nicity.view.core.ViewString;
import com.jonathancolt.nicity.view.event.AMouseEvent;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IMouseEvents;
import com.jonathancolt.nicity.view.interfaces.IMouseMotionEvents;
import com.jonathancolt.nicity.view.list.VItem;
import java.io.File;

/**
 *
 * @author Administrator
 */
public class VFile extends VItem implements IMouseEvents, IMouseMotionEvents {

    VFiles files;

    /**
     *
     * @param _files
     * @param _value
     */
    public VFile(VFiles _files, File _value) {
        files = _files;
        value = _value;
        if (VFiles.fsRootSet.get(_value) != null) {
            VChain chain = new VChain(UV.cEW);
            chain.add(VIcon.icon("resource"));
            chain.add(new VString(_value.toString()));
            setPlacer(new Placer(chain));
        } else {
            VChain chain = new VChain(UV.cEW);
            if (_value == null) {
                chain.add(VIcon.icon("null"));
            } else if (_value.getParentFile() == null) {
                chain.add(VIcon.icon("resource"));
            } else if (_value.isFile()) {
                String extension = UFile.getExtension(_value.getName()).toLowerCase();
                VPaintable icon = null;
                if (extension.isEmpty()) {
                    icon = VIcon.icon("file");
                } else {
                    icon = VIcon.icon("Dot" + extension);
                }
                chain.add(icon);
            } else {
                chain.add(VIcon.icon("folder"));
            }
            if (_value.getParentFile() == null) {
                chain.add(new ViewString(_value.toString()));
            } else {
                chain.add(new VString(this));
            }
            setPlacer(new Placer(chain));
        }
        setBorder(new ItemBorder());
    }

    /**
     *
     * @param e
     */
    @Override
    public void picked(IEvent e) {
        if (e instanceof AMouseEvent) {
            AMouseEvent me = (AMouseEvent) e;
            if (me.isControlDown()) {
                return;
            }
            if (me.isShiftDown()) {
                return;
            }
        }
        if (value instanceof File) {
            files.setPOV(e.who(), (File) value);
        }
    }

    /**
     *
     * @param _e
     */
    @Override
    public void selected(IEvent _e) {
        files.value.setValue(value);
        if (!files.browserOnly) {
            getRootView().dispose();
        }
    }

    /**
     *
     * @param _value
     */
    public void setValue(Object _value) {
        if (_value instanceof File) {
            value = _value;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Object hashObject() {
        return value;
    }

    @Override
    public String toString() {
        if (VFiles.fsRootSet.get(value) != null) {
            return value.toString();
        }
        if (value == null) {
            return "null";
        }
        File file = (File) value;
        String s = ((File) value).getName();
        if (file.isDirectory()) {
            s += File.separator;
        }
        return s;
    }
}
