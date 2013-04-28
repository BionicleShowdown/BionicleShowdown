/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

/**
 *
 * @author Inferno
 */
class CPUPlayer extends Player {

    public CPUPlayer() 
    {
        super("", "CPU");
    }
    
    public CPUPlayer(String playerNumber)
    {
        super(playerNumber, "CPU");
    }
    
    public CPUPlayer(String playerNumber, PlayableCharacter currentCharacter)
    {
        super(playerNumber, "CPU", currentCharacter);
    }

}
