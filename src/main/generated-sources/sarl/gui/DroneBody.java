package gui;

import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import java.util.Objects;
import java.util.UUID;
import javafx.scene.image.Image;
import org.arakhne.afc.math.geometry.d2.i.Point2i;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author arnaud
 */
@SarlSpecification("0.12")
@SarlElementType(10)
@SuppressWarnings("all")
public class DroneBody {
  private final UUID id;
  
  private final Image image;
  
  private Point2i position;
  
  private int velocity = 3;
  
  public DroneBody(final int x, final int y, final UUID id) {
    Point2i _point2i = new Point2i(x, y);
    this.position = _point2i;
    this.id = id;
    System.out.println("New body created");
    Image _image = new Image("/resources/img/drone_blue.png", 30, 30, true, true);
    this.image = _image;
  }
  
  @Pure
  public UUID getId() {
    return this.id;
  }
  
  @Pure
  public Image getImage() {
    return this.image;
  }
  
  @Pure
  public Point2i getPosition() {
    if (((this.position.getX() >= 1020) || (this.position.getX() <= 0))) {
      int _velocity = this.velocity;
      this.velocity = (_velocity * (-1));
    }
    double _x = this.position.getX();
    this.position.setX((_x + this.velocity));
    return this.position;
  }
  
  public void setPosition(final Point2i p) {
    this.position = p;
  }
  
  @Override
  @Pure
  @SyntheticMember
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DroneBody other = (DroneBody) obj;
    if (!Objects.equals(this.id, other.id))
      return false;
    if (other.velocity != this.velocity)
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Objects.hashCode(this.id);
    result = prime * result + Integer.hashCode(this.velocity);
    return result;
  }
}
