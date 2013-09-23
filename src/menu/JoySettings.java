/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import com.jme3.input.Joystick;
import com.jme3.input.JoystickAxis;
import com.jme3.input.JoystickButton;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dell Notebook
 */
public class JoySettings 
{
    private String joyName = "";
    private final String originalName;
    
    private final List<String> originalPositiveAxisNames = new ArrayList();
    private final List<String> originalNegativeAxisNames = new ArrayList();
    private final List<String> originalButtonNames = new ArrayList();
    
    private List<String> positiveAxisNames = new ArrayList();
    private List<String> negativeAxisNames = new ArrayList();
    private List<String> buttonNames = new ArrayList();
    
    public JoySettings()
    {
        originalName = "";
    }
    
    // Make list of axis indexes and button indexes
    // Assign each index a name
    // Allow name to be retrieved using "Axis"/"Button" and index
    
    
    public JoySettings(Joystick joy)
    {
        joyName = joy.getName();
        originalName = joy.getName();
        
        for (JoystickAxis axis : joy.getAxes()) 
        {
            originalPositiveAxisNames.add("Positive " + axis.getName());
            originalNegativeAxisNames.add("Negative " + axis.getName());
            positiveAxisNames.add("Positive " + axis.getName());
            negativeAxisNames.add("Negative " + axis.getName());
        }
        for (JoystickButton button : joy.getButtons())
        {
            originalButtonNames.add(button.getName());
            buttonNames.add(button.getName());
        }
    }
    
    public String getJoyName()
    {
        return joyName;
    }
    
    public String getOriginalName()
    {
        return originalName;
    }
    
    public String getAxisName(int index, boolean isPositive)
    {
        if (isPositive)
        {
            return positiveAxisNames.get(index);
        }
        else
        {
            return negativeAxisNames.get(index);
        }
    }
    
    public String getButtonName(int index)
    {
        return buttonNames.get(index);
    }
    
    public List<String> getControlNames()
    {
        List<String> controlList = new ArrayList();
        
        for (int i=0; i < positiveAxisNames.size(); i++)
        {
            controlList.add(positiveAxisNames.get(i));
            controlList.add(negativeAxisNames.get(i));
        }
        for (int i=0; i < buttonNames.size(); i++)
        {
            controlList.add(buttonNames.get(i));
        }
        return controlList;
    }
    
    public void resetSettings()
    {
        positiveAxisNames.clear();
        negativeAxisNames.clear();
        buttonNames.clear();
        
        for (String name : originalPositiveAxisNames)
        {
            positiveAxisNames.add(name);
        }
        for (String name : originalNegativeAxisNames)
        {
            negativeAxisNames.add(name);
        }
        for (String name : originalButtonNames)
        {
            buttonNames.add(name);
        }
        joyName = originalName;
    }
    
    public void setAxisName(int index, boolean isPositive, String newName)
    {
        if (isPositive)
        {
            positiveAxisNames.set(index, newName);
        }
        else
        {
            negativeAxisNames.set(index, newName);
        }
    }
    
    public void setButtonName(int index, String newName)
    {
        buttonNames.set(index, newName);
    }
    
    public void setJoyName(String newName)
    {
        joyName = newName;
    }
    
    
    public void setControlName(int index, String newName)
    {
        // Even numbers less than twice the length of the Joy Axis list are positive
        // Odd are negative
        // Anything after is a button
        
        // 4 Axes, 4 Buttons:
        // 0, 2, 4, 6 are Pos; 0, 1, 2, 3
        // 1, 3, 5, 7 are Neg; 0, 1, 2, 3
        // 8, 9, 10, 11 are Buttons; 0, 1, 2, 3
        
        // x < 4*2 --> x < 8
        // if (x % 2) = 0 --> i = x / 2
        // if !(x % 2) = 0 --> i = x / 2
        // if x > 4*2 --> x > 8
        // i = x - 8
        
        boolean isPositive;
        if (index < (positiveAxisNames.size() * 2))
        {
            if ((index % 2) == 0)
            {
                isPositive = true;
            }
            else 
            {
                isPositive = false;
            }
            setAxisName((index / 2), isPositive, newName);
        }
        else
        {
            setButtonName((index - (positiveAxisNames.size() * 2)), newName);
        }
    }
    
    
    @Override
    public String toString()
    {
        return joyName;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof JoySettings)
        {
            JoySettings joySet = (JoySettings) o;
            if (originalName.equals(joySet.getOriginalName()))
            {
                return true;
            }
        }
        return false;
    }
}
