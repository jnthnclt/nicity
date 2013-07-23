/*
 * NullListController.java.java
 *
 * Created on 01-03-2010 01:32:11 PM
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

import com.jonathancolt.nicity.core.collection.IBackcall;
import com.jonathancolt.nicity.core.collection.NullBackcall;
import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.view.interfaces.IListController;
import com.jonathancolt.nicity.view.interfaces.IVItem;
import java.util.Comparator;

/**
 *
 * @author Administrator
 */
public class NullListController implements IListController {

    /**
     *
     */
    public static final NullListController cNull = new NullListController();

    /**
     *
     */
    public NullListController() {
    }

    /**
     *
     * @param _list
     */
    @Override
    public void setVList(AVList _list) {
    }

    /**
     *
     * @return
     */
    @Override
    public AVList getVList() {
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean hasMultiSelect() {
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public IVItem[] getItems() {
        return new IVItem[0];
    }

    /**
     *
     * @return
     */
    @Override
    public IVItem getSelectedItem() {
        return NullVItem.cNull;
    }

    /**
     *
     * @return
     */
    @Override
    public IVItem[] getSelectedItems() {
        return new IVItem[0];
    }

    /**
     *
     */
    @Override
    public void selectAllItems() {
    }

    /**
     *
     */
    @Override
    public void deselectAllItems() {
    }

    /**
     *
     * @param _toItem
     */
    @Override
    public void selectFromSelectedItemTo(IVItem _toItem) {
    }

    /**
     *
     * @param _toItem
     */
    @Override
    public void deselectFromSelectedItemTo(IVItem _toItem) {
    }

    /**
     *
     * @param _itemToSelect
     */
    @Override
    public void selectOneItem(IVItem _itemToSelect) {
    }

    /**
     *
     * @param _itemToSelect
     */
    @Override
    public void selectItem(IVItem _itemToSelect) {
    }

    /**
     *
     * @param _itemToDeselect
     */
    @Override
    public void deselectItem(IVItem _itemToDeselect) {
    }

    /**
     *
     * @param _itemToToggle
     */
    @Override
    public void toggleItem(IVItem _itemToToggle) {
    }

    /**
     *
     * @param _itemsToSelect
     */
    @Override
    public void selectItems(IVItem[] _itemsToSelect) {
    }

    /**
     *
     * @param _itemsToDeselect
     */
    @Override
    public void deselectItems(IVItem[] _itemsToDeselect) {
    }

    /**
     *
     * @param _itemsToToggle
     */
    @Override
    public void toggleItems(IVItem[] _itemsToToggle) {
    }

    /**
     *
     * @return
     */
    @Override
    public IBackcall getBackcall() {
        return NullBackcall.cNull;
    }

    /**
     *
     * @param _backcall
     */
    @Override
    public void setBackcall(IBackcall _backcall) {
    }

    /**
     *
     * @param _comparator
     */
    @Override
    public void setComparator(Comparator _comparator) {
    }

    /**
     *
     * @param _
     */
    @Override
    public void filterModified(IOut _) {
    }

    /**
     *
     * @param _
     */
    @Override
    public void listModified(IOut _) {
    }

    /**
     *
     * @return
     */
    @Override
    public String getFilter() {
        return "";
    }

    /**
     *
     * @param _rex
     */
    @Override
    public void setFilter(String _rex) {
    }
}
