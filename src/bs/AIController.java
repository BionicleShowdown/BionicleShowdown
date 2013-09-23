/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs;

import Characters.PlayableCharacter;
import Players.Player;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;




public class AIController extends AbstractControl implements AnimEventListener {
    
    // Public member data
    //Sets other code to be launched
    public IdleMovement behaviourOnLostTrack; //MonoBehaviour is other code
    public EnemyMoveAttack behaviourOnSpotted;
    
    // Private memeber data
    private CharacterControl container;
    private Transform character;
    private Spatial target_spatial;
    private boolean insideInterestArea = true;
    private Node rootNode;
    
    private Node shootables;
    private GhostControl ghost;

    private BulletAppState bulletAppState;
    private float distance = 100f;
    
    private int stock = 3;
    private boolean grabbingLedge = false;
    private float startGravity;
    private float startFallSpeed;
    private float startJumpSpeed;
    private AnimChannel animationChannel;
    private AnimControl animationControl;
    private Spatial model;
    private String number;

    private InGameState sourceState;
    private float targetDistMax = 25f;

    /*function Awake () {
    //On Awake collects position, rotation, and scale relative to executer of the script
    character = transform;
    //On Awake attempts to find out whether the GameObject has the wanted target tag 
    player = GameObject.FindWithTag ("target").transform;
    }*/
    
    SceneGraphVisitor visitor = new SceneGraphVisitor() {
        public void visit(Spatial spatial) {
            
            if(spatial.getUserData("tag")=="target"){
                target_spatial = spatial;
            }
        }
    };
    
    
    public AIController(Player p,Spatial m,Node shoot, Node root, BulletAppState bas, InGameState ss){
        //This is needed for the raycasting. The ray needs to know
        //what items it can collide with, so this group of nodes
        //passed from outside is that group of items.
        bulletAppState = bas;
        shootables = shoot;
        rootNode = root;
        model = m;
        number = p.playerNumber;
        sourceState = ss;

    }
    
    public int getNumber(){
        return Integer.valueOf((number.charAt(6)))-48;
    }
    
    public void respawn(Spatial event, Node respawn, BulletAppState bas){
        //if players lives are greater than 1
        
        stock--; // Moved this up, as stock would technically be removed before verifying they're still alive
        sourceState.adjustStock(stock, number);
        
        if(stock > 0){
//            stock--;
            respawn.attachChild(event);
            container.setPhysicsLocation(respawn.getWorldTranslation());
        } else {
            event.removeFromParent();
            bas.getPhysicsSpace().remove(event);
        }
        
    }
    public void decreaseStock() {
        stock--;
    }

    public void increaseStock() {
        stock++;
    }

    public int getStock() {
        return (stock);
    }
    
    public void setStock(int newStock)
    {
        stock = newStock;
    }
    //This is the initialization of the control for the AI
    @Override
    public void setSpatial(Spatial spatial) {
      super.setSpatial(spatial);
      behaviourOnLostTrack = spatial.getControl(IdleMovement.class);
      behaviourOnSpotted = spatial.getControl(EnemyMoveAttack.class);
      behaviourOnLostTrack.setEnabled(true);
      behaviourOnSpotted.setEnabled(false);
      character = spatial.getWorldTransform();
      container = spatial.getControl(CharacterControl.class);
      startGravity = container.getGravity();
      startFallSpeed = container.getFallSpeed();
      startJumpSpeed = container.getJumpSpeed();
      animationControl = model.getControl(AnimControl.class);
      animationControl.addListener(this);
      System.out.println(animationControl.getNumChannels() + " Channels");
      animationChannel = animationControl.getChannel(0);
      /*ghost = new GhostControl(new BoxCollisionShape(new Vector3f(6,4,1)));
      Node position = new Node();
      position.addControl(ghost);
      position.setLocalTranslation(0,.5f,10f);
      ((Node)spatial).attachChild(position);
      bulletAppState.getPhysicsSpace().add(ghost);*/
    }
    
    public void findTarget(){
        rootNode.breadthFirstTraversal(visitor);
        behaviourOnSpotted.setTarget(target_spatial);
    }
        
    public GhostControl getGhostControl() {
        return ghost;
    }

    private void OnEnable (Spatial spatial) {
    //On OnEnable launches wanted code that was preset
        behaviourOnLostTrack.setEnabled(true);
        behaviourOnSpotted.setEnabled(false);
    }
    
    public void collision() {
        if(CanSeeTarget()){
            if(target_spatial != null && distance < targetDistMax){
                //Triggers OnSpotted
                OnEnterInterestArea();
                //System.out.println(ghost);
            }
        } else {
            OnExitInterestArea();
        }
    }
    
    private void OnEnterInterestArea(){
        //Sets this varible to true if bounding boxes intersect for a reason
        insideInterestArea = true;
        OnSpotted();
    }
    
    
    public void OnExitInterestArea() {
        //Alerts that bounding boxes no longer intersect target and set OnLostTract function
        insideInterestArea = false;
        OnLostTrack();
    }
    
    private void OnSpotted() {
        //OnSpotted checks if insideInterestArea is true, then launches behaviors based off that
        if(!insideInterestArea){
            return;
        }
        
        if(!behaviourOnSpotted.isEnabled()) {
            behaviourOnSpotted.setEnabled(true);
            behaviourOnLostTrack.setEnabled(false);
        }
    }
    
    public void grabLedge(Spatial event){
        System.out.println("Character " + container.getPhysicsLocation().y);
        System.out.println("Ledge " + event.getWorldTranslation().y);
        if(container.getPhysicsLocation().y < event.getWorldTranslation().y && !grabbingLedge){
            
            
            grabbingLedge = true;
            animationChannel.setAnim("Slow Walk");
            container.setGravity(0);
            container.setFallSpeed(0);
            container.setJumpSpeed(0);
        }
    }
    
    private void OnLostTrack() {
       // OnLostTrack resets behaviours and execution of this code launches the behaviourOnLostTrac code
        System.out.println("Entered ON LOST TRACK");
        if(!(behaviourOnLostTrack.isEnabled())){ 
            behaviourOnLostTrack.setEnabled(true);
            behaviourOnSpotted.setEnabled(false);
        }
    }
    public void resetGravity() {
        container.setGravity(startGravity);
        container.setFallSpeed(startFallSpeed);
        container.setJumpSpeed(startJumpSpeed);
        grabbingLedge = false;
        System.out.println("settings are: " + startGravity + " " + startFallSpeed + " " + startJumpSpeed);
    }
    
    public boolean CanSeeTarget(){
        //When CanSeeTarget is activated, it collects position of target relative to this code executer...
        //Then a var hit is created that fires an indefenintely unending line in all directions, called a RayCast...
        //When the RayCast collides, it collects the targets position relative to the event of hit and repeats this until cancelled
        Vector3f targetDirection = new Vector3f(0,0,0)  ;
        target_spatial.getWorldTransform().getTranslation().subtract(character.getTranslation(),targetDirection);
        CollisionResults results = new CollisionResults();
        Ray hit = new Ray(character.getTranslation(),targetDirection);
        target_spatial.collideWith(hit,results);
        for(int i = 0; i < results.size(); i++){     
            if((results.getCollision(i).getGeometry().getWorldTranslation().getX()) == target_spatial.getWorldTranslation().getX()){
                distance = results.getCollision(i).getContactPoint().distance(character.getTranslation());
                return true;
            } 
        }
        return false;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        collision();
        //CanSeeTarget();

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
       // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return null;
        //throw new UnsupportedOperationException("Not supported yet.");
    }
   

    public boolean isGrabbingLedge(){
        return grabbingLedge;
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

}
