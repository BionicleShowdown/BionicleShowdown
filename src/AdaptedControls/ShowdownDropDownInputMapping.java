package AdaptedControls;



import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

/**
 * StandardDropDown Keyboard Mapping for Nifty.
 * @author void
 */
public class ShowdownDropDownInputMapping implements NiftyInputMapping {

  public NiftyInputEvent convert(final KeyboardInputEvent inputEvent) {
//    if (inputEvent.isKeyDown()) {
//      if (inputEvent.getKey() == KeyboardInputEvent.KEY_S) {
//        return NiftyInputEvent.MoveCursorDown;
//      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_W) {
//        return NiftyInputEvent.MoveCursorUp;
//      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_RETURN) {
//        return NiftyInputEvent.Activate;
//      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_SPACE) {
//        return NiftyInputEvent.Activate;
//      } else if (inputEvent.getKey() == KeyboardInputEvent.KEY_TAB) {
//        if (inputEvent.isShiftDown()) {
//          return NiftyInputEvent.PrevInputElement;
//        } else {
//          return NiftyInputEvent.NextInputElement;
//        }
//      }
//    }
    return null;
  }
}
