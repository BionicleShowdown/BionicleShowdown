
package menu;

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
public class menu extends AbstractAppState implements ScreenController 
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
    StartState startState;
    //static menu app = new menu(); // Defined outside of function to allow use in all methods
    static AppSettings settings = new AppSettings(true); // Defined outside of function to allow use in all methods
    
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
    
    
    
    /* Records the current screen the player is on. */
    int currentScreen = 0;
    
    final int startScreen = 0;
    final int mainMenu = 1;
    final int fightScreen = 2;
    final int trainingScreen = 3;
    final int extrasScreen = 4;
    final int optionsScreen = 5;
    
    
    /* Creates players. CurrentPlayer is set to one of the four players. The four players can be set to named players. */
    Player currentPlayer = null;
    Player player1 = new NullPlayer("Player1");
    Player player2 = new NullPlayer("Player2");
    Player player3 = new NullPlayer("Player3");
    Player player4 = new NullPlayer("Player4");
    
    String currentPlayerSelection = "";
    
    
    /* Creates characters. This may not be necessary */
    final PlayableCharacter random = new RandomCharacter();
    final PlayableCharacter tahu = new Tahu();
    final PlayableCharacter kopaka = new Kopaka();
    
    
    /* Records whether the TeamType is Free For All or a Team Match. */
    String teamType = "Free For All";
    
    public menu(){}
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.audioRenderer = this.app.getAudioRenderer();
        this.guiViewPort = this.app.getViewPort();
        this.stateManager = this.app.getStateManager();
        nifty = Main.getNifty();
        nifty.registerScreenController(this);
        nifty.addXml("Interface/screen.xml");
        nifty.gotoScreen("MainMenu");          //Just for the first one, got to the start screen
        

        nifty.setDebugOptionPanelColors(false);
    }
    

    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void update(float tpf) {
        /* Nothing Yet*/
    }

    @Override
    public void cleanup() {
              
        
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);


    }
    
    public void onStartScreen() 
    {
        screenHeight = settings.getHeight(); // Gets the height of the window after the window is created, but before it is used
        screenWidth = settings.getWidth(); // Gets the width of the window after the window is created, but before it is used
        
        /* Initializes all Buttons */
        fightButton = new Button(fightImage, 0.1, 0.2, 0.35, 0.25);
        trainingButton = new Button(trainingImage, .55, .20, .35, .25);
        extrasButton = new Button(extrasImage, .1, .55, .35, .25);
        optionsButton = new Button(optionsImage, .55, .55, .35, .25);
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
             nifty.gotoScreen("CharSelect");
             nifty.getScreen("CharSelect").getScreenController();
             currentScreen = fightScreen;
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
        if (optionsButton.isClickable(x, y))
        {
            System.out.println("Clickable.");
            System.out.println("Option-ing.");
        }
        else
        {
            System.out.println("Not Clickable.");
        }
    }
    
    public void selectRandom() // Add to selectCharacter method
    {
        System.out.println(currentPlayerSelection);
        if (currentPlayer == null)
        {
            return;
        }
        currentPlayer.currentCharacter = new RandomCharacter();
        System.out.println(currentPlayer.currentCharacter);
        NiftyImage image = nifty.getRenderEngine().createImage("Interface/CharacterSelect/SelectedRandom.png", false);
        nifty.getCurrentScreen().findElementByName(currentPlayerSelection + "Select").getRenderer(ImageRenderer.class).setImage(image);
    }
    
//    public void selectTahu()
//    {
//        System.out.println(currentPlayerSelection);
//        if (currentPlayer == null)
//        {
//            return;
//        }
//        currentPlayer.currentCharacter = new Tahu();
//        System.out.println(currentPlayer.currentCharacter);
//        NiftyImage image = nifty.getRenderEngine().createImage("Interface/CharacterSelect/SelectedTahu.png", false);
//        nifty.getCurrentScreen().findElementByName(currentPlayerSelection).getRenderer(ImageRenderer.class).setImage(image);
//    }
    
