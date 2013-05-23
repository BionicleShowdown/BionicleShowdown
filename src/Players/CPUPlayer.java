/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Players;

import Characters.PlayableCharacter;
import menu.Costume;

/**
 *
 * @author Inferno
 */
public class CPUPlayer extends Player {

    public CPUPlayer() 
    {
        super("", "CPU");
    }
    
    public CPUPlayer(String playerNumber)
    {
        super(playerNumber, "CPU");
    }
    
    public CPUPlayer(String playerNumber, PlayableCharacter currentCharacter, Costume currentCostume, String currentTeam)
    {
        super(playerNumber, "CPU", currentCharacter, currentCostume, currentTeam);
    }

}
