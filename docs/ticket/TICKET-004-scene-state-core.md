# TICKET-004: `SceneState` の基礎モデル実装

- 優先度: `P0`
- ステータス: `todo`
- 依存チケット: `TICKET-002`

## 目的

描画と更新の基礎となる `SceneState` と共通制約を実装する。

## スコープ

- `SceneState` を実装する
- `wallpaperOffsetX` 正規化の扱いを決める
- `surfaceWidth` / `surfaceHeight` の保持モデルを作る

## 完了条件

- [ ] `SceneState` が `data class` で実装されている
- [ ] `wallpaperOffsetX` を安全範囲に収める手段がある

## 参照ドキュメント

- [functional-design.md](../functional-design.md)
- [architecture.md](../architecture.md)