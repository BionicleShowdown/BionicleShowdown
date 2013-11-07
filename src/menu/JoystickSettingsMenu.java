/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMouseEvent;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.image.ImageModeFactory;
import de.lessvoid.nifty.render.image.ImageModeHelper;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import java.util.List;
import mygame.CompoundInputManager;
import mygame.Main;

/**
 *
 * @author Inferno
 */
public class JoystickSettingsMenu implements ScreenController, KeyInputHandler, ActionListener
{
    private AssetManager assetManager;
    private SimpleApplication app;
    private Node rootNode;
    private Nifty nifty;
    private NiftyJmeDisplay niftyDisplay;
    private Screen screen;
    private static CompoundInputManager compoundManager;
    private AudioRenderer audioRenderer;
    private ViewPort guiViewPort;
    private AppStateManager stateManager;
    private PlayerControlMenu playerControlMenu;
    
    private static Joystick controller;
    
    private static DropDown controllerDropper;
    private static DropDown controlDropper;
    
    private static ListBox controllerDropperBox;
    private static ListBox controlDropperBox;
    
    private static TextField controllerTextField;
    private static TextField controlTextField;
    
    private static String player;
    private static int currentControlIndex;
    private static JoySettings currentSettingsSelected;
    
    private static Element backButtonElement;
    private static Element controllerDropperElement;
    private static Element controllerFieldElement;
    private static Element controllerRevertElement;
    private static Element controllerSaveElement;;
    private static Element controlDropperElement;
    private static Element controlFieldElement;
    private static Element controlRevertElement;
    private static Element controlSaveElement;
    private static Element defaultSchemeElement;
    private static Element restoreElement;
    
    private static TextRenderer controllerRevertTextRenderer;
    private static TextRenderer controllerSaveTextRenderer;
    private static TextRenderer controlRevertTextRenderer;
    private static TextRenderer controlSaveTextRenderer;
    private static TextRenderer defaultSchemeTextRenderer;
    private static TextRenderer restoreTextRenderer;
    
    private static ImageRenderer backButtonImageRenderer;
    private static ImageRenderer controllerDropperImageRenderer;
    private static ImageRenderer controllerFieldImageRenderer;
    private static ImageRenderer controllerRevertImageRenderer;
    private static ImageRenderer controllerSaveImageRenderer;
    private static ImageRenderer controlDropperImageRenderer;
    private static ImageRenderer controlFieldImageRenderer;
    private static ImageRenderer controlRevertImageRenderer;
    private static ImageRenderer controlSaveImageRenderer;
    private static ImageRenderer defaultSchemeImageRenderer;
    private static ImageRenderer restoreImageRenderer;
    
    
    private static NiftyImage backImageNormal;
    private static NiftyImage backImageHover;
    
    private static NiftyImage edgeImageNormal;
    private static NiftyImage edgeImageHover;
    
    private Color defaultTextColor = new Color("#FFFFFF");
    private Color hoverTextColor = new Color("#FFE500");
    
    private final byte nullID = -1;
    private final byte backButtonID = 0;
    private final byte controllerDropperID = 1;
    private final byte controllerFieldID = 2;
    private final byte controllerRevertID = 3;
    private final byte controllerSaveID = 4;
    private final byte controlDropperID = 5;
    private final byte controlFieldID = 6;
    private final byte controlRevertID = 7;
    private final byte controlSaveID = 8;
    private final byte defaultSchemeID = 9;
    private final byte restoreID = 10;
    
    private ButtonLayout currentLayout = new ButtonLayout(nullID, backButtonID, backButtonID, backButtonID, backButtonID);
    private ButtonLayout backButtonLayout = new ButtonLayout(backButtonID, backButtonID, backButtonID, backButtonID, controllerDropperID);
    private ButtonLayout controllerDropperLayout = new ButtonLayout(controllerDropperID, controllerDropperID, controllerDropperID, backButtonID, controllerFieldID);
    private ButtonLayout controllerFieldLayout = new ButtonLayout(controllerFieldID, controllerRevertID, controllerSaveID, controllerDropperID, controlDropperID);
    private ButtonLayout controllerRevertLayout = new ButtonLayout(controllerRevertID, controllerRevertID, controllerFieldID, controllerDropperID, controlDropperID);
    private ButtonLayout controllerSaveLayout = new ButtonLayout(controllerSaveID, controllerFieldID, controllerSaveID, controllerDropperID, controlDropperID);
    private ButtonLayout controlDropperLayout = new ButtonLayout(controlDropperID, controlDropperID, controlDropperID, controllerFieldID, controlFieldID);
    private ButtonLayout controlFieldLayout = new ButtonLayout(controlFieldID, controlRevertID, controlSaveID, controlDropperID, defaultSchemeID);
    private ButtonLayout controlRevertLayout = new ButtonLayout(controlRevertID, controlRevertID, controlFieldID, controlDropperID, defaultSchemeID);
    private ButtonLayout controlSaveLayout = new ButtonLayout(controlSaveID, controlFieldID, controlSaveID, controlDropperID, defaultSchemeID);
    private ButtonLayout defaultSchemeLayout = new ButtonLayout(defaultSchemeID, defaultSchemeID, defaultSchemeID, controlFieldID, restoreID);
    private ButtonLayout restoreLayout = new ButtonLayout(restoreID, restoreID, restoreID, defaultSchemeID, restoreID);
    
    
    private boolean backButtonResetDone = false;
    private boolean backButtonWasClickable = false;
    private boolean controllerDropperResetDone = false;
    private boolean controllerDropperWasClickable = false;
    private boolean controllerFieldResetDone = false;
    private boolean controllerFieldWasClickable = false;
    private boolean controllerRevertResetDone = false;
    private boolean controllerRevertWasClickable = false;
    private boolean controllerSaveResetDone = false;
    private boolean controllerSaveWasClickable = false;
    private boolean controlDropperResetDone = false;
    private boolean controlDropperWasClickable = false;
    private boolean controlFieldResetDone = false;
    private boolean controlFieldWasClickable = false;
    private boolean controlRevertResetDone = false;
    private boolean controlRevertWasClickable = false;
    private boolean controlSaveResetDone = false;
    private boolean controlSaveWasClickable = false;
    private boolean defaultSchemeResetDone = false;
    private boolean defaultSchemeWasClickable = false;
    private boolean restoreResetDone = false;
    private boolean restoreWasClickable = false;
    