//    public void selectKopaka()
//    {
//        System.out.println(currentPlayerSelection);
//        if (currentPlayer == null)
//        {
//            return;
//        }
//        currentPlayer.currentCharacter = new Kopaka();
//        System.out.println(currentPlayer.currentCharacter);
//        NiftyImage image = nifty.getRenderEngine().createImage("Interface/CharacterSelect/SelectedKopaka.png", false);
//        nifty.getCurrentScreen().findElementByName(currentPlayerSelection).getRenderer(ImageRenderer.class).setImage(image);
//    }
    
    public void selectTahu()
    {
        selectCharacter(new Tahu());
    }
    
    public void selectKopaka()
    {
        selectCharacter(new Kopaka());
    }
     
    private void selectCharacter(PlayableCharacter character)
    {
        if (currentPlayer == null)
        {
            System.out.println("Null Character");
            return;
        }
        currentPlayer.currentCharacter = character;
        if (currentPlayer.playerType.equals("Null"))
        {
            setPlayerType(currentPlayerSelection); // Sets all Nulls to a computer player when first chosen
            if (currentPlayerSelection.equals("Player1")) // Sets Player 1 to a human player when first chosen
            {
                setPlayerType(currentPlayerSelection);
            }
        }
        if (currentPlayer.costume == null)
        {
            currentPlayer.costume = currentPlayer.currentCharacter.costumeKeys.get(0);
            System.out.println("------> Current costume is: " + currentPlayer.costume);
        }
        if (teamType.equals("Team Match"))
        {
           if (!currentPlayer.costume.owner.equals(currentPlayer.currentCharacter.name))
           {
               currentPlayer.costume.owner = currentPlayer.currentCharacter.name;
           }
        }
        if (!currentPlayer.costume.owner.equals(currentPlayer.currentCharacter.name))
        {
            System.out.println("<-------" + currentPlayer.costume);
            for (Costume costume : currentPlayer.currentCharacter.costumeKeys) 
            {
                if (currentPlayer.costume.name.equals(costume.name))
                {
                    currentPlayer.costume = costume;
                }
            }
            System.out.println("------->" + currentPlayer.costume);
        }
        if (!currentPlayer.costume.owner.equals(currentPlayer.currentCharacter.name))
        {
            currentPlayer.costume = currentPlayer.currentCharacter.costumeKeys.get(0);
        }
        System.out.println(currentPlayer.playerNumber + "'s Character is: " + currentPlayer.currentCharacter + " with the " + currentPlayer.playerType + " type, and the " + currentPlayer.costume + " costume.");
        if (!costumeNotDuplicate(currentPlayer)) // If the costume is a duplicate, change the costume
        {
            setPlayerCostume(currentPlayer.playerNumber);
        }
        if (teamType.equals("Team Match"))
        {
            if (currentPlayer.team.equals(""))
            {
                setPlayerCostume(currentPlayer.playerNumber);
            }
            if (currentPlayer.costume.name.equals("Standard"))
            {
                setPlayerCostume(currentPlayer.playerNumber);
            }
        }
//        System.out.println("Before: " + currentPlayer.currentCharacter);
//        currentPlayer.currentCharacter = character;
//        System.out.println("After: " + currentPlayer.currentCharacter);
        printPlayerSelections();
        NiftyImage image = nifty.getRenderEngine().createImage("Interface/CharacterSelect/" + currentPlayer.costume + ".png", false);
        nifty.getCurrentScreen().findElementByName(currentPlayerSelection + "Select").getRenderer(ImageRenderer.class).setImage(image);
        matchReady();
    }
     
    
    public void setPlayer(String player)
    {
        Player temp;
        if (player.equals("Player1"))
        {
            temp = player1;
        }
        else if (player.equals("Player2"))
        {
            temp = player2;
        }
        else if (player.equals("Player3"))
        {
            temp = player3;
        }
        else if (player.equals("Player4"))
        {
            temp = player4;
        }
        else
        {
            System.out.println("An Error has Occured.");
            return;
        }
        System.out.println(temp.playerNumber);
        currentPlayer = temp;
        currentPlayerSelection = currentPlayer.playerNumber;
    }
    
    public void setPlayerType(String player)
    {
//        System.out.print("Changing player type ");
        Player temp;
        
        if (player.equals("Player1"))
        {
//            System.out.print("from " + player1.playerType);
            player1 = cyclePlayerType(player1);
            temp = player1;
        }
        else if (player.equals("Player2"))
        {
//            System.out.print("from " + player2.playerType);
            player2 = cyclePlayerType(player2);
            temp = player2;
        }
        else if (player.equals("Player3"))
        {
//            System.out.print("from " + player3.playerType);
            player3 = cyclePlayerType(player3);
            temp = player3;
        }
        else if (player.equals("Player4"))
        {
//            System.out.print("from " + player4.playerType);
            player4 = cyclePlayerType(player4);
            temp = player4;
        }
        else
        {
            System.out.println("An Error has Occured");
            return;
        }
        
        currentPlayer = temp;
        currentPlayerSelection = currentPlayer.playerNumber;
//        System.out.println(" to " + temp.playerType);
        NiftyImage image = nifty.getRenderEngine().createImage("Interface/PlayerSelect/PlayerType" + temp.playerType + ".png", false);
        nifty.getCurrentScreen().findElementByName(temp.playerNumber + "Type").getRenderer(ImageRenderer.class).setImage(image);
        matchReady();
    }
    
    private Player cyclePlayerType(Player player)
    {
        if (player.playerType.equals("Null"))
        {
            NiftyImage image = nifty.getRenderEngine().createImage("Interface/PlayerSelect/" + player.playerNumber + "Select.png", false);
            nifty.getCurrentScreen().findElementByName(player.playerNumber + "Select").getRenderer(ImageRenderer.class).setImage(image);
            System.out.println(player.currentCharacter);
            return new CPUPlayer(player.playerNumber, player.currentCharacter);
        }
        else if (player.playerType.equals("CPU"))
        {
            System.out.println(player.currentCharacter);
            return new HumanPlayer(player.playerNumber, player.currentCharacter);
        }
        else if (player.playerType.equals("Human"))
        {
            NiftyImage image = nifty.getRenderEngine().createImage("Interface/PlayerSelect/PlayerNullSelect.png", false);
            nifty.getCurrentScreen().findElementByName(player.playerNumber + "Select").getRenderer(ImageRenderer.class).setImage(image);
            System.out.println(player.currentCharacter);
            return new NullPlayer(player.playerNumber);
        }
        else
        {
            return null;
        }
    }
    
    public void setPlayerCostume(String player)
    {
        if (teamType.equals("Free For All"))
        {
            setPlayerFFACostume(player);
        }
        else if (teamType.equals("Team Match"))
        {
            setPlayerTeamCostume(player);
            // TODO: Add something to actually add members to the team, or else TeamFull will not work!
        }
        else
        {
            System.out.println("An Error has Occurred");
        }
    }
    
    public void setPlayerFFACostume(String player)
    {
        Player temp = null;
        
        if (player.equals("Player1"))
        {
            player1.costume = cyclePlayerCostume(player1);
            if (costumeNotDuplicate(player1)) // If the costume isn't a duplicate, then continue the method
            {
                temp = player1;
            }
            else // But if it is a duplicate, call the method again to cycle to the next costume
            {
                System.out.println("Player 1 Costume " + player1.costume.name + " is a duplicate.");
                setPlayerCostume(player);
            }
        }
        else if (player.equals("Player2"))
        {
            player2.costume = cyclePlayerCostume(player2);
            if (costumeNotDuplicate(player2))
            {
                temp = player2;
            }
            else
            {
                setPlayerCostume(player);
            }
        }
        else if (player.equals("Player3"))
        {
            player3.costume = cyclePlayerCostume(player3);
            if (costumeNotDuplicate(player3))
            {
                temp = player3;
            }
            else
            {
                setPlayerCostume(player);
            }
        }
        else if (player.equals("Player4"))
        {
            player4.costume = cyclePlayerCostume(player4);
            if (costumeNotDuplicate(player4))
            {
                temp = player4;
            }
            else
            {
                setPlayerCostume(player);
            }
        }
        else
        {
            System.out.println("An Error has Occured");
            return;
        }
        
        if (temp == null) // This occurs occasionally for some reason, but the method works anyway.
        {
            System.out.println("An Error has Occured");
//            System.exit(0);
            return;
        }
        System.out.println("New Costume is: " + temp.costume.name);
        
        NiftyImage image = nifty.getRenderEngine().createImage("Interface/CharacterSelect/" + temp.currentCharacter.name + "/" + temp.costume.name + ".png", false);
        nifty.getCurrentScreen().findElementByName(player + "Select").getRenderer(ImageRenderer.class).setImage(image);
//        System.out.println(" to " + temp.playerType);
//        NiftyImage image = nifty.getRenderEngine().createImage("Interface/PlayerSelect/PlayerType" + temp.playerType + ".png", false);
//        nifty.getCurrentScreen().findElementByName(temp.playerNumber + "Type").getRenderer(ImageRenderer.class).setImage(image);
    }
    
    // Make a thing for team matches
    public void setPlayerTeamCostume(String player)
    {
        Player temp = null;
        
        if (player.equals("Player1"))
        {
            player1.costume = cyclePlayerTeam(player1);
            temp = player1;
        }
        else if (player.equals("Player2"))
        {
            player2.costume = cyclePlayerTeam(player2);
            temp = player2;
        }
        else if (player.equals("Player3"))
        {
            player3.costume = cyclePlayerTeam(player3);
            temp = player3;
        }
        else if (player.equals("Player4"))
        {
            player4.costume = cyclePlayerTeam(player4);
            temp = player4;
        }
        else
        {
            System.out.println("An Error has Occured.");
            return;
        }
        
        if (temp == null) // This may occur, I'm unsure as of yet.
        {
            System.out.println("An Error has Occured");
            // System.exit(0);
            return;
        }
        System.out.println("New Costume is: " + temp.costume.name);
        
        NiftyImage image = nifty.getRenderEngine().createImage("Interface/CharacterSelect/" + temp.currentCharacter.name + "/" + temp.costume.name + ".png", false);
        nifty.getCurrentScreen().findElementByName(player + "Select").getRenderer(ImageRenderer.class).setImage(image);
    }
    
