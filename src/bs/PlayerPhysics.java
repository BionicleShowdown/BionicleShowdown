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
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.input.KeyInput;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.Main;
import Players.Player;


/**
 *
 * @author JSC
 */
public class PlayerPhysics implements PhysicsCollisionListener 
{

    private static final Logger logger = Logger.getLogger(Stage.class.getName());
    private Spatial model;
    private InputManager im;
    private PlayerControl pc;
    private Node playerNode; 
    private Vector3f extent;
    private CapsuleCollisionShape character;
    private CharacterControl player;
    private BulletAppState bulletAppState;
    private Camera cam;
    private Player menuPlayer;
    private AIController aicontroller;
    private IdleMovement idling;
    private EnemyMoveAttack attacking;
    private Node rootNode;
    private Node shootables;
    
    private InGameState sourceState;


    /* Player class builds player with proper inputs, name, etc.
     * Haven't worked out the passing of data nor the inputs, 
     * but online docs should help. Look at PlayerControl for
     * logic, this is merely setup
     */
    PlayerPhysics(Node root, Player p, BulletAppState bas, InputManager ipm, Camera cm, InGameState ss, boolean exists) 
    {

        bulletAppState = bas;
        im = ipm;
        cam = cm;
        menuPlayer = p;
        rootNode = root;
        sourceState = ss;
        //If the character chosen is the same, generate a clone for its model
        if(exists)
        {
            model = p.getCharacter().getModel().clone();
        } else 
        {
            model = p.getCharacter().getModel();
        }
        
        //Generate Bounding Box and setup up its Character Control, then
        //attach that control and add it to the physics space listener
        extent = ((BoundingBox) model.getWorldBound()).getExtent(new Vector3f());
        model.setLocalTranslation(new Vector3f(0f,-extent.getY(),0f));
       // model.rotate(0,3.14f/2f,0);
        playerNode = new Node("Player");
        playerNode.attachChild(model);
        setupCharacterControl();
        
        
        //For dev purposes only (AI), make player 2 without controls
        if(!p.toString().equals("Player2")){
            pc = new PlayerControl(p,model,im, player, cam, sourceState);
            playerNode.addControl(pc);
            playerNode.setUserData("tag","target");
        }   else {
            idling = new IdleMovement(model); 
            aicontroller = new AIController(shootables, rootNode,bulletAppState);
            attacking = new EnemyMoveAttack(model,rootNode, aicontroller);
            playerNode.addControl(idling);
            playerNode.addControl(attacking);
            playerNode.addControl(aicontroller);
        }
        bulletAppState.getPhysicsSpace().add(playerNode);
        rootNode.attachChild(playerNode);
        
    }
    
    /* Return the node of the player*/
    public Node getPlayer() 
    {
        return (playerNode);
    }
    
    /* Returns the menu player. */
    public Player getMenuPlayer()
    {
        return menuPlayer;
    }
    
    /*Return CharacterControl */
    public CharacterControl getCharacterControl() 
    {
        return (player);
    }

    
    
    
    /* Setup CharacterControl
     * aka main attrubutes and CapsuleCollider
     */
    private void setupCharacterControl() 
    {
        character = new CapsuleCollisionShape(extent.getZ() + 0.7f, extent.getY(), 1);
        player = new CharacterControl(character, 1f);
        player.setViewDirection(new Vector3f(1,0,0));
        playerNode.addControl(player);
        bulletAppState.getPhysicsSpace().add(player);
        player.setGravity(menuPlayer.getCharacter().getWeight());
        player.setJumpSpeed(menuPlayer.getCharacter().getJumpSpeed());
        player.setFallSpeed(menuPlayer.getCharacter().getFallSpeed());
        player.setMaxSlope(menuPlayer.getCharacter().getMaxSlope());
    }

    /* Setup specifc features of player */
    public void setupFeatures(float gravity, float fallSpeed, float jumpSpeed) 
    {
        player.setGravity(gravity);
        player.setJumpSpeed(jumpSpeed);
        player.setFallSpeed(fallSpeed);
    }
    
    public void setStock(int newStock)
    {
        if (pc == null)
        {
            return;
        }
        pc.setStock(newStock);
    }

    public void collision(PhysicsCollisionEvent event) {
    }
}
