/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import Characters.PlayableCharacter;

/**
 *
 * @author Inferno
 */
public class Player 
{
    public String name = "";
    public String playerType = "Null";
    public String playerNumber = "";
    float handicap = 0;
    float gravity = 0; // A float so it's a percentage of gravity's effect
    public PlayableCharacter preferredCharacter = null;
    public PlayableCharacter currentCharacter = preferredCharacter;
//    Costumes costume = Costumes.Standard;
    public Costume costume;
    public String team = "";
    private boolean isPlaying = false;
    
    
    
    
    public Player()
    {
        
    }
    
    public Player(String playerNumber, String playerType)
    {
        this.playerNumber = playerNumber;
        this.playerType = playerType;
    }
    
    public Player(String playerNumber, String playerType, PlayableCharacter currentCharacter, Costume currentCostume, String team)
    {
        this.playerNumber = playerNumber;
        this.playerType = playerType;
        this.currentCharacter = currentCharacter;
        this.costume = currentCostume;
        this.team = team;
    }
    
    public Costume getCostume()
    {
        return costume;
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
    
    public boolean equals(Player otherPlayer)
    {
        if (otherPlayer == null)
        {
            return false;
        }
        return playerNumber.equals(otherPlayer.playerNumber);
    }
    
    @Override
    public String toString()
    {
        return playerNumber;
    }
}
