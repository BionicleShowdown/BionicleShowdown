package mygame;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.ControllerEvent;
import net.java.games.input.ControllerListener;

public abstract class AdaptedControllerEnvironment extends ControllerEnvironment
{
  public static AdaptedControllerEnvironment defaultEnvironment = null;

  protected final ArrayList controllerListeners = new ArrayList();

  static void logln(String msg)
  {
    log(msg + "\n");
  }

  static void log(String msg) {
    System.out.print(msg);
  }

  public abstract Controller[] getControllers();

  @Override
  public void addControllerListener(ControllerListener l)
  {
    assert (l != null);
    this.controllerListeners.add(l);
  }

  public abstract boolean isSupported();

  @Override
  public void removeControllerListener(ControllerListener l)
  {
    assert (l != null);
    this.controllerListeners.remove(l);
  }

  @Override
  protected void fireControllerAdded(Controller c)
  {
    ControllerEvent ev = new ControllerEvent(c);
    Iterator it = this.controllerListeners.iterator();
    while (it.hasNext())
      ((ControllerListener)it.next()).controllerAdded(ev);
  }

  @Override
  protected void fireControllerRemoved(Controller c)
  {
    ControllerEvent ev = new ControllerEvent(c);
    Iterator it = this.controllerListeners.iterator();
    while (it.hasNext())
      ((ControllerListener)it.next()).controllerRemoved(ev);
  }

  public static AdaptedControllerEnvironment getDefaultEnvironment()
  {
      if (defaultEnvironment == null)
      {
          defaultEnvironment = new AdaptedDefaultControllerEnvironment();
      }
    return defaultEnvironment;
  }
  
  public static void resetEnvironment()
  {
      defaultEnvironment = null;
  }
}
