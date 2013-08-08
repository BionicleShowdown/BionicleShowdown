
package menu;

import AudioNodes.SFXAudioNode;
import Characters.Tahu;
import Characters.Kopaka;
import Characters.PlayableCharacter;
import Characters.RandomCharacter;
import bs.StandardMatchState;
import bs.StartState;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.FastMath;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.FocusGainedEvent;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMouseEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseMovedEvent;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.imageio.ImageIO;
import jme3tools.converters.ImageToAwt;
import mygame.Main; 


/**
 *
 * @author JSC and Inferno
 */
public class MainMenu extends AbstractAppState implements ScreenController, KeyInputHandler, ActionListener
{
    private AssetManager assetManager;
    private SimpleApplication app;
    private Node rootNode;
    private Nifty nifty;
    private NiftyJmeDisplay niftyDisplay;
    private Screen screen;
    private InputManager inputManager;
    private AudioRenderer audioRenderer;
    private ViewPort guiViewPort;
    private AppStateManager stateManager;
    private StandardMatchState inGameState;
    private CharacterSelectMenu characterSelectMenu;
    
    StartState startState;
    static AppSettings settings = Main.getSettings(); // Defined outside of function to allow use in all methods
    
    /* Intiializes the screenHeight and screenWidth so they can be used later (initialized in onStartScreen() */
    static int screenHeight;
    static int screenWidth;
   
    /* Files used to initialize Button objects */
//    File fightImage = new File("assets/Interface/MainMenu/Fight.png");
//    File trainingImage = new File("assets/Interface/MainMenu/Training.png");
//    File extrasImage = new File("assets/Interface/MainMenu/Extras.png");
//    File optionsImage = new File("assets/Interface/MainMenu/Options.png");
    
    /* Uninitialized Button objects (they are initialized in onStartScreen() */
    Button fightButton;
    Button trainingButton;
    Button extrasButton;
    Button optionsButton;
    
//    Element fightImageElement;
//    Element trainingImageElement;
//    Element extrasImageElement;
//    Element optionsImageElement;
    
    Element fightPanel;
    Element trainingPanel;
    Element extrasPanel;
    Element optionsPanel;
    
    ImageRenderer fightImageRenderer;
    ImageRenderer trainingImageRenderer;
    ImageRenderer extrasImageRenderer;
    ImageRenderer optionsImageRenderer;
    
    NiftyImage fightImageNormal;
    NiftyImage trainingImageNormal;
    NiftyImage extrasImageNormal;
    NiftyImage optionsImageNormal;
    
    NiftyImage fightImageHover;
    NiftyImage trainingImageHover;
    NiftyImage extrasImageHover;
    NiftyImage optionsImageHover;
    
    SFXAudioNode fightButtonSound;
    boolean fightButtonWasClickable = false;
    boolean fightResetDone = false;
    SFXAudioNode trainingButtonSound;
    boolean trainingButtonWasClickable = false;
    boolean trainingResetDone = false;
    SFXAudioNode extrasButtonSound;
    boolean extrasButtonWasClickable = false;
    boolean extrasResetDone = false;
    SFXAudioNode optionsButtonSound;
    boolean optionsButtonWasClickable = false;
    boolean optionsResetDone = false;
    
    boolean keysUsed = false;
    
    byte nullID = -1;
    byte fightID = 0;
    byte trainingID = 1;
    byte extrasID = 2;
    byte optionsID = 3;
    
    ButtonLayout currentLayout = new ButtonLayout(nullID, fightID, fightID, fightID, fightID);
    ButtonLayout fightLayout = new ButtonLayout(fightID, fightID, trainingID, fightID, extrasID);
    ButtonLayout trainingLayout = new ButtonLayout(trainingID, fightID, trainingID, trainingID, optionsID);
    ButtonLayout extrasLayout = new ButtonLayout(extrasID, extrasID, optionsID, fightID, extrasID);
    ButtonLayout optionsLayout = new ButtonLayout(optionsID, extrasID, optionsID, trainingID, optionsID);
    
    
    /* Records whether the TeamType is Free For All or a Team Match. */
    String teamType = "Free For All";
    
