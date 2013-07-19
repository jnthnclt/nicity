/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package colt.nicity.view.rpp;

import colt.nicity.core.collection.CArray;
import colt.nicity.core.lang.UClass;
import colt.nicity.view.border.BuldgeBorder;
import colt.nicity.view.border.MenuItemBorder;
import colt.nicity.view.border.ViewBorder;
import colt.nicity.view.core.UV;
import colt.nicity.view.core.VChain;
import colt.nicity.view.core.VPan;
import colt.nicity.view.core.VString;
import colt.nicity.view.core.ViewColor;
import colt.nicity.view.core.Viewer;
import colt.nicity.view.interfaces.IEvent;
import colt.nicity.view.interfaces.IView;
import colt.nicity.view.list.VItem;
import colt.nicity.view.list.VList;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonathan
 */
public class RPPHome extends Viewer {

    public static void main(String[] args) {
        System.out.println(new File("./").getAbsolutePath());
        ViewColor.onGray();
        UV.exitFrame(new RPPHome("/home/jonathan/Dropbox/Code/nicity-goal/target/nicity-goal-1.0-SNAPSHOT.jar"), "");
    }
    String[] jars;
    Viewer menu = new Viewer();
    Viewer content = new Viewer();
    CArray<VItem> apps = new CArray<>();

    public RPPHome(String... jars) {
        this.jars = jars;
        init();
        
        menu.setView(menu());
        menu.spans(UV.cXEW);
        content.setView(new VPan(new VList(apps, 1), 400, 600));
        
        VChain c = new VChain(UV.cSWNW);
        c.add(menu);
        c.add(content);
        setContent(c);
        setBorder(new ViewBorder());
    }

    private void init() {
        apps.removeAll();
        for (String jar : jars) {
            for (Class c : UClass.getClasseNamesInPackage(IRPPViewable.class, null, jar)) {
                apps.insertLast(new VItem(new VString(c.getName()), c) {

                    @Override
                    public void picked(IEvent _e) {
                        try {
                            Class c = (Class) getValue();
                            Class[] argTypes = new Class[]{String[].class};
                            Method m = c.getDeclaredMethod("viewable", argTypes);
                            System.out.println(m);
                            String[] args = new String[0];
                            IView v = (IView) m.invoke(null, (Object) args);
                            content.setView(v);
                        } catch (                IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | IllegalAccessException ex) {
                            Logger.getLogger(RPPHome.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        }

    }

    IView menu() {
        VChain m = new VChain(UV.cEW);
        m.add(UV.border(new VItem("Apps") {

            @Override
            public void picked(IEvent _e) {
                init();
                content.setView(new VPan(new VList(apps, 1), 400, 600));
            }
        }, new MenuItemBorder()));
        m.setBorder(new BuldgeBorder(ViewColor.cThemeHighlight));
        m.spans(UV.cXEW);
        return m;
    }
}
