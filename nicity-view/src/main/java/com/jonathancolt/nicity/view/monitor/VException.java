/*
 * VException.java.java
 *
 * Created on 01-03-2010 01:32:53 PM
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
package com.jonathancolt.nicity.view.monitor;

import com.jonathancolt.nicity.core.lang.UString;
import com.jonathancolt.nicity.core.lang.UTrace;
import com.jonathancolt.nicity.view.border.ItemBorder;
import com.jonathancolt.nicity.view.border.PopupBorder;
import com.jonathancolt.nicity.view.core.AWindow;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.VString;
import com.jonathancolt.nicity.view.core.ViewText;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.interfaces.IEmailer;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.list.AItem;
import com.jonathancolt.nicity.view.list.VItem;

/**
 *
 * @author Administrator
 */
public class VException extends AItem {

    /**
     *
     */
    public static IEmailer errorMailer;
    private Throwable throwable;

    /**
     *
     * @param _x
     */
    public VException(Throwable _x) {
        this(_x, (String[]) null);
    }

    /**
     *
     * @param _throwable
     * @param _why
     */
    public VException(final Throwable _throwable, final String _why) {
        this(_throwable, new String[]{_why});
    }

    /**
     *
     * @param _throwable
     * @param _why
     */
    public VException(final Throwable _throwable, final String[] _why) {

        throwable = _throwable;

        if (_why != null) {
            for (String s : _why) {
                System.out.println(s);
            }
        }
        throwable.printStackTrace();//??

        final String[] trace = new UTrace(_throwable).getCalls();
        Viewer viewer = new Viewer(new ViewText(trace));
        viewer.setBorder(new PopupBorder(10));

        VChain c = new VChain(UV.cSN);
        if (_why != null) {
            Viewer vw = new Viewer(new ViewText(_why));
            vw.setBorder(new ItemBorder(10));
            c.add(vw);
        }
        //c.add(new VString("jvmName:" + Run.jvmName));
        c.add(new VString(this));
        c.add(viewer);
        if (errorMailer != null) {
            c.add(new VItem("Report") {

                @Override
                public void picked(IEvent _e) {
                    new Thread() {

                        @Override
                        public void run() {
                            String message = UString.toString(trace, "\n");
                            message += "\n";
                            if (_why != null) {
                                message += UString.toString(_why, "\n");
                            }
                            String[] tos = new String[]{errorMailer.emailersAddress()};
                            errorMailer.sendMail(null, "adk.VException", tos, null, null, _throwable.toString(), message);
                        }
                    }.start();
                }
            });
        }
        setContent(c);
    }

    /**
     *
     * @return
     */
    @Override
    public Object getValue() {
        return throwable;
    }

    @Override
    public String toString() {
        return throwable.toString();
    }
    AWindow window;

    /**
     *
     * @param _centerRelativeTo
     */
    public void toFront(IView _centerRelativeTo) {
        VExceptions.e(this);
    }
}
