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
import de.lessvoid.nifty.controls.FocusGainedEvent;
import de.lessvoid.nifty.controls.FocusLostEvent;
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
    
    int stock;
    
    int hours;
    int minutes;
    int seconds;
    
    int widgets;
    
    static int MAX_STOCK = 999;
    static int MAX_TIME = 36000;
    static int MAX_WIDGETS = 1000000;
    
    public MatchSettingsMenu()
    {
        
    }
    
    public MatchSettingsMenu(Match currentMatch)
    {
        this.currentMatch = currentMatch;
    }
    
    public void initiate(Application app) 
    {
        System.out.println("Initiation Stock: " + currentMatch.getMatchSettings().getStock());
        this.app = (SimpleApplication) app;
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
        getStock(currentMatch.getMatchSettings().getStock()); // Sets the stock value from the MatchSettings (I tried this in the constructor, but it didn't work)
        getTime(currentMatch.getMatchSettings().getTime()); // Sets the time value from the MatchSettings
        getWidgets(currentMatch.getMatchSettings().getWidgets()); // Sets the widget value from the MatchSettings
        screen.findNiftyControl("StockValue", CenteredTextField.class).setText("" + stock + "");
        screen.findNiftyControl("HourValue", CenteredTextField.class).setText("" + hours + "");
        screen.findNiftyControl("MinuteValue", CenteredTextField.class).setText("" + minutes + "");
        screen.findNiftyControl("SecondValue", CenteredTextField.class).setText("" + seconds + "");
        screen.findNiftyControl("WidgetValue", CenteredTextField.class).setText("" + widgets + "");
        String stockString = "" + stock + "";
        screen.findNiftyControl("StockValue", CenteredTextField.class).setCursorPosition(stockString.length());
        cycleFocus();
    }

    public void onEndScreen() 
    {
        
    }
    
    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
        this.screen = screen;
    }
    
    public void adjustStock(String change)
    {
        int adjust = new Integer(change);
        if ((adjust < 0) && (stock <= 0))
        {
            return;
        }
        if ((adjust > 0) && (stock >= MAX_STOCK))
        {
            return;
        }
        int newStock = currentMatch.getMatchSettings().getStock() + adjust;
        screen.findNiftyControl("StockValue", CenteredTextField.class).setText("" + newStock + "");
        currentMatch.getMatchSettings().setStock(newStock);
        stock = currentMatch.getMatchSettings().getStock();
        String stockString = "" + stock + "";
        screen.findNiftyControl("StockValue", CenteredTextField.class).setCursorPosition(stockString.length());
        cycleFocus();
    }
    
    public void adjustTime(String change)
    {
        System.out.println("TIMES ARE CHANGING!");
        int adjust = new Integer(change);
        int time = currentMatch.getMatchSettings().getTime();
        if ((adjust < 0) && (time <= 0))
        {
            return;
        }
        if ((adjust > 0) && (time >= MAX_TIME))
        {
            return;
        }
        int newTime = time + adjust;
        currentMatch.getMatchSettings().setTime(newTime);
        getTime(newTime);
        screen.findNiftyControl("HourValue", CenteredTextField.class).setText("" + hours + "");
        screen.findNiftyControl("MinuteValue", CenteredTextField.class).setText("" + minutes + "");
        screen.findNiftyControl("SecondValue", CenteredTextField.class).setText("" + seconds + "");
        cycleFocus();
    }
    
    public void adjustWidgets(String change)
    {
        int adjust = new Integer(change);
        if ((adjust < 0) && (widgets <= 0))
        {
            return;
        }
        if ((adjust > 0) && (widgets >= MAX_WIDGETS))
        {
            return;
        }
        int newWidgets = currentMatch.getMatchSettings().getWidgets() + adjust;
        screen.findNiftyControl("WidgetValue", CenteredTextField.class).setText("" + newWidgets + "");
        currentMatch.getMatchSettings().setWidgets(newWidgets);
        widgets = currentMatch.getMatchSettings().getWidgets();
        String widgetString = "" + widgets + "";
        screen.findNiftyControl("WidgetValue", CenteredTextField.class).setCursorPosition(widgetString.length());
        cycleFocus();
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
            currentMatch.getMatchSettings().setStock(newStock);
        }
        else
        {
            currentMatch.getMatchSettings().setStock(0); // Will be changed to something like -1 later, to reflect a lack of stock
        }
        
        if (currentMatch.getMatchSettings().getStock() > MAX_STOCK)
        {
            currentMatch.getMatchSettings().setStock(MAX_STOCK);
            event.getTextFieldControl().setText("" + MAX_STOCK + "");
        }
        
        stock = currentMatch.getMatchSettings().getStock();
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
    
    @NiftyEventSubscriber(id="WidgetValue")
    public void changeWidget(String id, CenteredTextFieldChangedEvent event)
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
//        adjustFieldPad(event.getTextFieldControl().getId(), event.getText());
//        event.getTextFieldControl().getElement().findElementByName("StockValue#field").findElementByName("StockValue#field#text").getRenderer(TextRenderer.class).setFont(nifty.createFont("showdown-style/1942-report-16.fnt"));
        
        if (!currentValue.equals(""))
        {
            int newWidget = new Integer(event.getTextFieldControl().getText());
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            System.out.println(newWidget);
            currentMatch.getMatchSettings().setWidgets(newWidget);
        }
        else
        {
            currentMatch.getMatchSettings().setWidgets(0); // Will be changed to something like -1 later, to reflect a lack of stock
        }
        
        if (currentMatch.getMatchSettings().getWidgets() > MAX_WIDGETS)
        {
            currentMatch.getMatchSettings().setWidgets(MAX_WIDGETS);
            event.getTextFieldControl().setText("" + MAX_WIDGETS + "");
        }
        
        widgets = currentMatch.getMatchSettings().getWidgets();
    }
    
    public void goBack()
    {
        CharacterSelectMenu characterSelectMenu = new CharacterSelectMenu(currentMatch);
        characterSelectMenu.initiate(app);
    }

    private void setTime() 
    {
        int currentTime = (hours * 3600) + (minutes * 60) + seconds;
        currentMatch.getMatchSettings().setTime(currentTime);
    }

    private void getTime(int time) 
    {
        hours = (time / 3600);
        minutes = ((time - (hours * 3600)) / 60);
        seconds = ((time - (hours * 3600) - (minutes * 60)));
    }

    private void getStock(int matchStock) 
    {
        stock = matchStock;
    }

    private void getWidgets(int matchWidgets) 
    {
        widgets = matchWidgets;
    }
    
    
    
    
    @NiftyEventSubscriber(id="StockValue")
    public void checkStockOutOfFocus(String id, FocusLostEvent event)
    {
        String currentStock = screen.findNiftyControl(id, CenteredTextField.class).getText();
        
        if (currentStock.equals(""))
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("0");
        }
        if (currentStock.startsWith("0"))
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("" + stock + "");
        }
    }
    
    @NiftyEventSubscriber(id="StockValue")
    public void checkStockInFocus(String id, FocusGainedEvent event)
    {
        String stock = screen.findNiftyControl(id, CenteredTextField.class).getText();
        
        if (stock.equals("0"))
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("");
        }
    }
    
    @NiftyEventSubscriber(id="HourValue")
    public void checkHoursOutOfFocus(String id, FocusLostEvent event)
    {
        String currentHours = screen.findNiftyControl(id, CenteredTextField.class).getText();
        if (currentHours.equals("") || currentHours.equals("0"))
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("00");
        }
        else if (currentHours.startsWith("0"))
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("" + hours + "");
        }
    }
    
    @NiftyEventSubscriber(id="HourValue")
    public void checkHoursInFocus(String id, FocusGainedEvent event)
    {
        String currentHours = screen.findNiftyControl(id, CenteredTextField.class).getText();
        if (currentHours.equals("00"))
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("");
        }
    }
    
    @NiftyEventSubscriber(id="MinuteValue")
    public void checkMinutesOutOfFocus(String id, FocusLostEvent event)
    {
        String minutes = screen.findNiftyControl(id, CenteredTextField.class).getText();
        if (minutes.equals(""))
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("00");
        }
        if (minutes.length() == 1)
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("0" + minutes);
        }
    }
    
    @NiftyEventSubscriber(id="MinuteValue")
    public void checkMinutesInFocus(String id, FocusGainedEvent event)
    {
        String currentMinutes = screen.findNiftyControl(id, CenteredTextField.class).getText();
        if (currentMinutes.equals("00"))
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("");
        }
        else
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("" + minutes + "");
        }
    }
    
    @NiftyEventSubscriber(id="SecondValue")
    public void checkSecondsOutOfFocus(String id, FocusLostEvent event)
    {
        String seconds = screen.findNiftyControl(id, CenteredTextField.class).getText();
        if (seconds.equals(""))
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("00");
        }
        if (seconds.length() == 1)
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("0" + seconds);
        }
    }
    
    @NiftyEventSubscriber(id="SecondValue")
    public void checkSecondsInFocus(String id, FocusGainedEvent event)
    {
        String currentSeconds = screen.findNiftyControl(id, CenteredTextField.class).getText();
        if (currentSeconds.equals("00"))
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("");
        }
        else
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("" + seconds + "");
        }
    }
    
    @NiftyEventSubscriber(id="WidgetValue")
    public void checkWidgetsOutOfFocus(String id, FocusLostEvent event)
    {
        String currentWidgets = screen.findNiftyControl(id, CenteredTextField.class).getText();
        
        if (currentWidgets.equals(""))
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("0");
        }
        if (currentWidgets.startsWith("0"))
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("" + widgets + "");
        }
    }
    
    @NiftyEventSubscriber(id="WidgetValue")
    public void checkWidgetsInFocus(String id, FocusGainedEvent event)
    {
        String widgets = screen.findNiftyControl(id, CenteredTextField.class).getText();
        
        if (widgets.equals("0"))
        {
            screen.findNiftyControl(id, CenteredTextField.class).setText("");
        }
    }
    
    public void cycleFocus()
    {
        screen.findNiftyControl("StockValue", CenteredTextField.class).setFocus();
        screen.findNiftyControl("HourValue", CenteredTextField.class).setFocus();
        screen.findNiftyControl("MinuteValue", CenteredTextField.class).setFocus();
        screen.findNiftyControl("SecondValue", CenteredTextField.class).setFocus();
        screen.findNiftyControl("WidgetValue", CenteredTextField.class).setFocus();
        screen.findElementByName("BackButton").setFocusable(true);
        screen.findElementByName("BackButton").setFocus();
    }
}