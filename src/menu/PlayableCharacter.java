/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Inferno
 */
class PlayableCharacter 
{
    String name;
    float weight;
    float speed;
    String symbol; // String because it'll be passed to the GUI as the pathname.
    String model; // String because it'll be passed to the GUI as the pathname.
    Costumes currentCostume = Costumes.Standard; // Costume setter will call .name() and attach the path
    protected final static Costumes[] costumeArray = {Costumes.Standard, Costumes.Alignment, Costumes.Inverse, Costumes.Reverse, Costumes.Special};
    protected final static String[] initialCostumeNames = {"Standard", "Alignment", "Inverse", "Reverse"};//, "Special"};
    protected final static String[] initialTeams = {"Red", "Blue", "Green"};
    protected final static Costumes[] teamCostumeArray = {Costumes.Red, Costumes.LightRed, Costumes.DarkRed, Costumes.Blue, Costumes.LightBlue, Costumes.DarkBlue, Costumes.Green, Costumes.LightGreen, Costumes.DarkGreen};
//    ArrayList<Costumes> costumeKeys = new ArrayList(); // A list which holds all regular costumes
//    ArrayList<Costumes> teamCostumeKeys = new ArrayList(); // A list which holds all team costumes
    List<Costume> costumeKeys = new ArrayList(); // A list which holds all regular costumes
    List<Costume> teamCostumeKeys = new ArrayList(); // A list which holds all team costumes
    
    public PlayableCharacter()
    {
        
    }
    
    public PlayableCharacter(String name)
    {
        this.name = name;
    }
    
//    // Unecessary
//    protected void setTeamCostumeList(Costumes[] teamCostumeArray)
//    {
//        teamCostumeKeys.addAll(Arrays.asList(teamCostumeArray)); // Turns the passed in array into a list
//    }
    
//    // There is likely another way this would need to be done, otherwise the costume would need to be reunlocked for every instantiation
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
