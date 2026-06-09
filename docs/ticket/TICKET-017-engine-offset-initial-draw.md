# TICKET-017: Engine の offset 反映と初回描画実装

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: `TICKET-010`, `TICKET-016`

## 目的

壁紙オフセットを描画へ反映し、初回描画と再描画の起動経路を実装する。

## スコープ

- `onOffsetsChanged()` を実装する
- 初回描画トリガを接続する
- offset を `SceneState` に反映する

## 完了条件

- [x] offset 変更が背景描画へ反映される（onOffsetsChanged → coordinator.updateState → wallpaperOffsetX）
- [x] 初回描画経路が成立している（onSurfaceChanged → coordinator.start() → drawFrame()）

## 参照ドキュメント

- [product-requirements.md](../product-requirements.md)
- [functional-design.md](../functional-design.md)