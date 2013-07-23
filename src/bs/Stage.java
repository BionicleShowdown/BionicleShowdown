/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;
import com.jme3.system.lwjgl.LwjglTimer;
import java.util.ArrayList;
import java.util.logging.*;

/**
 *
 * @author CotA
 */
public class Stage implements PhysicsCollisionListener {

    private static final Logger logger = Logger.getLogger(Stage.class.getName());
    private Spatial stage;
    private Spatial lastCollided;
    private LwjglTimer timer = new LwjglTimer();
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
    private GhostControl boundingBoxBot;
    private GhostControl boundingBoxTop;
    private GhostControl boundingBoxRight;
    private GhostControl boundingBoxLeft;
    private float zExtent;
    //private Spatial;
    private int ledgeCount = 0;
    private ArrayList ledges = new ArrayList();
    
    private StandardMatchState currentInGameState;

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
    public Stage(Spatial s, BulletAppState bas) {
        bulletAppState = bas;
        stage = s;
        stage.breadthFirstTraversal(getDimensions);
        stage.breadthFirstTraversal(assignControls);
        stage.depthFirstTraversal(translateBounds);
        setupStage();
        
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }

    /* Getter for the Node containing
     * all the blast barriers
     */
    public Node getStageNode() {
        return (stageNode);
    }

    public Node getp1Spawn() {
        return (p1SpawnNode);
    }

    public Node getp2Spawn() {
        return (p2SpawnNode);
    }

    public Node getp3Spawn() {
        return (p3SpawnNode);
    }

    public Node getp4Spawn() {
        return (p4SpawnNode);
    }

    public Node getRespawnNode() {
        return (respawnNode);
    }
    
    public void setInGameState(StandardMatchState state)
    {
        currentInGameState = state;
    }

    /* Sets up spatial for stage, spawn
     * nodes for the players, and all
     * the blast barriers provided in 
     * the constructor
     */
    private void setupStage() {
        stageNode.attachChild(stage);
        DirectionalLight dir = new DirectionalLight();
        dir.setDirection(new Vector3f(0, 3, 0));
        stageNode.addLight(dir);
    }

