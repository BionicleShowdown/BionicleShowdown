/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

/**
 *
 * @author JSC
 */
public final class Characters {
    
    private AssetManager assetManager;
    Object[][] characters;
    
    public Characters(SimpleApplication app){
        assetManager = app.getAssetManager();
        
        characters = new Object[][]{{"Tahu",assetManager.loadModel("Models/Characters/Tahu/Tahu.mesh.j3o"),90,30,30},
                                      {2,3,4},
                                      {4,5,2}};
    }
    
    /**
     *
     * @param row
     * @return Character's name
     */
    public String getName(int row){
        return((String)characters[row][0]);
    }
    
    /**
     *
     * @param row
     * @return Character's model
     */
    public Spatial getModel(int row){
        return((Spatial)characters[row][1]);
    }
    
    public int getGravity(int row){
        return((Integer)characters[row][2]);
    }
    
    public int getJumpSpeed(int row){
        return((Integer)characters[row][3]);
    }
    
    public int getFallSpeed(int row){
        return((Integer)characters[row][4]);
    }
    
}
