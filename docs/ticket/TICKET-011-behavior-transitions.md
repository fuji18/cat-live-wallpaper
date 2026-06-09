# TICKET-011: 猫の通常遷移ロジック実装

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: `TICKET-004`, `TICKET-005`

## 目的

`WALK` / `IDLE` / `PLAY` の通常遷移と最低滞在時間を実装する。

## スコープ

- 最低滞在時間を持つ遷移を実装する
- 過剰な状態切替を抑える

## 完了条件

- [x] 通常遷移が成立する（WALK→IDLE→WALK/PLAY→IDLE の遷移ロジックを実装）
- [x] 最低滞在時間を守る（stateDurationMs でガード、期限前は advanceFrame のみ）

## 参照ドキュメント

- [product-requirements.md](../product-requirements.md)
- [functional-design.md](../functional-design.md)