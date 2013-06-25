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
import com.jme3.bullet.control.GhostControl;
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
import java.util.ArrayList;
import java.util.logging.Logger;
import menu.Match;
import mygame.Main;

/**
 *
 * @author CotA
 */
public class InGameState extends AbstractAppState implements ScreenController 
{

    private static final Logger logger = Logger.getLogger(InGameState.class.getName());
    private SimpleApplication app;
    private AssetManager assetManager;
    private Node rootNode;
    private Node guiNode;
    private Node localRootNode = new Node("In Game RootNode");
    private Node localGuiNode = new Node("In Game GuiNode");
    private AppSettings settings;
    private ViewPort viewPort;
    private Stage loadStage;
    private ArrayList ledges;
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
    private PlayerPhysics[] players = new PlayerPhysics[4];
    
    private Match currentMatch;

    public InGameState() 
    {
        
    }
    
    public InGameState(Match currentMatch)
    {
        this.currentMatch = currentMatch;
    }


    @Override
    public void initialize(AppStateManager stateManager, Application app) 
    {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication)app;
        this.rootNode = this.app.getRootNode();
        this.guiNode = this.app.getGuiNode();
        this.assetManager = this.app.getAssetManager();
        this.settings = this.app.getContext().getSettings();
        this.viewPort = this.app.getViewPort();
        this.inputManager = this.app.getInputManager();
        this.audioRenderer = this.app.getAudioRenderer();
        this.guiViewPort = this.app.getViewPort();
        this.cam = this.app.getCamera();
        
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        rootNode.attachChild(localRootNode);    //Creates rootNode 
        guiNode.attachChild(localGuiNode);      //Creates guiNode

         // Can use same music node across all menus. Not sure if ideal, but it is functional, and seems to work well.
        Main.changeMusic("Sounds/Music/Super Smash Bionicle Main Theme 2.wav"); 

        //Initialize enviornment
        createEnviron();
        
        //Attach stage
        localRootNode.attachChild(loadStage.getStageNode());
        
       // bulletAppState.getPhysicsSpace().enableDebug(assetManager);

        //Get Main's nifty
        nifty = Main.getNifty();
        nifty.registerScreenController(this);
        nifty.addXml("Interface/GUIS/InGameHUD.xml");
        nifty.gotoScreen("inGameHud");
        
        //Add the InGameHUD xml file, and go this screen
        nifty.setDebugOptionPanelColors(false); // Added this so the true on the Menu won't make the screen look weird.

