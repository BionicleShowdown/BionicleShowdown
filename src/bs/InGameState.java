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
import com.jme3.scene.Spatial;

/**
 *
 * @author CotA
 */
public class InGameState extends AbstractAppState {
    
    private SimpleApplication app;
    private AssetManager assetManager;
    Spatial stage;
    
    
    
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager,app);
        this.app = (SimpleApplication) app;
        this.assetManager = app.getAssetManager();
    }
    
    @Override
    public void update(float tpf){
        /*Nothing yet*/
    }
    
    @Override
    public void stateAttached(AppStateManager stateManager){
        /*Nothing yet*/
    }
}
