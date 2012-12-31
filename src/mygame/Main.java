package mygame;

import bs.InGameState;
import bs.StartState;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private InGameState inGameState;
    private StartState startState;
    
    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(800,600);
        settings.setUseInput(true);
        settings.setTitle("Bionicle Showdown");
        settings.setSettingsDialogImage("Textures/Menu/ShowdownLogo.png");
        Main app = new Main();
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        setDisplayStatView(false);
        setDisplayFps(false);
        
        startState = new StartState(this);
        //inGameState = new InGameState(this);
        
        stateManager.attach(startState);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
