// SparkleDecorator.java
// 大きめの白目を描いたうえで、上にキラキラを重ねるデコレータ
// ・基底 Eyes は描かず、このデコレータが目そのものを描く
// ・キラキラは白で塗りつぶし＋黒の縁取りで必ず見える

import processing.core.PApplet;
import processing.core.PConstants;

public class SparkleDecorator extends PartDecorator {
  // 嬉しい用の大きい目サイズ（好みで調整）
  private static final float EYE_W = 45;
  private static final float EYE_H = 45;

  private float sparkleProgress = 0f;
  private int sparkleIndex = 0;

  public SparkleDecorator(FacePart part) { super(part); }

  @Override
  public void update() {
    // 他デコレータ連携のために update は回しておく（目の描画は本クラスで完結）
    decoratedPart.update();
    sparkleProgress += 2f;
    if (sparkleProgress >= PConstants.PI) {
      sparkleProgress = 0f;
      sparkleIndex = (sparkleIndex + 1) % 2;
    }
  }

  @Override
  public void draw() {
    refreshCenter();

    app.pushMatrix();
    app.translate(centerX, centerY);
    app.scale(s);

    // 1) 大きい白目（基底 Eyes は描かない）
    app.pushStyle();
    app.ellipseMode(PConstants.CENTER);
    app.noStroke();
    app.fill(255);
    app.ellipse(-50, 0, EYE_W, EYE_H);
    app.ellipse( 50, 0, EYE_W, EYE_H);
    app.popStyle();

    // 2) キラキラを上に重ねる（白塗り＋黒縁で背景や白目の上でも視認性◎）
    float pulse = PApplet.sin(sparkleProgress) * 3f;
    float large = 12f + (sparkleIndex == 0 ? pulse : 0f);
    float small =  7f + (sparkleIndex != 0 ? pulse : 0f);

    drawDiamond(-60, -8, large);
    drawDiamond(-40, 12, small);
    drawDiamond( 40, -8, large);
    drawDiamond( 60, 12, small);

    app.popMatrix();
  }

  private void drawDiamond(float x, float y, float size) {
    app.pushStyle();
    app.fill(255);
    app.stroke(0);
    app.strokeWeight(2);
    app.beginShape();
    app.vertex(x,             y - size);
    app.vertex(x + size*0.7f, y);
    app.vertex(x,             y + size);
    app.vertex(x - size*0.7f, y);
    app.endShape(PConstants.CLOSE);
    app.popStyle();
  }
}
