/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

/**
 *
 * @author Inferno
 */
class NullPlayer extends Player {

    public NullPlayer()
    {
        super("", "Null");
    }
    
    public NullPlayer(String playerNumber) 
    {
        super(playerNumber, "Null");
    }

}
