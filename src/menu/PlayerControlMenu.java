/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.input.JoyInput;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.input.lwjgl.JInputJoyInput;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.AdaptedDefaultControllerEnvironment;
import mygame.CompoundInputManager;
import mygame.Main;
import mygame.ShowdownJoyInput;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

/**
 *
 * @author Inferno
 */
public class PlayerControlMenu implements ScreenController, ActionListener
{
    private AssetManager assetManager;
    private Main app;
    private Node rootNode;
    private Nifty nifty;
    private NiftyJmeDisplay niftyDisplay;
    private Screen screen;
    public static InputManager inputManager;
    public static InputManager joyManager;
    public static CompoundInputManager compoundManager;
    private AudioRenderer audioRenderer;
    private ViewPort guiViewPort;
    private AppStateManager stateManager;
    private ControlsHub controlsHub;
    
    private static KeyMapListener keymapListener;
    
    private static TextRenderer acceptTextRenderer;
    private static TextRenderer backTextRenderer;
    private static TextRenderer pauseTextRenderer;
    private static TextRenderer leftTextRenderer;
    private static TextRenderer rightTextRenderer;
    private static TextRenderer upTextRenderer;
    private static TextRenderer downTextRenderer;
    private static TextRenderer jumpTextRenderer;
    private static TextRenderer attackTextRenderer;
    private static TextRenderer specialTextRenderer;
    private static TextRenderer grabTextRenderer;
    private static TextRenderer shieldTextRenderer;
    private static TextRenderer taunt1TextRenderer;
    private static TextRenderer taunt2TextRenderer;
    private static TextRenderer taunt3TextRenderer;
    
    
    public static String player;
    private static String currentControl = "";
    private static ControlScheme currentScheme;
    
//    private static final ControlScheme player1DefaultScheme = ControlScheme.copyDefault("Player 1 Defaults");
//    private static final ControlScheme player2DefaultScheme = ControlScheme.copyDefault("Player 2 Defaults");
//    private static final ControlScheme player3DefaultScheme = ControlScheme.copyDefault("Player 3 Defaults");
//    private static final ControlScheme player4DefaultScheme = ControlScheme.copyDefault("Player 4 Defaults");
    
    private static ControlScheme player1PreferenceScheme = ControlScheme.copyDefault("Player 1 Defaults");
    
    public static ControlScheme player1Scheme = player1PreferenceScheme; // = player1DefaultScheme;
    public static ControlScheme player2Scheme;
    public static ControlScheme player3Scheme;
    public static ControlScheme player4Scheme;
    
    PlayerControlMenu()
    {
        
    }

    PlayerControlMenu(ControlsHub controlsHub, String player) 
    {
        this.controlsHub = controlsHub;
        PlayerControlMenu.player = player;
    }
    
