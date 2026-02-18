// Eyes.java
import processing.core.PApplet;
import processing.core.PConstants;

public class Eyes extends FacePart {
  private final float w, h;           // ← 「素の直径」(s を掛けない)
  public Eyes(PApplet app, float s) { // factory 用の public (PApplet,float)
    super(app, s);
    this.w = 35;                      // 好みで調整（直径）
    this.h = 35;
  }

  @Override
  public void draw() {
    refreshCenter();

    app.pushMatrix();
    app.translate(centerX, centerY);
    app.scale(s);                     // ← スケールはここで一括

    app.pushStyle();                  // ← スタイルを局所化（他のパーツの影響を遮断）
    app.ellipseMode(PConstants.CENTER); // ← 直径指定に固定
    app.noStroke();
    app.fill(255);

    app.ellipse(-55, 0, w, h);        // ← w,h は素の直径
    app.ellipse( 55, 0, w, h);

    app.popStyle();
    app.popMatrix();
  }
}
