import processing.core.PApplet;
public class Nose extends FacePart {
  private final float w, h;

  public Nose(PApplet app, float s) {         // ← factory 用
    this(app, s, 20, 12); // デフォルトサイズは適宜
  }

  public Nose(PApplet app, float s, float w, float h) { // ← 既存のも public に
    super(app, s);
    this.w = w;
    this.h = h;
  }

  public void draw() {
    refreshCenter();
    app.pushMatrix();
    app.translate(centerX, centerY);
    app.scale(s);
    app.fill(255);
    app.noStroke();
    app.ellipse(0, 10, w, h);
    app.popMatrix();
  }
}
