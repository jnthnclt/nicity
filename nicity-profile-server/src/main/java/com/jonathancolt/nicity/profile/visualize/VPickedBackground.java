/*
 * $Revision$
 * $Date$
 *
 * Copyright (C) 1999-$year$ Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
 */
package com.jonathancolt.nicity.profile.visualize;

import com.jonathancolt.nicity.core.value.IValue;
import com.jonathancolt.nicity.view.border.SolidBorder;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.VString;
import com.jonathancolt.nicity.view.core.ViewColor;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.value.VRadioList;
import com.jonathancolt.nicity.view.value.VToggle;
import java.util.concurrent.TimeUnit;

/**
 *
 */
class VPickedBackground extends Viewer {

    VPickedBackground(final VCallDepthStack callDepthStack) {

        final NameUtils nameUtils = new NameUtils();

        VRadioList valueStrat = new VRadioList(UV.cEW, new IValue() {
            public Object getValue() {
                return null;
            }

            public void setValue(Object arg0) {
                callDepthStack.setValueStrategy((ValueStrategy) arg0);
            }
        });
        valueStrat.add("totaltime", new ValueStrategy() {
            public long value(VCallDepthStack.InterfaceArea callArea) {
                return TimeUnit.MILLISECONDS.toNanos(1) + (Math.max(callArea.callClass.getSuccesslatency(), callArea.callClass.getFailedlatency()) * callArea.callClass.getCalled());
            }

            public String name(long value) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }, true);
        valueStrat.add("latency", new ValueStrategy() {
            public long value(VCallDepthStack.InterfaceArea callArea) {
                return TimeUnit.MILLISECONDS.toNanos(1) + Math.max(callArea.callClass.getSuccesslatency(), callArea.callClass.getFailedlatency());
            }

            public String name(long value) {
                return nameUtils.latencyString(value);
            }
        }, true);
        valueStrat.add("called", new ValueStrategy() {
            public long value(VCallDepthStack.InterfaceArea callArea) {
                return Math.max(callArea.callClass.getCalled(), callArea.callClass.getFailed());
            }

            public String name(long value) {
                return value + " called";
            }
        }, false);
        valueStrat.add("calledBy", new ValueStrategy() {
            public long value(VCallDepthStack.InterfaceArea callArea) {
                return callArea.calledByCount;
            }

            public String name(long value) {
                return value + " calledByCount";
            }
        }, false);
        valueStrat.add("callsTo", new ValueStrategy() {
            public long value(VCallDepthStack.InterfaceArea callArea) {
                return callArea.callsTo.size();
            }

            public String name(long value) {
                return value + " callsTo";
            }
        }, false);
        valueStrat.add("callComplexity", new ValueStrategy() {
            public long value(VCallDepthStack.InterfaceArea callArea) {
                return callArea.calledByCount * callArea.callsTo.size();
            }

            public String name(long value) {
                return value + " callComplexity";
            }
        }, false);
        valueStrat.add("constant", new ValueStrategy() {
            public long value(VCallDepthStack.InterfaceArea callArea) {
                return 1;
            }

            public String name(long value) {
                return "";
            }
        }, false);

        VRadioList stackStrat = new VRadioList(UV.cEW, new IValue() {
            public Object getValue() {
                return null;
            }

            public void setValue(Object arg0) {
                callDepthStack.setStackStrategy((ValueStrategy) arg0);
            }
        });
        stackStrat.add("totaltime", new ValueStrategy() {
            public long value(VCallDepthStack.InterfaceArea callArea) {
                return TimeUnit.MILLISECONDS.toNanos(1) + (Math.max(callArea.callClass.getSuccesslatency(), callArea.callClass.getFailedlatency()) * callArea.callClass.getCalled());
            }

            public String name(long value) {
                return nameUtils.latencyString(value);
            }
        }, true);
        stackStrat.add("latency", new ValueStrategy() {
            public long value(VCallDepthStack.InterfaceArea callArea) {
                return TimeUnit.MILLISECONDS.toNanos(1) + Math.max(callArea.callClass.getSuccesslatency(), callArea.callClass.getFailedlatency());
            }

            public String name(long value) {
                return nameUtils.latencyString(value);
            }
        }, false);
        stackStrat.add("called", new ValueStrategy() {
            public long value(VCallDepthStack.InterfaceArea callArea) {
                return Math.max(callArea.callClass.getCalled(), callArea.callClass.getFailed());
            }

            public String name(long value) {
                return value + " called";
            }
        }, false);
        stackStrat.add("calledBy", new ValueStrategy() {
            public long value(VCallDepthStack.InterfaceArea callArea) {
                return callArea.calledByCount;
            }

            public String name(long value) {
                return value + " calledBy";
            }
        }, false);
        stackStrat.add("callsTo", new ValueStrategy() {
            public long value(VCallDepthStack.InterfaceArea callArea) {
                return callArea.callsTo.size();
            }

            public String name(long value) {
                return value + " callsTo";
            }
        }, false);
        stackStrat.add("callComplexity", new ValueStrategy() {
            public long value(VCallDepthStack.InterfaceArea callArea) {
                return callArea.calledByCount * callArea.callsTo.size();
            }

            public String name(long value) {
                return value + " callComplexity";
            }
        }, false);
        stackStrat.add("constant", new ValueStrategy() {
            public long value(VCallDepthStack.InterfaceArea callArea) {
                return 1;
            }

            public String name(long value) {
                return "";
            }
        }, false);


        VRadioList colorings = new VRadioList(UV.cEW, new IValue() {
            public Object getValue() {
                return null;
            }

            public void setValue(Object arg0) {
                callDepthStack.setColoring((Coloring) arg0);
            }
        });
        colorings.add("heat", new Heat(), true);
        colorings.add("grays", new Grayscale(), false);
        colorings.add("class", new ClassColoring(), false);


        VRadioList barStrat = new VRadioList(UV.cEW, new IValue() {
            public Object getValue() {
                return null;
            }

            public void setValue(Object arg0) {
                callDepthStack.setBarStrategy((BarStrategy) arg0);
            }
        });
        barStrat.add("totaltime", new BarStrategy() {
            public Object value(VCallDepthStack.InterfaceArea callArea) {
                long tn = Math.max(callArea.callClass.getSuccesslatency(), callArea.callClass.getFailedlatency()) * callArea.callClass.getCalled();
                return nameUtils.nanosToIndex(tn);
            }

            public String name(Object value) {
                long v = (Long) value;
                return nameUtils.nanoIndexToString(v);
            }
        }, false);
        barStrat.add("latency", new BarStrategy() {
            public Object value(VCallDepthStack.InterfaceArea callArea) {
                long tn = TimeUnit.MILLISECONDS.toNanos(1) + Math.max(callArea.callClass.getSuccesslatency(), callArea.callClass.getFailedlatency());
                return nameUtils.nanosToIndex(tn);
            }

            public String name(Object value) {
                long v = (Long) value;
                return nameUtils.nanoIndexToString(v);
            }
        }, false);
        barStrat.add("called", new BarStrategy() {
            public Object value(VCallDepthStack.InterfaceArea callArea) {
                long called = Math.max(callArea.callClass.getCalled(), callArea.callClass.getFailed());
                return nameUtils.calledOrderOfMagnitudeIndex(called);
            }

            public String name(Object value) {
                long v = (Long) value;
                return nameUtils.orderOfMagnitudeIndexToString(v);
            }
        }, false);
        barStrat.add("calledBy", new BarStrategy() {
            public Object value(VCallDepthStack.InterfaceArea callArea) {
                return 1L+(long) (callArea.calledByCount);
            }

            public String name(Object value) {
                return ((Long)value-1L) + " calledBy";
            }
        }, false);
        barStrat.add("callsTo", new BarStrategy() {
            public Object value(VCallDepthStack.InterfaceArea callArea) {
                return 1L+(long) (callArea.callsTo.size());
            }

            public String name(Object value) {
                return ((Long)value-1L) + " callsTo";
            }
        }, false);
        barStrat.add("callComplexity", new BarStrategy() {
            public Object value(VCallDepthStack.InterfaceArea callArea) {
                return (long) (1 + callArea.calledByCount * callArea.callsTo.size());
            }

            public String name(Object value) {
                return ((Long)value-1L) + " callComplexity";
            }
        }, false);
        barStrat.add("stackDepths", new BarStrategy() {
            public Object value(VCallDepthStack.InterfaceArea callArea) {
                return 1+(long) (callArea.depths.size());
            }

            public String name(Object value) {
                return ((Long)value-1L) + " stackDepths";
            }
        }, false);
        barStrat.add("className", new BarStrategy() {
            public Object value(VCallDepthStack.InterfaceArea callArea) {
                return callArea.getName();
            }

            public String name(Object value) {
                return nameUtils.shortName(value.toString());
            }
        }, true);


        VToggle ascend = new VToggle(" sort ", true) {
            @Override
            public void picked(IEvent _e) {
                super.picked(_e);
                callDepthStack.sortOrder = isTrue();
                callDepthStack.paint();
            }
        };

        VChain c = new VChain(UV.cSWNW);
        c.add(UV.border(new VChain(UV.cEW, new VString("Bars:"), barStrat, ascend), new SolidBorder(ViewColor.cTheme, 4)));
        c.add(UV.border(new VChain(UV.cEW, new VString("Stack:"), stackStrat), new SolidBorder(ViewColor.cTheme, 4)));
        c.add(UV.border(new VChain(UV.cEW, new VString("Value:"), valueStrat), new SolidBorder(ViewColor.cTheme, 4)));
        c.add(UV.border(new VChain(UV.cEW, new VString("Color:"), colorings), new SolidBorder(ViewColor.cTheme, 4)));
        setContent(c);
    }
}
