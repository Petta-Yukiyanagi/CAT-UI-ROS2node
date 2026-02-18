// Decorator_ZzzDecorator.java
// ------------------------------------------------------
// 「Zzz…」のスリープエフェクト（EmptyPartを基点）
// ・フォントは共有（Fonts）を使用し、毎フレーム textSize で調整
// ------------------------------------------------------

import processing.core.PApplet;
import processing.core.PConstants;

public class ZzzDecorator extends PartDecorator {
  private float relY, alpha, sway, pulse;
  private int pulseIndex;

  public ZzzDecorator(PApplet app, float s) {
    super(new EmptyPart(app, s));
    Fonts.ensureLoaded(app);   // 共有フォントを一度だけロード
    relY = -40 * s;
    alpha = 255;
    sway  = 0;
    pulse = 0;
    pulseIndex = 0;
  }

  @Override
  public void update() {
    sway  += 0.05f;       // 左右のゆらぎ
    relY  -= 0.6f * s;    // 少しずつ上へ
    alpha -= 1.0f;        // フェードアウト
    if (alpha <= 0) {     // ループ
      relY  = -40 * s;
      alpha = 255;
    }
    pulse += 0.1f;        // サイズの脈動
    if (pulse >= PConstants.PI) {
      pulse = 0;
      pulseIndex = (pulseIndex + 1) % 3;
    }
  }

  @Override
  public void draw() {
    decoratedPart.refreshCenter();
    float cx = decoratedPart.centerX;
    float cy = decoratedPart.centerY;
    float zx = cx + 80 * s + PApplet.sin(sway) * 15 * s;

    float baseSize = 50 * s;
    float p = PApplet.sin(pulse) * 20 * s;
    float z1 = (pulseIndex == 0 ? baseSize + p : baseSize);
    float z2 = (pulseIndex == 1 ? baseSize + p : baseSize);
    float z3 = (pulseIndex == 2 ? baseSize + p : baseSize);

    app.pushStyle();
    app.noStroke();
    app.fill(255, alpha);
    app.textFont(Fonts.funwari());   // 共有フォント

    app.textSize(z1);
    app.text("Z", zx, cy + relY);

    app.textSize(z2);
    app.text("z", zx + 35 * s, cy + relY + 8 * s);

    app.textSize(z3);
    app.text("z", zx + 65 * s, cy + relY + 16 * s);

    app.textSize(38 * s);
    app.text("...", zx + 95 * s, cy + relY + 18 * s);

    app.popStyle();
  }
}
