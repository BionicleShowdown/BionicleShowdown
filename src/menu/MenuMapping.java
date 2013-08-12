/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import mygame.Main;

/**
 *
 * @author Inferno
 */
public class MenuMapping implements NiftyInputMapping
{
    public NiftyInputEvent convert(KeyboardInputEvent inputEvent) 
    {
        if (inputEvent.isKeyDown()) 
        {
            return handleKeyDownEvent(inputEvent);
        } 
        else 
        {
            return handleKeyUpEvent(inputEvent);
        }
    }

    private NiftyInputEvent handleKeyDownEvent(KeyboardInputEvent inputEvent) 
    {
        if (inputEvent.getKey() == KeyboardInputEvent.KEY_RETURN)
        {
            return NiftyInputEvent.Activate;
        }
        else if (inputEvent.getKey() == KeyboardInputEvent.KEY_BACK)
        {
            return NiftyInputEvent.Backspace;
        }
        else if (inputEvent.getKey() == KeyboardInputEvent.KEY_A)
        {
            return NiftyInputEvent.MoveCursorLeft;
        }
        else if (inputEvent.getKey() == KeyboardInputEvent.KEY_D)
        {
            return NiftyInputEvent.MoveCursorRight;
        }
        else if (inputEvent.getKey() == KeyboardInputEvent.KEY_W)
        {
            return NiftyInputEvent.MoveCursorUp;
        }
        else if (inputEvent.getKey() == KeyboardInputEvent.KEY_S)
        {
            return NiftyInputEvent.MoveCursorDown;
        }
        else
        {
            return null;
        }
    }

    private NiftyInputEvent handleKeyUpEvent(KeyboardInputEvent inputEvent) 
    {
        return null;
    }

}
