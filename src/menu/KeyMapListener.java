/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.input.controls.JoyButtonTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import mygame.CompoundInputManager;

/**
 *
 * @author Inferno
 */
public class KeyMapListener implements RawInputListener 
{
//    private InputManager inputManager = PlayerControlMenu.inputManager;
//    private InputManager joyManager = PlayerControlMenu.joyManager;
    private CompoundInputManager compoundManager = PlayerControlMenu.compoundManager;
    private Trigger lastTrigger;
//    private int lastKeyValue = -1;
//    private KeyNames keynames = new KeyNames(); // This is the menu.KeyNames, not the JME one
//    private String lastKeyName = "";
    
    private boolean justActivated = false;

    public KeyMapListener()
    {
        
    }

    public void beginInput() 
    {
        
    }

    public void endInput() 
    {
        
    }

    public void onJoyAxisEvent(JoyAxisEvent evt) 
    {
//        System.out.println("Joy");
//        if (compoundManager != null)
//        {
//            if (compoundManager.hasMapping("Change"))
//            {
//                if (lastTrigger != null)
//                {
//                    compoundManager.deleteTrigger("Change", lastTrigger);
//                }
//            }
//        }
        float deadzone = compoundManager.getAxisDeadZone();
//        System.out.println("DEADZONE: " + deadzone);
        if ((evt.getValue() < 1) && (evt.getValue() > -1))
        {
            return;
        }
        System.out.println(evt.getAxis().getLogicalId());
//        System.out.println("Moved Joystick");
        boolean isNegative;
        String orientation;
        if (evt.getValue() < 0)
        {
            isNegative = true;
            orientation = "Negative";
        }
        else if (evt.getValue() > 0)
        {
            isNegative = false;
            orientation = "Positive";
        }
        else
        {
            return;
        }
        
        lastTrigger = new JoyAxisTrigger(evt.getJoyIndex(), evt.getAxisIndex(), isNegative);
//        lastKeyName = orientation + " " + evt.getAxis().getName();
        
//        System.out.println("Last Key Name: " + lastKeyName);
        System.out.println("Last Trigger Name: " + lastTrigger.getName());
        
        PlayerControlMenu.changeControlTrigger(PlayerControlMenu.currentControl, lastTrigger);
        
//        if (compoundManager != null)
//        {
//           compoundManager.addMapping("Change", lastTrigger);
//        }
    }

    public void onJoyButtonEvent(JoyButtonEvent evt) 
    {
        if (!evt.isPressed())
        {
            if (justActivated)
            {
                justActivated = false;
                return;
            }
            if (compoundManager != null)
            {
                if (compoundManager.hasMapping("Change"))
                {
                    if (lastTrigger != null)
                    {
                        compoundManager.deleteTrigger("Change", lastTrigger);
                    }
                }
            }
            
            lastTrigger = new JoyButtonTrigger(evt.getJoyIndex(), evt.getButtonIndex());
//            lastKeyName = JoystickRegistry.getJoySettings(evt.getButton().getJoystick().getName()).getButtonName(evt.getButtonIndex());
//            System.out.println("Button Made: " + lastKeyName);
            
            if (compoundManager != null)
            {
            compoundManager.addMapping("Change", lastTrigger);
            }
        }
    }

    public void onMouseMotionEvent(MouseMotionEvent evt) 
    {
        
    }

    public void onMouseButtonEvent(MouseButtonEvent evt) 
    {
        
    }

    public void onKeyEvent(KeyInputEvent evt) 
    {
//        System.out.println("ON KEY INPUT MANAGE: " + compoundManager);
        
        if (justActivated)
        {
            justActivated = false;
            return;
        }
        
        if (compoundManager != null)
        {
            if (compoundManager.hasMapping("Change"))
            {
                if (lastTrigger != null && lastTrigger instanceof KeyTrigger)
                {
                    compoundManager.deleteTrigger("Change", lastTrigger);
                }
            }
        }
        
        lastTrigger = new KeyTrigger(evt.getKeyCode());
//        lastKeyValue = evt.getKeyCode();
//        System.out.println(lastKeyValue);
//        System.out.println(keynames.getName(lastKeyValue));
//        lastKeyName = keynames.getName(lastKeyValue);
//        System.out.println("Key Made: " + lastKeyName);
//        PlayerControlMenu.isWaiting = false;
        
        if (compoundManager != null)
        {
           compoundManager.addMapping("Change", lastTrigger);
        }
    }

    public void onTouchEvent(TouchEvent evt) 
    {
        
    }
    
    public Trigger getLastTrigger()
    {
        return lastTrigger;
    }
    
//    public int getLastKeyValue()
//    {
//        return lastKeyValue;
//    }
//    
//    public String getLastKeyName()
//    {
//        return lastKeyName;
//    }
    
    public void justActivated()
    {
        justActivated = true;
    }
    
    public void reset()
    {
        lastTrigger = null;
//        lastKeyValue = -1;
//        lastKeyName = "";
    }

}
