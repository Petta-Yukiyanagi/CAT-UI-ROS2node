// Ears.java
import processing.core.PApplet;

public class Ears extends FacePart {          // ← public に
  public Ears(PApplet app, float s) {         // ← これも public に
    super(app, s);
  }

  @Override
  public void draw() {
    refreshCenter();
    app.pushMatrix();
    app.translate(centerX, centerY);
    app.scale(s);

    app.stroke(255);
    app.strokeWeight(8);
    app.noFill();

    float nXL=-100,nYL=-80, dXL=-115,dY=-65;
    float nXR= 100,            dXR= 115;
    float bLL=-125,bRL=-75, bLR=75, bRR=125, baseY=-20;

    float tXL = PApplet.lerp(nXL, dXL, animProgress);
    float tXR = PApplet.lerp(nXR, dXR, animProgress);
    float tY  = PApplet.lerp(nYL, dY,  animProgress);

    app.line(tXL,tY,bLL,baseY);
    app.bezier(tXL,tY,-95,-60,-80,-40,bRL,baseY);
    app.line(tXR,tY,bRR,baseY);
    app.bezier(tXR,tY, 95,-60, 80,-40,bLR,baseY);

    app.popMatrix();
  }
}
