// TextDisplay.java - テキスト表示システム
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import java.util.ArrayList;
import java.util.List;

/**
 * メインコードから使いやすいテキスト表示インターフェース
 */
public class TextDisplay {
  private final PApplet app;
  private final float scale;
  private final TextScrollerSimple scroller;
  
  // 表示位置の設定
  private final float textX;
  private final float textY;
  
  // 自動送り設定
  private boolean autoAdvanceEnabled = true;
  private int autoAdvanceDelay = 180; // 3秒（60fps * 3）
  
  public TextDisplay(PApplet app, float globalScale) {
    this.app = app;
    this.scale = globalScale;
    
    // テキスト表示位置を計算（画面下部中央）
    this.textX = app.width * 0.5f;
    this.textY = app.height * 0.75f;
    
    // TextScrollerSimpleを初期化
    this.scroller = new TextScrollerSimple(app, textX, textY, globalScale);
    
    // 自動送り設定を適用
    this.scroller.setAutoAdvance(autoAdvanceEnabled, autoAdvanceDelay);
  }
  
  /**
   * メッセージを表示開始
   * @param message 表示したいテキスト（改行 \n 対応）
   */
  public void showMessage(String message) {
    scroller.setText(message);
  }
  
  /**
   * 次のページに進む（スペースキー等で呼び出し）
   */
  public void nextPage() {
    scroller.nextPage();
  }
  
  /**
   * 更新処理（draw()内で呼び出し）
   */
  public void update() {
    scroller.update();
  }
  
  /**
   * 描画処理（draw()内で呼び出し）
   */
  public void draw() {
    scroller.draw();
  }
  
  /**
   * タイピング速度を変更
   * @param speed 1フレームあたりの文字数（1〜10推奨）
   */
  public void setSpeed(int speed) {
    scroller.setSpeed(speed);
  }
  
  /**
   * 現在テキスト表示中かどうか
   * @return 表示中ならtrue
   */
  public boolean isDisplaying() {
    return scroller.hasContent();
  }
  
  /**
   * 自動文字送りの有効/無効を設定
   * @param enabled true=自動送り有効, false=手動送りのみ
   */
  public void setAutoAdvance(boolean enabled) {
    this.autoAdvanceEnabled = enabled;
    scroller.setAutoAdvance(enabled, autoAdvanceDelay);
  }
  
  /**
   * 自動文字送りの待機時間を設定
   * @param delayFrames 待機フレーム数（60fps基準, 180=3秒）
   */
  public void setAutoAdvanceDelay(int delayFrames) {
    this.autoAdvanceDelay = delayFrames;
    scroller.setAutoAdvance(autoAdvanceEnabled, delayFrames);
  }
  
  /**
   * 自動文字送りの設定を一度に行う
   * @param enabled true=自動送り有効
   * @param delaySeconds 待機秒数（例: 3.0f = 3秒）
   */
  public void setAutoAdvance(boolean enabled, float delaySeconds) {
    this.autoAdvanceEnabled = enabled;
    this.autoAdvanceDelay = (int)(delaySeconds * 60); // 秒をフレーム数に変換
    scroller.setAutoAdvance(enabled, autoAdvanceDelay);
  }
}

/**
 * 実際のテキスト処理を行うクラス
 */
class TextScrollerSimple {
  // Processing参照
  private final PApplet app;
  
  // 表示設定
  private final float x, y;
  private final float scale;
  private final PFont font;
  
  // ページ管理
  private final List<String> pages = new ArrayList<>();
  private int currentPageIndex = 0;
  
  // アニメーション状態
  private String currentPageText = "";
  private String displayText = "";
  private int characterIndex = 0;
  private int animationTimer = 0;
  private int typingSpeed = 3;
  private boolean isPageComplete = false;
  
  // 自動送り設定
  private boolean autoAdvanceEnabled = false;
  private int autoAdvanceDelay = 180; // 3秒
  private int autoAdvanceTimer = 0;
  
  public TextScrollerSimple(PApplet app, float x, float y, float globalScale) {
    this.app = app;
    this.x = x;
    this.y = y;
    this.scale = globalScale;
    
    // フォント設定
    float fontSize = 28 * globalScale;
    this.font = app.createFont("SansSerif", fontSize);
  }
  
  /**
   * テキストを設定して表示開始
   */
  public void setText(String text) {
    if (text == null || text.trim().isEmpty()) {
      pages.clear();
      currentPageText = "";
      displayText = "";
      return;
    }
    
    // ページ分割
    divideIntoPages(text);
    
    // 最初のページから開始
    currentPageIndex = 0;
    startCurrentPage();
  }
  
