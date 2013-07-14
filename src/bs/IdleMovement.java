/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.system.lwjgl.LwjglTimer;
import java.sql.Time;

/**
 *
 * @author JSC
 */
public class IdleMovement extends AbstractControl implements AnimEventListener{

    CharacterControl container;
    private AnimChannel animationChannel;
    private AnimControl animationControl;
    private Spatial model;
    
    public IdleMovement(Spatial m){
        model = m;
    }
    @Override
    public void setSpatial(Spatial spatial) {
      super.setSpatial(spatial);
      container = spatial.getControl(CharacterControl.class);
      animationControl = model.getControl(AnimControl.class);
      animationControl.addListener(this);
      animationChannel = animationControl.createChannel();
      //animationChannel.setAnim("Idle");
    }
    @Override
    protected void controlUpdate(float tpf) {
        container.setWalkDirection(Vector3f.ZERO);
        if(!"Idle".equals(animationChannel.getAnimationName())){
                animationChannel.setAnim("Idle",.3f);
        }
        System.out.println("IDLE ON");
        
    }


    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
       // throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
