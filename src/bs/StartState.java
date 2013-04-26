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

    public StartState() {
    }

    public StartState(SimpleApplication app) {
        this.app = (SimpleApplication) app;
        this.assetManager = app.getAssetManager();
        this.inputManager = app.getInputManager();
        this.audioRenderer = app.getAudioRenderer();
        this.guiViewPort = app.getViewPort();


    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        nifty = Main.getNifty();
        nifty.addXml("Interface/GUIS/StartScreenPulse.xml");
        //stateManager.attach((StartState)nifty.getScreen("start").getScreenController());
        nifty.gotoScreen("start");          //Just for the first one, got to the start screen


    }

    @Override
    public void update(float tpf) {
        /* Nothing Yet*/
    }

    @Override
    public void cleanup() {
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
    }

    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
