# リポジトリ構造定義書 (Repository Structure Document)

## 目的と前提

本書は、Cat Live Wallpaper プロジェクトの推奨リポジトリ構造を定義する。2026-06-07 時点のリポジトリは docs 先行フェーズであり Android アプリモジュールは未作成だが、今後の実装で採用する目標構造をここで固定する。

### 現状と目標

| 項目 | 現状 | 目標 |
|------|------|------|
| 永続ドキュメント | `docs/` に保存済み | 継続運用 |
| 作業単位ドキュメント | `.steering/` を使用 | 継続運用 |
| Android アプリ本体 | 未作成 | `app/` モジュールを追加 |
| CI 設定 | `.github/` 配下を利用可能 | lint / test / build を自動化 |

## ルート構造

```text
project-root/
├── app/                              # Android アプリモジュール
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/example/catlivewallpaper/
│       │   │   ├── wallpaper/
│       │   │   ├── orchestration/
│       │   │   ├── logic/
│       │   │   ├── render/
│       │   │   │   └── assets/
│       │   │   └── model/
│       │   └── res/
│       │       ├── drawable-nodpi/
│       │       ├── values/
│       │       └── xml/
│       ├── test/java/com/example/catlivewallpaper/
│       └── androidTest/java/com/example/catlivewallpaper/
├── docs/                             # 永続ドキュメント
├── .steering/                        # 作業単位ドキュメント
├── .claude/                          # スキル・テンプレート
├── .github/workflows/                # CI 設定
├── gradle/                           # Gradle Wrapper 付帯ファイル
├── build.gradle.kts                  # ルートビルド設定
├── settings.gradle.kts               # モジュール定義
├── gradle.properties                 # Gradle 共通設定
├── README.md                         # セットアップと概要
├── eslint.config.js                  # リポジトリ補助ツール設定
└── vitest.config.ts                  # リポジトリ補助ツール設定
```

### package 名の扱い

- 例示では `com.example.catlivewallpaper` を使う。
- 実際の `applicationId` が確定したら、`java/` 配下と Manifest の package 参照を一括で揃える。
- package 名が決まるまでは、ドキュメントとサンプルコードで同じ仮名を使い、揺れを作らない。

## ディレクトリ詳細

### app/

**役割**: ライブ壁紙 APK / AAB を生成する Android アプリ本体。

**配置するもの**:
- `build.gradle.kts`: モジュール依存、SDK レベル、ビルドタイプ
- `src/main/`: 本番コードとリソース
- `src/test/`: JVM ベースのユニットテスト
- `src/androidTest/`: 実機またはエミュレータ前提の統合テスト

**配置しないもの**:
- プロジェクト全体の設計書
- 作業メモ
- 一時的な検証スクリプト

### docs/

**役割**: プロジェクト全体で参照する永続ドキュメント群。

**必須ファイル**:
- `product-requirements.md`
- `functional-design.md`
- `architecture.md`
- `repository-structure.md`
- `development-guidelines.md`
- `glossary.md`

**関連ディレクトリ**:
- `ideas/`: 初期メモや検討メモ
- `ticket/`: 実装順と依存関係を管理するチケット群

**運用ルール**:
- プロダクトや設計の前提が変わった場合は、関連ドキュメントを同時に更新する。
- アイデア段階のメモは `docs/ideas/` に置き、正式決定事項だけを正式ドキュメントへ昇格する。
- `docs/ticket/` は実行計画であり、仕様や技術判断の正本にはしない。

### .steering/

**役割**: 作業単位の要求、設計、タスクリストを保存する短期ドキュメント群。

**構造**:
```text
.steering/
└── YYYYMMDD-task-name/
    ├── requirements.md
    ├── design.md
    └── tasklist.md
```

**運用ルール**:
- ユーザー指示ごとに新しいディレクトリを作る。
- `tasklist.md` の完了状態を実作業と同期させる。
- 作業完了後も履歴として保持し、後続の判断材料にする。

### .github/workflows/

**役割**: lint、テスト、ビルド、将来の Play 配布準備を自動化する。

**想定ファイル**:
- `android-ci.yml`: ユニットテスト、lint、assembleDebug
- `release.yml`: タグ作成後のリリースビルドと配布補助

### .claude/

**役割**: スキル、テンプレート、エージェント運用の補助情報を格納する。

**扱い**:
- 実装コードは置かない。
- プロジェクト運用ルールのテンプレート変更時のみ更新する。

