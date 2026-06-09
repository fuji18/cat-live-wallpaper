# TICKET-005: 猫 / 毛糸玉 / テーマ状態モデル実装

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: `TICKET-002`

## 目的

猫、毛糸玉、テーマの状態を表すモデルと enum 群を実装する。

## スコープ

- `CatStateSnapshot`, `ToyState` を実装する
- `CatMode`, `FacingDirection`, `SceneTheme`, `ToySource` を実装する

## 完了条件

- [x] 猫状態と毛糸玉状態を表現できる（CatStateSnapshot / ToyState data class を実装）
- [x] enum が正式ドキュメントの語彙と一致する（CatMode: WALK/IDLE/PLAY、FacingDirection: LEFT/RIGHT、ToySource: USER_TAP/AUTO_PLAY）

## 参照ドキュメント

- [functional-design.md](../functional-design.md)
- [glossary.md](../glossary.md)