    private boolean controllerDropperActive = false;
    private boolean controlDropperActive = false;
    
    private boolean keysUsed = false;
    
    
    public JoystickSettingsMenu(PlayerControlMenu playerControlMenu, String player)
    {
        this.playerControlMenu = playerControlMenu;
        JoystickSettingsMenu.player = player;
    }
    
    public void initiate(Application app)
    {
        this.app = (SimpleApplication) app;
        this.assetManager = this.app.getAssetManager();
        JoystickSettingsMenu.compoundManager = Main.getCompoundManager();
        this.audioRenderer = this.app.getAudioRenderer();
        this.guiViewPort = this.app.getViewPort();
        this.stateManager = this.app.getStateManager();
        this.niftyDisplay = Main.getNiftyDisplay();
        nifty = Main.getNifty();
        nifty.registerScreenController(this);
        nifty.addXml("Interface/GUIS/JoystickSettingsMenu.xml");
        nifty.gotoScreen("JoystickSettingsMenu");          //Just for the first one, go to the start screen
        nifty.setDebugOptionPanelColors(true); // Leave it on true for now
    }

    public void bind(Nifty nifty, Screen screen) 
    {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() 
    {
        currentControlIndex = 0;
        
        /* Panels */
        backButtonElement = screen.findElementByName("BackButtonPanel");
        controllerDropperElement = screen.findElementByName("ControllerNamePanel");
        controllerFieldElement = screen.findElementByName("ControllerNameFieldPanel");
        controllerRevertElement = screen.findElementByName("ControllerRevertPanel");
        controllerSaveElement = screen.findElementByName("ControllerSavePanel");
        controlDropperElement = screen.findElementByName("ControlNamePanel");
        controlFieldElement = screen.findElementByName("ControlNameFieldPanel");
        controlRevertElement = screen.findElementByName("ControlRevertPanel");
        controlSaveElement = screen.findElementByName("ControlSavePanel");
        defaultSchemeElement = screen.findElementByName("ControlSchemePanel");
        restoreElement = screen.findElementByName("RestoreDefaultsPanel");
        
        /* Image Renderers */
        backButtonImageRenderer = screen.findElementByName("BackButtonImage").getRenderer(ImageRenderer.class);
        controllerDropperImageRenderer = screen.findElementByName("ControllerNameImage").getRenderer(ImageRenderer.class);
        controllerFieldImageRenderer = screen.findElementByName("ControllerNameFieldImage").getRenderer(ImageRenderer.class);
        controllerRevertImageRenderer = screen.findElementByName("ControllerRevertImage").getRenderer(ImageRenderer.class);
        controllerSaveImageRenderer = screen.findElementByName("ControllerSaveImage").getRenderer(ImageRenderer.class);
        controlDropperImageRenderer = screen.findElementByName("ControlNameImage").getRenderer(ImageRenderer.class);
        controlFieldImageRenderer = screen.findElementByName("ControlNameFieldImage").getRenderer(ImageRenderer.class);
        controlRevertImageRenderer = screen.findElementByName("ControlRevertImage").getRenderer(ImageRenderer.class);
        controlSaveImageRenderer = screen.findElementByName("ControlSaveImage").getRenderer(ImageRenderer.class);
        defaultSchemeImageRenderer = screen.findElementByName("ControlSchemeImage").getRenderer(ImageRenderer.class);
        restoreImageRenderer = screen.findElementByName("RestoreDefaultsImage").getRenderer(ImageRenderer.class);
    
        /* Text Renderers */
        controllerRevertTextRenderer = screen.findElementByName("ControllerRevertText").getRenderer(TextRenderer.class);
        controllerSaveTextRenderer = screen.findElementByName("ControllerSaveText").getRenderer(TextRenderer.class);
        controlRevertTextRenderer = screen.findElementByName("ControlRevertText").getRenderer(TextRenderer.class);
        controlSaveTextRenderer = screen.findElementByName("ControlSaveText").getRenderer(TextRenderer.class);
        defaultSchemeTextRenderer = screen.findElementByName("ControlSchemeText").getRenderer(TextRenderer.class);
        restoreTextRenderer = screen.findElementByName("RestoreDefaultsText").getRenderer(TextRenderer.class);
        
        
        backImageNormal = nifty.createImage("Interface/General/BackButton.png", false);
        backImageHover = nifty.createImage("Interface/General/BackButtonHover.png", false);
        
        edgeImageNormal = nifty.createImage("Interface/General/ButtonEdgeStandard.png", false);
        edgeImageHover = nifty.createImage("Interface/General/ButtonEdgeHover.png", false);
        
        String areaProviderProperty = ImageModeHelper.getAreaProviderProperty("resize:14,2,14,14,14,2,14,2,14,2,14,14");
        String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty("resize:14,2,14,14,14,2,14,2,14,2,14,14");
        
        edgeImageNormal.setImageMode(ImageModeFactory.getSharedInstance().createImageMode(areaProviderProperty, renderStrategyProperty));
        edgeImageHover.setImageMode(ImageModeFactory.getSharedInstance().createImageMode(areaProviderProperty, renderStrategyProperty));
        
        
        controllerDropper = screen.findNiftyControl("ControllerName", DropDown.class);
        controlDropper = screen.findNiftyControl("ControlName", DropDown.class);
        
        // This will need to change if another layer is added, I think.
        controllerDropperBox = screen.getRootElement().getElements().get(2).findElementByName("#panel").findNiftyControl("#listBox", ListBox.class);
        controlDropperBox = screen.getRootElement().getElements().get(3).findElementByName("#panel").findNiftyControl("#listBox", ListBox.class);
        
        controllerTextField = screen.findNiftyControl("ControllerNameField", TextField.class);
        controlTextField = screen.findNiftyControl("ControlNameField", TextField.class);
        
        controllerDropper.addAllItems(JoystickRegistry.getAllJoySettings());
        
        getController();
        
        if (controller != null)
        {
            controllerDropper.selectItem(JoystickRegistry.getJoySettings(controller.getName()));
            controllerTextField.setText(JoystickRegistry.getJoySettings(controller.getName()).getJoyName());
        }
//        System.out.println("Control Names: " + JoystickRegistry.getJoySettings(controller.getName()).getControlNames().toArray().length);
        controlDropper.clear();
        controlDropper.addAllItems(JoystickRegistry.getJoySettings(controller.getName()).getControlNames());
        System.out.println("Control Names Num: " + controlDropper.getItems().toArray().length);
        currentSettingsSelected = (JoySettings) controllerDropper.getSelection();
        
        currentLayout = new ButtonLayout(nullID, backButtonID, backButtonID, backButtonID, backButtonID);
        
        initJoy();
//        backButtonElement.setFocus();
    }

    public void onEndScreen() 
    {
        /* Make sure to delete mappings so they don't seep through to the next menu.
           Do NOT clear Mappings, as that will also remove the Escape to exit. */
        
            compoundManager.deleteMapping("Accept");
            compoundManager.deleteMapping("Right");
            compoundManager.deleteMapping("Left");
            compoundManager.deleteMapping("Up");
            compoundManager.deleteMapping("Down");
    }
    
    public void goBack()
    {
        System.out.println("GOING BACK NOW");
        playerControlMenu.initiate(app);
    }
    
    
    public void getController()
    {
        if ("Player 1".equals(player))
        {
            if (Main.joysticks.length >= 1)
            {
                controller = Main.joysticks[0];
            }
        }
        else if ("Player 2".equals(player))
        {
            if (Main.joysticks.length >= 2)
            {
                controller = Main.joysticks[1];
            }
        }
        else if ("Player 3".equals(player))
        {
            if (Main.joysticks.length >= 3)
            {
                controller = Main.joysticks[2];
            }
        }
        else if ("Player 4".equals(player))
        {
            if (Main.joysticks.length >= 4)
            {
                controller = Main.joysticks[3];
            }
        }
        else
        {
            controller = null;
        }
    }
    
    @NiftyEventSubscriber(id="ControllerName")
    public void onControllerChange(String id, DropDownSelectionChangedEvent event)
    {
        if (event.getSelection() == null)
        {
            return;
        }
        
        // If Controller Changed, Reload Control Dropper
        // If not, do nothing to Control Dropper
        
        JoySettings settings = (JoySettings) event.getSelection();
        
        if (settings.equals(currentSettingsSelected))
        {
            System.out.println("No Changes: " + currentSettingsSelected);
            return;
        }
        
        currentSettingsSelected = settings;
        
        controlDropper.clear();
        controlDropper.addAllItems(JoystickRegistry.getJoySettings(settings.getOriginalName()).getControlNames());
        controllerTextField.setText(settings.getJoyName());
        controlDropperBox.selectItemByIndex(0);
        controlDropperBox.setFocusItemByIndex(0);
        controlDropperBox.showItemByIndex(0);
//        JoySettings settings = (JoySettings) event.getSelection();
//        
//        if (!settings.equals(currentSettingsSelected))
//        {
//            System.out.println("Switched Controllers");
//            currentControlIndex = 0;
//        }
//        
//        currentSettingsSelected = settings;
//        
//        System.out.println(settings);
//        controlDropper.clear();
//        controlDropper.addAllItems(JoystickRegistry.getJoySettings(settings.getOriginalName()).getControlNames());
//        controllerTextField.setText(settings.getJoyName());
//        
////        if (currentControlIndex >= controlDropper.itemCount())
////        {
////            System.out.println("More than there is.");
////            currentControlIndex = 0;
////        }
//        
//        controlDropper.selectItemByIndex(currentControlIndex);
//        controlDropperBox.setFocusItemByIndex(currentControlIndex);
//        controlDropperBox.showItemByIndex(currentControlIndex);
    }
    
    public void saveControllerName()
    {
        JoySettings settings = (JoySettings) controllerDropper.getSelection();
        JoystickRegistry.getJoySettings(settings.getOriginalName()).setJoyName(controllerTextField.getText());
//        controllerDropper.clear();
//        controllerDropper.addAllItems(JoystickRegistry.getAllJoySettings());
        
        int index = controllerDropper.getSelectedIndex();
        controllerDropper.removeItemByIndex(index);
        controllerDropper.insertItem(settings, index);
        
        controllerDropper.selectItem(settings);
        controllerDropperBox.setFocusItem(settings);
        controllerDropperBox.showItem(settings);
        System.out.println("Index of item: " + controlDropperBox.getFocusItemIndex());
        controlDropper.selectItemByIndex(currentControlIndex);
        controlDropperBox.setFocusItemByIndex(currentControlIndex);
        controlDropperBox.showItemByIndex(currentControlIndex);
    }
    
    public void revertControllerName()
    {
        controllerTextField.setText(controllerDropper.getSelection().toString());
    }
    
    
    @NiftyEventSubscriber(id="ControlName")
    public void onControlChange(String id, DropDownSelectionChangedEvent event)
    {
        if (event.getSelection() == null)
        {
            return;
        }
        controlTextField.setText(event.getSelection().toString());
    }
    
    public void saveControlName()
    {
        currentControlIndex = controlDropper.getSelectedIndex();
        
        String contents = controlTextField.getText();
        JoySettings settings = (JoySettings) controllerDropper.getSelection();
        settings.setControlName(currentControlIndex, contents);
        
        controlDropper.removeItemByIndex(currentControlIndex);
        controlDropper.insertItem(contents, currentControlIndex);
//        controlDropper.clear();
//        controlDropper.addAllItems(settings.getControlNames());
        controlDropper.selectItemByIndex(currentControlIndex);
        controlDropperBox.setFocusItemByIndex(currentControlIndex);
        controlDropperBox.showItemByIndex(currentControlIndex);
    }
    
    public void revertControlName()
    {
        controlTextField.setText(controlDropper.getSelection().toString());
    }
    
    public void restoreDefaults()
    {
        JoySettings settings = (JoySettings) controllerDropper.getSelection();
        settings.resetSettings();
        
        int index = controllerDropper.getSelectedIndex();
        controllerDropper.removeItemByIndex(index);
        controllerDropper.insertItem(settings, index);
        
        currentSettingsSelected = settings;
        
        controlDropper.clear();
        controlDropper.addAllItems(JoystickRegistry.getJoySettings(settings.getOriginalName()).getControlNames());
        controllerTextField.setText(settings.getJoyName());
        controlDropperBox.selectItemByIndex(0);
        controlDropperBox.setFocusItemByIndex(0);
        controlDropperBox.showItemByIndex(0);
    }
    
    
    
    /* 0
     *     1
     *  3  2  4
     *  
     *     5
     *  7  6  8
     * 
     *     9
     *    10
     */
    
    
    public void adaptActive(byte newButton)
    {
        if (!keysUsed && (currentLayout.Identity() == newButton))
        {
            switch (newButton)
            {
                case backButtonID: 
                {
                    currentLayout = backButtonLayout;
                    backButtonImageRenderer.setImage(backImageHover);
                    backButtonElement.setFocus();
                    backButtonWasClickable = true;
                    break;
                }
                case controllerDropperID:
                {
                    currentLayout = controllerDropperLayout;
                    controllerDropperImageRenderer.setImage(edgeImageHover);
//                    controllerDropper.setFocus();
                    controllerDropperWasClickable = true;
                    break;
                }
                case controllerFieldID:
                {
                    currentLayout = controllerFieldLayout;
                    controllerFieldImageRenderer.setImage(edgeImageHover);
//                    controllerTextField.setFocus();
                    controllerFieldWasClickable = true;
                    break;
                }
                case controllerRevertID:
                {
                    currentLayout = controllerRevertLayout;
                    controllerRevertImageRenderer.setImage(edgeImageHover);
                    controllerRevertTextRenderer.setColor(hoverTextColor);
                    controllerRevertElement.setFocus();
                    controllerRevertWasClickable = true;
                    break;
                }
                case controllerSaveID:
                {
                    currentLayout = controllerSaveLayout;
                    controllerSaveImageRenderer.setImage(edgeImageHover);
                    controllerSaveTextRenderer.setColor(hoverTextColor);
                    controllerSaveElement.setFocus();
                    controllerSaveWasClickable = true;
                    break;
                }
                case controlDropperID:
                {
                    currentLayout = controlDropperLayout;
                    controlDropperImageRenderer.setImage(edgeImageHover);
//                    controlDropper.setFocus();
                    controlDropperWasClickable = true;
                    break;
                }
                case controlFieldID:
                {
                    currentLayout = controlFieldLayout;
                    controlFieldImageRenderer.setImage(edgeImageHover);
//                    controlTextField.setFocus();
                    controlFieldWasClickable = true;
                    break;
                }
                case controlRevertID:
                {
                    currentLayout = controlRevertLayout;
                    controlRevertImageRenderer.setImage(edgeImageHover);
                    controlRevertTextRenderer.setColor(hoverTextColor);
                    controlRevertElement.setFocus();
                    controlRevertWasClickable = true;
                    break;
                }
                case controlSaveID:
                {
                    currentLayout = controlSaveLayout;
                    controlSaveImageRenderer.setImage(edgeImageHover);
                    controlSaveTextRenderer.setColor(hoverTextColor);
                    controlSaveElement.setFocus();
                    controlSaveWasClickable = true;
                    break;
                } 
                case defaultSchemeID:
                {
                    currentLayout = defaultSchemeLayout;
                    defaultSchemeImageRenderer.setImage(edgeImageHover);
                    defaultSchemeTextRenderer.setColor(hoverTextColor);
                    defaultSchemeElement.setFocus();
                    defaultSchemeWasClickable = true;
                    break;
                }
                case restoreID:
                {
                    currentLayout = restoreLayout;
                    restoreImageRenderer.setImage(edgeImageHover);
                    restoreTextRenderer.setColor(hoverTextColor);
                    restoreElement.setFocus();
                    restoreWasClickable = true;
                    break;
                }
                default:
                {
                    
                }
            }
        }
        if (newButton == currentLayout.Identity())
        {
            return;
        }
        switch (currentLayout.Identity())
        {
            case backButtonID: 
            {
                currentLayout = backButtonLayout;
                backButtonImageRenderer.setImage(backImageNormal);
                backButtonWasClickable = false;
                break;
            }
            case controllerDropperID:
            {
                currentLayout = controllerDropperLayout;
                controllerDropperImageRenderer.setImage(edgeImageNormal);
                controllerDropperWasClickable = false;
                break;
            }
            case controllerFieldID:
            {
                currentLayout = controllerFieldLayout;
                controllerFieldImageRenderer.setImage(edgeImageNormal);
                controllerFieldWasClickable = false;
                break;
            }
            case controllerRevertID:
            {
                currentLayout = controllerRevertLayout;
                controllerRevertImageRenderer.setImage(edgeImageNormal);
                controllerRevertTextRenderer.setColor(defaultTextColor);
                controllerRevertWasClickable = false;
                break;
            }
            case controllerSaveID:
            {
                currentLayout = controllerSaveLayout;
                controllerSaveImageRenderer.setImage(edgeImageNormal);
                controllerSaveTextRenderer.setColor(defaultTextColor);
                controllerSaveWasClickable = false;
                break;
            }
            case controlDropperID:
            {
                currentLayout = controlDropperLayout;
                controlDropperImageRenderer.setImage(edgeImageNormal);
                controlDropperWasClickable = false;
                break;
            }
            case controlFieldID:
            {
                currentLayout = controlFieldLayout;
                controlFieldImageRenderer.setImage(edgeImageNormal);
                controlFieldWasClickable = false;
                break;
            }
            case controlRevertID:
            {
                currentLayout = controlRevertLayout;
                controlRevertImageRenderer.setImage(edgeImageNormal);
                controlRevertTextRenderer.setColor(defaultTextColor);
                controlRevertWasClickable = false;
                break;
            }
            case controlSaveID:
            {
                currentLayout = controlSaveLayout;
                controlSaveImageRenderer.setImage(edgeImageNormal);
                controlSaveTextRenderer.setColor(defaultTextColor);
                controlSaveWasClickable = false;
                break;
            }    
            case defaultSchemeID:
            {
                currentLayout = defaultSchemeLayout;
                defaultSchemeImageRenderer.setImage(edgeImageNormal);
                defaultSchemeTextRenderer.setColor(defaultTextColor);
                defaultSchemeWasClickable = false;
                break;
            }
            case restoreID:
            {
                currentLayout = restoreLayout;
                restoreImageRenderer.setImage(edgeImageNormal);
                restoreTextRenderer.setColor(defaultTextColor);
                restoreWasClickable = false;
                break;
            }
            default:
            {
                
            }
        }
        switch (newButton)
        {
            case backButtonID: 
            {
                currentLayout = backButtonLayout;
                backButtonImageRenderer.setImage(backImageHover);
                backButtonElement.setFocus();
                backButtonWasClickable = true;
                break;
            }
            case controllerDropperID:
            {
                currentLayout = controllerDropperLayout;
                controllerDropperImageRenderer.setImage(edgeImageHover);
//                controllerDropper.setFocus();
                controllerDropperWasClickable = true;
                break;
            }
            case controllerFieldID:
            {
                currentLayout = controllerFieldLayout;
                controllerFieldImageRenderer.setImage(edgeImageHover);
//                controllerTextField.setFocus();
                controllerFieldWasClickable = true;
                break;
            }
            case controllerRevertID:
            {
                currentLayout = controllerRevertLayout;
                controllerRevertImageRenderer.setImage(edgeImageHover);
                controllerRevertTextRenderer.setColor(hoverTextColor);
                controllerRevertElement.setFocus();
                controllerRevertWasClickable = true;
                break;
            }
            case controllerSaveID:
            {
                currentLayout = controllerSaveLayout;
                controllerSaveImageRenderer.setImage(edgeImageHover);
                controllerSaveTextRenderer.setColor(hoverTextColor);
                controllerSaveElement.setFocus();
                controllerSaveWasClickable = true;
                break;
            }
            case controlDropperID:
            {
                currentLayout = controlDropperLayout;
                controlDropperImageRenderer.setImage(edgeImageHover);
//                controlDropper.setFocus();
                controlDropperWasClickable = true;
                break;
            }
            case controlFieldID:
            {
                currentLayout = controlFieldLayout;
                controlFieldImageRenderer.setImage(edgeImageHover);
//                controlTextField.setFocus();
                controlFieldWasClickable = true;
                break;
            }
            case controlRevertID:
            {
                currentLayout = controlRevertLayout;
                controlRevertImageRenderer.setImage(edgeImageHover);
                controlRevertTextRenderer.setColor(hoverTextColor);
                controlRevertElement.setFocus();
                controlRevertWasClickable = true;
                break;
            }
            case controlSaveID:
            {
                currentLayout = controlSaveLayout;
                controlSaveImageRenderer.setImage(edgeImageHover);
                controlSaveTextRenderer.setColor(hoverTextColor);
                controlSaveElement.setFocus();
                controlSaveWasClickable = true;
                break;
            }    
            case defaultSchemeID:
            {
                currentLayout = defaultSchemeLayout;
                defaultSchemeImageRenderer.setImage(edgeImageHover);
                defaultSchemeTextRenderer.setColor(hoverTextColor);
                defaultSchemeElement.setFocus();
                defaultSchemeWasClickable = true;
                break;
            }
            case restoreID:
            {
                currentLayout = restoreLayout;
                restoreImageRenderer.setImage(edgeImageHover);
                restoreTextRenderer.setColor(hoverTextColor);
                restoreElement.setFocus();
                restoreWasClickable = true;
                break;
            }
            default:
            {
                
            }
        }
    }
    
    public void activateActive(byte newButton)
    {
        switch (currentLayout.Identity())
        {
            case backButtonID:
            {
                goBack();
                break;
            }
            case controllerDropperID:
            {
//                controllerDropper.setFocus();
//                System.out.println("Element of interest: " + screen.getFocusHandler().getKeyboardFocusElement());
                if (screen.getFocusHandler().getKeyboardFocusElement() == controllerDropper.getElement())
                {
                    controllerDropperActive = false;
                    System.out.println("They are the same!");
                    backButtonElement.setFocus();
                }
                else
                {
                    System.out.println("changed-ed-ed");
                    controllerDropperActive = true;
                    controllerDropper.setFocus();
                }
                break;
            }
            case controllerFieldID:
            {
//                controllerTextField.setFocus();
                if (screen.getFocusHandler().getKeyboardFocusElement() == controllerTextField.getElement())
                {
                    System.out.println("They are the same!");
                    backButtonElement.setFocus();
                }
                else
                {
                    controllerTextField.setFocus();
                }
                break;
            }
            case controllerRevertID:
            {
                revertControllerName();
                break;
            }
            case controllerSaveID:
            {
                saveControllerName();
                break;
            }
            case controlDropperID:
            {
                if (screen.getFocusHandler().getKeyboardFocusElement() == controlDropper.getElement())
                {
                    controlDropperActive = false;
                    System.out.println("They are the same!");
                    backButtonElement.setFocus();
                }
                else
                {
                    controlDropperActive = true;
                    controlDropper.setFocus();
                }
                break;
            }
            case controlFieldID:
            {
                if (screen.getFocusHandler().getKeyboardFocusElement() == controlTextField.getElement())
                {
                    System.out.println("They are the same!");
                    backButtonElement.setFocus();
                }
                else
                {
                    controlTextField.setFocus();
                }
                break;
            }
            case controlRevertID:
            {
                revertControlName();
                break;
            }
            case controlSaveID:
            {
                saveControlName();
                break;
            }
            case defaultSchemeID:
            {
                break;
            }
            case restoreID:
            {
                restoreDefaults();
                break;
            }
            default:
            {
                
            }
        }
    }
    
    public boolean keyEvent(NiftyInputEvent event) 
    {
        if (event == NiftyInputEvent.Activate)
        {
            System.out.println("ACTIVATE!!!");
            if (keysUsed)
            {
                activateActive(currentLayout.Identity());
            }
            return true;
        }
        else if (event == NiftyInputEvent.Backspace)
        {
            goBack();
        }
        else if (event == NiftyInputEvent.MoveCursorLeft)
        {
            System.out.println("Left");
            if (!keysUsed)
            {
                adaptActive(currentLayout.Identity());
                keysUsed = true;
            }
            else
            {
                adaptActive(currentLayout.Left());
            }
            return true;
        }
        else if (event == NiftyInputEvent.MoveCursorRight)
        {
            System.out.println("Right");
            if (!keysUsed)
            {
                adaptActive(currentLayout.Identity());
                keysUsed = true;
            }
            else
            {
                adaptActive(currentLayout.Right());
            }
            return true;
        }
        else if (event == NiftyInputEvent.MoveCursorUp)
        {
            System.out.println("Up");
            if (controllerDropperActive == true)
            {
                controllerDropper.selectItemByIndex(controllerDropper.getSelectedIndex() - 1);
                return true;
            }
            if (controlDropperActive == true)
            {
                controlDropper.selectItemByIndex(controlDropper.getSelectedIndex() - 1);
                return true;
            }
            if (!keysUsed)
            {
                adaptActive(currentLayout.Identity());
                keysUsed = true;
            }
            else
            {
                adaptActive(currentLayout.Up());
            }
            return true;
        }
        else if (event == NiftyInputEvent.MoveCursorDown)
        {
            System.out.println("Down");
            if (controllerDropperActive == true)
            {
                controllerDropper.selectItemByIndex(controllerDropper.getSelectedIndex() + 1);
                return true;
            }
            if (controlDropperActive == true)
            {
                controlDropper.selectItemByIndex(controlDropper.getSelectedIndex() + 1);
                return true;
            }
            if (!keysUsed)
            {
                adaptActive(currentLayout.Identity());
                keysUsed = true;
            }
            else
            {
                adaptActive(currentLayout.Down());
            }
            return true;
        }
        return false;
    }
    
    @NiftyEventSubscriber(id="MouseCatcher")
    public void buttonCaller(String id, NiftyMouseEvent event)
    {
        int x = event.getMouseX();
        int y = event.getMouseY();
        boolean wasClick = event.isButton0Down();
        
        if (wasClick)
        {
            keysUsed = false;
            backButtonResetDone = false;
            controllerDropperResetDone = false;
            controllerFieldResetDone = false;
            controllerRevertResetDone = false;
            controllerSaveResetDone = false;
            controlDropperResetDone = false;
            controlFieldResetDone = false;
            controlRevertResetDone = false;
            controlSaveResetDone = false;
            defaultSchemeResetDone = false;
            restoreResetDone = false;
            
            controllerDropperActive = false;
            controlDropperActive = false;
            
            backButtonElement.setFocus();
        }
        
        onBackButtonHover(backButtonElement.isMouseInsideElement(x, y), wasClick);
        onControllerDropperHover(controllerDropperElement.isMouseInsideElement(x, y), wasClick);
        onControllerFieldHover(controllerFieldElement.isMouseInsideElement(x, y), wasClick);
        onControllerRevertHover(controllerRevertElement.isMouseInsideElement(x, y), wasClick);
        onControllerSaveHover(controllerSaveElement.isMouseInsideElement(x, y), wasClick);
        
        onControlDropperHover(controlDropperElement.isMouseInsideElement(x, y), wasClick);
        onControlFieldHover(controlFieldElement.isMouseInsideElement(x, y), wasClick);
        onControlRevertHover(controlRevertElement.isMouseInsideElement(x, y), wasClick);
        onControlSaveHover(controlSaveElement.isMouseInsideElement(x, y), wasClick);
        
        onDefaultSchemeHover(defaultSchemeElement.isMouseInsideElement(x, y), wasClick);
        onRestoreHover(restoreElement.isMouseInsideElement(x, y), wasClick);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Hover Methods">
    public void onBackButtonHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                goBack();
                return;
            }
            if (!backButtonWasClickable)
            {
                backButtonImageRenderer.setImage(backImageHover);
                backButtonWasClickable = true;
                backButtonResetDone = false;
                adaptActive(backButtonID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!backButtonResetDone)
        {
            backButtonImageRenderer.setImage(backImageNormal);
            backButtonWasClickable = false;
            backButtonResetDone = true;
        }
    }
    
    public void onControllerDropperHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                controllerDropper.setFocus();
                activateActive(controllerDropperID);
                return;
            }
            if (!controllerDropperWasClickable)
            {
                controllerDropper.setFocus();
                controllerDropperImageRenderer.setImage(edgeImageHover);
                controllerDropperWasClickable = true;
                controllerDropperResetDone = false;
                adaptActive(controllerDropperID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!controllerDropperResetDone)
        {
            controllerDropperImageRenderer.setImage(edgeImageNormal);
            if (screen.getFocusHandler().getKeyboardFocusElement() == controllerDropper.getElement())
            {
                backButtonElement.setFocus();
            }
            controllerDropperWasClickable = false;
            controllerDropperResetDone = true;
        }
    }
    
