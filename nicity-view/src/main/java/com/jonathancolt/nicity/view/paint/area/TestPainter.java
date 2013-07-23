/*
 * TestPainter.java.java
 *
 * Created on 02-01-2010 09:30:16 PM
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

package com.jonathancolt.nicity.view.paint.area;

import com.jonathancolt.nicity.core.lang.URandom;
import com.jonathancolt.nicity.core.memory.struct.V_D;
import com.jonathancolt.nicity.core.memory.struct.XYWH_I;
import com.jonathancolt.nicity.core.memory.struct.XY_D;
import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.ViewColor;
import com.jonathancolt.nicity.view.flavor.OutlineFlavor;
import com.jonathancolt.nicity.view.flavor.RoundFlavor;
import com.jonathancolt.nicity.view.flavor.SoftFlavor;
import com.jonathancolt.nicity.view.flavor.StringFlavor;
import com.jonathancolt.nicity.view.flavor.TabFlavor;
import com.jonathancolt.nicity.view.flavor.ZonesFlavor;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.paint.lens.OffestLens;
import com.jonathancolt.nicity.view.paint.lens.ZoomLens;
import com.jonathancolt.nicity.view.rpp.IRPPViewable;

/**
 *
 * @author Administrator
 */
public class TestPainter implements IRPPViewable {
    
    public static IView viewable(String[] args) {
        ViewColor.onGray();
        return painter();
    }
    
    static Painter painter;
    /**
     *
     * @param _args
     */
    public static void main(String[] _args) {
        
        UV.exitFrame(viewable(_args), "");
    }
    
    public static Painter painter() {
        painter = new Painter(600,600);
        painter.lensStack.addLens(new OffestLens());
        ZoomLens lens = new ZoomLens();
        lens.zoom = 1;
        painter.lensStack.addLens(lens);
        painter.add(new TestArea(new V_D(0.25), new V_D(0.25), new V_D(0.25), new V_D(0.25)));

        ClickableArea clickable = new ClickableArea(new V_D(0.5), new V_D(0.5), new V_D(0.25), new V_D(0.25)) {

            @Override
            public void picked() {
                System.out.println("Picked :)");
            }

            @Override
            public void selected() {
                System.out.println("Selected :)");
            }
        };
        clickable.add(new RoundFlavor());
        clickable.add(clickable.whenOver(new OutlineFlavor()));
        clickable.add(clickable.whenPressed(new SoftFlavor()));
        clickable.add(new StringFlavor("Jonathan"));

        painter.add(clickable);

        final ItemsArea items = new ItemsArea(new V_D(0.5), new V_D(0), new V_D(0.25), new V_D(1d), 1, 1);
        //items.add(items.whenOver(new OutlineFlavor()));
        for(int i=0;i<16;i++) {
            items.add(newItem("Item:"+i));
        }
        painter.add(items);


        ItemArea item = new ItemArea(new V_D(0), new V_D(0.5), new V_D(0.25), new V_D(0.25)) {
            @Override
            public void picked() {
                System.out.println("Picked :)");
                painter.move(new AreaTransition(items, 200+URandom.rand(5000)));
            }

            @Override
            public void selected() {
                System.out.println("Selected :)");
                painter.move(new AreaTransition(this, 200+URandom.rand(5000)));
            }
        };
        item.add(new RoundFlavor());
        item.add(item.whenOver(new OutlineFlavor()));
        item.add(item.whenPressed(new SoftFlavor()));
        item.add(item.whenChoosen(new ZonesFlavor()));
        item.add(item.whenOverChoosen(new TabFlavor()));
        item.add(new StringFlavor("Colt"));

        painter.add(item);

        item = new ItemArea(new V_D(0.5), new V_D(0), new V_D(0.25), new V_D(0.25)) {
            @Override
            public void picked() {
                System.out.println("Picked :)");
            }

            @Override
            public void selected() {
                System.out.println("Selected :)");
                painter.move(new AreaTransition(this, 200+URandom.rand(5000)));
            }
        };
        item.add(new RoundFlavor());
        item.add(item.whenOver(new OutlineFlavor()));
        item.add(item.whenPressed(new SoftFlavor()));
        item.add(item.whenChoosen(new ZonesFlavor()));
        item.add(item.whenOverChoosen(new TabFlavor()));
        item.add(new StringFlavor("Anina"));

        painter.add(item);

        GroupAreas group = new GroupAreas(new V_D(0), new V_D(0), new V_D(1), new V_D(0.25));
        group.add(newItem("Group"));
        painter.add(group);
        return painter;
    }

    static ItemArea newItem(String _name) {
        ItemArea item = new ItemArea(new V_D(0.1), new V_D(0.1), new V_D(0.1), new V_D(0.9)) {
            @Override
            public void picked() {
                System.out.println("Picked :)");
            }

            @Override
            public void selected() {
                System.out.println("Selected :)");
            }
        };
        item.add(new RoundFlavor());
        item.add(item.whenOver(new OutlineFlavor()));
        item.add(item.whenPressed(new SoftFlavor()));
        item.add(item.whenChoosen(new ZonesFlavor()));
        item.add(item.whenOverChoosen(new TabFlavor()));
        item.add(new StringFlavor(_name));
        return item;
    }

    static class TestArea extends RectPaintedArea {
        String mode = "";
        AColor color = AColor.randomSolid(90, 120);
        TestArea(V_D _x,V_D _y,V_D _w,V_D _h) {
            super(_x,_y,_w,_h);
        }
        @Override
        public void paint(ICanvas _canvas,XYWH_I _xywh) {
            _canvas.setColor(color);
            _canvas.rect(true, _xywh.x, _xywh.y, _xywh.w,_xywh.h);
            _canvas.setColor(AColor.black);
            _canvas.drawString(mode, _xywh.x, _xywh.y+_xywh.h);
        }

        private XY_D op;
        @Override
        public void mouse(PaintableEvent _e) {
            mode = _e.mode;
            
            XY_D xp = _e.lensPoint();
            if (_e.mode == null ? UPaintableEvent.cPressed == null : _e.mode.equals(UPaintableEvent.cPressed)) {
                op = new XY_D(x.getV()-xp.x,y.getV()-xp.y);
            }
            if (_e.mode == null ? UPaintableEvent.cReleased == null : _e.mode.equals(UPaintableEvent.cReleased)) {
                op = null;
            }
            if (_e.mode == null ? UPaintableEvent.cDragged == null : _e.mode.equals(UPaintableEvent.cDragged)) {
                x.setV(xp.x+op.x);
                y.setV(xp.y+op.y);
            }
            if (_e.mode == null ? UPaintableEvent.cReleased == null : _e.mode.equals(UPaintableEvent.cReleased)) {
                if (_e.m.isDoubleClick()) {
                    painter.add(new TestArea(new V_D(0.25), new V_D(0.25), new V_D(0.25), new V_D(0.25)));
                }
            }
        }
        
    }
}
