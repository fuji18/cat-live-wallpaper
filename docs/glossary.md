# プロジェクト用語集 (Glossary)

## 概要

このドキュメントは、Cat Live Wallpaper プロジェクトで使う用語を統一するための用語集である。

**更新日**: 2026-06-07

## ドメイン用語

### ライブ壁紙

**定義**: Android のホーム画面背景として継続的に描画される動的な壁紙。

**説明**: 本プロジェクトでは、手描き風の部屋背景と猫の簡易アニメーションで構成されるローカル完結型のライブ壁紙を指す。

**関連用語**: `WallpaperService`、プレビュー、壁紙オフセット

**使用例**:
- 「ライブ壁紙として適用できることを MVP の成立条件とする」
- 「ライブ壁紙表示中のみ描画更新する」

**英語表記**: Live Wallpaper

### 壁紙オフセット

**定義**: ホーム画面の左右スクロール量に応じて、背景の見える範囲を水平移動させるための値。

**説明**: `Engine.onOffsetsChanged()` で受け取る値を元にし、`wallpaperOffsetX` として `0.0f..1.0f` に正規化して扱う。

**関連用語**: `SceneState`、背景切り出し、`onOffsetsChanged()`

**使用例**:
- 「壁紙オフセットは次の `drawFrame()` で反映する」
- 「壁紙オフセットは 0.0f から 1.0f の範囲にクランプする」

**英語表記**: Wallpaper Offset

### タップ反応

**定義**: ユーザーのタップ入力に対して、猫や毛糸玉の表示で反応を返す振る舞い。

**説明**: MVP では新しい猫状態を増やさず、毛糸玉の一時表示と `PLAY` 優先遷移で反応を表現する。

**関連用語**: 毛糸玉、`ToyState`、`PLAY`

**使用例**:
- 「タップ反応は MVP に含める」
- 「タップ反応の初回可視化は 500ms 以内を目標とする」

**英語表記**: Tap Reaction

### 後景移動範囲

**定義**: 猫が前景へ出すぎないように制限する、背景奥側の移動可能範囲。

**説明**: 機能設計では `surfaceWidth * 0.2f` から `surfaceWidth * 0.8f` として定義する。

**関連用語**: `CatStateSnapshot`、`positionX`、奥行き表現

**使用例**:
- 「猫の `positionX` は後景移動範囲に収める」
- 「後景移動範囲は行動制御側で保証する」

**英語表記**: Background Movement Range

### 夜背景

**定義**: 昼背景とは別に、夜の雰囲気を表現する背景差分。

**説明**: P1 以降の追加要素であり、MVP では受け皿のみを持つ。未配置時は昼背景へフォールバックする。

**関連用語**: `SceneTheme`、`DAY`、`NIGHT`、フォールバック

**使用例**:
- 「夜背景は MVP の成立条件ではない」
- 「夜背景未配置時は昼背景へフォールバックする」

**英語表記**: Night Background

## 技術用語

### WallpaperService

**定義**: Android でライブ壁紙を実装するための標準 API。

**公式サイト**: https://developer.android.com/reference/android/service/wallpaper/WallpaperService

**本プロジェクトでの用途**: ライブ壁紙サービス本体と `Engine` の生成起点として使う。

**バージョン**: Android API 26+

