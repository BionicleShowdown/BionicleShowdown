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
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMouseEvent;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.CompoundInputManager;
import mygame.Main;

/**
 *
 * @author Inferno
 */
public class ControlsHub implements ScreenController, KeyInputHandler, ActionListener
{
    private AssetManager assetManager;
    private SimpleApplication app;
    private Node rootNode;
    private Nifty nifty;
    private NiftyJmeDisplay niftyDisplay;
    private Screen screen;
    private InputManager inputManager;
    private CompoundInputManager compoundManager;
    private AudioRenderer audioRenderer;
    private ViewPort guiViewPort;
    private AppStateManager stateManager;
    private OptionsMenu optionsMenu;
    
    private static Element backButtonPanelElement;
    private static Element player1Panel;
    private static Element player2Panel;
    private static Element player3Panel;
    private static Element player4Panel;
    
    private static ImageRenderer backButtonImageRenderer;
    private static ImageRenderer player1ImageRenderer;
    private static ImageRenderer player2ImageRenderer;
    private static ImageRenderer player3ImageRenderer;
    private static ImageRenderer player4ImageRenderer;
    
    private static NiftyImage backImageNormal;
    private static NiftyImage backImageHover;
    
    private static NiftyImage player1ImageNormal;
    private static NiftyImage player2ImageNormal;
    private static NiftyImage player3ImageNormal;
    private static NiftyImage player4ImageNormal;
    
    private static NiftyImage player1ImageHover;
    private static NiftyImage player2ImageHover;
    private static NiftyImage player3ImageHover;
    private static NiftyImage player4ImageHover;
    
    private boolean backButtonWasClickable = false;
    private boolean backButtonResetDone = false;
    private boolean player1WasClickable = false;
    private boolean player1ResetDone = false;
    private boolean player2WasClickable = false;
    private boolean player2ResetDone = false;
    private boolean player3WasClickable = false;
    private boolean player3ResetDone = false;
    private boolean player4WasClickable = false;
    private boolean player4ResetDone = false;
    
    private boolean keysUsed = false;
    
    private final byte nullID = -1;
    private final byte backButtonID = 0;
    private final byte player1ID = 1;
    private final byte player2ID = 2;
    private final byte player3ID = 3;
    private final byte player4ID = 4;
    
    private ButtonLayout currentLayout = new ButtonLayout(nullID, backButtonID, backButtonID, backButtonID, backButtonID);
    private ButtonLayout backButtonLayout = new ButtonLayout(backButtonID, backButtonID, backButtonID, backButtonID, player1ID);
    private ButtonLayout player1Layout = new ButtonLayout(player1ID, player1ID, player2ID, backButtonID, player3ID);
    private ButtonLayout player2Layout = new ButtonLayout(player2ID, player1ID, player2ID, player2ID, player4ID);
    private ButtonLayout player3Layout = new ButtonLayout(player3ID, player3ID, player4ID, player1ID, player3ID);
    private ButtonLayout player4Layout = new ButtonLayout(player4ID, player3ID, player4ID, player2ID, player4ID);

    
    public ControlsHub()
    {
        
    }
    
    public ControlsHub(OptionsMenu optionsMenu) 
    {
        this.optionsMenu = optionsMenu;
    }
    
