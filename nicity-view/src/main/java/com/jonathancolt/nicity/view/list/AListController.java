/*
 * AListController.java.java
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
import com.jonathancolt.nicity.core.lang.ASetObject;
import com.jonathancolt.nicity.core.lang.IOut;
import com.jonathancolt.nicity.core.observer.AObserver;
import com.jonathancolt.nicity.core.observer.Change;
import com.jonathancolt.nicity.core.observer.IObservable;
import com.jonathancolt.nicity.core.observer.IObserver;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IListController;

/**
 *
 * @author Administrator
 */
public abstract class AListController extends ASetObject implements IListController {

    /**
     *
     */
    protected AVList list;
    /**
     *
     */
    protected IBackcall backcall = NullBackcall.cNull;
    /**
     *
     */
    protected IObserver observer;
    /**
     *
     */
    protected long autoUpdateElapse = 600;

    /**
     *
     */
    public AListController() {
    }

    /**
     *
     * @param _backcall
     */
    public AListController(IBackcall _backcall) {
        this(_backcall, 600);
    }

    /**
     *
     * @param _backcall
     * @param _autoUpdateElapse
     */
    public AListController(IBackcall _backcall, long _autoUpdateElapse) {
        autoUpdateElapse = _autoUpdateElapse;
        if (_backcall == null) {
            _backcall = NullBackcall.cNull;
        }
        backcall = _backcall;
        if (backcall instanceof IObservable) {
            attachObserver((IObservable) backcall);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Object hashObject() {
        return this;
    }

    /**
     *
     * @param _backcall
     */
    protected void attachObserver(IObservable _backcall) {
        if (observer == null) {
            observer = new AObserver() {

                @Override
                public void bound(IObservable _observable) {
                    //listModified(null);
                }

                @Override
                public void change(Change _change) {
                    listModified(null);
                }

                @Override
                public void released(IObservable _observable) {
                    //listModified(null);
                }
            };
        }
        ((IObservable) backcall).bind(observer);
    }

    /**
     *
     * @param _list
     */
    @Override
    public void setVList(AVList _list) {
        list = _list;
    }

    /**
     *
     * @return
     */
    @Override
    public AVList getVList() {
        return list;
    }

    /**
     *
     * @return
     */
    public Object getValue() {
        return list;
    }

    /**
     *
     * @return
     */
    @Override
    public IBackcall getBackcall() {
        return backcall;
    }

    /**
     *
     * @param _backcall
     */
    @Override
    public void setBackcall(IBackcall _backcall) {
        if (_backcall == null) {
            backcall = NullBackcall.cNull;
        } else {
            backcall = _backcall;
            if (backcall instanceof IObservable) {
                attachObserver((IObservable) backcall);
            }
        }
        if (list == null) {
            return;
        }
        list.paint();
    }

    /**
     *
     * @param _list
     * @param _task
     * @return
     */
    public IEvent processEvent(AVList _list, IEvent _task) {
        return _task;
    }

    /**
     *
     * @param _
     */
    @Override
    public void listModified(IOut _) {
        if (list == null) {
            return;
        }
        list.paint();
    }

    /**
     *
     * @param _
     */
    @Override
    public void filterModified(IOut _) {
        if (list == null) {
            return;
        }
        list.paint();
    }

    /**
     *
     * @return
     */
    @Override
    public String getFilter() {
        return "";
    }
}