    public MainMenu() 
    {
    
    }
    
    public void initiate(Application app) 
    {
        this.app = (SimpleApplication) app;
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.audioRenderer = this.app.getAudioRenderer();
        this.guiViewPort = this.app.getViewPort();
        this.stateManager = this.app.getStateManager();
        nifty = Main.getNifty();
        nifty.registerScreenController(this);
        nifty.addXml("Interface/GUIS/MainMenu.xml");
        nifty.gotoScreen("MainMenu");          //Just for the first one, got to the start screen
        
        nifty.setDebugOptionPanelColors(true); // Leave it on true for now, so you can see the costume buttons.
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) 
    {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.audioRenderer = this.app.getAudioRenderer();
        this.guiViewPort = this.app.getViewPort();
        this.stateManager = this.app.getStateManager();
        nifty = Main.getNifty();
        nifty.registerScreenController(this);
        nifty.addXml("Interface/GUIS/MainMenu.xml");
        nifty.gotoScreen("MainMenu");          //Just for the first one, got to the start screen
        
        nifty.setDebugOptionPanelColors(true); // Leave it on true for now, so you can see the costume buttons.
    }
    

    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void update(float tpf) 
    {
        /* Nothing Yet*/
    }

    @Override
    public void cleanup() 
    {
              
        
    }

    @Override
    public void setEnabled(boolean enabled) 
    {
        super.setEnabled(enabled);
    }
    