    public void onControllerFieldHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                controllerTextField.setFocus();
                activateActive(controllerFieldID);
                return;
            }
            if (!controllerFieldWasClickable)
            {
                controllerTextField.setFocus();
                controllerFieldImageRenderer.setImage(edgeImageHover);
                controllerFieldWasClickable = true;
                controllerFieldResetDone = false;
                adaptActive(controllerFieldID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!controllerFieldResetDone)
        {
            controllerFieldImageRenderer.setImage(edgeImageNormal);
            if (screen.getFocusHandler().getKeyboardFocusElement() == controllerTextField.getElement())
            {
                backButtonElement.setFocus();
            }
            controllerFieldWasClickable = false;
            controllerFieldResetDone = true;
        }
    }
    
    public void onControllerRevertHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                activateActive(controllerRevertID);
                return;
            }
            if (!controllerRevertWasClickable)
            {
                controllerRevertTextRenderer.setColor(hoverTextColor);
                controllerRevertImageRenderer.setImage(edgeImageHover);
                controllerRevertWasClickable = true;
                controllerRevertResetDone = false;
                adaptActive(controllerRevertID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!controllerRevertResetDone)
        {
            controllerRevertTextRenderer.setColor(defaultTextColor);
            controllerRevertImageRenderer.setImage(edgeImageNormal);
            controllerRevertWasClickable = false;
            controllerRevertResetDone = true;
        }
    }
    
    public void onControllerSaveHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                activateActive(controllerSaveID);
                return;
            }
            if (!controllerSaveWasClickable)
            {
                controllerSaveTextRenderer.setColor(hoverTextColor);
                controllerSaveImageRenderer.setImage(edgeImageHover);
                controllerSaveWasClickable = true;
                controllerSaveResetDone = false;
                adaptActive(controllerSaveID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!controllerSaveResetDone)
        {
            controllerSaveTextRenderer.setColor(defaultTextColor);
            controllerSaveImageRenderer.setImage(edgeImageNormal);
            controllerSaveWasClickable = false;
            controllerSaveResetDone = true;
        }
    }
    
    public void onControlDropperHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                controlDropper.setFocus();
                activateActive(controlDropperID);
                return;
            }
            if (!controlDropperWasClickable)
            {
                controlDropper.setFocus();
                controlDropperImageRenderer.setImage(edgeImageHover);
                controlDropperWasClickable = true;
                controlDropperResetDone = false;
                adaptActive(controlDropperID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!controlDropperResetDone)
        {
            controlDropperImageRenderer.setImage(edgeImageNormal);
            if (screen.getFocusHandler().getKeyboardFocusElement() == controlDropper.getElement())
            {
                backButtonElement.setFocus();
            }
            controlDropperWasClickable = false;
            controlDropperResetDone = true;
        }
    }
    
    public void onControlFieldHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                controlTextField.setFocus();
                activateActive(controlFieldID);
                return;
            }
            if (!controlFieldWasClickable)
            {
                controlTextField.setFocus();
                controlFieldImageRenderer.setImage(edgeImageHover);
                controlFieldWasClickable = true;
                controlFieldResetDone = false;
                adaptActive(controlFieldID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!controlFieldResetDone)
        {
            controlFieldImageRenderer.setImage(edgeImageNormal);
            if (screen.getFocusHandler().getKeyboardFocusElement() == controlTextField.getElement())
            {
                backButtonElement.setFocus();
            }
            controlFieldWasClickable = false;
            controlFieldResetDone = true;
        }
    }
    
    public void onControlRevertHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                activateActive(controlRevertID);
                return;
            }
            if (!controlRevertWasClickable)
            {
                controlRevertTextRenderer.setColor(hoverTextColor);
                controlRevertImageRenderer.setImage(edgeImageHover);
                controlRevertWasClickable = true;
                controlRevertResetDone = false;
                adaptActive(controlRevertID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!controlRevertResetDone)
        {
            controlRevertTextRenderer.setColor(defaultTextColor);
            controlRevertImageRenderer.setImage(edgeImageNormal);
            controlRevertWasClickable = false;
            controlRevertResetDone = true;
        }
    }
    
    public void onControlSaveHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                activateActive(controlSaveID);
                return;
            }
            if (!controlSaveWasClickable)
            {
                controlSaveTextRenderer.setColor(hoverTextColor);
                controlSaveImageRenderer.setImage(edgeImageHover);
                controlSaveWasClickable = true;
                controlSaveResetDone = false;
                adaptActive(controlSaveID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!controlSaveResetDone)
        {
            controlSaveTextRenderer.setColor(defaultTextColor);
            controlSaveImageRenderer.setImage(edgeImageNormal);
            controlSaveWasClickable = false;
            controlSaveResetDone = true;
        }
    }
    
    public void onDefaultSchemeHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                activateActive(defaultSchemeID);
                return;
            }
            if (!defaultSchemeWasClickable)
            {
                defaultSchemeTextRenderer.setColor(hoverTextColor);
                defaultSchemeImageRenderer.setImage(edgeImageHover);
                defaultSchemeWasClickable = true;
                defaultSchemeResetDone = false;
                adaptActive(defaultSchemeID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!defaultSchemeResetDone)
        {
            defaultSchemeTextRenderer.setColor(defaultTextColor);
            defaultSchemeImageRenderer.setImage(edgeImageNormal);
            defaultSchemeWasClickable = false;
            defaultSchemeResetDone = true;
        }
    }
    
    public void onRestoreHover(boolean isAvailable, boolean wasClick)
    {
        if (isAvailable)
        {
            if (wasClick)
            {
                activateActive(restoreID);
                return;
            }
            if (!restoreWasClickable)
            {
                restoreTextRenderer.setColor(hoverTextColor);
                restoreImageRenderer.setImage(edgeImageHover);
                restoreWasClickable = true;
                restoreResetDone = false;
                adaptActive(restoreID);
            }
        }
        else if (keysUsed)
        {
            return;
        }
        else if (!restoreResetDone)
        {
            restoreTextRenderer.setColor(defaultTextColor);
            restoreImageRenderer.setImage(edgeImageNormal);
            restoreWasClickable = false;
            restoreResetDone = true;
        }
    }
    // </editor-fold>

    private void initJoy() 
    {
//        inputManager.setCursorVisible(false);
        if (Main.joysticks.length != 0)
        {
            Main.joysticks[0].getButton("0").assignButton("Accept");
            
            if (Main.joysticks[0].getName().equals("Logitech Dual Action"))
            {
                compoundManager.deleteMapping("Accept");
                Main.joysticks[0].getButton("2").assignButton("Accept");
            }
            
            Main.joysticks[0].getXAxis().assignAxis("Right", "Left");
            Main.joysticks[0].getYAxis().assignAxis("Down", "Up");
            compoundManager.addListener(this, "Accept", "Right", "Left", "Up", "Down");
        }
    }

    public void onAction(String name, boolean isPressed, float tpf) 
    {
        if (isPressed && name.equals("Accept"))
        {
            System.out.println("Accept");
            niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_RETURN, '0', true, false));
        }
        else if (isPressed && name.equals("Right"))
        {
            System.out.println("Right");
            niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_D, 'D', true, false));
        }
        else if (isPressed && name.equals("Left"))
        {
            System.out.println("Left");
            niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_A, 'A', true, false));
        }
        else if (isPressed && name.equals("Up"))
        {
            System.out.println("Up");
            niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_W, 'W', true, false));
        }
        else if (isPressed && name.equals("Down"))
        {
            System.out.println("Down");
            niftyDisplay.simulateKeyEvent(new KeyInputEvent(KeyInput.KEY_S, 'S', true, false));
        }
    }
    
}