//    private Costume cyclePlayerCostume(Player player)
//    {
//        if (teamType.equals("Free For All"))
//        {
//            return cyclePlayerFFACostume(player);
//        }
//        else if (teamType.equals("Team Match"))
//        {
//            return cyclePlayerTeamCostume(player);
//        }
//        else
//        {
//            System.out.println("An Error has Occured.");
//            return player.costume;
//        }
//    }
    
    
    //TODO Figure out a way to implement team selections
    private Costume cyclePlayerCostume(Player player)
    {
        System.out.println("Cycling Player Costume");
        if (player.playerType.equals("Null"))
        {
            return new Costume("Standard", player.currentCharacter.name);
        }
        PlayableCharacter temp = player.currentCharacter;
//        Costumes currentCostume = player.costume;
        Costume currentCostume = player.costume;
        if (currentCostume == null)
        {
            return temp.costumeKeys.get(0);
        }
        if (!currentCostume.owner.equals(temp.name))
        {
            System.out.println("<-------" + currentCostume);
            for (Costume costume : temp.costumeKeys) 
            {
                if (currentCostume.name.equals(costume.name))
                {
                    currentCostume = costume;
                }
            }
            System.out.println("------->" + currentCostume);
        }
        System.out.println(temp.costumeKeys);
        System.out.println(temp.costumeKeys.contains(currentCostume));
        
        int i = temp.costumeKeys.indexOf(currentCostume);
        if (i < (temp.costumeKeys.size() - 1))
        {
            System.out.println("Index " + i + " of " + temp.costumeKeys.size());
            return temp.costumeKeys.get(++i);
        }
        else
        {
            return temp.costumeKeys.get(0);
        }
    }
    
    private Costume cyclePlayerTeam(Player player)
    {
        System.out.println("Cycling Player Team");
        if (player.playerType.equals("Null"))
        {
            return new Costume("Red", player.currentCharacter.name);
        }
        
        int index = 0;
        String oldTeam = "";
        if (player.costume == null)
        {
            oldTeam = player.team;
            player.team = Team.teamList.get(index);
            while (Team.teamFull(player.team))
            {
                index++;
                if (index >= Team.teamList.size())
                {
                    index = 0;
                }
                player.team = Team.teamList.get(index);
                System.out.println("Player's team is: " + player.team);
            }  
        }
        else
        {
            index = Team.teamList.indexOf(player.team);
            index++;
            if (index >= Team.teamList.size())
            {
                index = 0;
            }
            oldTeam = player.team;
            player.team = Team.teamList.get(index);
            while (Team.teamFull(player.team))
            {
                index++;
                if (index >= Team.teamList.size())
                {
                    index = 0;
                }
                player.team = Team.teamList.get(index);
                System.out.println("Player's team is: " + player.team);
            }
        }
        
        System.out.println("Red team's members were: " + Team.availableTeams.get(Team.teamList.get(index)).members);
        Team.removeMember(oldTeam, player);
        Team.addMember(player.team, player);
        System.out.println("Red team's members are now: " + Team.availableTeams.get(Team.teamList.get(index)).members);
        
        return cyclePlayerTeamCostume(player, 0);
    }
    
    private Costume cyclePlayerTeamCostume(Player player, int index)
    {
        if (player == null)
        {
            return null;
        }
        
        player.costume = new Costume(PlayableCharacter.teamCostumes.get(player.team).get(index), player.currentCharacter.name);
        
        if (costumeNotDuplicate(player))
        {
            return player.costume;
        }
        else
        {
            return cyclePlayerTeamCostume(player, index + 1);
        }
    }
    
    private boolean costumeNotDuplicate(Player player)
    {
        boolean cond1 = true;
        boolean cond2 = true;
        boolean cond3 = true;
        boolean cond4 = true;
        
        if (!player.equals(player1) && (!player1.playerType.equals("Null")))
        {
            cond1 = !player.sameCostumeAndCharacter(player1); // If the player isn't player 1 and also doesn't have the same costume and character as player 1, this is true
        }
        if (!player.equals(player2) && (!player2.playerType.equals("Null")))
        {
            cond2 = !player.sameCostumeAndCharacter(player2);
        }
        if (!player.equals(player3) && (!player3.playerType.equals("Null")))
        {
            cond3 = !player.sameCostumeAndCharacter(player3);
        }
        if (!player.equals(player4) && (!player4.playerType.equals("Null")))
        {
            cond4 = !player.sameCostumeAndCharacter(player4);
        }
        System.out.println(cond1 + ", " + cond2 + ", " + cond3 + ", " + cond4);
        return (cond1 && cond2 && cond3 && cond4); // If all the conditions are true, then the costume is not a duplicate in the match
    }
    
    public void switchTeamType()
    {
      if (teamType.equals("Free For All"))
      {
          teamType = "Team Match";
          NiftyImage image = nifty.getRenderEngine().createImage("Interface/TeamMatch.png", false);
          nifty.getCurrentScreen().findElementByName("TeamType").getRenderer(ImageRenderer.class).setImage(image);
          System.out.println("Changing costumes");
//          setPlayerCostume(player1.playerNumber);
//          setPlayerCostume(player2.playerNumber);
//          setPlayerCostume(player3.playerNumber);
//          setPlayerCostume(player4.playerNumber);
          nullifyAndResetCostumesAndTeams();
      }
      else
      {
          teamType = "Free For All";
          NiftyImage image = nifty.getRenderEngine().createImage("Interface/FreeForAll.png", false);
          nifty.getCurrentScreen().findElementByName("TeamType").getRenderer(ImageRenderer.class).setImage(image);
          System.out.println("Changing costumes");
//          setPlayerCostume(player1.playerNumber);
//          setPlayerCostume(player2.playerNumber);
//          setPlayerCostume(player3.playerNumber);
//          setPlayerCostume(player4.playerNumber);
          nullifyAndResetCostumesAndTeams();
      }
    }
    
    public void nullifyAndResetCostumesAndTeams()
    {
        player1.costume = null;
        player2.costume = null;
        player3.costume = null;
        player4.costume = null;
        Team.removeMember(player1.team, player1);
        Team.removeMember(player2.team, player2);
        Team.removeMember(player3.team, player3);
        Team.removeMember(player4.team, player4);
        player1.team = "";
        player2.team = "";
        player3.team = "";
        player4.team = "";
        setPlayerCostume(player1.playerNumber);
        setPlayerCostume(player2.playerNumber);
        setPlayerCostume(player3.playerNumber);
        setPlayerCostume(player4.playerNumber);
    }
  
  
    public void goBack()
    {
      nifty.gotoScreen("MainMenu");
      nifty.getScreen("MainMenu").getScreenController();
      
      switch (currentScreen)
      {
      
          case fightScreen:
          case trainingScreen:
          case extrasScreen:
          case optionsScreen:
          {
               nifty.gotoScreen("MainMenu");
               nifty.getScreen("MainMenu").getScreenController();
               break;
          }
              
          
                  
                  
          default:
          {
//              nifty.gotoScreen("StartScreen");
//              TODO: Create a Start Screen
          }    
              
      }
    }
    
    private int numberOfPlayers()
    {
        int numberOfPlayers = 0;
        if (player1.canPlay())
        {
            numberOfPlayers++;
        }
        if (player2.canPlay())
        {
            numberOfPlayers++;
        }
        if (player3.canPlay())
        {
            numberOfPlayers++;
        }
        if (player4.canPlay())
        {
            numberOfPlayers++;
        }
        return numberOfPlayers;
    }
    
    public int numberOfTeams()
    {
        List<String> teamsInvolved = new ArrayList(4);
        if (player1.hasTeam())
        {
            if (!teamsInvolved.contains(player1.team))
            {
                teamsInvolved.add(player1.team);
            }
        }
        if (player2.hasTeam())
        {
            if (!teamsInvolved.contains(player2.team))
            {
                teamsInvolved.add(player2.team);
            }
        }
        if (player3.hasTeam())
        {
            if (!teamsInvolved.contains(player3.team))
            {
                teamsInvolved.add(player3.team);
            }
        }
        if (player4.hasTeam())
        {
            if (!teamsInvolved.contains(player4.team))
            {
                teamsInvolved.add(player4.team);
            }
        }
        
        System.out.println(teamsInvolved);
        return teamsInvolved.size();
    }
    
    public boolean matchReady()
    {
        int numberOfPlayers = numberOfPlayers();
        int numberOfTeams = numberOfTeams();
        
        if (teamType.equals("Team Match"))
        {
            if (numberOfTeams > 1)
            {
                NiftyImage image = nifty.getRenderEngine().createImage("Interface/MatchReady.png", false);
                nifty.getCurrentScreen().findElementByName("StartMatch").getRenderer(ImageRenderer.class).setImage(image);
                return true;
            }
            else
            {
                NiftyImage image = nifty.getRenderEngine().createImage("Interface/MatchNotReady.png", false);
                nifty.getCurrentScreen().findElementByName("StartMatch").getRenderer(ImageRenderer.class).setImage(image);
                return false;  
            }
        }
        
        if (numberOfPlayers > 1)
        {
            NiftyImage image = nifty.getRenderEngine().createImage("Interface/MatchReady.png", false);
            nifty.getCurrentScreen().findElementByName("StartMatch").getRenderer(ImageRenderer.class).setImage(image);
            return true;
        }
        else 
        {
           NiftyImage image = nifty.getRenderEngine().createImage("Interface/MatchNotReady.png", false);
           nifty.getCurrentScreen().findElementByName("StartMatch").getRenderer(ImageRenderer.class).setImage(image);
           return false; 
        }     
    }
    
    public void startMatch()
    {
        System.out.println("Working?");
        inGameState = new InGameState();
        stateManager.detach(this);
        stateManager.attach(inGameState);
    }
    
    
    public void printPlayerSelections()
    {
        System.out.println(currentPlayer.playerType + " Player (" + currentPlayer.playerNumber + ") as " + currentPlayer.currentCharacter + " with " + currentPlayer.costume + " costume.");
        System.out.println(player1.playerType + " Player as " + player1.currentCharacter + " with " + player1.costume + " costume on " + player1.team + " Team.");
        System.out.println(player2.playerType + " Player as " + player2.currentCharacter + " with " + player2.costume + " costume on " + player2.team + " Team.");
        System.out.println(player3.playerType + " Player as " + player3.currentCharacter + " with " + player3.costume + " costume on " + player3.team + " Team.");
        System.out.println(player4.playerType + " Player as " + player4.currentCharacter + " with " + player4.costume + " costume on " + player4.team + " Team.");
        for (String color : Team.availableTeams.keySet()) 
        {
            System.out.println("Team " + color + " has members: " + Team.stringMembers(color));
        }
    }
    
    
    
//    public void toggleToFullscreen() // This toggles fullscreen, but is really ugly
//    {
//        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//        DisplayMode[] modes = device.getDisplayModes();
//        int i=0; // note: there are usually several, let's pick the first
//        settings.setResolution(modes[i].getWidth(),modes[i].getHeight());
//        settings.setFrequency(modes[i].getRefreshRate());
//        settings.setDepthBits(modes[i].getBitDepth());
//        settings.setFullscreen(device.isFullScreenSupported());
//        app.setSettings(settings);
//        app.restart(); // restart the context to apply changes
//    }
      
}
