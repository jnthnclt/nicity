package com.jonathancolt.nicity.view.rpp;

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

import com.jonathancolt.nicity.core.collection.CSet;
import com.jonathancolt.nicity.core.collection.keyed.KeyedValue;
import com.jonathancolt.nicity.view.border.ViewBorder;
import com.jonathancolt.nicity.view.interfaces.IView;

/**
 * 
 * @author jonathan
 */
public class RPPViews {

    /**
     * 
     */
    public RPPViews() {
    }
    private CSet<KeyedValue<String, RPPWindow>> views = new CSet<>();

    /**
     * 
     * @param _view
     * @param _key
     */
    public void register(IView _view, String _key) {
        _view.setBorder(new ViewBorder());
        RPPWindow v = new RPPWindow(_view);
        KeyedValue.add(views, _key, v);
    }

    /**
     * 
     * @param _key
     * @return
     */
    public RPPWindow view(long _who,String _key) {
        
        RPPWindow view = KeyedValue.get(views,_key);
        if (view == null) {
            register(new RPPHome("/home/jonathan/Dropbox/Code/nicity-goal/target/nicity-goal-1.0-SNAPSHOT.jar"),_key);
            view =  KeyedValue.get(views,_key);
        }
        return view;
    }

    /**
     * 
     * @param _key
     */
    public void release(String _key) {
        KeyedValue.remove(views, _key);
    }
}
