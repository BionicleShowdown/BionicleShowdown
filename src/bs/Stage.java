/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;
import java.util.logging.*;
/**
 *
 * @author CotA
 */
public class Stage implements PhysicsCollisionListener {
    
    private static final Logger logger = Logger.getLogger(Stage.class.getName());
    
    private Spatial stage;
    private float top;
    private float bot;
    private float left;
    private float right;
    private Node stageNode = new Node("Full Stage Scene");
    private Node p1SpawnNode;
    private Node p2SpawnNode;
    private Node p3SpawnNode;
    private Node p4SpawnNode;
    private Node respawnNode;
    private BulletAppState bulletAppState;
    private Node rightLedgeNode = new Node("Right ledge node");
    private Node leftLedgeNode = new Node("Left ledge node");
    private boolean leftLedgeGrabbed = false;
    private boolean rightLedgeGrabbed= false;
    private GhostControl boundingBoxBot;
    private GhostControl boundingBoxTop;
    private GhostControl boundingBoxRight;
    private GhostControl boundingBoxLeft;
    private int ledgeCount = 0;
    
    
    
    
    /*Constructor accepts a spatial (yet to be implemented) for the stage
     * and the height and width of the stage from
     * the CENTER to setup the blast barriers, as well as the BulletAppState
     * from the AppState. Then, blast barriers
     * are applied to the stage
     */
    /* Overloaded constructor accepts spatial for the stage (yet to
     * be implemented), then distances from center for each blast
     * barrier, starting from top and moving clockwise (top, right,
     * bottom, left) as well as the BulletAppState from the AppState.
     * Specifying 0 for a certain side will make it not appear
     * The height and the width here become the entire stage from
     * top to bottom and left to right so that the blast barriers
     * can cover the entire stage accordingly.
     * Then, the blast barriers are applied to the stage     * 
     */    
    public Stage(Spatial s, BulletAppState bas){
        bulletAppState = bas;
        stage = s;
        stage.breadthFirstTraversal(getDimensions);
        setupStage();
        stage.breadthFirstTraversal(assignControls);
        stage.depthFirstTraversal(translateBounds);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }
    
    /* Getter for the Node containing
     * all the blast barriers
     */
    public Node getStageNode(){
        return(stageNode);
    }
    
    public Node getp1Spawn(){
        return(p1SpawnNode);
    }
    public Node getp2Spawn(){
        return(p2SpawnNode);
    }
    public Node getp3Spawn(){
        return(p3SpawnNode);
    }
    public Node getp4Spawn(){
        return(p4SpawnNode);
    }
    
    /* Sets up spatial for stage, spawn
     * nodes for the players, and all
     * the blast barriers provided in 
     * the constructor
     */
    private void setupStage(){    
        stageNode.attachChild(stage);
    }
    
