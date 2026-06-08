# 開発ガイドライン (Development Guidelines)

## 目的と適用範囲

本書は Cat Live Wallpaper プロジェクトの実装・レビュー・運用の標準ルールを定義する。現在のリポジトリは docs 先行フェーズだが、今後 Android 実装が追加された際にもそのまま参照できるよう、ドキュメント運用ルールと Kotlin / Android 実装規約を同じ文書で管理する。

### 参照優先順位

設計上の記述が競合した場合は、次の順で優先する。

1. `docs/architecture.md`
2. `docs/functional-design.md`
3. `docs/product-requirements.md`
4. `docs/ideas/initial-requirements.md`

## 開発の基本原則

### docs 先行で進める

- 機能追加や仕様変更は、必要に応じて先に設計書を更新する。
- 実装だけが先行し、ドキュメントが古くなる状態を作らない。
- 一時メモは `.steering/`、長期参照する内容は `docs/` に残す。

### 変更は小さく、責務は分ける

- 1 回の変更は 1 つの目的に絞る。
- レイヤー境界をまたぐ変更は、なぜ複数層に触れる必要があるかを PR 説明に明記する。
- generic な `Utils` や `Helper` に責務を寄せ集めない。

### バッテリー消費への配慮を最優先にする

- 60fps 前提で実装しない。
- `WALK` / `PLAY` は最大 15fps、`IDLE` は最大 2fps を超えない。
- 毎フレームの Bitmap 再読込や不要なレイアウト再計算を禁止する。

### immutable な状態を保つ

- `SceneState` 系の状態は Kotlin `data class` と `val` を基本とする。
- 状態更新は `copy()` で行い、共有 mutable state を増やさない。
- 入力イベントはその場で描画せず、次の `drawFrame()` で一貫して反映する。

## Kotlin / Android 実装規約

### 命名規則

#### package / directory

- package 名は lower-case を使う。
- package は責務単位で分ける。
  - `wallpaper`
  - `orchestration`
  - `logic`
  - `render`
  - `model`
- `utils`、`common`、`misc` は新設しない。

#### クラス・関数・変数

```kotlin
class CatBehaviorController
class BitmapRepository

data class SceneState(
    val surfaceWidth: Int,
    val wallpaperOffsetX: Float,
)

fun clampOffset(offset: Float): Float = offset.coerceIn(0f, 1f)
```

- クラス名は PascalCase
- 関数・変数名は lowerCamelCase
- Boolean は `is`、`has`、`should`、`can` で始める
- 定数は `UPPER_SNAKE_CASE`

#### リソース名

- drawable 名は lower_snake_case を使う
- 壁紙素材は役割が分かる接頭辞を付ける
  - `background_room_day.png`
  - `cat_walk_1.png`
  - `toy_yarn.png`
- XML 名も lower_snake_case とする
  - `wallpaper.xml`
  - `preferences.xml`

### ファイル設計

- 1 ファイル 1 主責務を基本とする。
- 300 行を超え始めたら分割を検討する。
- 1 つの public クラスにつき 1 ファイルを原則とする。
- 補助関数を増やす前に、既存クラスの責務が肥大化していないか確認する。

### 関数設計

- 関数名は結果ではなく動作を表す。
- 引数が 4 つを超える場合は value object 化または data class 化を検討する。
- 時刻、乱数、スケジューラは差し替え可能にし、テストしやすくする。

```kotlin
data class TapReactionRequest(
    val x: Float,
    val y: Float,
    val timestampMs: Long,
)

fun onTap(request: TapReactionRequest): ToyState {
    // ...
}
```

### コメント規約

- コメントは「何をしているか」ではなく「なぜそうするか」を書く。
- public API、複雑なフォールバック、非自明な制約には簡潔な説明を付ける。
- obvious な代入や if 文に説明コメントは付けない。

```kotlin
// 壁紙オフセット通知は次フレームで反映し、入力と描画の順序を固定する。
pendingOffsetX = normalizedOffsetX
```

## レイヤー別ルール

### wallpaper

- Android ライフサイクルの受け口に徹する。
- `WallpaperService.Engine` から直接猫の遷移ルールを書かない。
- `onVisibilityChanged(false)` では即座に次フレーム予約を止める。

### orchestration

