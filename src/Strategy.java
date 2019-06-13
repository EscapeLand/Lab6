
public class Strategy {
  public static Strategy st = new Strategy();

  public boolean choose(int choose, Monkey mo){
    switch(choose){
      case 0:
        return naive(mo);
      case 1:
        return naivePlus(mo);
      case 2:
        return vFixed(mo);
      default: assert false;
    }
    return false;
  }
  synchronized boolean naive(Monkey mo){
    if(mo.direction.equals("L->R")) {
      for (int i = 0; i < Ladder.n; i++) {
        if (Ladder.ladders[i].monkeyNum == 0) {
          Ladder.ladders[i].rungList[0].monkey = mo;
          mo.lNum = Ladder.ladders[i];
          mo.lnum = i;
          mo.rNum = 0;
          mo.direction = Dir.ON;
          Ladder.ladders[i].speed = mo.getVelocity();
          Ladder.ladders[i].monkeyNum++;
          Ladder.ladders[i].dir = Dir.R;
          return true;
        }
      }
    }
    else{
      for (int i = 0; i < Ladder.n; i++) {
        if (Ladder.ladders[i].monkeyNum == 0) {
          Ladder.ladders[i].rungList[Ladder.ladders[i].rungNumber - 1].monkey = mo;
          Ladder.ladders[i].speed = mo.getVelocity();
          mo.lNum = Ladder.ladders[i];
          mo.lnum = i;
          mo.rNum = Ladder.ladders[i].rungNumber - 1;
          mo.direction = Dir.ON;
          Ladder.ladders[i].monkeyNum++;
          Ladder.ladders[i].dir = Dir.L;
          return true;
        }
      }
    }
    return false;
  }

  synchronized boolean naivePlus(Monkey mo){
    if(!naive(mo)){
      for (int i = 0; i < Ladder.n; i++){
        if(mo.direction == Ladder.ladders[i].dir
            && Ladder.ladders[i].rungList[getFirst(Ladder.h, mo)].monkey == null){
          Ladder.ladders[i].rungList[getFirst(Ladder.h, mo)].monkey = mo;
          mo.lNum = Ladder.ladders[i];
          mo.lnum = i;
          mo.rNum = getFirst(Ladder.h,mo);
          mo.direction = Dir.ON;
          Ladder.ladders[i].monkeyNum++;
          return true;
        }
      }
    }
    else{
      return true;
    }

    return false;
  }

  synchronized boolean vFixed(Monkey mo){
    if(!naive(mo)) {
      int result = -1;
      int det = 100;
      int num = Ladder.h;
      for (int i = 0; i < Ladder.n; i++){
        if(mo.direction == Ladder.ladders[i].dir
            && Ladder.ladders[i].rungList[getFirst(num, mo)].monkey == null){
          int newDet = Math.abs(mo.getVelocity() - Ladder.ladders[i].speed );
          if(newDet< det){
            result = i;
            det = newDet;
          }
        }
      }
      if(result == -1) return false;
      else{
        Ladder.ladders[result].rungList[getFirst(num,mo)].monkey = mo;
        mo.lNum = Ladder.ladders[result];
        mo.lnum = result;
        mo.rNum = getFirst(num,mo);
        mo.direction = Dir.ON;
        Ladder.ladders[result].monkeyNum++;
        return true;
      }
    }
    else{
      return true;
    }

  }

  int change(Monkey mo) {
    if(mo.direction == Dir.R){
      return 0;
    }
    else {
      return 1;
    }
  }

  int getFirst(int num, Monkey mo){
    if(change(mo) == 0){
      return 0;
    }
    else{
      return num-1;
    }
  }


}