    public void initiate(Application app)
    {
        this.app = (SimpleApplication) app;
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.compoundManager = Main.getCompoundManager();
        this.audioRenderer = this.app.getAudioRenderer();
        this.guiViewPort = this.app.getViewPort();
        this.stateManager = this.app.getStateManager();
        this.niftyDisplay = Main.getNiftyDisplay();
        nifty = Main.getNifty();
        nifty.registerScreenController(this);
        nifty.addXml("Interface/GUIS/ControlsHub.xml");
        nifty.gotoScreen("ControlsHub");          //Just for the first one, go to the start screen
        nifty.setDebugOptionPanelColors(true); // Leave it on true for now
    }

    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() 
    {
        backButtonPanelElement = screen.findElementByName("BackButtonPanel");
        player1Panel = screen.findElementByName("Player1ControlsPanel");
        player2Panel = screen.findElementByName("Player2ControlsPanel");
        player3Panel = screen.findElementByName("Player3ControlsPanel");
        player4Panel = screen.findElementByName("Player4ControlsPanel");
        
        backButtonImageRenderer = screen.findElementByName("BackButtonImage").getRenderer(ImageRenderer.class);
        player1ImageRenderer = screen.findElementByName("Player1ControlsImage").getRenderer(ImageRenderer.class);
        player2ImageRenderer = screen.findElementByName("Player2ControlsImage").getRenderer(ImageRenderer.class);
        player3ImageRenderer = screen.findElementByName("Player3ControlsImage").getRenderer(ImageRenderer.class);
        player4ImageRenderer = screen.findElementByName("Player4ControlsImage").getRenderer(ImageRenderer.class);
        
        backImageNormal = nifty.createImage("Interface/General/BackButton.png", false);
        player1ImageNormal = nifty.createImage("Interface/Options/ControlsPlayer1.png", false);
        player2ImageNormal = nifty.createImage("Interface/Options/ControlsPlayer2.png", false);
        player3ImageNormal = nifty.createImage("Interface/Options/ControlsPlayer3.png", false);
        player4ImageNormal = nifty.createImage("Interface/Options/ControlsPlayer4.png", false);

        backImageHover = nifty.createImage("Interface/General/BackButtonHover.png", false);
        player1ImageHover = nifty.createImage("Interface/Options/ControlsPlayer1Hover.png", false);
        player2ImageHover = nifty.createImage("Interface/Options/ControlsPlayer2Hover.png", false);
        player3ImageHover = nifty.createImage("Interface/Options/ControlsPlayer3Hover.png", false);
        player4ImageHover = nifty.createImage("Interface/Options/ControlsPlayer4Hover.png", false);
        
//        fightPanel.setFocusable(true);
//        fightPanel.setFocus();
        currentLayout = new ButtonLayout(nullID, backButtonID, backButtonID, backButtonID, backButtonID);
        keysUsed = false;
        
        initJoy();
    }

    public void onEndScreen() 
    {
        /* Make sure to delete mappings so they don't seep through to the next menu.
           Do NOT clear Mappings, as that will also remove the Escape to exit. */
        
            compoundManager.deleteMapping("Accept");
            compoundManager.deleteMapping("Right");
            compoundManager.deleteMapping("Left");
            compoundManager.deleteMapping("Up");
            compoundManager.deleteMapping("Down");
    }
    
    @NiftyEventSubscriber(id="MouseCatcher")
    public void buttonCaller(String id, NiftyMouseEvent event)
    {
        int x = event.getMouseX();
        int y = event.getMouseY();
        boolean wasClick = event.isButton0Down();
        
        if (wasClick)
        {
            keysUsed = false;
            backButtonResetDone = false;
            player1ResetDone = false;
            player2ResetDone = false;
            player3ResetDone = false;
            player4ResetDone = false;
        }
        
        onBackButtonHover(backButtonPanelElement.isMouseInsideElement(x, y), wasClick);
        
        player1ButtonControl(player1Panel.isMouseInsideElement(x, y), wasClick);
        player2ButtonControl(player2Panel.isMouseInsideElement(x, y), wasClick);
        player3ButtonControl(player3Panel.isMouseInsideElement(x, y), wasClick);
        player4ButtonControl(player4Panel.isMouseInsideElement(x, y), wasClick);
        
    }
    
    public void onBackButtonHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                goBack();
                return;
            }
            if (!backButtonWasClickable)
            {
                backButtonImageRenderer.setImage(backImageHover);
                backButtonWasClickable = true;
                backButtonResetDone = false;
                adaptActive(backButtonID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!backButtonResetDone)
        {
            backButtonImageRenderer.setImage(backImageNormal);
            backButtonWasClickable = false;
            backButtonResetDone = true;
        }
    }
    
    public void player1ButtonControl(boolean isAvailable, boolean wasClick)
    {
        System.out.println(isAvailable);
        if (isAvailable)
        {
            if (wasClick)
            {
                activateActive();
                return;
            }
            if (!player1WasClickable)
            {
                player1ImageRenderer.setImage(player1ImageHover);
                player1WasClickable = true;
                player1ResetDone = false;
                adaptActive(player1ID);
            }
//            adaptActive(fightID);
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!player1ResetDone)
        {
            player1ImageRenderer.setImage(player1ImageNormal);
            player1WasClickable = false;
            player1ResetDone = true;
        }
    }
    
    public void player2ButtonControl(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                activateActive();
                return;
            }
            if (!player2WasClickable)
            {
                player2ImageRenderer.setImage(player2ImageHover);
                player2WasClickable = true;
                player2ResetDone = false;
                adaptActive(player2ID);
            }
