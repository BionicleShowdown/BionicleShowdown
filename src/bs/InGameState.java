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
import com.jme3.bullet.BulletAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
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
        
        Stage stage = new Stage(7,7,3,2,bulletAppState);
        
        rootNode.attachChild(stage.getBoundingBoxes());
        
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
