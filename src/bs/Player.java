/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JSC
 */
public class Player implements PhysicsCollisionListener{
    
    private static final Logger logger = Logger.getLogger(Stage.class.getName());

    private Spatial model;
    private Node playerNode = new Node("Player");
    private Vector3f extent;
    private CapsuleCollisionShape character;
    private CharacterControl player;
    private BulletAppState bulletAppState;
    private int[] userData;
    
    Player(Spatial s, BulletAppState bas){
        logger.log(Level.WARNING, "Player created");
        bulletAppState = bas;
        model = s;
        //userData = ud;
        playerNode.attachChild(model);
        extent = ((BoundingBox) model.getWorldBound()).getExtent(new Vector3f());
        logger.log(Level.WARNING, "X: {0} Y: {1} Z: {2}", new Object[]{extent.getX(), extent.getY(), extent.getZ()});
        setupCharacterControl();
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }
    
    public Node getPlayer(){
        return(playerNode);
    }
    public CharacterControl getPlayerControl(){
        return(player);
    }
    
    private void setupCharacterControl(){
        character = new CapsuleCollisionShape(extent.getZ()+0.2f, extent.getY()+0.1f, 1);
        player = new CharacterControl(character,0.1f);
        //player.setApplyPhysicsLocal(true);
        playerNode.addControl(player);
        bulletAppState.getPhysicsSpace().add(player);
        player.setFallSpeed(0.05f);
        
    }
    
    public void setupFeatures(float gravity, float fallSpeed, float jumpSpeed){
        player.setGravity(gravity);
        player.setJumpSpeed(jumpSpeed);
        player.setFallSpeed(fallSpeed);
    }
    
    public void collision(PhysicsCollisionEvent event) {
        
    }
    
}
