/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs;

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
import com.jme3.scene.control.AbstractControl;

public class PlayerControl extends AbstractControl implements ActionListener, AnalogListener {

    private InputManager inputManager;
    private boolean left = false, right = false;
    private Vector3f walkDirection = new Vector3f(0, 0, 0);
    private CharacterControl character;
    private int health;
    private int stock;
    private Camera cam;

    /* PlayerControl will manage input and collision logic */
    PlayerControl(InputManager input, CharacterControl cc, Camera cm) {
        character = cc;
        inputManager = input;
        initKeys();
        health = 0;
        cam = cm;
    }

    @Override
    protected void controlUpdate(float tpf) {
        Vector3f camDir = cam.getDirection().clone().multLocal(0.25f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.25f);
        camDir.y = 0;
        camLeft.y = 0;
        walkDirection.set(0, 0, 0);

        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }

        if (!character.onGround()) {
            // airTime = airTime + tpf;
        } else {
            // airTime = 0;
        }

        if (walkDirection.length() == 0) {
        } else {
            character.setViewDirection(walkDirection);
        }
        character.setWalkDirection(walkDirection); // THIS IS WHERE THE WALKING HAPPENS
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onAction(String name, boolean pressed, float tpf) {
        health++;
        if (name.equals("Right")) {
            if (pressed) {
                right = true;
            } else {
                right = false;
            }
        }
        if (name.equals("Left")) {
            if (pressed) {
                left = true;
            } else {
                left = false;
            }
        }
        if (name.equals("Jump")){
            character.jump();
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

    private void initKeys() {

        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addListener(this, "Right","Left","Jump");
    }
}
