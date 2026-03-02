# CAT-UI-ROS2node

このリポジトリは、**猫ロボットシステム全体のうち「UI（表情表示）」を担当するパーツ**です。

ロボットの状態やイベントを、  
ログや数値ではなく **猫の表情と短いメッセージ** として人に伝えるための UI（CAT-UI）を提供します。

---

## このリポジトリの位置づけ

本プロジェクトは、複数のリポジトリから構成される **分割設計のロボットシステム** です。

その中で **CAT-UI-ROS2node は UI 表示専用のパーツ** にあたります。

---

## 関連リポジトリ

### 🐳 ROS2_System_Dockerfile（全部入り・起動用）

**システム全体を起動する Docker 環境**

https://github.com/Petta-Yukiyanagi/ROS2_System_Dockerfile

- ROS 2 本体
- 各種制御・ロジックノード
- UI とのブリッジ
- 本リポジトリ（CAT-UI-ROS2node）を含めて起動

---

### 🔁 CAT-UI-ROS2bridge-node（Bridge）

**ROS2 と UI の間をつなぐブリッジノード**

- ROS2 のトピックを監視
- JSON を生成
- `ipc/broadcast` に書き出す

https://github.com/Petta-Yukiyanagi/CAT-UI-ROS2bridge-node

---

### 🐱 CAT-UI-ROS2node（このリポジトリ）

**UI（猫の表情・テキスト表示）を担当するパーツ**

- CAT-UI（Java / Processing ベース）
- UI 実行に必要なライブラリ群
- UI が読み取る **IPC 用 JSON の例**

---

## システム全体構成図

```
ROS2_System_Dockerfile
 ├─ ROS 2 ノード群
 │   └─ catui_bridge
 │        ↓
 │   ipc/broadcast/*.json
 │        ↓
 └─ CAT-UI-ROS2node
     └─ CAT-UI（表情・テキスト表示）
```

---

## IPC（File IPC / JSON）

CAT-UI は、**`ipc/broadcast` ディレクトリに配置された JSON ファイル**を入力として動作します。

### JSON 例

以下は、UI の動作確認用として使われる JSON の一例です。

```json
{
  "face": 3,
  "text": "反応テスト",
  "reset_after": 3
}
```

- `face`  
  表情ID（数値）。UI 側で定義された表情に対応します。
- `text`  
  画面に表示するメッセージ。
- `reset_after`  
  指定秒数後に表情・表示をリセットします。

---


## 使い方（UI単体）

```bash
chmod +x ./CAT-UI
./CAT-UI
```
