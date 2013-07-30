/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

import Players.Player;
import de.lessvoid.nifty.elements.Element;
import java.util.HashMap;

/**
 *
 * @author Inferno
 */
public class Match 
{
    /* Match Players */
    private Player player1 = null;
    private Player player2 = null;
    private Player player3 = null;
    private Player player4 = null;
    
    /* Match Type */
    private int stock;
    private int time; // Time in seconds.
    private int widgets;
    
    private boolean isBeingBalanced = false;
    
    private MatchStatistics matchStatistics;
    
    // Extend into FFAMatch and TeamMatch?
    // Includes: Players, Teams, Gametype, Game settings, Statistics
    // Gets filled in with Statistics post-game, then gets passed to the stats menu
    
    /* Used for transfer back to CharSelectMenu */
    public int p1X;
    public int p1Y;
    public int p2X;
    public int p2Y;
    public int p3X;
    public int p3Y;
    public int p4X;
    public int p4Y;
    
    private String teamType;
    
    
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
    
    public boolean isBeingBalanced()
    {
        return isBeingBalanced;
    }
    
    public boolean isStockMatch()
    {
        if (stock != 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean isTimeMatch()
    {
        if (time != 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean isWidgetMatch()
    {
        if (widgets != 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public int getStock()
    {
        return stock;
    }
    
    public int getTime()
    {
        return time;
    }
    
    public int getWidgets()
    {
        return widgets;
    }
    
    public void setBalance(boolean balance)
    {
        isBeingBalanced = balance;
    }
    
    public void setStock(int newStock)
    {
        stock = newStock;
    }
    
    public void setTime(int newTime)
    {
        time = newTime;
    }
    
    public void setWidgets(int newWidgets)
    {
        widgets = newWidgets;
    }
    
    // Do same for Time and Widget
    
    // When match is over, clear the teams of all members (the teams are static)

    public void addDropperPositions(int p1X, int p1Y, int p2X, int p2Y, int p3X, int p3Y, int p4X, int p4Y) 
    {
        this.p1X = p1X;
        this.p1Y = p1Y;
        this.p2X = p2X;
        this.p2Y = p2Y;
        this.p3X = p3X;
        this.p3Y = p3Y;
        this.p4X = p4X;
        this.p4Y = p4Y;
    }
    
    public String getTeamType()
    {
        return teamType;
    }

    public void setTeamType(String teamType) 
    {
        this.teamType = teamType;
    }

    void reloadPlayers(Player player1, Player player2, Player player3, Player player4) 
    {
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.player4 = player4;
    }
}
