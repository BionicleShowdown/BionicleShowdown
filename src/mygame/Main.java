package mygame;

import bs.Characters;
import bs.InGameState;
import bs.StartState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static Characters charList;
    private InGameState inGameState;
    private StartState startState;
    private boolean isRunning = false;
    private Trigger enterTrigger = new KeyTrigger(KeyInput.KEY_RETURN);
    private static Nifty nifty;
    private NiftyJmeDisplay niftyDisplay;
    public static int[] player1Mappings = new int[]{KeyInput.KEY_W,KeyInput.KEY_A,KeyInput.KEY_D,KeyInput.KEY_O};

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(800, 600);
        settings.setUseInput(true);
        settings.setTitle("Bionicle Showdown");
        settings.setSettingsDialogImage("Textures/Menu/FullVoidLogo.png");
        Main app = new Main();
        
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        //stateManager.attach(new VideoRecorderAppState()); //starts recording(remove when not needed)
        
        setDisplayStatView(true);  //For now, leave this own to ensure quality play
        setDisplayFps(true);

        startState = new StartState(this);      //assign all the states here
        inGameState = new InGameState(this);

        charList = new Characters((SimpleApplication)this);
        initKeys();
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        stateManager.attach(startState);               //Attach the first start
        
        //guiViewPort.addProcessor(niftyDisplay);     //Put nifty gui into action
        flyCam.setEnabled(false);
        // flyCam.setDragToRotate(true);               //Required whne nifty gui is in use
    }
    /*This ActionListener will handle all the switching of states*/
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals("Start Game") && !isPressed) {
                if (!isRunning) {
                    stateManager.detach(startState);
                    stateManager.attach(inGameState);   //This will become the menuState for the menu 
                    //nifty.gotoScreen("inGameHud");      //Nifty is separate from the actual state, so switch screens too.
                    isRunning = !isRunning;
                }

            }
        }
    };

    public static Characters getCharList(){
        return(charList);
    }
    public static Nifty getNifty() {
        return (nifty);
    }

    private void initKeys() {
        inputManager.addMapping("Start Game", enterTrigger);
        inputManager.addListener(actionListener, new String[]{"Start Game"});
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
