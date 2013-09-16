/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

import AdaptedControls.HeadlessSlider;
import AdaptedControls.HeadlessSliderChangedEvent;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.FocusGainedEvent;
import de.lessvoid.nifty.controls.NiftyControl;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMouseEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseMovedEvent;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import mygame.CompoundInputManager;
import mygame.Main;

/**
 *
 * @author Inferno
 */
public class OptionsMenu implements ScreenController, KeyInputHandler, ActionListener, AnalogListener
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
    private MainMenu mainMenu;
    
    private float preMuteVolume;
    private static float defaultMusicVolume = 1.0f;
    public static float currentMusicVolume = 1.0f;
    private static float defaultSFXVolume = 1.0f;
    public static float currentSFXVolume = defaultSFXVolume;
    private static float defaultVoiceVolume = 1.0f;
    public static float currentVoiceVolume = defaultVoiceVolume;
    
    private NiftyImage backImageNormal;
    private NiftyImage controlsImageNormal;
    
    private NiftyImage backImageHover;
    private NiftyImage controlsImageHover;
    
    private HeadlessSlider musicSlider;
    private HeadlessSlider sfxSlider;
    private HeadlessSlider voiceSlider;
    
    private Element backPanelElement;
    private Element controlsPanelElement;
    
    private ImageRenderer backImageRenderer;
    private TextRenderer musicTextRenderer;
    private TextRenderer sfxTextRenderer;
    private TextRenderer voiceTextRenderer;
    private ImageRenderer controlsImageRenderer;
    
    private Color defaultTextColor = new Color("#FFFFFF");
    private Color hoverTextColor = new Color("#FFE500");
    
    private byte nullID = -1;
    private byte backID = 0;
    private byte musicID = 1;
    private byte musicDownID = 2;
    private byte musicUpID = 3;
    private byte sfxID = 4;
    private byte sfxDownID = 5;
    private byte sfxUpID = 6;
    private byte voiceID = 7;
    private byte voiceDownID = 8;
    private byte voiceUpID = 9;
    private byte controlsID = 10;
    
    private ButtonLayout currentLayout = new ButtonLayout(nullID, backID, backID, backID, backID);
    private ButtonLayout backLayout = new ButtonLayout(backID, backID, backID, backID, musicID);
    private ButtonLayout musicLayout = new ButtonLayout(musicID, musicDownID, musicUpID, backID, sfxID);
    private ButtonLayout sfxLayout = new ButtonLayout(sfxID, sfxDownID, sfxUpID, musicID, voiceID);
    private ButtonLayout voiceLayout = new ButtonLayout(voiceID, voiceDownID, voiceUpID, sfxID, controlsID);
    private ButtonLayout controlsLayout = new ButtonLayout(controlsID, controlsID, controlsID, voiceID, controlsID);
    
    private float slideSlow = 0;
    
    private boolean backResetDone = false;
    private boolean backButtonWasClickable = false;
    private boolean controlsResetDone = false;
    private boolean controlsButtonWasClickable = false;
    
    private boolean musicResetDone = false;
    private boolean musicSliderWasHovered = false;
    private boolean sfxResetDone = false;
    private boolean sfxSliderWasHovered = false;
    private boolean voiceResetDone = false;
    private boolean voiceSliderWasHovered = false;
    
    private boolean keysUsed = false;
    
    public OptionsMenu()
    {
        
    }
    
    public OptionsMenu(MainMenu mainMenu)
    {
        this.mainMenu = mainMenu;
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
        nifty.addXml("Interface/GUIS/OptionsMenu.xml");
        nifty.gotoScreen("OptionsMenu");          //Just for the first one, go to the start screen
        
        nifty.setDebugOptionPanelColors(false); // Leave it on true for now
    }
    
    @Override
    public void onStartScreen()
    {
        backPanelElement = screen.findElementByName("BackButtonPanel");
        controlsPanelElement = screen.findElementByName("ControlsPanel");
        
        backImageNormal = nifty.createImage("Interface/General/BackButton.png", false);
        controlsImageNormal = nifty.createImage("Interface/Options/Controls.png", false);
        
        backImageHover = nifty.createImage("Interface/General/BackButtonHover.png", false);
        controlsImageHover = nifty.createImage("Interface/Options/ControlsHover.png", false);
        
        backImageRenderer = backPanelElement.findElementByName("BackButtonImage").getRenderer(ImageRenderer.class);
        controlsImageRenderer = controlsPanelElement.findElementByName("ControlsImage").getRenderer(ImageRenderer.class);
        
        musicTextRenderer = screen.findElementByName("CurrentMusicVolume").getRenderer(TextRenderer.class);
        sfxTextRenderer = screen.findElementByName("CurrentSFXVolume").getRenderer(TextRenderer.class);
        voiceTextRenderer = screen.findElementByName("CurrentVoiceVolume").getRenderer(TextRenderer.class);
        
        musicTextRenderer.setText(getMusicVolume());
        sfxTextRenderer.setText(getSFXVolume());
        voiceTextRenderer.setText(getVoiceVolume());
        
        musicSlider = screen.findNiftyControl("musicVolumeSlider", HeadlessSlider.class);
        sfxSlider = screen.findNiftyControl("sfxVolumeSlider", HeadlessSlider.class);
        voiceSlider = screen.findNiftyControl("voiceVolumeSlider", HeadlessSlider.class);
        
        musicSlider.setValue(Main.getMusic().getVolume());
        sfxSlider.setValue(currentSFXVolume);
        voiceSlider.setValue(currentVoiceVolume);
        
        currentLayout = new ButtonLayout(nullID, backID, backID, backID, backID);
        backPanelElement.setFocus();
        
        initJoy();
//        initKeys();
    }
    
    @Override
    public void onEndScreen()
    {
            compoundManager.deleteMapping("Accept");
            compoundManager.deleteMapping("Right");
            compoundManager.deleteMapping("Left");
            compoundManager.deleteMapping("Up");
            compoundManager.deleteMapping("Down");
    }

    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
        this.screen = screen;
    }
    
    /**
     * Toggles Mute for Music Volume
     */
    public void muteMusic()
    {
        float volume = Main.getMusic().getVolume();
        if (volume == 0)
        {
            Main.getMusic().setVolume(preMuteVolume);
            Main.setMusicVolume(preMuteVolume);
            currentMusicVolume = preMuteVolume;
        }
        else
        {
            preMuteVolume = Main.getMusic().getVolume();
            Main.getMusic().setVolume(0);
            Main.setMusicVolume(0);
            currentMusicVolume = 0;
        }
    }
    
