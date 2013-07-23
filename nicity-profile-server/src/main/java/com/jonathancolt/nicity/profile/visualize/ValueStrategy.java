/*
 * $Revision$
 * $Date$
 *
 * Copyright (C) 1999-$year$ Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
 */

package com.jonathancolt.nicity.profile.visualize;

/**
 *
 */
interface ValueStrategy {

    long value(VCallDepthStack.InterfaceArea callArea);

    String name(long value);

}
