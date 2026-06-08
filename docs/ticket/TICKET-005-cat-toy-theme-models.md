# TICKET-005: 猫 / 毛糸玉 / テーマ状態モデル実装

- 優先度: `P0`
- ステータス: `todo`
- 依存チケット: `TICKET-002`

## 目的

猫、毛糸玉、テーマの状態を表すモデルと enum 群を実装する。

## スコープ

- `CatStateSnapshot`, `ToyState` を実装する
- `CatMode`, `FacingDirection`, `SceneTheme`, `ToySource` を実装する

## 完了条件

- [ ] 猫状態と毛糸玉状態を表現できる
- [ ] enum が正式ドキュメントの語彙と一致する

## 参照ドキュメント

- [functional-design.md](../functional-design.md)
- [glossary.md](../glossary.md)