- `drawFrame()` の順序を一元管理する。
- `TouchReactionController.update()` → `CatBehaviorController.update()` → `SceneRenderer.render()` の順序を崩さない。
- 表示状態とスケジューラの開始・停止をここで集中的に扱う。

### logic

- Android UI API や `Canvas` を直接参照しない。
- 入力値はクランプや正規化で安全値に丸める。
- 状態遷移時間、移動範囲、テーマ判定はここに置く。

### render

- 描画に必要な計算とアセット読み込みだけを担当する。
- 猫の行動確率や優先順位ロジックを持ち込まない。
- `BitmapFactory.decodeResource()` は初期化時または再読込時のみ許可する。

### model

- `data class` と enum を中心に構成する。
- 破壊的変更を前提にした setter を持たない。
- Android ライフサイクル依存を避ける。

## 状態管理とスレッド運用

### 状態更新ルール

- `SceneState` はオーケストレーション層の唯一の実行時状態とする。
- `wallpaperOffsetX` は常に `0.0f..1.0f` へクランプする。
- 猫の `positionX` は後景移動範囲に収める。
- タップは `ToyState` へ変換し、次フレームで消費する。

### スレッドルール

- MVP のフレーム更新は 1 本のハンドラ経路に集約する。
- 描画中に `Bitmap` を mutable 変更しない。
- `SurfaceHolder` が無効な場合は安全にスキップし、クラッシュさせない。

## エラーハンドリングとロギング

### 基本方針

- 予期される入力異常は丸める、無視する、フォールバックするのいずれかで吸収する。
- 予期しないエラーはクラッシュを避けつつ原因追跡可能な形で記録する。
- リリースビルドは継続動作を優先する。

### 必須ルール

- `drawFrame()` 全体で未処理例外を外へ漏らさない。
- asset 読込失敗時は昼背景のみなどの縮退動作に切り替える。
- `OutOfMemoryError` を想定し、任意アセットから先に解放する。
- タップ座標の絶対値を詳細ログへ残し続けない。

### ログレベル

| レベル | 用途 |
|--------|------|
| `DEBUG` | 状態遷移、フレーム間隔、レイアウト時間の検証 |
| `INFO` | 起動、壁紙適用、主要なライフサイクル切り替え |
| `WARN` | フォールバック発生、軽微な入力補正 |
| `ERROR` | 描画不能、アセット読込失敗、復旧不能な異常 |

## リソースとアセット管理

- PNG 素材は `drawable-nodpi/` に置く。
- 夜背景は P1 まで任意アセットとして扱う。
- デコード済み Bitmap 総量は MVP で 24MB 以内を目標にする。
- `onSurfaceDestroyed()` または `onDestroy()` で `BitmapRepository.clear()` を呼び、参照と Bitmap を解放する。
- 毎フレームの decode、scale、layout 再計算を禁止する。

## テスト戦略

### テストの優先順位

1. ユニットテスト
2. 統合テスト
3. E2E テスト

### 対象と目標

| 種別 | 対象 | 目標 |
|------|------|------|
| ユニットテスト | `logic/`, `orchestration/`, `model/` | ドメインロジック層の重要ロジックで 80% 以上 |
| 統合テスト | `wallpaper/` と `render/` の連携 | プレビュー、再レイアウト、タップ反応を網羅 |
| E2E テスト | 壁紙選択から適用までの主要フロー | MVP の主要シナリオ 100% |

### テスト記述ルール

- Given / When / Then を意識して書く。
- テスト名は「条件」と「期待結果」が分かる形にする。
- 時刻、乱数、スケジューラはテストダブルを注入可能にする。

```kotlin
@Test
fun update_whenTapReactionIsPending_prioritizesPlayMode() {
    // Given
    // When
    // Then
}
```

### 標準検証コマンド

Android プロジェクト初期化後は、次を標準の検証コマンドとする。

```bash
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
./gradlew lint
./gradlew assembleDebug
```

docs 先行フェーズでは、作成・更新した Markdown の診断確認を最低限の品質ゲートとする。

## 開発環境の運用 (VSCode / Android Studio)

実装は VSCode、ローカルでの動作確認は Android Studio という2ツール構成を前提とする。両ツールでビルド結果や設定が食い違わないよう、次のルールを明示する。

### ツールの役割分担

