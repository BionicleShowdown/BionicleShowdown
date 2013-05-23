
package menu;

import Characters.Tahu;
import Characters.Kopaka;
import Characters.PlayableCharacter;
import Characters.RandomCharacter;
import bs.InGameState;
import bs.StartState;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import mygame.Main;

/**
 *
 * @author JSC and Inferno
 */
public class MainMenu extends AbstractAppState implements ScreenController 
{
    private AssetManager assetManager;
    private SimpleApplication app;
    private Node rootNode;
    private Nifty nifty;
    private NiftyJmeDisplay niftyDisplay;
    private Screen screen;
    private InputManager inputManager;
    private AudioRenderer audioRenderer;
    private ViewPort guiViewPort;
    private AppStateManager stateManager;
    private InGameState inGameState;
    private CharacterSelectMenu characterSelectMenu;
    
    StartState startState;
    static AppSettings settings = Main.getSettings(); // Defined outside of function to allow use in all methods
    
    /* Intiializes the screenHeight and screenWidth so they can be used later (initialized in onStartScreen() */
    static int screenHeight;
    static int screenWidth;
   
    /* Files used to initialize Button objects */
    File fightImage = new File("assets/Interface/MainMenu/Fight.png");
    File trainingImage = new File("assets/Interface/MainMenu/Training.png");
    File extrasImage = new File("assets/Interface/MainMenu/Extras.png");
    File optionsImage = new File("assets/Interface/MainMenu/Options.png");
    
    /* Uninitialized Button objects (they are initialized in onStartScreen() */
    Button fightButton;
    Button trainingButton;
    Button extrasButton;
    Button optionsButton;
    
    
    /* Records whether the TeamType is Free For All or a Team Match. */
    String teamType = "Free For All";
    
    public MainMenu() // Don't compress it like that please, it's ugly. 
    {
    
    }
    
    public void initiate(Application app) 
    {
        this.app = (SimpleApplication) app;
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.audioRenderer = this.app.getAudioRenderer();
        this.guiViewPort = this.app.getViewPort();
        this.stateManager = this.app.getStateManager();
        nifty = Main.getNifty();
        nifty.registerScreenController(this);
        nifty.addXml("Interface/GUIS/MainMenu.xml");
        nifty.gotoScreen("MainMenu");          //Just for the first one, got to the start screen
        
        nifty.setDebugOptionPanelColors(true); // Leave it on true for now, so you can see the costume buttons.
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) 
    {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.audioRenderer = this.app.getAudioRenderer();
        this.guiViewPort = this.app.getViewPort();
        this.stateManager = this.app.getStateManager();
        nifty = Main.getNifty();
        nifty.registerScreenController(this);
        nifty.addXml("Interface/GUIS/MainMenu.xml");
        nifty.gotoScreen("MainMenu");          //Just for the first one, got to the start screen
        
        nifty.setDebugOptionPanelColors(true); // Leave it on true for now, so you can see the costume buttons.
    }
    

    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void update(float tpf) 
    {
        /* Nothing Yet*/
    }

    @Override
    public void cleanup() 
    {
              
        
    }

    @Override
    public void setEnabled(boolean enabled) 
    {
        super.setEnabled(enabled);
    }
    
    public void onStartScreen() 
    {
        screenHeight = settings.getHeight(); // Gets the height of the window after the window is created, but before it is used
        screenWidth = settings.getWidth(); // Gets the width of the window after the window is created, but before it is used
        System.out.println("Screen height is: " + screenHeight + ", Screen width is: " + screenWidth);
        /* Initializes all Buttons */
//        fightButton = new Button(fightImage, 0.1, 0.2, 0.35, 0.25);
//        trainingButton = new Button(trainingImage, .55, .20, .35, .25);
//        extrasButton = new Button(extrasImage, .1, .55, .35, .25);
//        optionsButton = new Button(optionsImage, .55, .55, .35, .25);
        
        // Not sure which version would be better
        fightButton = new Button(new File("assets/Interface/MainMenu/Fight.png"), 0.1, 0.2, 0.35, 0.25);
        trainingButton = new Button(new File("assets/Interface/MainMenu/Training.png"), .55, .20, .35, .25);
        extrasButton = new Button(new File("assets/Interface/MainMenu/Extras.png"), .1, .55, .35, .25);
        optionsButton = new Button(new File("assets/Interface/MainMenu/Options.png"), .55, .55, .35, .25);
    }

    public void onEndScreen() 
    {
        
    }
    
    public void fightScreen(int x, int y)
    {
        System.out.println("" + x + ", " + y);
         if (fightButton.isClickable(x, y))
         {
             System.out.println("This spot is clickable.");
             System.out.println("Fighting");
//             clearPlayersAndTeams();
//             nifty.gotoScreen("CharSelect");
//             nifty.getScreen("CharSelect").getScreenController();
//             currentScreen = fightScreen;
             characterSelectMenu = new CharacterSelectMenu(this);
             characterSelectMenu.initiate(app);
         }
         else
         {
             System.out.println("Not clickable.");
         }
    }
    
    public void trainingScreen(int x, int y) 
    {
        System.out.println("" + x + ", " + y);
        if (trainingButton.isClickable(x, y))
        {
            System.out.println("Clickable.");
            System.out.println("Training.");
            Tahu.unlockCostume("Special");
            Team.addTeam("Green");
        }
        else
        {
            System.out.println("Not Clickable.");
        }
    }
    
    public void extrasScreen(int x, int y)
    {
        System.out.println("" + x + ", " + y);
        if (extrasButton.isClickable(x, y))
        {
            System.out.println("Clickable.");
            System.out.println("Extra-ing.");
        }
        else
        {
            System.out.println("Not Clickable.");
        }
    }
        
    public void optionsScreen(int x, int y)
    {
        System.out.println("" + x + ", " + y);
        optionsButton.printAll();
        if (optionsButton.isClickable(x, y))
        {
            System.out.println("Clickable.");
            System.out.println("Option-ing.");
            OptionsMenu optionsMenu = new OptionsMenu(this);
            optionsMenu.initiate(app);
        }
        else
        {
            System.out.println("Not Clickable.");
        }
    }
          
}
