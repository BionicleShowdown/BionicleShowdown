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
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.input.controls.JoyButtonTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.lwjgl.JInputJoyInput;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMouseEvent;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.image.ImageModeFactory;
import de.lessvoid.nifty.render.image.ImageModeHelper;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
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
public class PlayerControlMenu implements ScreenController, ActionListener, KeyInputHandler
{
    private AssetManager assetManager;
    private SimpleApplication app;
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
    private static KeyNames keynames = new KeyNames();
    
    
    private static Element backButtonPanelElement;
    /* Column 1 */
    private static Element acceptPanelElement;
    private static Element backPanelElement;
    private static Element pausePanelElement;
    private static Element leftPanelElement;
    private static Element rightPanelElement;
    private static Element upPanelElement;
    private static Element downPanelElement;
    private static Element jumpPanelElement;
    private static Element joystickPanelElement;
    private static Element joystickSettingsPanelElement;
    /* Column 2 */
    private static Element attackPanelElement;
    private static Element specialPanelElement;
    private static Element grabPanelElement;
    private static Element shieldPanelElement;
    private static Element taunt1PanelElement;
    private static Element taunt2PanelElement;
    private static Element taunt3PanelElement;
    private static Element restorePanelElement;
    private static Element savePanelElement;
    private static Element loadPanelElement;
    
    
    /* Column 1 */
    private static TextRenderer acceptNameTextRenderer;
    private static TextRenderer backNameTextRenderer;
    private static TextRenderer pauseNameTextRenderer;
    private static TextRenderer leftNameTextRenderer;
    private static TextRenderer rightNameTextRenderer;
    private static TextRenderer upNameTextRenderer;
    private static TextRenderer downNameTextRenderer;
    private static TextRenderer jumpNameTextRenderer;

    /* Column 2 */
    private static TextRenderer attackNameTextRenderer;
    private static TextRenderer specialNameTextRenderer;
    private static TextRenderer grabNameTextRenderer;
    private static TextRenderer shieldNameTextRenderer;
    private static TextRenderer taunt1NameTextRenderer;
    private static TextRenderer taunt2NameTextRenderer;
    private static TextRenderer taunt3NameTextRenderer;
    
    
    /* Column 1 */
    private static TextRenderer acceptTextRenderer;
    private static TextRenderer backTextRenderer;
    private static TextRenderer pauseTextRenderer;
    private static TextRenderer leftTextRenderer;
    private static TextRenderer rightTextRenderer;
    private static TextRenderer upTextRenderer;
    private static TextRenderer downTextRenderer;
    private static TextRenderer jumpTextRenderer;
    private static TextRenderer joystickTextRenderer;
    private static TextRenderer joystickSettingsTextRenderer;
    /* Column 2 */
    private static TextRenderer attackTextRenderer;
    private static TextRenderer specialTextRenderer;
    private static TextRenderer grabTextRenderer;
    private static TextRenderer shieldTextRenderer;
    private static TextRenderer taunt1TextRenderer;
    private static TextRenderer taunt2TextRenderer;
    private static TextRenderer taunt3TextRenderer;
    private static TextRenderer restoreTextRenderer;
    private static TextRenderer saveTextRenderer;
    private static TextRenderer loadTextRenderer;
    
    private static ImageRenderer backButtonImageRenderer;
    /* Column 1 */
    private static ImageRenderer acceptImageRenderer;
    private static ImageRenderer backImageRenderer;
    private static ImageRenderer pauseImageRenderer;
    private static ImageRenderer leftImageRenderer;
    private static ImageRenderer rightImageRenderer;
    private static ImageRenderer upImageRenderer;
    private static ImageRenderer downImageRenderer;
    private static ImageRenderer jumpImageRenderer;
    private static ImageRenderer joystickImageRenderer;
    private static ImageRenderer joystickSettingsImageRenderer;
    /* Column 2 */
    private static ImageRenderer attackImageRenderer;
    private static ImageRenderer specialImageRenderer;
    private static ImageRenderer grabImageRenderer;
    private static ImageRenderer shieldImageRenderer;
    private static ImageRenderer taunt1ImageRenderer;
    private static ImageRenderer taunt2ImageRenderer;
    private static ImageRenderer taunt3ImageRenderer;
    private static ImageRenderer restoreImageRenderer;
    private static ImageRenderer saveImageRenderer;
    private static ImageRenderer loadImageRenderer;
    
    private static NiftyImage backImageNormal;
    private static NiftyImage backImageHover;
    
    private static NiftyImage edgeImageNormal;
    private static NiftyImage edgeImageHover;
    
    private Color defaultTextColor = new Color("#FFFFFF");
    private Color hoverTextColor = new Color("#FFE500");
    
    
    public static String player;
    public static String currentControl = "";
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
    
    
    /* Button Layout Stuff */
    private final byte nullID = -1;
    private final byte backButtonID = 0;
    /* Column 1 */
    private final byte acceptID = 1;
    private final byte backID = 2;
    private final byte pauseID = 3;
    private final byte leftID = 4;
    private final byte rightID = 5;
    private final byte upID = 6;
    private final byte downID = 7;
    private final byte jumpID = 8;
    private final byte joystickID = 9;
    private final byte joystickSettingsID = 10;
    /* Column 2 */
    private final byte attackID = 11;
    private final byte specialID = 12;
    private final byte grabID = 13;
    private final byte shieldID = 14;
    private final byte taunt1ID = 15;
    private final byte taunt2ID = 16;
    private final byte taunt3ID = 17;
    private final byte restoreID = 18;
    private final byte saveID = 19;
    private final byte loadID = 20;
    
    private ButtonLayout currentLayout = new ButtonLayout(nullID, backButtonID, backButtonID, backButtonID, backButtonID);
    private ButtonLayout backButtonLayout = new ButtonLayout(backButtonID, backButtonID, backButtonID, backButtonID, acceptID);
    /* Column 1 */
    private ButtonLayout acceptLayout = new ButtonLayout(acceptID, acceptID, attackID, backButtonID, backID);
    private ButtonLayout backLayout = new ButtonLayout(backID, backID, specialID, acceptID, pauseID);
    private ButtonLayout pauseLayout = new ButtonLayout(pauseID, pauseID, grabID, backID, leftID);
    private ButtonLayout leftLayout = new ButtonLayout(leftID, leftID, shieldID, pauseID, rightID);
    private ButtonLayout rightLayout = new ButtonLayout(rightID, rightID, taunt1ID, leftID, upID);
    private ButtonLayout upLayout = new ButtonLayout(upID, upID, taunt2ID, rightID, downID);
    private ButtonLayout downLayout = new ButtonLayout(downID, downID, taunt3ID, upID, jumpID);
    private ButtonLayout jumpLayout = new ButtonLayout(jumpID, jumpID, restoreID, downID, joystickID);
    private ButtonLayout joystickLayout = new ButtonLayout(joystickID, joystickID, saveID, jumpID, joystickSettingsID);
    private ButtonLayout joystickSettingsLayout = new ButtonLayout(joystickSettingsID, joystickSettingsID, loadID, joystickID, joystickSettingsID);
    /* Column 2 */
    private ButtonLayout attackLayout = new ButtonLayout(attackID, acceptID, attackID, attackID, specialID);
    private ButtonLayout specialLayout = new ButtonLayout(specialID, backID, specialID, attackID, grabID);
    private ButtonLayout grabLayout = new ButtonLayout(grabID, pauseID, grabID, specialID, shieldID);
    private ButtonLayout shieldLayout = new ButtonLayout(shieldID, leftID, shieldID, grabID, taunt1ID);
    private ButtonLayout taunt1Layout = new ButtonLayout(taunt1ID, rightID, taunt1ID, shieldID, taunt2ID);
    private ButtonLayout taunt2Layout = new ButtonLayout(taunt2ID, upID, taunt2ID, taunt1ID, taunt3ID);
    private ButtonLayout taunt3Layout = new ButtonLayout(taunt3ID, downID, taunt3ID, taunt2ID, restoreID);
    private ButtonLayout restoreLayout = new ButtonLayout(restoreID, jumpID, restoreID, taunt3ID, saveID);
    private ButtonLayout saveLayout = new ButtonLayout(saveID, joystickID, saveID, restoreID, loadID);
    private ButtonLayout loadLayout = new ButtonLayout(loadID, joystickSettingsID, loadID, saveID, loadID);
    
