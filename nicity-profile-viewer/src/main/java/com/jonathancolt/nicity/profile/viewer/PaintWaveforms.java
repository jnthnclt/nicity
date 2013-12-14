/*
 * Copyright 2013 jonathan.
 *
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

import com.jonathancolt.nicity.core.lang.MinMaxDouble;
import com.jonathancolt.nicity.core.lang.UDouble;
import com.jonathancolt.nicity.view.core.AColor;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.ViewColor;
import com.jonathancolt.nicity.view.interfaces.ICanvas;
import java.text.DecimalFormat;

/**
 *
 * @author jonathan
 */
public class PaintWaveforms {

    static final DecimalFormat df = new DecimalFormat("0.0");

    public void paintGrid(AColor color, double[] hits, MinMaxDouble mmd, String prefix, int xOffset, int yOffset, String suffix, boolean solid, ICanvas g, int _x, int _y, int _w, int _h) {
        g.setColor(ViewColor.cVisualizeTheme.brighter());
        float hs = _h / 10f;
        for (int i = 0; i < 10; i++) {
            int y = _y + (int) (hs * i);
            g.line(_x, y, _x + _w, y);
        }

        float ws = _w / 100f;
        for (int i = 0; i < 100; i++) {
            int x = _x + (int) (ws * i);
            g.line(x, _y, x, _y + _h);
        }

        g.setColor(ViewColor.cVisualizeTheme);
        g.roundRect(false, _x, _y, _w, _h, 4, 4);

    }

    public void paintWaveform(AColor color, double[] hits, MinMaxDouble mmd, String prefix, int xOffset, int yOffset, String suffix, boolean solid, ICanvas g, int _x, int _y, int _w, int _h) {
        if (hits == null) {
            return;
        }
        mmd.value(0d);
        for (double d : hits) {
            mmd.value(d);
        }

        g.setColor(color);
        for (int i = 1; i < hits.length; i++) {
            int fy = _y + _h - (int) (UDouble.clamp(mmd.zeroToOne(hits[i - 1]), 0, 1) * _h);
            int ty = _y + _h - (int) (UDouble.clamp(mmd.zeroToOne(hits[i - 0]), 0, 1) * _h);
            int fx = _x + (int) (((double) (i - 1) / (double) hits.length) * _w);
            int tx = _x + (int) (((double) (i - 0) / (double) hits.length) * _w);
            if (solid) {
                g.polygon(true, new int[]{fx, fx, tx, tx}, new int[]{_y + _h, fy, ty, _y + _h}, 4);
            } else {
                g.line(fx, fy, tx, ty);
            }
        }
    }

    public void paintLabels(AColor color, double[] hits, MinMaxDouble mmd, String prefix, int xOffset, int yOffset, String suffix, boolean solid, ICanvas g, int _x, int _y, int _w, int _h) {
        mmd.value(0d);
        for (double d : hits) {
            mmd.value(d);
        }

        g.setColor(color);
        g.roundRect(true, _x + xOffset - 16, _y + yOffset - 12, 12, 12, 3, 3);
        g.setFont(UV.fonts[UV.cSmall]);
        g.setColor(ViewColor.cThemeFont);
        String summary = prefix + "    last=" + df.format(hits[hits.length - 1]) + suffix + "    min=" + df.format(mmd.min) + " " + suffix + "    max=" + df.format(mmd.max) + " " + suffix + "    mean=" + df.format(mmd.mean()) + " " + suffix;
        g.drawString(summary, _x + xOffset, _y + yOffset);

    }
}
