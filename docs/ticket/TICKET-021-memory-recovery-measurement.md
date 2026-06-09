# TICKET-021: メモリ復旧と性能計測ログ実装

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: `TICKET-008`, `TICKET-016`, `TICKET-017`, `TICKET-019`, `TICKET-020`

## 目的

Bitmap 解放、縮退動作、性能計測ログを入れて MVP の復旧性を高める。

## スコープ

- decode 失敗や surface 無効時の復旧を実装する
- メモリ解放の接続を実装する
- 起動時間 / 再レイアウト / タップ反応の計測ログを入れる

## 完了条件

- [x] 主要な異常で即クラッシュしない（onSurfaceChangedでloadAll失敗→early return、drawFrame全体をtry/catch→ログのみで次フレーム予約継続）
- [x] 性能計測ログを取得できる（engine_created_ms、relayout_ms、tap_reaction_ms をLog.dで計測）

## 参照ドキュメント

- [architecture.md](../architecture.md)
- [development-guidelines.md](../development-guidelines.md)