**関連ドキュメント**: [技術仕様書](./architecture.md#テクノロジースタック)

### Engine

**定義**: `WallpaperService` に紐づく実行単位で、表示状態・サーフェス・入力を扱うオブジェクト。

**本プロジェクトでの用途**: `CatWallpaperEngine` が `drawFrame()` の起点となり、オフセット更新やタップ入力を受け取る。

**関連用語**: `CatWallpaperEngine`、`onSurfaceChanged()`、`onTouchEvent()`

### Canvas / SurfaceHolder

**定義**: Android の 2D 描画先と、そのサーフェスを管理する API。

**本プロジェクトでの用途**: 背景、猫、毛糸玉を順に描画する MVP の主描画手段として使う。

**バージョン**: Android API 26+

**関連ドキュメント**: [機能設計書](./functional-design.md#技術スタック)

### BitmapRepository

**定義**: PNG 素材の読み込み、保持、解放を担当するコンポーネント。

**本プロジェクトでの用途**: 必須アセットを初期化時に読み込み、毎フレームの再読込を防ぐ。

**関連用語**: `AssetSet`、Bitmap、メモリ管理戦略

### FrameTicker

**定義**: 次フレームの描画予約を担当するスケジューラ。

**本プロジェクトでの用途**: `WALK` / `PLAY` では最大 15fps、`IDLE` では最大 2fps の更新間隔を選択する。

**関連用語**: `drawFrame()`、`Handler`、`Looper`

### DataStore

**定義**: Android のキー値永続化手段。

**本プロジェクトでの用途**: MVP では未導入。P1 以降の設定画面追加時にのみ導入候補となる。

**関連用語**: 設定画面、永続化、P1

## 略語・頭字語

### MVP

**正式名称**: Minimum Viable Product

**意味**: 最小成立機能で価値検証を行う初期リリース範囲。

**本プロジェクトでの使用**: 1 匹の猫、1 部屋、3 状態、タップ反応、低負荷更新を含む最小構成を指す。

### KPI

**正式名称**: Key Performance Indicator

**意味**: 成功を測るための定量指標。

**本プロジェクトでの使用**: 壁紙適用完了率、7 日後継続利用率、タップ反応体験率などを指す。

### P0/P1/P2

**正式名称**: Priority Level

**意味**: 機能の優先度レベル。

**定義**:
- `P0` (必須): MVP の成立に必須の機能
- `P1` (重要): MVP 後の早期リリース対象
- `P2` (できれば): 長期的な拡張候補

**本プロジェクトでの使用**: PRD の機能優先度と、夜背景や設定画面などの拡張順序を表す。

**使用例**:
- 「タップ反応は P0 に含める」
- 「夜背景は P1 で追加する」

### ANR

**正式名称**: Application Not Responding

**意味**: Android でアプリの応答停止と判定される状態。

**本プロジェクトでの使用**: Android Vitals による本番監視の対象。

### APK

**正式名称**: Android Package

**意味**: Android アプリを端末へ配布・インストールする成果物形式。

**本プロジェクトでの使用**: Debug ビルドやローカル検証用成果物として扱う。

### AAB

**正式名称**: Android App Bundle

**意味**: Google Play 配布向けの成果物形式。

**本プロジェクトでの使用**: Internal Testing と本番配布の標準成果物として扱う。

### CI/CD

**正式名称**: Continuous Integration / Continuous Delivery

**意味**: テスト、ビルド、配布準備を継続的に自動化する仕組み。

**本プロジェクトでの使用**: lint、テスト、ビルド、段階的ロールアウトの準備フローを指す。

## アーキテクチャ用語

### Android 統合レイヤー

**定義**: Android のライフサイクルと入力を受ける最上位レイヤー。

**本プロジェクトでの適用**: `CatWallpaperService` と `CatWallpaperEngine` が属する。

**関連コンポーネント**: `WallpaperService`、`Engine`

### オーケストレーションレイヤー

**定義**: 状態更新順序と描画呼び出しを調停するレイヤー。

**本プロジェクトでの適用**: `FrameTicker` と `drawFrame()` の更新順序制御が属する。

**関連コンポーネント**: `FrameTicker`、`SceneState`

### ドメインロジックレイヤー

**定義**: 猫の状態遷移やタップ反応など、振る舞いの判断を行うレイヤー。

**本プロジェクトでの適用**: `CatBehaviorController`、`TouchReactionController`、`SceneThemeResolver` が属する。

**関連コンポーネント**: `CatMode`、`ToyState`

### 描画・アセットレイヤー

**定義**: Bitmap 読み込み、背景切り出し、Canvas 描画を行うレイヤー。

**本プロジェクトでの適用**: `BitmapRepository`、`SceneRenderer`、`AssetSet` が属する。

**関連コンポーネント**: `Canvas`、`SurfaceHolder`、Bitmap

### immutable state

**定義**: 既存オブジェクトを破壊的に変更せず、新しい状態を再生成して扱う方針。

**本プロジェクトでの適用**: `SceneState`、`CatStateSnapshot`、`ToyState` は `data class` と `copy()` を前提に更新する。

**関連コンポーネント**: `SceneState`、`copy()`、状態整合性

### フォールバック

**定義**: 必須ではない機能や素材が利用できない場合に、より単純な安全動作へ縮退すること。

**本プロジェクトでの適用**: 夜背景未配置時に昼背景を使う、Bitmap 読込失敗時に最小スプライトセットで継続する、などを指す。

**関連コンポーネント**: 夜背景、`BitmapRepository`、エラーハンドリング戦略

## ステータス・状態

### CatMode

| ステータス | 意味 | 遷移条件 | 次の状態 |
|-----------|------|---------|---------|
| `WALK` | 猫が横移動しながら歩く状態 | `IDLE` 終了、`PLAY` 終了後の通常遷移 | `IDLE`, `PLAY` |
| `IDLE` | 猫が座って停止している状態 | `WALK` 終了、初期状態 | `WALK`, `PLAY` |
| `PLAY` | 猫が毛糸玉に反応して遊ぶ状態 | タップ反応、通常遷移の優先分岐 | `IDLE`, `WALK` |

**状態遷移の要点**:
- タップ直後は通常遷移より `PLAY` を優先する。
- 短時間で過剰に切り替わらないよう最低滞在時間を持たせる。

### SceneTheme

| ステータス | 意味 | MVP での扱い | P1 以降 |
|-----------|------|-------------|---------|
| `DAY` | 昼背景を使うテーマ | 標準で使用 | 継続使用 |
| `NIGHT` | 夜背景を使うテーマ | 受け皿のみ | 夜背景追加後に使用 |

## データモデル用語

### SceneState

**定義**: 1 フレーム分の描画と状態更新に必要な全体状態。

**主要フィールド**:
- `surfaceWidth`: サーフェス幅
- `surfaceHeight`: サーフェス高
- `wallpaperOffsetX`: 正規化済みの壁紙オフセット
- `isVisible`: 壁紙が可視状態か
- `theme`: 現在のテーマ
- `cat`: 猫状態
- `toy`: 毛糸玉状態

**関連エンティティ**: `CatStateSnapshot`、`ToyState`

### CatStateSnapshot

**定義**: 猫の現在モード、向き、位置、速度、アニメーションフレームを表す状態。

**主要フィールド**:
- `mode`
- `facing`
- `positionX`
- `positionY`
- `velocityX`
- `frameIndex`

**制約**: `positionX` は後景移動範囲に収める。

### ToyState

**定義**: 毛糸玉の表示位置、表示期限、発生源を表す状態。

**主要フィールド**:
- `isVisible`
- `anchorX`
- `anchorY`
- `visibleUntilMs`
- `source`

**関連用語**: タップ反応、`PLAY`

### AssetSet

**定義**: 背景と猫スプライト、毛糸玉をまとめた描画用アセット集合。

**主要フィールド**:
- `backgroundDay`
- `backgroundNight`
- `catWalkFrames`
- `catIdleFrame`
- `catPlayFrames`
- `toyYarn`

**制約**: `backgroundNight` は nullable とし、未配置時は昼背景へフォールバックする。

## エラー・例外

### Surface 無効状態

**クラス名**: Android 標準の `SurfaceHolder` 無効状態

**発生条件**: 壁紙が非表示になった、またはサーフェス破棄直後で描画面が利用できない。

**対処方法**: 描画をスキップし、次回の有効状態まで待つ。

### Bitmap 読込失敗

**発生条件**: PNG 素材が見つからない、または decode に失敗した。

**対処方法**: 任意素材を読み込まない、または最小構成へ縮退して継続する。

### OutOfMemoryError

**発生条件**: デコード済み Bitmap 総量がメモリ予算を超えた。

**対処方法**: 夜背景など任意アセットを解放し、必要に応じて低解像度で再読込する。
