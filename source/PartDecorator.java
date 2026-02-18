// Decorator_PartDecorator.java
// ------------------------------------------------------
// デコレータの抽象基底クラス
// ・全てのデコレータはこのクラスを継承する
// ・装飾対象（decoratedPart）に update/draw を委譲する基本実装を持つ
// ・アニメ進行度も委譲できるようにしてある
// ------------------------------------------------------

public abstract class PartDecorator extends FacePart {
  protected final FacePart decoratedPart; // 装飾対象のパーツ

  // コンストラクタ：装飾対象の FacePart を受け取る
  public PartDecorator(FacePart part) {
    super(part.app, part.s);
    this.decoratedPart = part;
  }

  // デフォルトの update は装飾対象にそのまま委譲
  @Override
  public void update() { decoratedPart.update(); }

  // デフォルトの draw も装飾対象にそのまま委譲
  @Override
  public void draw() { decoratedPart.draw(); }

  // アニメーション進行度の伝搬
  @Override
  public void setAnimProgress(float p) { decoratedPart.setAnimProgress(p); }
}
