/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs;


import MoveControls.Tahu.FireballControl;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.system.lwjgl.LwjglTimer;
import java.sql.Time;
import java.util.List;


/**
 *
 * @author JSC
 */
public class EnemyMoveAttack extends AbstractControl implements AnimEventListener,PhysicsCollisionListener {


    public Spatial motor; //This takes the GameObject of the executer of the code
    public Transform head; //This takes a prefab assigned to the head of the executer of the code


    public float targetDistanceMin = 3.0f; //This sets the max and min view range
    public float targetDistanceMax = 35.0f;
   
    private AssetManager assetManager;


    public Control weaponBehaviours[] = {}; //This takes the location of the weapons the executer of the code has and sets them in an array
    public float fireFrequency = 2f;
    
    private Vector3f targetDirection;


    // Private memeber data
   // private AI ai; //This is the code that launched this code

 
    private Transform character;


    private Transform target;

    private float delayFight = 0;
    private boolean inRange = false;
    private float nextRaycastTime = 0; //This takes the time at which a Raycast wave was fired
    private float lastRaycastSuccessfulTime = 0; //This tallys the amount of times the Raycast wave collided with the target
    private float noticeTime = 0; //This tallys the time it took for the AI to register the collision of the Raycast


    private boolean firing = false;
    private float lastFireTime  = -1f;
    private int nextWeaponToFire = 0;
    
    private boolean closeFiring = false;
    private float nextMelee;
    private float nextReaction;
    private float nextDodge;
    private boolean fighting = false;
    private Vector3f fireballPos;
    private boolean fireballShot = false;
    private Spatial fireballO;
    private Spatial fireball;


    private AIController ai;
    //public var meleeClip : AnimationClip;
    //public var dodgeClip : AnimationClip;
    public float meleeRate  = 1.5f; //This is to be the time it takes to preform the melee attack animation clip
    public float dodgeRate = 1.5f; //This is to be the time it takes to preform the dodge animation clip
    
    public float reationTime  = 2;
    private GhostControl ghost;
    private LwjglTimer time;
    private Node root;
    private CharacterControl container;
    /*function Awake () {
    //This locates the targets position and checks and recieves that inforcation from the previous code
    character = motor.transform;
    target = GameObject.FindWithTag ("target").transform;
    ai = transform.parent.GetComponentInChildren.<AI> ();
    }
    */
    private AnimControl animationControl;
    private AnimChannel animationChannel;
    private Spatial model;
    private GhostControl meleeBox;
    private GhostControl projBox;
    private BulletAppState bas;
    
    
    public EnemyMoveAttack(Spatial m,Node root,AIController AI, BulletAppState bulletAppState){
        this.root = root;
        model=m;
        time = new LwjglTimer();
        ai = AI;
        bas = bulletAppState;
        assetManager = ai.getAssetManager();
        fireballO = assetManager.loadModel("Scenes/Fireball.j3o");
        fireball = fireballO.clone();
    }
    
    SceneGraphVisitorAdapter getFireball = new SceneGraphVisitorAdapter() {

        @Override
        public void visit(Node node) {
            super.visit(node);
            findFireball(node);
        }

        private void findFireball(Node node) {
            if (node.getName().equals("Fireball")) {
                fireballPos = node.getWorldTranslation();
            }
        }
    };
    
    
    SceneGraphVisitor setTarget = new SceneGraphVisitor() {
        public void visit(Spatial spatial) {
            if(spatial.getUserData("tag")=="target"){
                target = spatial.getWorldTransform();
            }
        }
    };
    
    
    @Override
    public void setSpatial(Spatial spatial){
        super.setSpatial(spatial);
        motor = spatial;
        character = motor.getWorldTransform();
        
        meleeBox = new GhostControl(new BoxCollisionShape(new Vector3f(1,4,5)));
        projBox = new GhostControl(new BoxCollisionShape(new Vector3f(1,4,5)));
        Node projBoxPos = new Node("projectile box position");
        Node meleeBoxPos = new Node("melee box position");
        projBoxPos.addControl(projBox);
        meleeBoxPos.addControl(meleeBox);
        ((Node)spatial).attachChild(projBoxPos);
        ((Node)spatial).attachChild(meleeBoxPos);
        meleeBoxPos.setLocalTranslation(0f,.6f,9f);
        projBoxPos.setLocalTranslation(0f,.6f,20f);
        bas.getPhysicsSpace().add(meleeBox);
        bas.getPhysicsSpace().add(projBox);
        //ghost = ai.getGhostControl();
        container = spatial.getControl(CharacterControl.class);
        animationControl = model.getControl(AnimControl.class);
        animationControl.addListener(this);
        System.out.println(animationControl.getNumChannels() + " Channels");
        animationChannel = animationControl.getChannel(0);
        //animationChannel.setAnim("Idle");
        model.depthFirstTraversal(getFireball);

        
    }
    
