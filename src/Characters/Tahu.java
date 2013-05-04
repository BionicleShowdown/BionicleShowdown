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
public class Tahu extends PlayableCharacter 
{
    // Creates the costume list which will be used by all instances of Tahu (to allow unlockable costumes)
//    public static ArrayList<Costumes> tahuCostumes = new ArrayList(Arrays.asList(costumeArray));
    public static List<Costume> tahuCostumes = Costume.buildCostumeList(initialCostumeNames, "Tahu");
//    public static List<Costumes> tahuTeamCostumes = new ArrayList(Arrays.asList(teamCostumeArray));
    
    public Tahu() 
    {
        super("Tahu");
        costumeKeys = tahuCostumes;
        modelPath = "Models/Characters/Tahu/Tahu.mesh.j3o";
        weight = 90;
        fallSpeed = 30;
        jumpSpeed = 30;
//        teamCostumeKeys = tahuTeamCostumes;
    }
    
    public static void unlockCostume(String unlockedCostume)
    {
        unlockCostume(tahuCostumes, new Costume(unlockedCostume, "Tahu"));
    }
    
}
