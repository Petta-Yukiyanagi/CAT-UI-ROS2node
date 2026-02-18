import processing.core.PApplet;

public class SleepEyesDecorator extends PartDecorator {
  public SleepEyesDecorator(FacePart part) { super(part); }

  @Override
  public void draw() {
    refreshCenter();
    app.pushStyle();
    app.pushMatrix();
    app.translate(centerX, centerY);
    app.scale(s);

    app.noFill();
    app.stroke(255);
    app.strokeWeight(6);

    float eyeW = 25;   // 横幅（お好みで調整）
    float eyeH = 5;    // 高さ（薄くする）
    app.ellipse(-50, 0, eyeW, eyeH);
    app.ellipse( 50, 0, eyeW, eyeH);

    app.popMatrix();
    app.popStyle();
  }

  @Override
  public void update() {
    // 目のベースは描かないので特に何もしない（必要ならdecoratedPart.update()を呼んでもOK）
  }
}