    public void setTarget(Spatial t){
        target =t.getWorldTransform();
    }
       /*function OnEnable () {
    //Sets inRange to false and begins firing Raycasts
    inRange = false;
    nextRaycastTime = Time.time + 1;
    lastRaycastSuccessfulTime = Time.time;
    noticeTime = Time.time;
    }*/
    
    private void OnEnable(){
        //Sets inRange to false and begins firing Raycasts
        inRange = false;
        nextRaycastTime = time.getTimeInSeconds() + 1;
        lastRaycastSuccessfulTime = time.getTimeInSeconds();
        noticeTime = time.getTimeInSeconds();
        
    }
    
    private void OnDisable(){
        Shoot(false);
    }
    
    private void Shoot(boolean state){
        firing = state;
        if (closeFiring == false) {
            firing = state;


        }
    }
    
    
        /*function Fire () {
    //Sends message to weapon locations and tells them to fire
    if (weaponBehaviours[nextWeaponToFire]) {
    weaponBehaviours[nextWeaponToFire].SendMessage ("Fire");
    nextWeaponToFire = (nextWeaponToFire + 1) % weaponBehaviours.Length;
    lastFireTime = Time.time;
    }
    }*/
    
    public void Fire(){
        if(nextWeaponToFire < weaponBehaviours.length){
            //weaponBehaviours[nextWeaponToFire].Fire();
            nextWeaponToFire = (nextWeaponToFire + 1) % weaponBehaviours.length;
            lastFireTime = time.getTimeInSeconds();
        }
    }
    
    
    @Override
    protected void controlUpdate(float tpf) {
        //System.out.println("oh my god");
        // Calculate the direction from the target to the executer of the code
        if(!"Walk".equals(animationChannel.getAnimationName()) && !fighting){
                animationChannel.setAnim("Walk",.3f);
        }
       targetDirection = new Vector3f(0,0,0);
       target.getTranslation().subtract(character.getTranslation(),targetDirection);
       targetDirection.y = 0;
       targetDirection.z = 0;
       Quaternion previous = motor.getLocalRotation();
       float targetDist = targetDirection.length();
       targetDirection.divide(targetDist);
       
       //Set the executer of the code to face the target...
       //that is, to face the direction from this character to the target
      
       container.setViewDirection(targetDirection);


       
       // For a short moment after noticing target…
       // only look at the target but don't walk towards or attack yet.
       if (time.getTimeInSeconds() < noticeTime + 1.5) {
           //motor.movementDirection = Vector3f.ZERO;
           return;
       }
       if (inRange && targetDist > targetDistanceMax){
           inRange = false;
            
       } else if (!inRange && targetDist < targetDistanceMax){
           inRange = true;
       }
       //Begins to walk towards target if inRange
       if (!inRange || fighting){
           
           container.setWalkDirection(Vector3f.ZERO);
           if(!inRange) {
               ai.OnExitInterestArea();
           }
          // motor.getLocalRotation().lookAt(targetDirection,Vector3f.UNIT_Y);
       } else {
           
           container.setWalkDirection(targetDirection.mult(.01f));
       }
       
       if (time.getTimeInSeconds() > nextRaycastTime) {
           nextRaycastTime = time.getTimeInSeconds() + 1;
           if (ai.CanSeeTarget() && inRange) {
               lastRaycastSuccessfulTime = time.getTimeInSeconds();
               if(time.getTimeInSeconds() > nextReaction){
                   nextReaction = time.getTimeInSeconds() + reationTime;
                   Shoot (true);
               } else {
                   Shoot (false);
               }
           } else {
               Shoot(false);
               ai.OnExitInterestArea();               
           }
        }
       if (firing) {
           //Determines when to fire weapons on set frequency
           if (time.getTimeInSeconds() > lastFireTime + 1 / fireFrequency) {
               Fire ();
           }
       }
       
       projectile(tpf);
       melee(tpf);
       
       checkMoveActions();
    }
    