        //Rotate the camera to start position
        cam.setLocation(new Vector3f(0, 10, 70));
        cam.setRotation(new Quaternion(0f, -1f, 0f, 0f));
    }
    
    /*Create players algo
     * First, check the length,
     * and with each increment in length
     * create the next player appropriately
     */
    public void createEnviron()         /**STILL NEEDS ADJUSTING **/
    {
        
        //players are all null at start
        
        players[0] = null; 
        players[1] = null;
        players[2] = null;
        players[3] = null;
        
        
        players[0] = new PlayerPhysics(currentMatch.getPlayer1(), bulletAppState, inputManager, cam,false);
        for(int i = 1; i < 4; i++)
        {
            switch(i){
                case 1:
                    if(!currentMatch.getPlayer2().canPlay()){
                        break;
                    } else if(currentMatch.getPlayer2().sameCharacter(currentMatch.getPlayer1())){
                        //players[1]= new PlayerPhysics(currentMatch.getPlayer2(), bulletAppState, inputManager, cam,true);
                    } else {
                        //players[1]= new PlayerPhysics(currentMatch.getPlayer2(), bulletAppState, inputManager, cam,false);
                    }
                    break;
                case 2:
                    if(!currentMatch.getPlayer3().canPlay()){
                        break;
                    }else if(currentMatch.getPlayer3().sameCharacter(currentMatch.getPlayer2()) || currentMatch.getPlayer3().sameCharacter(currentMatch.getPlayer1())){
                       // players[2]= new PlayerPhysics(currentMatch.getPlayer3(), bulletAppState, inputManager, cam,true);
                    } else {
                       // players[2]= new PlayerPhysics(currentMatch.getPlayer3(), bulletAppState, inputManager, cam,false);
                    }
                    break;
                case 3:
                    if(!currentMatch.getPlayer4().canPlay()){
                        break;
                    }else if(currentMatch.getPlayer4().sameCharacter(currentMatch.getPlayer3()) || currentMatch.getPlayer4().sameCharacter(currentMatch.getPlayer2()) || currentMatch.getPlayer4().sameCharacter(currentMatch.getPlayer1())){
                      //  players[3]= new PlayerPhysics(currentMatch.getPlayer3(), bulletAppState, inputManager, cam,true);
                    } else {
                      //  players[3]= new PlayerPhysics(currentMatch.getPlayer3(), bulletAppState, inputManager, cam,false);
                    }
                    break;
                    
            }
        }
        
        loadStage = new Stage(assetManager.loadModel("Scenes/StageScenes/ShowdownScene.j3o"), bulletAppState);  
        ledges = loadStage.getLedges();
        
        for(int i = 0; i < 4; i++){
            switch (i){
                case 0:
                    if(players[0] != null){
                        loadStage.getp1Spawn().attachChild(players[i].getPlayer());
                        players[i].getCharacterControl().setPhysicsLocation((((Spatial) loadStage.getp1Spawn()).getWorldTranslation()));
                    }
                    break;
                case 1:
                    if(players[1] != null){
                        loadStage.getp2Spawn().attachChild(players[i].getPlayer());
                        players[i].getCharacterControl().setPhysicsLocation((((Spatial) loadStage.getp1Spawn()).getWorldTranslation()));
                    }
                    break;
                case 2:
                    if(players[2] != null){
                        loadStage.getp3Spawn().attachChild(players[i].getPlayer());
                        players[i].getCharacterControl().setPhysicsLocation((((Spatial) loadStage.getp1Spawn()).getWorldTranslation()));
                    }
                    break;
                case 3:
                    if(players[3] != null){
                        loadStage.getp4Spawn().attachChild(players[i].getPlayer());
                        players[i].getCharacterControl().setPhysicsLocation((((Spatial) loadStage.getp1Spawn()).getWorldTranslation()));
                    }
                    break;
            }
        }
            
        //loadStage.getp2Spawn().attachChild(two.getPlayer());
        //loadStage.getp3Spawn().attachChild(three.getPlayer());

        
        //two.getCharacterControl().setPhysicsLocation((((Spatial) loadStage.getp2Spawn()).getWorldTranslation()));
        //three.getCharacterControl().setPhysicsLocation((((Spatial) loadStage.getp3Spawn()).getWorldTranslation()));   
    }
    
    @Override
    public void update(float tpf) 
    {
        for(int i =0; i < ledges.size(); i++){
            System.out.println(((Spatial)ledges.get(i)).getControl(GhostControl.class).getOverlappingCount());
            System.out.println(((Spatial)ledges.get(i)).getUserData("ledgeGrabbed"));
            if(((Spatial)ledges.get(i)).getControl(GhostControl.class).getOverlappingCount() == 0 && (Boolean)((Spatial)ledges.get(i)).getUserData("ledgeGrabbed")){
                ((Spatial)ledges.get(i)).setUserData("ledgeGrabbed",false);
                System.out.println((Integer)((Spatial)ledges.get(i)).getUserData("playerGrabbing")-1);
                players[(Integer)((Spatial)ledges.get(i)).getUserData("playerGrabbing")-1].getPlayer().getControl(PlayerControl.class).resetGravity();
                
            }
        }
        
        //JME says looping streamed music isn't possible. So instead
        //look for when the music is stopped, create a new
        //instance of that music, and play it again.
//        if(music.getStatus() == Status.Stopped)
//        {
//            music = new AudioNode(assetManager, "Sounds/Music/Super Smash Bionicle Main Theme 2.wav", true);
//            music.play();
//        }
    }

    @Override
    public void cleanup() 
    {
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localRootNode);

    }

    @Override
    public void setEnabled(boolean enabled) 
    {
        super.setEnabled(enabled);

        if (enabled) {
            System.out.println("enable");
        } else {
            System.out.println("disabled");
        }

    }

    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() 
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onEndScreen() 
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