- **VSCode**: コード編集、および `./gradlew testDebugUnitTest` / `lint` / `assembleDebug` などコマンドラインで完結する検証
- **Android Studio**: エミュレータ・実機を使った動作確認、`connectedDebugAndroidTest` の実行、ライブ壁紙としての見た目・タップ反応の確認

`connectedDebugAndroidTest` やライブ壁紙のプレビューはエミュレータまたは実機が前提であり、VSCode 単体では実行できない。「コード変更とユニットテスト/lint は VSCode、実機確認と計装テストは Android Studio」という役割分担を崩さない。

### JDK / SDK バージョンの固定

- JDK バージョンは各 IDE の内蔵 JDK 任せにせず、`build.gradle.kts` の Gradle Toolchain で明示的に指定する。
- これにより、VSCode のターミナルから実行する `./gradlew` と Android Studio が実行するビルドが同一の JDK 上で動作し、ビルド結果の差異を防ぐ。
- `compileSdk` / `targetSdk` / `minSdk` の基準は前述の「依存関係とバージョン更新」に従う。

### ローカル専用ファイルの扱い

- `local.properties` は Android Studio が自動生成する SDK パスを含むためコミットしない。`.gitignore` に追加し、各環境・各開発者がそれぞれ保持する。
- `.idea/`(Android Studio)と `.vscode/`(VSCode)の設定ディレクトリは個人の作業環境設定であり、原則コミットしない。
- チームで揃えたい設定(推奨拡張機能など)がある場合は、`.vscode/extensions.json` のように共有価値のある最小限のファイルのみ例外的にコミットを検討する。

詳細なファイル配置は [repository-structure.md](./repository-structure.md) の「ローカル専用ファイルと IDE 設定」を参照する。

## Git / PR 運用

### ブランチ戦略

- `main`: 常にレビュー済みで共有可能な状態を保つ
- `feature/<topic>`: 機能追加
- `fix/<topic>`: 不具合修正
- `docs/<topic>`: ドキュメント更新
- `release/<version>`: Play 配布前の調整が必要になった場合のみ作成

### コミットメッセージ

Conventional Commits を使う。

```text
feat(wallpaper): add frame ticker based draw loop
fix(render): clamp wallpaper offset before layout calculation
docs(glossary): define CatMode and SceneTheme
```

### Pull Request の必須項目

- 何を変えたか
- なぜ変えたか
- どの設計書に影響するか
- 実施した検証
- レビュアーに特に見てほしい点

### マージ条件

- 影響範囲に応じた設計書更新が完了している
- 関連テストまたは診断確認が完了している
- 指摘コメントへの対応方針が整理されている

## コードレビュー観点

- レイヤー違反がないか
- 状態更新が immutable 前提を壊していないか
- 15fps / 2fps の制約を破る実装になっていないか
- Bitmap の読込・解放タイミングが適切か
- 仕様変更時にドキュメント更新が追従しているか

## 依存関係とバージョン更新

- `compileSdk` / `targetSdk` は 34、`minSdk` は 26 を基準とする。
- Kotlin は 2.0.x、AGP は 8.7.x、Gradle Wrapper は 8.10.x のパッチ更新のみを許可する。
- 依存更新は単独 PR に分け、更新理由と影響範囲を記録する。
- 破壊的変更を含むマイナー / メジャー更新は、先に `architecture.md` の更新を行う。

## ドキュメントと steering の運用

### 更新順序

仕様変更時は、必要な範囲に応じて次の順で更新する。

1. `docs/product-requirements.md`
2. `docs/functional-design.md`
3. `docs/architecture.md`
4. `docs/repository-structure.md`
5. `docs/development-guidelines.md`
6. `docs/glossary.md`

ディレクトリ配置と責務分割の詳細は [repository-structure.md](./repository-structure.md) を参照する。

### steering 運用

- 作業ごとに `.steering/YYYYMMDD-task-name/` を作る。
- `requirements.md`、`design.md`、`tasklist.md` を揃える。
- `tasklist.md` の完了状態を放置したまま作業を終えない。

## Definition of Done

次を満たした時点で、その変更を完了とみなす。

- 仕様変更が必要な場合、関連する設計書が更新されている。
- 実装変更がある場合、関連するテストまたは診断が通っている。
- レイヤー違反、状態管理違反、過剰な負荷増加がない。
- PR 説明または作業記録から、変更理由と検証内容が追える。
