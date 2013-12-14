package com.jonathancolt.nicity.view.rpp;

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

import com.jonathancolt.nicity.core.lang.NullOut;
import com.jonathancolt.nicity.core.lang.UString;
import com.jonathancolt.nicity.core.lang.UText;
import com.jonathancolt.nicity.view.adaptor.IFontConstants;
import com.jonathancolt.nicity.view.border.LineBorder;
import com.jonathancolt.nicity.view.border.ViewBorder;
import com.jonathancolt.nicity.view.concurrent.VConcurrentEditText;
import com.jonathancolt.nicity.view.core.AFont;
import com.jonathancolt.nicity.view.core.UV;
import com.jonathancolt.nicity.view.core.VButton;
import com.jonathancolt.nicity.view.core.VChain;
import com.jonathancolt.nicity.view.core.VPan;
import com.jonathancolt.nicity.view.core.VString;
import com.jonathancolt.nicity.view.core.Viewer;
import com.jonathancolt.nicity.view.interfaces.IEvent;
import com.jonathancolt.nicity.view.interfaces.IView;
import com.jonathancolt.nicity.view.ngraph.NG;
import com.jonathancolt.nicity.view.ngraph.NGEnvAnim;
import com.jonathancolt.nicity.view.value.VFiles;
import java.io.File;

public class VTextEditor extends Viewer implements IRPPViewable {

    public static IView viewable(String[] args) {
        return new VTextEditor();
    }
    String name;
    boolean graphing = false;

    public VTextEditor() {
        this("untitled");
    }

    VTextEditor(String _name) {
        name = _name;

        String[] t = new String[]{
            "Example: We the People of the United States, in Order to form a more perfect Union,",
            "establish Justice, insure domestic Tranquility, provide for the common defence,",
            "promote the general Welfare, and secure the Blessings of Liberty to ourselves",
            "and our Posterity, do ordain and establish this Constitution for the United States",
            "of America."
        };
        final VConcurrentEditText text = new VConcurrentEditText(t, new AFont(IFontConstants.cMonospaced, IFontConstants.cPlain, 14));
        final VPan pan = new VPan(text, 600, 800);
        pan.setBorder(new LineBorder());


        VChain f = new VChain(UV.cEW);
        f.add(new VButton("Open") {

            @Override
            public void picked(IEvent _e) {
                VFiles f = new VFiles(new File(System.getProperty("user.dir")), "Save", true);
                f.init(false, true, 400, 400);
                UV.popup(this, UV.cNS, f, true, true);
                File open = f.waitForSet();
                text.setText(UText.loadTextFile(open));
            }
        });
        f.add(new VButton("Graph Text") {

            @Override
            public void picked(IEvent _e) {
                if (graphing) {
                    graphing = !graphing;
                    setView(new VString("Graph Text"));
                    pan.setView(text);
                } else {
                    graphing = !graphing;
                    setView(new VString("Edit Text"));
                    NGEnvAnim env = new NGEnvAnim((int) pan.getW(), (int) pan.getH());
                    NG ng = new NG();
                    String t = UString.toString(text.getText(), " ");
                    String[] words = UString.toStringArray(t, " ");
                    for (int i = 1; i < words.length; i++) {
                        ng.order(words[i - 1], words[i]);
                    }
                    env.set(NullOut.cNull, ng);
                    pan.setView(env);
                }
                paint();
            }
        });
        f.add(new VButton("Save") {

            @Override
            public void picked(IEvent _e) {
                VFiles f = new VFiles(new File(System.getProperty("user.dir")), "Save", true);
                f.init(false, true, 400, 400);
                UV.popup(this, UV.cNS, f, true, true);
                File saveTo = f.waitForSet();
                UText.saveTextFile(text.getText(), saveTo);
            }
        });
        f.setBorder(new ViewBorder());


        setContent(new VChain(UV.cSN, f,new Viewer(pan)));
    }
}
