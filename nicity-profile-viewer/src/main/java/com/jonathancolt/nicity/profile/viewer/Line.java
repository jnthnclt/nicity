/*
 * Copyright 2013 jonathan.
 *
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
 */
package com.jonathancolt.nicity.profile.viewer;

/*
 * #%L
 * nicity-profile-viewer
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

import com.jonathancolt.nicity.core.memory.struct.XYWH_I;
import com.jonathancolt.nicity.core.memory.struct.XY_I;
import com.jonathancolt.nicity.view.adaptor.IPath;
import com.jonathancolt.nicity.view.adaptor.VS;
import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.paint.UPaint;
import java.util.Set;

/**
 *
 * @author jonathan
 */
public class Line {
    final InterfaceArea fromArea;
    final XYWH_I fromRect;
    final InterfaceArea.MethodArea toArea;
    final XYWH_I toRect;
    final Set<String> pinnedClass;

    public Line(InterfaceArea fromArea, XYWH_I fromRect, InterfaceArea.MethodArea toArea, XYWH_I toRect, Set<String> pinnedClass) {
        this.fromArea = fromArea;
        this.fromRect = fromRect;
        this.toArea = toArea;
        this.toRect = toRect;
        this.pinnedClass = pinnedClass;
    }

    void paint(ICanvas _g, int _x, int _y, int _w, int _h, InterfaceArea over, InterfaceArea.MethodArea overMethod) {
        if (overMethod == null && (over == toArea.interfaceArea || over == fromArea || pinnedClass.contains(fromArea.getName()) || pinnedClass.contains(toArea.interfaceArea.getName()))) {
            _g.setAlpha(1f, 0);
            _g.setColor(toArea.interfaceArea.color.brighter());
        } else if (overMethod != null && overMethod == toArea) {
            _g.setAlpha(1f, 0);
            _g.setColor(toArea.interfaceArea.color.brighter());
        } else {
            _g.setColor(AColor.gray);
            _g.setAlpha(0.25f, 0);
        }
        IPath path = VS.path();
        if (fromArea == toArea.interfaceArea) {
            fromToSelf(_h, path, _y, _g);
        } else if (fromArea.x > toArea.x) {
            fromToBehind(_h, path, _y, _g);
        } else {
            fromToAhead(path, _g);
        }
        VCallDepthStack.interfaceBar.paintFlavor(_g, fromRect.x + fromRect.w, fromRect.y, 4, fromRect.h, toArea.interfaceArea.color);
        _g.setAlpha(1f, 0);
    }

    private void fromToAhead(IPath path, ICanvas _g) {
        UPaint.moon(path, new XY_I(fromRect.x + fromRect.w, fromRect.y + (fromRect.h / 2)), new XY_I(toRect.x, toRect.y + (toRect.h / 2)), -6, 6);
        _g.fill(path);
        _g.setColor(AColor.darkGray);
        _g.draw(path);
    }

    private void fromToBehind(int _h, IPath path, int _y, ICanvas _g) {
        int pad = 20 + (int) (48 * (1f - ((float) fromRect.y / (float) _h)));
        path.moveTo(fromRect.x + fromRect.w, fromRect.y);
        path.curveTo(fromRect.x + fromRect.w, fromRect.y, fromRect.x + fromRect.w + pad, fromRect.y, fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2) + pad);
        path.lineTo(fromRect.x + fromRect.w + pad, _y + _h + pad);
        path.lineTo(fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2) + pad);
        path.curveTo(fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2) + pad, fromRect.x + fromRect.w + pad / 2, fromRect.y + (fromRect.h / 2), fromRect.x + fromRect.w, fromRect.y + fromRect.h);
        path.closePath();
        _g.fill(path);
        path = VS.path();
        path.moveTo(fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2) + pad);
        path.lineTo(fromRect.x + fromRect.w + pad, _y + _h + pad);
        path.curveTo(fromRect.x + fromRect.w + pad, _y + _h + pad, fromRect.x + fromRect.w + pad, _y + _h + pad + pad, fromRect.x + fromRect.w, _y + _h + pad + pad);
        path.lineTo(toRect.x, _y + _h + pad + pad);
        path.curveTo(toRect.x, _y + _h + pad + pad, toRect.x - pad, _y + _h + pad + pad, toRect.x - pad, _y + _h + pad);
        path.lineTo(toRect.x - pad, toRect.y + (toRect.h / 2) + pad);
        path.curveTo(toRect.x - pad, toRect.y + (toRect.h / 2) + pad, toRect.x - pad, toRect.y + (toRect.h / 2), toRect.x, toRect.y + (toRect.h / 2));
        UPaint.arrowHead(path, toRect.x, toRect.y + (toRect.h / 2), 270, 10, 45);
        _g.draw(path);
    }

    private void fromToSelf(int _h, IPath path, int _y, ICanvas _g) {
        int pad = 20 + (int) (48 * ((float) fromRect.y / (float) _h));
        path.moveTo(fromRect.x + fromRect.w, fromRect.y);
        path.curveTo(fromRect.x + fromRect.w, fromRect.y, fromRect.x + fromRect.w + pad / 2, fromRect.y + (fromRect.h / 2), fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2) - pad);
        path.lineTo(fromRect.x + fromRect.w + pad, _y - pad);
        path.lineTo(fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2) - pad);
        path.curveTo(fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2) - pad, fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2), fromRect.x + fromRect.w, fromRect.y + (fromRect.h));
        path.closePath();
        _g.fill(path);
        path = VS.path();
        path.moveTo(fromRect.x + fromRect.w + pad, fromRect.y + (fromRect.h / 2) - pad);
        path.lineTo(fromRect.x + fromRect.w + pad, _y - pad);
        path.curveTo(fromRect.x + fromRect.w + pad, _y - pad, fromRect.x + fromRect.w + pad, _y - (pad + pad), fromRect.x + fromRect.w, _y - (pad + pad));
        path.lineTo(toRect.x, _y - (pad + pad));
        path.curveTo(toRect.x - pad, _y - (pad + pad), toRect.x - pad, _y - (pad + pad), toRect.x - pad, _y - pad);
        path.lineTo(toRect.x - pad, toRect.y + (toRect.h / 2) - pad);
        path.curveTo(toRect.x - pad, toRect.y + (toRect.h / 2) - pad, toRect.x - pad, toRect.y + (toRect.h / 2), toRect.x, toRect.y + (toRect.h / 2));
        UPaint.arrowHead(path, toRect.x, toRect.y + (toRect.h / 2), 270, 10, 45);
        _g.draw(path);
    }

}
