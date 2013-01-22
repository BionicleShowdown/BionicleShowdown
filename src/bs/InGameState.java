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
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author CotA
 */
public class InGameState extends AbstractAppState implements ScreenController{
    
    private SimpleApplication app;
    private AssetManager assetManager;
    private Node rootNode;
    private Node guiNode;
    private Node localRootNode = new Node ("In Game RootNode");
    private Node localGuiNode = new Node ("In Game GuiNode");
    private AppSettings settings;
    private ViewPort viewPort;
    Spatial stage;
    private Nifty nifty;
    private NiftyJmeDisplay niftyDisplay;
    private BulletAppState bulletAppState;
    private Screen screen;
    private InputManager inputManager;
    private AudioRenderer audioRenderer;
    private ViewPort guiViewPort;
    
    
    public InGameState(SimpleApplication app){
        this.app = (SimpleApplication) app;
        this.rootNode = app.getRootNode();
        this.guiNode = app.getGuiNode();
        this.assetManager = app.getAssetManager();  
        this.settings = app.getContext().getSettings();
        this.viewPort = app.getViewPort();
        this.inputManager = app.getInputManager();
        this.audioRenderer = app.getAudioRenderer();
        this.guiViewPort = app.getViewPort();
        
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        this.app = (SimpleApplication) app;
        bulletAppState = new BulletAppState();
        
        stateManager.attach(bulletAppState);
       
        rootNode.attachChild(localRootNode);
        guiNode.attachChild(localGuiNode);
        
        //Test spatial for stage
        Box b = new Box(Vector3f.ZERO, 4, 0.2f, 1);
        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        
        Box q = new Box(Vector3f.ZERO, 0.3f, 0.3f, 0.3f);
        Geometry geomq = new Geometry("Box", q);
        Material matq = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matq.setColor("Color", ColorRGBA.Blue);
        geomq.setMaterial(matq);
        
        Geometry geoms = geomq.clone();
        Geometry geomp = geomq.clone();
        Geometry geoma = geomq.clone();
        
        Stage loadStage = new Stage(assetManager.loadModel("Scenes/TestScene.j3o"),bulletAppState);
        
        Player one = new Player(geoms, bulletAppState);
        loadStage.getp1Spawn().attachChild(one.getPlayer());
        
        one.getPlayerControl().setPhysicsLocation((((Spatial)loadStage.getp1Spawn()).getWorldTranslation()));
        
        loadStage.getStageNode().attachChild(geom);
        
        localRootNode.attachChild(loadStage.getStageNode());
        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();            //Create and assign display
        nifty.addXml("Interface/GUIS/InGameHUD.xml");
        nifty.getScreen("inGameHud").getScreenController();
        nifty.gotoScreen("inGameHud");          //Just for the first one, got to the start screen
        
        guiViewPort.addProcessor(niftyDisplay); 
        

    }
    
    @Override
    public void update(float tpf){
    }
    
    
    @Override
    public void cleanup(){
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localRootNode);
        nifty.exit();
        guiViewPort.removeProcessor(niftyDisplay);
    }
    
    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        
        if(enabled){
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
