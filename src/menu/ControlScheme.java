/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import java.util.HashMap;

/**
 *
 * @author Inferno
 */
public class ControlScheme 
{
    public static final HashMap<String, ControlScheme> defaultControlSchemes = createDefaults();
    public static HashMap<String, ControlScheme> savedControlSchemes = new HashMap();
    
    private Trigger acceptTrigger;
    private Trigger backTrigger;
    private Trigger pauseTrigger;
    private Trigger leftTrigger;
    private Trigger rightTrigger;
    private Trigger upTrigger;
    private Trigger downTrigger;
    private Trigger jumpTrigger;
    private Trigger attackTrigger;
    private Trigger specialTrigger;
    private Trigger grabTrigger;
    private Trigger shieldTrigger;
    private Trigger taunt1Trigger;
    private Trigger taunt2Trigger;
    private Trigger taunt3Trigger;
    private Trigger leftrightTrigger;

    
//    private String acceptName;
//    private String backName;
//    private String pauseName;
//    private String leftName;
//    private String rightName;
//    private String upName;
//    private String downName;
//    private String jumpName;
//    private String attackName;
//    private String specialName;
//    private String grabName;
//    private String shieldName;
//    private String taunt1Name;
//    private String taunt2Name;
//    private String taunt3Name;
    
    public ControlScheme()
    {
        
    }
    
    public ControlScheme(Trigger[] ta)
    {
        acceptTrigger = ta[0];
        backTrigger = ta[1];
        pauseTrigger = ta[2];
        leftTrigger = ta[3];
        rightTrigger = ta[4];
        upTrigger = ta[5];
        downTrigger = ta[6];
        jumpTrigger = ta[7];
        leftrightTrigger = ta[8];
        attackTrigger = ta[9];
        specialTrigger = ta[10];
        grabTrigger = ta[11];
        shieldTrigger = ta[12];
        taunt1Trigger = ta[13];
        taunt2Trigger = ta[14];
        taunt3Trigger = ta[15];
    }
    
//    public ControlScheme(Trigger[] ta, String[] sa)
//    {
//        this(ta);
//        acceptName = sa[0];
//        backName = sa[1];
//        pauseName = sa[2];
//        leftName = sa[3];
//        rightName = sa[4];
//        upName = sa[5];
//        downName = sa[6];
//        jumpName = sa[7];
//        attackName = sa[8];
//        specialName = sa[9];
//        grabName = sa[10];
//        shieldName = sa[11];
//        taunt1Name = sa[12];
//        taunt2Name = sa[13];
//        taunt3Name = sa[14];
//    }
    
    // A Trigger for all Controls
    // A Static list of Default Controls
    // A Static list of Saved Controls
    
