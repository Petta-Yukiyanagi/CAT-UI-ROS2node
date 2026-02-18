// Decorator_EmptyPart.java
// ------------------------------------------------------
// スタンドアロン系デコレータ（？やZzzなど）の描画基点に使う
// 「何もしない」プレースホルダパーツ。
// 例：new QuestionMarkDecorator(app, s) の内部で new EmptyPart(app, s) を渡す。
// ------------------------------------------------------

import processing.core.PApplet;

public class EmptyPart extends FacePart {
  public EmptyPart(PApplet app, float s) {
    super(app, s);
  }

  @Override
  public void update() { /* no-op */ }

  @Override
  public void draw() { /* no-op */ }
}
