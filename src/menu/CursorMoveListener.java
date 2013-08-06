/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.FastMath;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.tools.SizeValue;

/**
 *
 * @author Inferno
 */
public class CursorMoveListener implements RawInputListener 
{
    private Element cursor;
    private AppSettings settings;
    private float x=400, y=400;
    private NiftyMouse nm;
    
    public CursorMoveListener()
    {
        
    }
    
    public CursorMoveListener(AppSettings settings, NiftyMouse nm)
    {
        this.settings = settings;
        this.nm = nm;
    }
    
    public CursorMoveListener(Element cursor, AppSettings settings)
    {
        this.cursor = cursor;
        this.settings = settings;
    }
    
//    public CursorMoveListener(Picture cursor, AppSettings settings)
//    {
//        this.cursor = cursor;
//        this.settings = settings;
//    }
    
    public void beginInput() 
    {
        
    }

    public void endInput() {
    }

    public void onJoyAxisEvent(JoyAxisEvent evt) 
    {
        String axn = evt.getAxis().getName();
//        System.out.println("Axis Name: " + axn);
        System.out.println(x);
        if (axn.equalsIgnoreCase("X"))
        {
            x += evt.getValue();
        }
        
        x = FastMath.clamp(x, 0, settings.getWidth());
        
        nm.setMousePosition((int) x, (int) y);
    }

    public void onJoyButtonEvent(JoyButtonEvent evt) 
    {
        
    }

    public void onMouseMotionEvent(MouseMotionEvent evt) 
    {
//        System.out.println("Moved Mouse");
//        x = evt.getX() - 15;
//        y = settings.getHeight() - evt.getY() - 15;
//        System.out.println(x + ", " + y);
//        
//        x = FastMath.clamp(x, 0, settings.getWidth() - 30);
//        y = FastMath.clamp(y, 0, settings.getHeight() - 30);
//        
//        cursor.setConstraintX(new SizeValue((int) x + "px"));
//        cursor.getParent().layoutElements();
//        cursor.setConstraintY(new SizeValue((int) y + "px"));
//        cursor.getParent().layoutElements();
    }

    public void onMouseButtonEvent(MouseButtonEvent evt) 
    {
        
    }

    public void onKeyEvent(KeyInputEvent evt) 
    {
        
    }

    public void onTouchEvent(TouchEvent evt) 
    {
        
    }
}
