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
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.NiftyControl;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.Main;

/**
 *
 * @author Inferno
 */
public class OptionsMenu implements ScreenController
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
    private MainMenu mainMenu;
    
    private float preMuteVolume;
    private static float defaultMusicVolume = 1.0f;
    public static float currentMusicVolume = 1.0f;
    private static float defaultSFXVolume = 1.0f;
    public static float currentSFXVolume = defaultSFXVolume;
    private static float defaultVoiceVolume = 1.0f;
    public static float currentVoiceVolume = defaultVoiceVolume;
    
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
        this.audioRenderer = this.app.getAudioRenderer();
        this.guiViewPort = this.app.getViewPort();
        this.stateManager = this.app.getStateManager();
        nifty = Main.getNifty();
        nifty.registerScreenController(this);
        nifty.addXml("Interface/GUIS/OptionsMenu.xml");
        nifty.gotoScreen("OptionsMenu");          //Just for the first one, got to the start screen
        
        nifty.setDebugOptionPanelColors(true); // Leave it on true for now, so you can see the costume buttons.
    }
    
    @Override
    public void onStartScreen()
    {
        nifty.getCurrentScreen().findElementByName("CurrentMusicVolume").getRenderer(TextRenderer.class).setText(getMusicVolume());
        nifty.getCurrentScreen().findElementByName("CurrentSFXVolume").getRenderer(TextRenderer.class).setText(getSFXVolume());
        nifty.getCurrentScreen().findElementByName("CurrentVoiceVolume").getRenderer(TextRenderer.class).setText(getVoiceVolume());
        screen.findNiftyControl("musicVolumeSlider", Slider.class).setValue(Main.getMusic().getVolume());
        screen.findNiftyControl("sfxVolumeSlider", Slider.class).setValue(currentSFXVolume);
        screen.findNiftyControl("voiceVolumeSlider", Slider.class).setValue(currentVoiceVolume);
    }
    
    @Override
    public void onEndScreen()
    {
        
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
    
    public void switchMusic()
    {
        String currentMusicSelection = Main.getMusicSelection();
        if (currentMusicSelection.equals("Sounds/Music/Fire and Ice.wav"))
        {
            Main.changeMusic("Sounds/Music/Super Smash Bionicle Main Theme 2.wav");
        }
        else
        {
            Main.changeMusic("Sounds/Music/Fire and Ice.wav");
        }
    }
    
    // Changes the volume value and updates the text on the volume label when the Slider changes
    @NiftyEventSubscriber(id="musicVolumeSlider")
    public void getMusicVolume(String id, SliderChangedEvent event)
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
        nifty.getCurrentScreen().findElementByName("CurrentMusicVolume").getRenderer(TextRenderer.class).setText("Music Volume: " + volume + "%");
    }
    
    @NiftyEventSubscriber(id="sfxVolumeSlider")
    public void getSFXVolume(String id, SliderChangedEvent event)
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
        nifty.getCurrentScreen().findElementByName("CurrentSFXVolume").getRenderer(TextRenderer.class).setText("SFX Volume: " + volume + "%");
    }
    
    @NiftyEventSubscriber(id="voiceVolumeSlider")
    public void getVoiceVolume(String id, SliderChangedEvent event)
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
        nifty.getCurrentScreen().findElementByName("CurrentVoiceVolume").getRenderer(TextRenderer.class).setText("Voice Volume: " + volume + "%");
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
    public void changeMusicVolume(String id, SliderChangedEvent event)
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
        System.out.println("" + event.getValue());
        currentMusicVolume = volume;
        Main.setMusicVolume(volume);
        Main.getMusic().setVolume(volume);
    }
    
    @NiftyEventSubscriber(id="sfxVolumeSlider")
    public void changeSFXVolume(String id, SliderChangedEvent event)
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
        System.out.println("" + event.getValue());
        currentSFXVolume = volume;
    }
    
    @NiftyEventSubscriber(id="voiceVolumeSlider")
    public void changeVoiceVolume(String id, SliderChangedEvent event)
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
        System.out.println("" + event.getValue());
        currentVoiceVolume = volume;
    }
    
    public void controlsScreen()
    {
        System.out.println("Controls Screen!");
    }
    
    public void goBack()
    {
        mainMenu.initiate(app);
    }

}
