/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Characters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import menu.Costume;

/**
 *
 * @author Inferno
 */
public class Kopaka extends PlayableCharacter 
{
    // Creates the costume list which will be used by all instances of Tahu (to allow unlockable costumes)
//    public static ArrayList<Costumes> kopakaCostumes = new ArrayList(Arrays.asList(costumeArray));
//    public static ArrayList<Costumes> kopakaTeamCostumes = new ArrayList(Arrays.asList(teamCostumeArray));
    public static List<Costume> kopakaCostumes = Costume.buildCostumeList(initialCostumeNames, "Kopaka");
    
    public Kopaka() 
    {
        super("Kopaka");
        costumeKeys = kopakaCostumes;
//        teamCostumeKeys = kopakaTeamCostumes;
    }
    
    public static void unlockCostume(String unlockedCostume)
    {
        unlockCostume(kopakaCostumes, new Costume(unlockedCostume, "Kopaka"));
    }
    
}