    /* Detects only stage necessary collisions
     * which are of the ledges and the blast
     * barriers.
     */
    public void collision(PhysicsCollisionEvent event) {
        for(int i = 0; i < ledgeCount; i++){
            if(event.getNodeA().getName().equals("ledgeNode") && (Integer)event.getNodeA().getUserData("Number") == i){
                if((Boolean)event.getNodeA().getUserData("ledgeGrabbed") == false){
                    logger.log(Level.WARNING,"Ledge {0} hit", new Object[]{i});
                    event.getNodeA().setUserData("ledgeGrabbed", true);
                    
                }
            }
            else if(event.getNodeB().getName().equals("ledgeNode") && (Integer)event.getNodeB().getUserData("Number") == i){
                if((Boolean)event.getNodeB().getUserData("ledgeGrabbed") == false){
                    logger.log(Level.WARNING,"Ledge {0} hit", new Object[]{i});
                    event.getNodeB().setUserData("ledgeGrabbed", true);
                }
            }
            else{
                for(int j = 0; j < ledgeCount; j++){
                    if(((Integer)((Node)stage).getChild("ledgeNode").getUserData("Number")==i) && ((Boolean)((Node)stage).getChild("ledgeNode").getUserData("ledgeGrabbed")==false)){
                        ((Node)stage).getChild("ledgeNode").setUserData("ledgeGrabbed",true);
                        logger.log(Level.WARNING,"Set to true");
                        break;
                    }
                }
            }
                
        }
        if(event.getNodeA().getName().equals("bottomBoundingBoxNode")){
            if(!event.getNodeB().getName().equals("rightBoundingBoxNode") && !event.getNodeB().getName().equals("leftBoundingBoxNode")){
                logger.log(Level.WARNING,"Item Destroyed");
                event.getNodeB().removeFromParent();
                /*Call player life loss+respawn. Probably one function defined in player */
            }
        }
        else if(event.getNodeB().getName().equals("bottomBoundingBoxNode")){
            if(!event.getNodeA().getName().equals("rightBoundingBoxNode") && !event.getNodeA().getName().equals("leftBoundingBoxNode")){
                logger.log(Level.WARNING,"Item Destroyed");
                event.getNodeA().removeFromParent();
                /*Call player life loss+respawn. Probably one function defined in player */
            }
        }
        if(event.getNodeA().getName().equals("topBoundingBoxNode")){
            if(!event.getNodeB().getName().equals("rightBoundingBoxNode") && !event.getNodeB().getName().equals("leftBoundingBoxNode")){
                logger.log(Level.WARNING,"Item Destroyed");
                event.getNodeB().removeFromParent();
                /*Call player life loss+respawn. Probably one function defined in player */
            }
        }
        else if(event.getNodeB().getName().equals("topBoundingBoxNode")){
            if(!event.getNodeA().getName().equals("rightBoundingBoxNode") && !event.getNodeA().getName().equals("leftBoundingBoxNode")){
                logger.log(Level.WARNING,"Item Destroyed");
                event.getNodeA().removeFromParent();
                /*Call player life loss+respawn. Probably one function defined in player */
            }
        }
        if(event.getNodeA().getName().equals("rightBoundingBoxNode")){
            if(!event.getNodeB().getName().equals("topBoundingBoxNode") && !event.getNodeB().getName().equals("bottomBoundingBoxNode")){
                logger.log(Level.WARNING,"Item Destroyed");
                event.getNodeB().removeFromParent();
                /*Call player life loss+respawn. Probably one function defined in player */
            }
        }
        else if(event.getNodeB().getName().equals("rightBoundingBoxNode")){
            if(!event.getNodeA().getName().equals("topBoundingBoxNode") && !event.getNodeA().getName().equals("bottomBoundingBoxNode")){
                logger.log(Level.WARNING,"Item Destroyed");
                event.getNodeA().removeFromParent();
                /*Call player life loss+respawn. Probably one function defined in player */
            }
        }
        if(event.getNodeA().getName().equals("rightBoundingBoxNode")){
            if(!event.getNodeB().getName().equals("topBoundingBoxNode") && !event.getNodeB().getName().equals("bottomBoundingBoxNode")){
                logger.log(Level.WARNING,"Item Destroyed");
                event.getNodeB().removeFromParent();
                /*Call player life loss+respawn. Probably one function defined in player */
            }
        }
        else if(event.getNodeB().getName().equals("rightBoundingBoxNode")){
            if(!event.getNodeA().getName().equals("topBoundingBoxNode") && !event.getNodeA().getName().equals("bottomBoundingBoxNode")){
                logger.log(Level.WARNING,"Item Destroyed");
                event.getNodeA().removeFromParent();
                /*Call player life loss+respawn. Probably one function defined in player */
            }
        }
        
        
    }
    