    private boolean backButtonResetDone = false;
    private boolean backButtonWasClickable = false;
    /* Column 1 */
    private boolean acceptResetDone = false;
    private boolean acceptWasClickable = false;
    private boolean backResetDone = false;
    private boolean backWasClickable = false;
    private boolean pauseResetDone = false;
    private boolean pauseWasClickable = false;
    private boolean leftResetDone = false;
    private boolean leftWasClickable = false;
    private boolean rightResetDone = false;
    private boolean rightWasClickable = false;
    private boolean upResetDone = false;
    private boolean upWasClickable = false;
    private boolean downResetDone = false;
    private boolean downWasClickable = false;
    private boolean jumpResetDone = false;
    private boolean jumpWasClickable = false;
    private boolean joystickResetDone = false;
    private boolean joystickWasClickable = false;
    private boolean joystickSettingsResetDone = false;
    private boolean joystickSettingsWasClickable = false;
    /* Column 2 */
    private boolean attackResetDone = false;
    private boolean attackWasClickable = false;
    private boolean specialResetDone = false;
    private boolean specialWasClickable = false;
    private boolean grabResetDone = false;
    private boolean grabWasClickable = false;
    private boolean shieldResetDone = false;
    private boolean shieldWasClickable = false;
    private boolean taunt1ResetDone = false;
    private boolean taunt1WasClickable = false;
    private boolean taunt2ResetDone = false;
    private boolean taunt2WasClickable = false;
    private boolean taunt3ResetDone = false;
    private boolean taunt3WasClickable = false;
    private boolean restoreResetDone = false;
    private boolean restoreWasClickable = false;
    private boolean saveResetDone = false;
    private boolean saveWasClickable = false;
    private boolean loadResetDone = false;
    private boolean loadWasClickable = false;
    
    private boolean keysUsed = false;
    
    
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
        this.app = (SimpleApplication) app;
        this.assetManager = this.app.getAssetManager();
//        inputManager = this.app.getInputManager();
//        joyManager = this.app.getJoyManager();
        PlayerControlMenu.compoundManager = Main.getCompoundManager();
        this.audioRenderer = this.app.getAudioRenderer();
        this.guiViewPort = this.app.getViewPort();
        this.stateManager = this.app.getStateManager();
        this.niftyDisplay = Main.getNiftyDisplay();
        nifty = Main.getNifty();
        nifty.registerScreenController(this);
        nifty.addXml("Interface/GUIS/PlayerControlMenu.xml");
        nifty.gotoScreen("PlayerControlMenu");          //Just for the first one, go to the start screen
        nifty.setDebugOptionPanelColors(true); // Leave it on true for now
        
        keymapListener = new KeyMapListener();
        
