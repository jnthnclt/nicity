/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonathancolt.nicity.view.adaptor;

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

/**
 *
 * @author jonathan
 */
public interface IPath {
    public Object getRawPath();

    public void moveTo(int x, int y);

    public void quadTo(float x1, float y1, float x2, float y2);

    public void lineTo(int i, int y);

    public void closePath();

    public void moveTo(float _sx, float _sy);

    public void lineTo(float f, float f0);

    public void curveTo(float f, float _fromY, float f0, float _fromY0, float midX, float midY);
}
