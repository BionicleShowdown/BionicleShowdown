package AdaptedControls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when the current text of an TextField changes.
 * @author void
 */
public class CenteredTextFieldChangedEvent implements NiftyEvent<Void> {
  private CenteredTextField textField;
  private String currentText;

  public CenteredTextFieldChangedEvent(final CenteredTextField textFieldControl, final String currentText) {
    this.textField = textFieldControl;
    this.currentText = currentText;
  }

  public CenteredTextField getTextFieldControl() {
    return textField;
  }

  public String getText() {
    return currentText;
  }
}