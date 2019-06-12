
public class Strategy {

  public  boolean choose(int choose,ladders la ,monkey mo){
    switch(choose){
      case 0:
        return zero(la,mo);
      case 1:
        return first(la,mo);
      case 2:
        return zero(la,mo);
        default:
          return zero(la,mo);
    }
  }
  synchronized boolean zero(ladders la ,monkey mo){
    if(mo.direction.equals("L->R")) {
      for (int i = 0; i < la.ladderNumber; i++) {
        if (la.ladderList[i].monkeyNum == 0) {
          la.ladderList[i].rungList[0].monkey = mo;
          mo.lNum = i;
          mo.rNum = 0;
          mo.direction="on";
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
          mo.lNum = i;
          mo.rNum = 0;
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
          mo.lNum = i;
          mo.rNum = 0;
          mo.direction="on";
          la.ladderList[i].monkeyNum++;
          la.ladderList[i].dir = change(mo);
          return true;
        }
      }
    }
    else{
      return true;
    }

    return false;
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


