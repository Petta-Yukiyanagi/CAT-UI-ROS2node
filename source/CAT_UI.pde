// ==========================
// フィールド
// ==========================
Character character;
TextDisplay textDisplay;
float globalScale;

FaceIPC ipc;

// ==========================
// setup
// ==========================
void setup() {
  fullScreen();
  smooth(8);

  // ★ 正しい IPC ルート（PC依存なし）
  println("IPC ROOT = " + dataPath("ipc"));

  globalScale = min(width / 400.0, height / 300.0);

  character = new Character(this, globalScale);
  textDisplay = new TextDisplay(this, globalScale);
  textDisplay.setAutoAdvance(true, 2.5f);

  try {
    // ★ ここが超重要：絶対に "ipc" を直接渡さない
    ipc = new FaceIPC(dataPath("ipc"));
  } catch (Exception e) {
    e.printStackTrace();
  }
}

// ==========================
// draw
// ==========================
void draw() {
  background(0);

  handleIPC();

  character.update();
  character.draw();

  textDisplay.update();
  textDisplay.draw();
}

// ==========================
// IPC 処理
// ==========================
void handleIPC() {
  if (ipc == null) return;

  try {
    for (UICommand cmd : ipc.poll()) {

      // face ID → 表情
      if (cmd.faceId >= 0) {
        character.setExpression(faceIdToExpression(cmd.faceId));
      }

      // テキスト表示
      if (cmd.text != null) {
        textDisplay.showMessage(cmd.text);
      }

      // reset_after
      if (cmd.resetAfter > 0) {
        scheduleReset(cmd.resetAfter);
      }
    }
  } catch (Exception e) {
    e.printStackTrace();
  }
}

// ==========================
// face ID → Expression
// ==========================
String faceIdToExpression(int id) {
  switch (id) {
    case 0: return "NORMAL";
    case 1: return "QUESTION";
    case 2: return "HAPPY";
    case 3: return "SMILE";
    case 4: return "SLEEPING";
    default: return "NORMAL";
  }
}

// ==========================
// reset_after 処理
// ==========================
void scheduleReset(float seconds) {
  int delayMs = int(seconds * 1000);

  new java.util.Timer().schedule(
    new java.util.TimerTask() {
      public void run() {
        character.setExpression("NORMAL");
      }
    },
    delayMs
  );
}

// ==========================
// キー入力（デバッグ用）!
// ==========================
void keyPressed() {
  if (key == '1') {
    character.setExpression("QUESTION");
    textDisplay.showMessage("デバッグ入力");
    scheduleReset(2.0);
  }
}
