/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.input.controls.JoyButtonTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.input.event.InputEvent;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.Vector2f;
import menu.KeyMapListener;

/**
 *
 * @author Dell Notebook
 */
public class CompoundInputManager
{
    private InputManager inputManager;
    private InputManager joyManager;
    
    public CompoundInputManager(InputManager inputManager, InputManager joyManager)
    {
        this.inputManager = inputManager;
        this.joyManager = joyManager;
    }
    
    public InputManager getInputManager()
    {
        return inputManager;
    }
    
    public InputManager getJoyManager()
    {
        return joyManager;
    }

    public void addMapping(String mappingName, Trigger... triggers) 
    {
        for (Trigger trigger : triggers)
        {
            if (trigger instanceof MouseButtonTrigger || trigger instanceof MouseAxisTrigger || trigger instanceof KeyTrigger)
            {
                inputManager.addMapping(mappingName, trigger);
            }
            else if (trigger instanceof JoyAxisTrigger || trigger instanceof JoyButtonTrigger)
            {
                joyManager.addMapping(mappingName, trigger);
            }
        }
    }
    
    public void addListener(InputListener listener, String... mappingNames)
    {
        inputManager.addListener(listener, mappingNames);
        joyManager.addListener(listener, mappingNames);
    }
    
    public void removeListener(InputListener listener)
    {
        inputManager.removeListener(listener);
        joyManager.removeListener(listener);
    }

    public Joystick[] getJoysticks() 
    {
        return joyManager.getJoysticks();
    }
    
    public float getAxisDeadZone()
    {
        return joyManager.getAxisDeadZone();
    }

    public void setAxisDeadZone(float deadzone) 
    {
        inputManager.setAxisDeadZone(deadzone);
        joyManager.setAxisDeadZone(deadzone);
    }

    public void addRawInputListener(KeyMapListener listener) 
    {
        inputManager.addRawInputListener(listener);
        joyManager.addRawInputListener(listener);
    }
    
    public void removeRawInputListener(KeyMapListener listener)
    {
        inputManager.removeRawInputListener(listener);
        joyManager.removeRawInputListener(listener);
    }

    public void update(float tpf) 
    {
        inputManager.update(tpf);
        joyManager.update(tpf);
    }

    public void clearMappings() 
    {
        inputManager.clearMappings();
        joyManager.clearMappings();
    }

    public void clearRawInputListeners() 
    {
        inputManager.clearRawInputListeners();
        joyManager.clearRawInputListeners();
    }

    public void deleteMapping(String mappingName) 
    {
        if (inputManager.hasMapping(mappingName))
        {
            inputManager.deleteMapping(mappingName);
        }
        if (joyManager.hasMapping(mappingName))
        {
            joyManager.deleteMapping(mappingName);
        }
    }

    public void deleteTrigger(String mappingName, Trigger trigger) 
    {
        if (hasMapping(mappingName) == false)
        {
            return;
        }
        if (trigger instanceof MouseButtonTrigger || trigger instanceof MouseAxisTrigger || trigger instanceof KeyTrigger)
        {
            inputManager.deleteTrigger(mappingName, trigger);
        }
        else if (trigger instanceof JoyAxisTrigger || trigger instanceof JoyButtonTrigger)
        {
            joyManager.deleteTrigger(mappingName, trigger);
        }
    }

    public boolean hasMapping(String mappingName) 
    {
        if (inputManager.hasMapping(mappingName) || joyManager.hasMapping(mappingName))
        {
            return true;
        }
        return false;
    }

    public Vector2f getCursorPosition() 
    {
        return inputManager.getCursorPosition();
    }

    public boolean getSimulateMouse() 
    {
        return inputManager.getSimulateMouse();
    }

    public boolean isCursorVisible() 
    {
        return inputManager.isCursorVisible();
    }

    public void reset() 
    {
        inputManager.reset();
        joyManager.reset();
    }

    public void setCursorVisible(boolean visible) 
    {
        inputManager.setCursorVisible(visible);
        joyManager.setCursorVisible(visible);
    }

    public void setMouseCursor(JmeCursor jmeCursor) 
    {
        inputManager.setMouseCursor(jmeCursor);
        joyManager.setMouseCursor(jmeCursor);
    }

    public void setSimulateKeyboard(boolean value) 
    {
        inputManager.setSimulateKeyboard(value);
    }

    public void setSimulateMouse(boolean value) 
    {
        inputManager.setSimulateMouse(value);
    }

    public void beginInput() 
    {
        inputManager.beginInput();
        joyManager.beginInput();
    }
    
    public void endInput()
    {
        inputManager.endInput();
        joyManager.endInput();
    }

    public void onJoyAxisEvent(JoyAxisEvent evt) 
    {
        joyManager.onJoyAxisEvent(evt);
    }
    
    public void onJoyButtonEvent(JoyButtonEvent evt)
    {
        joyManager.onJoyButtonEvent(evt);
    }
    
    public void onKeyEvent(KeyInputEvent evt)
    {
        inputManager.onKeyEvent(evt);
    }
    
    public void onMouseButtonEvent(MouseButtonEvent evt)
    {
        inputManager.onMouseButtonEvent(evt);
    }
    
    public void onMouseMotionEvent(MouseMotionEvent evt)
    {
        inputManager.onMouseMotionEvent(evt);
    }

    public void onTouchEvent(TouchEvent evt) 
    {
        inputManager.onTouchEvent(evt);
    }

    public void onTouchEventQueued(TouchEvent evt) 
    {
        inputManager.onTouchEventQueued(evt);
    }
    
}
