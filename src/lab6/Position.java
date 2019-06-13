package lab6;

public class Position {
 private final Ladder lad;
 private int pos;
 
 public Position(Ladder lad, int pos) {
   this.lad = lad;
   this.pos = pos;
 }
 
 public void setPos(int pos) {
   this.pos = pos;
 }
 
 public int pos() {
   return this.pos;
 }
 
 public Ladder lad() {
   return this.lad;
 }
}
