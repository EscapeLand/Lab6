
public class Strategy {

  public  boolean choose(int choose,ladders la ,monkey mo){
    switch(choose){
      case 0:
        return zero(la,mo);
      case 1:
        return first(la,mo);
      case 2:
        return second(la,mo);
        default:
          return zero(la,mo);
    }
  }
  synchronized boolean zero(ladders la ,monkey mo){
    if(mo.direction.equals("L->R")) {
      for (int i = 0; i < la.ladderNumber; i++) {
        if (la.ladderList[i].monkeyNum == 0) {
          la.ladderList[i].rungList[0].monkey = mo;
          mo.lNum = la.ladderList[i];
          mo.lnum = i;
          mo.rNum = 0;
          mo.direction="on";
          la.ladderList[i].speed = mo.getVelocity();
          la.ladderList[i].monkeyNum++;
          la.ladderList[i].dir = 0;
          return true;
        }
      }
    }
    else{
      for (int i = 0; i < la.ladderNumber; i++) {
        if (la.ladderList[i].monkeyNum == 0) {
          la.ladderList[i].rungList[la.ladderList[i].rungNumber-1].monkey = mo;
          la.ladderList[i].speed = mo.getVelocity();
          mo.lNum = la.ladderList[i];
          mo.lnum = i;
          mo.rNum = la.ladderList[i].rungNumber-1;
          mo.direction="on";
          la.ladderList[i].monkeyNum++;
          la.ladderList[i].dir = 1;
          return true;
        }
      }
    }
    return false;
  }

  synchronized boolean first(ladders la,monkey mo){
    if(!zero(la,mo)){
      int num = la.rungNumber;
      for (int i = 0; i < la.ladderNumber; i++){
        if(change(mo) == la.ladderList[i].dir && la.ladderList[i].rungList[getfirst(num,mo)].monkey==null){
          la.ladderList[i].rungList[getfirst(num,mo)].monkey = mo;
          mo.lNum = la.ladderList[i];
          mo.lnum = i;
          mo.rNum = getfirst(num,mo);
          mo.direction="on";
          la.ladderList[i].monkeyNum++;
          return true;
        }
      }
    }
    else{
      return true;
    }

    return false;
  }

  synchronized boolean second(ladders la,monkey mo){
    if(!zero(la,mo)) {
      int result = -1;
      int det = 100;
      int num = la.rungNumber;
      for (int i = 0; i < la.ladderNumber; i++){
        if(change(mo) == la.ladderList[i].dir && la.ladderList[i].rungList[getfirst(num,mo)].monkey==null){
          int newDet = Math.abs(mo.getVelocity() - la.ladderList[i].speed );
          if(newDet< det){
            result = i;
            det = newDet;
          }
        }
      }
      if(result == -1) return false;
      else{
        la.ladderList[result].rungList[getfirst(num,mo)].monkey = mo;
        mo.lNum = la.ladderList[result];
        mo.lnum = result;
        mo.rNum = getfirst(num,mo);
        mo.direction="on";
        la.ladderList[result].monkeyNum++;
        return true;
      }
    }
    else{
      return true;
    }

  }

  int change(monkey mo) {
    if(mo.direction.equals("L->R")){
      return 0;
    }
    else {
      return 1;
    }
  }

  int getfirst(int num,monkey mo){
    if(change(mo)==0){
      return 0;
    }
    else{
      return num-1;
    }
  }


}


