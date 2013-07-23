/*
 * $Revision$
 * $Date$
 *
 * Copyright (C) 1999-$year$ Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
 */
package com.jonathancolt.nicity.profile.visualize;

import com.jonathancolt.nicity.view.core.AColor;

/**
 *
 */
public class ClassColoring implements Coloring {

    public AColor value(VCallDepthStack.InterfaceArea callArea, long maxV) {
        return AColor.getHashSolid(callArea.callClass.getName());
    }
}