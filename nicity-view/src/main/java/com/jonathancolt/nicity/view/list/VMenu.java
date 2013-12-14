/*
 * VMenu.java.java
 *
 * Created on 01-03-2010 01:32:32 PM
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
package com.jonathancolt.nicity.view.list;

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
import com.jonathancolt.nicity.core.memory.struct.XY_I;
import com.jonathancolt.nicity.view.adaptor.IKeyEventConstants;
import com.jonathancolt.nicity.view.border.ButtonBorder;
import com.jonathancolt.nicity.view.border.PopupBorder;
import com.jonathancolt.nicity.view.core.Placer;
import com.jonathancolt.nicity.view.core.UPopup;
import com.jonathancolt.nicity.view.core.VIcon;
import com.jonathancolt.nicity.view.core.VPaintable;
import com.jonathancolt.nicity.view.core.ViewString;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.core.WindowPopup;
import com.jonathancolt.nicity.view.event.KeyPressed;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IKeyEvents;
import com.jonathancolt.nicity.view.interfaces.IListController;
import com.jonathancolt.nicity.view.interfaces.IToolTip;
import com.jonathancolt.nicity.view.interfaces.IVItem;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.list.event.ItemPicked;
import com.jonathancolt.nicity.view.list.event.ItemSelected;

/**
 *
 * @author Administrator
 */
public class VMenu extends AItem implements IKeyEvents {

    /**
     *
     */
    protected IVItem selectedItem;
    /**
     *
     */
    protected IListController controller;
    /**
     *
     */
    protected WindowPopup popup;
    /**
     *
     */
    protected int cellCount = -1;
    /**
     *
     */
    protected IToolTip toolTip;
    /**
     *
     */
    protected String title;
    /**
     *
     */
    protected IView trigger;
    /**
     *
     */
    protected IView notify;

    /**
     *
     * @param _notify
     * @param _backcall
     */
    public VMenu(IView _notify, IBackcall _backcall) {
        this(_notify, new ListController(_backcall));
    }

    /**
     *
     * @param _notify
     * @param _controller
     */
    public VMenu(IView _notify, IListController _controller) {
        this(_notify, "", _controller, new VPaintable(VIcon.image("OpenCombo")));
    }

    /**
     *
     * @param _notify
     * @param _title
     * @param _backcall
     * @param _trigger
     */
    public VMenu(IView _notify, String _title, IBackcall _backcall, IView _trigger) {
        this(_notify, _title, new ListController(_backcall), _trigger);
    }

    /**
     *
     * @param _notify
     * @param _title
     * @param _controller
     * @param _trigger
     */
    public VMenu(IView _notify, String _title, IListController _controller, IView _trigger) {
        title = _title;
        controller = _controller;
        controller.setComparator(null);
        notify = _notify;
        trigger = _trigger;

        IVItem[] items = controller.getItems();
        if (items == null || items.length == 0) {
            selectedItem = new VItem(new ViewString("empty"), "empty");
        } else {
            selectedItem = items[0];
        }
        item(selectedItem);
        placer = new Placer(_trigger);
        setBorder(new ButtonBorder());
    }

    /**
     *
     * @return
     */
    public IListController getController() {
        return controller;
    }

    @Override
    public String toString() {
        if (title == null) {
            return super.toString();
        }
        return title.toString();
    }

    private void item(IVItem _selectedItem) {
        selectedItem = _selectedItem;
    }

    /**
     *
     * @param _backcall
     */
    public void setBackcall(IBackcall _backcall) {
        controller.setBackcall(_backcall);
        IVItem[] items = controller.getItems();
        IVItem selectItem = null;
        if (items == null || items.length == 0) {
            selectItem = new VItem(new ViewString("empty"), "empty");
        } else {
            selectItem = items[0];
        }
        selectItem(selectItem);
    }

    /**
     *
     * @param _filter
     */
    public void setFilter(String _filter) {
        controller.setFilter(_filter);
        controller.filterModified(null);
    }

    /**
     *
     * @param _cellCount
     */
    public void setCellCount(int _cellCount) {
        cellCount = _cellCount;
    }

    /**
     *
     * @param _toolTip
     */
    @Override
    public void setToolTip(IToolTip _toolTip) {
        toolTip = _toolTip;
    }

    /**
     *
     * @return
     */
    @Override
    public IToolTip getToolTip() {
        return toolTip;
    }

    /**
     *
     * @return
     */
    public Object getSelectedValue() {
        return selectedItem.getValue();
    }

    /**
     *
     * @return
     */
    public IVItem getSelectedItem() {
        return selectedItem;
    }

    /**
     *
     * @param _selectItem
     */
    public void setSelectedItem(IVItem _selectItem) {
        selectItem(_selectItem);
    }

    /**
     *
     * @return
     */
    @Override
    public Object getValue() {
        return getSelectedItem();
    }

    /**
     *
     * @param _item
     */
    protected void selectItem(IVItem _item) {
        if (_item == null) {
            return;
        }
        item(_item);
        layoutInterior();
        parent.get().layoutInterior();
        flush();
        if (popup != null) {
            popup.hidePopup();
            popup = null;
            grabFocus(0);//!! propagate who
        }
    }

    /**
     *
     * @param _task
     */
    @Override
    public void promoteEvent(IEvent _task) {
        if (_task instanceof ItemPicked) {
            ItemPicked ip = (ItemPicked) _task;
            Object source = ip.getSource();
            if (source == this) {
                showPopup(0);
            } else {
                selectItem((IVItem) source);
                ip.setSource(this);
                notify.promoteEvent(_task);
            }
        } else if (_task instanceof ItemSelected) {
            ItemSelected ip = (ItemSelected) _task;
            Object source = ip.getSource();
            if (source == this) {
                showPopup(0);
            } else {
                selectItem((IVItem) source);
                ip.setSource(this);
                notify.promoteEvent(_task);
            }
        } else {
            notify.promoteEvent(_task);
            super.promoteEvent(_task);
        }
    }

    /**
     *
     * @param _who
     */
    public void showPopup(long _who) {
        XY_I p = getLocationOnScreen();
        showPopup(_who, new XY_I(p.x, p.y + (int) getH()));
    }

    /**
     *
     * @param _who
     * @param p
     */
    public void showPopup(long _who, XY_I p) {
        if (popup == null) {
            controller.listModified(null);
            VGrid list = new VGrid(controller, cellCount);
            list.layoutInterior();

            Viewer scrollViewer = new Viewer(UPopup.scrollView(list, p));
            scrollViewer.setBorder(new PopupBorder());
            popup = new WindowPopup(this, scrollViewer);
            popup.setAutoFocusable(false);
            popup.showPopup(p, this);
            list.grabFocus(_who);

        } else {
            popup.hidePopup();
            popup = null;
            showPopup(_who);
        }
    }

    // IKeyEvents
    /**
     *
     * @param e
     */
    @Override
    public void keyPressed(KeyPressed e) {
        int code = e.getKeyCode();
        if (code == IKeyEventConstants.cEnter) {
            showPopup(e.who());
        }
        super.keyPressed(e);
    }
}
