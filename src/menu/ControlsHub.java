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
public class ControlsHub implements ScreenController
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
    private OptionsMenu optionsMenu;

    
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
        
    }

    public void onEndScreen() 
    {
        
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

}
