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
public class MatchSettings 
{
    /* Match Type */
    private int stock = 7;
    private int time = 300; // Time in seconds.
    private int widgets = 10000;
    
    private String teamType;
    
    
    public MatchSettings()
    {
        
    }
    
    public MatchSettings(int Stock, int time, int widgets)
    {
        
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
    
    public String getTeamType()
    {
        return teamType;
    }

    public void setTeamType(String teamType) 
    {
        this.teamType = teamType;
    }
}
