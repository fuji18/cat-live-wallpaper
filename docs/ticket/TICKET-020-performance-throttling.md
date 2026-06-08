# TICKET-020: 描画頻度 throttling の実装

- 優先度: `P0`
- ステータス: `todo`
- 依存チケット: `TICKET-014`, `TICKET-016`, `TICKET-017`, `TICKET-019`

## 目的

状態と表示状態に応じて描画頻度を制御し、最大 15fps / 2fps を守る。

## スコープ

- 非表示時の描画停止を徹底する
- `WALK` / `PLAY` と `IDLE` の間隔切替をつなぐ

## 完了条件

- [ ] 非表示時に描画ループが止まる
- [ ] 最大 15fps / 2fps を超えない

## 参照ドキュメント

- [product-requirements.md](../product-requirements.md)
- [architecture.md](../architecture.md)