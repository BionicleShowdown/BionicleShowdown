/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

/**
 *
 * @author Inferno
 */
class HumanPlayer extends Player {

    public HumanPlayer() 
    {
        super("", "Human");
    }
    
    public HumanPlayer(String playerNumber)
    {
        super(playerNumber, "Human");
    }
    
    public HumanPlayer(String playerNumber, PlayableCharacter currentCharacter)
    {
        super(playerNumber, "Human", currentCharacter);
    }

}
