// Decorator_QuestionMarkDecorator.java
// ------------------------------------------------------
// 「？」を頭上に浮かべるエフェクト・デコレータ
// ・EmptyPart を基点にする
// ・フォントは共有（Fonts.ensureLoaded/app.textFont(Fonts.funwari())）
// ------------------------------------------------------

import processing.core.PApplet;
import processing.core.PConstants;

public class QuestionMarkDecorator extends PartDecorator {
  private final float[] relX = new float[3];
  private final float[] relY = new float[3];
  private float qMarkAlpha = 0;
  private boolean qMarksInitialized = false;

  public QuestionMarkDecorator(PApplet app, float s) {
    super(new EmptyPart(app, s));
    Fonts.ensureLoaded(app);            // ← 共有フォントをロード（初回のみ）
  }

  @Override
  public void update() {
    if (!qMarksInitialized && qMarkAlpha <= 0) {
      float arcRadius = 70.0f * s;
      relX[0] = PApplet.cos(-PConstants.PI * 0.20f) * arcRadius; relY[0] = PApplet.sin(-PConstants.PI * 0.20f) * arcRadius;
      relX[1] = PApplet.cos(-PConstants.PI * 0.35f) * arcRadius; relY[1] = PApplet.sin(-PConstants.PI * 0.35f) * arcRadius;
      relX[2] = PApplet.cos(-PConstants.PI * 0.50f) * arcRadius; relY[2] = PApplet.sin(-PConstants.PI * 0.50f) * arcRadius;
      qMarkAlpha = 255;
      qMarksInitialized = true;
    }
    if (qMarkAlpha > 0) {
      relX[1] += 0.9f * s;  relY[1] -= 0.9f * s;
      if (qMarkAlpha < 170) { relX[0] += 1.3f * s; relY[0] -= 0.3f * s; }
      if (qMarkAlpha < 100) { relX[2] += 0.3f * s; relY[2] -= 1.3f * s; }
      qMarkAlpha -= 3.0f;
    } else {
      qMarksInitialized = false;
    }
  }

  @Override
  public void draw() {
    ((FacePart)decoratedPart).refreshCenter();
    if (qMarkAlpha <= 0) return;

    float offsetX = 100 * s;
    float cx = ((FacePart)decoratedPart).centerX;
    float cy = ((FacePart)decoratedPart).centerY;

    app.pushStyle();
    app.noStroke();
    app.fill(255, qMarkAlpha);
    app.textFont(Fonts.funwari()); // ← 共有フォントを使用
    app.textSize(100 * s);         // ← サイズは毎回ここで調整

    app.text("?", cx + relX[1] + offsetX, cy + relY[1]);
    if (qMarkAlpha < 170) app.text("?", cx + relX[0] + offsetX, cy + relY[0]);
    if (qMarkAlpha < 100) app.text("?", cx + relX[2] + offsetX, cy + relY[2]);

    app.popStyle();
  }
}
