package com.jonathancolt.nicity.view.rpp;

import com.jonathancolt.nicity.core.collection.CSet;
import com.jonathancolt.nicity.core.collection.keyed.KeyedValue;
import com.jonathancolt.nicity.view.border.ViewBorder;
import com.jonathancolt.nicity.view.interfaces.IView;

/**
 * 
 * @author jonathan
 */
public class RPPViews {

    /**
     * 
     */
    public RPPViews() {
    }
    private CSet<KeyedValue<String, RPPWindow>> views = new CSet<>();

    /**
     * 
     * @param _view
     * @param _key
     */
    public void register(IView _view, String _key) {
        _view.setBorder(new ViewBorder());
        RPPWindow v = new RPPWindow(_view);
        KeyedValue.add(views, _key, v);
    }

    /**
     * 
     * @param _key
     * @return
     */
    public RPPWindow view(long _who,String _key) {
        
        RPPWindow view = KeyedValue.get(views,_key);
        if (view == null) {
            register(new RPPHome("/home/jonathan/Dropbox/Code/nicity-goal/target/nicity-goal-1.0-SNAPSHOT.jar"),_key);
            view =  KeyedValue.get(views,_key);
        }
        return view;
    }

    /**
     * 
     * @param _key
     */
    public void release(String _key) {
        KeyedValue.remove(views, _key);
    }
}
