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
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioNode.Status;
import com.jme3.audio.AudioRenderer;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.logging.Logger;
import mygame.Main;

/**
 *
 * @author CotA
 */
public class InGameState extends AbstractAppState implements ScreenController {

    private static final Logger logger = Logger.getLogger(InGameState.class.getName());
    private SimpleApplication app;
    private AssetManager assetManager;
    private Node rootNode;
    private Node guiNode;
    private Node localRootNode = new Node("In Game RootNode");
    private Node localGuiNode = new Node("In Game GuiNode");
    private AppSettings settings;
    private ViewPort viewPort;
    Stage loadStage;
    private NiftyJmeDisplay niftyDisplay;
    private Screen screen;
    private Nifty nifty;
    private BulletAppState bulletAppState;
    private InputManager inputManager;
    private AudioRenderer audioRenderer;
    private ViewPort guiViewPort;
    private Camera cam;
    private Camera flyCam;
    private AudioNode music;

    public InGameState() {
    }

    public InGameState(SimpleApplication app) {
        this.app = (SimpleApplication) app;
        this.rootNode = app.getRootNode();
        this.guiNode = app.getGuiNode();
        this.assetManager = app.getAssetManager();
        this.settings = app.getContext().getSettings();
        this.viewPort = app.getViewPort();
        this.inputManager = app.getInputManager();
        this.audioRenderer = app.getAudioRenderer();
        this.guiViewPort = app.getViewPort();
        this.cam = app.getCamera();

        
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        rootNode.attachChild(localRootNode);    //Creates rootNode 
        guiNode.attachChild(localGuiNode);      //Creates guiNode

        //Load Stage in Game
        loadStage = new Stage(assetManager.loadModel("Scenes/StageScenes/ShowdownScene.j3o"), bulletAppState);   
        
        //Start streaming music
        music = new AudioNode(assetManager, "Sounds/Music/Super Smash Bionicle Main Theme 2.wav", true);
        music.play();
        
        //Initialize players
        createPlayers();
        
        //Attach stage
        localRootNode.attachChild(loadStage.getStageNode());
        
        //bulletAppState.getPhysicsSpace().enableDebug(assetManager);

        //Get Main's nifty
        nifty = Main.getNifty();
        
        //Add the InGameHUD xml file, and go this screen
        nifty.addXml("Interface/GUIS/InGameHUD.xml");
        nifty.gotoScreen("inGameHud");        

        //Rotate the camera to start position
        cam.setLocation(new Vector3f(0, 10, 70));
        cam.setRotation(new Quaternion(0f, -1f, 0f, 0f));
    }
    
    /*Create players algo
     * First, check the length,
     * and with each increment in length
     * create the next player appropriately
     */
    public void createPlayers()         /**STILL NEEDS ADJUSTING **/
    {
        int[] characters;
        characters = new int[]{0};
        
        //players are all null at start
        Player one = null;
        Player two = null;
        Player three = null;
        Player four = null;
        
        
        one = new Player(characters[0], bulletAppState, inputManager, cam,false);
        if(characters.length > 1) {
            for(int i = 0; i < characters.length; i++){
                if(characters[i] == characters[1] && i!=1){
                    two = new Player(characters[1], bulletAppState, inputManager, cam,true);
                    break;
                }
                if(i == characters.length-1){
                    two = new Player(characters[1], bulletAppState, inputManager, cam,false);
                }
            }
        }
        if(characters.length > 2){
            for(int i = 0; i < characters.length; i++){
                if(characters[i] == characters[2] && i!=2){
                    three = new Player(characters[1], bulletAppState, inputManager, cam,true);
                    break;
                }
                if(i == characters.length-1){
                    three = new Player(0, bulletAppState, inputManager, cam,false);
                }
            }
        }
        
        
        loadStage.getp1Spawn().attachChild(one.getPlayer());
        //loadStage.getp2Spawn().attachChild(two.getPlayer());
        //loadStage.getp3Spawn().attachChild(three.getPlayer());

        one.getCharacterControl().setPhysicsLocation((((Spatial) loadStage.getp1Spawn()).getWorldTranslation()));
        //two.getCharacterControl().setPhysicsLocation((((Spatial) loadStage.getp2Spawn()).getWorldTranslation()));
        //three.getCharacterControl().setPhysicsLocation((((Spatial) loadStage.getp3Spawn()).getWorldTranslation()));   
    }
    
    @Override
    public void update(float tpf) {
        //JME says looping streamed music isn't possible. So instead
        //look for when the music is stopped, create a new
        //instance of that music, and play it again.
        if(music.getStatus() == Status.Stopped){
            music = new AudioNode(assetManager, "Sounds/Music/Super Smash Bionicle Main Theme 2.wav", true);
            music.play();
        }
    }

    @Override
    public void cleanup() {
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localRootNode);

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (enabled) {
            System.out.println("enable");
        } else {
            System.out.println("disabled");
        }

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
