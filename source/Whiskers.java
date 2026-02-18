// Whiskers.java
import processing.core.PApplet;

public class Whiskers extends FacePart {        // ← public に
  public Whiskers(PApplet app, float s) {       // ← public (PApplet,float) を用意
    super(app, s);
  }

  @Override
  public void draw() {
    refreshCenter();
    app.pushMatrix();
    app.translate(centerX, centerY);
    app.scale(s);

    app.stroke(255);
    app.strokeWeight(12);
    app.line(-150, -5, -170, 30);
    app.line(-170, -5, -190, 30);
    app.line( 150,  5,  170, 30);
    app.line( 170,  5,  190, 30);

    app.popMatrix();
  }
}
