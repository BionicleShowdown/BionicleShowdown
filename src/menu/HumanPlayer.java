/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

import Characters.PlayableCharacter;

/**
 *
 * @author Inferno
 */
public class HumanPlayer extends Player 
{

    public HumanPlayer() 
    {
        super("", "Human");
    }
    
    public HumanPlayer(String playerNumber)
    {
        super(playerNumber, "Human");
    }
    
    public HumanPlayer(String playerNumber, PlayableCharacter currentCharacter, Costume currentCostume, String currentTeam)
    {
        super(playerNumber, "Human", currentCharacter, currentCostume, currentTeam);
    }

}
