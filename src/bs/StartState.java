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
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;

/**
 *
 * @author CotA
 */
public class StartState extends AbstractAppState{
    
    private Node guiNode;
    private AssetManager assetManager;
    private SimpleApplication app;
    private Node localGuiNode = new Node("Start Screen GuiNode");
    private Picture startPic;
    private AppSettings settings;
    
    public StartState(SimpleApplication app){
        this.app = (SimpleApplication) app;
        this.guiNode = app.getGuiNode();
        this.assetManager = app.getAssetManager();  
        this.settings = app.getContext().getSettings();
       
        
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager,app);
        
        startPic = new Picture("Start Image");
        startPic.setImage(assetManager,"Textures/Menu/StartScreen.png", true);
        startPic.setWidth(settings.getWidth());
        startPic.setHeight(settings.getHeight());
        localGuiNode.attachChild(startPic);
    }
    
    @Override
    public void update(float tpf) {
        /* Nothing Yet*/
    }
    
    @Override
    public void stateAttached(AppStateManager stateManager){
        guiNode.attachChild(localGuiNode);
    }
    
    @Override
    public void stateDetached(AppStateManager stateManager) {
        guiNode.detachChild(localGuiNode);

    }
    
    
}
