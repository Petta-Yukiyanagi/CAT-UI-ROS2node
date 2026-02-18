// Mouth.java
import processing.core.PApplet;
import processing.core.PConstants;

public class Mouth extends FacePart {
  // "normal" | "happy" | "surprised" | "nikoSmile"
  private final String type;

  public Mouth(PApplet app, float s, String type) {
    super(app, s);
    this.type = (type == null ? "" : type);
  }

  @Override
  public void draw() {
    refreshCenter();

    app.pushMatrix();
    app.translate(centerX, centerY);
    app.scale(s);

    app.pushStyle();            // ← 他パーツへのスタイル汚染を防ぐ

    if ("nikoSmile".equalsIgnoreCase(this.type)) {
      drawNikoSmile();
    } else if ("happy".equalsIgnoreCase(this.type)) {
      // 旧「happy」相当：口角を上げたシンプルな線
      app.stroke(255);
      app.noFill();
      app.line(0, 10,  20, 30);
      app.line(0, 10, -20, 30);
    } else if ("surprised".equalsIgnoreCase(this.type)) {
      // びっくり：小さめの縦楕円
      app.noStroke();
      app.fill(255);
      app.ellipse(0, 32, 15, 22);
    } else {
      // normal：軽い口カーブ（元コード準拠）
      app.stroke(255);
      app.noFill();
      app.strokeWeight(6);   // ローカル座標前提で適度に
      app.bezier( 0,  7,   8, 30,  20, 30,  30, 30);
      app.bezier( 0,  7,  -8, 30, -20, 30, -30, 30);
    }

    app.popStyle();
    app.popMatrix();
  }

  // ————————————————————————————————————————————————
  // 「にこっ」と笑った口（横広＋少し開口＋控えめな牙）
  // ————————————————————————————————————————————————
  private void drawNikoSmile() {
    // 上唇（白い線・太め）
    app.noFill();
    app.stroke(255);
    app.strokeWeight(6);
    app.bezier( 0,  7,   8, 20,  20, 20,  20, 20);
    app.bezier( 0,  7,  -8, 20, -20, 20, -20, 20);

    // 牙（控えめな三角を2つ）
    app.fill(255);
    app.noStroke();
    app.triangle(-12, 18, -6, 18, -9, 25);  // 左
    app.triangle( 6, 18,  12, 18,  9, 25);  // 右

    // 下唇（白い線・太め）
    app.noFill();
    app.stroke(255);
    app.strokeWeight(6);
    app.bezier(  15,  20,  15, 30,  15, 45,  0, 45);
    app.bezier( -15,  20,  -15, 30,  -15, 45,  0, 45);

  }
}
