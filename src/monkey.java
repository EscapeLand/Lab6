import java.util.Objects;

public class monkey{
  public int id;
  private int velocity;
  public String direction;
  private int choose;
  public int lNum;
  public int rNum;

  public monkey(int id, int velocity, String direction, int choose) {
    this.id = id;
    this.velocity = velocity;
    this.direction = direction;
    this.choose = choose;
  }

  public int getVelocity() {
    return velocity;
  }

  public int getChoose() {
    return choose;
  }

  public monkey clone(){
    return new monkey(id,velocity,direction,choose);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof monkey)) {
      return false;
    }
    monkey monkey = (monkey) o;
    return velocity == monkey.velocity &&
        choose == monkey.choose &&
        direction.equals(monkey.direction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(velocity, direction, choose);
  }
}