    /* Detects only stage necessary collisions
     * which are of the ledges and the blast
     * barriers.
     */
    public void collision(PhysicsCollisionEvent event) {
        
        int currentPlayer = 1;
        
        if (event.getNodeA().getName() == null || event.getNodeB().getName() == null){ 
            return;
        } else {
            for (int i = 0; i < ledges.size(); i++) {
                if (event.getNodeA().getName().equals("ledgeNode") && (Integer) event.getNodeA().getUserData("Number") == i) {
                    if ((Boolean) event.getNodeA().getUserData("ledgeGrabbed") == false && !event.getNodeB().getName().equals("platform") ) {
                        logger.log(Level.WARNING, "Ledge {0} hit", new Object[]{i});
                        if(event.getNodeB().getControl(PlayerControl.class).isGrabbingLedge()){
                            event.getNodeA().setUserData("ledgeGrabbed", true);
                        } else {
                            event.getNodeB().getControl(PlayerControl.class).resetGravity();
                        }
                        event.getNodeB().getControl(PlayerControl.class).grabLedge((Spatial)event.getNodeA());
                        event.getNodeA().setUserData("playerGrabbing",event.getNodeB().getControl(PlayerControl.class).getNumber());

                        System.out.println("PlayerControl " + event.getNodeB().getControl(PlayerControl.class));

                        if(event.getNodeB().getControl(PlayerControl.class).isGrabbingLedge()){
                            event.getNodeA().setUserData("ledgeGrabbed", true);
                        }
                    } else if ((Boolean) event.getNodeA().getUserData("ledgeGrabbed") == true){
                        System.out.println("Node A: " + event.getNodeB().getControl(PlayerControl.class).isGrabbingLedge());
                        if(!event.getNodeB().getControl(PlayerControl.class).isGrabbingLedge()){
                            event.getNodeA().setUserData("ledgeGrabbed", false);
                        }
                    }
                } else if (event.getNodeB().getName().equals("ledgeNode") && (Integer) event.getNodeB().getUserData("Number") == i) {
                    if ((Boolean) event.getNodeB().getUserData("ledgeGrabbed") == false && !event.getNodeA().getName().equals("platform")) {
                        logger.log(Level.WARNING, "Ledge {0} hit", new Object[]{i});
                        if(event.getNodeA().getControl(PlayerControl.class).isGrabbingLedge()){
                            event.getNodeB().setUserData("ledgeGrabbed", true);
                        } else {
                            event.getNodeA().getControl(PlayerControl.class).resetGravity();
                        }
                        event.getNodeA().getControl(PlayerControl.class).grabLedge((Spatial)event.getNodeB());
                        event.getNodeB().setUserData("playerGrabbing",event.getNodeA().getControl(PlayerControl.class).getNumber());
                        System.out.println("PlayerControl " + event.getNodeA().getControl(PlayerControl.class));


                    }else if ((Boolean) event.getNodeB().getUserData("ledgeGrabbed") == true){
                        System.out.println("Node B: " + event.getNodeA().getControl(PlayerControl.class).isGrabbingLedge());
                        if(!event.getNodeA().getControl(PlayerControl.class).isGrabbingLedge()){                      
                            event.getNodeB().setUserData("ledgeGrabbed", false);
                        }
                    }
                }

            }
            if (event.getNodeA().getName().equals("bottomBoundingBoxNode")) {
                if (!event.getNodeB().getName().equals("rightBoundingBoxNode") && !event.getNodeB().getName().equals("leftBoundingBoxNode")) {
                    logger.log(Level.WARNING, "Item Destroyed");
                    if(timer.getTime() > 10 || event.getNodeB() != lastCollided){
                        lastCollided = event.getNodeB();
                        timer.reset();
                        event.getNodeB().getControl(PlayerControl.class).respawn(event.getNodeB(),respawnNode,bulletAppState);     
                        logger.log(Level.WARNING, "stock {0} ", new Object[]{event.getNodeB().getControl(PlayerControl.class).getStock()});
                        
                    }
                }
            } else if (event.getNodeB().getName().equals("bottomBoundingBoxNode")) {
                if (!event.getNodeA().getName().equals("rightBoundingBoxNode") && !event.getNodeA().getName().equals("leftBoundingBoxNode")) {
                    logger.log(Level.WARNING, "Item Destroyed");
                    if(timer.getTime() > 10 || event.getNodeA() != lastCollided){
                        lastCollided = event.getNodeA();
                        timer.reset();
                        event.getNodeA().getControl(PlayerControl.class).respawn(event.getNodeA(),respawnNode,bulletAppState);     
                        logger.log(Level.WARNING, "stock {0} ", new Object[]{event.getNodeA().getControl(PlayerControl.class).getStock()});
                        
                    }
                }
            }
            if (event.getNodeA().getName().equals("topBoundingBoxNode")) {
                if (!event.getNodeB().getName().equals("rightBoundingBoxNode") && !event.getNodeB().getName().equals("leftBoundingBoxNode")) {
                    logger.log(Level.WARNING, "Item Destroyed");
                    if(timer.getTime() > 10 || event.getNodeB() != lastCollided){
                        lastCollided = event.getNodeB();
                        timer.reset();
                        event.getNodeB().getControl(PlayerControl.class).respawn(event.getNodeB(),respawnNode,bulletAppState);     
                        logger.log(Level.WARNING, "stock {0} ", new Object[]{event.getNodeB().getControl(PlayerControl.class).getStock()});
                        
                    }
                }
            } else if (event.getNodeB().getName().equals("topBoundingBoxNode")) {
                if (!event.getNodeA().getName().equals("rightBoundingBoxNode") && !event.getNodeA().getName().equals("leftBoundingBoxNode")) {
                    logger.log(Level.WARNING, "Item Destroyed");
                    if(timer.getTime() > 10 || event.getNodeA() != lastCollided){
                        lastCollided = event.getNodeA();
                        timer.reset();
                        event.getNodeA().getControl(PlayerControl.class).respawn(event.getNodeA(),respawnNode,bulletAppState);     
                        logger.log(Level.WARNING, "stock {0} ", new Object[]{event.getNodeA().getControl(PlayerControl.class).getStock()});
                        
                    }
                }
            }
            if (event.getNodeA().getName().equals("rightBoundingBoxNode")) {
                if (!event.getNodeB().getName().equals("topBoundingBoxNode") && !event.getNodeB().getName().equals("bottomBoundingBoxNode")) {
                    logger.log(Level.WARNING, "Item Destroyed");
                    if(timer.getTime() > 10 || event.getNodeB() != lastCollided){
                        lastCollided = event.getNodeB();
                        timer.reset();
                        event.getNodeB().getControl(PlayerControl.class).respawn(event.getNodeB(),respawnNode,bulletAppState);     
                        logger.log(Level.WARNING, "stock {0} ", new Object[]{event.getNodeB().getControl(PlayerControl.class).getStock()});
                        
                    }
                }
            } else if (event.getNodeB().getName().equals("rightBoundingBoxNode")) {
                if (!event.getNodeA().getName().equals("topBoundingBoxNode") && !event.getNodeA().getName().equals("bottomBoundingBoxNode")) {
                    logger.log(Level.WARNING, "Item Destroyed");
                    if(timer.getTime() > 10 || event.getNodeA() != lastCollided){
                        lastCollided = event.getNodeA();
                        timer.reset();
                        event.getNodeA().getControl(PlayerControl.class).respawn(event.getNodeA(),respawnNode,bulletAppState);     
                        logger.log(Level.WARNING, "stock {0} ", new Object[]{event.getNodeA().getControl(PlayerControl.class).getStock()});
                        
                    }
                }
            }
            if (event.getNodeA().getName().equals("rightBoundingBoxNode")) {
                if (!event.getNodeB().getName().equals("topBoundingBoxNode") && !event.getNodeB().getName().equals("bottomBoundingBoxNode")) {
                    logger.log(Level.WARNING, "Item Destroyed");
                    if(timer.getTime() > 10 || event.getNodeB() != lastCollided){
                        lastCollided = event.getNodeB();
                        timer.reset();
                        event.getNodeB().getControl(PlayerControl.class).respawn(event.getNodeB(),respawnNode,bulletAppState);     
                        logger.log(Level.WARNING, "stock {0} ", new Object[]{event.getNodeB().getControl(PlayerControl.class).getStock()});
                        
                    }
                }
            } else if (event.getNodeB().getName().equals("rightBoundingBoxNode")) {
                if (!event.getNodeA().getName().equals("topBoundingBoxNode") && !event.getNodeA().getName().equals("bottomBoundingBoxNode")) {
                    logger.log(Level.WARNING, "Item Destroyed");
                    if(timer.getTime() > 10 || event.getNodeA() != lastCollided){
                        lastCollided = event.getNodeA();
                        timer.reset();
                        event.getNodeA().getControl(PlayerControl.class).respawn(event.getNodeA(),respawnNode,bulletAppState);     
                        logger.log(Level.WARNING, "stock {0} ", new Object[]{event.getNodeA().getControl(PlayerControl.class).getStock()});
                        
                    }

                }
            }
        }


    }
    
    
    
