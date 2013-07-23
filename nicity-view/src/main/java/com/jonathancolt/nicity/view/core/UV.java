/*
 * UV.java.java
 *
 * Created on 01-03-2010 01:34:44 PM
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

import com.jonathancolt.nicity.core.collection.CArray;
import com.jonathancolt.nicity.core.io.ICloseable;
import com.jonathancolt.nicity.core.lang.BitMasks;
import com.jonathancolt.nicity.core.lang.UMath;
import com.jonathancolt.nicity.core.memory.struct.XYWH_I;
import com.jonathancolt.nicity.core.memory.struct.XY_I;
import com.jonathancolt.nicity.view.adaptor.IFontConstants;
import com.jonathancolt.nicity.view.adaptor.VS;
import com.jonathancolt.nicity.view.border.BuldgeBorder;
import com.jonathancolt.nicity.view.border.LineBorder;
import com.jonathancolt.nicity.view.border.PadBorder;
import com.jonathancolt.nicity.view.border.SolidBorder;
import com.jonathancolt.nicity.view.border.WindowBorder;
import com.jonathancolt.nicity.view.border.ZonesBorder;
import com.jonathancolt.nicity.view.canvas.GlueAWTGraphicsToCanvas;
import com.jonathancolt.nicity.view.event.AMouseEvent;
import com.jonathancolt.nicity.view.event.AViewEvent;
import com.jonathancolt.nicity.view.event.FocusLost;
import com.jonathancolt.nicity.view.event.WindowClosed;
import com.jonathancolt.nicity.view.flavor.BevelFlavor;
import com.jonathancolt.nicity.view.flavor.BoxFlavor;
import com.jonathancolt.nicity.view.flavor.ButtonFlavor;
import com.jonathancolt.nicity.view.flavor.ItemFlavor;
import com.jonathancolt.nicity.view.flavor.NullFlavor;
import com.jonathancolt.nicity.view.flavor.OutlineFlavor;
import com.jonathancolt.nicity.view.flavor.RoundFlavor;
import com.jonathancolt.nicity.view.flavor.ScrollFlavor;
import com.jonathancolt.nicity.view.flavor.SliderBarFlavor;
import com.jonathancolt.nicity.view.flavor.SoftFlavor;
import com.jonathancolt.nicity.view.flavor.SolidFlavor;
import com.jonathancolt.nicity.view.flavor.TabFlavor;
import com.jonathancolt.nicity.view.flavor.VerticalBuldgeFlavor;
import com.jonathancolt.nicity.view.flavor.WindowFlavor;
import com.jonathancolt.nicity.view.flavor.ZonesFlavor;
import com.jonathancolt.nicity.view.image.IImage;
import com.jonathancolt.nicity.view.interfaces.IBorder;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IFramer;
import com.jonathancolt.nicity.view.interfaces.IPaintable;
import com.jonathancolt.nicity.view.interfaces.IPlacer;
import com.jonathancolt.nicity.view.interfaces.IPreview;
import com.jonathancolt.nicity.view.interfaces.IRootView;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.list.VItem;
import com.jonathancolt.nicity.view.monitor.VException;
import com.jonathancolt.nicity.view.value.VAlwaysOver;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Administrator
 */
public class UV {
    
    static {
        
        // this is a lame short cut to init AFlavor
        new BevelFlavor();
        new BoxFlavor();
        new ButtonFlavor();
        new ItemFlavor();
        new NullFlavor();
        new OutlineFlavor();
        new RoundFlavor();
        new ScrollFlavor();
        new SliderBarFlavor();
        new SoftFlavor();
        new SolidFlavor();
        new TabFlavor();
        new VerticalBuldgeFlavor();
        new WindowFlavor();
        new ZonesFlavor();
    }

    /**
     * 
     */
    public static boolean paintListCounts = false;
    /**
     * 
     */
    public static boolean paintWhos = false;
    
    /**
     *
     */
    public static final float cS = -1.0f;// S = shrink = -1;
    /**
     *
     */
    public static final float cI = 0.0f;// I = ignore = 0;
    /**
     *
     */
    public static final float cG = 1.0f;// G = grow = 1;
    
    /**
     *
     */
    public static final Flex cFSS = new Flex(NullView.cNull, cS, cS);
    /**
     *
     */
    public static final Flex cFSI = new Flex(NullView.cNull, cS, cI);
    /**
     *
     */
    public static final Flex cFSG = new Flex(NullView.cNull, cS, cG);
    
    /**
     *
     */
    public static final Flex cFIS = new Flex(NullView.cNull, cI, cS);
    /**
     *
     */
    public static final Flex cFII = new Flex(NullView.cNull, cI, cI);
    /**
     *
     */
    public static final Flex cFIG = new Flex(NullView.cNull, cI, cG);
    
