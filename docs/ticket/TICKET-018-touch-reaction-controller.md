# TICKET-018: `TouchReactionController` の実装

- 優先度: `P0`
- ステータス: `todo`
- 依存チケット: `TICKET-005`

## 目的

タップ位置から毛糸玉表示状態を生成 / 更新する controller を実装する。

## スコープ

- タップ受理 API を作る
- `ToyState` 更新を実装する
- 可視期限切れの更新を実装する

## 完了条件

- [ ] タップから `ToyState` を生成できる
- [ ] 可視期限切れで非表示へ戻せる

## 参照ドキュメント

- [functional-design.md](../functional-design.md)
- [glossary.md](../glossary.md)