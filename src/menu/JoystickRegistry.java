/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import com.jme3.input.Joystick;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Dell Notebook
 */
public class JoystickRegistry 
{
    private static HashMap<String, JoySettings> registry = createJoyRegistry();
    
    public JoystickRegistry()
    {
        
    }
    
    private static HashMap<String, JoySettings> createJoyRegistry()
    {
        return new HashMap();
    }
    
    public static void addJoySettings(Joystick joy)
    {
        if (joy == null)
        {
            return;
        }
        String joyName = joy.getName();
        
        // To prevent overriding old Joysticks every time
        if (registry.containsKey(joyName))
        {
            return;
        }
        
        JoySettings joySettings = new JoySettings(joy);
        registry.put(joyName, joySettings);
    }
    
    public static List<JoySettings> getAllJoySettings()
    {
        List<JoySettings> registeredJoySettings = new ArrayList(registry.size());
        for (JoySettings joy : registry.values()) 
        {
            registeredJoySettings.add(joy);
        }
        return registeredJoySettings;
    }
    
    public static JoySettings getJoySettings(String name)
    {
        if (name == null)
        {
            return null;
        }
        return registry.get(name);
    }
    
    public static void removeJoySettings(Joystick joy)
    {
        if (joy == null)
        {
            return;
        }
        String joyName = joy.getName();
        registry.remove(joyName);
    }
    
    public static List<String> getRegisteredNames()
    {
        List<String> registeredJoys = new ArrayList(registry.size());
        for (String joy : registry.keySet()) 
        {
            registeredJoys.add(joy);
        }
        return registeredJoys;
    }
}
