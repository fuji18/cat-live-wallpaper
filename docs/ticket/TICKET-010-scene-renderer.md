# TICKET-010: `SceneRenderer` の実装

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: `TICKET-005`, `TICKET-006`, `TICKET-007`, `TICKET-009`

## 目的

背景、猫、毛糸玉を `SceneState` と `AssetSet` から描画できるようにする。

## スコープ

- 背景描画を実装する
- 猫スプライト描画を実装する
- 毛糸玉描画を実装する

## 完了条件

- [x] 背景と猫を描画できる（BackgroundLayout + SceneThemeResolver + Canvas.drawBitmap）
- [x] 毛糸玉表示を描画できる（toy.isVisible 時のみ描画）
- [x] 描画レイヤーが状態遷移ロジックへ依存していない（SceneState / AssetSet を受け取るのみ）

## 参照ドキュメント

- [functional-design.md](../functional-design.md)
- [product-requirements.md](../product-requirements.md)