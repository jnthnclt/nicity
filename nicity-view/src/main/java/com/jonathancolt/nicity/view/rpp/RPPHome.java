/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import com.jonathancolt.nicity.core.lang.UClass;
import com.jonathancolt.nicity.view.border.BuldgeBorder;
import com.jonathancolt.nicity.view.border.MenuItemBorder;
import com.jonathancolt.nicity.view.border.ViewBorder;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.VPan;
import com.jonathancolt.nicity.view.core.VString;
import com.jonathancolt.nicity.view.core.ViewColor;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.list.VItem;
import com.jonathancolt.nicity.view.list.VList;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonathan
 */
public class RPPHome extends Viewer {

    public static void main(String[] args) {
        System.out.println(new File("./").getAbsolutePath());
        ViewColor.onGray();
        UV.exitFrame(new RPPHome("/home/jonathan/Dropbox/Code/nicity-goal/target/nicity-goal-1.0-SNAPSHOT.jar"), "");
    }
    String[] jars;
    Viewer menu = new Viewer();
    Viewer content = new Viewer();
    CArray<VItem> apps = new CArray<>();

    public RPPHome(String... jars) {
        this.jars = jars;
        init();
        
        menu.setView(menu());
        menu.spans(UV.cXEW);
        content.setView(new VPan(new VList(apps, 1), 400, 600));
        
        VChain c = new VChain(UV.cSWNW);
        c.add(menu);
        c.add(content);
        setContent(c);
        setBorder(new ViewBorder());
    }

    private void init() {
        apps.removeAll();
        for (String jar : jars) {
            for (Class c : UClass.getClasseNamesInPackage(IRPPViewable.class, null, jar)) {
                apps.insertLast(new VItem(new VString(c.getName()), c) {

                    @Override
                    public void picked(IEvent _e) {
                        try {
                            Class c = (Class) getValue();
                            Class[] argTypes = new Class[]{String[].class};
                            Method m = c.getDeclaredMethod("viewable", argTypes);
                            System.out.println(m);
                            String[] args = new String[0];
                            IView v = (IView) m.invoke(null, (Object) args);
                            content.setView(v);
                        } catch (                IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | IllegalAccessException ex) {
                            Logger.getLogger(RPPHome.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        }

    }

    IView menu() {
        VChain m = new VChain(UV.cEW);
        m.add(UV.border(new VItem("Apps") {

            @Override
            public void picked(IEvent _e) {
                init();
                content.setView(new VPan(new VList(apps, 1), 400, 600));
            }
        }, new MenuItemBorder()));
        m.setBorder(new BuldgeBorder(ViewColor.cThemeHighlight));
        m.spans(UV.cXEW);
        return m;
    }
}
