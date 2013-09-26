/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs;


import Characters.PlayableCharacter;
import MoveControls.Tahu.FireballControl;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import mygame.Main;
import Players.Player;
import com.jme3.asset.AssetManager;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.system.lwjgl.LwjglTimer;
import menu.PlayerControlMenu;
import mygame.CompoundInputManager;


public class PlayerControl extends AbstractControl implements ActionListener, AnalogListener, AnimEventListener {

    
    //MOST LIKELY TAHU SPECIFIC
    private InputManager inputManager;
    private CompoundInputManager compoundManager;
    private boolean left = false, right = false;
    private Vector3f walkDirection = new Vector3f(0, 0, 0);
    private CharacterControl character;
    private int health;
    private int stock = 3;
    private Spatial model;
    private Camera cam;
    private AssetManager assetManager;
    private AnimChannel animationChannel;
    private AnimControl animationControl;
    private Vector3f fireballPos;
    private Node root;
    private LwjglTimer startTime = new LwjglTimer();
    private Spatial fireball;
    
    //ComboMoves
    private ComboMove upTilt;
    private ComboMoveExecution upTiltExec;
    private ComboMove groundA;
    private ComboMoveExecution groundAExec;
    private ComboMove groundAA;
    private ComboMoveExecution groundAAExec;
    private ComboMove groundAAA;
    private ComboMoveExecution groundAAAExec;
    private ComboMove downTilt;
    private ComboMoveExecution downTiltExec;
    private ComboMove sideTilt;
    private ComboMoveExecution sideTiltExec;
    private ComboMove neutralB;
    private ComboMoveExecution neutralBExec;
    private ComboMove sideB;
    private ComboMoveExecution sideBExec;
    private ComboMove downB;
    private ComboMoveExecution downBExec;
    private ComboMove upB;
    private ComboMoveExecution upBExec;
    private ComboMove sideDodge;
    private ComboMoveExecution sideDodgeExec;
    private ComboMove dodgeRoll;
    private ComboMoveExecution dodgeRollExec;
    
    
    
    private HashSet<String> pressedMappings = new HashSet<String>();
    private List<ComboMove> invokedMoves = new ArrayList<ComboMove>();
    private ComboMove currentMove = null;
    private float currentMoveCastTime = 0;
    private float time = 0;
    private float airTime = 0;
    private boolean jumping = false;
    private boolean facingLeft = false;
    private boolean facingRight = false;
    private boolean ducking = false;
    private boolean fireballShot = false;
    
    private boolean grabbingLedge = false;
    private float startGravity;
    private float startFallSpeed;
    private float startJumpSpeed;
    private float startMoveSpeed;
    private String number;
    
    private InGameState sourceState;
    private PlayableCharacter playerCharacter;
    
    
    

