public class EarDroopDecorator extends PartDecorator {
  public EarDroopDecorator(FacePart part) { super(part); }

  @Override
  public void update() {
    // Ears は animProgress（0→1）で形が変わる前提だったので、常に 1 に固定
    decoratedPart.setAnimProgress(1.0f);
  }

  @Override
  public void draw() {
    decoratedPart.draw();   // ベースの耳描画をそのまま使う
  }
}
