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
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.logging.Logger;
import menu.MainMenu;
import mygame.Main;

/**
 *
 * @author CotA
 */
public class StartState extends AbstractAppState implements ScreenController {

    private static final Logger logger = Logger.getLogger(StartState.class.getName());
    private AssetManager assetManager;
    private SimpleApplication app;
    private NiftyJmeDisplay niftyDisplay;
    private Nifty nifty;
    private InputManager inputManager;
    private AudioRenderer audioRenderer;
    private ViewPort guiViewPort;
    private Screen screen;
    private AppStateManager stateManager;

    public StartState() {
    }


    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.audioRenderer = this.app.getAudioRenderer();
        this.guiViewPort = this.app.getViewPort();
        this.stateManager = this.app.getStateManager();
        nifty = Main.getNifty();
        nifty.registerScreenController(this);
        nifty.addXml("Interface/GUIS/StartScreenPulse.xml");
        nifty.gotoScreen("start");          //Just for the first one, got to the start screen
        


    }

    @Override
    public void update(float tpf) {
        /* Nothing Yet*/
    }

    @Override
    public void cleanup() {
        MainMenu menuState = new MainMenu();
        stateManager.attach(menuState);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);


    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() {
        //throw new UnsupportedOperationException("Not supported yet.");
//        inputManager.setCursorVisible(false);
    }

    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
