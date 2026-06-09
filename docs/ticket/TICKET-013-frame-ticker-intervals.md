# TICKET-013: `FrameTicker` 間隔制御の実装

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: `TICKET-004`, `TICKET-011`

## 目的

状態別の描画間隔を制御する `FrameTicker` を実装する。

## スコープ

- `WALK` / `PLAY` で 67ms を返す
- `IDLE` で 500ms を返す
- 次フレーム予約 API を作る

## 完了条件

- [x] 状態別に 67ms / 500ms を返せる（intervalFor: WALK/PLAY→67ms、IDLE→500ms）
- [x] 予約開始 / 停止 API がある（scheduleNext / cancel）

## 参照ドキュメント

- [functional-design.md](../functional-design.md)
- [architecture.md](../architecture.md)