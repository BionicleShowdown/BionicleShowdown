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

/**
 *
 * @author JSC
 */
public class Player implements PhysicsCollisionListener {

    private static final Logger logger = Logger.getLogger(Stage.class.getName());
    private Spatial model;
    private InputManager im;
    private PlayerControl pc;
    private Node playerNode = new Node("Player");
    private Vector3f extent;
    private CapsuleCollisionShape character;
    private CharacterControl player;
    private BulletAppState bulletAppState;
    private Camera cam;
    private int row;

    /* Player class builds player with proper inputs, name, etc.
     * Haven't worked out the passing of data nor the inputs, 
     * but online docs should help. Look at PlayerControl for
     * logic, this is merely setup
     */
    Player(int r, BulletAppState bas, InputManager ipm, Camera cm, boolean exists) {

        bulletAppState = bas;
        im = ipm;
        cam = cm;
        row = r;
        if(exists){
            model = Main.getCharList().getModel(0).clone();
        } else {
            model = Main.getCharList().getModel(0);
        }
        
        extent = ((BoundingBox) model.getWorldBound()).getExtent(new Vector3f());
        model.setLocalTranslation(new Vector3f(0f,-extent.getY(),0f));
        playerNode.attachChild(model);
        setupCharacterControl();
        pc = new PlayerControl(im, player, cam);
        playerNode.addControl(pc);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }
    
    /* Return the node of the player*/
    public Node getPlayer() {
        return (playerNode);
    }
    
    /*Return CharacterControl */
    public CharacterControl getCharacterControl() {
        return (player);
    }

    /* Setup CharacterControl */
    private void setupCharacterControl() {
        character = new CapsuleCollisionShape(extent.getZ() + 0.7f, extent.getY(), 1);
        player = new CharacterControl(character, 1f);
       
        playerNode.addControl(player);
        bulletAppState.getPhysicsSpace().add(player);
        player.setGravity(Main.getCharList().getGravity(row));
        player.setJumpSpeed(Main.getCharList().getJumpSpeed(row));
        player.setFallSpeed(Main.getCharList().getJumpSpeed(row));

    }

    /* Setup specifc features of player */
    public void setupFeatures(float gravity, float fallSpeed, float jumpSpeed) {
        player.setGravity(gravity);
        player.setJumpSpeed(jumpSpeed);
        player.setFallSpeed(fallSpeed);
    }

    public void collision(PhysicsCollisionEvent event) {
    }
}
