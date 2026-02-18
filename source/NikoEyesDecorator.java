// Decorator_NikoEyesDecorator.java
// にこっと細めた笑い目（白目は描かず、細いカーブだけ描く）

import processing.core.PApplet;

public class NikoEyesDecorator extends PartDecorator {
  public NikoEyesDecorator(FacePart part) { super(part); }

  @Override public void update() { /* no-op */ }

  @Override
  public void draw() {
    refreshCenter();

    app.pushMatrix();
    app.translate(centerX, centerY);
    app.scale(s);

    app.pushStyle();
    app.noFill();
    app.stroke(255);
    app.strokeWeight(6);

    // 左右とも軽く持ち上がったカーブ（目を細めた形）
    // 位置・形はお好みで微調整してください
    app.bezier(-70, -2, -55, -12, -40, -12, -25, -2); // 左目上の線
    app.bezier( 25, -2,  40, -12,  55, -12,  70, -2); // 右目上の線


    app.popStyle();
    app.popMatrix();
  }
}
