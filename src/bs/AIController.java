/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs;

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




public class AIController extends AbstractControl{
    
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
    
    public AIController(Node shoot, Node root, BulletAppState bas){
        //This is needed for the raycasting. The ray needs to know
        //what items it can collide with, so this group of nodes
        //passed from outside is that group of items.
        bulletAppState = bas;
        shootables = shoot;
        rootNode = root;        
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
        System.out.println(distance);
        if(CanSeeTarget()){
            if(target_spatial != null && /*&& ghost.getOverlappingObjects().contains(target_spatial.getControl(CharacterControl.class)) &&*/ distance < 12f ){
                //Triggers OnSpotted
                OnEnterInterestArea();
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
        
        System.out.println("Entered ON SPOTTED");
        if(!behaviourOnSpotted.isEnabled()) {
            behaviourOnSpotted.setEnabled(true);
            behaviourOnLostTrack.setEnabled(false);
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
        System.out.println("AI ON");
        //CanSeeTarget();

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
       // throw new UnsupportedOperationException("Not supported yet.");
    }


}
