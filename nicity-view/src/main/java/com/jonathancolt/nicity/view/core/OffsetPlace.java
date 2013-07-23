/*
 * OffsetPlace.java.java
 *
 * Created on 01-03-2010 01:29:42 PM
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

/**
 *
 * @author Administrator
 */
public class OffsetPlace extends Place {

    /**
     *
     * @param _px
     * @param _py
     * @param _cx
     * @param _cy
     * @param _ox
     * @param _oy
     */
    public OffsetPlace(float _px, float _py, float _cx, float _cy, float _ox, float _oy) {
        super(_px, _py, _cx, _cy, _ox, _oy);
    }

    /**
     *
     * @param _place
     * @param _ox
     * @param _oy
     */
    public OffsetPlace(Place _place, float _ox, float _oy) {
        super(_place.px, _place.py, _place.cx, _place.cy, _ox, _oy);
    }

    /**
     *
     * @param _place
     * @param _flex
     */
    public OffsetPlace(Place _place, Flex _flex) {
        super(_place, _flex);
    }

    /**
     *
     * @param _place
     */
    public OffsetPlace(Place _place) {
        super(_place);
    }

    /**
     *
     * @param _px
     */
    @Override
    public void setParentX(float _px) {
    }

    /**
     *
     * @param _py
     */
    @Override
    public void setParentY(float _py) {
    }

    /**
     *
     * @param _cx
     */
    @Override
    public void setChildX(float _cx) {
    }

    /**
     *
     * @param _cy
     */
    @Override
    public void setChildY(float _cy) {
    }

    /**
     *
     * @param _ox
     */
    @Override
    public void setOffsetX(int _ox) {
        ox = _ox;
    }

    /**
     *
     * @param _oy
     */
    @Override
    public void setOffsetY(int _oy) {
        oy = _oy;
    }
}
