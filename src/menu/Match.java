/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

import Players.Player;

/**
 *
 * @author Inferno
 */
public class Match 
{
    private Player player1 = null;
    private Player player2 = null;
    private Player player3 = null;
    private Player player4 = null;
    // Extend into FFAMatch and TeamMatch?
    // Includes: Players, Teams, Gametype, Game settings, Statistics
    // Gets filled in with Statistics post-game, then gets passed to the stats menu
    
    public Match()
    {
        
    }
    
    public Match(Player player1, Player player2, Player player3, Player player4)
    {
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.player4 = player4;
    }
    
    
    public Player getPlayer1()
    {
        return player1;
    }
    
    public Player getPlayer2()
    {
        return player2;
    }
    
    public Player getPlayer3()
    {
        return player3;
    }
    
    public Player getPlayer4()
    {
        return player4;
    }
    // When match is over, clear the teams of all members (the teams are static)
}