## Android ソース構造

### wallpaper/

**役割**: Android 統合レイヤー。

**主な配置ファイル**:
- `CatWallpaperService.kt`
- `CatWallpaperEngine.kt`

**責務**:
- `WallpaperService` と `Engine` のライフサイクルを扱う。
- `onVisibilityChanged()`、`onSurfaceChanged()`、`onOffsetsChanged()`、`onTouchEvent()` を受け取る。
- オーケストレーション層へ状態更新を委譲する。

**依存関係**:
- 依存可能: `orchestration/`, `logic/`, `render/`, `model/`
- 依存禁止: `androidTest/`, `docs/`, `.steering/`

### orchestration/

**役割**: フレーム更新順序と状態同期の調停。

**主な配置ファイル**:
- `FrameTicker.kt`
- `SceneUpdateCoordinator.kt` または同等の補助クラス

**責務**:
- `drawFrame()` の起点となるスケジューリングを行う。
- ドメインロジックと描画レイヤーの呼び出し順を固定する。
- 表示状態に応じて次フレーム予約を開始・停止する。

**依存関係**:
- 依存可能: `logic/`, `render/`, `model/`
- 依存禁止: `wallpaper/` への逆依存、永続化ライブラリへの直接依存

### logic/

**役割**: 猫の振る舞いと入力反応の決定。

**主な配置ファイル**:
- `CatBehaviorController.kt`
- `TouchReactionController.kt`
- `SceneThemeResolver.kt`

**責務**:
- `WALK` / `IDLE` / `PLAY` の遷移を決定する。
- タップ反応と毛糸玉表示の条件を決定する。
- MVP では `DAY` 固定、P1 以降で `NIGHT` を扱える前提を保つ。

**依存関係**:
- 依存可能: `model/`
- 依存禁止: `wallpaper/`, `render/` への直接依存

### render/

**役割**: Canvas 描画とレイアウト計算。

**主な配置ファイル**:
- `SceneRenderer.kt`
- `BitmapRepository.kt`
- `BackgroundLayoutCalculator.kt`

**責務**:
- 背景切り出し、猫スプライト描画、毛糸玉描画を行う。
- Bitmap を初回に読み込み、毎フレーム再読込しない。
- サーフェスサイズやオフセットに応じた描画座標を計算する。

**依存関係**:
- 依存可能: `model/`, `render/assets/`, Android Graphics API
- 依存禁止: `logic/` の状態遷移ルールへの直接依存

### render/assets/

**役割**: Bitmap ハンドルとアセット集合の表現。

**主な配置ファイル**:
- `AssetSet.kt`
- `BitmapDecodeOptions.kt`

**責務**:
- `Bitmap` を含むアセット表現を render 層内へ閉じ込める。
- `backgroundNight` のような任意アセットを nullable として扱う。

### model/

**役割**: immutable なランタイム状態モデル。

**主な配置ファイル**:
- `SceneState.kt`
- `CatStateSnapshot.kt`
- `ToyState.kt`
- `CatMode.kt`
- `FacingDirection.kt`
- `SceneTheme.kt`

**責務**:
- 描画や振る舞いの元になるデータ構造を定義する。
- 可能な限り Android UI クラスへの依存を避ける。
- `copy()` 前提の状態更新をしやすい形に保つ。

## リソース配置

### res/drawable-nodpi/

**役割**: ピクセルベースの手描き風 PNG 素材を保存する。

**配置ファイル**:
- `background_room_day.png`
- `background_room_night.png`
- `cat_walk_1.png`
- `cat_walk_2.png`
- `cat_sit.png`
- `cat_play_1.png`
- `cat_play_2.png`
- `toy_yarn.png`

**ルール**:
- MVP の必須素材は昼背景、歩き 2 枚、座り 1 枚、遊び 2 枚、毛糸玉。
- 夜背景は P1 の受け皿としてファイル名だけ先行確保してよい。
- 素材名は lower_snake_case に統一する。

### res/values/

**役割**: 文字列、色、寸法などの定数リソース。

**主な配置ファイル**:
- `strings.xml`
- `colors.xml`
- `dimens.xml` (必要時のみ)

### res/xml/

**役割**: ライブ壁紙メタデータや将来の設定 XML を置く。

**主な配置ファイル**:
- `wallpaper.xml`
- `preferences.xml` (P1 の設定画面導入後)

## テスト配置

