# TICKET-013: `FrameTicker` 間隔制御の実装

- 優先度: `P0`
- ステータス: `todo`
- 依存チケット: `TICKET-004`, `TICKET-011`

## 目的

状態別の描画間隔を制御する `FrameTicker` を実装する。

## スコープ

- `WALK` / `PLAY` で 67ms を返す
- `IDLE` で 500ms を返す
- 次フレーム予約 API を作る

## 完了条件

- [ ] 状態別に 67ms / 500ms を返せる
- [ ] 予約開始 / 停止 API がある

## 参照ドキュメント

- [functional-design.md](../functional-design.md)
- [architecture.md](../architecture.md)