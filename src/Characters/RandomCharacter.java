/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Characters;

import java.util.List;
import menu.Costume;

/**
 *
 * @author Inferno
 */
public class RandomCharacter extends PlayableCharacter 
{
    public static List<Costume> randomCostumes = Costume.buildCostumeList(initialCostumeNames, "Random");

    public RandomCharacter() 
    {
        super("Random"); //Random has a Random Character and a Random Costume
        costumeKeys = randomCostumes;
    }
    
}
