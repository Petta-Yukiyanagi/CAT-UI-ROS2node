// Decorator_BlinkingDecorator.java
// ------------------------------------------------------
// まばたきを表現するデコレータ
// ・短い間だけ目を閉じる矩形を描画し、通常時は元の目を描画
// ・update は特に不要なので draw のみオーバーライド
// ------------------------------------------------------

import processing.core.PApplet;
import processing.core.PConstants;

public class BlinkingDecorator extends PartDecorator {

  public BlinkingDecorator(FacePart part) {
    super(part);
  }

  @Override
  public void draw() {
    int cycleFrame = app.frameCount % 180;
    boolean isBlink = (cycleFrame > 0 && cycleFrame < 10);

    if (isBlink) {
      refreshCenter();
      app.pushMatrix();
      app.translate(centerX, centerY);
      app.scale(s);
      app.noStroke();
      app.fill(255);
      app.rectMode(PConstants.CORNER);
      app.rect(-70, -3, 40, 6);
      app.rect( 30, -3, 40, 6);
      app.popMatrix();
      return;
    }

    // 通常時は装飾対象（素の目）を描画
    decoratedPart.draw();
  }
}
