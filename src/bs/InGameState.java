
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs;

import com.jme3.app.state.AbstractAppState;

/**
 *
 * @author Inferno
 */
abstract class InGameState extends AbstractAppState 
{
    public InGameState()
    {
        
    }

     // This is a procedure that will be filled by those that use it.
    public abstract void adjustStock(int stock, String number);
}
//>>>>>>> branch 'master' of https://github.com/BionicleShowdown/BionicleShowdown.git
