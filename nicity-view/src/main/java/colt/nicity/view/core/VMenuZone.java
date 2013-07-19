/*
 * VMenuZone.java.java
 *
 * Created on 01-03-2010 01:32:09 PM
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
package colt.nicity.view.core;

import colt.nicity.view.border.ZoneBorder;
import colt.nicity.view.interfaces.IView;
import colt.nicity.view.list.AItem;

/**
 *
 * @author Administrator
 */
public class VMenuZone extends AItem {

    /**
     *
     * @param _menu
     * @param _content
     */
    public VMenuZone(String _menu, IView _content) {
        Viewer v = new Viewer(_content);
        setContent(v);
        setBorder(new ZoneBorder(_menu, ViewColor.cTheme, 6));
    }
}