    public void onStartScreen() 
    {
//        inputManager.setCursorVisible(false);
//        inputManager.addRawInputListener(new CursorMoveListener(settings, nifty.getNiftyMouse()));
        // The next few lines were for a likely dropped Iconic Cursor (it made it impossible to use Nifty properly [as the cursor was invisible])
        Element cursorElement = screen.findElementByName("Cursor");
        cursorElement.hide();
        
//        CursorMoveListener cursor = new CursorMoveListener(cursorElement, settings);
//        
//        float startX = inputManager.getCursorPosition().x;
//        float startY = settings.getHeight() - inputManager.getCursorPosition().y;
//        
//        cursorElement.setConstraintX(new SizeValue((int) startX + "px"));
//        cursorElement.getParent().layoutElements();
//        cursorElement.setConstraintY(new SizeValue((int) startY + "px"));
//        cursorElement.getParent().layoutElements();
//        
//        inputManager.addRawInputListener(cursor);
        
        screenHeight = settings.getHeight(); // Gets the height of the window after the window is created, but before it is used
        screenWidth = settings.getWidth(); // Gets the width of the window after the window is created, but before it is used
        System.out.println("Screen height is: " + screenHeight + ", Screen width is: " + screenWidth);
        /* Initializes all Buttons */
//        fightButton = new Button(fightImage, 0.1, 0.2, 0.35, 0.25);
//        trainingButton = new Button(trainingImage, .55, .20, .35, .25);
//        extrasButton = new Button(extrasImage, .1, .55, .35, .25);
//        optionsButton = new Button(optionsImage, .55, .55, .35, .25);
        // Not sure which version would be better
        fightButton = new Button(app, "Interface/MainMenu/Fight.png", 0.1, 0.2, 0.35, 0.25, false);
        trainingButton = new Button(app, "Interface/MainMenu/Training.png", .55, .20, .35, .25, false);
        extrasButton = new Button(app, "Interface/MainMenu/Extras.png", .1, .55, .35, .25, false);
        optionsButton = new Button(app, "Interface/MainMenu/Options.png", .55, .55, .35, .25, false);
        
        fightButtonSound = new SFXAudioNode(assetManager, "Sounds/Announcements/CharacterSelected/Tahu.wav");
        trainingButtonSound = new SFXAudioNode(assetManager, "Sounds/Announcements/CharacterSelected/Kopaka.wav");
        extrasButtonSound = new SFXAudioNode(assetManager, "Sounds/Announcements/CharacterSelected/Kopaka.wav");
        optionsButtonSound = new SFXAudioNode(assetManager, "Sounds/Announcements/CharacterSelected/Tahu.wav");
        
        fightPanel = screen.findElementByName("FightPanel");
        trainingPanel = screen.findElementByName("TrainingPanel");
        extrasPanel = screen.findElementByName("ExtrasPanel");
        optionsPanel = screen.findElementByName("OptionsPanel");
        
//        fightImageElement = screen.findElementByName("FightImage");
//        trainingImageElement = screen.findElementByName("TrainingImage");
//        optionsImageElement = screen.findElementByName("OptionsImage");
//        extrasImageElement = screen.findElementByName("ExtrasImage");
        
        fightImageRenderer = screen.findElementByName("FightImage").getRenderer(ImageRenderer.class);
        trainingImageRenderer = screen.findElementByName("TrainingImage").getRenderer(ImageRenderer.class);
        extrasImageRenderer = screen.findElementByName("ExtrasImage").getRenderer(ImageRenderer.class);
        optionsImageRenderer = screen.findElementByName("OptionsImage").getRenderer(ImageRenderer.class);
        
        fightImageNormal = nifty.createImage("Interface/MainMenu/Fight.png", false);
        trainingImageNormal = nifty.createImage("Interface/MainMenu/Training.png", false);
        extrasImageNormal = nifty.createImage("Interface/MainMenu/Extras.png", false);
        optionsImageNormal = nifty.createImage("Interface/MainMenu/Options.png", false);

        fightImageHover = nifty.createImage("Interface/MainMenu/FightHover.png", false);
        trainingImageHover = nifty.createImage("Interface/MainMenu/TrainingHover.png", false);
        extrasImageHover = nifty.createImage("Interface/MainMenu/ExtrasHover.png", false);
        optionsImageHover = nifty.createImage("Interface/MainMenu/OptionsHover.png", false);
        
//        fightPanel.setFocusable(true);
//        fightPanel.setFocus();
        currentLayout = new ButtonLayout(fightID, fightID, fightID, fightID, fightID);
        keysUsed = false;
        
        initJoy();
    }

    public void onEndScreen() 
    {
        
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
            fightResetDone = false;
            trainingResetDone = false;
            extrasResetDone = false;
            optionsResetDone = false;
        }
        
        fightButtonControl(fightButton.isClickable(x, y), wasClick);
        trainingButtonControl(trainingButton.isClickable(x, y), wasClick);
        extrasButtonControl(extrasButton.isClickable(x, y), wasClick);
        optionsButtonControl(optionsButton.isClickable(x, y), wasClick);
        
    }
    
    public void fightButtonControl(boolean isAvailable, boolean wasClick)
    {
        System.out.println(isAvailable);
        if (isAvailable)
        {
            if (wasClick)
            {
                fightButtonClicked();
                return;
            }
            if (!fightButtonWasClickable)
            {
                fightImageRenderer.setImage(fightImageHover);
//                fightButtonSound = new SFXAudioNode(assetManager, "Sounds/Announcements/CharacterSelected/Tahu.wav");
                fightButtonSound.playInstance();
                fightButtonWasClickable = true;
                fightResetDone = false;
                adaptActive(fightID);
            }
//            adaptActive(fightID);
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!fightResetDone)
        {
            fightImageRenderer.setImage(fightImageNormal);
            fightButtonWasClickable = false;
            fightResetDone = true;
        }
    }
    
    public void trainingButtonControl(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                trainingButtonClicked();
                return;
            }
            if (!trainingButtonWasClickable)
            {
                trainingImageRenderer.setImage(trainingImageHover);
                trainingButtonSound.playInstance();
                trainingButtonWasClickable = true;
                trainingResetDone = false;
                adaptActive(trainingID);
            }