    /* PlayerControl will manage input and collision logic */
    PlayerControl(Node r,Player p,Spatial s,InputManager input,CompoundInputManager compound, CharacterControl cc, Camera cm, InGameState ss) 
    {
        root = r;
        model = s;
        character = cc;
        compoundManager = compound;
        inputManager = input;
        initKeys();
        health = 0;
        cam = cm;
        playerCharacter = p.currentCharacter;
        sourceState = ss;
        assetManager = ss.getAssetManager();
        number = p.playerNumber;
        startGravity = character.getGravity();
        startFallSpeed = character.getFallSpeed();
        startJumpSpeed = character.getJumpSpeed();
        startMoveSpeed = playerCharacter.getMoveSpeed();
        
        animationControl = model.getControl(AnimControl.class);
        animationControl.addListener(this);
        animationChannel = animationControl.createChannel();
        animationChannel.setAnim("Idle");
        model.depthFirstTraversal(getFireball);
        
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
    
    @Override
    protected void controlUpdate(float tpf) {
        Vector3f camDir = cam.getDirection().clone().multLocal(0.25f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.25f);
        camDir.y = 0;
        camLeft.x = camLeft.x * startMoveSpeed;
        camLeft.y = 0;
        walkDirection.set(0, 0, 0);
        time += tpf;
        groundAExec.updateExpiration(time);
        groundAAExec.updateExpiration(time);
        groundAAAExec.updateExpiration(time);
        upTiltExec.updateExpiration(time);
        downTiltExec.updateExpiration(time);
        sideTiltExec.updateExpiration(time);
        neutralBExec.updateExpiration(time);
        downBExec.updateExpiration(time);
        sideBExec.updateExpiration(time);
        upBExec.updateExpiration(time);
        sideDodgeExec.updateExpiration(time);
        dodgeRollExec.updateExpiration(time);

        if (!character.onGround()) {
            airTime = airTime + tpf;
        } else {
            airTime = 0;
        }
        
        if(isIdling()){
            IdleState();
        } else if(isFighting()){
            FightingState(tpf,camLeft);
        } else if (isActing()) {
            ActingState(camLeft);
        } 
        checkMoveActions();
        character.setWalkDirection(walkDirection); // THIS IS WHERE THE WALKING HAPPENS
        
        
        // ... update more ComboExecs here....

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onAction(String name, boolean pressed, float tpf) {
        //Record pressed mappings
        if(name.equals("Left")){
            if(pressed && currentMove == null){
                facingLeft = true;
                facingRight = false;
                left = true;
            } else{
                left = false;
            }
        }
        
        if(name.equals("Right")){
            if(pressed && currentMove == null){
                facingRight = true;
                facingLeft = false;
                right = true;
            } else {
                right = false;
            }
        }
        if(name.equals("Down")){
            if(pressed && currentMove == null){
                ducking = true;
            } else {
                ducking = false;
            }
                
        }
        if (pressed){
            pressedMappings.add(name);
        }else{
            pressedMappings.remove(name);
        }
        //Update ComboExec objects if state has changed
        if(currentMove == null){
            if (upTiltExec.updateState(pressedMappings, time) ) {
                jumping = false;
                invokedMoves.add(upTilt);
            }
            else if(downTiltExec.updateState(pressedMappings, time)) {
                invokedMoves.add(downTilt);
            }
            else if(sideTiltExec.updateState(pressedMappings, time)) {
                invokedMoves.add(sideTilt);
            }
            else if(downBExec.updateState(pressedMappings, time)) {
                invokedMoves.add(downB);
            }
            else if(upBExec.updateState(pressedMappings, time)) {
                invokedMoves.add(upB);
            }
            else if (sideBExec.updateState(pressedMappings, time)) {
                invokedMoves.add(sideB);
            }
            else if(neutralBExec.updateState(pressedMappings, time)) {
                invokedMoves.add(neutralB);
            }
            else if(sideDodgeExec.updateState(pressedMappings, time)) {
                invokedMoves.add(sideDodge);
            }
            else if(dodgeRollExec.updateState(pressedMappings, time)) {
                invokedMoves.add(dodgeRoll);
            }
            else if(groundAExec.updateState(pressedMappings, time) && character.onGround()) {
                invokedMoves.add(groundA);
            }
           
        }

        if(currentMove != null && currentMove.getMoveName().equals(groundA.getMoveName())){
            if (groundAAExec.updateState(pressedMappings, time)){ 
                invokedMoves.add(groundAA);
            }
        }
        if(currentMove != null && currentMove.getMoveName().equals(groundAA.getMoveName())){
            if (groundAAAExec.updateState(pressedMappings, time)){
                invokedMoves.add(groundAAA);
            }
        }
        // If any ComboMoves have been sucessfully triggered:
        if (invokedMoves.size() > 0){
            // identify the move with highest priorityd
            float priority = 0;
            ComboMove toExec = null;
            for (ComboMove move : invokedMoves){
                if (move.getPriority() > priority){
                    priority = move.getPriority();
                    toExec = move;
                }
            }
            if (currentMove != null && currentMove.getPriority() > toExec.getPriority()){
                invokedMoves.remove(toExec);
                return; // skip lower-priority moves
            }

            // If a ComboMove has been identified, store it in currentMove
            currentMove = toExec;
            currentMoveCastTime = currentMove.getCastTime();
            if(!currentMove.getMoveName().equals(animationChannel.getAnimationName())){
                animationChannel.setAnim(currentMove.getMoveName());
                animationChannel.setLoopMode(LoopMode.DontLoop);
            }
            invokedMoves.remove(currentMove);       
        }

        else if(name.equals("Jump") && pressed && !isFighting()){
            if(grabbingLedge){
                resetGravity();
            } else {
                jumping = true;
            }
        }
        
    }

   
    public int getHealth() {
        return health;
    }

    public void setHealth(int h) {
        health = h;
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
    
    public void resetGravity() {
        character.setGravity(startGravity);
        character.setFallSpeed(startFallSpeed);
        character.setJumpSpeed(startJumpSpeed);
        grabbingLedge = false;
    }
    
    public void grabLedge(Spatial event){
        if(character.getPhysicsLocation().y < event.getWorldTranslation().y && !grabbingLedge){
            
            
            grabbingLedge = true;
            animationChannel.setAnim("Slow Walk");
            character.setGravity(0);
            character.setFallSpeed(0);
            character.setJumpSpeed(0);
        }
    }
    public boolean isGrabbingLedge(){
        return grabbingLedge;
    }

    public void respawn(Spatial event, Node respawn, BulletAppState bas){
        //if players lives are greater than 1
        
        stock--; // Moved this up, as stock would technically be removed before verifying they're still alive
        sourceState.adjustStock(stock, number);
        
        if(stock > 0)
        {
            if(!"Idle".equals(animationChannel.getAnimationName()))
            {
                animationChannel.setAnim("Idle", .2f);
            }
            left = false;
            right = false;
//            stock--;
//            sourceState.adjustStock(stock, number);
            respawn.attachChild(event);
            character.setPhysicsLocation(respawn.getWorldTranslation());
        } 
        else 
        {
            event.removeFromParent();
            bas.getPhysicsSpace().remove(event);
        }
        
    }
    
    public void onAnalog(String name, float value, float tpf) {
        // System.out.println(name + " = " + value);
    }
    
    public int getNumber(){
        return Integer.valueOf((number.charAt(6)))-48;
    }
    
    public void IdleState(){
        //onAnimCycleDone(animationControl,animationChannel,getAnim());
        if("Run".equals(animationChannel.getAnimationName())){
            animationChannel.setAnim("Idle", .2f);
        }
        //fireballShot = false;

    }
    public boolean isIdling(){
        return(character.onGround() && !isActing() && !isFighting());
    }
    
    public void ActingState(Vector3f camLeft){
        if(left || right){
            if(!"Run".equals(animationChannel.getAnimationName()) && character.onGround()){
                animationChannel.setAnim("Run");
            }
            if(left){
                walkDirection.addLocal(camLeft);
                character.setViewDirection(walkDirection);
            } else{
                walkDirection.addLocal(camLeft.negate());
                character.setViewDirection(walkDirection);
            }
        }
        if(jumping){
            if(!"Jump".equals(animationChannel.getAnimationName())){
                animationChannel.setSpeed(2f);
                animationChannel.setAnim("Jump",.5f);
                animationChannel.setTime(.8f);
                animationChannel.setLoopMode(LoopMode.DontLoop);
            }
            character.jump();
            jumping = false;
        }
        if(ducking){
            if(!"Duck".equals(animationChannel.getAnimationName())){
                animationChannel.setAnim("Duck",.3f);
                animationChannel.setLoopMode(LoopMode.DontLoop);
            }
        }
        if(grabbingLedge){
            if(!"Slow Walk".equals(animationChannel.getAnimationName())){
                animationChannel.setAnim("Slow Walk",.3f);
                animationChannel.setLoopMode(LoopMode.Loop);
            }
        }
    }
    
    public boolean isActing(){
        if((left || right || jumping || ducking || grabbingLedge) && !isFighting()){
            return true;
        }
        return false;
    }
    
    public void FightingState(float tpf, Vector3f camLeft){
        
        
        if (currentMove != null){
            time ++;
            //character.setKinematic(true);
            currentMoveCastTime -= tpf;
            
            if(currentMove.getMoveName().equals("First A") && time < 50){
                if(facingLeft){
                    walkDirection.addLocal(camLeft.multLocal(0.2f));
                } else{
                    walkDirection.addLocal(camLeft.negate().multLocal(0.2f));
                }
                //character.setPhysicsLocation(newPos);
            } else if("Second A".equals(currentMove.getMoveName()) && time < 150){
                if(facingLeft){
                    walkDirection.addLocal(camLeft.multLocal(0.2f));
                } else {
                    walkDirection.addLocal(camLeft.negate().multLocal(0.2f));
                }
            } else if("Third A".equals(currentMove.getMoveName()) && time < 300){
                if(facingLeft){
                    walkDirection.addLocal(camLeft.multLocal(0.2f));
                } else {
                    walkDirection.addLocal(camLeft.negate().multLocal(0.2f));
                }
            } else if("Up B".equals(currentMove.getMoveName())){
                walkDirection = new Vector3f(0,1.2f,0);
            } else if("Neutral B".equals(currentMove.getMoveName()) && !fireballShot){
                if(startTime.getTimeInSeconds() >= .5){
                    fireball = assetManager.loadModel("Scenes/Fireball.j3o");
                    fireball.setLocalTranslation(fireballPos);
                    FireballControl fireballControl = new FireballControl(10,facingRight);
                    fireball.addControl(fireballControl);
                    root.attachChild(fireball);
                    fireballShot = true;
                }
                
            }else {
                walkDirection = new Vector3f(0,0,0);
            }
            
            if (currentMoveCastTime <= 0){
                System.out.println("THIS COMBO WAS TRIGGERED: " + currentMove.getMoveName());
                time = 0;
                currentMoveCastTime = 0;
                currentMove = null;
            }
            
        }
    }
    
    
    public boolean isFighting(){
        if(currentMove != null){
            return true;
        }
        return false;
    }
    
    

    private void initKeys() {

        compoundManager.addMapping("Right", PlayerControlMenu.player1Scheme.getRight());
        compoundManager.addMapping("Left", PlayerControlMenu.player1Scheme.getLeft());
        compoundManager.addMapping("UpAction", PlayerControlMenu.player1Scheme.getUp());
        compoundManager.addMapping("Down", PlayerControlMenu.player1Scheme.getDown());
        compoundManager.addMapping("Jump", PlayerControlMenu.player1Scheme.getJump());
        compoundManager.addMapping("Normal Attack", PlayerControlMenu.player1Scheme.getAttack());
        compoundManager.addMapping("Special", PlayerControlMenu.player1Scheme.getSpecial());
        compoundManager.addMapping("Dodge", PlayerControlMenu.player1Scheme.getDodge());
        compoundManager.addMapping("RightLeftAction", PlayerControlMenu.player1Scheme.getLeftRight());
//        inputManager.addMapping("Right", new KeyTrigger(Main.player1Mappings[2]));
//        inputManager.addMapping("Left", new KeyTrigger(Main.player1Mappings[1]));
//        inputManager.addMapping("Jump", new KeyTrigger(Main.player1Mappings[0]));
//        inputManager.addMapping("Down", new KeyTrigger(Main.player1Mappings[4]));
//        inputManager.addMapping("UpAction", new KeyTrigger(Main.player1Mappings[3]));
//        inputManager.addMapping("RightLeftAction", new KeyTrigger(Main.player1Mappings[6]));
//        inputManager.addMapping("Special", new KeyTrigger(Main.player1Mappings[7]));
//        inputManager.addMapping("Normal Attack",new KeyTrigger(Main.player1Mappings[5]));
//        inputManager.addMapping("Dodge",new KeyTrigger(Main.player1Mappings[8]));
        compoundManager.addListener(this, "Right","Left","Jump","Normal Attack", "UpAction", "Down","RightLeftAction","Special","Dodge");
        
        /*if ((Main.joysticks.length != 0) && (Main.joysticks[0].getName().equals("Controller (XBOX 360 For Windows)")))
        {
            Main.joysticks[0].getXAxis().assignAxis("Right", "Left");
            Main.joysticks[0].getXAxis().assignAxis("RightLeftAction", "RightLeftAction");
            Main.joysticks[0].getYAxis().assignAxis("Down", "UpAction");
            Main.joysticks[0].getButton("0").assignButton("Jump");
            Main.joysticks[0].getButton("2").assignButton("Normal Attack");
            Main.joysticks[0].getButton("3").assignButton("Special");
        }
        
        if ((Main.joysticks.length != 0) && (Main.joysticks[0].getName().equals("Logitech Dual Action")))
        {
            Main.joysticks[0].getXAxis().assignAxis("Right", "Left");
            Main.joysticks[0].getXAxis().assignAxis("RightLeftAction", "RightLeftAction");
            Main.joysticks[0].getYAxis().assignAxis("Down", "UpAction");
            Main.joysticks[0].getButton("2").assignButton("Jump");
            Main.joysticks[0].getButton("1").assignButton("Normal Attack");
            Main.joysticks[0].getButton("4").assignButton("Special");
        }*/
        
        
        groundA = new ComboMove("First A");
        groundA.press("Normal Attack").done();   
        groundA.setUseFinalState(false); 
        groundA.setPriority(0.1f);
        groundAExec = new ComboMoveExecution(groundA);
        
        groundAA = new ComboMove("Second A");
        groundAA.press("Normal Attack").done();
        groundAA.setUseFinalState(false); 
        groundAA.setPriority(0.2f);
        groundAAExec = new ComboMoveExecution(groundAA);
        
        groundAAA = new ComboMove("Third A");
        groundAAA.press("Normal Attack").done();
        groundAA.setUseFinalState(true);
        groundAAA.setPriority(0.3f);
        groundAAAExec = new ComboMoveExecution(groundAAA);
        
        upTilt = new ComboMove("Up Tilt");
        upTilt.press("UpAction","Normal Attack").done();
        upTilt.setUseFinalState(true);
        upTilt.setPriority(0.1f);
        upTiltExec = new ComboMoveExecution(upTilt);
        
        downTilt = new ComboMove("DTilt");
        downTilt.press("Down","Normal Attack").done();
        downTilt.setUseFinalState(true);
        downTilt.setPriority(0.1f);
        downTiltExec = new ComboMoveExecution(downTilt);
        
        sideTilt = new ComboMove("F Tilt");
        sideTilt.press("RightLeftAction","Normal Attack").done();
        sideTilt.setUseFinalState(true);
        sideTilt.setPriority(0.1f);
        sideTiltExec = new ComboMoveExecution(sideTilt);
        
        neutralB = new ComboMove("Neutral B");
        neutralB.press("Special").done();
        neutralB.setUseFinalState(true);
        neutralB.setPriority(0.1f);
        neutralBExec = new ComboMoveExecution(neutralB);
        
        sideB = new ComboMove("Side B");
        sideB.press("RightLeftAction","Special").done();
        sideB.setUseFinalState(true);
        sideB.setPriority(0.1f);
        sideBExec = new ComboMoveExecution(sideB);
        
        downB = new ComboMove("Down B");
        downB.press("Down","Special").done();
        downB.setUseFinalState(true);
        downB.setPriority(0.1f);
        downBExec = new ComboMoveExecution(downB);
        
        upB = new ComboMove("Up B");
        upB.press("UpAction","Special").done();
        upB.setUseFinalState(true);
        upB.setPriority(0.1f);
        upBExec = new ComboMoveExecution(upB);
        
        sideDodge = new ComboMove("Side Dodge");
        sideDodge.press("Down","Dodge").done();
        sideDodge.setUseFinalState(true);
        sideDodge.setPriority(0.1f);
        sideDodgeExec = new ComboMoveExecution(sideDodge);
        
        dodgeRoll = new ComboMove("Roll Dodge Forward");
        dodgeRoll.press("RightLeftAction","Dodge").done();
        dodgeRoll.setUseFinalState(true);
        dodgeRoll.setPriority(0.1f);
        dodgeRollExec = new ComboMoveExecution(dodgeRoll);
        
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if(isIdling()){
            if(!"Idle".equals(animName)){
                animationChannel.setAnim("Idle", .2f);
            }
        }
        if("Forward Roll".equals(animName)){
            System.out.println("Rolled");
        }
        
    
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        if (animName.equals("Neutral B")){
            startTime.reset();
        }
    }
    
    

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return null;
//        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
    public float getMoveSpeed()
    {
        return startMoveSpeed;
    }
    
    public float getJumpSpeed()
    {
        return startJumpSpeed;
    }
    
    public float getFallSpeed()
    {
        return startFallSpeed;
    }
    
    public float getWeight()
    {
        return startGravity;
    }
    
    
    public void setMoveSpeed(float newSpeed)
    {
        startMoveSpeed = newSpeed;
    }
    
    public void setJumpSpeed(float newSpeed)
    {
        startJumpSpeed = newSpeed;
    }
   
    public void setFallSpeed(float newSpeed)
    {
        startFallSpeed = newSpeed;
    }
    
    public void setWeight(float newWeight)
    {
        startGravity = newWeight;
    }
    
    public String getAnim(){
        return animationChannel.getAnimationName();
    }
    
    private void checkMoveActions(){
        if(startTime.getTimeInSeconds() > 2 && fireballShot){
            root.detachChild(fireball);
            fireballShot = false;
        }
    }
    
    
}

