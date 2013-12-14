/*
 * ULAF.java.java
 *
 * Created on 01-03-2010 01:30:54 PM
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

import com.jonathancolt.nicity.view.flavor.AFlavor;
import com.jonathancolt.nicity.view.flavor.ItemFlavor;
import com.jonathancolt.nicity.view.flavor.NullFlavor;
import com.jonathancolt.nicity.view.flavor.OutlineFlavor;
import com.jonathancolt.nicity.view.flavor.RoundFlavor;
import com.jonathancolt.nicity.view.flavor.ScrollFlavor;
import com.jonathancolt.nicity.view.flavor.TabFlavor;
import com.jonathancolt.nicity.view.flavor.WindowFlavor;
import com.jonathancolt.nicity.view.flavor.ZonesFlavor;

/**
 *
 * @author Administrator
 */
public class ULAF {

    /**
     *
     */
    public static AFlavor cDragging = new OutlineFlavor();
    /**
     *
     */
    public static AFlavor cDropping = new OutlineFlavor();
    /**
     *
     */
    public static AFlavor cItemFG = new NullFlavor();
    /**
     *
     */
    public static AFlavor cItemBG = new ItemFlavor();
    /**
     *
     */
    public static AFlavor cButtonFG = new NullFlavor();
    /**
     *
     */
    public static AFlavor cButtonBG = new ScrollFlavor();
    /**
     *
     */
    public static AFlavor cRoundButtonFG = new NullFlavor();
    /**
     *
     */
    public static AFlavor cRoundButtonBG = new RoundFlavor();
    /**
     *
     */
    public static AFlavor cScrollVertical = new ScrollFlavor();
    /**
     *
     */
    public static AFlavor cScrollHorizontal = new ScrollFlavor();
    /**
     *
     */
    public static AFlavor cWindow = new WindowFlavor();
    /**
     *
     */
    public static AFlavor cZones = new ZonesFlavor();
    /**
     *
     */
    public static AFlavor cTab = new TabFlavor();
}