//            adaptActive(trainingID);
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!trainingResetDone)
        {
            trainingImageRenderer.setImage(trainingImageNormal);
            trainingButtonWasClickable = false;
            trainingResetDone = true;
        }
    }
    
    public void extrasButtonControl(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                extrasButtonClicked();
                return;
            }
            if (!extrasButtonWasClickable)
            {
                extrasImageRenderer.setImage(extrasImageHover);
                extrasButtonSound.playInstance();
                extrasButtonWasClickable = true;
                extrasResetDone = false;
                adaptActive(extrasID);
            } 
//            adaptActive(extrasID);
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!extrasResetDone)
        {
            extrasImageRenderer.setImage(extrasImageNormal);
            extrasButtonWasClickable = false;
            extrasResetDone = true;
        }
    }
    
    public void optionsButtonControl(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                optionsButtonClicked();
                return;
            }
            if (!optionsButtonWasClickable)
            {
                optionsImageRenderer.setImage(optionsImageHover);
                optionsButtonSound.playInstance();
                optionsButtonWasClickable = true;
                optionsResetDone = false;
                adaptActive(optionsID);
            }
//            adaptActive(optionsID);
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!optionsResetDone)
        {
            optionsImageRenderer.setImage(optionsImageNormal);
            optionsButtonWasClickable = false;
            optionsResetDone = true;
        }
    }
    
    public void fightButtonClicked()
    {
        System.out.println("Fighting");
        characterSelectMenu = new CharacterSelectMenu(this);
        characterSelectMenu.initiate(app);
    }
    
    public void trainingButtonClicked()
    {
        System.out.println("Training");
        Tahu.unlockCostume("Special");
        Team.addTeam("Green");
    }
    
    public void extrasButtonClicked()
    {
        System.out.println("Extra-ing");
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTime());
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int second = Calendar.getInstance().get(Calendar.SECOND);
        System.out.println(Calendar.getInstance().getTimeZone());
        System.out.println(month + "/" + day + "/" + year + " at " + hour + ":" + minute + ":" + second);
    }
    
    public void optionsButtonClicked()
    {
        System.out.println("Option-ing");
        OptionsMenu optionsMenu = new OptionsMenu(this);
        optionsMenu.initiate(app);
    }
    
    
//    @NiftyEventSubscriber(id="FightPanel")
//    public void onFightFocus(String id, FocusGainedEvent event)
//    {
//        System.out.println("Focus");
//        fightImageRenderer.setImage(fightImageHover);
//    }
//    
//    @NiftyEventSubscriber(id="TrainingPanel")
//    public void onTrainingFocus(String id, FocusGainedEvent event)
//    {
//        System.out.println("Focus!");
//        trainingImageRenderer.setImage(trainingImageHover);
//    }
    
