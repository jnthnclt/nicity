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

import com.jonathancolt.nicity.core.collection.CArray;
import com.jonathancolt.nicity.core.lang.ASetObject;
import com.jonathancolt.nicity.core.lang.ICallback;
import com.jonathancolt.nicity.core.lang.UBase64;
import com.jonathancolt.nicity.core.memory.struct.XYWH_I;
import com.jonathancolt.nicity.view.canvas.FilerCanvas;
import com.jonathancolt.nicity.view.core.ADisplay;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.interfaces.IView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author jonathan
 */
public class RPPDisplay extends ADisplay {

    /**
     *
     * @param _view
     */
    public RPPDisplay(IView _view) {
        super(_view);
        System.out.println("RPP Display for " + _view);
    }

    @Override
    public ICanvas display(long _who, float _w, float _h) {
        ICanvas c = new FilerCanvas(_who, new RPPFiler(), new ICallback() {

            @Override
            public Object callback(Object _value) {
                //System.out.println(_value);
                return _value;
            }
        });
        return c;
    }
    //private long lastMillis = Long.MIN_VALUE;

    @Override
    public void displayable(ICanvas _g, XYWH_I _region) {
        _g.dispose();

        // keep from painting more that 30fps
//        long millis = System.currentTimeMillis();
//        if (lastMillis != Long.MIN_VALUE) {
//            long d = millis - lastMillis;
//            if ((d * 2) < 1000 / 30) { // * 2 assuming next repaint will take similair amount of time
//                long sleep = (1000 / 30) - (d * 2);
//                try {
//                    Thread.sleep(sleep);
//                } catch (Exception x) {
//                }
//            }
//        }
//        lastMillis = millis;


        FilerCanvas c = (FilerCanvas) _g;
        try {
            RPPFiler f = (RPPFiler) c.filer();
            byte[] data = f.getBytes();
            RPP s = new RPP();
            s.region = _region;
            s.version = System.identityHashCode(data);
            s.rppBase64 = new String(UBase64.encode(data));
            Collection<PaintUpdates> _updateable = updateable;// get stack copy
            for (PaintUpdates u : _updateable) {
                u.add(s);
            }
            for (PaintUpdates u : _updateable) {
                u.updated();
            }

        } catch (Exception x) {
        }
    }
    final Map<Long, PaintUpdates> updatesFor = new ConcurrentHashMap<>();
    Collection<PaintUpdates> updateable = new ArrayList<>();

    /**
     *
     * @param _id
     * @return
     */
    public PaintUpdates updateFor(long _id) {
        boolean joined = false;
        PaintUpdates u = updatesFor.get(_id);
        if (u == null) {
            u = new PaintUpdates(_id);
            updatesFor.put(_id, u);
            updateable = updatesFor.values();
            joined = true;
        }
        if (joined) {
            displaying().paint();// repaint when someone new joins
        }
        return u;

    }

    /**
     *
     */
    public class PaintUpdates extends ASetObject {

        long who;
        CArray<RPP> update = new CArray<>(RPP.class);
        final Object lock = new Object();
        int waiting = 0;

        PaintUpdates(long _who) {
            who = _who;
        }

        @Override
        public Object hashObject() {
            return who;
        }

        /**
         *
         */
        public void updated() {
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        /**
         *
         * @param _update
         */
        public void add(RPP _update) {
            synchronized (lock) {
                update.insertLast(_update);
            }
        }

        /**
         *
         * @param _wait
         * @return
         */
        public RPP[] remove(boolean _wait) {
            synchronized (lock) {
                if (update.getCount() > 0) {
                    return (RPP[]) update.removeAll();
                } else {
                    if (!_wait) {
                        return new RPP[0];
                    }
                    try {
                        lock.wait(3000); //!!
                    } catch (InterruptedException ex) {
                    }
                    return (RPP[]) update.removeAll();
                }
            }
        }
    }
}
