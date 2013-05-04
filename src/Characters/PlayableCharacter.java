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
import menu.Costumes;
import menu.Player;
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
    protected final static Costumes[] costumeArray = {Costumes.Standard, Costumes.Alignment, Costumes.Inverse, Costumes.Reverse, Costumes.Special};
    protected final static String[] initialCostumeNames = {"Standard", "Alignment", "Inverse", "Reverse"};//, "Special"};
    protected final static String[] initialTeams = {"Red", "Blue"};//, "Green"};
    private AssetManager assetManager;
//    protected final static Costumes[] teamCostumeArray = {Costumes.Red, Costumes.LightRed, Costumes.DarkRed, Costumes.Blue, Costumes.LightBlue, Costumes.DarkBlue, Costumes.Green, Costumes.LightGreen, Costumes.DarkGreen};
//    ArrayList<Costumes> costumeKeys = new ArrayList(); // A list which holds all regular costumes
//    ArrayList<Costumes> teamCostumeKeys = new ArrayList(); // A list which holds all team costumes
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
    
    public void initializeCharacter(SimpleApplication app, Player player)
    {
        assetManager = app.getAssetManager();
        model = assetManager.loadModel(modelPath);
        material = new Material(assetManager, "Materials/Characters/" + player.getCostume());
        
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
