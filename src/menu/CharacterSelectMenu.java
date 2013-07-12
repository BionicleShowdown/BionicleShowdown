
package menu;

import AudioNodes.SFXAudioNode;
import AudioNodes.VoiceAudioNode;
import Players.CPUPlayer;
import Players.Player;
import Players.HumanPlayer;
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
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.DraggableDragCanceledEvent;
import de.lessvoid.nifty.controls.DraggableDragStartedEvent;
import de.lessvoid.nifty.controls.DroppableDroppedEvent;
import de.lessvoid.nifty.controls.NiftyControl;
import de.lessvoid.nifty.controls.dragndrop.DraggableControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
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
 * @author Inferno
 */
public class CharacterSelectMenu implements ScreenController 
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
    private MainMenu mainMenu;
    
    StartState startState;
    //static menu app = new menu(); // Defined outside of function to allow use in all methods
    static AppSettings settings = Main.getSettings(); // Defined outside of function to allow use in all methods
    
    /* Intiializes the screenHeight and screenWidth so they can be used later (initialized in onStartScreen() */
    static int screenHeight;
    static int screenWidth;
    
    /* Creates players. CurrentPlayer is set to one of the four players. The four players can be set to named players. */
    Player currentPlayer = null;
    Player player1 = new NullPlayer("Player1");
    Player player2 = new NullPlayer("Player2");
    Player player3 = new NullPlayer("Player3");
    Player player4 = new NullPlayer("Player4");
    
    String currentPlayerSelection = "";
    
    VoiceAudioNode characterSelectedAnnounce;
    
    // Current X and Y coordinates
    int currentX = 0;
    int currentY = 0;
    
    // Creates the Player Droppers
    Button player1Dropper;
    Button player2Dropper;
    Button player3Dropper;
    Button player4Dropper;
    
    /* Creates characters. This may not be necessary */
