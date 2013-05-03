/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Inferno
 */
class PlayableCharacter 
{
    String name;
    protected float weight;
    protected float speed;
    protected String symbol; // String because it'll be passed to the GUI as the pathname.
    protected String model; // String because it'll be passed to the GUI as the pathname.
    protected final static Costumes[] costumeArray = {Costumes.Standard, Costumes.Alignment, Costumes.Inverse, Costumes.Reverse, Costumes.Special};
    protected final static String[] initialCostumeNames = {"Standard", "Alignment", "Inverse", "Reverse"};//, "Special"};
    protected final static String[] initialTeams = {"Red", "Blue", "Green"};
    protected final static Costumes[] teamCostumeArray = {Costumes.Red, Costumes.LightRed, Costumes.DarkRed, Costumes.Blue, Costumes.LightBlue, Costumes.DarkBlue, Costumes.Green, Costumes.LightGreen, Costumes.DarkGreen};
//    ArrayList<Costumes> costumeKeys = new ArrayList(); // A list which holds all regular costumes
//    ArrayList<Costumes> teamCostumeKeys = new ArrayList(); // A list which holds all team costumes
    public List<Costume> costumeKeys = new ArrayList(); // A list which holds all regular costumes
    public List<Costume> teamCostumeKeys = new ArrayList(); // A list which holds all team costumes
    public static HashMap<String, List<String>> teamCostumes = Team.buildTeamMap(initialTeams);
    
    public PlayableCharacter()
    {
        
    }
    
    public PlayableCharacter(String name)
    {
        this.name = name;
    }
    
    public float getWeight()
    {
        return weight;
    }
    
    public float getSpeed()
    {
        return speed;
    }
    
    public String getModel()
    {
        return model;
    }
    
    public String getSymbol()
    {
        return symbol;
    }
    
    protected static void unlockCostume(List<Costume> costumeList, Costume unlockedCostume)
    {
        if (costumeList.contains(unlockedCostume))
        {
            return;
        }
        costumeList.add(unlockedCostume);
    }
    
    @Override
    public String toString()
    {
        return this.name;
    }
    
    
}