    private void melee(float tpf){
        if(animationChannel.getAnimationName().equals("Walk") || animationChannel.getAnimationName().equals("Idle") || fighting) {
            List objects = meleeBox.getOverlappingObjects();
            System.out.println(objects);
            for(int i = 0; i < objects.size(); i++){
                if(objects.get(i).getClass().equals(CharacterControl.class)){
                    if(Math.random()<.05 ) {//|| fighting){
                        /*if(!fighting){
                            float x = (int)(Math.random()*10);
                            delayFight = x + System.currentTimeMillis()/1000;
                        }
                        System.out.println(delayFight + " delayed " + System.currentTimeMillis()/1000 + " time");
                        if(System.currentTimeMillis() > delayFight){*/
                        //    delayFight = 0;
                        if(!fighting){
                            animationChannel.setAnim("First A", 0.3f);
                        }
                        //}
                        fighting = true;
                    } 
                }
            }
        }
    }
    
    private void projectile(float tpf){
        if(animationChannel.getAnimationName().equals("Walk") || animationChannel.getAnimationName().equals("Idle") || fighting) {
            List objects = projBox.getOverlappingObjects();
            System.out.println(objects);
            for(int i = 0; i < objects.size(); i++){
                if(objects.get(i).getClass().equals(CharacterControl.class)){
                    if(Math.random()<.2 && !fireballShot) {//|| fighting){
                        /*if(!fighting){
                            float x = (int)(Math.random()*10);
                            delayFight = x + System.currentTimeMillis()/1000;
                        }
                        System.out.println(delayFight + " delayed " + System.currentTimeMillis()/1000 + " time");
                        if(System.currentTimeMillis() > delayFight){*/
                        //    delayFight = 0;
                        if(!fighting){
                            animationChannel.setAnim("Neutral B", .3f);
                        }
                        //}
                        fighting = true;
                    }
                }
            }
        }
    }
    
    private void checkMoveActions(){
        if( "Neutral B".equals(animationChannel.getAnimationName()) &&
            animationChannel.getTime() > 0.6f && animationChannel.getTime() < 0.7f && !fireballShot){
            fireball = fireballO.clone();
            fireball.setLocalTranslation(fireballPos);
            FireballControl fireballControl = new FireballControl(10,targetDirection.x>0);
            fireball.addControl(fireballControl);
            root.attachChild(fireball);
            fireballShot = true;
            System.out.println("FIRED A SPEEDING BULLET");
        }
        
        System.out.println(fireballShot + " fireball");

    }
    
    private boolean chanceDodge(){
        int chance = 1+ ((int)Math.random() * 5);
        if (chance == 1) {
            return true;
        }
        return false;
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }


    public void collision(PhysicsCollisionEvent event) {
        if (event.getNodeA().getUserData("tag").equals("target") && time.getTimeInSeconds() > nextMelee) {
            nextMelee = time.getTimeInSeconds() + meleeRate;
            closeFiring = true;
            //animation.Play(meleeClip.name);
        //Apply damage to player here
        } else if (event.getNodeB().getUserData("tag").equals("target") && time.getTimeInSeconds() > nextMelee) {
            nextMelee = time.getTimeInSeconds() + meleeRate;
            closeFiring = true;
            //animation.Play(meleeClip.name);
        //Apply damage to player here
        }
        
        if (event.getNodeA().getUserData("tag").equals("projectile") && chanceDodge() == true && time.getTimeInSeconds() > nextDodge) {
            nextDodge = time.getTimeInSeconds() + dodgeRate;
            closeFiring = false;
            //animation.Play(dodgeClip.name);
        } else if (event.getNodeB().getUserData("tag").equals("projectile") && chanceDodge() == true && time.getTimeInSeconds() > nextDodge) {
            nextDodge = time.getTimeInSeconds() + dodgeRate;
            closeFiring = false;
            //animation.Play(dodgeClip.name);
        }
        
        if (event.getNodeA().getUserData("tag").equals("projectile") && chanceDodge() == false) {


            closeFiring = false;
            //Apply damage to Aganonce Chronicler
        }
        
      
    }


    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if(!animName.equals("Walk") || !animName.equals("Idle")){
            fighting = false;
            fireballShot = false;
        }
        if(animName.equals("Neutral B")){
            fireballShot = false;
        }
        //throw new UnsupportedOperationException("Not supported yet.");
    }


    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        if (animName.equals("Neutral B")){
            fireballShot = false;
            System.out.println(channel.getAnimMaxTime() + " MAX TIME");
        }
    }

    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}