    /**
     *
     */
    public static final Flex cFGS = new Flex(NullView.cNull, cG, cS);
    /**
     *
     */
    public static final Flex cFGI = new Flex(NullView.cNull, cG, cI);
    /**
     *
     */
    public static final Flex cFGG = new Flex(NullView.cNull, cG, cG);

    
    /**
     *
     */
    public static AFont[] fonts = new AFont[]{
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 8),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 10),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 18),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 24),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 32),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 18),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 18),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cItalic | IFontConstants.cBold, 14),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cBold, 15),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 15),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cItalic, 14),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cItalic, 24),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 15),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 15)
    };
    /**
     *
     */
    public static int cMicro = 0;
    /**
     *
     */
    public static int cSmall = 1;
    /**
     *
     */
    public static int cMedium = 2;
    /**
     *
     */
    public static int cLarge = 3;
    /**
     *
     */
    public static int cJumbo = 4;
    /**
     *
     */
    public static int cMessage = 5;
    /**
     *
     */
    public static int cTitle = 6;
    /**
     *
     */
    public static int cMenu = 7;
    /**
     *
     */
    public static int cBold = 8;
    /**
     *
     */
    public static int cText = 9;
    /**
     *
     */
    public static int cItalic = 10;
    /**
     *
     */
    public static int cTab = 11;
    /**
     *
     */
    public static int cButton = 12;
    /**
     *
     */
    public static int cToolTip = 13;
    /**
     *
     */
    public static String[] cFontUsageNames = new String[]{
        "Micro",
        "Small",
        "Large",
        "Jumbo",
        "Message",
        "Title",
        "Menu",
        "Bold",
        "Text",
        "Italic",
        "Tab",
        "Button",
        "ToolTip"
    };
    /**
     *
     */
    public static AFont[] cTexts = new AFont[]{
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 10),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 11),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 12),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 13),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 14),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 15),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 16),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 17),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 18),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 19),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 20),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 21),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 22),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 23),
        new AFont(IFontConstants.cDefaultFontName, IFontConstants.cPlain, 24),
    };
    /**
     *
     */
    public static final Place cScrollRefresh = new StaticPlace(1.0f, 0.0f, 1.0f, 0.0f, -16, 2);
    /**
     *
     */
    public static final Place cScrollAdd = new StaticPlace(0.0f, 0.0f, 0.0f, 0.0f, 2, 2);
    
    /**
     *
     */
    public static final Place cOrigin = new StaticPlace(0.0f, 0.0f, 0.0f, 0.0f, 0, 0);
    
    
    /**
     *
     */
    public static final Place cNWC = new StaticPlace(0.0f, 0.0f, 0.5f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cNWN = new StaticPlace(0.0f, 0.0f, 0.5f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cNWNE = new StaticPlace(0.0f, 0.0f, 1.0f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cNWE = new StaticPlace(0.0f, 0.0f, 1.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cNWSE = new StaticPlace(0.0f, 0.0f, 1.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cNWS = new StaticPlace(0.0f, 0.0f, 0.5f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cNWSW = new StaticPlace(0.0f, 0.0f, 0.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cNWW = new StaticPlace(0.0f, 0.0f, 0.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cNWNW = new StaticPlace(0.0f, 0.0f, 0.0f, 0.0f, 0, 0);
    
    /**
     *
     */
    public static final Place cNC = new StaticPlace(0.5f, 0.0f, 0.5f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cNN = new StaticPlace(0.5f, 0.0f, 0.5f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cNNE = new StaticPlace(0.5f, 0.0f, 1.0f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cNE = new StaticPlace(0.5f, 0.0f, 1.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cNSE = new StaticPlace(0.5f, 0.0f, 1.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cNS = new StaticPlace(0.5f, 0.0f, 0.5f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cNSW = new StaticPlace(0.5f, 0.0f, 0.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cNW = new StaticPlace(0.5f, 0.0f, 0.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cNNW = new StaticPlace(0.5f, 0.0f, 0.0f, 0.0f, 0, 0);
    
    /**
     *
     */
    public static final Place cNEC = new StaticPlace(1.0f, 0.0f, 0.5f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cNEN = new StaticPlace(1.0f, 0.0f, 0.5f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cNENE = new StaticPlace(1.0f, 0.0f, 1.0f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cNEE = new StaticPlace(1.0f, 0.0f, 1.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cNESE = new StaticPlace(1.0f, 0.0f, 1.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cNES = new StaticPlace(1.0f, 0.0f, 0.5f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cNESW = new StaticPlace(1.0f, 0.0f, 0.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cNEW = new StaticPlace(1.0f, 0.0f, 0.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cNENW = new StaticPlace(1.0f, 0.0f, 0.0f, 0.0f, 0, 0);
    
    
    /**
     *
     */
    public static final Place cWC = new StaticPlace(0.0f, 0.5f, 0.5f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cWN = new StaticPlace(0.0f, 0.5f, 0.5f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cWNE = new StaticPlace(0.0f, 0.5f, 1.0f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cWE = new StaticPlace(0.0f, 0.5f, 1.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cWSE = new StaticPlace(0.0f, 0.5f, 1.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cWS = new StaticPlace(0.0f, 0.5f, 0.5f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cWSW = new StaticPlace(0.0f, 0.5f, 0.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cWW = new StaticPlace(0.0f, 0.5f, 0.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cWNW = new StaticPlace(0.0f, 0.5f, 0.0f, 0.0f, 0, 0);
    
    
    /**
     *
     */
    public static final Place cCC = new StaticPlace(0.5f, 0.5f, 0.5f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cCN = new StaticPlace(0.5f, 0.5f, 0.5f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cCNE = new StaticPlace(0.5f, 0.5f, 1.0f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cCE = new StaticPlace(0.5f, 0.5f, 1.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cCSE = new StaticPlace(0.5f, 0.5f, 1.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cCS = new StaticPlace(0.5f, 0.5f, 0.5f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cCSW = new StaticPlace(0.5f, 0.5f, 0.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cCW = new StaticPlace(0.5f, 0.5f, 0.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cCNW = new StaticPlace(0.5f, 0.5f, 0.0f, 0.0f, 0, 0);
    
    
    /**
     *
     */
    public static final Place cEC = new StaticPlace(1.0f, 0.5f, 0.5f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cEN = new StaticPlace(1.0f, 0.5f, 0.5f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cENE = new StaticPlace(1.0f, 0.5f, 1.0f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cEE = new StaticPlace(1.0f, 0.5f, 1.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cESE = new StaticPlace(1.0f, 0.5f, 1.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cES = new StaticPlace(1.0f, 0.5f, 0.5f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cESW = new StaticPlace(1.0f, 0.5f, 0.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cEW = new StaticPlace(1.0f, 0.5f, 0.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cENW = new StaticPlace(1.0f, 0.5f, 0.0f, 0.0f, 0, 0);
    
    
    /**
     *
     */
    public static final Place cSWC = new StaticPlace(0.0f, 1.0f, 0.5f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cSWN = new StaticPlace(0.0f, 1.0f, 0.5f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cSWNE = new StaticPlace(0.0f, 1.0f, 1.0f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cSWE = new StaticPlace(0.0f, 1.0f, 1.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cSWSE = new StaticPlace(0.0f, 1.0f, 1.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cSWS = new StaticPlace(0.0f, 1.0f, 0.5f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cSWSW = new StaticPlace(0.0f, 1.0f, 0.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cSWW = new StaticPlace(0.0f, 1.0f, 0.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cSWNW = new StaticPlace(0.0f, 1.0f, 0.0f, 0.0f, 0, 0);
    
    /**
     *
     */
    public static final Place cSC = new StaticPlace(0.5f, 1.0f, 0.5f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cSN = new StaticPlace(0.5f, 1.0f, 0.5f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cSNE = new StaticPlace(0.5f, 1.0f, 1.0f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cSE = new StaticPlace(0.5f, 1.0f, 1.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cSSE = new StaticPlace(0.5f, 1.0f, 1.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cSS = new StaticPlace(0.5f, 1.0f, 0.5f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cSSW = new StaticPlace(0.5f, 1.0f, 0.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cSW = new StaticPlace(0.5f, 1.0f, 0.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cSNW = new StaticPlace(0.5f, 1.0f, 0.0f, 0.0f, 0, 0);
    
    /**
     *
     */
    public static final Place cSEC = new StaticPlace(1.0f, 1.0f, 0.5f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cSEN = new StaticPlace(1.0f, 1.0f, 0.5f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cSENE = new StaticPlace(1.0f, 1.0f, 1.0f, 0.0f, 0, 0);
    /**
     *
     */
    public static final Place cSEE = new StaticPlace(1.0f, 1.0f, 1.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cSESE = new StaticPlace(1.0f, 1.0f, 1.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cSES = new StaticPlace(1.0f, 1.0f, 0.5f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cSESW = new StaticPlace(1.0f, 1.0f, 0.0f, 1.0f, 0, 0);
    /**
     *
     */
    public static final Place cSEW = new StaticPlace(1.0f, 1.0f, 0.0f, 0.5f, 0, 0);
    /**
     *
     */
    public static final Place cSENW = new StaticPlace(1.0f, 1.0f, 0.0f, 0.0f, 0, 0);
    
    // flag Masks
    /**
     *
     */
    public final static int cInterior = BitMasks.c1;
    /**
     *
     */
    public final static int cFlexing = BitMasks.c2;
    /**
     *
     */
    public final static int cActive = BitMasks.c3;
    /**
     *
     */
    public final static int cMend = BitMasks.c4;
    /**
     *
     */
    public final static int cRepair = BitMasks.c5;

    /**
     *
     */
    public final static int cAllInterior = BitMasks.c8;
    /**
     *
     */
    public final static int cReverseX = BitMasks.c9;
    /**
     *
     */
    public final static int cReverseY = BitMasks.c10;
    
    /**
     *
     */
    public final static int cRepairBG = BitMasks.c11;
    /**
     *
     */
    public final static int cRepairMG = BitMasks.c12;
    /**
     *
     */
    public final static int cRepairFG = BitMasks.c13;
    
    /**
     *
     */
    public final static int cHasSelected = BitMasks.c16;
    /**
     *
     */
    public final static int cAllSelected = BitMasks.c17;
    /**
     *
     */
    public final static int cMultiSelectMode = BitMasks.c18;
    
    /**
     *
     */
    public final static int cDisabledEvents = BitMasks.c25;
    /**
     *
     */
    public final static int cDisabled = BitMasks.c26;
    /**
     *
     */
    public final static int cFocus = BitMasks.c27;
    
    /**
     *
     */
    public final static int cExpandN = BitMasks.c28;
    /**
     *
     */
    public final static int cExpandS = BitMasks.c29;
    /**
     *
     */
    public final static int cExpandE = BitMasks.c30;
    /**
     *
     */
    public final static int cExpandW = BitMasks.c31;
    
    /**
     *
     */
    public final static int cXN = cExpandN;
    
    /**
     *
     */
    public final static int cXNS = cExpandN | cExpandS;
    /**
     *
     */
    public final static int cXNE = cExpandN | cExpandE;
    /**
     *
     */
    public final static int cXNW = cExpandN | cExpandW;
    /**
     *
     */
    public final static int cXNEW = cExpandN | cExpandE | cExpandW;
    
    /**
     *
     */
    public final static int cXNSE = cExpandN | cExpandS | cExpandE;
    /**
     *
     */
    public final static int cXNSW = cExpandN | cExpandS | cExpandW;
    
    /**
     *
     */
    public final static int cXNSEW = cExpandN | cExpandS | cExpandE | cExpandW;
    
    /**
     *
     */
    public final static int cXS = cExpandS;
    /**
     *
     */
    public final static int cXSE = cExpandS | cExpandE;
    /**
     *
     */
    public final static int cXSW = cExpandS | cExpandW;
    /**
     *
     */
    public final static int cXSEW = cExpandS | cExpandE | cExpandW;
    
    /**
     *
     */
    public final static int cXE = cExpandE;
    /**
     *
     */
    public final static int cXW = cExpandW;
    /**
     *
     */
    public final static int cXEW = cExpandE | cExpandW;


    
    /**
     *
     * @param _content
     * @param _popup
     * @return
     */
    public static VChain refreshPopup(IView _content, IView _popup) {
        VPopupButton popup = new VPopupButton(VIcon.icon("open17x17"), _popup, true, true);
        return new VAlwaysOver(popup, _content, UV.cScrollRefresh);
    }

    
    /**
     *
     * @param _name
     * @param _content
     * @param _popup
     * @return
     */
    public static IView zonePopup(String _name, IView _content, IView _popup) {
        return new VPopupButton(UV.zone(_name, _content), UV.cNENE, _popup, true, true);
    }

    
    /**
     *
     * @param _view
     * @param _place
     * @return
     */
    public static <T extends IView> T spans(T _view, Place _place) {
        if (_view == null) {
            return _view;
        }
        if (_place == cEW || _place == cNENW || _place == cSESW) {
            _view.spans(UV.cXNS);
        } else if (_place == cSWNW || _place == cSN || _place == cSENE) {
            _view.spans(UV.cXEW);
        }
        return _view;
    }
    
    /**
     *
     * @param _view
     * @return
     */
    public static IView pad(IView _view) {
        Viewer area = new Viewer(_view);
        area.setBorder(new PadBorder(null,6,6));
        return area;
    }
    
    /**
     *
     * @param _name
     * @param _view
     * @return
     */
    public static IView zone(Object _name, IView _view) {
        return zone(_name, _view, ViewColor.cTheme);
    }
    

    /**
     *
     * @param _name
     * @param _view
     * @param _color
     * @return
     */
    public static IView zone(final Object _name, IView _view, AColor _color) {
        Viewer v = new Viewer(_view);
        VChain c = new VChain(UV.cSWNW);
        c.add(UV.border(new VString(_name,UV.fonts[UV.cSmall]),new SolidBorder(ViewColor.cTheme,2)));
        c.add(UV.border(v,new BuldgeBorder(ViewColor.cTheme,2)));
        return c;
    }
    
    /**
     *
     * @param _view
     * @return
     */
    public static IView zone(IView _view) {
        return zone(_view,2,2);
    }

    /**
     *
     * @param _view
     * @param _innerPadding
     * @param _outerPadding
     * @return
     */
    public static IView zone(IView _view,int _innerPadding,int _outerPadding) {
        Viewer area = new Viewer(new Viewer(_view));
        area.setBorder(new LineBorder(ViewColor.cItemTheme,AColor.gray,_innerPadding,6,_outerPadding));
        return area;
    }

    /**
     *
     * @param errors
     */
    public static void error(Throwable... errors) {
         VChain c = new VChain(UV.cSWNW);
         for(int i=0;i<errors.length;i++) {
             c.add(new VException(errors[i]));
         }
         UV.frame(UV.zone(c),"Error");
     }


    /**
     *
     * @param _message
     */
    public static void message(String... _message) {
         VChain c = new VChain(UV.cSWNW);
         for(int i=0;i<_message.length;i++) {
             c.add(new VString(_message[i],UV.fonts[UV.cTitle]));
         }

         VChain o = new VChain(UV.cSN);
         o.add(c);
         o.add(new Viewer(new VButton(new VString(" Ok")) {
             @Override
             public void picked(IEvent _e) {
                 getRootView().dispose();
             }
         }));

         VItem v = new VItem(UV.zone(o)) {
            @Override
            public void focusLost(FocusLost e) {
                super.focusLost(e);
                getRootView().dispose();
            }
         };
         v.setBorder(new WindowBorder(ViewColor.cTheme, 2));
        AWindow frame = new AWindow(v);
        frame.setTitle(_message[0]);
        centerWindow(frame);
        frame.show();
        v.grabFocus(0);//!! progagate who
     }
    
     /**
      *
      * @param _view
      * @return
      */
     public static IView zones(IView _view) {
        Viewer v = new Viewer(_view);
        Viewer areas = new Viewer(v);
        areas.setBorder(new ZonesBorder(ViewColor.cTheme, 2));
        return areas;
    }
    
    /**
     *
     * @param _view
     * @param _border
     * @return
     */
    public static <T extends IView> T border(T _view, IBorder _border) {
        if (_view  != null) {
            _view.setBorder(_border);
        }
        return _view;
    }
    
    /**
     *
     * @param _container
     * @param _edit
     * @param _back
     * @param _name
     * @param _return
     */
    static public void nested(final IView _container, final IView _edit,String _back,Object _name,VButton[] _return) {
        if (_return == null) {
            _return = new VButton[1];
        }
        if (_edit == null) {
            return;
        }
        if (_name == null) {
            _name = "......";
        }
        final IView backView = _container.getContent();

        
        VChain m = new VChain(cEW);
        VButton back = new VButton(new VChain(UV.cEW,VIcon.icon("left"),new VString(_back,UV.fonts[UV.cSmall]))) {
            @Override
            public IView spans(int _spans) { return  this; }
            @Override
            public void picked(IEvent _e) {
                if (_edit instanceof ICloseable) {
                    try {
                        ((ICloseable)_edit).close();
                    }
                    catch(Exception x) { x.printStackTrace(); }
                }
                _container.setView(backView);
                _container.getRootView().paint();
            }
        };
        _return[0] = back;
        m.add(back);
        m.add(new RigidBox(16,1));
        if (_name instanceof IView) {
            m.add((IView)_name);
        }
        else {
            m.add(new VString(_name, ViewColor.cThemeFont));
        }
        m.setBorder(new LineBorder());

        final VChain c = new VChain(cSWNW);
        c.add(m);
        c.add(_edit);
        _container.setView(c);
        _container.getRootView().paint();
    }
    
    
    /**
     *
     * @param _container
     * @param _edit
     * @param _name
     */
    static public void back(final IView _container, final IView _edit, String _name) {
        if (_edit == null) {
            return;
        }
        if (_name == null) {
            _name = "Back...";
        }
        if (_container == null) {
            UV.frame(_edit, _edit.toString());
        } else {
            final IView backView = _container.getContent();

            final VChain c = new VChain(cSWNW);

            VChain m = new VChain(cEW);
            m.spans(UV.cXEW);
            VItem dimiss = new VItem(" Dismiss ") {
                @Override
                public void picked(IEvent _e) {
                    _container.setView(_edit);
                    _container.getRootView().paint();

                    AViewEvent awe = WindowClosed.newInstance(0,null);
                    IView v = awe.disbatchEvent(backView, backView);
                    v.processEvent(awe);
                }
            };
            //dimiss.setToolTip("Dismiss this option to go Back");
            dimiss.spans(UV.cXNS);
            m.add(dimiss);
            VItem frame = new VItem(" Open In Seperate Window ") {

                @Override
                public void picked(IEvent _e) {
                    UV.frame(_edit, _edit.toString());
                    _container.setView(backView);
                    _container.getRootView().paint();
                }
            };
            //frame.setToolTip("Open in new Window");
            frame.spans(UV.cXNS);
            m.add(frame);
            VItem peek = new VItem(" Peek @ Preview View ") {

                @Override
                public void picked(IEvent _e) {
                    back(c, backView, "Peeking");
                }
            };
            //peek.setToolTip("Peek at previous View");
            peek.spans(UV.cXNS);
            m.add(peek);
            VItem back = new VItem(new VString(_name, ViewColor.cThemeFont)) {

                @Override
                public void picked(IEvent _e) {
                    _container.setView(backView);
                    _container.getRootView().paint();
                    AViewEvent awe = WindowClosed.newInstance(0,null);
                    IView v = awe.disbatchEvent(_edit, _edit);
                    v.processEvent(awe);
                }
            };
            //back.setToolTip("Go back to previous View");
            back.spans(UV.cXNSE);
            m.add(back);

            c.add(m);
            c.add(_edit);
            _container.setView(c);
            _container.getRootView().paint();
        }
    }

    
    /**
     *
     * @param _view
     * @param _file
     */
    public static void toDisk(IView _view, File _file) {
        VS.writeImageToFile(toImage(_view), "jpg", _file);
    }
    

    /**
     *
     * @param _view
     * @param _file
     */
    public static void toPNG(IView _view, File _file) {
        VS.writeImageToFile(toImage(_view), "png", _file);
    }
    

    /**
     *
     * @param _view
     * @param _file
     */
    public static void toGIF(IView _view, File _file) {
        VS.writeImageToFile(toImage(_view), "gif", _file);
    }
    

    /**
     *
     * @return
     */
    public static String[] getToDiskFormats() {
        return ImageIO.getReaderFormatNames();
    }
    

    /**
     *
     * @param _g
     * @param _view
     */
    public static void paint(ICanvas _g, IView _view) {
        float w = _view.getW();
        float h = _view.getH();
        _view.mend();
        _view.paint(
                _view, _g,
                new Layer(null, 0f, 0f, 0f, 0f, w, h),
                UV.cRepair,
                new XYWH_I(0, 0, 0, 0));
    }
   
    

    /**
     *
     * @param _view
     * @return
     */
    public static IImage toImage(IView _view) {
        float w = _view.getW();
        float h = _view.getH();
        if (w < 1 || h < 1) {
            return null;
        }
        IImage image = VS.systemImage((int)w,(int)h,VS.c32BitARGB);
        ICanvas c = image.canvas(0);
        AColor clear = new AColor(0, 0, 0, 0);
        c.setColor(clear);//was setbackgound
        c.rect(true,0, 0, (int) w, (int) h);//was clear rect
        _view.mend();
        _view.paint(
                _view, c,
                new Layer(null, 0f, 0f, 0f, 0f, w, h),
                UV.cRepair,
                new XYWH_I(0, 0, 0, 0));
        return image;
    }
    

    /**
     *
     * @param _view
     * @return
     */
    public static IPaintable toPaintableImage(IPreview _view) {
        return toPaintableImage(_view, 0d, 0d, 1d);
    }
    

    /**
     *
     * @param _view
     * @param _f
     * @param _a
     * @param _t
     * @return
     */
    public static IPaintable toPaintableImage(IPreview _view, double _f, double _a, double _t) {
        final float w = _view.preferedW(1);
        final float h = _view.preferedH(1);
        if (w < 1 || h < 1) {
            return null;
        }
        final BufferedImage image = new BufferedImage((int) w, (int) h, BufferedImage.TYPE_INT_ARGB);
        ICanvas g = new GlueAWTGraphicsToCanvas(0,image.createGraphics());
        AColor clear = new AColor(0, 0, 0, 0);
        g.setColor(clear);//was setbackgound

        g.rect(true,0, 0, (int) w, (int) h);//was clear rect

        _view.preview(g, 0, 0, (int) w, (int) h, _f, _a, _t,1);
        return new IPaintable() {
            @Override
            public void paint(ICanvas g,XYWH_I _xywh) {
                if (_xywh.w == -1) {
                    _xywh.w = (int) (getW(null,null));
                }
                if (_xywh.h == -1) {
                    _xywh.h = (int) (getH(null,null));
                }
                if (image != null) {
                    g.drawImage(image, _xywh.x,_xywh.y,_xywh.w,_xywh.h, null);
                }
            }

            @Override
            public float getW(IPaintable _under,IPaintable _over) {
                return w;
            }

            @Override
            public float getH(IPaintable _under,IPaintable _over) {
                return h;
            }
        };
    }
    

    /**
     *
     * @param _view
     * @param _x
     * @param _y
     * @param _w
     * @param _h
     * @return
     */
    public static IImage toImage(IView _view, float _x, float _y, float _w, float _h) {

        IImage image = VS.systemImage((int) _w, (int) _h,VS.c32BitARGB);
        ICanvas g = image.canvas(0);
        g.translate((int) -_x, (int) -_y);
        _view.mend();
        _view.paint(
                _view, g,
                new Layer(null, 0, 0, 0, 0, _view.getW(), _view.getH()),
                UV.cRepair,
                new XYWH_I(0, 0, 0, 0));
        return image;
    }
    

    /**
     *
     * @param _view
     */
    public static void toggleSelected(IView _view) {
        if (_view.isSelected()) {
            _view.deselectBorder();
        } else {
            _view.selectBorder();
        }
    }
   
    
    
    /**
     *
     * @param _parent
     * @param child
     * @param _place
     * @return
     */
    public static XY_I getLocationInView(IView _parent, IView child,Place _place) {
        XY_I p = getLocationInView(_parent,child);
        if (p == null) {
            return p;
        }
        p.x += (int) (_place.getChildX() * child.getW());
        p.y += (int) (_place.getChildY() * child.getH());
        return p;
    }
    /**
     *
     * @param _parent
     * @param child
     * @return
     */
    public static XY_I getLocationInView(IView _parent, IView child) {
        int x = 0;
        int y = 0;
        IView parent = child;
        while (_parent != parent &&
                parent != null &&
                parent != NullView.cNull && !(parent instanceof IRootView)
                ) {
            if (parent instanceof VClip) {
                x += parent.getX() + ((VClip) parent).ox();
                y += parent.getY() + ((VClip) parent).oy();
            } else {
                x += parent.getX();
                y += parent.getY();
            }
            parent = parent.getParentView();
        }
        if (parent instanceof IRootView) {
            x -= parent.getBorder().getX();
            y -= parent.getBorder().getY();
        }
        if (parent == _parent) {
            return new XY_I(x, y);
        }
        return null;
    }
    
    

    /**
     *
     * @param _parent
     * @param _child
     * @return
     */
    public static boolean isChild(IView _parent, IView _child) {
        if (_parent == null) {
            return false;
        }
        if (_child == null) {
            return false;
        }
        if (_child == _parent) {
            return false;
        }
        for (; _child != NullView.cNull && _child != NullRootView.cNull; _child = _child.getParentView()) {
            if (_child == _parent) {
                return true;
            }
        }
        return false;
    }
    

    /**
     *
     * @param _parent
     * @param _child
     * @return
     */
    public static IView getChild(IView _parent, IView _child) {
        if (_child == _parent) {
            return null;
        }
        for (; _child != NullView.cNull && _child != NullRootView.cNull; _child = _child.getParentView()) {
            if (_child == _parent) {
                return _child;
            }
        }
        return null;
    }
    

    /**
     *
     * @param _view
     * @param _parentClass
     * @return
     */
    public static IView findParent(IView _view, Class _parentClass) {
        for (_view = _view.getParentView(); _view != NullView.cNull && _view != NullRootView.cNull; _view = _view.getParentView()) {
            if (_parentClass.isInstance(_view)) {
                return _view;
            }
        }
        return NullView.cNull;
    }
    

    /**
     *
     * @param view
     * @return
     */
    public static int getDistanceToRootView(IView view) {
        if (view == NullView.cNull) {
            return -1;
        }
        if (view instanceof IRootView) {
            return 0;
        }
        int distance = 0;
        for (; view != NullView.cNull && !(view instanceof IRootView); view = view.getParentView(), distance++);
        if (view == NullView.cNull) {
            return -1;
        }
        return distance;
    }
    

    /**
     *
     * @param _who
     * @param view
     * @param direction
     */
    public static void navigateFocus(long _who,IView view, int direction) {
        IView parent = view.getParentView();
        
        int x = 0;
        int y = 0;
        IView focusView = null;
        float vx = view.getX();
        float vy = view.getY();
        
		/*
        for (int i=0;i<placers.length;i++) if (placers[i].getDirection(view) == direction) {
        IView v = placers[i].getView();
        if (v == view || !(v.isEventEnabled(AWTEvent.KEY_EVENT_MASK))) continue;
        if (focusView == null) {
        focusView = v;
        x = Math.abs(vx - v.getX());
        y = Math.abs(vy - v.getY());
        } else {
        int _x = Math.abs(vx - v.getX());
        int _y = Math.abs(vy - v.getY()); 
        if (_x +_y < x + y) {
        focusView = v;
        x = _x;
        y = _y;
        }
        }
        }
         */
        if (focusView != null) {
            focusView.grabFocus(_who);
        } else {
            view.transferFocusToParent(_who);
        }
    }
    

    /**
     *
     * @param placer
     * @return
     */
    public static IPlacer[] getAllPlacers(IPlacer placer) {
        CArray placers = new CArray(IPlacer.class);
        placers.insertLast(placer);
        traverse(placer.getViewable().getView(), placers);
        return (IPlacer[]) placers.getAll();
    }
    

    private static void traverse(IView view, CArray placers) {
        Object[] array = view.getPlacers().toArray();
        for (int i = 0; i < array.length; i++) {
            placers.insertLast(array[i]);
        }
        for (int i = 0; i < array.length; i++) {
            traverse(((IPlacer) array[i]).getViewable().getView(), placers);
        }
    }
     
    /**
     *
     */
    public static float cManualScreenWidth = 0.0f;
    /**
     *
     */
    public static float cManualScreenHeight = 0.0f;
     
    private static float screenWidth = 0.0f;
    private static float screenHeight = 0.0f;
    private static Toolkit tk;
    

    /**
     *
     */
    public static void init() {
        if (tk != null) {
            return;
        }
        tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;

   
    }
    

    /**
     *
     * @param _views
     * @return
     */
    public static XYWH_I getBound(IView[] _views) {
        if (_views == null) {
            return new XYWH_I(0, 0, 0, 0);
        }
        XYWH_I bounds = null;
        for (int i = 0; i < _views.length; i++) {
            XYWH_I r = _views[i].getEventBounds();//!!1-6-09 was getBounds
            if (bounds == null) {
                bounds = r;
            } else {
                bounds = bounds.union(r);
            }
        }
        return bounds;
    }
    

    /**
     *
     * @param _p
     * @param _view
     * @return
     */
    public static boolean isWithin(XY_I _p, IView _view) {
        if (_p == null) {
            return true;
        }
        if (_view == null) {
            return false;
        }
        if (_p.x < 0 || _p.y < 0) {
            return false;
        }
        if (_p.x > _view.getW() || _p.y > _view.getH()) {
            return false;
        }
        return true;
    }
    

    /**
     *
     * @return
     */
    public static float getScreenWidth() {
        if (tk == null) {
            init();
        }
        if (cManualScreenWidth > 0.0f) {
            return cManualScreenWidth;
        }
        return screenWidth;
    }
    

    /**
     *
     * @return
     */
    public static float getScreenHeight() {
        if (tk == null) {
            init();
        }
        if (cManualScreenHeight > 0.0f) {
            return cManualScreenHeight;
        }
        return screenHeight;
    }
    

    /**
     *
     * @param _window
     * @param _view
     */
    public static void centerWindowRelativeToView(IView _window, IView _view) {

        IRootView client = _view.getRootView();
        XY_I sl = client.getLocationOnScreen();

        float x = sl.x + ((client.getW() / 2) - (_window.getW() / 2));
        float y = sl.y + ((client.getH() / 2) - (_window.getH() / 2));

        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        _window.setLocation((int) x, (int) y);

    }
    

    /**
     *
     * @param _reference
     * @param _place
     * @param _view
     * @return
     */
    public static XY_I pointRelativeToView(IView _reference, Place _place, IView _view) {

        XY_I p = _reference.getLocationOnScreen();
        p.x += (int) (_place.getParentX() * _reference.getW());
        p.y += (int) (_place.getParentY() * _reference.getH());
        p.x -= (int) (_place.getChildX() * _view.getW());
        p.y -= (int) (_place.getChildY() * _view.getH());
        return p;

    }
    

    /**
     *
     * @param _window
     */
    public static void centerWindow(IView _window) {
        float x = (getScreenWidth() / 2) - (_window.getW() / 2);
        float y = (getScreenHeight() / 2) - (_window.getH() / 2);
        _window.setLocation((int) x, (int) y);

    }
    

    /**
     *
     * @param _window
     * @param x
     */
    public static void centerWindowVertically(IView _window, float x) {
        float y = (getScreenHeight() / 2) - (_window.getH() / 2);
        _window.setLocation((int) x, (int) y);

    }
    

    /**
     *
     * @param _window
     * @param y
     */
    public static void centerWindowHorizontally(IView _window, float y) {
        float x = (getScreenWidth() / 2) - (_window.getW() / 2);
        _window.setLocation((int) x, (int) y);
    }
    
    /**
     *
     */
    public static IFramer framer = null;
    /**
     *
     * @param _view
     * @param _name
     * @return
     */
    public static AWindow frame(IView _view, Object _name) {
        if (framer != null) {
            return framer.frame(_view, _name);
        }
        else {
            return frame(_view, _name, true, true);
        }
    }

    /**
     *
     * @param _view
     * @param _nameView
     * @param _name
     * @param _canClose
     * @param _canMinimize
     * @return
     */
    public static AWindow frame(IView _view,IView _nameView, Object _name, boolean _canClose, boolean _canMinimize) {
        VFrame frameViewer = new VFrame(_view,_nameView, _name, _canClose, _canMinimize);
        AWindow frame = new AWindow(frameViewer);
        frame.setTitle(_name.toString());
        centerWindow(frame);
        frame.show();
        return frame;
    }

    /**
     *
     * @param _view
     * @param _name
     * @param _canClose
     * @param _canMinimize
     * @return
     */
    public static AWindow frame(IView _view, Object _name, boolean _canClose, boolean _canMinimize) {
        VFrame frameViewer = new VFrame(_view, _name, _canClose, _canMinimize);
        AWindow frame = new AWindow(frameViewer);
        frame.setTitle(_name.toString());
        frame.layoutAllInterior();
        centerWindow(frame);
        frame.show();
        return frame;
    }

    /**
     *
     * @param _view
     * @param _name
     * @return
     */
    public static AWindow exitFrame(IView _view, Object _name) {
        VFrame frameViewer = new VFrame(_view, _name);
        AWindow frame = new AWindow(frameViewer);
        frame.setTitle(_name.toString());
        frame.systemExitOnClose(true);
        frame.layoutAllInterior();
        centerWindow(frame);
        frame.show();
        return frame;
    }
    

    /**
     *
     * @param _window
     */
    public static void centerWindow(AWindow _window) {
        if (_window == null) {
            return;
        }
        _window.layoutInterior();
        float x = (getScreenWidth() / 2) - (_window.getW() / 2);
        float y = (getScreenHeight() / 2) - (_window.getH() / 2);
        _window.setLocation((int) x, (int) y);
    }
    

    /**
     *
     * @param _window
     * @param _view
     */
    public static void centerWindowRelativeToView(AWindow _window, IView _view) {
        if (_window == null) {
            return;
        }
        _window.layoutInterior();

        if (_view == null) {
            centerWindow(_window);
            return;
        }
        IRootView client = _view.getRootView();
        XY_I sl = client.getLocationOnScreen();

        float x = sl.x + ((client.getW() / 2) - (_window.getW() / 2));
        float y = sl.y + ((client.getH() / 2) - (_window.getH() / 2));

        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        _window.setLocation((int) x, (int) y);

    }
    

    /**
     *
     * @param _view
     * @param _name
     * @return
     */
    public static AWindow window(IView _view, Object _name) {
        AWindow window = new AWindow(_view);
        window.setTitle(_name.toString());
        centerWindow(window);
        window.show();
        return window;
    }

    /**
     *
     * @param _view
     * @param _name
     * @return
     */
    public static AWindow exitWindow(IView _view, Object _name) {
        AWindow window = new AWindow(_view);
        window.setTitle(_name.toString());
        window.systemExitOnClose(true);
        centerWindow(window);
        window.show();
        return window;
    }


    
    /**
     *
     * @param _view
     * @return
     */
    public static XY_I toPoint(IView _view) {
        return new XY_I(
                (int) (_view.getX() + (_view.getW() / 2)),
                (int) (_view.getY() + (_view.getH() / 2)));
    }
    

    /**
     *
     * @param _view
     * @param _fp
     * @param _tp
     * @return
     */
    public static XY_I toPerimeterPoint(IView _view, XY_I _fp, XY_I _tp) {
        float radians = (float) (UMath.angle(_fp.x, _fp.y, _tp.x, _tp.y));
        double[] fxy = UMath.rectanglePerimeter(_view.getW(), _view.getH(), radians);
        return new XY_I((int) (_fp.x + fxy[0]), (int) (_fp.y + fxy[1]));
    }
    

    /**
     *
     * @param _view
     * @param _place
     * @return
     */
    public static XY_I toPoint(IView _view, Place _place) {
        float x = _view.getX();
        float y = _view.getY();

        x += (int) (_place.getChildX() * _view.getW());
        y += (int) (_place.getChildY() * _view.getH());
        return new XY_I((int) x, (int) y);
    }
    

    /**
     *
     * @param _view
     * @param _place
     * @param _reference
     * @return
     */
    public static XY_I toViewPoint(IView _view, Place _place, IView _reference) {
        float x = (int) (_place.getChildX() * _view.getW());
        float y = (int) (_place.getChildY() * _view.getH());
        XY_I los = UV.getLocationInView(_reference, _view);
        if (los == null) {
            return null;
        }
        return new XY_I((los.x) + (int) x, (los.y) + (int) y);
    }
    

    /**
     *
     * @param _view
     * @param _point
     * @param _reference
     * @return
     */
    public static XY_I toViewPoint(IView _view, XY_I _point, IView _reference) {
        float x = (int) (_point.x);
        float y = (int) (_point.y);
        XY_I los = UV.getLocationInView(_reference, _view);
        if (los == null) {
            return null;
        }
        return new XY_I((los.x) + (int) x, (los.y) + (int) y);
    }
    

    /**
     *
     * @param _view
     * @param _place
     * @param _offset
     * @return
     */
    public static XY_I toGraphicsPoint(IView _view, Place _place, XY_I _offset) {
        float x = (int) (_place.getChildX() * _view.getW());
        float y = (int) (_place.getChildY() * _view.getH());
        XY_I los = _view.getLocationOnScreen();
        return new XY_I((los.x - _offset.x) + (int) x, (los.y - _offset.y) + (int) y);
    }
    

    /**
     *
     * @param _view
     * @return
     */
    public static XY_I toScreenPoint(IView _view) {
        XY_I los = _view.getLocationOnScreen();
        return new XY_I(
                (int) (los.x + (_view.getW() / 2)),
                (int) (los.y + (_view.getH() / 2)));
    }
    

    /**
     *
     * @param _view
     * @param _offset
     * @return
     */
    public static XY_I toGraphicsPoint(IView _view, XY_I _offset) {
        XY_I los = _view.getLocationOnScreen();
        return new XY_I(
                (int) ((los.x - _offset.x) + (_view.getW() / 2)),
                (int) ((los.y - _offset.y) + (_view.getH() / 2)));
    }
    

    /**
     *
     * @param _view
     * @return
     */
    public static float[] toFloatPoint(IView _view) {
        return new float[]{
                    _view.getX() + (_view.getW() / 2),
                    _view.getY() + (_view.getH() / 2)
                };
    }
    
    /**
     *
     * @param xy
     * @param w
     * @param h
     * @return
     */
    public static XYWH_I toRect(float[] xy, int w, int h) {
        return new XYWH_I((int) (xy[0] - (w / 2)), (int) (xy[1] - (h / 2)), w, h);
    }

    /**
     *
     * @param _t
     */
    public static void fatal(Throwable _t) {
        for (Object w : AWindow.openWindows.getAll(Object.class)) {
            ((AWindow) w).dispose();
        }
        VChain c = new VChain(UV.cSN);
        c.add(new VException(_t));
        c.add(new VItem(" Exit ") {

            @Override
            public void picked(IEvent _e) {
                System.exit(0);
            }
        });
        UV.frame(c, "Fatal Exception", false, false);
    }
    
    public static void framePopup(IView ref,Place place,IView popup,String name) {
        popup(ref,place,new VFrame(popup, name,true,false),false,false);
    }
    
    public static void popup(IView ref,Place place,IView popup,boolean _hideOnExit, boolean _hideOnLost) {
        XY_I p = new XY_I(0,0);
        p.x += (int) (place.getParentX() * ref.getW());
        p.y += (int) (place.getParentY() * ref.getH());
        p.x -= (int) (place.getChildX() * popup.getW());
        p.y -= (int) (place.getChildY() * popup.getH());
        popup(ref,p,popup,_hideOnExit,_hideOnLost);
    }
    
    public static void framePopup(IView ref,IEvent event,IView popup,String name) {
        popup(ref,event,new VFrame(popup, name,true,false),false,false);
    }
    
    public static void popup(IView ref,IEvent event,IView popup,boolean _hideOnExit, boolean _hideOnLost) {
        XY_I p = new XY_I(0,0);
        if (event instanceof AMouseEvent)
        {
            XY_I ep = ((AMouseEvent) event).getPoint();
            if (p != null)
            {
                p.x += ep.x;
                p.y += ep.y;
            }
        }
        popup(ref,p,popup,_hideOnExit,_hideOnLost);
    }
    
    public static void popup(IView ref,XY_I place,IView popup,boolean _hideOnExit, boolean _hideOnLost) {
        IView at = ref;
        for (; at != NullView.cNull && !(at instanceof VPopupViewer); ) {
            at = at.getParentView();
        }
        if (at instanceof VPopupViewer) {
            ((VPopupViewer)at).popup(ref, popup, place,_hideOnExit,_hideOnLost);
        }
    }

    /**
     *
     * @param _vw
     * @param _vh
     * @param _w
     * @param _h
     * @return
     */
    public static XYWH_I fitInBox(int _vw, int _vh, int _w, int _h) {
        int px = 0;
        int py = 0;
        double phw = (double)_vh/(double)_vw;
        int nw = _w;
        int nh = (int)(_w*phw);
        if (nh > _h) {
            double pwh = (double)_vw/(double)_vh;
            nw = (int)(_h*pwh);
            nh = _h;
            px+= (_w-nw)/2;
        }
        else {
            py += (_h-nh)/2;
        }
        return new XYWH_I(px,py,nw,nh);
    }

}
