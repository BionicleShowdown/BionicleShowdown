/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

import AdaptedControls.CenteredTextField;
import AdaptedControls.CenteredTextFieldChangedEvent;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.SizeValue;
import mygame.Main;

/**
 *
 * @author Inferno
 */
public class MatchSettingsMenu implements ScreenController
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
    private Match currentMatch;
    
    private RenderFont fieldFont;
//    private CharacterSelectMenu characterSelectMenu;
    
    int stock = 7;
    
    int hours = 0;
    int minutes = 5;
    int seconds = 0;
    
    
    public MatchSettingsMenu()
    {
        
    }
    
    public void initiate(Application app, Match currentMatch) 
    {
        this.app = (SimpleApplication) app;
        this.currentMatch = currentMatch;
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.audioRenderer = this.app.getAudioRenderer();
        this.guiViewPort = this.app.getViewPort();
        this.stateManager = this.app.getStateManager();
        nifty = Main.getNifty();
        nifty.registerScreenController(this);
        nifty.addXml("Interface/GUIS/MatchSettingsMenu.xml");
        nifty.gotoScreen("MatchSettingsMenu");          //Just for the first one, got to the start screen
        
        nifty.setDebugOptionPanelColors(false); // Leave it on true for now, so you can see the costume buttons.
    }

    

    public void onStartScreen() 
    {
        fieldFont = nifty.createFont("showdown-style/1942-report-32.fnt");
        screen.findNiftyControl("StockValue", TextField.class).setText("" + stock + "");
        screen.findNiftyControl("HourValue", TextField.class).setText("" + hours + "");
        screen.findNiftyControl("MinuteValue", TextField.class).setText("" + minutes + "");
        screen.findNiftyControl("SecondValue", TextField.class).setText("" + seconds + "");
    }

    public void onEndScreen() 
    {
        
    }
    
    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
        this.screen = screen;
    }
    
    @NiftyEventSubscriber(id="StockValue")
    public void changeStock(String id, CenteredTextFieldChangedEvent event)
    {
        String currentValue = event.getText();
        for (int i = 0; i < currentValue.length(); i++) 
        {
            if (!Character.isDigit(currentValue.charAt(i)))
            {
                event.getTextFieldControl().setMaxLength(i);
            }
        }
        
        event.getTextFieldControl().setMaxLength(3);
//        adjustFieldPad(event.getTextFieldControl().getId(), event.getText());
//        event.getTextFieldControl().getElement().findElementByName("StockValue#field").findElementByName("StockValue#field#text").getRenderer(TextRenderer.class).setFont(nifty.createFont("showdown-style/1942-report-16.fnt"));
        
        if (!currentValue.equals(""))
        {
            int newStock = new Integer(event.getTextFieldControl().getText());
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            System.out.println(newStock);
            currentMatch.setStock(newStock);
        }
        else
        {
            currentMatch.setStock(0); // Will be changed to something like -1 later, to reflect a lack of stock
        }
        
    }
    
    @NiftyEventSubscriber(id="HourValue")
    public void changeHour(String id, CenteredTextFieldChangedEvent event)
    {
        String currentValue = event.getText();
        for (int i = 0; i < currentValue.length(); i++) 
        {
            if (!Character.isDigit(currentValue.charAt(i)))
            {
                event.getTextFieldControl().setMaxLength(i);
            }
        }
        
        event.getTextFieldControl().setMaxLength(-1);
        
        if (!currentValue.equals(""))
        {
            hours = new Integer(event.getTextFieldControl().getText());
        }
        else
        {
            hours = 0;
        }
        setTime();
    }
    
    @NiftyEventSubscriber(id="MinuteValue")
    public void changeMinute(String id, CenteredTextFieldChangedEvent event)
    {
        String currentValue = event.getText();
        for (int i = 0; i < currentValue.length(); i++) 
        {
            if (!Character.isDigit(currentValue.charAt(i)))
            {
                event.getTextFieldControl().setMaxLength(i);
            }
        }
        
        event.getTextFieldControl().setMaxLength(2);
        
        if (!currentValue.equals(""))
        {
            minutes = new Integer(event.getTextFieldControl().getText());
        }
        else
        {
            minutes = 0;
        }
        
        if (minutes >= 60)
        {
            minutes = 59;
            event.getTextFieldControl().setText("59");
        }
        setTime();
    }
    
    @NiftyEventSubscriber(id="SecondValue")
    public void changeSecond(String id, CenteredTextFieldChangedEvent event)
    {
        String currentValue = event.getText();
        for (int i = 0; i < currentValue.length(); i++) 
        {
            if (!Character.isDigit(currentValue.charAt(i)))
            {
                event.getTextFieldControl().setMaxLength(i);
            }
        }
        
        event.getTextFieldControl().setMaxLength(2);
        
        if (!currentValue.equals(""))
        {
            seconds = new Integer(event.getTextFieldControl().getText());
        }
        else
        {
            seconds = 0;
        }
        
        if (seconds >= 60)
        {
            seconds = 59;
            event.getTextFieldControl().setText("59");
        }
        setTime();
    }
    
    public void goBack()
    {
        CharacterSelectMenu characterSelectMenu = new CharacterSelectMenu(currentMatch);
        characterSelectMenu.initiate(app);
    }

    private void setTime() 
    {
        int currentTime = (hours * 3600) + (minutes * 60) + seconds;
        currentMatch.setTime(currentTime);
    }
}
