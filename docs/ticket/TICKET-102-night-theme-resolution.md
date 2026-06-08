# TICKET-102: 夜テーマ解決と描画切替実装

- 優先度: `P1`
- ステータス: `backlog`
- 依存チケット: `TICKET-010`, `TICKET-012`, `TICKET-101`

## 目的

`SceneTheme.NIGHT` を実際に使い、昼夜で描画を切り替える。

## スコープ

- `SceneThemeResolver` の夜判定実装
- renderer の背景切替実装

## 完了条件

- [ ] `SceneTheme.NIGHT` が利用される
- [ ] 昼夜の切替で描画が破綻しない

## 参照ドキュメント

- [functional-design.md](../functional-design.md)
- [architecture.md](../architecture.md)