        compoundManager.addRawInputListener(keymapListener);
        compoundManager.addListener(this, "Change");
    }

    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() 
    {
        backButtonPanelElement = screen.findElementByName("BackButtonPanel");
        /* Column 1 */
        acceptPanelElement = screen.findElementByName("AcceptPanel");
        backPanelElement = screen.findElementByName("BackPanel");
        pausePanelElement = screen.findElementByName("PausePanel");
        leftPanelElement = screen.findElementByName("LeftPanel");
        rightPanelElement = screen.findElementByName("RightPanel");
        upPanelElement = screen.findElementByName("UpPanel");
        downPanelElement = screen.findElementByName("DownPanel");
        jumpPanelElement = screen.findElementByName("JumpPanel");
        joystickPanelElement = screen.findElementByName("JoystickPanel");
        joystickSettingsPanelElement = screen.findElementByName("JoystickSettingsPanel");
        /* Column 2 */
        attackPanelElement = screen.findElementByName("AttackPanel");
        specialPanelElement = screen.findElementByName("SpecialPanel");
        grabPanelElement = screen.findElementByName("GrabPanel");
        shieldPanelElement = screen.findElementByName("ShieldPanel");
        taunt1PanelElement = screen.findElementByName("Taunt1Panel");
        taunt2PanelElement = screen.findElementByName("Taunt2Panel");
        taunt3PanelElement = screen.findElementByName("Taunt3Panel");
        restorePanelElement = screen.findElementByName("RestorePanel");
        savePanelElement = screen.findElementByName("SavePanel");
        loadPanelElement = screen.findElementByName("LoadPanel");
        
        /* Column 1 */
        acceptNameTextRenderer = screen.findElementByName("AcceptText").getRenderer(TextRenderer.class);
        backNameTextRenderer = screen.findElementByName("BackText").getRenderer(TextRenderer.class);
        pauseNameTextRenderer = screen.findElementByName("PauseText").getRenderer(TextRenderer.class);
        leftNameTextRenderer = screen.findElementByName("LeftText").getRenderer(TextRenderer.class);
        rightNameTextRenderer = screen.findElementByName("RightText").getRenderer(TextRenderer.class);
        upNameTextRenderer = screen.findElementByName("UpText").getRenderer(TextRenderer.class);
        downNameTextRenderer = screen.findElementByName("DownText").getRenderer(TextRenderer.class);
        jumpNameTextRenderer = screen.findElementByName("JumpText").getRenderer(TextRenderer.class);
        /* Column 2 */
        attackNameTextRenderer = screen.findElementByName("AttackText").getRenderer(TextRenderer.class);
        specialNameTextRenderer = screen.findElementByName("SpecialText").getRenderer(TextRenderer.class);
        grabNameTextRenderer = screen.findElementByName("GrabText").getRenderer(TextRenderer.class);
        shieldNameTextRenderer = screen.findElementByName("ShieldText").getRenderer(TextRenderer.class);
        taunt1NameTextRenderer = screen.findElementByName("Taunt1Text").getRenderer(TextRenderer.class);
        taunt2NameTextRenderer = screen.findElementByName("Taunt2Text").getRenderer(TextRenderer.class);
        taunt3NameTextRenderer = screen.findElementByName("Taunt3Text").getRenderer(TextRenderer.class);
        
        /* Column 1 */
        acceptTextRenderer = screen.findElementByName("AcceptControlText").getRenderer(TextRenderer.class);
        backTextRenderer = screen.findElementByName("BackControlText").getRenderer(TextRenderer.class);
        pauseTextRenderer = screen.findElementByName("PauseControlText").getRenderer(TextRenderer.class);
        leftTextRenderer = screen.findElementByName("LeftControlText").getRenderer(TextRenderer.class);
        rightTextRenderer = screen.findElementByName("RightControlText").getRenderer(TextRenderer.class);
        upTextRenderer = screen.findElementByName("UpControlText").getRenderer(TextRenderer.class);
        downTextRenderer = screen.findElementByName("DownControlText").getRenderer(TextRenderer.class);
        jumpTextRenderer = screen.findElementByName("JumpControlText").getRenderer(TextRenderer.class);
        joystickTextRenderer = screen.findElementByName("JoystickText").getRenderer(TextRenderer.class);
        joystickSettingsTextRenderer = screen.findElementByName("JoystickSettingsText").getRenderer(TextRenderer.class);
        /* Column 2 */
        attackTextRenderer = screen.findElementByName("AttackControlText").getRenderer(TextRenderer.class);
        specialTextRenderer = screen.findElementByName("SpecialControlText").getRenderer(TextRenderer.class);
        grabTextRenderer = screen.findElementByName("GrabControlText").getRenderer(TextRenderer.class);
        shieldTextRenderer = screen.findElementByName("ShieldControlText").getRenderer(TextRenderer.class);
        taunt1TextRenderer = screen.findElementByName("Taunt1ControlText").getRenderer(TextRenderer.class);
        taunt2TextRenderer = screen.findElementByName("Taunt2ControlText").getRenderer(TextRenderer.class);
        taunt3TextRenderer = screen.findElementByName("Taunt3ControlText").getRenderer(TextRenderer.class);
        restoreTextRenderer = screen.findElementByName("RestoreText").getRenderer(TextRenderer.class);
        saveTextRenderer = screen.findElementByName("SaveText").getRenderer(TextRenderer.class);
        loadTextRenderer = screen.findElementByName("LoadText").getRenderer(TextRenderer.class);
        
        backButtonImageRenderer = screen.findElementByName("BackButtonImage").getRenderer(ImageRenderer.class);
        /* Column 1 */
        acceptImageRenderer = screen.findElementByName("AcceptImage").getRenderer(ImageRenderer.class);
        backImageRenderer = screen.findElementByName("BackImage").getRenderer(ImageRenderer.class);
        pauseImageRenderer = screen.findElementByName("PauseImage").getRenderer(ImageRenderer.class);
        leftImageRenderer = screen.findElementByName("LeftImage").getRenderer(ImageRenderer.class);
        rightImageRenderer = screen.findElementByName("RightImage").getRenderer(ImageRenderer.class);
        upImageRenderer = screen.findElementByName("UpImage").getRenderer(ImageRenderer.class);
        downImageRenderer = screen.findElementByName("DownImage").getRenderer(ImageRenderer.class);
        jumpImageRenderer = screen.findElementByName("JumpImage").getRenderer(ImageRenderer.class);
        joystickImageRenderer = screen.findElementByName("JoystickImage").getRenderer(ImageRenderer.class);
        joystickSettingsImageRenderer = screen.findElementByName("JoystickSettingsImage").getRenderer(ImageRenderer.class);
        /* Column 2 */
        attackImageRenderer = screen.findElementByName("AttackImage").getRenderer(ImageRenderer.class);
        specialImageRenderer = screen.findElementByName("SpecialImage").getRenderer(ImageRenderer.class);
        grabImageRenderer = screen.findElementByName("GrabImage").getRenderer(ImageRenderer.class);
        shieldImageRenderer = screen.findElementByName("ShieldImage").getRenderer(ImageRenderer.class);
        taunt1ImageRenderer = screen.findElementByName("Taunt1Image").getRenderer(ImageRenderer.class);
        taunt2ImageRenderer = screen.findElementByName("Taunt2Image").getRenderer(ImageRenderer.class);
        taunt3ImageRenderer = screen.findElementByName("Taunt3Image").getRenderer(ImageRenderer.class);
        restoreImageRenderer = screen.findElementByName("RestoreImage").getRenderer(ImageRenderer.class);
        saveImageRenderer = screen.findElementByName("SaveImage").getRenderer(ImageRenderer.class);
        loadImageRenderer = screen.findElementByName("LoadImage").getRenderer(ImageRenderer.class);
        
        backImageNormal = nifty.createImage("Interface/General/BackButton.png", false);
        backImageHover = nifty.createImage("Interface/General/BackButtonHover.png", false);
        
        edgeImageNormal = nifty.createImage("Interface/General/ButtonEdgeStandard.png", false);
        edgeImageHover = nifty.createImage("Interface/General/ButtonEdgeHover.png", false);
        
        String areaProviderProperty = ImageModeHelper.getAreaProviderProperty("resize:14,2,14,14,14,2,14,2,14,2,14,14");
        String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty("resize:14,2,14,14,14,2,14,2,14,2,14,14");
        
        edgeImageNormal.setImageMode(ImageModeFactory.getSharedInstance().createImageMode(areaProviderProperty, renderStrategyProperty));
        edgeImageHover.setImageMode(ImageModeFactory.getSharedInstance().createImageMode(areaProviderProperty, renderStrategyProperty));
        
        currentLayout = new ButtonLayout(nullID, backButtonID, backButtonID, backButtonID, backButtonID);
        
        checkScheme();
        initScheme();
        initJoy();
    }

    public void onEndScreen() 
    {
        // TODO: Find some way to unregister listener, without impacting rest of file.
//        inputManager.deleteMapping("Change");
//        inputManager.removeRawInputListener(keymapListener);
        System.out.println("Ending Control Menu");
        compoundManager.deleteMapping("Accept");
        compoundManager.deleteMapping("Right");
        compoundManager.deleteMapping("Left");
        compoundManager.deleteMapping("Up");
        compoundManager.deleteMapping("Down");
        
        compoundManager.deleteMapping("Change");
        compoundManager.removeRawInputListener(keymapListener);
        maintainScheme();
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
        Main.joysticks = compoundManager.getJoysticks();
        compoundManager.setAxisDeadZone(0.5f);
        initJoy();
    }
    
    public void joystickSettings()
    {
        JoystickSettingsMenu joystickSettingsMenu = new JoystickSettingsMenu(this, player);
        joystickSettingsMenu.initiate(app);  
//        reeditRegistry(Main.joysticks[0]);
//        setupControls(currentScheme);
//        acceptTextRenderer.setText(TriggerFilter(currentScheme.getAccept()));
//        System.out.println("Accept: " + currentScheme.getAcceptName());
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
        acceptTextRenderer.setText(TriggerFilter(scheme.getAccept()));
        backTextRenderer.setText(TriggerFilter(scheme.getBack()));
        pauseTextRenderer.setText(TriggerFilter(scheme.getPause()));
        leftTextRenderer.setText(TriggerFilter(scheme.getLeft()));
        rightTextRenderer.setText(TriggerFilter(scheme.getRight()));
        upTextRenderer.setText(TriggerFilter(scheme.getUp()));
        downTextRenderer.setText(TriggerFilter(scheme.getDown()));
        jumpTextRenderer.setText(TriggerFilter(scheme.getJump()));
        attackTextRenderer.setText(TriggerFilter(scheme.getAttack()));
        specialTextRenderer.setText(TriggerFilter(scheme.getSpecial()));
        grabTextRenderer.setText(TriggerFilter(scheme.getGrab()));
        shieldTextRenderer.setText(TriggerFilter(scheme.getShield()));
        taunt1TextRenderer.setText(TriggerFilter(scheme.getTaunt1()));
        taunt2TextRenderer.setText(TriggerFilter(scheme.getTaunt2()));
        taunt3TextRenderer.setText(TriggerFilter(scheme.getTaunt3()));
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
    
    private void initJoy() 
    {
        if (Main.joysticks.length != 0)
        {
            JoystickRegistry.addJoySettings(Main.joysticks[0]);
            if (Main.joysticks.length >= 2)
            {
                JoystickRegistry.addJoySettings(Main.joysticks[1]);
            }
            // Temporary Testing
//            editRegistry(Main.joysticks[0]);
            Main.joysticks[0].getButton("0").assignButton("Accept");
            
            if (Main.joysticks[0].getName().equals("Logitech Dual Action"))
            {
                compoundManager.deleteMapping("Accept");
                Main.joysticks[0].getButton("2").assignButton("Accept");
            }
            
            Main.joysticks[0].getXAxis().assignAxis("Right", "Left");
            Main.joysticks[0].getYAxis().assignAxis("Down", "Up");
            compoundManager.addListener(this, "Accept", "Right", "Left", "Down", "Up");
        }
    }

    public void onAction(String name, boolean isPressed, float tpf) 
    {
        if ("".equals(currentControl))
        {
            if (isPressed && name.equals("Accept"))
            {
                System.out.println("Accept");
                keymapListener.justActivated();
                keymapListener.reset();
                niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_RETURN, '0', true, false));
            }
            else if (isPressed && name.equals("Right"))
            {
                System.out.println("Right");
                niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_D, '0', true, false));
            }
            else if (isPressed && name.equals("Left"))
            {
                System.out.println("Left");
                niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_A, '0', true, false));
            }
            else if (isPressed && name.equals("Up"))
            {
                System.out.println("Up");
                niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_W, '0', true, false));
            }
            else if (isPressed && name.equals("Down"))
            {
                System.out.println("Down");
                niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_S, '0', true, false));
            }
            return;
        }
        changeControlTrigger(currentControl, keymapListener.getLastTrigger());
    }
    
    public static void changeControlTrigger(String control, Trigger trigger)
    {
        if (trigger == null)
        {
            System.out.println("Null Trigger");
            return;
        }
        System.out.println("Recieved Trigger Name: " + trigger.getName());
        if ("Accept".equals(control))
        {
            currentScheme.setAccept(trigger);
//            currentScheme.setAccept(name);
            acceptTextRenderer.setText(TriggerFilter(currentScheme.getAccept()));
            
        }
        else if ("Back".equals(control))
        {
            currentScheme.setBack(trigger);
//            currentScheme.setBack(name);
            backTextRenderer.setText(TriggerFilter(currentScheme.getBack()));
        }
        else if ("Pause".equals(control))
        {
            currentScheme.setPause(trigger);
//            currentScheme.setPause(name);
            pauseTextRenderer.setText(TriggerFilter(currentScheme.getPause()));
        }
        else if ("Left".equals(control))
        {
            currentScheme.setLeft(trigger);
//            currentScheme.setLeft(name);
            leftTextRenderer.setText(TriggerFilter(currentScheme.getLeft()));
        }
        else if ("Right".equals(control))
        {
            currentScheme.setRight(trigger);
//            currentScheme.setRight(name);
            rightTextRenderer.setText(TriggerFilter(currentScheme.getRight()));
        }
        else if ("Up".equals(control))
        {
            currentScheme.setUp(trigger);
//            currentScheme.setUp(name);
            upTextRenderer.setText(TriggerFilter(currentScheme.getUp()));
        }
        else if ("Down".equals(control))
        {
            currentScheme.setDown(trigger);
//            currentScheme.setDown(name);
            downTextRenderer.setText(TriggerFilter(currentScheme.getDown()));
        }
        else if ("Jump".equals(control))
        {
            currentScheme.setJump(trigger);
//            currentScheme.setJump(name);
            jumpTextRenderer.setText(TriggerFilter(currentScheme.getJump()));
        }
        else if ("Attack".equals(control))
        {
            currentScheme.setAttack(trigger);
//            currentScheme.setAttack(name);
            attackTextRenderer.setText(TriggerFilter(currentScheme.getAttack()));
        }
        else if ("Special".equals(control))
        {
            currentScheme.setSpecial(trigger);
//            currentScheme.setSpecial(name);
            specialTextRenderer.setText(TriggerFilter(currentScheme.getSpecial()));
        }
        else if ("Grab".equals(control))
        {
            currentScheme.setGrab(trigger);
//            currentScheme.setGrab(name);
            grabTextRenderer.setText(TriggerFilter(currentScheme.getGrab()));
        }
        else if ("Shield".equals(control))
        {
            currentScheme.setShield(trigger);
//            currentScheme.setShield(name);
            shieldTextRenderer.setText(TriggerFilter(currentScheme.getShield()));
        }
        else if ("Taunt 1".equals(control))
        {
            currentScheme.setTaunt1(trigger);
//            currentScheme.setTaunt1(name);
            taunt1TextRenderer.setText(TriggerFilter(currentScheme.getTaunt1()));
        }
        else if ("Taunt 2".equals(control))
        {
            currentScheme.setTaunt2(trigger);
//            currentScheme.setTaunt2(name);
            taunt2TextRenderer.setText(TriggerFilter(currentScheme.getTaunt2()));
        }
        else if ("Taunt 3".equals(control))
        {
            currentScheme.setTaunt3(trigger);
//            currentScheme.setTaunt3(name);
            taunt3TextRenderer.setText(TriggerFilter(currentScheme.getTaunt3()));
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
    
    /* 0
     *     1    11
     *     2    12
     *     3    13
     *     4    14
     *     5    15
     *     6    16
     *     7    17
     *     8    18
     *     
     *     9    19
     *     10   20
     */
    
    
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
                case acceptID:
                {
                    currentLayout = acceptLayout;
                    acceptImageRenderer.setImage(edgeImageHover);
                    acceptTextRenderer.setColor(hoverTextColor);
                    acceptNameTextRenderer.setColor(hoverTextColor);
                    acceptWasClickable = true;
                    break;
                }
                case backID:
                {
                    currentLayout = backLayout;
                    backImageRenderer.setImage(edgeImageHover);
                    backTextRenderer.setColor(hoverTextColor);
                    backNameTextRenderer.setColor(hoverTextColor);
                    backWasClickable = true;
                    break;
                }
                case pauseID:
                {
                    currentLayout = pauseLayout;
                    pauseImageRenderer.setImage(edgeImageHover);
                    pauseTextRenderer.setColor(hoverTextColor);
                    pauseNameTextRenderer.setColor(hoverTextColor);
                    pauseWasClickable = true;
                    break;
                }
                case leftID:
                {
                    currentLayout = leftLayout;
                    leftImageRenderer.setImage(edgeImageHover);
                    leftTextRenderer.setColor(hoverTextColor);
                    leftNameTextRenderer.setColor(hoverTextColor);
                    leftWasClickable = true;
                    break;
                }
                case rightID:
                {
                    currentLayout = acceptLayout;
                    rightImageRenderer.setImage(edgeImageHover);
                    rightTextRenderer.setColor(hoverTextColor);
                    rightNameTextRenderer.setColor(hoverTextColor);
                    rightWasClickable = true;
                    break;
                }
                case upID:
                {
                    currentLayout = upLayout;
                    upImageRenderer.setImage(edgeImageHover);
                    upTextRenderer.setColor(hoverTextColor);
                    upNameTextRenderer.setColor(hoverTextColor);
                    upWasClickable = true;
                    break;
                }
                case downID:
                {
                    currentLayout = downLayout;
                    downImageRenderer.setImage(edgeImageHover);
                    downTextRenderer.setColor(hoverTextColor);
                    downNameTextRenderer.setColor(hoverTextColor);
                    downWasClickable = true;
                    break;
                }
                case jumpID:
                {
                    currentLayout = jumpLayout;
                    jumpImageRenderer.setImage(edgeImageHover);
                    jumpTextRenderer.setColor(hoverTextColor);
                    jumpNameTextRenderer.setColor(hoverTextColor);
                    jumpWasClickable = true;
                    break;
                }
                case joystickID:
                {
                    currentLayout = joystickLayout;
                    joystickImageRenderer.setImage(edgeImageHover);
                    joystickTextRenderer.setColor(hoverTextColor);
                    joystickWasClickable = true;
                    break;
                }
                case joystickSettingsID:
                {
                    currentLayout = joystickSettingsLayout;
                    joystickSettingsImageRenderer.setImage(edgeImageHover);
                    joystickSettingsTextRenderer.setColor(hoverTextColor);
                    joystickSettingsWasClickable = true;
                    break;
                }
                case attackID:
                {
                    currentLayout = attackLayout;
                    attackImageRenderer.setImage(edgeImageHover);
                    attackTextRenderer.setColor(hoverTextColor);
                    attackNameTextRenderer.setColor(hoverTextColor);
                    attackWasClickable = true;
                    break;
                }
                case specialID:
                {
                    currentLayout = specialLayout;
                    specialImageRenderer.setImage(edgeImageHover);
                    specialTextRenderer.setColor(hoverTextColor);
                    specialNameTextRenderer.setColor(hoverTextColor);
                    specialWasClickable = true;
                    break;
                }
                case grabID:
                {
                    currentLayout = grabLayout;
                    grabImageRenderer.setImage(edgeImageHover);
                    grabTextRenderer.setColor(hoverTextColor);
                    grabNameTextRenderer.setColor(hoverTextColor);
                    grabWasClickable = true;
                    break;
                }
                case shieldID:
                {
                    currentLayout = shieldLayout;
                    shieldImageRenderer.setImage(edgeImageHover);
                    shieldTextRenderer.setColor(hoverTextColor);
                    shieldNameTextRenderer.setColor(hoverTextColor);
                    shieldWasClickable = true;
                    break;
                }
                case taunt1ID:
                {
                    currentLayout = taunt1Layout;
                    taunt1ImageRenderer.setImage(edgeImageHover);
                    taunt1TextRenderer.setColor(hoverTextColor);
                    taunt1NameTextRenderer.setColor(hoverTextColor);
                    taunt1WasClickable = true;
                    break;
                }
                case taunt2ID:
                {
                    currentLayout = taunt2Layout;
                    taunt2ImageRenderer.setImage(edgeImageHover);
                    taunt2TextRenderer.setColor(hoverTextColor);
                    taunt2NameTextRenderer.setColor(hoverTextColor);
                    taunt2WasClickable = true;
                    break;
                }
                case taunt3ID:
                {
                    currentLayout = taunt3Layout;
                    taunt3ImageRenderer.setImage(edgeImageHover);
                    taunt3TextRenderer.setColor(hoverTextColor);
                    taunt3NameTextRenderer.setColor(hoverTextColor);
                    taunt3WasClickable = true;
                    break;
                }
                case restoreID:
                {
                    currentLayout = restoreLayout;
                    restoreImageRenderer.setImage(edgeImageHover);
                    restoreTextRenderer.setColor(hoverTextColor);
                    restoreWasClickable = true;
                    break;
                }
                case saveID:
                {
                    currentLayout = saveLayout;
                    saveImageRenderer.setImage(edgeImageHover);
                    saveTextRenderer.setColor(hoverTextColor);
                    saveWasClickable = true;
                    break;
                }
                case loadID:
                {
                    currentLayout = loadLayout;
                    loadImageRenderer.setImage(edgeImageHover);
                    loadTextRenderer.setColor(hoverTextColor);
                    loadWasClickable = true;
                    break;
                }
                default:
                {
                
                }
            }
        }
        if (newButton == currentLayout.Identity())
        {
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
            case acceptID:
            {
                currentLayout = acceptLayout;
                acceptImageRenderer.setImage(edgeImageNormal);
                acceptTextRenderer.setColor(defaultTextColor);
                acceptNameTextRenderer.setColor(defaultTextColor);
                acceptWasClickable = false;
                break;
            }
            case backID:
            {
                currentLayout = backLayout;
                backImageRenderer.setImage(edgeImageNormal);
                backTextRenderer.setColor(defaultTextColor);
                backNameTextRenderer.setColor(defaultTextColor);
                backWasClickable = false;
                break;
            }
            case pauseID:
            {
                currentLayout = pauseLayout;
                pauseImageRenderer.setImage(edgeImageNormal);
                pauseTextRenderer.setColor(defaultTextColor);
                pauseNameTextRenderer.setColor(defaultTextColor);
                pauseWasClickable = false;
                break;
            }
            case leftID:
            {
                currentLayout = leftLayout;
                leftImageRenderer.setImage(edgeImageNormal);
                leftTextRenderer.setColor(defaultTextColor);
                leftNameTextRenderer.setColor(defaultTextColor);
                leftWasClickable = false;
                break;
            }
            case rightID:
            {
                currentLayout = rightLayout;
                rightImageRenderer.setImage(edgeImageNormal);
                rightTextRenderer.setColor(defaultTextColor);
                rightNameTextRenderer.setColor(defaultTextColor);
                rightWasClickable = false;
                break;
            }
            case upID:
            {
                currentLayout = upLayout;
                upImageRenderer.setImage(edgeImageNormal);
                upTextRenderer.setColor(defaultTextColor);
                upNameTextRenderer.setColor(defaultTextColor);
                upWasClickable = false;
                break;
            }
            case downID:
            {
                currentLayout = downLayout;
                downImageRenderer.setImage(edgeImageNormal);
                downTextRenderer.setColor(defaultTextColor);
                downNameTextRenderer.setColor(defaultTextColor);
                downWasClickable = false;
                break;
            }
            case jumpID:
            {
                currentLayout = jumpLayout;
                jumpImageRenderer.setImage(edgeImageNormal);
                jumpTextRenderer.setColor(defaultTextColor);
                jumpNameTextRenderer.setColor(defaultTextColor);
                jumpWasClickable = false;
                break;
            }
            case joystickID:
            {
                currentLayout = joystickLayout;
                joystickImageRenderer.setImage(edgeImageNormal);
                joystickTextRenderer.setColor(defaultTextColor);
                joystickWasClickable = false;
                break;
            }
            case joystickSettingsID:
            {
                currentLayout = joystickSettingsLayout;
                joystickSettingsImageRenderer.setImage(edgeImageNormal);
                joystickSettingsTextRenderer.setColor(defaultTextColor);
                joystickSettingsWasClickable = false;
                break;
            }
            case attackID:
            {
                currentLayout = attackLayout;
                attackImageRenderer.setImage(edgeImageNormal);
                attackTextRenderer.setColor(defaultTextColor);
                attackNameTextRenderer.setColor(defaultTextColor);
                attackWasClickable = false;
                break;
            }
            case specialID:
            {
                currentLayout = specialLayout;
                specialImageRenderer.setImage(edgeImageNormal);
                specialTextRenderer.setColor(defaultTextColor);
                specialNameTextRenderer.setColor(defaultTextColor);
                specialWasClickable = false;
                break;
            }
            case grabID:
            {
                currentLayout = grabLayout;
                grabImageRenderer.setImage(edgeImageNormal);
                grabTextRenderer.setColor(defaultTextColor);
                grabNameTextRenderer.setColor(defaultTextColor);
                grabWasClickable = false;
                break;
            }
            case shieldID:
            {
                currentLayout = shieldLayout;
                shieldImageRenderer.setImage(edgeImageNormal);
                shieldTextRenderer.setColor(defaultTextColor);
                shieldNameTextRenderer.setColor(defaultTextColor);
                shieldWasClickable = false;
                break;
            }
            case taunt1ID:
            {
                currentLayout = taunt1Layout;
                taunt1ImageRenderer.setImage(edgeImageNormal);
                taunt1TextRenderer.setColor(defaultTextColor);
                taunt1NameTextRenderer.setColor(defaultTextColor);
                taunt1WasClickable = false;
                break;
            }
            case taunt2ID:
            {
                currentLayout = taunt2Layout;
                taunt2ImageRenderer.setImage(edgeImageNormal);
                taunt2TextRenderer.setColor(defaultTextColor);
                taunt2NameTextRenderer.setColor(defaultTextColor);
                taunt2WasClickable = false;
                break;
            }
            case taunt3ID:
            {
                currentLayout = taunt3Layout;
                taunt3ImageRenderer.setImage(edgeImageNormal);
                taunt3TextRenderer.setColor(defaultTextColor);
                taunt3NameTextRenderer.setColor(defaultTextColor);
                taunt3WasClickable = false;
                break;
            }
            case restoreID:
            {
                currentLayout = restoreLayout;
                restoreImageRenderer.setImage(edgeImageNormal);
                restoreTextRenderer.setColor(defaultTextColor);
                restoreWasClickable = false;
                break;
            }
            case saveID:
            {
                currentLayout = saveLayout;
                saveImageRenderer.setImage(edgeImageNormal);
                saveTextRenderer.setColor(defaultTextColor);
                saveWasClickable = false;
                break;
            }
            case loadID:
            {
                currentLayout = loadLayout;
                loadImageRenderer.setImage(edgeImageNormal);
                loadTextRenderer.setColor(defaultTextColor);
                loadWasClickable = false;
                break;
            }
            default:
            {
                
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
            case acceptID:
            {
                currentLayout = acceptLayout;
                acceptImageRenderer.setImage(edgeImageHover);
                acceptTextRenderer.setColor(hoverTextColor);
                acceptNameTextRenderer.setColor(hoverTextColor);
                acceptWasClickable = true;
                break;
            }
            case backID:
            {
                currentLayout = backLayout;
                backImageRenderer.setImage(edgeImageHover);
                backTextRenderer.setColor(hoverTextColor);
                backNameTextRenderer.setColor(hoverTextColor);
                backWasClickable = true;
                break;
            }
            case pauseID:
            {
                currentLayout = pauseLayout;
                pauseImageRenderer.setImage(edgeImageHover);
                pauseTextRenderer.setColor(hoverTextColor);
                pauseNameTextRenderer.setColor(hoverTextColor);
                pauseWasClickable = true;
                break;
            }
            case leftID:
            {
                currentLayout = leftLayout;
                leftImageRenderer.setImage(edgeImageHover);
                leftTextRenderer.setColor(hoverTextColor);
                leftNameTextRenderer.setColor(hoverTextColor);
                leftWasClickable = true;
                break;
            }
            case rightID:
            {
                currentLayout = rightLayout;
                rightImageRenderer.setImage(edgeImageHover);
                rightTextRenderer.setColor(hoverTextColor);
                rightNameTextRenderer.setColor(hoverTextColor);
                rightWasClickable = true;
                break;
            }
            case upID:
            {
                currentLayout = upLayout;
                upImageRenderer.setImage(edgeImageHover);
                upTextRenderer.setColor(hoverTextColor);
                upNameTextRenderer.setColor(hoverTextColor);
                upWasClickable = true;
                break;
            }
            case downID:
            {
                currentLayout = downLayout;
                downImageRenderer.setImage(edgeImageHover);
                downTextRenderer.setColor(hoverTextColor);
                downNameTextRenderer.setColor(hoverTextColor);
                downWasClickable = true;
                break;
            }
            case jumpID:
            {
                currentLayout = jumpLayout;
                jumpImageRenderer.setImage(edgeImageHover);
                jumpTextRenderer.setColor(hoverTextColor);
                jumpNameTextRenderer.setColor(hoverTextColor);
                jumpWasClickable = true;
                break;
            }
            case joystickID:
            {
                currentLayout = joystickLayout;
                joystickImageRenderer.setImage(edgeImageHover);
                joystickTextRenderer.setColor(hoverTextColor);
                joystickWasClickable = true;
                break;
            }
            case joystickSettingsID:
            {
                currentLayout = joystickSettingsLayout;
                joystickSettingsImageRenderer.setImage(edgeImageHover);
                joystickSettingsTextRenderer.setColor(hoverTextColor);
                joystickSettingsWasClickable = true;
                break;
            }
            case attackID:
            {
                currentLayout = attackLayout;
                attackImageRenderer.setImage(edgeImageHover);
                attackTextRenderer.setColor(hoverTextColor);
                attackNameTextRenderer.setColor(hoverTextColor);
                attackWasClickable = true;
                break;
            }
            case specialID:
            {
                currentLayout = specialLayout;
                specialImageRenderer.setImage(edgeImageHover);
                specialTextRenderer.setColor(hoverTextColor);
                specialNameTextRenderer.setColor(hoverTextColor);
                specialWasClickable = true;
                break;
            }
            case grabID:
            {
                currentLayout = grabLayout;
                grabImageRenderer.setImage(edgeImageHover);
                grabTextRenderer.setColor(hoverTextColor);
                grabNameTextRenderer.setColor(hoverTextColor);
                grabWasClickable = true;
                break;
            }
            case shieldID:
            {
                currentLayout = shieldLayout;
                shieldImageRenderer.setImage(edgeImageHover);
                shieldTextRenderer.setColor(hoverTextColor);
                shieldNameTextRenderer.setColor(hoverTextColor);
                shieldWasClickable = true;
                break;
            }
            case taunt1ID:
            {
                currentLayout = taunt1Layout;
                taunt1ImageRenderer.setImage(edgeImageHover);
                taunt1TextRenderer.setColor(hoverTextColor);
                taunt1NameTextRenderer.setColor(hoverTextColor);
                taunt1WasClickable = true;
                break;
            }
            case taunt2ID:
            {
                currentLayout = taunt2Layout;
                taunt2ImageRenderer.setImage(edgeImageHover);
                taunt2TextRenderer.setColor(hoverTextColor);
                taunt2NameTextRenderer.setColor(hoverTextColor);
                taunt2WasClickable = true;
                break;
            }
            case taunt3ID:
            {
                currentLayout = taunt3Layout;
                taunt3ImageRenderer.setImage(edgeImageHover);
                taunt3TextRenderer.setColor(hoverTextColor);
                taunt3NameTextRenderer.setColor(hoverTextColor);
                taunt3WasClickable = true;
                break;
            }
            case restoreID:
            {
                currentLayout = restoreLayout;
                restoreImageRenderer.setImage(edgeImageHover);
                restoreTextRenderer.setColor(hoverTextColor);
                restoreWasClickable = true;
                break;
            }
            case saveID:
            {
                currentLayout = saveLayout;
                saveImageRenderer.setImage(edgeImageHover);
                saveTextRenderer.setColor(hoverTextColor);
                saveWasClickable = true;
                break;
            }
            case loadID:
            {
                currentLayout = loadLayout;
                loadImageRenderer.setImage(edgeImageHover);
                loadTextRenderer.setColor(hoverTextColor);
                loadWasClickable = true;
                break;
            }
            default:
            {
                
            }
        }
    }
    
    public void activateActive(byte newButton)
    {
        switch (currentLayout.Identity())
        {
            case backButtonID: 
            {
                goBack();
                break;
            }
            case acceptID:
            {
                changeControl("Accept");
                break;
            }
            case backID:
            {
                changeControl("Back");
                break;
            }
            case pauseID:
            {
                changeControl("Pause");
                break;
            }
            case leftID:
            {
                changeControl("Left");
                break;
            }
            case rightID:
            {
                changeControl("Right");
                break;
            }
            case upID:
            {
                changeControl("Up");
                break;
            }
            case downID:
            {
                changeControl("Down");
                break;
            }
            case jumpID:
            {
                changeControl("Jump");
                break;
            }
            case joystickID:
            {
                useJoystick();
                break;
            }
            case joystickSettingsID:
            {
                joystickSettings();
                break;
            }
            case attackID:
            {
                changeControl("Attack");
                break;
            }
            case specialID:
            {
                changeControl("Special");
                break;
            }
            case grabID:
            {
                changeControl("Grab");
                break;
            }
            case shieldID:
            {
                changeControl("Shield");
                break;
            }
            case taunt1ID:
            {
                changeControl("Taunt 1");
                break;
            }
            case taunt2ID:
            {
                changeControl("Taunt 2");
                break;
            }
            case taunt3ID:
            {
                changeControl("Taunt 3");
                break;
            }
            case restoreID:
            {
                restoreDefaults();
                break;
            }
            case saveID:
            {
                // saveScheme();
                break;
            }
            case loadID:
            {
                // loadScheme();
                break;
            }
            default:
            {
                
            }
        }
    }

    public boolean keyEvent(NiftyInputEvent event) 
    {
        if (currentControl.equals(""))
        {
            if (event == NiftyInputEvent.Activate)
            {
                System.out.println("ACTIVATED");
                keymapListener.justActivated();
                activateActive(currentLayout.Identity());
                return true;
            }
            else if (event == NiftyInputEvent.Backspace)
            {
                System.out.println("Back");
                goBack();
                return true;
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
        }
        return false;
    }
    
    @NiftyEventSubscriber(id="MouseCatcher")
    public void buttonCaller(String id, NiftyMouseEvent event)
    {
        int x = event.getMouseX();
        int y = event.getMouseY();
        boolean wasClick = event.isButton0Down();
        boolean wasRelease = event.isButton0Release();
        
        if (wasClick)
        {
            keysUsed = false;
            backButtonResetDone = false;
            acceptResetDone = false;
            backResetDone = false;
            pauseResetDone = false;
            leftResetDone = false;
            rightResetDone = false;
            upResetDone = false;
            downResetDone = false;
            jumpResetDone = false;
            joystickResetDone = false;
            joystickSettingsResetDone = false;
            attackResetDone = false;
            specialResetDone = false;
            grabResetDone = false;
            shieldResetDone = false;
            taunt1ResetDone = false;
            taunt2ResetDone = false;
            taunt3ResetDone = false;
            restoreResetDone = false;
            saveResetDone = false;
            loadResetDone = false;
        }
        
        onBackButtonHover(backButtonPanelElement.isMouseInsideElement(x, y), wasClick);
        
        /* Column 1 */
        onAcceptHover(acceptPanelElement.isMouseInsideElement(x, y), wasClick);
        onBackHover(backPanelElement.isMouseInsideElement(x, y), wasClick);
        onPauseHover(pausePanelElement.isMouseInsideElement(x, y), wasClick);
        onLeftHover(leftPanelElement.isMouseInsideElement(x, y), wasClick);
        onRightHover(rightPanelElement.isMouseInsideElement(x, y), wasClick);
        onUpHover(upPanelElement.isMouseInsideElement(x, y), wasClick);
        onDownHover(downPanelElement.isMouseInsideElement(x, y), wasClick);
        onJumpHover(jumpPanelElement.isMouseInsideElement(x, y), wasClick);
        onJoystickHover(joystickPanelElement.isMouseInsideElement(x, y), wasClick);
        onJoystickSettingsHover(joystickSettingsPanelElement.isMouseInsideElement(x, y), wasClick);
        
        /* Column 2 */
        onAttackHover(attackPanelElement.isMouseInsideElement(x, y), wasClick);
        onSpecialHover(specialPanelElement.isMouseInsideElement(x, y), wasClick);
        onGrabHover(grabPanelElement.isMouseInsideElement(x, y), wasClick);
        onShieldHover(shieldPanelElement.isMouseInsideElement(x, y), wasClick);
        onTaunt1Hover(taunt1PanelElement.isMouseInsideElement(x, y), wasClick);
        onTaunt2Hover(taunt2PanelElement.isMouseInsideElement(x, y), wasClick);
        onTaunt3Hover(taunt3PanelElement.isMouseInsideElement(x, y), wasClick);
        onRestoreHover(restorePanelElement.isMouseInsideElement(x, y), wasClick);
        onSaveHover(savePanelElement.isMouseInsideElement(x, y), wasClick);
        onLoadHover(loadPanelElement.isMouseInsideElement(x, y), wasClick);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Hover Methods">
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
    
    /* Column 1 */
    
    public void onAcceptHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(acceptID);
                return;
            }
            if (!acceptWasClickable)
            {
                acceptTextRenderer.setColor(hoverTextColor);
                acceptNameTextRenderer.setColor(hoverTextColor);
                acceptImageRenderer.setImage(edgeImageHover);
                acceptWasClickable = true;
                acceptResetDone = false;
                adaptActive(acceptID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!acceptResetDone)
        {
            acceptTextRenderer.setColor(defaultTextColor);
            acceptNameTextRenderer.setColor(defaultTextColor);
            acceptImageRenderer.setImage(edgeImageNormal);
            acceptWasClickable = false;
            acceptResetDone = true;
        }
    }
    
    public void onBackHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(backID);
                return;
            }
            if (!backWasClickable)
            {
                backTextRenderer.setColor(hoverTextColor);
                backNameTextRenderer.setColor(hoverTextColor);
                backImageRenderer.setImage(edgeImageHover);
                backWasClickable = true;
                backResetDone = false;
                adaptActive(backID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!backResetDone)
        {
            backTextRenderer.setColor(defaultTextColor);
            backNameTextRenderer.setColor(defaultTextColor);
            backImageRenderer.setImage(edgeImageNormal);
            backWasClickable = false;
            backResetDone = true;
        }
    }
    
    public void onPauseHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(pauseID);
                return;
            }
            if (!pauseWasClickable)
            {
                pauseTextRenderer.setColor(hoverTextColor);
                pauseNameTextRenderer.setColor(hoverTextColor);
                pauseImageRenderer.setImage(edgeImageHover);
                pauseWasClickable = true;
                pauseResetDone = false;
                adaptActive(pauseID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!pauseResetDone)
        {
            pauseTextRenderer.setColor(defaultTextColor);
            pauseNameTextRenderer.setColor(defaultTextColor);
            pauseImageRenderer.setImage(edgeImageNormal);
            pauseWasClickable = false;
            pauseResetDone = true;
        }
    }
    
    public void onLeftHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(leftID);
                return;
            }
            if (!leftWasClickable)
            {
                leftTextRenderer.setColor(hoverTextColor);
                leftNameTextRenderer.setColor(hoverTextColor);
                leftImageRenderer.setImage(edgeImageHover);
                leftWasClickable = true;
                leftResetDone = false;
                adaptActive(leftID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!leftResetDone)
        {
            leftTextRenderer.setColor(defaultTextColor);
            leftNameTextRenderer.setColor(defaultTextColor);
            leftImageRenderer.setImage(edgeImageNormal);
            leftWasClickable = false;
            leftResetDone = true;
        }
    }
    
    public void onRightHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(rightID);
                return;
            }
            if (!rightWasClickable)
            {
                rightTextRenderer.setColor(hoverTextColor);
                rightNameTextRenderer.setColor(hoverTextColor);
                rightImageRenderer.setImage(edgeImageHover);
                rightWasClickable = true;
                rightResetDone = false;
                adaptActive(rightID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!rightResetDone)
        {
            rightTextRenderer.setColor(defaultTextColor);
            rightNameTextRenderer.setColor(defaultTextColor);
            rightImageRenderer.setImage(edgeImageNormal);
            rightWasClickable = false;
            rightResetDone = true;
        }
    }
    
    public void onUpHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(upID);
                return;
            }
            if (!upWasClickable)
            {
                upTextRenderer.setColor(hoverTextColor);
                upNameTextRenderer.setColor(hoverTextColor);
                upImageRenderer.setImage(edgeImageHover);
                upWasClickable = true;
                upResetDone = false;
                adaptActive(upID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!upResetDone)
        {
            upTextRenderer.setColor(defaultTextColor);
            upNameTextRenderer.setColor(defaultTextColor);
            upImageRenderer.setImage(edgeImageNormal);
            upWasClickable = false;
            upResetDone = true;
        }
    }
    
    public void onDownHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(downID);
                return;
            }
            if (!downWasClickable)
            {
                downTextRenderer.setColor(hoverTextColor);
                downNameTextRenderer.setColor(hoverTextColor);
                downImageRenderer.setImage(edgeImageHover);
                downWasClickable = true;
                downResetDone = false;
                adaptActive(downID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!downResetDone)
        {
            downTextRenderer.setColor(defaultTextColor);
            downNameTextRenderer.setColor(defaultTextColor);
            downImageRenderer.setImage(edgeImageNormal);
            downWasClickable = false;
            downResetDone = true;
        }
    }
    
    public void onJumpHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(jumpID);
                return;
            }
            if (!jumpWasClickable)
            {
                jumpTextRenderer.setColor(hoverTextColor);
                jumpNameTextRenderer.setColor(hoverTextColor);
                jumpImageRenderer.setImage(edgeImageHover);
                jumpWasClickable = true;
                jumpResetDone = false;
                adaptActive(jumpID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!jumpResetDone)
        {
            jumpTextRenderer.setColor(defaultTextColor);
            jumpNameTextRenderer.setColor(defaultTextColor);
            jumpImageRenderer.setImage(edgeImageNormal);
            jumpWasClickable = false;
            jumpResetDone = true;
        }
    }
    
    public void onJoystickHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(joystickID);
                return;
            }
            if (!joystickWasClickable)
            {
                joystickTextRenderer.setColor(hoverTextColor);
                joystickImageRenderer.setImage(edgeImageHover);
                joystickWasClickable = true;
                joystickResetDone = false;
                adaptActive(joystickID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!joystickResetDone)
        {
            joystickTextRenderer.setColor(defaultTextColor);
            joystickImageRenderer.setImage(edgeImageNormal);
            joystickWasClickable = false;
            joystickResetDone = true;
        }
    }
    
    public void onJoystickSettingsHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(joystickSettingsID);
                return;
            }
            if (!joystickSettingsWasClickable)
            {
                joystickSettingsTextRenderer.setColor(hoverTextColor);
                joystickSettingsImageRenderer.setImage(edgeImageHover);
                joystickSettingsWasClickable = true;
                joystickSettingsResetDone = false;
                adaptActive(joystickSettingsID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!joystickSettingsResetDone)
        {
            joystickSettingsTextRenderer.setColor(defaultTextColor);
            joystickSettingsImageRenderer.setImage(edgeImageNormal);
            joystickSettingsWasClickable = false;
            joystickSettingsResetDone = true;
        }
    }
    
    /* Column 2 */
    
    public void onAttackHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(attackID);
                return;
            }
            if (!attackWasClickable)
            {
                attackTextRenderer.setColor(hoverTextColor);
                attackNameTextRenderer.setColor(hoverTextColor);
                attackImageRenderer.setImage(edgeImageHover);
                attackWasClickable = true;
                attackResetDone = false;
                adaptActive(attackID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!attackResetDone)
        {
            attackTextRenderer.setColor(defaultTextColor);
            attackNameTextRenderer.setColor(defaultTextColor);
            attackImageRenderer.setImage(edgeImageNormal);
            attackWasClickable = false;
            attackResetDone = true;
        }
    }
    
    public void onSpecialHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(specialID);
                return;
            }
            if (!specialWasClickable)
            {
                specialTextRenderer.setColor(hoverTextColor);
                specialNameTextRenderer.setColor(hoverTextColor);
                specialImageRenderer.setImage(edgeImageHover);
                specialWasClickable = true;
                specialResetDone = false;
                adaptActive(specialID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!specialResetDone)
        {
            specialTextRenderer.setColor(defaultTextColor);
            specialNameTextRenderer.setColor(defaultTextColor);
            specialImageRenderer.setImage(edgeImageNormal);
            specialWasClickable = false;
            specialResetDone = true;
        }
    }
    
    public void onGrabHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(grabID);
                return;
            }
            if (!grabWasClickable)
            {
                grabTextRenderer.setColor(hoverTextColor);
                grabNameTextRenderer.setColor(hoverTextColor);
                grabImageRenderer.setImage(edgeImageHover);
                grabWasClickable = true;
                grabResetDone = false;
                adaptActive(grabID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!grabResetDone)
        {
            grabTextRenderer.setColor(defaultTextColor);
            grabNameTextRenderer.setColor(defaultTextColor);
            grabImageRenderer.setImage(edgeImageNormal);
            grabWasClickable = false;
            grabResetDone = true;
        }
    }
    
    public void onShieldHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(shieldID);
                return;
            }
            if (!shieldWasClickable)
            {
                shieldTextRenderer.setColor(hoverTextColor);
                shieldNameTextRenderer.setColor(hoverTextColor);
                shieldImageRenderer.setImage(edgeImageHover);
                shieldWasClickable = true;
                shieldResetDone = false;
                adaptActive(shieldID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!shieldResetDone)
        {
            shieldTextRenderer.setColor(defaultTextColor);
            shieldNameTextRenderer.setColor(defaultTextColor);
            shieldImageRenderer.setImage(edgeImageNormal);
            shieldWasClickable = false;
            shieldResetDone = true;
        }
    }
    
    public void onTaunt1Hover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(taunt1ID);
                return;
            }
            if (!taunt1WasClickable)
            {
                taunt1TextRenderer.setColor(hoverTextColor);
                taunt1NameTextRenderer.setColor(hoverTextColor);
                taunt1ImageRenderer.setImage(edgeImageHover);
                taunt1WasClickable = true;
                taunt1ResetDone = false;
                adaptActive(taunt1ID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!taunt1ResetDone)
        {
            taunt1TextRenderer.setColor(defaultTextColor);
            taunt1NameTextRenderer.setColor(defaultTextColor);
            taunt1ImageRenderer.setImage(edgeImageNormal);
            taunt1WasClickable = false;
            taunt1ResetDone = true;
        }
    }
    
    public void onTaunt2Hover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(taunt2ID);
                return;
            }
            if (!taunt2WasClickable)
            {
                taunt2TextRenderer.setColor(hoverTextColor);
                taunt2NameTextRenderer.setColor(hoverTextColor);
                taunt2ImageRenderer.setImage(edgeImageHover);
                taunt2WasClickable = true;
                taunt2ResetDone = false;
                adaptActive(taunt2ID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!taunt2ResetDone)
        {
            taunt2TextRenderer.setColor(defaultTextColor);
            taunt2NameTextRenderer.setColor(defaultTextColor);
            taunt2ImageRenderer.setImage(edgeImageNormal);
            taunt2WasClickable = false;
            taunt2ResetDone = true;
        }
    }
    
    public void onTaunt3Hover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(taunt3ID);
                return;
            }
            if (!taunt3WasClickable)
            {
                taunt3TextRenderer.setColor(hoverTextColor);
                taunt3NameTextRenderer.setColor(hoverTextColor);
                taunt3ImageRenderer.setImage(edgeImageHover);
                taunt3WasClickable = true;
                taunt3ResetDone = false;
                adaptActive(taunt3ID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!taunt3ResetDone)
        {
            taunt3TextRenderer.setColor(defaultTextColor);
            taunt3NameTextRenderer.setColor(defaultTextColor);
            taunt3ImageRenderer.setImage(edgeImageNormal);
            taunt3WasClickable = false;
            taunt3ResetDone = true;
        }
    }
    
    public void onRestoreHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(restoreID);
                return;
            }
            if (!restoreWasClickable)
            {
                restoreTextRenderer.setColor(hoverTextColor);
                restoreImageRenderer.setImage(edgeImageHover);
                restoreWasClickable = true;
                restoreResetDone = false;
                adaptActive(restoreID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!restoreResetDone)
        {
            restoreTextRenderer.setColor(defaultTextColor);
            restoreImageRenderer.setImage(edgeImageNormal);
            restoreWasClickable = false;
            restoreResetDone = true;
        }
    }
    
    public void onSaveHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(saveID);
                return;
            }
            if (!saveWasClickable)
            {
                saveTextRenderer.setColor(hoverTextColor);
                saveImageRenderer.setImage(edgeImageHover);
                saveWasClickable = true;
                saveResetDone = false;
                adaptActive(saveID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!saveResetDone)
        {
            saveTextRenderer.setColor(defaultTextColor);
            saveImageRenderer.setImage(edgeImageNormal);
            saveWasClickable = false;
            saveResetDone = true;
        }
    }
    
    public void onLoadHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                keymapListener.reset();
                activateActive(loadID);
                return;
            }
            if (!loadWasClickable)
            {
                loadTextRenderer.setColor(hoverTextColor);
                loadImageRenderer.setImage(edgeImageHover);
                loadWasClickable = true;
                loadResetDone = false;
                adaptActive(loadID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!loadResetDone)
        {
            loadTextRenderer.setColor(defaultTextColor);
            loadImageRenderer.setImage(edgeImageNormal);
            loadWasClickable = false;
            loadResetDone = true;
        }
    }
    
    // </editor-fold>


    public void editRegistry(Joystick joy)
    {
        JoySettings joySet = JoystickRegistry.getJoySettings(joy.getName());
        joySet.setButtonName(0, "A");
        joySet.setButtonName(1, "B");
        joySet.setButtonName(2, "X");
        joySet.setButtonName(3, "Y");
    }
    
    public void reeditRegistry(Joystick joy)
    {
        JoySettings joySet = JoystickRegistry.getJoySettings(joy.getName());
        System.out.println(joySet.getButtonName(0));
        joySet.setButtonName(0, "Hello");
        joySet.setAxisName(6, true, "Right");
        joySet.setAxisName(6, false, "Left");
        System.out.println(joySet.getButtonName(0));
    }
    
    
    public static String TriggerFilter(Trigger trigger)
    {
        String triggerName = "";
        if (trigger instanceof KeyTrigger)
        {
            KeyTrigger keyTrig = (KeyTrigger) trigger;
            triggerName = keynames.getName(keyTrig.getKeyCode());
        }
        else if (trigger instanceof JoyAxisTrigger)
        {
            JoyAxisTrigger joyAxTrig = (JoyAxisTrigger) trigger;
            Joystick joystick = Main.joysticks[joyAxTrig.getJoyId()];
            String orientation = "";
            if (!joyAxTrig.isNegative())
            {
                triggerName = JoystickRegistry.getJoySettings(joystick.getName()).getAxisName(joyAxTrig.getAxisId(), true);
            }
            else
            {
                triggerName = JoystickRegistry.getJoySettings(joystick.getName()).getAxisName(joyAxTrig.getAxisId(), false);
            }
        }
        else if (trigger instanceof JoyButtonTrigger)
        {
            JoyButtonTrigger joyButTrig = (JoyButtonTrigger) trigger;
            Joystick joystick = Main.joysticks[joyButTrig.getJoyId()];
            triggerName = JoystickRegistry.getJoySettings(joystick.getName()).getButtonName(joyButTrig.getAxisId());
        }
        return triggerName;
    }
}