    // <editor-fold defaultstate="collapsed" desc="Trigger Getters">
    public Trigger getAccept()
    {
        return acceptTrigger;
    }
    public Trigger getBack()
    {
        return backTrigger;
    }
    public Trigger getPause()
    {
        return pauseTrigger;
    }
    public Trigger getLeft()
    {
        return leftTrigger;
    }
    public Trigger getRight()
    {
        return rightTrigger;
    }
    public Trigger getUp()
    {
        return upTrigger;
    }
    public Trigger getDown()
    {
        return downTrigger;
    }
    public Trigger getJump()
    {
        return jumpTrigger;
    }
    public Trigger getAttack()
    {
        return attackTrigger;
    }
    public Trigger getSpecial()
    {
        return specialTrigger;
    }
    public Trigger getGrab()
    {
        return grabTrigger;
    }
    public Trigger getShield()
    {
        return shieldTrigger;
    }
    public Trigger getTaunt1()
    {
        return taunt1Trigger;
    }
    public Trigger getTaunt2()
    {
        return taunt2Trigger;
    }
    public Trigger getTaunt3()
    {
        return taunt3Trigger;
    }
    public Trigger getLeftRight(){
        return leftrightTrigger;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Trigger Setters">
    public void setAccept(Trigger accept)
    {
        this.acceptTrigger = accept;
    }
    public void setBack(Trigger back)
    {
        this.backTrigger = back;
    }
    public void setPause(Trigger pause)
    {
        this.pauseTrigger = pause;
    }
    public void setLeft(Trigger left)
    {
        this.leftTrigger = left;
    }
    public void setRight(Trigger right)
    {
        this.rightTrigger = right;
    }
    public void setUp(Trigger up)
    {
        this.upTrigger = up;
    }
    public void setDown(Trigger down)
    {
        this.downTrigger = down;
    }
    public void setJump(Trigger jump)
    {
        this.jumpTrigger = jump;
    }
    public void setAttack(Trigger attack)
    {
        this.attackTrigger = attack;
    }
    public void setSpecial(Trigger special)
    {
        this.specialTrigger = special;
    }
    public void setGrab(Trigger grab)
    {
        this.grabTrigger = grab;
    }
    public void setShield(Trigger shield)
    {
        this.shieldTrigger = shield;
    }
    public void setTaunt1(Trigger taunt1)
    {
        this.taunt1Trigger = taunt1;
    }
    public void setTaunt2(Trigger taunt2)
    {
        this.taunt2Trigger = taunt2;
    }
    public void setTaunt3(Trigger taunt3)
    {
        this.taunt3Trigger = taunt3;
    }
    public void setLeftRight(Trigger leftright){
        this.leftrightTrigger = leftright;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Name Getters">
//    public String getAcceptName()
//    {
//        return acceptName;
//    }
//    public String getBackName()
//    {
//        return backName;
//    }
//    public String getPauseName()
//    {
//        return pauseName;
//    }
//    public String getLeftName()
//    {
//        return leftName;
//    }
//    public String getRightName()
//    {
//        return rightName;
//    }
//    public String getUpName()
//    {
//        return upName;
//    }
//    public String getDownName()
//    {
//        return downName;
//    }
//    public String getJumpName()
//    {
//        return jumpName;
//    }
//    public String getAttackName()
//    {
//        return attackName;
//    }
//    public String getSpecialName()
//    {
//        return specialName;
//    }
//    public String getGrabName()
//    {
//        return grabName;
//    }
//    public String getShieldName()
//    {
//        return shieldName;
//    }
//    public String getTaunt1Name()
//    {
//        return taunt1Name;
//    }
//    public String getTaunt2Name()
//    {
//        return taunt2Name;
//    }
//    public String getTaunt3Name()
//    {
//        return taunt3Name;
//    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Name Setters">
//    public void setAccept(String accept)
//    {
//        this.acceptName = accept;
//    }
//    public void setBack(String back)
//    {
//        this.backName = back;
//    }
//    public void setPause(String pause)
//    {
//        this.pauseName = pause;
//    }
//    public void setLeft(String left)
//    {
//        this.leftName = left;
//    }
//    public void setRight(String right)
//    {
//        this.rightName = right;
//    }
//    public void setUp(String up)
//    {
//        this.upName = up;
//    }
//    public void setDown(String down)
//    {
//        this.downName = down;
//    }
//    public void setJump(String jump)
//    {
//        this.jumpName = jump;
//    }
//    public void setAttack(String attack)
//    {
//        this.attackName = attack;
//    }
//    public void setSpecial(String special)
//    {
//        this.specialName = special;
//    }
//    public void setGrab(String grab)
//    {
//        this.grabName = grab;
//    }
//    public void setShield(String shield)
//    {
//        this.shieldName = shield;
//    }
//    public void setTaunt1(String taunt1)
//    {
//        this.taunt1Name = taunt1;
//    }
//    public void setTaunt2(String taunt2)
//    {
//        this.taunt2Name = taunt2;
//    }
//    public void setTaunt3(String taunt3)
//    {
//        this.taunt3Name = taunt3;
//    }
    // </editor-fold>

    private static HashMap<String, ControlScheme> createDefaults() 
    {
        HashMap<String, ControlScheme> tempMap = new HashMap();
        Trigger[] p1 = {new KeyTrigger(KeyInput.KEY_RETURN), new KeyTrigger(KeyInput.KEY_BACK), new KeyTrigger(KeyInput.KEY_RBRACKET), 
                        new KeyTrigger(KeyInput.KEY_A), new KeyTrigger(KeyInput.KEY_D), new KeyTrigger(KeyInput.KEY_W), new KeyTrigger(KeyInput.KEY_S),
                        new KeyTrigger(KeyInput.KEY_LSHIFT), new KeyTrigger(KeyInput.KEY_Z), new KeyTrigger(KeyInput.KEY_O), new KeyTrigger(KeyInput.KEY_P), new KeyTrigger(KeyInput.KEY_I),
                        new KeyTrigger(KeyInput.KEY_LBRACKET), new KeyTrigger(KeyInput.KEY_1), new KeyTrigger(KeyInput.KEY_2), new KeyTrigger(KeyInput.KEY_3)};
//        String[] p1s = {"Return", "Backspace", "Right Bracket", "A", "D", "W", "S", "Left Shift", "O", "P", "I", "Left Bracket", "1", "2", "3"};
        tempMap.put("Player 1 Defaults", new ControlScheme(p1));
        
        Trigger[] p2 = {new KeyTrigger(KeyInput.KEY_END), new KeyTrigger(KeyInput.KEY_RSHIFT), new KeyTrigger(KeyInput.KEY_MINUS), 
                        new KeyTrigger(KeyInput.KEY_LEFT), new KeyTrigger(KeyInput.KEY_RIGHT), new KeyTrigger(KeyInput.KEY_UP), new KeyTrigger(KeyInput.KEY_DOWN),
                        new KeyTrigger(KeyInput.KEY_RMETA), new KeyTrigger(KeyInput.KEY_RSHIFT), new KeyTrigger(KeyInput.KEY_NUMPAD5), new KeyTrigger(KeyInput.KEY_NUMPAD6), new KeyTrigger(KeyInput.KEY_NUMPAD4),
                        new KeyTrigger(KeyInput.KEY_ADD), new KeyTrigger(KeyInput.KEY_NUMPAD7), new KeyTrigger(KeyInput.KEY_NUMPAD8), new KeyTrigger(KeyInput.KEY_NUMPAD9)};
        
//        String[] p2s = {"End", "Right Shift", "Numpad Minus", "Left Arrow", "Right Arrow", "Up Arrow", "Down Arrow", "Right Meta", "Numpad 5", 
//                        "Numpad 6", "Numpad 4", "Numpad Plus", "Numpad 7", "Numpad 8", "Numpad 9"};
        
        tempMap.put("Player 2 Defaults", new ControlScheme(p2));
        
        return tempMap;
    }
    
    
    public static ControlScheme copyDefault(String schemeName)
    {
        return defaultControlSchemes.get(schemeName).duplicate();
    }
    
    public static ControlScheme copySaved(String schemeName)
    {
        return savedControlSchemes.get(schemeName).duplicate();
    }
    
    private ControlScheme duplicate()
    {
        Trigger[] dt = {this.acceptTrigger, this.backTrigger, this.pauseTrigger, this.leftTrigger, this.rightTrigger, this.upTrigger, this.downTrigger, this.jumpTrigger,this.leftrightTrigger,
                        this.attackTrigger, this.specialTrigger, this.grabTrigger, this.shieldTrigger, this.taunt1Trigger, this.taunt2Trigger, this.taunt3Trigger};
//        String[] dn = {this.acceptName, this.backName, this.pauseName, this.leftName, this.rightName, this.upName, this.downName, this.jumpName,
//                       this.attackName, this.specialName, this.grabName, this.shieldName, this.taunt1Name, this.taunt2Name, this.taunt3Name};
        
        return new ControlScheme(dt);
    }

}