//    public void switchMusic()
//    {
//        String currentMusicSelection = Main.getMusicSelection();
//        if (currentMusicSelection.equals("Sounds/Music/Fire and Ice.wav"))
//        {
//            Main.changeMusic("Sounds/Music/Super Smash Bionicle Main Theme 2.wav");
//        }
//        else
//        {
//            Main.changeMusic("Sounds/Music/Fire and Ice.wav");
//        }
//    }
    
    // Changes the volume value and updates the text on the volume label when the Slider changes
    @NiftyEventSubscriber(id="musicVolumeSlider")
    public void getMusicVolume(String id, HeadlessSliderChangedEvent event)
    {
        float floatVolume = Main.getMusic().getVolume();
        int volume = Math.round(floatVolume * 100);
        if (volume < 0)
        {
            volume = 0;
        }
        if (volume > 100)
        {
            volume = 100;
        }
        musicTextRenderer.setText("Music Volume: " + volume + "%");
    }
    
    @NiftyEventSubscriber(id="sfxVolumeSlider")
    public void getSFXVolume(String id, HeadlessSliderChangedEvent event)
    {
        float floatVolume = currentSFXVolume;
        int volume = Math.round(floatVolume * 100);
        if (volume < 0)
        {
            volume = 0;
        }
        if (volume > 100)
        {
            volume = 100;
        }
        sfxTextRenderer.setText("SFX Volume: " + volume + "%");
    }
    
    @NiftyEventSubscriber(id="voiceVolumeSlider")
    public void getVoiceVolume(String id, HeadlessSliderChangedEvent event)
    {
        float floatVolume = currentVoiceVolume;
        int volume = Math.round(floatVolume * 100);
        if (volume < 0)
        {
            volume = 0;
        }
        if (volume > 100)
        {
            volume = 100;
        }
        voiceTextRenderer.setText("Voice Volume: " + volume + "%");
    }
    
    // Used by OptionsMenu.xml to get initial volume.
    public String getMusicVolume()
    {
        int volume = Math.round(Main.getMusic().getVolume() * 100);
        return "Music Volume: " + volume + "%";
    }
    
    // Used by OptionsMenu.xml to get initial volume.
    public String getSFXVolume()
    {
        int volume = Math.round(currentSFXVolume * 100);
        return "SFX Volume: " + volume + "%";
    }
    
    // Used by OptionsMenu.xml to get initial volume.
    public String getVoiceVolume()
    {
        int volume = Math.round(currentVoiceVolume * 100);
        return "Voice Volume: " + volume + "%";
    }
    
    // Can change music volume.
    @NiftyEventSubscriber(id="musicVolumeSlider")
    public void changeMusicVolume(String id, HeadlessSliderChangedEvent event)
    {
        float volume = event.getValue();
        if (volume < 0)
        {
            volume = 0;
        }
        if (volume > 1)
        {
            volume = 1;
        }
        event.getSlider().setValue(volume);
        System.out.println("" + event.getValue());
        currentMusicVolume = volume;
        Main.setMusicVolume(volume);
        Main.getMusic().setVolume(volume);
    }
    
    @NiftyEventSubscriber(id="sfxVolumeSlider")
    public void changeSFXVolume(String id, HeadlessSliderChangedEvent event)
    {
        float volume = event.getValue();
        if (volume < 0)
        {
            volume = 0;
        }
        if (volume > 1)
        {
            volume = 1;
        }
        event.getSlider().setValue(volume);
        System.out.println("" + event.getValue());
        currentSFXVolume = volume;
    }
    
    @NiftyEventSubscriber(id="voiceVolumeSlider")
    public void changeVoiceVolume(String id, HeadlessSliderChangedEvent event)
    {
        float volume = event.getValue();
        if (volume < 0)
        {
            volume = 0;
        }
        if (volume > 1)
        {
            volume = 1;
        }
        event.getSlider().setValue(volume);
        System.out.println("" + event.getValue());
        currentVoiceVolume = volume;
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
            backResetDone = false;
            musicResetDone = false;
            sfxResetDone = false;
            voiceResetDone = false;
            controlsResetDone = false;
            backPanelElement.setFocus();
        }
        
        onBackHover(backPanelElement.isMouseInsideElement(x, y), wasClick);
        onMusicHover(musicSlider.getElement().isMouseInsideElement(x, y), wasClick);
        onSFXHover(sfxSlider.getElement().isMouseInsideElement(x, y), wasClick);
        onVoiceHover(voiceSlider.getElement().isMouseInsideElement(x, y), wasClick);
        onControlsHover(controlsPanelElement.isMouseInsideElement(x, y), wasClick);
        
    }
    
    public void onBackHover(boolean isAvailable, boolean wasClick)
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
                backImageRenderer.setImage(backImageHover);
                backButtonWasClickable = true;
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
            backImageRenderer.setImage(backImageNormal);
            backButtonWasClickable = false;
            backResetDone = true;
        }
    }
    
    public void onMusicHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                musicSlider.setFocus();
                return;
            }
            if (!musicSliderWasHovered)
            {
                musicTextRenderer.setColor(hoverTextColor);
                musicSliderWasHovered = true;
                musicResetDone = false;
                adaptActive(musicID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!musicResetDone)
        {
            musicTextRenderer.setColor(defaultTextColor);
            if (screen.getFocusHandler().getKeyboardFocusElement() == musicSlider.getElement())
            {
                backPanelElement.setFocus();
            }
            musicSliderWasHovered = false;
            musicResetDone = true;
        }
    }
    
    public void onSFXHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                sfxSlider.setFocus();
                return;
            }
            if (!sfxSliderWasHovered)
            {
                sfxTextRenderer.setColor(hoverTextColor);
                sfxSliderWasHovered = true;
                sfxResetDone = false;
                adaptActive(sfxID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!sfxResetDone)
        {
            sfxTextRenderer.setColor(defaultTextColor);
            if (screen.getFocusHandler().getKeyboardFocusElement() == sfxSlider.getElement())
            {
                backPanelElement.setFocus();
            }
            sfxSliderWasHovered = false;
            sfxResetDone = true;
        }
    }
    
    public void onVoiceHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                voiceSlider.setFocus();
                return;
            }
            if (!voiceSliderWasHovered)
            {
                voiceTextRenderer.setColor(hoverTextColor);
                voiceSliderWasHovered = true;
                voiceResetDone = false;
                adaptActive(voiceID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!voiceResetDone)
        {
            voiceTextRenderer.setColor(defaultTextColor);
            if (screen.getFocusHandler().getKeyboardFocusElement() == voiceSlider.getElement())
            {
                backPanelElement.setFocus();
            }
            voiceSliderWasHovered = false;
            voiceResetDone = true;
        }
    }
    
    public void onControlsHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                controlsScreen();
                return;
            }
            if (!controlsButtonWasClickable)
            {
                controlsImageRenderer.setImage(controlsImageHover);
                controlsButtonWasClickable = true;
                controlsResetDone = false;
                adaptActive(controlsID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!controlsResetDone)
        {
            controlsImageRenderer.setImage(controlsImageNormal);
            controlsButtonWasClickable = false;
            controlsResetDone = true;
        }
    }
    
    @NiftyEventSubscriber(id="musicVolumeSlider")
    public void onMusicFocus(String id, FocusGainedEvent event)
    {
        currentLayout = musicLayout;
    }
    
    @NiftyEventSubscriber(id="sfxVolumeSlider")
    public void onSFXFocus(String id, FocusGainedEvent event)
    {
        currentLayout = sfxLayout;
    }
    
    @NiftyEventSubscriber(id="voiceVolumeSlider")
    public void onVoiceFocus(String id, FocusGainedEvent event)
    {
        currentLayout = voiceLayout;
    }
    
    public void controlsScreen()
    {
        System.out.println("Controls Screen!");
        ControlsHub controlsHub = new ControlsHub(this);
        controlsHub.initiate(app);
    }
    
    public void goBack()
    {
        mainMenu.initiate(app);
    }
    
    
    /*
     *  0
     * 
     *  2      1      3
     *  5      4      6
     *  8      7      9
     * 
     *         10
     * 
     */
    
    
    public void adaptActive(byte newButton)
    {
        if (!keysUsed && (currentLayout.Identity() == newButton))
        {
            switch (newButton)
            {
                case 0:
                {
                    System.out.println("Back");
                    currentLayout = backLayout;
                    backImageRenderer.setImage(backImageHover);
                    backButtonWasClickable = true;
                    break;
                }
                case 1:
                {
                    musicTextRenderer.setColor(hoverTextColor);
                    currentLayout = musicLayout;
                    musicSliderWasHovered = true;
                    musicSlider.setFocus();
                    break;
                }
                case 4:
                {
                    sfxTextRenderer.setColor(hoverTextColor);
                    currentLayout = sfxLayout;
                    sfxSliderWasHovered = true;
                    sfxSlider.setFocus();
                    break;
                }
                case 7:
                {
                    voiceTextRenderer.setColor(hoverTextColor);
                    currentLayout = voiceLayout;
                    voiceSliderWasHovered = true;
                    voiceSlider.setFocus();
                    break;
                }
                case 10:
                {
                    currentLayout = controlsLayout;
                    controlsPanelElement.setFocus();
                    controlsImageRenderer.setImage(controlsImageHover);
                    controlsButtonWasClickable = true;
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
            case 0:
            {
                currentLayout = backLayout;
                backImageRenderer.setImage(backImageNormal);
                backButtonWasClickable = false;
                break;
            }
            case 1:
            {
                if (newButton == 2 || newButton == 3)
                {
                    break;
                }
                musicTextRenderer.setColor(defaultTextColor);
                currentLayout = musicLayout;
                musicSliderWasHovered = false;
                break;
            }
            case 4:
            {
                if (newButton == 5 || newButton == 6)
                {
                    break;
                }
                sfxTextRenderer.setColor(defaultTextColor);
                currentLayout = sfxLayout;
                sfxSliderWasHovered = false;
                break;
            }
            case 7:
            {
                if (newButton == 8 || newButton == 9)
                {
                    break;
                }
                voiceTextRenderer.setColor(defaultTextColor);
                currentLayout = voiceLayout;
                voiceSliderWasHovered = false;
                break;
            }
            case 10:
            {
                currentLayout = controlsLayout;
                controlsImageRenderer.setImage(controlsImageNormal);
                controlsButtonWasClickable = false;
                break;
            }
            default:
            {
                
            }
        }
        switch (newButton)
        {
            case 0:
            {
                currentLayout = backLayout;
                backPanelElement.setFocus();
                backImageRenderer.setImage(backImageHover);
                backButtonWasClickable = true;
                break;
            }
            case 1:
            {
                musicTextRenderer.setColor(hoverTextColor);
                currentLayout = musicLayout;
                musicSlider.setFocus();
                musicSliderWasHovered = true;
                break;
            }
            case 2:
            {
                System.out.println("DOWNCLICK");
                musicSlider.upClick();
                musicSlider.setFocus();
                break;
            }
            case 3:
            {
                musicSlider.downClick();
                musicSlider.setFocus();
                break;
            }
            case 4:
            {
                sfxTextRenderer.setColor(hoverTextColor);
                currentLayout = sfxLayout;
                sfxSlider.setFocus();
                sfxSliderWasHovered = true;
                break;
            }
            case 5:
            {
                sfxSlider.upClick();
                sfxSlider.setFocus();
                break;
            }
            case 6:
            {
                sfxSlider.downClick();
                sfxSlider.setFocus();
                break;
            }
            case 7:
            {
                voiceTextRenderer.setColor(hoverTextColor);
                currentLayout = voiceLayout;
                voiceSlider.setFocus();
                voiceSliderWasHovered = true;
                break;
            }
            case 8:
            {
                voiceSlider.upClick();
                voiceSlider.setFocus();
                break;
            }
            case 9:
            {
                voiceSlider.downClick();
                voiceSlider.setFocus();
                break;
            }
            case 10:
            {
                currentLayout = controlsLayout;
                controlsPanelElement.setFocus();
                controlsImageRenderer.setImage(controlsImageHover);
                controlsButtonWasClickable = true;
                break;
            }
            default:
            {
                currentLayout = backLayout;
                backImageRenderer.setImage(backImageHover);
                backButtonWasClickable = true;
                break;
            }
        }
    }
    
    public void activateActive()
    {
        switch (currentLayout.Identity())
        {
            case 0: 
            {
                goBack();
                break;
            }
            case 10:
            {
                controlsScreen();
                break;
            }
            default:
            {
                
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
            compoundManager.addListener(this, "Accept", "Right", "Left", "Down", "Up");
        }
    }
    
    public void onAction(String name, boolean isPressed, float tpf) 
    {
        if (isPressed && name.equals("Accept"))
        {
            System.out.println("Accept");
            niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_RETURN, '0', true, false));
        }
        else if (isPressed && name.equals("Up"))
        {
            System.out.println("Up");
            niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_W, 'W', true, false));
        }
        else if (isPressed && name.equals("Down"))
        {
            System.out.println("Down");
            System.out.println("Going Down Now");
            niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_S, 'S', true, false));
        }
        
    }

    public void onAnalog(String name, float value, float tpf) 
    {
        if (slideSlow < 10)
        {
            slideSlow += (value / tpf);
            return;
        }
        else
        {
            slideSlow = 0;
        }
        if (name.equals("Right"))
        {
            System.out.println("Right");
            niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_D, 'D', true, false));
        }
        else if (name.equals("Left"))
        {
            System.out.println("Left");
            niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_A, 'A', true, false));
        }
    }

}