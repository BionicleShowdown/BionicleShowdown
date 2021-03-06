package mygame;


import AudioNodes.MusicAudioNode;
import bs.Characters;
import bs.StartMenu;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.audio.AudioSource;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext.Type;
import de.lessvoid.nifty.Nifty;
import java.io.IOException;
import menu.KeyMapListener;
//import com.aurellem.capture.Capture;
//import com.aurellem.capture.IsoTimer;


/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication 
{


    public static Characters charList;
    private StartMenu startState;
    private boolean isRunning = false;
    private Trigger enterTrigger = new KeyTrigger(KeyInput.KEY_RETURN);
    private static Nifty nifty;
    private static NiftyJmeDisplay niftyDisplay;
    private static MusicAudioNode music;
    public static String musicSelection = "Sounds/Music/Fire and Ice.wav";
    private static float musicVolume = 1.0f;
    private static AppSettings settings = new AppSettings(true); // Made this outside of main method so it could be acquired with getSettings()
    
    private ShowdownInputManager joyManager;
    private static CompoundInputManager compoundManager;
    
    public static Joystick[] joysticks;
    
    private KeyMapListener keymapListener = new KeyMapListener();
    
    private AppActionListener actionListener = new AppActionListener();
    
    public static int xCursor, yCursor;
    
    //Temp Input Mappings for testing
    public static int[] player1Mappings = new int[]{KeyInput.KEY_W,KeyInput.KEY_A,KeyInput.KEY_D,
        KeyInput.KEY_1,KeyInput.KEY_S,KeyInput.KEY_O,KeyInput.KEY_2,KeyInput.KEY_P,KeyInput.KEY_LBRACKET};


    public static void main(String[] args) throws IOException
    {
//        AppSettings settings = new AppSettings(true);

        settings.setResolution(800, 600);
        settings.setUseInput(true);
        settings.setTitle("Bionicle Showdown");
        
        settings.setSettingsDialogImage("Textures/Menu/FullVoidLogo.png");
        SimpleApplication app = new Main();
        
        app.setSettings(settings);
        settings.setUseJoysticks(true);
        /*try {
            //Testing Audio+Video capture
            File video = File.createTempFile("Test", ".avi");
            File audio = File.createTempFile("TestingAudio",".wav");
            app.setTimer(new IsoTimer(60));
            app.setShowSettings(false);
            Capture.captureVideo(app,video);
            Capture.captureAudio(app,audio);
            System.out.println("Vid" + video.getCanonicalPath());
            System.out.println("Aud" + audio.getCanonicalPath());
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        
        app.start();
    }


    @Override
    public void simpleInitApp () 
    {
//        if (inputEnabled)
//        {
//            rebuildInputManager();
//        }
        //stateManager.attach(new VideoRecorderAppState()); //starts recording(remove when not needed)
        joyManager = new ShowdownInputManager(this.mouseInput, this.keyInput, new ShowdownJoyInput(), this.touchInput);
        inputManager = new InputManager(this.mouseInput, this.keyInput, null, this.touchInput);
        defaultMapInputManager();
        
        compoundManager = new CompoundInputManager(inputManager, joyManager);
        
        
        startState = new StartMenu();
        setDisplayStatView(true);       //Change to false for release
        music = new MusicAudioNode(assetManager, "Sounds/Music/Fire and Ice.wav", true);
        music.play();

        charList = new Characters((SimpleApplication)this);
//        initKeys();
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        stateManager.attach(startState);               //Attach the first state
        
        initJoy();
        
        flyCam.setEnabled(false);
    }
    /*This ActionListener will handle all the switching of states*/
//    private ActionListener actionListener = new ActionListener() 
//    {
//        public void onAction(String name, boolean isPressed, float tpf) 
//        {
////            System.out.println("Key Name: " + keymapListener.getLastKeyName());
//            // Simulates a Mouse Action, thus initializing JME's Cursor Position (not really necessary)
////            try
////            {
////                Robot rob = new Robot();
////                rob.mouseWheel(1);
////            }
////            catch (AWTException e)
////            {
////                System.out.println("Failed");
////            }
////            
//            if (name.equals("Start Game") && !isPressed) 
//            {
//                if (!isRunning) 
//                {
////                    xCursor = (int) inputManager.getCursorPosition().x;
////                    yCursor = (int) inputManager.getCursorPosition().y;
//                    stateManager.detach(startMenu);
//                    isRunning = !isRunning;
//                }
//            }
//        }
//    };
    
    /*Getter for list of characters
     * currently unlocked
     */
    public static Characters getCharList()
    {
        return(charList);
    }
    
    /*Get Nifty Gui
     * 
     */
    public static Nifty getNifty() 
    {
        return (nifty);
    }
    
    public static MusicAudioNode getMusic()
    {
        return (music);
    }
    
    public static String getMusicSelection()
    {
        return musicSelection;
    }
    
    public static void changeMusic(String newMusicSelection)
    {
        music.stop();
        musicSelection = newMusicSelection;
    }
    
    public static void setMusicVolume(float volume)
    {
        musicVolume = volume;
    }


//    private void initKeys() 
//    {
//        // Key Input testing stuff
//        
////        inputManager.addRawInputListener(keymapListener); // NECESSARY WHEN TESTING KEY ASSIGNMENTS HERE
//        inputManager.addMapping("Start Game", enterTrigger);
////        inputManager.addMapping("Start Game", keymapListener.getLastTrigger());
//        inputManager.addListener(actionListener, "Start Game");
//    }
    
    /**
     * Returns the settings for use by the menu; so buttons are set up properly.
     * If not available, the isClickable() method in Class Button will not work correctly.
     * @return The game's settings. 
     */
    public static AppSettings getSettings()
    {
        return settings;
    }
    
    /**
     * Returns the Nifty Display for use by simulating key events for Nifty to use a Joystick
     * @return The game's NiftyDisplay
     */
    public static NiftyJmeDisplay getNiftyDisplay()
    {
        return niftyDisplay;
    }


    @Override
    public void simpleUpdate(float tpf) 
    {
        joyManager.update(tpf);
//        inputManager.setCursorVisible(false);
//        PointerInfo a = MouseInfo.getPointerInfo();
//        Point b = a.getLocation();
//        System.out.println(b.x);
//        System.out.println(b.y);
//        try
//        {
//            Robot rob = new Robot();
////            rob.mouseMove(0, 0);
////            rob.mouseMove(b.x, b.y);
//            rob.mouseWheel(1);
//        }
//        catch (AWTException e)
//        {
//            System.out.println("Failed");
//        }
        
        
//        System.out.println(inputManager.getCursorPosition().x);
//        System.out.println(inputManager.getCursorPosition().y);
        
        
        // Key input testing stuff
//        if (keymapListener.getLastTrigger() != null)
//        {
//            inputManager.addMapping("Start Game", keymapListener.getLastTrigger());
//        }
//        if (keymapListener.getLastKeyValue() != -1)
//        {
//            player1Mappings[0] = keymapListener.getLastKeyValue();
//        }
        
        
//        if (musicShouldStop)
//        {
//            music.stop();
//        }
        
        if(music.getStatus() == AudioSource.Status.Stopped)
        {
            music = new MusicAudioNode(assetManager, musicSelection, true);
            music.setVolume(musicVolume);
            music.play();
        }
    }


    @Override
    public void simpleRender(RenderManager rm) 
    {
        //TODO: add render code
    }

    private void initJoy() 
    {
        joysticks = joyManager.getJoysticks();
//        joysticks = checkJoysticks();
        System.out.println(joysticks);
        System.out.println(joysticks.length);
        if (joysticks.length == 0)
        {
            System.out.println("FAILED");
        }
        else
        {
            for (int i=0; i < joysticks.length; i++)
            {
                System.out.println("Number of Buttons: " + joysticks[i].getButtonCount());
                System.out.println("Number of Axis: " + joysticks[i].getAxisCount());
                System.out.println(joysticks[i].getName());
                System.out.println(joysticks);
                System.out.println(joysticks[i].getButtons());
                System.out.println(joysticks[i].getAxes());
//                joysticks[i].getButton("0").assignButton("Start Game");
//                joysticks[i].getAxis("x").assignAxis("", "Start Game");
                compoundManager.setAxisDeadZone(0.5f);
//                joysticks[i].rumble(0.1f);
                System.out.println(joysticks[i].getAxis("x").getDeadZone());
            }
        }
    }
    
//    public Joystick[] checkJoysticks()
//    {
//        if (AdaptedControllerEnvironment.defaultEnvironment != null)
//        {
//            AdaptedControllerEnvironment.resetEnvironment();
//        }
//        JoyInput jput = new ShowdownJoyInput();
//        jput.setInputListener(inputManager);
//        Joystick[] jsticks = jput.loadJoysticks(inputManager);
//        return jsticks;
//    }
    
    public void defaultMapInputManager()
    {
        if (inputManager != null) 
        {
        
            if (context.getType() == Type.Display) {
                inputManager.addMapping(INPUT_MAPPING_EXIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
            }

            if (stateManager.getState(StatsAppState.class) != null) {
                inputManager.addMapping(INPUT_MAPPING_HIDE_STATS, new KeyTrigger(KeyInput.KEY_F5));
                inputManager.addListener(actionListener, INPUT_MAPPING_HIDE_STATS);            
            }
            
            inputManager.addListener(actionListener, INPUT_MAPPING_EXIT);            
        }
    }

    
    private class AppActionListener implements ActionListener {

        public void onAction(String name, boolean value, float tpf) {
            if (!value) {
                return;
            }

            if (name.equals(INPUT_MAPPING_EXIT)) {
                stop();
            }else if (name.equals(INPUT_MAPPING_HIDE_STATS)){
                if (stateManager.getState(StatsAppState.class) != null) {
                    stateManager.getState(StatsAppState.class).toggleStats();
                }
            }
        }
    };
    
    public InputManager getJoyManager()
    {
        return joyManager;
    }
    
    public static CompoundInputManager getCompoundManager()
    {
        return compoundManager;
    }
}