    /*Gets dimensions of stage as well as simple details that don't 
     * require said dimensions
     */
    SceneGraphVisitorAdapter getDimensions = new SceneGraphVisitorAdapter() {
        @Override
        public void visit(Geometry geom) {
            super.visit(geom);
            findPlatforms(geom);

        }

        @Override
        public void visit(Node node) {
            super.visit(node);
            findRespawn(node);
            findDimensions(node);
            checkSpawnPoints(node);
            findPlatforms(node);
        }

        private void findRespawn(Node node) {
            if (node.getName().equals("respawnNode")) {
                respawnNode = node;
            }
        }

        private void findDimensions(Spatial spatial) {
            if (spatial.getName().equals("bottomBoundingBoxNode")) {
                bot = -(((Node) spatial).getLocalTranslation().getY());
            }
            if (spatial.getName().equals("topBoundingBoxNode")) {
                top = ((Node) spatial).getLocalTranslation().getY();
            }
            if (spatial.getName().equals("rightBoundingBoxNode")) {
                right = ((Node) spatial).getLocalTranslation().getX();
                System.out.println("Right is " + right);
            }
            if (spatial.getName().equals("leftBoundingBoxNode")) {
                left = -(((Node) spatial).getLocalTranslation().getX());
            }
        }

        private void checkSpawnPoints(Spatial spatial) {
            if (spatial.getName().equals("p1SpawnNode")) {
                p1SpawnNode = (Node) spatial;
            }
            if (spatial.getName().equals("p2SpawnNode")) {
                p2SpawnNode = (Node) spatial;
            }
            if (spatial.getName().equals("p3SpawnNode")) {
                p3SpawnNode = (Node) spatial;
            }
            if (spatial.getName().equals("p4SpawnNode")) {
                p4SpawnNode = (Node) spatial;
            }
        }

        private void findPlatforms(Spatial spatial) {
            if (spatial.getName().equals("platform")) {
                if (spatial.getUserData("name").equals("main")) {
                    zExtent = ((BoundingBox) spatial.getWorldBound()).getZExtent();
                    logger.log(Level.WARNING, "Z extent is {0}", new Object[]{zExtent});
                }
                CollisionShape platformShape = CollisionShapeFactory.createMeshShape((Node) spatial);
                RigidBodyControl platform = new RigidBodyControl(platformShape, 1);
                platform.setKinematic(true);
                spatial.addControl(platform);
                bulletAppState.getPhysicsSpace().add(platform);
                logger.log(Level.WARNING, "Found {0}", new Object[]{spatial.getName()});
            }
        }
    };
    /*Assigns controls to blast barriers.
     */
    SceneGraphVisitorAdapter assignControls = new SceneGraphVisitorAdapter() {
        @Override
        public void visit(Geometry geom) {
            super.visit(geom);
        }

        @Override
        public void visit(Node node) {
            super.visit(node);
            checkForMySpatial(node);
            createLedges(node);
        }

        private void checkForMySpatial(Spatial spatial) {
            if (spatial.getName().equals("bottomBoundingBoxNode")) {
                boundingBoxBot = new GhostControl(new BoxCollisionShape(new Vector3f((right + left) / 2, 1, zExtent)));
                ((Node) spatial).addControl(boundingBoxBot);
                bulletAppState.getPhysicsSpace().add(boundingBoxBot);
            }
            if (spatial.getName().equals("topBoundingBoxNode")) {
                boundingBoxTop = new GhostControl(new BoxCollisionShape(new Vector3f((right + left) / 2, 1, zExtent)));
                ((Node) spatial).addControl(boundingBoxTop);
                bulletAppState.getPhysicsSpace().add(boundingBoxTop);
            }
            if (spatial.getName().equals("rightBoundingBoxNode")) {
                boundingBoxRight = new GhostControl(new BoxCollisionShape(new Vector3f(1, (top + bot) / 2, zExtent)));
                ((Node) spatial).addControl(boundingBoxRight);
                bulletAppState.getPhysicsSpace().add(boundingBoxRight);
            }
            if (spatial.getName().equals("leftBoundingBoxNode")) {
                boundingBoxLeft = new GhostControl(new BoxCollisionShape(new Vector3f(1, (top + bot) / 2, zExtent)));
                ((Node) spatial).addControl(boundingBoxLeft);
                bulletAppState.getPhysicsSpace().add(boundingBoxLeft);
            }
            System.out.println("Instance of " + spatial.getName());
        }

        private void createLedges(Spatial spatial) {
            if (spatial.getName().equals("ledgeNode")) {
                if(spatial.getControl(GhostControl.class) == null){                    
                    spatial.addControl(new GhostControl(new CapsuleCollisionShape(zExtent / 8, zExtent, 2)));
                    ledges.add(spatial);
                    System.out.println("Added" + ledgeCount);
                    bulletAppState.getPhysicsSpace().add(spatial.getControl(GhostControl.class));
                }
            }
        }
    };
    public ArrayList getLedges(){
        return ledges;
    }
    /*Adjust bounds to differening dimensions 
     * so the stage is uniform in terms of
     * the blast barriers
     */
    SceneGraphVisitorAdapter translateBounds = new SceneGraphVisitorAdapter() {
        @Override
        public void visit(Geometry geom) {
            super.visit(geom);
            setTranslations(geom);
        }

        @Override
        public void visit(Node node) {
            super.visit(node);
            setTranslations(node);

        }

        private void setTranslations(Spatial spatial) {
            if (spatial.getName().equals("bottomBoundingBoxNode")) {
                ((Node) spatial).getLocalTranslation().setX((-left + right) / 2);
            }
            if (spatial.getName().equals("topBoundingBoxNode")) {
                ((Node) spatial).getLocalTranslation().setX((-left + right) / 2);
            }
            if (spatial.getName().equals("rightBoundingBoxNode")) {
                ((Node) spatial).getLocalTranslation().setY((top + (-bot)) / 2);
            }
            if (spatial.getName().equals("leftBoundingBoxNode")) {
                ((Node) spatial).getLocalTranslation().setY((top + (-bot)) / 2);
            }
        }
    };
}
