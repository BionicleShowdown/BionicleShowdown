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
    public static List<Costume> tahuCostumes = Costume.buildCostumeList(initialCostumeNames, "Tahu");
    
    public Tahu() 
    {
        super("Tahu");
        costumeKeys = tahuCostumes;
        weight = 90;
        fallSpeed = 30;
        jumpSpeed = 30;
        moveSpeed = 2;
    }
    
    public static void unlockCostume(String unlockedCostume)
    {
        unlockCostume(tahuCostumes, new Costume(unlockedCostume, "Tahu"));
    }
    
}
