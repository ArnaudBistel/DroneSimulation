package controller;

import agents.AppManager;
import gui.DroneBody;
import gui.MainApp;
import io.sarl.javafx.FxViewerController;
import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.12")
@SarlElementType(10)
@SuppressWarnings("all")
public class MainWindowController extends FxViewerController {
  private Map<UUID, DroneBody> bodies = new TreeMap<UUID, DroneBody>();
  
  private MainApp mainApp;
  
  @FXML
  private Button startButton;
  
  private final AtomicBoolean started = new AtomicBoolean();
  
  @FXML
  private Canvas drawZone;
  
  @Pure
  public void setMainApp(final MainApp mainApp) {
    this.mainApp = mainApp;
  }
  
  /**
   * def initialize {
   * 
   * if (!started.getAndSet(true)) {
   * startAgentApplication(typeof(HelloAgent)) [
   * //
   * ]
   * }
   * }
   */
  @FXML
  public void startAgent() {
    this.startButton.setVisible(false);
    boolean _andSet = this.started.getAndSet(true);
    if ((!_andSet)) {
      final Procedure0 _function = () -> {
      };
      this.startAgentApplication(AppManager.class, _function);
      this.startSimulation();
    }
  }
  
  public DroneBody addBody(final DroneBody body) {
    return this.bodies.put(body.getId(), body);
  }
  
  public void startSimulation() {
    abstract class __MainWindowController_0 extends AnimationTimer {
      public abstract void handle(final long now);
    }
    
    __MainWindowController_0 timer = new __MainWindowController_0() {
      @Override
      public void handle(final long now) {
        long lastUpdate = 0l;
        if (((now - lastUpdate) >= 28_000_000)) {
          MainWindowController.this.update();
          lastUpdate = now;
        }
      }
    };
    timer.start();
  }
  
  public void update() {
    final GraphicsContext gc = this.drawZone.getGraphicsContext2D();
    gc.clearRect(0, 0, this.drawZone.getWidth(), this.drawZone.getHeight());
    Collection<DroneBody> _values = this.bodies.values();
    for (final DroneBody b : _values) {
      gc.drawImage(b.getImage(), b.getPosition().getX(), b.getPosition().getY());
    }
  }
  
  @Override
  @Pure
  @SyntheticMember
  public boolean equals(final Object obj) {
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    return result;
  }
  
  @SyntheticMember
  public MainWindowController() {
    super();
  }
}
