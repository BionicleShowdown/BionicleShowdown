/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.logging.*;
/**
 *
 * @author CotA
 */
public class Stage implements PhysicsCollisionListener {
    
    private static final Logger logger = Logger.getLogger(Stage.class.getName());
    
   // private Spatial stage;
    private int height;
    private int width;
    private int top = -1;
    private int bottom = -1;
    private int left = -1;
    private int right = -1;
    private Node boundingBoxNodes;
    private BulletAppState bulletAppState;
    
    
    /*Constructor accepts a spatial (yet to be implemented) for the stage
     * and the height and width of the stage from
     * the CENTER to setup the blast barriers, as well as the BulletAppState
     * from the AppState. Then, blast barriers
     * are applied to the stage
     */
    public Stage(int h, int w, BulletAppState bas){
       // stage = s;
        height = h;
        width = w;
        bulletAppState = bas;
        setupStage();
    }
    /* Overloaded constructor accepts spatial for the stage (yet to
     * be implemented), then distances from center for each blast
     * barrier, starting from top and moving clockwise (top, right,
     * bottom, left) as well as the BulletAppState from the AppState.
     * The height and the width here become the entire stage from
     * top to bottom and left to right so that the blast barriers
     * can cover the entire stage accordingly.
     * Then, the blast barriers are applied to the stage     * 
     */
    public Stage(int t, int r, int b, int l, BulletAppState bas){
        // stage = s;
        height = t+b;
        width = r+l;
        top = t;
        bottom  = b;
        left = l;
        right = r;
        bulletAppState = bas;
        setupStage();
    }
    
    /* Getter for the Node containing
     * all the blast barriers
     */
    public Node getBoundingBoxes(){
        return(boundingBoxNodes);
    }
    /* Getter for the height of 
     * the stage
     */
    public int getHeight(){
        return(height);
    }
    /* Getter for the width of
     * the stage
     */
    public int getWidth(){
        return(width);
    }
    
    /* Sets up spatial for stage and all
     * the blast barriers provided in 
     * the constructor
     */
    private void setupStage(){
        boundingBoxNodes = new Node("Gather all blast barriers");
        
        GhostControl boundingBoxBot = new GhostControl(new BoxCollisionShape(new Vector3f(width,1,1)));
        GhostControl boundingBoxTop = new GhostControl(new BoxCollisionShape(new Vector3f(width,1,1)));
        GhostControl boundingBoxRight = new GhostControl(new BoxCollisionShape(new Vector3f(1,height,1)));
        GhostControl boundingBoxLeft = new GhostControl(new BoxCollisionShape(new Vector3f(1,height,1)));
        
        Node boundingBoxRightNode = new Node("Right blast barrier");
        Node boundingBoxLeftNode = new Node("Left blast barrier");
        Node boundingBoxTopNode = new Node("Top blast barrier");
        Node boundingBoxBotNode = new Node("Bottom blast barrier");
        
        boundingBoxTopNode.addControl(boundingBoxTop);
        boundingBoxBotNode.addControl(boundingBoxBot);
        boundingBoxRightNode.addControl(boundingBoxRight);
        boundingBoxLeftNode.addControl(boundingBoxLeft);
        
        if(top == -1 && bottom == -1 && right == -1 && left == -1) {
        
            boundingBoxTopNode.setLocalTranslation(new Vector3f(0,height,0));
            boundingBoxBotNode.setLocalTranslation(new Vector3f(0,-height,0));
            boundingBoxLeftNode.setLocalTranslation(new Vector3f(-width,0,0));
            boundingBoxRightNode.setLocalTranslation(new Vector3f(width,0,0));
        
        } else {
            
            boundingBoxTopNode.setLocalTranslation(new Vector3f(0,top,0));
            boundingBoxBotNode.setLocalTranslation(new Vector3f(0,-bottom,0));
            boundingBoxLeftNode.setLocalTranslation(new Vector3f(-left,0,0));
            boundingBoxRightNode.setLocalTranslation(new Vector3f(right,0,0));
        }
       
        if(top != 0){
            boundingBoxNodes.attachChild(boundingBoxTopNode);
        }
        if(bottom != 0){
            boundingBoxNodes.attachChild(boundingBoxBotNode);
        }
        if(right != 0) {
            boundingBoxNodes.attachChild(boundingBoxRightNode);
        }
        if(left != 0) {
            boundingBoxNodes.attachChild(boundingBoxLeftNode);
        }
        
        bulletAppState.getPhysicsSpace().add(boundingBoxBot);
        bulletAppState.getPhysicsSpace().add(boundingBoxTop);
        bulletAppState.getPhysicsSpace().add(boundingBoxLeft);
        bulletAppState.getPhysicsSpace().add(boundingBoxRight);
        
    }
    
    /* Detects collisions for players, which will
     * then call a method which would destroy the player
     * subtract one from health, and respawn him (yet
     * to be created).
     */
    public void collision(PhysicsCollisionEvent event) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