//    public boolean inputEvent(NiftyInputEvent event)
//    {
//        if (event == NiftyInputEvent.Activate)
//        {
//            System.out.println("Activate");
//            return true;
//        }
//        return false;
//    }
    
    
    public void adaptActive(byte newButton)
    {
        if (!keysUsed && (currentLayout.Identity() == newButton))
        {
            switch (newButton)
            {
                case 0:
                {
                    System.out.println("Fight");
                    currentLayout = fightLayout;
                    fightButtonSound.playInstance();
                    fightImageRenderer.setImage(fightImageHover);
                    break;
                }
                case 1:
                {
                    System.out.println("Training");
                    currentLayout = trainingLayout;
                    trainingButtonSound.playInstance();
                    trainingImageRenderer.setImage(trainingImageHover);
                    break;
                }
                case 2:
                {
                    System.out.println("Extras");
                    currentLayout = extrasLayout;
                    extrasButtonSound.playInstance();
                    extrasImageRenderer.setImage(extrasImageHover);
                    break;
                }
                case 3:
                {
                    System.out.println("Options");
                    currentLayout = optionsLayout;
                    optionsButtonSound.playInstance();
                    optionsImageRenderer.setImage(optionsImageHover);
                    break;
                }
                default:
                {
                    System.out.println("Fight");
                    currentLayout = fightLayout;
                    fightButtonSound.playInstance();
                    fightImageRenderer.setImage(fightImageHover);
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
            case 0:
            {
                System.out.println("Fight");
                currentLayout = fightLayout;
                fightButtonWasClickable = false;
                fightImageRenderer.setImage(fightImageNormal);
                break;
            }
            case 1:
            {
                System.out.println("Training");
                currentLayout = trainingLayout;
                trainingButtonWasClickable = false;
                trainingImageRenderer.setImage(trainingImageNormal);
                break;
            }
            case 2:
            {
                System.out.println("Extras");
                currentLayout = extrasLayout;
                extrasButtonWasClickable = false;
                extrasImageRenderer.setImage(extrasImageNormal);
                break;
            }
            case 3:
            {
                System.out.println("Options");
                currentLayout = optionsLayout;
                optionsButtonWasClickable = false;
                optionsImageRenderer.setImage(optionsImageNormal);
                break;
            }
            default:
            {
                System.out.println("Fight");
                currentLayout = fightLayout;
                fightButtonWasClickable = false;
                fightImageRenderer.setImage(fightImageNormal);
            }
        }
        switch (newButton)
        {
            case 0:
            {
                System.out.println("Fight");
                currentLayout = fightLayout;
                fightButtonSound.playInstance();
                fightImageRenderer.setImage(fightImageHover);
                break;
            }
            case 1:
            {
                System.out.println("Training");
                currentLayout = trainingLayout;
                trainingButtonSound.playInstance();
                trainingImageRenderer.setImage(trainingImageHover);
                break;
            }
            case 2:
            {
                System.out.println("Extras");
                currentLayout = extrasLayout;
                extrasButtonSound.playInstance();
                extrasImageRenderer.setImage(extrasImageHover);
                break;
            }
            case 3:
            {
                System.out.println("Options");
                currentLayout = optionsLayout;
                optionsButtonSound.playInstance();
                optionsImageRenderer.setImage(optionsImageHover);
                break;
            }
            default:
            {
                System.out.println("Fight");
                currentLayout = fightLayout;
                fightButtonSound.playInstance();
                fightImageRenderer.setImage(fightImageHover);
            }
        }
    }
    
    public void activateActive()
    {
        switch (currentLayout.Identity())
        {
            case 0:
            {
                fightButtonClicked();
                break;
            }
            case 1:
            {
                trainingButtonClicked();
                break;
            }
            case 2:
            {
                extrasButtonClicked();
                break;
            }
            case 3:
            {
                optionsButtonClicked();
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
    
    
    
//    @NiftyEventSubscriber(id="FightPanel")
//    public void fightButtonHoverCheck(String id, NiftyMouseMovedEvent event)
//    {
//        NiftyImage image;
//        Element element = event.getElement().findElementByName("FightImage");
//        if (fightButton.isClickable(event.getMouseX(), event.getMouseY()))
//        {
////            event.getElement().startEffect(EffectEventId.onHover);
//            if (fightButtonWasClickable == false)
//            {
//                fightButtonSound = new SFXAudioNode(assetManager, "Sounds/Announcements/CharacterSelected/Tahu.wav");
//                fightButtonSound.play();
//                image = nifty.createImage("Interface/MainMenu/FightHover.png", false);
//                element.getRenderer(ImageRenderer.class).setImage(image);
//            }
//            fightButtonWasClickable = true;
//            return;
//        }
//        
//        System.out.println(fightButton.isClickable(event.getMouseX(), event.getMouseY()));
////        event.getElement().stopEffect(EffectEventId.onHover);
//        image = nifty.createImage("Interface/MainMenu/Fight.png", false);
//        element.getRenderer(ImageRenderer.class).setImage(image);
//        fightButtonWasClickable = false;
//    }
//    
//    @NiftyEventSubscriber(id="TrainingPanel")
//    public void trainingButtonHoverCheck(String id, NiftyMouseMovedEvent event)
//    {
//        NiftyImage image;
//        Element element = event.getElement().findElementByName("TrainingImage");
//        if (trainingButton.isClickable(event.getMouseX(), event.getMouseY()))
//        {
//            if (trainingButtonWasClickable == false)
//            {
//                trainingButtonSound = new SFXAudioNode(assetManager, "Sounds/Announcements/CharacterSelected/Kopaka.wav");
//                trainingButtonSound.play();
//                image = nifty.createImage("Interface/MainMenu/TrainingHover.png", false);
//                element.getRenderer(ImageRenderer.class).setImage(image);
//            }
//            trainingButtonWasClickable = true;
//            return;
//        }
////        event.getElement().stopEffect(EffectEventId.onHover);
//        image = nifty.createImage("Interface/MainMenu/Training.png", false);
//        element.getRenderer(ImageRenderer.class).setImage(image);
//        trainingButtonWasClickable = false;
//    }
//    
//    @NiftyEventSubscriber(id="OptionsPanel")
//    public void optionsButtonHoverCheck(String id, NiftyMouseMovedEvent event)
//    {
//        NiftyImage image;
//        Element element = event.getElement().findElementByName("OptionsImage");
//        if (optionsButton.isClickable(event.getMouseX(), event.getMouseY()))
//        {
//            if (optionsButtonWasClickable == false)
//            {
//                optionsButtonSound = new SFXAudioNode(assetManager, "Sounds/Announcements/CharacterSelected/Tahu.wav");
//                optionsButtonSound.play();
//                image = nifty.createImage("Interface/MainMenu/OptionsHover.png", false);
//                element.getRenderer(ImageRenderer.class).setImage(image);
//                
//            }
//            optionsButtonWasClickable = true;
//            return;
//        }
////        event.getElement().stopEffect(EffectEventId.onHover);
//        image = nifty.createImage("Interface/MainMenu/Options.png", false);
//        element.getRenderer(ImageRenderer.class).setImage(image);
//        optionsButtonWasClickable = false;
//    }
//    
//    @NiftyEventSubscriber(id="ExtrasPanel")
//    public void extrasButtonHoverCheck(String id, NiftyMouseMovedEvent event)
//    {
//        NiftyImage image;
//        Element element = event.getElement().findElementByName("ExtrasImage");
//        if (extrasButton.isClickable(event.getMouseX(), event.getMouseY()))
//        {
//            if (extrasButtonWasClickable == false)
//            {
//                extrasButtonSound = new SFXAudioNode(assetManager, "Sounds/Announcements/CharacterSelected/Kopaka.wav");
//                extrasButtonSound.play();
//                image = nifty.createImage("Interface/MainMenu/ExtrasHover.png", false);
//                element.getRenderer(ImageRenderer.class).setImage(image);
//            }
//            extrasButtonWasClickable = true;
//            return;
//        }
////        event.getElement().stopEffect(EffectEventId.onHover);
//        image = nifty.createImage("Interface/MainMenu/Extras.png", false);
//        element.getRenderer(ImageRenderer.class).setImage(image);
//        extrasButtonWasClickable = false;
//    }
//    
//    
//    public void fightScreen(int x, int y)
//    {
//        System.out.println("" + x + ", " + y);
//         if (fightButton.isClickable(x, y))
//         {
//             System.out.println("This spot is clickable.");
//             System.out.println("Fighting");
////             clearPlayersAndTeams();
////             nifty.gotoScreen("CharSelect");
////             nifty.getScreen("CharSelect").getScreenController();
////             currentScreen = fightScreen;
//             characterSelectMenu = new CharacterSelectMenu(this);
//             characterSelectMenu.initiate(app);
//         }
//         else
//         {
//             System.out.println("Not clickable.");
//         }
//    }
//    
//    public void trainingScreen(int x, int y) 
//    {
//        System.out.println("" + x + ", " + y);
//        if (trainingButton.isClickable(x, y))
//        {
//            System.out.println("Clickable.");
//            System.out.println("Training.");
//            Tahu.unlockCostume("Special");
//            Team.addTeam("Green");
//        }
//        else
//        {
//            System.out.println("Not Clickable.");
//        }
//    }
//    
//    public void extrasScreen(int x, int y)
//    {
//        System.out.println("" + x + ", " + y);
//        if (extrasButton.isClickable(x, y))
//        {
//            System.out.println("Clickable.");
//            System.out.println("Extra-ing.");
//            
//            Calendar calendar = Calendar.getInstance();
//            System.out.println(calendar.getTime());
//            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
//            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
//            int year = Calendar.getInstance().get(Calendar.YEAR);
//            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//            int minute = Calendar.getInstance().get(Calendar.MINUTE);
//            int second = Calendar.getInstance().get(Calendar.SECOND);
//            System.out.println(Calendar.getInstance().getTimeZone());
//            System.out.println(month + "/" + day + "/" + year + " at " + hour + ":" + minute + ":" + second);
//        }
//        else
//        {
//            System.out.println("Not Clickable.");
//        }
//    }
//        
//    public void optionsScreen(int x, int y)
//    {
//        System.out.println("" + x + ", " + y);
//        optionsButton.printAll();
//        if (optionsButton.isClickable(x, y))
//        {
//            System.out.println("Clickable.");
//            System.out.println("Option-ing.");
//            OptionsMenu optionsMenu = new OptionsMenu(this);
//            optionsMenu.initiate(app);
//        }
//        else
//        {
//            System.out.println("Not Clickable.");
//        }
//    }
    
//    private class CursorMoveListener implements RawInputListener
//    {
//        private Element cursor;
//        private AppSettings settings;
//        private float x=0, y=0;
//        
//        public CursorMoveListener()
//        {
//            
//        }
//        
//        public CursorMoveListener(Element cursor, AppSettings settings)
//        {
//            this.cursor = cursor;
//            this.settings = settings;
//        }
//
//        public void beginInput() 
//        {
//            
//        }
//
//        public void endInput() 
//        {
//            
//        }
//
//        public void onJoyAxisEvent(JoyAxisEvent evt) 
//        {
//            float deadzone = inputManager.getAxisDeadZone();
//            System.out.println("Axis Deadzones: " + inputManager.getAxisDeadZone());
//            System.out.println("Moved Joystick");
//            if (evt.getAxis().getName().equalsIgnoreCase("x"))
//            {
//                System.out.println("X Value: " + evt.getValue());
//                if ((evt.getValue() >= deadzone) || (evt.getValue() <= -deadzone))
//                {
//                    x += evt.getValue() * 10;
//                }
//            }
//            if (evt.getAxis().getName().equalsIgnoreCase("y"))
//            {
//                System.out.println("Y Value: " + evt.getValue());
//                if ((evt.getValue() >= deadzone) || (evt.getValue() <= -deadzone))
//                {
//                    y += evt.getValue() * 10;
//                }
//            }
//            
//            x = FastMath.clamp(x, 0, settings.getWidth() - 12);
//            y = FastMath.clamp(y, 0, settings.getHeight() - 19);
//            
//            System.out.println(x + ", " + y);
//            
//            cursor.setConstraintX(new SizeValue((int) x + "px"));
//            cursor.getParent().layoutElements();
//            cursor.setConstraintY(new SizeValue((int) y + "px"));
//            cursor.getParent().layoutElements();
//            
//            fightButtonControl(fightButton.isClickable((int) x, (int) y), false);
//        }
//
//        public void onJoyButtonEvent(JoyButtonEvent evt) 
//        {
//            
//        }
//
//        public void onMouseMotionEvent(MouseMotionEvent evt) 
//        {
//            System.out.println("Moved Mouse");
//            x = evt.getX();
//            y = settings.getHeight() - evt.getY();
//            System.out.println(x + ", " + y);
//        
//            x = FastMath.clamp(x, 0, settings.getWidth() - 12);
//            y = FastMath.clamp(y, 0, settings.getHeight() - 19);
//        
//            cursor.setConstraintX(new SizeValue((int) x + "px"));
//            cursor.getParent().layoutElements();
//            cursor.setConstraintY(new SizeValue((int) y + "px"));
//            cursor.getParent().layoutElements();
//            
//            fightButtonControl(fightButton.isClickable((int) x, (int) y), false);
////            trainingButtonControl(trainingButton.isClickable(x, y), wasClick);
////            extrasButtonControl(extrasButton.isClickable(x, y), wasClick);
////            optionsButtonControl(optionsButton.isClickable(x, y), wasClick);
//        }
//
//        public void onMouseButtonEvent(MouseButtonEvent evt) 
//        {
//            x = evt.getX();
//            y = settings.getHeight() - evt.getY();
//            System.out.println("Clicked Mouse");
//            System.out.println(fightButton.isClickable((int) x, (int) y));
//            fightButtonControl(fightButton.isClickable((int) x, (int) y), true);
//        }
//
//        public void onKeyEvent(KeyInputEvent evt) 
//        {
//            if (evt.getKeyCode() == KeyInput.KEY_A)
//            {
//                x -= 10;
//            }
//            if (evt.getKeyCode() == KeyInput.KEY_D)
//            {
//                x += 10;
//            }
//            if (evt.getKeyCode() == KeyInput.KEY_W)
//            {
//                y -= 10;
//            }
//            if (evt.getKeyCode() == KeyInput.KEY_S)
//            {
//                y += 10;
//            }
//            
//            x = FastMath.clamp(x, 0, settings.getWidth() - 12);
//            y = FastMath.clamp(y, 0, settings.getHeight() - 19);
//        
//            cursor.setConstraintX(new SizeValue((int) x + "px"));
//            cursor.getParent().layoutElements();
//            cursor.setConstraintY(new SizeValue((int) y + "px"));
//            cursor.getParent().layoutElements();
//            
//            fightButtonControl(fightButton.isClickable((int) x, (int) y), false);
//        }
//
//        public void onTouchEvent(TouchEvent evt) 
//        {
//            
//        }
//    };

    private void initJoy() 
    {
        if (Main.joysticks.length != 0)
        {
            Main.joysticks[0].getXAxis().assignAxis("Right", "Left");
            Main.joysticks[0].getYAxis().assignAxis("Down", "Up");
            Main.joysticks[0].getButton("0").assignButton("Accept");
            inputManager.addListener(this, "Right", "Left", "Up", "Down", "Accept");
        }
    }

    public void onAction(String name, boolean isPressed, float tpf) 
    {
        if (name.equals("Accept"))
        {
            System.out.println("Accept");
            Main.getNiftyDisplay().simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_RETURN, '0', true, false));
        }
        if (name.equals("Right"))
        {
            System.out.println("Right");
            Main.getNiftyDisplay().simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_D, 'D', true, false));
        }
        if (name.equals("Left"))
        {
            System.out.println("Left");
            Main.getNiftyDisplay().simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_A, 'A', true, false));
        }
        if (name.equals("Up"))
        {
            System.out.println("Up");
            Main.getNiftyDisplay().simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_W, 'W', true, false));
        }
        if (name.equals("Down"))
        {
            System.out.println("Down");
            Main.getNiftyDisplay().simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_S, 'S', true, false));
        }
    }

    
          
}