    public void initiate(Application app)
    {
        this.app = (Main) app;
        this.assetManager = this.app.getAssetManager();
//        inputManager = this.app.getInputManager();
//        joyManager = this.app.getJoyManager();
        this.compoundManager = Main.getCompoundManager();
        this.audioRenderer = this.app.getAudioRenderer();
        this.guiViewPort = this.app.getViewPort();
        this.stateManager = this.app.getStateManager();
        this.niftyDisplay = Main.getNiftyDisplay();
        nifty = Main.getNifty();
        nifty.registerScreenController(this);
        nifty.addXml("Interface/GUIS/PlayerControlMenu.xml");
        nifty.gotoScreen("PlayerControlMenu");          //Just for the first one, go to the start screen
        nifty.setDebugOptionPanelColors(true); // Leave it on true for now
        
        System.out.println("On initiate Player is :" + player);
        
        System.out.println("ACCEPT RENDER INIT: " + acceptTextRenderer);
        
        keymapListener = new KeyMapListener();
        
        compoundManager.addRawInputListener(keymapListener);
        compoundManager.addListener(this, "Change");
//        inputManager.addRawInputListener(keymapListener);
//        joyManager.addRawInputListener(keymapListener);
//        inputManager.addListener(this, "Change");
//        joyManager.addListener(this, "Change");
        System.out.println("Current Scheme is: " + currentScheme);
    }

    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() 
    {
        acceptTextRenderer = screen.findElementByName("AcceptControlText").getRenderer(TextRenderer.class);
        backTextRenderer = screen.findElementByName("BackControlText").getRenderer(TextRenderer.class);
        pauseTextRenderer = screen.findElementByName("PauseControlText").getRenderer(TextRenderer.class);
        leftTextRenderer = screen.findElementByName("LeftControlText").getRenderer(TextRenderer.class);
        rightTextRenderer = screen.findElementByName("RightControlText").getRenderer(TextRenderer.class);
        upTextRenderer = screen.findElementByName("UpControlText").getRenderer(TextRenderer.class);
        downTextRenderer = screen.findElementByName("DownControlText").getRenderer(TextRenderer.class);
        jumpTextRenderer = screen.findElementByName("JumpControlText").getRenderer(TextRenderer.class);
        attackTextRenderer = screen.findElementByName("AttackControlText").getRenderer(TextRenderer.class);
        specialTextRenderer = screen.findElementByName("SpecialControlText").getRenderer(TextRenderer.class);
        grabTextRenderer = screen.findElementByName("GrabControlText").getRenderer(TextRenderer.class);
        shieldTextRenderer = screen.findElementByName("ShieldControlText").getRenderer(TextRenderer.class);
        taunt1TextRenderer = screen.findElementByName("Taunt1ControlText").getRenderer(TextRenderer.class);
        taunt2TextRenderer = screen.findElementByName("Taunt2ControlText").getRenderer(TextRenderer.class);
        taunt3TextRenderer = screen.findElementByName("Taunt3ControlText").getRenderer(TextRenderer.class);
       
        System.out.println("ACCEPT RENDER START: " + acceptTextRenderer);
        checkScheme();
        initScheme();
    }

    public void onEndScreen() 
    {
        // TODO: Find some way to unregister listener, without impacting rest of file.
//        inputManager.deleteMapping("Change");
//        inputManager.removeRawInputListener(keymapListener);
        compoundManager.deleteMapping("Change");
        compoundManager.removeRawInputListener(keymapListener);
        maintainScheme();
    }
    
    public void changeAccept()
    {
        currentControl = "Accept";
//        System.out.println("ACCEPT RENDER CHANGE: " + acceptTextRenderer);
//        System.out.println("ACCEPT THIS CHANGE");
//        currentScheme.setAccept(new KeyTrigger(KeyInput.KEY_B));
//        currentScheme.setAccept("B");
//        acceptTextRenderer.setText(currentScheme.getAcceptName());
//        System.out.println(currentScheme.getAcceptName());
//        System.out.println(ControlScheme.defaultControlSchemes.get("Player 1 Defaults").getAcceptName());
//        System.out.println(currentScheme.getAccept());
//        System.out.println(ControlScheme.defaultControlSchemes.get("Player 1 Defaults").getAccept());
    }
    
    public void changeBack()
    {
        currentControl = "Back";
    }
    
    public void changeControl(String control)
    {
        System.out.println("New Control: " + control);
        currentControl = control;
    }
    
    
    public void restoreDefaults()
    {
        if (player.equals("Player 1"))
        {
            setupControls(ControlScheme.copyDefault("Player 1 Defaults"));
        }
        else if (player.equals("Player 2"))
        {
            setupControls(ControlScheme.copyDefault("Player 2 Defaults"));
        }
        else if (player.equals("Player 3"))
        {
            setupControls(ControlScheme.copyDefault("Player 3 Defaults"));
        }
        else if (player.equals("Player 4"))
        {
            setupControls(ControlScheme.copyDefault("Player 4 Defaults"));
        }
    }
    
