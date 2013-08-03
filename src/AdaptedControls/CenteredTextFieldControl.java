package AdaptedControls;

import de.lessvoid.nifty.ClipboardAWT;
import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.controls.textfield.TextFieldLogic;
import de.lessvoid.nifty.controls.textfield.TextFieldView;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.elements.tools.FontHelper;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * A TextFieldControl.
 * 
 * @author void
 * @author Inferno
 * @deprecated Please use {@link AdaptedControls.CenteredTextField} when accessing NiftyControls.
 */
@Deprecated
public class CenteredTextFieldControl extends AbstractController implements CenteredTextField, TextFieldView {
  private static final int CURSOR_Y = 0;
  private Nifty nifty;
  private Screen screen;
  private Element textElement;
  private Element fieldElement;
  private Element cursorElement;
  private TextFieldLogic textField;
  private int firstVisibleCharacterIndex;
  private int lastVisibleCharacterIndex;
  private int fieldWidth;
  private int fromClickCursorPos;
  private int toClickCursorPos;
  private FocusHandler focusHandler;
  private Character passwordChar;

  public void bind(
      final Nifty niftyParam,
      final Screen screenParam,
      final Element newElement,
      final Properties properties,
      final Attributes controlDefinitionAttributes) {
    super.bind(newElement);

    this.nifty = niftyParam;
    this.screen = screenParam;
    this.fromClickCursorPos = -1;
    this.toClickCursorPos = -1;

    this.textField = new TextFieldLogic(properties.getProperty("text", ""), new ClipboardAWT(), this);
    this.textField.toFirstPosition();

    this.textElement = getElement().findElementByName("#text");
    this.fieldElement = getElement().findElementByName("#field");
    this.cursorElement = getElement().findElementByName("#cursor");

    passwordChar = null;
    if (properties.containsKey("passwordChar")) {
      passwordChar = properties.get("passwordChar").toString().charAt(0);
    }
    if (properties.containsKey("maxLength")) {
      setMaxLength(new Integer(properties.getProperty("maxLength")));
    }
  }

  @Override
  public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    this.focusHandler = screen.getFocusHandler();

    this.textField.initWithText(textElement.getRenderer(TextRenderer.class).getOriginalText());
    this.fieldWidth = this.fieldElement.getWidth() - this.cursorElement.getWidth();

    TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    this.firstVisibleCharacterIndex = 0;
    this.lastVisibleCharacterIndex = FontHelper.getVisibleCharactersFromStart(textRenderer.getFont(), this.textField.getText(), fieldWidth, 1.0f);
    
    
    
