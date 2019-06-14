
class Strategy {
  static boolean choose(int choose, Monkey mo){
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

  static synchronized boolean naive(Monkey mo){
    assert mo.dir == Dir.R || mo.dir == Dir.L;
    for (int i = 0; i < Ladder.n; i++) {
      if (Ladder.ladders[i].size() == 0) {
        Ladder.ladders[i].addMonkey(mo);
        return true;
      }
    }
    return false;
  }

  static synchronized boolean naivePlus(Monkey mo){
    if(naive(mo)) return true;
    for (int i = 0; i < Ladder.n; i++){
      if(mo.dir == Ladder.ladders[i].dir
          && Ladder.ladders[i].firstEmpty(mo.dir)){
        Ladder.ladders[i].addMonkey(mo);
        return true;
      }
    }
    return false;

  }

  static synchronized boolean vFixed(Monkey mo){
    if(!naive(mo)) {
      int result = -1;
      int det = 100;
      for (int i = 0; i < Ladder.n; i++){
        if(mo.dir == Ladder.ladders[i].dir
            && Ladder.ladders[i].firstEmpty(mo.dir)){
          int newDet = Math.abs(mo.MV - Ladder.ladders[i].tailSpeed());
          if(newDet< det){
            result = i;
            det = newDet;
          }
        }
      }
      if(result == -1) return false;
      else{
        Ladder.ladders[result].addMonkey(mo);
        return true;
      }
    }
    else{
      return true;
    }

  }


}


