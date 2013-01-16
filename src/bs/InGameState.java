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
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
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
    private BulletAppState bulletAppState;
    private Screen screen;
    
    
    public InGameState(SimpleApplication app){
        this.app = (SimpleApplication) app;
        this.rootNode = app.getRootNode();
        this.guiNode = app.getGuiNode();
        this.assetManager = app.getAssetManager();  
        this.settings = app.getContext().getSettings();
        this.viewPort = app.getViewPort();
        
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
        
        Node p1SpawnNode = loadStage.getp1Spawn();
        Node p2SpawnNode = loadStage.getp2Spawn();
        Node p3SpawnNode = loadStage.getp3Spawn();
        Node p4SpawnNode = loadStage.getp4Spawn();
                
        p1SpawnNode.attachChild(geomq);
        p2SpawnNode.attachChild(geoms);
        p3SpawnNode.attachChild(geoma);
        p4SpawnNode.attachChild(geomp);
        
        CapsuleCollisionShape character = new CapsuleCollisionShape(.6f,.6f,1);
        CharacterControl player = new CharacterControl(character,0.1f);
        player.setGravity(3f);
        Node p1 = new Node("p1");
        Spatial playerModel = geomq.clone();
        playerModel.setLocalScale(1f);
        p1.attachChild(playerModel);
        p1.addControl(player);
        BoundingBox stageBox = (BoundingBox) geom.getWorldBound();
        float stageLength = stageBox.getXExtent();
        player.setPhysicsLocation(new Vector3f(-6f,4f,0f));
        
        localRootNode.attachChild(p1);
        localRootNode.attachChild(loadStage.getStageNode());
        bulletAppState.getPhysicsSpace().add(p1);
        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        

    }
    
    @Override
    public void update(float tpf){
    }
    
    
    @Override
    public void cleanup(){
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localRootNode);
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
   
    @Override
    public void stateAttached(AppStateManager stateManager){
        
        
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