    public void useJoystick()
    {
        System.out.println("Checking Joysticks Before: " + Main.joysticks.length);
//        Main.joysticks = app.checkJoysticks();
        Main.joysticks = compoundManager.getJoysticks();
        System.out.println("After: " + Main.joysticks.length);
        
        compoundManager.setAxisDeadZone(0.5f);
        
//        Main.joysticks[0].getButton("0").assignButton("Test");
//        joyManager.addListener(this, "Test");
//        app.rebuildInputManager();
//        inputManager = app.getInputManager();
//        System.out.println(inputManager.getJoysticks());
//        System.out.println("Checking for Joysticks");
//        Main.joysticks = inputManager.getJoysticks();
//        System.out.println(Main.joysticks.length);
//        inputManager.addRawInputListener(keymapListener);
//        inputManager.addListener(this, "Change");
//        keymapListener.changeInputManager(inputManager);
//        this.nifty = Main.getNifty();
    }
    
    public void goBack()
    {
        controlsHub.initiate(app);
    }
    
    
    private void initScheme()
    {
        if (player.equals("Player 1"))
        {
            setupControls(player1Scheme);
        }
        else if (player.equals("Player 2"))
        {
            setupControls(player2Scheme);
        }
        else if (player.equals("Player 3"))
        {
            setupControls(player3Scheme);
        }
        else if (player.equals("Player 4"))
        {
            setupControls(player4Scheme);
        }
    }
    
    private void maintainScheme()
    {
        if (player.equals("Player 1"))
        {
            player1Scheme = currentScheme;
        }
        else if (player.equals("Player 2"))
        {
            player2Scheme = currentScheme;
        }
        else if (player.equals("Player 3"))
        {
            player3Scheme = currentScheme;
        }
        else if (player.equals("Player 4"))
        {
            player4Scheme = currentScheme;
        }
    }

    private void setupControls(ControlScheme scheme) 
    {
        currentScheme = scheme;
        acceptTextRenderer.setText(scheme.getAcceptName());
        backTextRenderer.setText(scheme.getBackName());
        pauseTextRenderer.setText(scheme.getPauseName());
        leftTextRenderer.setText(scheme.getLeftName());
        rightTextRenderer.setText(scheme.getRightName());
        upTextRenderer.setText(scheme.getUpName());
        downTextRenderer.setText(scheme.getDownName());
        jumpTextRenderer.setText(scheme.getJumpName());
        attackTextRenderer.setText(scheme.getAttackName());
        specialTextRenderer.setText(scheme.getSpecialName());
        grabTextRenderer.setText(scheme.getGrabName());
        shieldTextRenderer.setText(scheme.getShieldName());
        taunt1TextRenderer.setText(scheme.getTaunt1Name());
        taunt2TextRenderer.setText(scheme.getTaunt2Name());
        taunt3TextRenderer.setText(scheme.getTaunt3Name());
    }

    private void checkScheme() 
    {
        System.out.println("Player = " + player);
        if (player.equals("Player 1"))
        {
            if (player1Scheme == null)
            {
                player1Scheme = ControlScheme.copyDefault("Player 1 Defaults");
            }
        }
        else if (player.equals("Player 2"))
        {
            if (player2Scheme == null)
            {
                player2Scheme = ControlScheme.copyDefault("Player 2 Defaults");
            }
        }
        else if (player.equals("Player 3"))
        {
            if (player3Scheme == null)
            {
                player3Scheme = ControlScheme.copyDefault("Player 3 Defaults");
            }
        }
        else if (player.equals("Player 4"))
        {
            if (player4Scheme == null)
            {
                player4Scheme = ControlScheme.copyDefault("Player 4 Defaults");
            }
        }
    }

    public void onAction(String name, boolean isPressed, float tpf) 
    {
        if ("Test".equals(name))
        {
            System.out.println("A pressed.");
            return;
        }
        
        changeControlTrigger(currentControl, keymapListener.getLastTrigger(), keymapListener.getLastKeyName());
//        System.out.println(acceptTextRenderer);
//        if ("Change".equals(name))
//        {
//            if ("Accept".equals(currentControl))
//            {
//                System.out.println(currentScheme);
//                System.out.println(keymapListener);
//                currentScheme.setAccept(keymapListener.getLastTrigger());
//                currentScheme.setAccept(keymapListener.getLastKeyName());
//                acceptTextRenderer.setText(currentScheme.getAcceptName());
//                currentControl = "";
//            }
//            else if ("Back".equals(currentControl))
//            {
//                currentScheme.setBack(keymapListener.getLastTrigger());
//                currentScheme.setBack(keymapListener.getLastKeyName());
//                backTextRenderer.setText(currentScheme.getBackName());
//                currentControl = "";
//            }
//        }
    }
    
