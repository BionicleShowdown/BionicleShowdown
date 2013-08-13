/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

import com.jme3.input.RawInputListener;
import com.jme3.input.controls.JoyButtonTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;

/**
 *
 * @author Inferno
 */
public class KeyMapListener implements RawInputListener 
{
    private KeyTrigger lastTrigger;
    private int lastKeyValue = -1;

    public void beginInput() 
    {
        
    }

    public void endInput() 
    {
        
    }

    public void onJoyAxisEvent(JoyAxisEvent evt) 
    {
        
    }

    public void onJoyButtonEvent(JoyButtonEvent evt) 
    {
        
    }

    public void onMouseMotionEvent(MouseMotionEvent evt) 
    {
        
    }

    public void onMouseButtonEvent(MouseButtonEvent evt) 
    {
        
    }

    public void onKeyEvent(KeyInputEvent evt) 
    {
        lastTrigger = new KeyTrigger(evt.getKeyCode());
        lastKeyValue = evt.getKeyCode();
    }

    public void onTouchEvent(TouchEvent evt) 
    {
        
    }
    
    public Trigger getLastTrigger()
    {
        return lastTrigger;
    }
    
    public int getLastKeyValue()
    {
        return lastKeyValue;
    }

}