//    final PlayableCharacter random = new RandomCharacter();
//    final PlayableCharacter tahu = new Tahu();
//    final PlayableCharacter kopaka = new Kopaka();
    
    
    /* Records whether the TeamType is Free For All or a Team Match. */
    String teamType = "Free For All";
    
    public CharacterSelectMenu() // Don't compress it like that please, it's ugly. 
    {
    
    }
    
    public CharacterSelectMenu(MainMenu state)
    {
        this.mainMenu = state;
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
        nifty.addXml("Interface/GUIS/CharacterSelectMenu.xml");
        nifty.gotoScreen("CharSelect");
//        nifty.getScreen("CharSelect").getScreenController();
        nifty.setDebugOptionPanelColors(false); // Leave it on true for now, so you can see the costume buttons.
    }
    
   
    

    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
        this.screen = screen;
    }

    
    public void onStartScreen() 
    {
        screenHeight = settings.getHeight(); // Gets the height of the window after the window is created, but before it is used
        screenWidth = settings.getWidth(); // Gets the width of the window after the window is created, but before it is used
        System.out.println("Screen height is: " + screenHeight + ", Screen width is: " + screenWidth);
        resetVariables(); // Not entirely sure why this is necessary if the Menu is a new instance, but it is.
        disableAndHideDroppers(); // Sets the droppers to not exist until a player isn't null. May start Player 1 as not null to begin with.
        player1Dropper = new Button(app, "Interface/CharacterSelect/Player1Dropper.png", screen.findElementByName("Player1Dropper"), false);
        player2Dropper = new Button(app, "Interface/CharacterSelect/Player2Dropper.png", screen.findElementByName("Player2Dropper"), false);
        player3Dropper = new Button(app, "Interface/CharacterSelect/Player3Dropper.png", screen.findElementByName("Player3Dropper"), false);
        player4Dropper = new Button(app, "Interface/CharacterSelect/Player4Dropper.png", screen.findElementByName("Player4Dropper"), false);
        setPlayerType("Player1"); // Called twice so Player1 will first appear as the Human type.
        setPlayerType("Player1");
        matchReady();
    }

    public void onEndScreen() 
    {
        
    }
    
    @NiftyEventSubscriber(pattern="Player.*") // Could switch to ".*Dropper"
    public void getCoordinates(String id, NiftyMousePrimaryClickedEvent event)
    {
        System.out.println("<<<---GETTING COORDINATES--->>>");
        System.out.println("X is: " + event.getMouseX() + ", Y is: " + event.getMouseY());
        currentX = event.getMouseX();
        currentY = event.getMouseY();
        
    }
    
    @NiftyEventSubscriber(pattern=".*SelectPanel") 
    // Something.* makes prefix, Something.*Else gets anything that begins with Something and ends with Else, and .*Else gets anything that ends with Else 
    public void activatePlayer(String id, NiftyMousePrimaryClickedEvent event)
    {
        String playerToActivate = event.getElement().getId();
        playerToActivate = playerToActivate.substring(0, 7);
        System.out.println("The Player to activate is " + playerToActivate);
        Player player = getPlayerFromNumber(playerToActivate);
        if ("Null".equals(player.playerType))
        {
            if (player == player1)
            {
                setPlayerType(playerToActivate);
            }
            setPlayerType(playerToActivate);
        }
    }
    
    @NiftyEventSubscriber(pattern="Player.*")
    public void allowDrag(String id, DraggableDragStartedEvent event)
    {
        Button checkButton;
        String draggable = event.getDraggable().getId();
        if ("Player1".equals(draggable))
        {
            checkButton = player1Dropper;
        }
        else if ("Player2".equals(draggable))
        {
            checkButton = player2Dropper;
        }
        else if ("Player3".equals(draggable))
        {
            checkButton = player3Dropper;
        }
        else if ("Player4".equals(draggable))
        {
            checkButton = player4Dropper;
        }
        else
        {
            System.out.println("An Error Has Occured");
            return;
        }
        checkButton.mobileRefresh();
        System.out.println("X is: " + currentX + ", Y is: " + currentY);
        System.out.println(checkButton.image);
        checkButton.printAll();
        if (checkButton.isClickable(currentX, currentY))
        {
            System.out.println("It is clickable.");
            return;
        }
        else
        {
            System.out.println("It is not clickable.");
            DraggableControl control = screen.findNiftyControl(draggable, DraggableControl.class);
            control.dragStop(); // Manually ends the Drag operation if the mouse isn't within the Dropper image
        }
    }
    
    @NiftyEventSubscriber(id="PlayerCatcher")
    public void playerNullify(String id, DroppableDroppedEvent event)
    {
        System.out.println("-----------------------------");
        System.out.println("Player not in Allowed Area!!!");
        System.out.println("-----------------------------");
        for (int i = 0; i < 3; i++) 
        {
            setPlayerType(event.getDraggable().getId()); 
        }
        Element element = screen.findElementByName(event.getDraggable().getId());
        element.markForMove(screen.findElementByName(event.getDraggable().getId() + "DropperSource"));
        
    }
    
    @NiftyEventSubscriber(id="TahuSelect")
    public void selectTahu(String id, DroppableDroppedEvent event)
    {
        selectCharacter(new Tahu(), event);
    }
    
    @NiftyEventSubscriber(id="KopakaSelect")
    public void selectKopaka(String id, DroppableDroppedEvent event)
    {
        selectCharacter(new Kopaka(), event);
    }
    
    @NiftyEventSubscriber(id="RandomSelect")
    public void selectRandom(String id, DroppableDroppedEvent event)
    {
        selectCharacter(new RandomCharacter(), event);
    }
     
    private void selectCharacter(PlayableCharacter character, DroppableDroppedEvent event)
    {
        System.out.println("----------------------------");
        System.out.println(event.getDraggable().getId());
        setPlayer(event.getDraggable().getId());
        
        // Need Random Announce
        characterSelectedAnnounce = new VoiceAudioNode(assetManager, "Sounds/Announcements/CharacterSelected/" + character.name + ".wav");
        characterSelectedAnnounce.play();
        
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
               currentPlayer.costume = cyclePlayerTeamCostume(currentPlayer, 0);
               NiftyImage image = nifty.getRenderEngine().createImage("Interface/CharacterSelect/" + currentPlayer.costume + ".png", false);
               nifty.getCurrentScreen().findElementByName(currentPlayerSelection + "Select").findElementByName(currentPlayerSelection + "Select").getRenderer(ImageRenderer.class).setImage(image);
               matchReady();
               return;
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
                System.out.println("Player has no team!");
                setPlayerCostume(currentPlayer.playerNumber);
            }
            if (currentPlayer.costume.name.equals("Standard"))
            {
                System.out.println("The player's team was " + currentPlayer.team);
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
        Element dropper = screen.findElementByName(player.playerNumber);
//        DraggableControl dropper = screen.findNiftyControl(player.playerNumber, DraggableControl.class);
        System.out.println("Element was initially enabled: " + dropper.isEnabled());
        if (player.playerType.equals("Null"))
        {
            dropper.show();
            dropper.enable();
            System.out.println("Element is currently enabled: " + dropper.isEnabled());
            NiftyImage image = nifty.getRenderEngine().createImage("Interface/PlayerSelect/" + player.playerNumber + "Select.png", false);
            nifty.getCurrentScreen().findElementByName(player.playerNumber + "Select").getRenderer(ImageRenderer.class).setImage(image);
            System.out.println(player.currentCharacter);
            return new CPUPlayer(player.playerNumber, player.currentCharacter, player.costume, player.team);
        }
        else if (player.playerType.equals("CPU"))
        {
            System.out.println("Player's team is " + player.team + "||||||||||||||");
            System.out.println(player.currentCharacter);
            return new HumanPlayer(player.playerNumber, player.currentCharacter, player.costume, player.team);
        }
        else if (player.playerType.equals("Human"))
        {
            dropper.hide();
            dropper.markForMove(screen.findElementByName(dropper.getId() + "DropperSource"));
            dropper.disable();
            System.out.println("Element " + dropper.getId() + " is currently enabled: " + dropper.isEnabled());
            System.out.println(player.playerNumber + "'s team was " + player.team);
            Team.removeMember(player.team, player);
            player.team = "";
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
            matchReady();
        }
        else if (teamType.equals("Team Match"))
        {
            setPlayerTeamCostume(player);
            matchReady();
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
            if (player1.currentCharacter == null)
            {
                return;
            }
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
            if (player2.currentCharacter == null)
            {
                return;
            }
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
            if (player3.currentCharacter == null)
            {
                return;
            }
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
            if (player4.currentCharacter == null)
            {
                return;
            }
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
            if (player1.currentCharacter == null)
            {
                return;
            }
            player1.costume = cyclePlayerTeam(player1);
            temp = player1;
        }
        else if (player.equals("Player2"))
        {
            if (player2.currentCharacter == null)
            {
                return;
            }
            player2.costume = cyclePlayerTeam(player2);
            temp = player2;
        }
        else if (player.equals("Player3"))
        {
            if (player3.currentCharacter == null)
            {
                return;
            }
            player3.costume = cyclePlayerTeam(player3);
            temp = player3;
        }
        else if (player.equals("Player4"))
        {
            if (player4.currentCharacter == null)
            {
                return;
            }
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
    
    private Costume cyclePlayerCostume(Player player)
    {
        System.out.println("Cycling Player Costume");
        if (player.playerType.equals("Null"))
        {
            return new Costume("Standard", player.currentCharacter.name);
        }
        PlayableCharacter temp = player.currentCharacter;
        
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
        
        System.out.println(oldTeam + " team's members were: " + Team.stringMembers(oldTeam) + " (old team)");
        System.out.println(player.team + " team's members were: " + Team.stringMembers(player.team) + " (new team)");
        Team.removeMember(oldTeam, player);
        Team.addMember(player.team, player);
        System.out.println(oldTeam + " team's members are now: " + Team.stringMembers(oldTeam) + " (old team)");
        System.out.println(player.team + " team's members are now: " + Team.stringMembers(player.team) + " (new team)");
        
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
        System.out.println("Team Type before: " + teamType + "<----------------------------");
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
          System.out.println("Team Type after: " + teamType + "<----------------------------");
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
          System.out.println("Team Type after: " + teamType + "<----------------------------");
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
        Element startMatchButton = screen.findElementByName("StartMatch");
        
        if (teamType.equals("Team Match"))
        {
            if (numberOfTeams > 1)
            {
                NiftyImage image = nifty.getRenderEngine().createImage("Interface/MatchReady.png", false);
                nifty.getCurrentScreen().findElementByName("StartMatch").getRenderer(ImageRenderer.class).setImage(image);
                startMatchButton.enable();
                return true;
            }
            else
            {
                NiftyImage image = nifty.getRenderEngine().createImage("Interface/MatchNotReady.png", false);
                nifty.getCurrentScreen().findElementByName("StartMatch").getRenderer(ImageRenderer.class).setImage(image);
                startMatchButton.disable();
                return false;  
            }
        }
        
        if (numberOfPlayers > 1)
        {
            NiftyImage image = nifty.getRenderEngine().createImage("Interface/MatchReady.png", false);
            nifty.getCurrentScreen().findElementByName("StartMatch").getRenderer(ImageRenderer.class).setImage(image);
            startMatchButton.enable();
            return true;
        }
        else 
        {
           NiftyImage image = nifty.getRenderEngine().createImage("Interface/MatchNotReady.png", false);
           nifty.getCurrentScreen().findElementByName("StartMatch").getRenderer(ImageRenderer.class).setImage(image);
           startMatchButton.disable();
           return false; 
        }     
    }
    
    @NiftyEventSubscriber(id="StartMatch")
    public void startMatch(String id, NiftyMousePrimaryClickedEvent event)
    {
        System.out.println("Working?");
        player1.getCharacter().initializeCharacter(app,player1);
        player2.getCharacter().initializeCharacter(app,player2);
        //player3.getCharacter().initializeCharacter(app,player3);
        //player4.getCharacter().initializeCharacter(app,player4);
        Match currentMatch = new Match(player1, player2, player3, player4);
        inGameState = new InGameState(currentMatch);
        stateManager.detach(mainMenu);
        stateManager.attach(inGameState);
    }
    
    
    public void printPlayerSelections()
    {
        if (currentPlayer != null)
        {
            System.out.println(currentPlayer.playerType + " Player (" + currentPlayer.playerNumber + ") as " + currentPlayer.currentCharacter + " with " + currentPlayer.costume + " costume.");
        }
        System.out.println(player1.playerType + " Player as " + player1.currentCharacter + " with " + player1.costume + " costume on " + player1.team + " Team.");
        System.out.println(player2.playerType + " Player as " + player2.currentCharacter + " with " + player2.costume + " costume on " + player2.team + " Team.");
        System.out.println(player3.playerType + " Player as " + player3.currentCharacter + " with " + player3.costume + " costume on " + player3.team + " Team.");
        System.out.println(player4.playerType + " Player as " + player4.currentCharacter + " with " + player4.costume + " costume on " + player4.team + " Team.");
        for (String color : Team.availableTeams.keySet()) 
        {
            System.out.println("Team " + color + " has members: " + Team.stringMembers(color));
        }
    }

    // Could change this part to just initialize all button images correctly
    private void clearPlayersAndTeams() 
    {
        System.out.println("Clearing Players and Teams");
        currentPlayer = null;
        player1 = new NullPlayer("Player1");
        player2 = new NullPlayer("Player2");
        player3 = new NullPlayer("Player3");
        player4 = new NullPlayer("Player4");
        Team.clearAllMembers();
    }
  
    public void goBack()
    {
        mainMenu = new MainMenu();
        mainMenu.initiate(app);
    }
    
    // TODO: Move Char Select Screen to a different file!

    private void resetVariables() 
    {
        System.out.println("Team Type Before: " + teamType);
        clearPlayersAndTeams();
        teamType = "Free For All";
        currentPlayerSelection = "";
        System.out.println("Team Type After: " + teamType);
    }
    
    private void disableAndHideDroppers()
    {
        Element player1Dropper = screen.findElementByName("Player1");
        Element player2Dropper = screen.findElementByName("Player2");
        Element player3Dropper = screen.findElementByName("Player3");
        Element player4Dropper = screen.findElementByName("Player4");
        player1Dropper.disable();
        player2Dropper.disable();
        player3Dropper.disable();
        player4Dropper.disable();
        player1Dropper.hide();
        player2Dropper.hide();
        player3Dropper.hide();
        player4Dropper.hide();
    }

    private Player getPlayerFromNumber(String player) 
    {
        Player temp = null;
        
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
            System.out.println("Player Does Not Exist.");
        }
        return temp;
    }
      
}

