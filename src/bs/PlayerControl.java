/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
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

public class PlayerControl extends AbstractControl implements ActionListener, AnalogListener, AnimEventListener {

    private InputManager inputManager;
    private boolean left = false, right = false;
    private Vector3f walkDirection = new Vector3f(0, 0, 0);
    private CharacterControl character;
    private int health;
    private int stock = 3;
    private Spatial model;
    private Camera cam;
    private AnimChannel animationChannel;
    private AnimControl animationControl;
    
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
    

    /* PlayerControl will manage input and collision logic */
    PlayerControl(Spatial s,InputManager input, CharacterControl cc, Camera cm) {
        model = s;
        character = cc;
        inputManager = input;
        initKeys();
        health = 0;
        cam = cm;
        
        animationControl = model.getControl(AnimControl.class);
        animationControl.addListener(this);
        animationChannel = animationControl.createChannel();
        animationChannel.setAnim("Idle");
        
    }

    @Override
    protected void controlUpdate(float tpf) {
        Vector3f camDir = cam.getDirection().clone().multLocal(0.25f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.25f);
        camDir.y = 0;
        camLeft.y = 0;
        walkDirection.set(0, 0, 0);
        time += tpf;
        groundAExec.updateExpiration(time);
        groundAAExec.updateExpiration(time);
        groundAAAExec.updateExpiration(time);
        upTiltExec.updateExpiration(time);
        downTiltExec.updateExpiration(time);


        if (!character.onGround()) {
            airTime = airTime + tpf;
        } else {
            airTime = 0;
        }
        
        if(isFighting()){
            FightingState(tpf,camLeft);
        } else if (isActing()) {
            ActingState(camLeft);
        } else if(isIdling()){
            IdleState();
        }
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
            else if (groundAExec.updateState(pressedMappings, time) && character.onGround()) {
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
            jumping = true;
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

    public void respawn(Spatial event, Node respawn, BulletAppState bas){
        //if players lives are greater than 1
        if(stock > 0){
            if(!"Idle".equals(animationChannel.getAnimationName())){
                animationChannel.setAnim("Idle", .2f);
            }
            left = false;
            right = false;
            stock--;
            respawn.attachChild(event);
            character.setPhysicsLocation(respawn.getWorldTranslation());
        } else {
            event.removeFromParent();
            bas.getPhysicsSpace().remove(event);
        }
        
    }
    
    public void onAnalog(String name, float value, float tpf) {
        // System.out.println(name + " = " + value);
    }
    
    
    
    public void IdleState(){
        
        if(!"Idle".equals(animationChannel.getAnimationName())){
            animationChannel.setAnim("Idle", .2f);
        }
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
                animationChannel.setAnim("Jump",.3f);
                //animationChannel.setTime(.3f);
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
    }
    
    public boolean isActing(){
        if((left || right || jumping || ducking) && !isFighting()){
            return true;
        }
        return false;
    }
    
    public void FightingState(float tpf, Vector3f camLeft){
        System.out.println(time + " time");
        
        
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
            } else {
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

        inputManager.addMapping("Right", new KeyTrigger(Main.player1Mappings[2]));
        inputManager.addMapping("Left", new KeyTrigger(Main.player1Mappings[1]));
        inputManager.addMapping("Jump", new KeyTrigger(Main.player1Mappings[0]));
        inputManager.addMapping("Down", new KeyTrigger(Main.player1Mappings[4]));
        inputManager.addMapping("UpAction", new KeyTrigger(Main.player1Mappings[3]));
        inputManager.addMapping("Normal Attack",new KeyTrigger(Main.player1Mappings[5]));
        inputManager.addListener(this, "Right","Left","Jump","Normal Attack", "UpAction", "Down");
        
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
        
        downTilt = new ComboMove("D Tilt");
        downTilt.press("Down","Normal Attack").done();
        downTilt.setUseFinalState(true);
        downTilt.setPriority(0.1f);
        downTiltExec = new ComboMoveExecution(downTilt);
        
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }

    public Control cloneForSpatial(Spatial spatial) {
        return null;
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
