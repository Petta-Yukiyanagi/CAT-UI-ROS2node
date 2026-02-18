public class EarFlapDecorator extends PartDecorator {
  private static final float UP_SPEED = 0.15f;
  private static final float DOWN_SPEED = 3.0f;

  private int state = 1;      // 1:上げ  2:落下
  private float localAnim = 0f;

  public EarFlapDecorator(FacePart part) { super(part); }

  @Override
  public void update() {
    if (state == 1) {
      localAnim += UP_SPEED;
      if (localAnim >= 1f) { localAnim = 1f; state = 2; }
    } else {
      localAnim -= DOWN_SPEED;
      if (localAnim <= 0f) { localAnim = 0f; state = 1; }
    }
    decoratedPart.setAnimProgress(localAnim);
  }
}
