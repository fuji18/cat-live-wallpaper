# TICKET-016: Engine の visibility / surface ライフサイクル実装

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: `TICKET-015`

## 目的

Engine の可視状態と surface 変更に応じて描画の開始 / 停止を制御する。

## スコープ

- `onVisibilityChanged()` を実装する
- `onSurfaceChanged()` を実装する
- surface 無効時の安全なスキップを入れる

## 完了条件

- [x] 可視状態に応じて描画開始 / 停止できる
- [x] surface 変更を安全に処理できる

## 参照ドキュメント

- [product-requirements.md](../product-requirements.md)
- [architecture.md](../architecture.md)