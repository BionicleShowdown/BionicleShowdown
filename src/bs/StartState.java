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
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author CotA
 */
public class StartState extends AbstractAppState implements ScreenController{
    
    private Node guiNode;
    private AssetManager assetManager;
    private SimpleApplication app;
    private Node localGuiNode = new Node("Start Screen GuiNode");
    private Picture startPic;
    private AppSettings settings;
    private NiftyJmeDisplay niftyDisplay;
    private Nifty nifty;
    private Screen screen;
    
    public StartState(SimpleApplication app){
        this.app = (SimpleApplication) app;
        this.guiNode = app.getGuiNode();
        this.assetManager = app.getAssetManager();  
        this.settings = app.getContext().getSettings();
       
        
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager,app);

        
    }
    
    
    
    @Override
    public void update(float tpf) {
        /* Nothing Yet*/
    }
    
    
    @Override
    public void cleanup(){
        
    }
    
    @Override
    public void setEnabled(boolean enabled){
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
