// Decorator_EarTwitchDecorator.java
// ------------------------------------------------------
// 耳が「ピクッ」と動くデコレータ
// ・一定時間ごとに耳を上げ下げするアニメーションを付与
// ・update 内で state を管理し、耳の animProgress を制御する
// ------------------------------------------------------

public class EarTwitchDecorator extends PartDecorator {
  private int state = 0;    // 0:待機  1:上げ  2:戻し
  private int timer;
  private int count = 0;    // 繰り返し回数
  private float localAnim = 0f;

  public EarTwitchDecorator(FacePart part) {
    super(part);
    timer = (int) app.random(180, 600); // ランダムな間隔で発動
  }

  @Override
  public void update() {
    if (state == 0) { // 待機中
      timer--;
      if (timer <= 0) {
        state = 1;
        count = 2;
        localAnim = 0f;
      }
    } else if (state == 1) { // 耳を上げる
      localAnim += 0.1f;
      if (localAnim >= 1f) {
        localAnim = 1f;
        state = 2;
      }
    } else if (state == 2) { // 耳を戻す
      localAnim -= 0.2f;
      if (localAnim <= 0f) {
        localAnim = 0f;
        count--;
        if (count > 0) {
          state = 1;
        } else {
          state = 0;
          timer = (int) app.random(180, 600);
        }
      }
    }
    decoratedPart.setAnimProgress(localAnim);
  }
}
