/*
 * AVEditBoolean.java.java
 *
 * Created on 03-12-2010 06:40:37 PM
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

import com.jonathancolt.nicity.core.observer.AObserver;
import com.jonathancolt.nicity.core.observer.Change;
import com.jonathancolt.nicity.core.observer.IObservable;
import com.jonathancolt.nicity.core.observer.IObserver;
import com.jonathancolt.nicity.core.value.Value;
import com.jonathancolt.nicity.view.core.Placer;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.list.AItem;

/**
 *
 * @author Administrator
 */
abstract public class AVEditBoolean extends AItem {

    /**
     *
     * @return
     */
    abstract public IView trueView();
    /**
     *
     * @return
     */
    abstract public IView falseView();

    Value<Boolean> value;
    IObserver observer;

    /**
     *
     * @param _value
     */
    public AVEditBoolean(Value<Boolean> _value) {
        value = _value;
        observer = new AObserver() {

            @Override
            public void change(Change _change) {
                refresh();
            }

            @Override
            public void bound(IObservable _observable) {
                refresh();
            }

            @Override
            public void released(IObservable _observable) {
            }
        };
        value.bind(observer);
    }

    /**
     *
     * @param _e
     */
    @Override
    public void picked(IEvent _e) {
        value.setValue(!isTrue());
    }

    /**
     *
     * @return
     */
    public boolean isTrue() {
        return value.booleanValue();
    }

    /**
     *
     * @return
     */
    public boolean isFalse() {
        return !value.booleanValue();
    }

    /**
     *
     */
    public void refresh() {
        if (value.booleanValue()) {
            placer = new Placer(trueView());
            selectBorder();
        } else {
            placer = new Placer(falseView());
            deselectBorder();
        }
        paint();
    }
}
