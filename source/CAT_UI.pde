// ==========================
// フィールド
// ==========================
Character character;
TextDisplay textDisplay;
float globalScale;

FaceIPC ipc;

// UI設計の基準サイズ（論理解像度）
static final int BASE_W = 400;
static final int BASE_H = 300;

// IPC ポーリング制御（無駄に毎フレーム読まない）
int ipcIntervalMs = 50;   // 20Hz
int lastIpcTime = 0;

// ==========================
// setup
// ==========================
void setup() {
  // ★ 画面サイズに自動追従（fullScreen は使わない）
  size(displayWidth, displayHeight);

  // ★ 軽量・安定設定
  frameRate(20);
  noSmooth();

  println("IPC ROOT = " + dataPath("ipc"));

  // ★ スケール計算（モニタが変わってもOK）
  globalScale = min(
    width  / (float)BASE_W,
    height / (float)BASE_H
  );

  character = new Character(this, globalScale);
  textDisplay = new TextDisplay(this, globalScale);
  textDisplay.setAutoAdvance(true, 2.5f);

  try {
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

  // ★ IPC は一定間隔で読む
  if (millis() - lastIpcTime > ipcIntervalMs) {
    handleIPC();
    lastIpcTime = millis();
  }

  // ★ UI はスケールして描画
  pushMatrix();
  scale(globalScale);

  character.update();
  character.draw();

  textDisplay.update();
  textDisplay.draw();

  popMatrix();
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
// キー入力（デバッグ用）
// ==========================
void keyPressed() {
  if (key == '1') {
    character.setExpression("QUESTION");
    textDisplay.showMessage("デバッグ入力");
    scheduleReset(2.0);
  }
}