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
    
    private float preMuteVolume;
    
    public void initiate(SimpleApplication app) 
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
        }
        else
        {
            preMuteVolume = Main.getMusic().getVolume();
            Main.getMusic().setVolume(0);
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
    
    // Can change music volume. Should hopefully be changed to a slider rather than a Button.
    public void changeMusicVolume(String direction)
    {
        float volume = Main.getMusic().getVolume();
        System.out.println("Volume was: " + volume);
        if (direction.equals("+"))
        {
            Main.getMusic().setVolume(volume + 1);
        }
        else if (direction.equals("-"))
        {
            Main.getMusic().setVolume(volume - 1);
        }
        else
        {
            System.out.println("An Error has Occured.");
        }
        System.out.println("Volume is now: " + Main.getMusic().getVolume());
    }

}
