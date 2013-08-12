/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.logging.Logger;
import menu.MainMenu;
import mygame.Main;

/**
 *
 * @author CotA
 */
public class StartMenu extends AbstractAppState implements ScreenController, KeyInputHandler, ActionListener
{

    private static final Logger logger = Logger.getLogger(StartMenu.class.getName());
    private AssetManager assetManager;
    private SimpleApplication app;
    private NiftyJmeDisplay niftyDisplay;
    private Nifty nifty;
    private InputManager inputManager;
    private AudioRenderer audioRenderer;
    private ViewPort guiViewPort;
    private Screen screen;
    private AppStateManager stateManager;

    public StartMenu() 
    {
        
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
        this.niftyDisplay = Main.getNiftyDisplay();
        nifty = Main.getNifty();
        nifty.registerScreenController(this);
        nifty.addXml("Interface/GUIS/StartMenu.xml");
        nifty.gotoScreen("StartScreen");          //Just for the first one, got to the start screen
        
        nifty.setDebugOptionPanelColors(false);

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

    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() 
    {
//        inputManager.setCursorVisible(false);
        initJoy();
    }

    public void onEndScreen() 
    {
        if (Main.joysticks.length != 0)
        {
            inputManager.deleteMapping("Accept");
        }
    }
    
    
    public void startGame()
    {
        MainMenu menuState = new MainMenu();
        stateManager.attach(menuState);
    }
    

    public boolean keyEvent(NiftyInputEvent event) 
    {
        if (event == NiftyInputEvent.Activate)
        {
            startGame();
            return true;
        }
        return false;
    }
    
    private void initJoy() 
    {
        if (Main.joysticks.length != 0)
        {
            Main.joysticks[0].getButton("6").assignButton("Accept");
            
            if (Main.joysticks[0].getName().equals("Logitech Dual Action"))
            {
                inputManager.deleteMapping("Accept");
                Main.joysticks[0].getButton("9").assignButton("Accept");
            }
            inputManager.addListener(this, "Accept");
        }
    }

    public void onAction(String name, boolean isPressed, float tpf) 
    {
        if (isPressed && name.equals("Accept"))
        {
            System.out.println("Accept");
            niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_RETURN, '0', true, false));
        }
    }

    
}