    public void changeControlTrigger(String control, Trigger trigger, String name)
    {
        if ("Accept".equals(control))
        {
            currentScheme.setAccept(trigger);
            currentScheme.setAccept(name);
            acceptTextRenderer.setText(currentScheme.getAcceptName());
            
        }
        else if ("Back".equals(control))
        {
            currentScheme.setBack(trigger);
            currentScheme.setBack(name);
            backTextRenderer.setText(currentScheme.getBackName());
        }
        else if ("Pause".equals(control))
        {
            currentScheme.setPause(trigger);
            currentScheme.setPause(name);
            pauseTextRenderer.setText(currentScheme.getPauseName());
        }
        else if ("Left".equals(control))
        {
            currentScheme.setLeft(trigger);
            currentScheme.setLeft(name);
            leftTextRenderer.setText(currentScheme.getLeftName());
        }
        else if ("Right".equals(control))
        {
            currentScheme.setRight(trigger);
            currentScheme.setRight(name);
            rightTextRenderer.setText(currentScheme.getRightName());
        }
        else if ("Up".equals(control))
        {
            currentScheme.setUp(trigger);
            currentScheme.setUp(name);
            upTextRenderer.setText(currentScheme.getUpName());
        }
        else if ("Down".equals(control))
        {
            currentScheme.setDown(trigger);
            currentScheme.setDown(name);
            downTextRenderer.setText(currentScheme.getDownName());
        }
        else if ("Jump".equals(control))
        {
            currentScheme.setJump(trigger);
            currentScheme.setJump(name);
            jumpTextRenderer.setText(currentScheme.getJumpName());
        }
        else if ("Attack".equals(control))
        {
            currentScheme.setAttack(trigger);
            currentScheme.setAttack(name);
            attackTextRenderer.setText(currentScheme.getAttackName());
        }
        else if ("Special".equals(control))
        {
            currentScheme.setSpecial(trigger);
            currentScheme.setSpecial(name);
            specialTextRenderer.setText(currentScheme.getSpecialName());
        }
        else if ("Grab".equals(control))
        {
            currentScheme.setGrab(trigger);
            currentScheme.setGrab(name);
            grabTextRenderer.setText(currentScheme.getGrabName());
        }
        else if ("Shield".equals(control))
        {
            currentScheme.setShield(trigger);
            currentScheme.setShield(name);
            shieldTextRenderer.setText(currentScheme.getShieldName());
        }
        else if ("Taunt 1".equals(control))
        {
            currentScheme.setTaunt1(trigger);
            currentScheme.setTaunt1(name);
            taunt1TextRenderer.setText(currentScheme.getTaunt1Name());
        }
        else if ("Taunt 2".equals(control))
        {
            currentScheme.setTaunt2(trigger);
            currentScheme.setTaunt2(name);
            taunt2TextRenderer.setText(currentScheme.getTaunt2Name());
        }
        else if ("Taunt 3".equals(control))
        {
            currentScheme.setTaunt3(trigger);
            currentScheme.setTaunt3(name);
            taunt3TextRenderer.setText(currentScheme.getTaunt3Name());
        }
        currentControl = "";
    }
    
    
    public static ControlScheme getControls(String player)
    {
        if ("Player 1".equals(player))
        {
            return player1Scheme;
        }
        else if ("Player 2".equals(player))
        {
            return player2Scheme;
        }
        else if ("Player 3".equals(player))
        {
            return player3Scheme;
        }
        else if ("Player 4".equals(player))
        {
            return player4Scheme;
        }
        else
        {
            return null;
        }
    }

}