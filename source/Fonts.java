// Fonts.java
import processing.core.*;

public final class Fonts {
  private static PFont FUNWARI;

  private Fonts() {}

  public static void ensureLoaded(PApplet app) {
    if (FUNWARI != null) return;
    try {
      // 1) data/ に .vlw があるならこちらが最速・確実
      // 例: Cat_Ending/data/funwariround-48.vlw
      FUNWARI = app.loadFont("funwariround-48.vlw");
    } catch (Exception ignore) {
      // 2) 無ければインストール済みシステムフォント名で作成（適当な代替名に変更可）
      // 第3引数 true でアンチエイリアス
      FUNWARI = app.createFont("Arial", 100, true);
    }
  }

  public static PFont funwari() {
    return FUNWARI;
  }
}
