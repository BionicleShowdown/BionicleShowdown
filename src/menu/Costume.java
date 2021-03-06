/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Inferno
 */
public class Costume 
{
    public String name;
    public String owner;
    
    public Costume()
    {
        
    }
    
    public Costume(String name)
    {
        this.name = name;
    }
    
    public Costume(String name, String owner)
    {
        this.name = name;
        this.owner = owner;
    }
    
    public static List<Costume> buildCostumeList(String[] costumeNames, String owner)
    {
        List<Costume> costumeList = new ArrayList(costumeNames.length);
        for (int i = 0; i < costumeNames.length; i++) 
        {
            costumeList.add(new Costume(costumeNames[i], owner));
        }
        return costumeList;
    }
    
    @Override
    public boolean equals(Object compCostume)
    {
        Costume otherCostume = (Costume) compCostume;
        if (otherCostume == null)
        {
            return false;
        }
        if ((name == null) || (owner == null))
        {
            return false;
        }
        return ((name.equals(otherCostume.name)) && (owner.equals(otherCostume.owner)));
    }
    
    @Override
    public String toString()
    {
        return owner + "/" + name;
    }
}
