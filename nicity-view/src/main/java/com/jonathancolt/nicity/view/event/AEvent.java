/*
 * AEvent.java.java
 *
 * Created on 01-03-2010 01:31:39 PM
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
package com.jonathancolt.nicity.view.event;

import com.jonathancolt.nicity.view.interfaces.IEvent;

/**
 *
 * @author Administrator
 */
abstract public class AEvent implements IEvent {
    /**
     *
     */
    protected long who;
    /**
     *
     */
    protected Object source;
        /**
         *
         */
        public AEvent() {}
        /**
         *
         * @param _source
         */
        public AEvent(Object _source) { source = _source; }
        /**
         *
         * @param _source
         */
        public void setSource(Object _source) { source = _source; }
        /**
         *
         * @return
         */
    @Override
        public Object getSource() { return source; }
    /**
     *
     * @return
     */
    @Override
    public long who() { return who; }
}