    /*Gets dimensions of stage as well as simple details that don't 
     * require said dimensions
     */
    SceneGraphVisitorAdapter getDimensions = new SceneGraphVisitorAdapter() {
        
        
        @Override
        public void visit(Geometry geom){
            super.visit(geom);
            
        }
        
        @Override
        public void visit(Node node){
            super.visit(node);
            findRespawn(node);
            findDimensions(node);
            checkSpawnPoints(node);
            countLedges(node);
        }
        
        private void findRespawn(Spatial spatial){
            if(spatial.getName().equals("respawnNode")){
              respawnNode = (Node)spatial;
          }
        }
        private void findDimensions(Spatial spatial){
          if(spatial.getName().equals("bottomBoundingBoxNode")){
              bot = -(((Node)spatial).getLocalTranslation().getY());
          }
          if(spatial.getName().equals("topBoundingBoxNode")){
              top = ((Node)spatial).getLocalTranslation().getY();
          }
          if(spatial.getName().equals("rightBoundingBoxNode")){
              right = ((Node)spatial).getLocalTranslation().getX();
              System.out.println("Right is " + right);
          }
          if(spatial.getName().equals("leftBoundingBoxNode")){
              left = -(((Node)spatial).getLocalTranslation().getX());
          }
        }
              
        private void checkSpawnPoints(Spatial spatial){
            if(spatial.getName().equals("p1SpawnNode")){
                p1SpawnNode = (Node)spatial;
            }
            if(spatial.getName().equals("p2SpawnNode")){
                p2SpawnNode = (Node)spatial;
            }
            if(spatial.getName().equals("p3SpawnNode")){
                p3SpawnNode = (Node)spatial;
            }
            if(spatial.getName().equals("p4SpawnNode")){
                p4SpawnNode = (Node)spatial;
            }
        }
        
        private void countLedges(Spatial spatial){
            if(spatial.getName().equals("ledgeNode")){
                ledgeCount++;
                spatial.addControl(new GhostControl(new CapsuleCollisionShape(0.5f,2,2)));
                bulletAppState.getPhysicsSpace().add(spatial);
            }
        }
        
    };
    
    
    /*Assigns controls to blast barriers.
     */
    SceneGraphVisitorAdapter assignControls = new SceneGraphVisitorAdapter() {
        
        @Override
        public void visit(Geometry geom){
            super.visit(geom);
            checkForMySpatial(geom);
        }
        
        @Override
        public void visit(Node node){
            super.visit(node);
            checkForMySpatial(node);
        }
        
       
        private void checkForMySpatial(Spatial spatial) {
          if(spatial.getName().equals("bottomBoundingBoxNode")){
            boundingBoxBot = new GhostControl(new BoxCollisionShape(new Vector3f((right+left)/2,1,1)));
            ((Node) spatial).addControl(boundingBoxBot);
            bulletAppState.getPhysicsSpace().add(boundingBoxBot);
          }
          if(spatial.getName().equals("topBoundingBoxNode")){
            boundingBoxTop = new GhostControl(new BoxCollisionShape(new Vector3f((right+left)/2,1,1)));
            ((Node) spatial).addControl(boundingBoxTop);
            bulletAppState.getPhysicsSpace().add(boundingBoxTop);
          }
          if(spatial.getName().equals("rightBoundingBoxNode")){
            boundingBoxRight = new GhostControl(new BoxCollisionShape(new Vector3f(1,(top+bot)/2,1)));
            ((Node) spatial).addControl(boundingBoxRight);
            bulletAppState.getPhysicsSpace().add(boundingBoxRight);
          }
          if(spatial.getName().equals("leftBoundingBoxNode")){
            boundingBoxLeft = new GhostControl(new BoxCollisionShape(new Vector3f(1,(top+bot)/2,1)));
            ((Node) spatial).addControl(boundingBoxLeft);
            bulletAppState.getPhysicsSpace().add(boundingBoxLeft);
          }
            System.out.println("Instance of " + spatial.getName());
        }
    };
    
    
    /*Adjust bounds to differening dimensions 
     * so the stage is uniform in terms of
     * the blast barriers
     */
    SceneGraphVisitorAdapter translateBounds = new SceneGraphVisitorAdapter() {
        
        @Override
        public void visit(Geometry geom){
            super.visit(geom);
            setTranslations(geom);
        }
        
        @Override
        public void visit(Node node){
            super.visit(node);
            setTranslations(node);
        }
        
       
        private void setTranslations(Spatial spatial) {
          if(spatial.getName().equals("bottomBoundingBoxNode")){
            ((Node)spatial).getLocalTranslation().setX((-left+right)/2);
          }
          if(spatial.getName().equals("topBoundingBoxNode")){
            ((Node)spatial).getLocalTranslation().setX((-left+right)/2);
          }
          if(spatial.getName().equals("rightBoundingBoxNode")){
            ((Node)spatial).getLocalTranslation().setY((top+(-bot))/2);
          }
          if(spatial.getName().equals("leftBoundingBoxNode")){
            ((Node)spatial).getLocalTranslation().setY((top+(-bot))/2);
          }
        }
    };
}