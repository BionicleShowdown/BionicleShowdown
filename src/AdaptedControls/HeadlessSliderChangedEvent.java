package AdaptedControls;

import de.lessvoid.nifty.NiftyEvent;

/**
 * Nifty generates this event when a Slider value has been changed.
 * @author void
 */
public class HeadlessSliderChangedEvent implements NiftyEvent<Void> {
  private HeadlessSlider slider;
  private float value;

  public HeadlessSliderChangedEvent(final HeadlessSlider slider, final float newValue) {
    this.slider = slider;
    this.value = newValue;
  }

  public HeadlessSlider getSlider() {
    return slider;
  }

  public float getValue() {
    return value;
  }
}
