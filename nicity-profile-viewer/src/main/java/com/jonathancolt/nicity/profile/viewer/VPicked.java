/*
 * Copyright 2013 jonathan.
 *
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
 */
package com.jonathancolt.nicity.profile.viewer;

/*
 * #%L
 * nicity-profile-viewer
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

import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.list.VItem;
import java.util.Set;

/**
 *
 * @author jonathan
 */
public class VPicked extends Viewer {

    public VPicked(final InterfaceArea callArea, final Set<String> hideClass, final Set<String> pinnedClass) {
        VChain c = new VChain(UV.cSWNW);
        if (pinnedClass.contains(callArea.getName())) {
            c.add(new VItem("un-pin") {
                @Override
                public void picked(IEvent _e) {
                    super.picked(_e); //To change body of generated methods, choose Tools | Templates.
                    pinnedClass.remove(callArea.getName());
                    getRootView().dispose();
                }
            });
        } else {
            c.add(new VItem("pin") {
                @Override
                public void picked(IEvent _e) {
                    super.picked(_e); //To change body of generated methods, choose Tools | Templates.
                    pinnedClass.add(callArea.getName());
                    getRootView().dispose();
                }
            });
        }
        c.add(new VItem("hide") {
            @Override
            public void picked(IEvent _e) {
                super.picked(_e); //To change body of generated methods, choose Tools | Templates.
                hideClass.add(callArea.getName());
                pinnedClass.remove(callArea.getName());
                getRootView().dispose();
            }
        });
        c.add(new VItem("showall") {
            @Override
            public void picked(IEvent _e) {
                super.picked(_e); //To change body of generated methods, choose Tools | Templates.
                hideClass.clear();
                getRootView().dispose();
            }
        });
        setContent(c);
    }
}
