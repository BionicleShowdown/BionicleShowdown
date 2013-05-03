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
class Tahu extends PlayableCharacter 
{
    // Creates the costume list which will be used by all instances of Tahu (to allow unlockable costumes)
//    public static ArrayList<Costumes> tahuCostumes = new ArrayList(Arrays.asList(costumeArray));
    public static List<Costume> tahuCostumes = Costume.buildCostumeList(initialCostumeNames, "Tahu");
//    public static List<Costumes> tahuTeamCostumes = new ArrayList(Arrays.asList(teamCostumeArray));
    
    public Tahu() 
    {
        super("Tahu");
        costumeKeys = tahuCostumes;
//        teamCostumeKeys = tahuTeamCostumes;
    }
    
    public static void unlockCostume(String unlockedCostume)
    {
        unlockCostume(tahuCostumes, new Costume(unlockedCostume, "Tahu"));
    }
    
}
