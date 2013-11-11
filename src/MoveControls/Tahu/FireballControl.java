/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MoveControls.Tahu;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.system.lwjgl.LwjglTimer;

/**
 *
 * @author JSC
 */
public class FireballControl extends AbstractControl{
    private float speed = 1;
    private float direction = 1;
    private LwjglTimer startTime = new LwjglTimer();

    public FireballControl(){
        speed = 1;
    }
    public FireballControl(float sp,boolean dir){
        speed = sp;
        if(dir){
            direction = 1;
        } else {
            direction = -1;
        }
        startTime.reset();
    }

    
    @Override
    protected void controlUpdate(float tpf) {
        //throw new UnsupportedOperationException("Not supported yet.");
        spatial.move(tpf*speed*direction, 0, 0);
        shouldDisappear();
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    @Override
    public Control cloneForSpatial(Spatial spatial){
        Control control = new FireballControl();
        spatial.addControl(control);
        return control;
    }

    /**
     * @return the speed
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    private void shouldDisappear(){
        
        if (startTime.getTimeInSeconds() > 4f){
            spatial.getParent().detachChild(spatial);
        }
        
    }
}
