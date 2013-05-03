/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

/**
 *
 * @author Inferno
 */
class Player 
{
    String name = "";
    String playerType = "Null";
    String playerNumber = "";
    float handicap = 0;
    float gravity = 0; // A float so it's a percentage of gravity's effect
    PlayableCharacter preferredCharacter = null;
    PlayableCharacter currentCharacter = preferredCharacter;
//    Costumes costume = Costumes.Standard;
    Costume costume;
    String team = "";
    boolean isPlaying = false;
    
    
    
    
    Player()
    {
        
    }
    
    Player(String playerNumber, String playerType)
    {
        this.playerNumber = playerNumber;
        this.playerType = playerType;
    }
    
    Player(String playerNumber, String playerType, PlayableCharacter currentCharacter)
    {
        this.playerNumber = playerNumber;
        this.playerType = playerType;
        this.currentCharacter = currentCharacter;
    }
    
    
    
    public void setCharacter(PlayableCharacter character)
    {
        currentCharacter = character;
    }
    
    public PlayableCharacter getCharacter()
    {
        return currentCharacter;
    }
    
    public void setCostume(Costume costume)
    {
        this.costume = costume;
    }
    
    public boolean sameCostume(Player otherPlayer)
    {
        return this.costume.name.equals(otherPlayer.costume.name);
    }
    
    public boolean sameCharacter(Player otherPlayer)
    {
        String characterName = currentCharacter.name;
        String otherCharacterName = otherPlayer.currentCharacter.name;
        return characterName.equals(otherCharacterName);
    }
    
    public boolean sameCostumeAndCharacter(Player otherPlayer)
    {
        return this.costume.equals(otherPlayer.costume);
    }
    
    public boolean canPlay()
    {
        if ((currentCharacter != null) && (!playerType.equals("Null")))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean hasTeam()
    {
        if ((currentCharacter != null) && (!playerType.equals("Null") && (!team.equals(""))))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public String toString()
    {
        return playerNumber;
    }
}