### app/src/test/java/

**役割**: JVM で実行可能なユニットテスト。

**対象**:
- `logic/` の状態遷移
- `orchestration/` のフレーム間隔算出
- `model/` の制約検証

**命名規則**:
- `CatBehaviorControllerTest.kt`
- `FrameTickerTest.kt`

### app/src/androidTest/java/

**役割**: 実機またはエミュレータ前提の統合テスト。

**対象**:
- 壁紙プレビュー起動
- `onSurfaceChanged()` 後の再レイアウト
- タップ反応の初回可視化
- Bitmap 読み込みと解放

**命名規則**:
- `CatWallpaperEngineInstrumentedTest.kt`
- `SceneRendererInstrumentedTest.kt`

## ファイル配置規則

| ファイル種別 | 配置先 | 命名規則 | 例 |
|-------------|--------|---------|----|
| WallpaperService / Engine | `app/src/main/java/.../wallpaper/` | PascalCase + 役割接尾辞 | `CatWallpaperService.kt` |
| フレーム更新制御 | `app/src/main/java/.../orchestration/` | PascalCase | `FrameTicker.kt` |
| 行動制御 | `app/src/main/java/.../logic/` | PascalCase + Controller | `TouchReactionController.kt` |
| 描画処理 | `app/src/main/java/.../render/` | PascalCase + Renderer / Repository | `SceneRenderer.kt` |
| 状態モデル | `app/src/main/java/.../model/` | PascalCase | `SceneState.kt` |
| 画像素材 | `app/src/main/res/drawable-nodpi/` | lower_snake_case | `background_room_day.png` |
| ユニットテスト | `app/src/test/java/.../` | `ClassNameTest.kt` | `CatBehaviorControllerTest.kt` |
| 統合テスト | `app/src/androidTest/java/.../` | `ClassNameInstrumentedTest.kt` | `SceneRendererInstrumentedTest.kt` |

## 命名規則

### ディレクトリ名

- package ディレクトリは lower-case を使う。
- 役割が明確な名前を使い、`utils`、`helpers`、`misc` は作らない。
- 新しい機能追加時も、まず既存レイヤーへ責務を収められるかを確認する。

### ファイル名

- Kotlin クラスファイルは PascalCase を使う。
- 単一の責務を表す接尾辞を付ける。
  - `...Service`
  - `...Engine`
  - `...Controller`
  - `...Renderer`
  - `...Repository`
- Enum や data class は概念名をそのまま使う。

## 依存関係ルール

```text
wallpaper -> orchestration -> logic -> model
wallpaper -> render -> model
orchestration -> render -> model
logic -/-> render
model -/-> wallpaper, orchestration, logic, render
```

### 依存ルール詳細

| レイヤー | 依存可能 | 依存禁止 |
|---------|----------|----------|
| `wallpaper/` | `orchestration/`, `logic/`, `render/`, `model/` | テストコード、docs 逆参照 |
| `orchestration/` | `logic/`, `render/`, `model/` | `wallpaper/` への逆依存 |
| `logic/` | `model/` | `render/`, `wallpaper/` |
| `render/` | `model/`, Android Graphics API | `logic/` の状態遷移実装 |
| `model/` | Kotlin 標準ライブラリ | Android ライフサイクル、描画 API |

## スケーリング方針

### P1 の設定画面追加時

- `settings/` package を追加し、壁紙本体から分離する。
- `DataStore` 関連コードは `settings/` または `data/` に閉じ込め、MVP のランタイム状態とは分離する。
- 設定 UI リソースは `res/xml/` と必要な `res/layout/` を追加して管理する。

### テーマ追加時

- 追加背景や天候差分は `drawable-nodpi/` へ追加する。
- `SceneThemeResolver` の分岐追加だけで済むよう、描画パイプラインの責務は増やしすぎない。

## 関連ドキュメント

- 日々の実装規約、Git / PR 運用、テスト運用の詳細は [development-guidelines.md](./development-guidelines.md) を参照する。
- 状態モデルとレイヤー責務の技術判断は [architecture.md](./architecture.md) と [functional-design.md](./functional-design.md) を参照する。

## 非推奨パターン

- `logic/` から `Canvas` や `BitmapFactory` を直接呼ぶこと
- `render/` に猫の行動確率や遷移時間を埋め込むこと
- `model/` に mutable な共有状態を置くこと
- 一時的な検証コードを `app/src/main/` に残し続けること
