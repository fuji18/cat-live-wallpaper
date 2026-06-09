# TICKET-012: 移動範囲制御と昼テーマ解決の実装

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: `TICKET-005`, `TICKET-011`

## 目的

猫の移動方向と後景移動範囲制御、および MVP の昼テーマ解決を実装する。

## スコープ

- 左右移動と向き更新を実装する
- 後景移動範囲のクランプを実装する
- `SceneTheme.DAY` 固定の resolver を実装する

## 完了条件

- [x] 猫位置が後景移動範囲を超えない（positionX を 0.2f〜0.8f * surfaceWidth にクランプ、境界で方向反転）
- [x] MVP で `SceneTheme.DAY` を返せる（SceneThemeResolver.resolveTheme() = SceneTheme.DAY）

## 参照ドキュメント

- [functional-design.md](../functional-design.md)
- [glossary.md](../glossary.md)