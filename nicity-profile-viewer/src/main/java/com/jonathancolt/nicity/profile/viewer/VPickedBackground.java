/*
 * $Revision$
 * $Date$
 *
 * Copyright (C) 1999-$year$ Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
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

import com.jonathancolt.nicity.core.lang.MinMaxLong;
import com.jonathancolt.nicity.core.value.IValue;
import com.jonathancolt.nicity.profile.server.model.ClassMethod;
import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.value.VRadioList;
import com.jonathancolt.nicity.view.value.VToggle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 *
 */
class VPickedBackground extends Viewer {

    private RollupCalls rollupCalls = new RollupCalls();
    VRadioList valueStrat;
    VRadioList stackStrat;
    VRadioList colorings;
    VRadioList barStrat;

    VPickedBackground(final VCallDepthStack callDepthStack) {

        final NameUtils nameUtils = new NameUtils();

        valueStrat = new VRadioList(UV.cSWNW, new IValue() {
            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public void setValue(Object arg0) {
                callDepthStack.setValueStrategy((ValueStrategy) arg0);
            }
        });
        valueStrat.add("totaltime", new ValueStrategy() {

            @Override
            public String name() {
                return "totaltime";
            }


            @Override
            public long value(InterfaceArea callArea) {
                ConcurrentHashMap<String, ClassMethod> classMethods = callArea.callClass.getClassMethods();
                return TimeUnit.MILLISECONDS.toNanos(1) + (Math.max(rollupCalls.getSuccesslatency(classMethods), rollupCalls.getFailedlatency(classMethods)) * rollupCalls.getCalled(classMethods));
            }

            @Override
            public String name(long value) {
                return nameUtils.latencyString(value) + " totaltime";
            }
        }, true);
        valueStrat.add("latency", new ValueStrategy() {
            @Override
            public String name() {
                return "latency";
            }
            @Override
            public long value(InterfaceArea callArea) {
                ConcurrentHashMap<String, ClassMethod> classMethods = callArea.callClass.getClassMethods();
                return TimeUnit.MILLISECONDS.toNanos(1) + Math.max(rollupCalls.getSuccesslatency(classMethods), rollupCalls.getFailedlatency(classMethods));
            }

            @Override
            public String name(long value) {
                return nameUtils.latencyString(value);
            }
        }, true);
        valueStrat.add("called", new ValueStrategy() {
            @Override
            public String name() {
                return "called";
            }
            @Override
            public long value(InterfaceArea callArea) {
                ConcurrentHashMap<String, ClassMethod> classMethods = callArea.callClass.getClassMethods();
                return Math.max(rollupCalls.getCalled(classMethods), rollupCalls.getFailed(classMethods));
            }

            @Override
            public String name(long value) {
                return value + " called";
            }
        }, false);
        valueStrat.add("calledBy", new ValueStrategy() {
            @Override
            public String name() {
                return "calledBy";
            }
            @Override
            public long value(InterfaceArea callArea) {
                return callArea.calledByCount;
            }

            @Override
            public String name(long value) {
                return value + " calledByCount";
            }
        }, false);
        valueStrat.add("callsTo", new ValueStrategy() {
            @Override
            public String name() {
                return "callsTo";
            }
            @Override
            public long value(InterfaceArea callArea) {
                return callArea.callsTo.size();
            }

            @Override
            public String name(long value) {
                return value + " callsTo";
            }
        }, false);
        valueStrat.add("callComplexity", new ValueStrategy() {
            @Override
            public String name() {
                return "callComplexity";
            }
            @Override
            public long value(InterfaceArea callArea) {
                return callArea.calledByCount * callArea.callsTo.size();
            }

            @Override
            public String name(long value) {
                return value + " callComplexity";
            }
        }, false);
        valueStrat.add("constant", new ValueStrategy() {
            @Override
            public String name() {
                return "constant";
            }
            @Override
            public long value(InterfaceArea callArea) {
                return 1;
            }

            @Override
            public String name(long value) {
                return "";
            }
        }, false);

        stackStrat = new VRadioList(UV.cSWNW, new IValue() {
            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public void setValue(Object arg0) {
                callDepthStack.setStackStrategy((ValueStrategy) arg0);
            }
        });
        stackStrat.add("totaltime", new ValueStrategy() {
            @Override
            public String name() {
                return "totaltime";
            }
            @Override
            public long value(InterfaceArea callArea) {
                ConcurrentHashMap<String, ClassMethod> classMethods = callArea.callClass.getClassMethods();
                return TimeUnit.MILLISECONDS.toNanos(1) + (Math.max(rollupCalls.getSuccesslatency(classMethods), rollupCalls.getFailedlatency(classMethods)) * rollupCalls.getCalled(classMethods));
            }

            @Override
            public String name(long value) {
                return nameUtils.latencyString(value);
            }
        }, true);
        stackStrat.add("latency", new ValueStrategy() {
            @Override
            public String name() {
                return "latency";
            }
            @Override
            public long value(InterfaceArea callArea) {
                ConcurrentHashMap<String, ClassMethod> classMethods = callArea.callClass.getClassMethods();
                return TimeUnit.MILLISECONDS.toNanos(1) + Math.max(rollupCalls.getSuccesslatency(classMethods), rollupCalls.getFailedlatency(classMethods));
            }

            @Override
            public String name(long value) {
                return nameUtils.latencyString(value);
            }
        }, false);
        stackStrat.add("called", new ValueStrategy() {
            @Override
            public String name() {
                return "called";
            }
            @Override
            public long value(InterfaceArea callArea) {
                ConcurrentHashMap<String, ClassMethod> classMethods = callArea.callClass.getClassMethods();
                return Math.max(rollupCalls.getCalled(classMethods), rollupCalls.getFailed(classMethods));
            }

            @Override
            public String name(long value) {
                return value + " called";
            }
        }, false);
        stackStrat.add("calledBy", new ValueStrategy() {
            @Override
            public String name() {
                return "calledBy";
            }
            @Override
            public long value(InterfaceArea callArea) {
                return callArea.calledByCount;
            }

            @Override
            public String name(long value) {
                return value + " calledBy";
            }
        }, false);
        stackStrat.add("callsTo", new ValueStrategy() {
            @Override
            public String name() {
                return "callsTo";
            }
            @Override
            public long value(InterfaceArea callArea) {
                return callArea.callsTo.size();
            }

            @Override
            public String name(long value) {
                return value + " callsTo";
            }
        }, false);
        stackStrat.add("callComplexity", new ValueStrategy() {
            @Override
            public String name() {
                return "callComplexity";
            }
            @Override
            public long value(InterfaceArea callArea) {
                return callArea.calledByCount * callArea.callsTo.size();
            }

            @Override
            public String name(long value) {
                return value + " callComplexity";
            }
        }, false);
        stackStrat.add("constant", new ValueStrategy() {
            @Override
            public String name() {
                return "constant";
            }
            @Override
            public long value(InterfaceArea callArea) {
                return 1;
            }

            @Override
            public String name(long value) {
                return "";
            }
        }, false);


        colorings = new VRadioList(UV.cSWNW, new IValue() {
            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public void setValue(Object arg0) {
                callDepthStack.setColoring((Coloring) arg0);
            }
        });
        colorings.add("heat", new Heat(), true);
        colorings.add("calls", new Coloring() {

            @Override
            public AColor value(InterfaceArea callArea, long maxV) {
                double rank = 1d - (MinMaxLong.zeroToOne(0, maxV, callArea.calledByCount));
                return AColor.getWarmToCool(rank);
            }
        }, false);
        colorings.add("grays", new Grayscale(), false);
        colorings.add("class", new ClassColoring(), false);


        barStrat = new VRadioList(UV.cEW, new IValue() {
            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public void setValue(Object arg0) {
                callDepthStack.setBarStrategy((BarStrategy) arg0);
            }
        });
        barStrat.add("totaltime", new BarStrategy() {
            @Override
            public String name() {
                return "totaltime";
            }

            @Override
            public Object value(InterfaceArea callArea) {
                ConcurrentHashMap<String, ClassMethod> classMethods = callArea.callClass.getClassMethods();
                long tn = Math.max(rollupCalls.getSuccesslatency(classMethods), rollupCalls.getFailedlatency(classMethods)) * rollupCalls.getCalled(classMethods);
                return nameUtils.nanosToIndex(tn);
            }

            @Override
            public String name(Object value) {
                long v = (Long) value;
                return nameUtils.nanoIndexToString(v);
            }
        }, false);
        barStrat.add("latency", new BarStrategy() {
            @Override
            public String name() {
                return "latency";
            }

            @Override
            public Object value(InterfaceArea callArea) {
                ConcurrentHashMap<String, ClassMethod> classMethods = callArea.callClass.getClassMethods();
                long tn = TimeUnit.MILLISECONDS.toNanos(1) + Math.max(rollupCalls.getSuccesslatency(classMethods), rollupCalls.getFailedlatency(classMethods));
                return nameUtils.nanosToIndex(tn);
            }

            @Override
            public String name(Object value) {
                long v = (Long) value;
                return nameUtils.nanoIndexToString(v);
            }
        }, false);
        barStrat.add("called", new BarStrategy() {
            @Override
            public String name() {
                return "called";
            }

            @Override
            public Object value(InterfaceArea callArea) {
                ConcurrentHashMap<String, ClassMethod> classMethods = callArea.callClass.getClassMethods();
                long called = Math.max(rollupCalls.getCalled(classMethods), rollupCalls.getFailed(classMethods));
                return nameUtils.calledOrderOfMagnitudeIndex(called);
            }

            @Override
            public String name(Object value) {
                long v = (Long) value;
                return nameUtils.orderOfMagnitudeIndexToString(v);
            }
        }, false);
        barStrat.add("calledBy", new BarStrategy() {
            @Override
            public String name() {
                return "calledBy";
            }

            @Override
            public Object value(InterfaceArea callArea) {
                return 1L + (long) (callArea.calledByCount);
            }

            @Override
            public String name(Object value) {
                return ((Long) value - 1L) + " calledBy";
            }
        }, false);
        barStrat.add("callsTo", new BarStrategy() {
            @Override
            public String name() {
                return "callsTo";
            }

            @Override
            public Object value(InterfaceArea callArea) {
                return 1L + (long) (callArea.callsTo.size());
            }

            @Override
            public String name(Object value) {
                return ((Long) value - 1L) + " callsTo";
            }
        }, false);
        barStrat.add("callComplexity", new BarStrategy() {
            @Override
            public String name() {
                return "callComplexity";
            }

            @Override
            public Object value(InterfaceArea callArea) {
                return (long) (1 + callArea.calledByCount * callArea.callsTo.size());
            }

            @Override
            public String name(Object value) {
                return ((Long) value - 1L) + " callComplexity";
            }
        }, false);
        barStrat.add("stackDepths", new BarStrategy() {
            @Override
            public String name() {
                return "stackDepths";
            }

            @Override
            public Object value(InterfaceArea callArea) {
                return 1 + (long) (callArea.depths.size());
            }

            @Override
            public String name(Object value) {
                return ((Long) value - 1L) + " stackDepths";
            }
        }, false);
        barStrat.add("only one", new BarStrategy() {
            @Override
            public String name() {
                return "only one";
            }

            @Override
            public Object value(InterfaceArea callArea) {
                return "only one";
            }

            @Override
            public String name(Object value) {
                return nameUtils.shortName(value.toString());
            }
        }, false);
        barStrat.add("className", new BarStrategy() {
            @Override
            public String name() {
                return "className";
            }

            @Override
            public Object value(InterfaceArea callArea) {
                return callArea.getName();
            }

            @Override
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
        //c.add(UV.border(new VChain(UV.cEW, new VString("Bars:"), barStrat, ascend), new SolidBorder(ViewColor.cTheme, 4)));
        //c.add(UV.border(new VChain(UV.cEW, new VString("Stack:"), stackStrat), new SolidBorder(ViewColor.cTheme, 4)));
        //c.add(UV.border(new VChain(UV.cEW, new VString("Value:"), valueStrat), new SolidBorder(ViewColor.cTheme, 4)));
        //c.add(UV.border(new VChain(UV.cEW, new VString("Color:"), colorings), new SolidBorder(ViewColor.cTheme, 4)));
        setContent(c);
    }
}
