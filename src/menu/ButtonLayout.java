/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

/**
 * This is used when Key Navigation needs to be done on a menu.
 * 
 * For Main Menu: 0 = Fight, 1 = Train, 2 = Extras, and 3 = Options
 * @author Inferno
 */
class ButtonLayout 
{
    private byte identity, left, right, up, down;

    public ButtonLayout(byte identity, byte left, byte right, byte up, byte down) 
    {
        this.identity = identity;
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
    }
    
    /**
     * This Button's Identifier
     * @return identity
     */
    public byte Identity()
    {
        return identity;
    }
    
    /**
     * The Button on the Left's Identifier
     * @return left's identity
     */
    public byte Left()
    {
        return left;
    }
    
    /**
     * The Button on the Right's Identifier
     * @return right's identity
     */
    public byte Right()
    {
        return right;
    }
    
    /**
     * The Button above this one's Identifier
     * @return above's identity
     */
    public byte Up()
    {
        return up;
    }
    
    /**
     * The Button below this one's Identifier
     * @return below's identity
     */
    public byte Down()
    {
        return down;
    }
}
