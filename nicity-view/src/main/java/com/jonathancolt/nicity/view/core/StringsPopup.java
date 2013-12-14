/*
 * StringsPopup.java.java
 *
 * Created on 01-03-2010 01:32:10 PM
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

import com.jonathancolt.nicity.core.collection.CArray;
import com.jonathancolt.nicity.core.comparator.AValueComparator;
import com.jonathancolt.nicity.core.comparator.UValueComparator;
import com.jonathancolt.nicity.view.border.PopupBorder;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IVItem;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.list.AItem;
import com.jonathancolt.nicity.view.list.ListController;
import com.jonathancolt.nicity.view.list.VItem;
import com.jonathancolt.nicity.view.list.VList;

/**
 *
 * @author Administrator
 */
public class StringsPopup extends AItem {
    
    private CArray strings = new CArray();
    
    /**
     *
     * @param _strings
     */
    public StringsPopup(Object[] _strings) {
        strings.insertLast(_strings);
        ListController listController = new ListController(strings) {
            @Override
            public IVItem vItem(Object _value) {
                VItem item = new VItem(new VString(_value), _value) {
                    @Override
                    public void picked(IEvent _e) {
                        setValue(getValue());
                    }
                };
                item.spans(UV.cXEW);
                return item;
            }
        };
        listController.setComparator(UValueComparator.toString(AValueComparator.cAscending));
        VList list = new VList(listController, 1);
        setContent(list);
        setBorder(new PopupBorder());
    }
    
    /**
     *
     * @param _view
     * @param _place
     */
    public void show(IView _view, Place _place) {
        //WindowPopup popup = new WindowPopup(_view,this);
        //popup.layoutInterior();
        //popup.setAutoFocusable(false);
        //popup.showPopup(_view,_place,this);
        UV.popup(_view, _place, this, true, true);
    }
    
    // IValue
    
    /**
     *
     * @param _value
     */
    public void setValue(Object _value) {
        ;
    }
   
    @Override
    public Object getValue() {
        return strings;
    }
}
