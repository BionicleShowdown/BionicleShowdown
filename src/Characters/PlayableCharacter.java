/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Characters;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import menu.Costume;
import Players.Player;
import menu.Team;

/**
 *
 * @author Inferno
 */
public class PlayableCharacter 
{
    public String name;
    protected int weight; // Should this be a Float?
    protected int jumpSpeed; // ySpeed; Calculated from Weight?
    protected int fallSpeed; // Calculated from Weight?
    protected int moveSpeed; // xSpeed
    protected String symbol; // String because it'll be passed to the GUI as the pathname.
    protected String modelPath; // String because it'll be passed to the GUI as the pathname.
    protected Spatial model;
    protected Material material;
    protected final static String[] initialCostumeNames = {"Standard", "Alignment", "Inverse", "Reverse"};//, "Special"};
    protected final static String[] initialTeams = {"Red", "Blue"};//, "Green"};
    private AssetManager assetManager;
    public List<Costume> costumeKeys = new ArrayList(); // A list which holds all regular costumes
    public List<Costume> teamCostumeKeys = new ArrayList(); // A list which holds all team costumes
    public static HashMap<String, List<String>> teamCostumes = Team.buildTeamMap(initialTeams);
    
    public PlayableCharacter()
    {
        
    }
    
    public PlayableCharacter(String name)
    {
        this.name = name;
    }
    
    public int getWeight()
    {
        return weight;
    }
    
    public int getSpeed()
    {
        return moveSpeed;
    }
    
    public Spatial getModel()
    {
        return model;
    }
    
    public String getSymbol()
    {
        return symbol;
    }
    
    // Could be done from Player class. May make more sense.
    public void initializeCharacter(SimpleApplication app, Player player)
    {
        assetManager = app.getAssetManager();
        modelPath = "Models/Characters/" + name + "/" + name + ".mesh.j3o";
        model = assetManager.loadModel(modelPath); 
        material = new Material(assetManager, "Textures/Characters/" + player.getCostume()); // Only need to get the Costume because Costume's toString includes the character name
        
    }
    
    protected static void unlockCostume(List<Costume> costumeList, Costume unlockedCostume)
    {
        if (costumeList.contains(unlockedCostume))
        {
            return;
        }
        costumeList.add(unlockedCostume);
    }
    
    @Override
    public String toString()
    {
        return this.name;
    }
    
    
}
