import gui.MainApp;
import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import javafx.application.Application;

@SarlSpecification("0.12")
@SarlElementType(10)
@SuppressWarnings("all")
public class DroneSimulation {
  public static void main(final String... args) {
    Application.launch(MainApp.class);
  }
  
  @SyntheticMember
  public DroneSimulation() {
    super();
  }
}
