import java.util.ArrayList;
import java.util.List;
import javax.swing.plaf.multi.MultiViewportUI;

public class ladders {
  public ladder[] ladderList;
  public int ladderNumber;
  public int rungNumber;
  public static int leftnum = 0;
  public static int rightnum = 0;

  public ladders(int ladderNumber,int rungNumber) {
    this.ladderNumber = ladderNumber;
    this.rungNumber = rungNumber;
    ladderList = new ladder[ladderNumber];
    for(int i = 0;i < ladderNumber;i++){
      ladderList[i] = new ladder(rungNumber);
    }
  }

  public int checkFinish(){
    int tmp = 0;
    for(ladder l:ladderList){
      tmp = tmp +l.finish;
    }
    return tmp;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for(ladder l:ladderList){
      sb.append(l.toString());
      sb.append("\n");
    }
    return sb.toString();
  }


}
class ladder{
  public int rungNumber;
  public rung[] rungList;
  public int dir ;
  public int monkeyNum=0;
  public int finish = 0;
  public int speed;

  public ladder(int rungNumber) {
    this.rungNumber = rungNumber;
    rungList = new rung[rungNumber];
    for(int i = 0;i <rungNumber ;i++){
      rungList[i] = new rung();
    }
  }

  public void move(){
    if(dir == 0){
      for(int i = rungNumber-1;i >=0 ;i--){
        if(rungList[i].monkey !=null){
          if(i == rungNumber-1 ||(i + rungList[i].monkey.getVelocity() > rungNumber-1 && checkLR(i,rungList[i].monkey.getVelocity()) == i+rungList[i].monkey.getVelocity())){
            rungList[i].monkey.direction = "end";
            rungList[i].monkey = null;
            finish++;
            monkeyNum--;
          }
          else{
            rungList[i].monkey.rNum = checkLR(i,rungList[i].monkey.getVelocity());
            rungList[checkLR(i,rungList[i].monkey.getVelocity())].monkey = rungList[i].monkey;
            speed = Math.abs(checkLR(i,rungList[i].monkey.getVelocity()) - i);
            rungList[i].monkey = null;

          }
        }
        else{
          continue;
        }
      }
    }
    else if(dir == 1){
      for(int i = 0;i <rungNumber ;i++){
        if(rungList[i].monkey !=null){
          if(i == 0 || (i - rungList[i].monkey.getVelocity() < 0 && checkRL(i,rungList[i].monkey.getVelocity()) == i-rungList[i].monkey.getVelocity())){
            rungList[i].monkey.direction = "end";
            rungList[i].monkey = null;
            finish++;
            monkeyNum--;
          }
          else{
            rungList[i].monkey.rNum = checkRL(i,rungList[i].monkey.getVelocity());
            rungList[checkRL(i,rungList[i].monkey.getVelocity())].monkey = rungList[i].monkey;
            speed = Math.abs(checkRL(i,rungList[i].monkey.getVelocity()) - i);
            rungList[i].monkey = null;
          }
        }
        else{
          continue;
        }
      }
    }
  }

  public int checkLR(int i,int MV){
    int num=0;
    if(i +MV >rungNumber-1)num = rungNumber-1;
    else num = i+MV;
    for(i=i+1;i<=num;i++){
      if(rungList[i].monkey != null){
        return i-1;
      }
    }
    return num;
  }
  public int checkRL(int i,int MV){
    int num=0;
    if(i -MV <0)num = 0;
    else num = i-MV;
    for(i=i-1;i>=num;i--){
      if(rungList[i].monkey != null){
        return i+1;
      }
    }
    return num;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for(rung r:rungList){
      sb.append(r.String());
      sb.append(" ");
    }
    return sb.toString();
  }
}
class rung{
  public monkey monkey;

  public int String() {
    if(monkey == null){
      return 0;
    }
    else return monkey.id;
  }
}