    updateCursor();
    super.init(parameter, controlDefinitionAttributes);
  }

  public void onStartScreen() {
  }

  @Override
  public void layoutCallback() {
    this.fieldWidth = this.fieldElement.getWidth() - this.cursorElement.getWidth();
  }

  public void onClick(final int mouseX, final int mouseY) {
    String visibleString = textField.getText().substring(firstVisibleCharacterIndex, lastVisibleCharacterIndex);
    int indexFromPixel = getCursorPosFromMouse(mouseX, visibleString);
    if (indexFromPixel != -1) {
      fromClickCursorPos = firstVisibleCharacterIndex + indexFromPixel;
    }
    textField.resetSelection();
    textField.setCursorPosition(fromClickCursorPos);
    updateCursor();
  }

  public void onClickMouseMove(final int mouseX, final int mouseY) {
    String visibleString = textField.getText().substring(firstVisibleCharacterIndex, lastVisibleCharacterIndex);
    int indexFromPixel = getCursorPosFromMouse(mouseX, visibleString);
    if (indexFromPixel != -1) {
      toClickCursorPos = firstVisibleCharacterIndex + indexFromPixel;
    }

    textField.setCursorPosition(fromClickCursorPos);
    textField.startSelecting();
    textField.setCursorPosition(toClickCursorPos);
    textField.endSelecting();
    updateCursor();
  }

  private int getCursorPosFromMouse(final int mouseX, final String visibleString) {
    TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    
    int textLength = textField.getText().length();
    int characterAdvance = textRenderer.getFont().getCharacterAdvance('0', '1', 1.0f);
    int cursorPixelPos = (fieldElement.getWidth() / 2) - ((characterAdvance / 2) * textLength) - 2;
    
    
    return FontHelper.getCharacterIndexFromPixelPosition(textRenderer.getFont(), visibleString,
        (mouseX - (fieldElement.getX() + cursorPixelPos)), 1.0f);
  }
  
  public int getCursorPosition()
  {
      return textField.getCursorPosition();
  }

  public boolean inputEvent(final NiftyInputEvent inputEvent) {
    if (inputEvent == NiftyInputEvent.MoveCursorLeft) {
      textField.cursorLeft();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.MoveCursorRight) {
      textField.cursorRight();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.Delete) {
      textField.delete();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.Backspace) {
      textField.backspace();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.MoveCursorToLastPosition) {
      textField.toLastPosition();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.MoveCursorToFirstPosition) {
      textField.toFirstPosition();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.SelectionStart) {
      textField.startSelecting();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.SelectionEnd) {
      textField.endSelecting();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.Cut) {
      textField.cut(passwordChar);
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.Copy) {
      textField.copy(passwordChar);
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.Paste) {
      textField.put();
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.SelectAll) {
      if (textField.getText().length() > 0) {
        textField.setCursorPosition(0);
        textField.startSelecting();
        textField.setCursorPosition(textField.getText().length());
        textField.endSelecting();
        updateCursor();
        return true;
      }
    } else if (inputEvent == NiftyInputEvent.Character) {
      textField.insert(inputEvent.getCharacter());
      updateCursor();
      return true;
    } else if (inputEvent == NiftyInputEvent.NextInputElement) {
      if (focusHandler != null) {
        focusHandler.getNext(fieldElement).setFocus();
        updateCursor();
        return true;
      }
    } else if (inputEvent == NiftyInputEvent.PrevInputElement) {
      textField.endSelecting();
      if (focusHandler != null) {
        focusHandler.getPrev(fieldElement).setFocus();
        updateCursor();
        return true;
      }
    }

    updateCursor();
    return false;
  }

  private void updateCursor() {
    TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
    String text = textField.getText();
    checkBounds(text, textRenderer);
    calcLastVisibleIndex(textRenderer);

    // update text
    if (isPassword(passwordChar)) {
      int numChar = text.length();
      char[] chars = new char[numChar];
      for (int i = 0; i < numChar; ++i) {
        chars[i] = passwordChar;
      }
      text = new String(chars);
    }
    textRenderer.setText(text);
    textRenderer.setSelection(textField.getSelectionStart(), textField.getSelectionEnd());

    // calc cursor position
    int cursorPos = textField.getCursorPosition();

    // outside, move window to fit cursorPos inside [first,last]
    calcFirstVisibleIndex(cursorPos);
    calcLastVisibleIndex(textRenderer);

    String substring2 = text.substring(0, firstVisibleCharacterIndex);
    int d = textRenderer.getFont().getWidth(substring2);
    textRenderer.setXoffsetHack(-d);

    String substring = text.substring(0, cursorPos);
    int textWidth = textRenderer.getFont().getWidth(substring);
    int cursorPixelPos = textWidth - d;
    
    int textLength = textField.getText().length();
    int characterAdvance = textRenderer.getFont().getCharacterAdvance('0', '1', 1.0f);
    
    if (textLength == 0)
    {
        cursorPixelPos = (fieldElement.getWidth() / 2) - (characterAdvance / 2) - 2;
    }
    else
    {
        cursorPixelPos = (fieldElement.getWidth() / 2) - ((characterAdvance / 2) * textLength) + (characterAdvance * cursorPos) - 2;
    }
    // Starts off center - (xadvance / 2)
    // Goes up by (xadvance / 2) each time after first

    cursorElement.setConstraintX(new SizeValue(cursorPixelPos + "px"));
    cursorElement.setConstraintY(new SizeValue((getElement().getHeight() - cursorElement.getHeight()) / 2 + CURSOR_Y + "px"));
    cursorElement.startEffect(EffectEventId.onActive, null);
    if (screen != null) {
      screen.layoutLayers();
    }
  }

  private boolean isPassword(final Character currentPasswordChar) {
    return currentPasswordChar != null;
  }

  private void calcFirstVisibleIndex(final int cursorPos) {
    if (cursorPos > lastVisibleCharacterIndex) {
      int cursorPosDelta = cursorPos - lastVisibleCharacterIndex;
      firstVisibleCharacterIndex += cursorPosDelta;
    } else if (cursorPos < firstVisibleCharacterIndex) {
      int cursorPosDelta = firstVisibleCharacterIndex - cursorPos;
      firstVisibleCharacterIndex -= cursorPosDelta;
    }
  }

  private void checkBounds(final String text, final TextRenderer textRenderer) {
    int textLen = text.length();
    if (firstVisibleCharacterIndex > textLen) {
      // re position so that we show at much possible text
      lastVisibleCharacterIndex = textLen;
      firstVisibleCharacterIndex = FontHelper.getVisibleCharactersFromEnd(textRenderer.getFont(), text, fieldWidth, 1.0f);
    }
  }

  private void calcLastVisibleIndex(final TextRenderer textRenderer) {
    String currentText = this.textField.getText();
    if (firstVisibleCharacterIndex < currentText.length()) {
      String textToCheck = currentText.substring(firstVisibleCharacterIndex);
      int lengthFitting = FontHelper.getVisibleCharactersFromStart(textRenderer.getFont(), textToCheck, fieldWidth, 1.0f);
      lastVisibleCharacterIndex = lengthFitting + firstVisibleCharacterIndex;
    } else {
      lastVisibleCharacterIndex = firstVisibleCharacterIndex;
    }
  }

  @Override
  public void onFocus(final boolean getFocus) {
    if (cursorElement != null) {
      super.onFocus(getFocus);
      if (getFocus) {
        cursorElement.startEffect(EffectEventId.onCustom);
      } else {
        cursorElement.stopEffect(EffectEventId.onCustom);
      }
      updateCursor();
    }
  }

  @Override
  public String getText() {
    return textField.getText();
  }

  @Override
  public void setText(final String newText) {
    textField.initWithText(nifty.specialValuesReplace(newText));
    updateCursor();
  }

  @Override
  public void setMaxLength(final int maxLength) {
    textField.setMaxLength(maxLength);
    updateCursor();
  }

  @Override
  public void setCursorPosition(final int position) {
    textField.setCursorPosition(position);
    updateCursor();
  }

  @Override
  public void textChangeEvent(final String newText) {
    nifty.publishEvent(getElement().getId(), new CenteredTextFieldChangedEvent(this, newText));
  }

  @Override
  public void enablePasswordChar(final char passwordChar) {
    this.passwordChar = passwordChar;
    updateCursor();
  }

  @Override
  public void disablePasswordChar() {
    this.passwordChar = null;
    updateCursor();
  }

  @Override
  public boolean isPasswordCharEnabled() {
    return passwordChar != null;
  }

}