  /**
   * テキストをページに分割
   */
  private void divideIntoPages(String text) {
    pages.clear();
    
    // フォント設定してから幅を測定
    app.textFont(font);
    float maxWidth = app.width * 0.8f; // 画面幅の80%を使用
    
    // 改行で段落に分割
    String[] paragraphs = text.split("\\n");
    
    for (String paragraph : paragraphs) {
      if (paragraph.trim().isEmpty()) {
        continue;
      }
      
      // 長い段落は文字数で分割
      String currentLine = "";
      for (char ch : paragraph.toCharArray()) {
        String testLine = currentLine + ch;
        
        if (app.textWidth(testLine) > maxWidth && !currentLine.isEmpty()) {
          // 行が長すぎる場合は分割
          pages.add(currentLine.trim());
          currentLine = String.valueOf(ch);
        } else {
          currentLine += ch;
        }
      }
      
      // 残りの文字を追加
      if (!currentLine.trim().isEmpty()) {
        pages.add(currentLine.trim());
      }
    }
    
    // 空の場合は空文字を追加
    if (pages.isEmpty()) {
      pages.add("");
    }
  }
  
  /**
   * 現在のページのタイピングアニメーション開始
   */
  private void startCurrentPage() {
    if (currentPageIndex < pages.size()) {
      currentPageText = pages.get(currentPageIndex);
    } else {
      currentPageText = "";
    }
    
    displayText = "";
    characterIndex = 0;
    animationTimer = 0;
    autoAdvanceTimer = 0; // 自動送りタイマーリセット
    isPageComplete = false;
  }
  
  /**
   * 次のページに進む
   */
  public void nextPage() {
    if (!isPageComplete) {
      // アニメーション中の場合は即座に完了
      displayText = currentPageText;
      characterIndex = currentPageText.length();
      isPageComplete = true;
      autoAdvanceTimer = 0; // 自動送りタイマーリセット
    } else if (hasNextPage()) {
      // 次のページがある場合は進む
      currentPageIndex++;
      startCurrentPage();
    }
  }
  
  /**
   * 更新処理
   */
  public void update() {
    // タイピングアニメーション
    if (!isPageComplete && characterIndex < currentPageText.length()) {
      animationTimer++;
      
      if (animationTimer % typingSpeed == 0) {
        characterIndex++;
        displayText = currentPageText.substring(0, characterIndex);
      }
    }
    
    // アニメーション完了チェック
    if (!isPageComplete && characterIndex >= currentPageText.length()) {
      isPageComplete = true;
      autoAdvanceTimer = 0; // 完了時に自動送りタイマー開始
    }
    
    // 自動送り処理
    if (isPageComplete && autoAdvanceEnabled && hasNextPage()) {
      autoAdvanceTimer++;
      if (autoAdvanceTimer >= autoAdvanceDelay) {
        nextPage(); // 自動的に次のページへ
      }
    }
  }
  
  /**
   * 描画処理
   */
  public void draw() {
    if (displayText.isEmpty()) {
      return;
    }
    
    app.pushStyle();
    app.textFont(font);
    app.textAlign(PConstants.CENTER, PConstants.CENTER);
    app.fill(255);
    
    // テキスト描画
    app.text(displayText, x, y);
    
    // 続きがある場合の表示（点滅）
    if (isPageComplete && hasNextPage()) {
      if (app.frameCount % 60 < 30) { // 0.5秒間隔で点滅
        app.text("▼", x, y + 50 * scale);
      }
    }
    
    app.popStyle();
  }
  
  /**
   * タイピング速度設定
   */
  public void setSpeed(int speed) {
    this.typingSpeed = Math.max(1, speed);
  }
  
  /**
   * 次のページがあるかチェック
   */
  private boolean hasNextPage() {
    return currentPageIndex < pages.size() - 1;
  }
  
  /**
   * 表示すべきコンテンツがあるかチェック
   */
  public boolean hasContent() {
    return !pages.isEmpty() && !currentPageText.isEmpty();
  }
  
  /**
   * 自動送り設定
   * @param enabled 自動送り有効/無効
   * @param delayFrames 待機フレーム数
   */
  public void setAutoAdvance(boolean enabled, int delayFrames) {
    this.autoAdvanceEnabled = enabled;
    this.autoAdvanceDelay = delayFrames;
    this.autoAdvanceTimer = 0;
  }
}
