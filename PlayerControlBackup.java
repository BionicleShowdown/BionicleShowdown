/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
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
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
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
    private int stock;
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
    
    private HashSet<String> pressedMappings = new HashSet<String>();
    private List<ComboMove> invokedMoves = new ArrayList<ComboMove>();
    private ComboMove currentMove = null;
    private float currentMoveCastTime = 0;
    private float time = 0;
    private float airTime = 0;
    private boolean moving = false;

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
        
    }

    @Override
    protected void controlUpdate(float tpf) {
        Vector3f camDir = cam.getDirection().clone().multLocal(0.25f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.25f);
        camDir.y = 0;
        camLeft.y = 0;
        walkDirection.set(0, 0, 0);
        time += tpf;
        upTiltExec.updateExpiration(time);
        groundAExec.updateExpiration(time);
        groundAAExec.updateExpiration(time);


        if (!character.onGround()) {
            airTime = airTime + tpf;
        } else {
            airTime = 0;
        }
        if (currentMove != null){
            currentMoveCastTime -= tpf;
            
            if (currentMoveCastTime <= 0){
                System.out.println("THIS COMBO WAS TRIGGERED: " + currentMove.getMoveName());

                currentMoveCastTime = 0;
                currentMove = null;
                moving  = false;
            }
            
        }     
        else if(left){
            walkDirection.addLocal(camLeft);
            character.setViewDirection(walkDirection);
               
            if (!"Run".equals(animationChannel.getAnimationName())) {
                animationChannel.setAnim("Run");
            }
         
        } else if (right){
            walkDirection.addLocal(camLeft.negate());
            character.setViewDirection(walkDirection);
               
            if (!"Run".equals(animationChannel.getAnimationName())) {
                animationChannel.setAnim("Run");
            }
        } else if (walkDirection.length() == 0 && character.onGround()) {
            if (animationChannel.getAnimationName()==null||(animationChannel.getAnimationName()).equals("Run")) {
                animationChannel.setAnim("Idle");
             }
        }
        character.setWalkDirection(walkDirection); // THIS IS WHERE THE WALKING HAPPENS
        
        
        // ... update more ComboExecs here....

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onAction(String name, boolean pressed, float tpf) {
         // Record pressed mappings
        if (pressed){
            pressedMappings.add(name);
        }else{
            pressedMappings.remove(name);
        }

        // The pressed mappings have changed: Update ComboExecution objects
        if (upTiltExec.updateState(pressedMappings, time) && !(animationChannel.getAnimationName()).equals("Up Tilt")){
            invokedMoves.add(upTilt);
        }
        if (groundAExec.updateState(pressedMappings, time) && !(animationChannel.getAnimationName().equals("First A"))){
            invokedMoves.add(groundA);
        }
        /*if (groundAAExec.updateState(pressedMappings, time) && !(animationChannel.getAnimationName()).equals("Second A")){
            invokedMoves.add(groundAA);
        }*/
        
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
                return; // skip lower-priority moves
            }

            // If a ComboMove has been identified, store it in currentMove
            currentMove = toExec;
            currentMoveCastTime = currentMove.getCastTime();
            checkMove(currentMove.getMoveName());
            if (currentMove != null && !(currentMove.getMoveName()).equals(animationChannel.getAnimationName())) {
                animationChannel.setAnim(currentMove.getMoveName());
                invokedMoves.remove(currentMove);
                moving = true;
            }
            
        }
        
        else if (name.equals("Right")) {
            if (pressed) {
                right = true;
            } else {
                right = false;
            }
        }
        else if (name.equals("Left")) {
            if (pressed) {
                left = true;
            } else {
                left = false;
            }
        }
        else if (name.equals("Jump") && currentMove == null){
            character.jump();
        }
        
        
    }
    private int checkCombo(String move)
    {
        if(move.equals("Second A") && time>3f){
            invokedMoves.remove(currentMove);
            currentMove = null;  
            return(-1);
        }
        return(1);
    }
            
    
    
    private void checkMove(String move)
    {
        if (!character.onGround()){
            if(move.equals("Up Tilt")){
               invokedMoves.remove(currentMove);
               currentMove = null;  
            }
            if(move.equals("First A") || move.equals("Second A")){
               invokedMoves.remove(currentMove);
               currentMove = null;  
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

    public void onAnalog(String name, float value, float tpf) {
        // System.out.println(name + " = " + value);
    }
    
    public void IdleState(){
        animationChannel.setAnim("idle");  
    }
    
    public boolean isIdlng(){
        return(character.onGround() && animationChannel.getAnimationName()==null);
    }
    
    

    private void initKeys() {

        inputManager.addMapping("Right", new KeyTrigger(Main.player1Mappings[2]));
        inputManager.addMapping("Left", new KeyTrigger(Main.player1Mappings[1]));
        inputManager.addMapping("Jump", new KeyTrigger(Main.player1Mappings[0]));
        inputManager.addMapping("Normal Attack",new KeyTrigger(Main.player1Mappings[3]));
        inputManager.addListener(this, "Right","Left","Jump","Normal Attack");
        
        upTilt = new ComboMove("Up Tilt");
        upTilt.press("Normal Attack", "Jump").done();
        upTilt.setUseFinalState(true);
        upTilt.setPriority(0.3f);
        upTiltExec = new ComboMoveExecution(upTilt);
        
        groundA = new ComboMove("First A");
        groundA.press("Normal Attack").notPress("Jump").done();   
        groundA.setUseFinalState(true); 
        groundA.setPriority(0.1f);
        groundAExec = new ComboMoveExecution(groundA);
    
        groundAA = new ComboMove("Second A");
        groundAA.press("Normal Attack").done();
        groundAA.press("Normal Attack").done();
        groundAA.setUseFinalState(true);
        groundAA.setPriority(0.2f);
        groundAAExec = new ComboMoveExecution(groundAA);
        
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {

    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        
    }
}

