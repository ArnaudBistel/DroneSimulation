package agents;

import io.sarl.core.AgentKilled;
import io.sarl.core.AgentSpawned;
import io.sarl.core.ContextJoined;
import io.sarl.core.ContextLeft;
import io.sarl.core.Destroy;
import io.sarl.core.Initialize;
import io.sarl.core.Logging;
import io.sarl.core.MemberJoined;
import io.sarl.core.MemberLeft;
import io.sarl.core.ParticipantJoined;
import io.sarl.core.ParticipantLeft;
import io.sarl.core.SpaceCreated;
import io.sarl.core.SpaceDestroyed;
import io.sarl.lang.annotation.ImportedCapacityFeature;
import io.sarl.lang.annotation.PerceptGuardEvaluator;
import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.Agent;
import io.sarl.lang.core.AtomicSkillReference;
import io.sarl.lang.core.DynamicSkillProvider;
import io.sarl.lang.core.Event;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author arnaud
 */
@SarlSpecification("0.12")
@SarlElementType(19)
@SuppressWarnings("all")
public class Sprite extends Agent {
  private Image image;
  
  private double positionX;
  
  private double positionY;
  
  private double velocityX;
  
  private double velocityY;
  
  private double width;
  
  private double height;
  
  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    this.positionX = 100;
    this.positionY = 100;
    this.velocityX = 10;
    this.velocityY = 10;
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("The agent was started.");
  }
  
  private void $behaviorUnit$Destroy$1(final Destroy occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("The agent was stopped.");
  }
  
  protected void setImage(final Image i) {
    this.image = i;
    this.width = i.getWidth();
    this.height = i.getHeight();
  }
  
  protected void setImage(final String filename) {
    Image i = new Image(filename);
    this.setImage(i);
  }
  
  protected void setPosition(final double x, final double y) {
    this.positionX = x;
    this.positionY = y;
  }
  
  protected void setVelocity(final double x, final double y) {
    this.velocityX = x;
    this.velocityY = y;
  }
  
  protected void addVelocity(final double x, final double y) {
    double _velocityX = this.velocityX;
    this.velocityX = (_velocityX + x);
    double _velocityY = this.velocityY;
    this.velocityY = (_velocityY + y);
  }
  
  protected void update(final double time) {
    double _positionX = this.positionX;
    this.positionX = (_positionX + (this.velocityX * time));
    double _positionY = this.positionY;
    this.positionY = (_positionY + (this.velocityY * time));
  }
  
  protected void render(final GraphicsContext gc) {
    gc.drawImage(this.image, this.positionX, this.positionY);
  }
  
  @Pure
  protected Rectangle2D getBoundary() {
    return new Rectangle2D(this.positionX, this.positionY, this.width, this.height);
  }
  
  protected boolean intersects(final Sprite s) {
    return s.getBoundary().intersects(this.getBoundary());
  }
  
  @Pure
  protected String displayAsString() {
    return (((((((((" Position: [" + Double.valueOf(this.positionX)) + ",") + Double.valueOf(this.positionY)) + "]") + " Velocity: [") + Double.valueOf(this.velocityX)) + ",") + Double.valueOf(this.velocityY)) + "]");
  }
  
  private void $behaviorUnit$AgentSpawned$2(final AgentSpawned occurrence) {
  }
  
  private void $behaviorUnit$AgentKilled$3(final AgentKilled occurrence) {
  }
  
  private void $behaviorUnit$ContextJoined$4(final ContextJoined occurrence) {
  }
  
  private void $behaviorUnit$ContextLeft$5(final ContextLeft occurrence) {
  }
  
  private void $behaviorUnit$MemberJoined$6(final MemberJoined occurrence) {
  }
  
  private void $behaviorUnit$MemberLeft$7(final MemberLeft occurrence) {
  }
  
  private void $behaviorUnit$MemberLeft$8(final MemberLeft occurrence) {
  }
  
  private void $behaviorUnit$SpaceCreated$9(final SpaceCreated occurrence) {
  }
  
  private void $behaviorUnit$SpaceDestroyed$10(final SpaceDestroyed occurrence) {
  }
  
  private void $behaviorUnit$ParticipantJoined$11(final ParticipantJoined occurrence) {
  }
  
  private void $behaviorUnit$ParticipantLeft$12(final ParticipantLeft occurrence) {
  }
  
  @Extension
  @ImportedCapacityFeature(Logging.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_CORE_LOGGING;
  
  @SyntheticMember
  @Pure
  private Logging $CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_CORE_LOGGING == null || this.$CAPACITY_USE$IO_SARL_CORE_LOGGING.get() == null) {
      this.$CAPACITY_USE$IO_SARL_CORE_LOGGING = $getSkill(Logging.class);
    }
    return $castSkill(Logging.class, this.$CAPACITY_USE$IO_SARL_CORE_LOGGING);
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Initialize(final Initialize occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Initialize$0(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$ContextLeft(final ContextLeft occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$ContextLeft$5(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$SpaceDestroyed(final SpaceDestroyed occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$SpaceDestroyed$10(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$ContextJoined(final ContextJoined occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$ContextJoined$4(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$MemberLeft(final MemberLeft occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$MemberLeft$7(occurrence));
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$MemberLeft$8(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$SpaceCreated(final SpaceCreated occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$SpaceCreated$9(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$AgentSpawned(final AgentSpawned occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$AgentSpawned$2(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$ParticipantJoined(final ParticipantJoined occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$ParticipantJoined$11(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$ParticipantLeft(final ParticipantLeft occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$ParticipantLeft$12(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Destroy(final Destroy occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Destroy$1(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$AgentKilled(final AgentKilled occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$AgentKilled$3(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$MemberJoined(final MemberJoined occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$MemberJoined$6(occurrence));
  }
  
  @SyntheticMember
  @Override
  public void $getSupportedEvents(final Set<Class<? extends Event>> toBeFilled) {
    super.$getSupportedEvents(toBeFilled);
    toBeFilled.add(AgentKilled.class);
    toBeFilled.add(AgentSpawned.class);
    toBeFilled.add(ContextJoined.class);
    toBeFilled.add(ContextLeft.class);
    toBeFilled.add(Destroy.class);
    toBeFilled.add(Initialize.class);
    toBeFilled.add(MemberJoined.class);
    toBeFilled.add(MemberLeft.class);
    toBeFilled.add(ParticipantJoined.class);
    toBeFilled.add(ParticipantLeft.class);
    toBeFilled.add(SpaceCreated.class);
    toBeFilled.add(SpaceDestroyed.class);
  }
  
  @SyntheticMember
  @Override
  public boolean $isSupportedEvent(final Class<? extends Event> event) {
    if (AgentKilled.class.isAssignableFrom(event)) {
      return true;
    }
    if (AgentSpawned.class.isAssignableFrom(event)) {
      return true;
    }
    if (ContextJoined.class.isAssignableFrom(event)) {
      return true;
    }
    if (ContextLeft.class.isAssignableFrom(event)) {
      return true;
    }
    if (Destroy.class.isAssignableFrom(event)) {
      return true;
    }
    if (Initialize.class.isAssignableFrom(event)) {
      return true;
    }
    if (MemberJoined.class.isAssignableFrom(event)) {
      return true;
    }
    if (MemberLeft.class.isAssignableFrom(event)) {
      return true;
    }
    if (ParticipantJoined.class.isAssignableFrom(event)) {
      return true;
    }
    if (ParticipantLeft.class.isAssignableFrom(event)) {
      return true;
    }
    if (SpaceCreated.class.isAssignableFrom(event)) {
      return true;
    }
    if (SpaceDestroyed.class.isAssignableFrom(event)) {
      return true;
    }
    return false;
  }
  
  @SyntheticMember
  @Override
  public void $evaluateBehaviorGuards(final Object event, final Collection<Runnable> callbacks) {
    super.$evaluateBehaviorGuards(event, callbacks);
    if (event instanceof AgentKilled) {
      final AgentKilled occurrence = (AgentKilled) event;
      $guardEvaluator$AgentKilled(occurrence, callbacks);
    }
    if (event instanceof AgentSpawned) {
      final AgentSpawned occurrence = (AgentSpawned) event;
      $guardEvaluator$AgentSpawned(occurrence, callbacks);
    }
    if (event instanceof ContextJoined) {
      final ContextJoined occurrence = (ContextJoined) event;
      $guardEvaluator$ContextJoined(occurrence, callbacks);
    }
    if (event instanceof ContextLeft) {
      final ContextLeft occurrence = (ContextLeft) event;
      $guardEvaluator$ContextLeft(occurrence, callbacks);
    }
    if (event instanceof Destroy) {
      final Destroy occurrence = (Destroy) event;
      $guardEvaluator$Destroy(occurrence, callbacks);
    }
    if (event instanceof Initialize) {
      final Initialize occurrence = (Initialize) event;
      $guardEvaluator$Initialize(occurrence, callbacks);
    }
    if (event instanceof MemberJoined) {
      final MemberJoined occurrence = (MemberJoined) event;
      $guardEvaluator$MemberJoined(occurrence, callbacks);
    }
    if (event instanceof MemberLeft) {
      final MemberLeft occurrence = (MemberLeft) event;
      $guardEvaluator$MemberLeft(occurrence, callbacks);
    }
    if (event instanceof ParticipantJoined) {
      final ParticipantJoined occurrence = (ParticipantJoined) event;
      $guardEvaluator$ParticipantJoined(occurrence, callbacks);
    }
    if (event instanceof ParticipantLeft) {
      final ParticipantLeft occurrence = (ParticipantLeft) event;
      $guardEvaluator$ParticipantLeft(occurrence, callbacks);
    }
    if (event instanceof SpaceCreated) {
      final SpaceCreated occurrence = (SpaceCreated) event;
      $guardEvaluator$SpaceCreated(occurrence, callbacks);
    }
    if (event instanceof SpaceDestroyed) {
      final SpaceDestroyed occurrence = (SpaceDestroyed) event;
      $guardEvaluator$SpaceDestroyed(occurrence, callbacks);
    }
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
    Sprite other = (Sprite) obj;
    if (Double.doubleToLongBits(other.positionX) != Double.doubleToLongBits(this.positionX))
      return false;
    if (Double.doubleToLongBits(other.positionY) != Double.doubleToLongBits(this.positionY))
      return false;
    if (Double.doubleToLongBits(other.velocityX) != Double.doubleToLongBits(this.velocityX))
      return false;
    if (Double.doubleToLongBits(other.velocityY) != Double.doubleToLongBits(this.velocityY))
      return false;
    if (Double.doubleToLongBits(other.width) != Double.doubleToLongBits(this.width))
      return false;
    if (Double.doubleToLongBits(other.height) != Double.doubleToLongBits(this.height))
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Double.hashCode(this.positionX);
    result = prime * result + Double.hashCode(this.positionY);
    result = prime * result + Double.hashCode(this.velocityX);
    result = prime * result + Double.hashCode(this.velocityY);
    result = prime * result + Double.hashCode(this.width);
    result = prime * result + Double.hashCode(this.height);
    return result;
  }
  
  @SyntheticMember
  public Sprite(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  public Sprite(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