//            adaptActive(trainingID);
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!player2ResetDone)
        {
            player2ImageRenderer.setImage(player2ImageNormal);
            player2WasClickable = false;
            player2ResetDone = true;
        }
    }
    
    public void player3ButtonControl(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                activateActive();
                return;
            }
            if (!player3WasClickable)
            {
                player3ImageRenderer.setImage(player3ImageHover);
                player3WasClickable = true;
                player3ResetDone = false;
                adaptActive(player3ID);
            } 
//            adaptActive(extrasID);
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!player3ResetDone)
        {
            player3ImageRenderer.setImage(player3ImageNormal);
            player3WasClickable = false;
            player3ResetDone = true;
        }
    }
    
    public void player4ButtonControl(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                activateActive();
                return;
            }
            if (!player4WasClickable)
            {
                player4ImageRenderer.setImage(player4ImageHover);
                player4WasClickable = true;
                player4ResetDone = false;
                adaptActive(player4ID);
            }
//            adaptActive(optionsID);
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!player4ResetDone)
        {
            player4ImageRenderer.setImage(player4ImageNormal);
            player4WasClickable = false;
            player4ResetDone = true;
        }
    }
    
    
    
    
    
    
    
//    public void player1Controls()
//    {
//        PlayerControlMenu playerControlMenu = new PlayerControlMenu(this, "Player 1");
//        playerControlMenu.initiate(app);
//    }
    
    public void playerControls(String player)
    {
        System.out.println("The Player Coming in is... " + player);
        PlayerControlMenu playerControlMenu = new PlayerControlMenu(this, player);
        playerControlMenu.initiate(app);
    }
    
    public void goBack()
    {
        optionsMenu.initiate(app);
    }

    
    
    public void adaptActive(byte newButton)
    {
        if (!keysUsed && (currentLayout.Identity() == newButton))
        {
            switch (newButton)
            {
                case backButtonID: 
                {
                    currentLayout = backButtonLayout;
                    backButtonImageRenderer.setImage(backImageHover);
                    backButtonWasClickable = true;
                    break;
                }
                case player1ID:
                {
//                    System.out.println("Fight");
                    currentLayout = player1Layout;
                    player1ImageRenderer.setImage(player1ImageHover);
                    player1WasClickable = true;
                    break;
                }
                case player2ID:
                {
//                    System.out.println("Training");
                    currentLayout = player2Layout;
                    player2ImageRenderer.setImage(player2ImageHover);
                    player2WasClickable = true;
                    break;
                }
                case player3ID:
                {
//                    System.out.println("Extras");
                    currentLayout = player3Layout;
                    player3ImageRenderer.setImage(player3ImageHover);
                    player3WasClickable = true;
                    break;
                }
                case player4ID:
                {
//                    System.out.println("Options");
                    currentLayout = player4Layout;
                    player4ImageRenderer.setImage(player4ImageHover);
                    player4WasClickable = true;
                    break;
                }
                default:
                {
                    currentLayout = backButtonLayout;
                    backButtonImageRenderer.setImage(backImageHover);
                    backButtonWasClickable = true;
                }
            }
        }
        if (currentLayout.Identity() == newButton)
        {
            System.out.println("Already There");
            return;
        }
        switch (currentLayout.Identity())
        {
            case backButtonID: 
            {
                currentLayout = backButtonLayout;
                backButtonImageRenderer.setImage(backImageNormal);
                backButtonWasClickable = false;
                break;
            }
            case player1ID:
            {
//                System.out.println("Fight");
                currentLayout = player1Layout;
                player1WasClickable = false;
                player1ImageRenderer.setImage(player1ImageNormal);
                break;
            }
            case player2ID:
            {
//                System.out.println("Training");
                currentLayout = player2Layout;
                player2WasClickable = false;
                player2ImageRenderer.setImage(player2ImageNormal);
                break;
            }
            case player3ID:
            {
//                System.out.println("Extras");
                currentLayout = player3Layout;
                player3WasClickable = false;
                player3ImageRenderer.setImage(player3ImageNormal);
                break;
            }
            case player4ID:
            {
//                System.out.println("Options");
                currentLayout = player4Layout;
                player4WasClickable = false;
                player4ImageRenderer.setImage(player4ImageNormal);
                break;
            }
            default:
            {
                currentLayout = backButtonLayout;
                backButtonWasClickable = false;
                backButtonImageRenderer.setImage(backImageNormal);
            }
        }
        switch (newButton)
        {
            case backButtonID: 
            {
                currentLayout = backButtonLayout;
                backButtonImageRenderer.setImage(backImageHover);
                backButtonWasClickable = true;
                break;
            }
            case player1ID:
            {
//                System.out.println("Fight");
                currentLayout = player1Layout;
                player1ImageRenderer.setImage(player1ImageHover);
                player1WasClickable = true;
                break;
            }
            case player2ID:
            {
//                System.out.println("Training");
                currentLayout = player2Layout;
                player2ImageRenderer.setImage(player2ImageHover);
                player2WasClickable = true;
                break;
            }
            case player3ID:
            {
//                System.out.println("Extras");
                currentLayout = player3Layout;
                player3ImageRenderer.setImage(player3ImageHover);
                player3WasClickable = true;
                break;
            }
            case player4ID:
            {
//                System.out.println("Options");
                currentLayout = player4Layout;
                player4ImageRenderer.setImage(player4ImageHover);
                player4WasClickable = true;
                break;
            }
            default:
            {
                currentLayout = backButtonLayout;
                backButtonImageRenderer.setImage(backImageHover);
                backButtonWasClickable = true;
            }
        }
    }
    
    public void activateActive()
    {
        switch (currentLayout.Identity())
        {
            case backButtonID: 
            {
                goBack();
                break;
            }
            case player1ID:
            {
                playerControls("Player 1");
                break;
            }
            case player2ID:
            {
                playerControls("Player 2");
                break;
            }
            case player3ID:
            {
                playerControls("Player 3");
                break;
            }
            case player4ID:
            {
                playerControls("Player 4");
                break;
            }
            default:
            {
                System.out.println("No Selection");
            }
        }
    }
    
    public boolean keyEvent(NiftyInputEvent event)
    {
        if (event == NiftyInputEvent.Activate)
        {
            System.out.println("ACTIVATE!!!");
            if (keysUsed)
            {
                activateActive();
            }
            return true;
        }
        else if (event == NiftyInputEvent.Backspace)
        {
            System.out.println("Back");
            goBack();
        }
        else if (event == NiftyInputEvent.MoveCursorLeft)
        {
            System.out.println("Left");
            if (!keysUsed)
            {
                adaptActive(currentLayout.Identity());
                keysUsed = true;
            }
            else
            {
                adaptActive(currentLayout.Left());
            }
            return true;
        }
        else if (event == NiftyInputEvent.MoveCursorRight)
        {
            System.out.println("Right");
            if (!keysUsed)
            {
                adaptActive(currentLayout.Identity());
                keysUsed = true;
            }
            else
            {
                adaptActive(currentLayout.Right());
            }
            return true;
        }
        else if (event == NiftyInputEvent.MoveCursorUp)
        {
            System.out.println("Up");
            if (!keysUsed)
            {
                adaptActive(currentLayout.Identity());
                keysUsed = true;
            }
            else
            {
                adaptActive(currentLayout.Up());
            }
            return true;
        }
        else if (event == NiftyInputEvent.MoveCursorDown)
        {
            System.out.println("Down");
            if (!keysUsed)
            {
                adaptActive(currentLayout.Identity());
                keysUsed = true;
            }
            else
            {
                adaptActive(currentLayout.Down());
            }
            return true;
        }
        return false;
    }
    
    private void initJoy() 
    {
//        inputManager.setCursorVisible(false);
        if (Main.joysticks.length != 0)
        {
            Main.joysticks[0].getButton("0").assignButton("Accept");
            
            if (Main.joysticks[0].getName().equals("Logitech Dual Action"))
            {
                compoundManager.deleteMapping("Accept");
                Main.joysticks[0].getButton("2").assignButton("Accept");
            }
            
            Main.joysticks[0].getXAxis().assignAxis("Right", "Left");
            Main.joysticks[0].getYAxis().assignAxis("Down", "Up");
            compoundManager.addListener(this, "Accept", "Right", "Left", "Up", "Down");
        }
    }

    public void onAction(String name, boolean isPressed, float tpf) 
    {
        if (isPressed && name.equals("Accept"))
        {
            System.out.println("Accept");
            niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_RETURN, '0', true, false));
        }
        else if (isPressed && name.equals("Right"))
        {
            System.out.println("Right");
            niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_D, 'D', true, false));
        }
        else if (isPressed && name.equals("Left"))
        {
            System.out.println("Left");
            niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_A, 'A', true, false));
        }
        else if (isPressed && name.equals("Up"))
        {
            System.out.println("Up");
            niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_W, 'W', true, false));
        }
        else if (isPressed && name.equals("Down"))
        {
            System.out.println("Down");
            System.out.println("Woah woah");
            niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_S, 'S', true, false));
        }
    }
}
